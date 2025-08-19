package com.queuewas.domains.queue.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.dto.response.QueueStatusRes;
import com.queuewas.domains.queue.implement.RedisQueueManager;
import com.queuewas.domains.queue.type.QueueStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PollingQueueService {

	private final RedisQueueManager redisQueueManager;

	public QueueJoinRes join(QueueJoinReq queueJoinReq) {
		String token = queueJoinReq.token();

		if (!redisQueueManager.isAllowed(token)) {
			redisQueueManager.enqueue(token);
		}

		long queueNumber = redisQueueManager.getQueueNumber(token);
		return QueueJoinRes.from(queueNumber);
	}

	public QueueStatusRes readStatus(String token) {
		long queueNumber = redisQueueManager.getQueueNumber(token);
		return (queueNumber == 0L) ? QueueStatusRes.from(0L, QueueStatus.ALLOWED) :
			QueueStatusRes.from(queueNumber, QueueStatus.WAITING);
	}

	public void notifyEntranceToUsers(int count) {
		List<String> tokens = redisQueueManager.popTokens(count);
		if (tokens == null || tokens.isEmpty()) {
			return;
		}
		redisQueueManager.markAllowed(tokens);
	}

	public void notifyLogin(String token) {
		redisQueueManager.unmarkAllowed(token);
		redisQueueManager.remove(token);
	}
}
