package com.queuewas.domains.queue.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.dto.response.QueueStatusRes;
import com.queuewas.domains.queue.implement.BatchManager;
import com.queuewas.domains.queue.implement.QueueManager;
import com.queuewas.domains.queue.implement.SseAsyncSender;
import com.queuewas.domains.queue.implement.SseEmitterManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseQueueService {

	private final SseEmitterManager emitterManager;
	private final QueueManager queueManager;
	private final BatchManager batchManager;
	private final ScheduledExecutorService scheduler;
	private final SseAsyncSender asyncSender;

	public SseEmitter subscribe(String token) {
		// 대기열 등록
		queueManager.enqueue(token);
		long queueNumber = queueManager.getQueueNumber(token);

		// SSEEmitter 생성 및 저장
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		emitterManager.addEmitter(token, emitter);

		// 초기 응답 전송 (WAITING + 순번)
		asyncSender.send(emitter, "waiting", Map.of(
			"status", "WAITING",
			"number", queueNumber
		));

		// 연결 종료 핸들링
		emitter.onCompletion(() -> emitterManager.removeEmitter(token));
		emitter.onTimeout(() -> emitterManager.removeEmitter(token));
		emitter.onError((e) -> emitterManager.removeEmitter(token));

		return emitter;
	}

	public QueueStatusRes readStatus(String token) {
		QueueUser user = queueManager.getQueueUser(token);
		if (user == null) {
			throw new QueueException(QueueErrorCode.QUEUE_NOT_FOUND);
		}

		long queueNumber = queueManager.getQueueNumber(token);
		return QueueStatusRes.from(queueNumber, user.getStatus());
	}

	public void notifyEntranceToUsers(int count) {
		List<QueueUser> allowedUsers = queueManager.popUsers(count);
		if (allowedUsers.isEmpty()) {
			return;
		}

		String batchId = batchManager.registerBatch(allowedUsers);

		// ALLOWED 사용자에게 전송 및 sse 종료
		for (QueueUser user : allowedUsers) {
			SseEmitter emitter = emitterManager.getEmitter(user.getToken());
			if (emitter != null) {
				asyncSender.send(emitter, "allowed", Map.of("status", "ALLOWED"));
				emitter.complete();
				emitterManager.removeEmitter(user.getToken());
			}
		}

		// 일정 시간 후 배치 완료 처리
		scheduler.schedule(() -> batchManager.completeBatchPartially(batchId), 10, TimeUnit.SECONDS);

		// 대기 사용자에게 순번 변경 알림
		List<QueueUser> waitingUsers = queueManager.getAllWaitingUsers();
		for (QueueUser user : waitingUsers) {
			long queueNumber = queueManager.getQueueNumber(user.getToken());
			SseEmitter emitter = emitterManager.getEmitter(user.getToken());
			if (emitter != null) {
				asyncSender.send(emitter, "waiting", Map.of(
					"status", "WAITING",
					"number", queueNumber
				));
			}
		}
	}

	public void notifyLogin(String token) {
		QueueUser user = queueManager.getQueueUser(token);
		batchManager.notifyUserLogin(token, user);
		queueManager.remove(token);
		emitterManager.removeEmitter(token);
	}

	public void removeQueueInfo(String token) {
		queueManager.remove(token);
		emitterManager.removeEmitter(token);
	}
}
