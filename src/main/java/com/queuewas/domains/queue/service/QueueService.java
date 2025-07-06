package com.queuewas.domains.queue.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.common.response.SuccessResponse;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.dto.response.QueueStatusRes;
import com.queuewas.domains.queue.implement.BatchManager;
import com.queuewas.domains.queue.implement.QueueManager;
import com.queuewas.domains.queue.type.QueueStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueueService {
	private final QueueManager queueManager;
	private final BatchManager batchManager;
	private final ScheduledExecutorService scheduler;
	private final RestTemplate restTemplate;


	public QueueJoinRes join(QueueJoinReq queueJoinReq) {
		String token = queueJoinReq.token();
		queueManager.enqueue(token);

		long queueNumber = queueManager.getQueueNumber(token);

		return QueueJoinRes.from(queueNumber);
	}

	public QueueStatusRes readStatus(String token) {
		QueueUser queueUser = queueManager.getQueueUser(token);
		if (queueUser == null) {
			throw new QueueException(QueueErrorCode.QUEUE_NOT_FOUND);
		}

		long queueNumber = queueManager.getQueueNumber(token);

		return QueueStatusRes.from(queueNumber, queueUser.getStatus());
	}

	public CompletableFuture<Void> allowQueueStatusWithAck(int count) {
		List<QueueUser> users = queueManager.popUsers(count);
		if (users.isEmpty()) {
			return CompletableFuture.completedFuture(null);
		}

		String batchId = batchManager.registerBatch(users);
		CompletableFuture<Void> future = batchManager.getFuture(batchId);

		final int batchSize = 10;
		int totalBatches = (int) Math.ceil((double) users.size() / batchSize);

		for (int i = 0; i < totalBatches; i++) {
			int fromIndex = i * batchSize;
			int toIndex = Math.min(fromIndex + batchSize, users.size());
			List<QueueUser> subList = users.subList(fromIndex, toIndex);

			long delay = i; // 초 단위 간격
			scheduler.schedule(() -> {
				for (QueueUser user : subList) {
					user.updateStatus(QueueStatus.ALLOWED);
				}
			}, delay, TimeUnit.SECONDS);
		}

		// 10초 후 남은 유저들 롤백
		scheduler.schedule(() -> {
			rollbackBatch(batchId);
			batchManager.completeBatchPartially(batchId);
		}, 10, TimeUnit.SECONDS);

		return future.thenApply(v -> {
			int loggedInCount = batchManager.getLoggedInCount(batchId);
			notifyLoginSuccessToSessionServer(loggedInCount);
			return null;
		});
	}

	// public void handleQueueWithAck(int count, DeferredResult<ResponseEntity<?>> result) {
	// 	List<QueueUser> users = queueManager.popUsers(count);
	// 	if (users.isEmpty()) {
	// 		result.setResult(ResponseEntity.ok(SuccessResponse.noContent()));
	// 		return;
	// 	}
	//
	// 	String batchId = batchManager.registerBatch(users);
	// 	CompletableFuture<Void> future = batchManager.getFuture(batchId);
	//
	// 	// ✅ 1초 단위로 나눠서 ALLOWED 처리 (ex. 10명씩)
	// 	final int batchSize = 10;
	// 	int totalBatches = (int) Math.ceil((double) users.size() / batchSize);
	//
	// 	for (int i = 0; i < totalBatches; i++) {
	// 		int from = i * batchSize;
	// 		int to = Math.min(from + batchSize, users.size());
	// 		List<QueueUser> batchUsers = users.subList(from, to);
	//
	// 		long delay = i; // 초 단위
	// 		scheduler.schedule(() -> {
	// 			for (QueueUser user : batchUsers) {
	// 				user.updateStatus(QueueStatus.ALLOWED);
	// 			}
	// 		}, delay, TimeUnit.SECONDS);
	// 	}
	//
	// 	// ✅ 정상 로그인 전부 완료되었을 경우
	// 	future.thenAccept((v) -> {
	// 		result.setResult(ResponseEntity.ok(SuccessResponse.noContent()));
	// 	});
	//
	// 	// ✅ Timeout: 롤백 및 완료 처리
	// 	result.onTimeout(() -> {
	// 		rollbackBatch(batchId);
	// 		batchManager.completeBatchPartially(batchId);
	// 		result.setResult(ResponseEntity.ok(SuccessResponse.noContent()));
	// 	});
	//
	// 	// ✅ 예외 발생 시
	// 	result.onError((e) -> {
	// 		rollbackBatch(batchId);
	// 		batchManager.completeBatchPartially(batchId);
	// 		result.setErrorResult(ResponseEntity.internalServerError().body("오류 발생"));
	// 	});
	// }

	public void notifyLogin(String token) {
		QueueUser user = queueManager.getQueueUser(token);
		batchManager.notifyUserLogin(token, user);
		queueManager.remove(token);
	}

	public void removeQueueInfo(String token) {
		queueManager.remove(token);
	}

	public void rollbackBatch(String batchId) {
		Set<String> tokens = batchManager.getTokens(batchId);

		if (tokens == null || tokens.isEmpty()) return;

		for (String token : tokens) {
			QueueUser user = queueManager.getQueueUser(token);
			if (user != null && user.getStatus() == QueueStatus.ALLOWED) {
				user.updateStatus(QueueStatus.WAITING);
				queueManager.requeue(user);
			}
		}

		batchManager.removeBatch(batchId);
	}

	public void reset() {
		queueManager.reset();
	}

	public void notifyLoginSuccessToSessionServer(int successCount) {
		String url = "http://allclear-was-dev:8080/api/v1/session/notify-login-success?count=" + successCount;

		try {
			restTemplate.postForEntity(url, null, Void.class);
			log.info("✅ 수강신청 서버에 로그인 성공 {}명 통보 완료", successCount);
		} catch (Exception e) {
			log.warn("🚨 수강신청 서버 통보 실패", e);
		}
	}
}
