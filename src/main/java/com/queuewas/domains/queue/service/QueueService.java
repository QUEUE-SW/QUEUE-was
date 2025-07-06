package com.queuewas.domains.queue.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.dto.response.QueueStatusRes;
import com.queuewas.domains.queue.implement.BatchManager;
import com.queuewas.domains.queue.implement.QueueManager;
import com.queuewas.domains.queue.type.QueueStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueService {
	private final QueueManager queueManager;
	private final BatchManager batchManager;
	private final ScheduledExecutorService scheduler;


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

		scheduler.schedule(() -> {
			rollbackBatch(batchId);
			batchManager.completeBatchPartially(batchId);
		}, 10, TimeUnit.SECONDS);

		return future;
	}

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
}
