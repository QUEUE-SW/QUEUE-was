package com.queuewas.domains.queue.service;

import org.springframework.stereotype.Service;

import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.implement.QueueManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueueService {
	private final QueueManager queueManager;

	public QueueJoinRes join(QueueJoinReq queueJoinReq) {
		String token = queueJoinReq.token();
		long joinedAt = System.currentTimeMillis();
		int queueNumber = queueManager.getQueueNumber(token);

		QueueUser user = QueueUser.create(token, joinedAt, queueNumber);
		queueManager.enqueue(user);

		return QueueJoinRes.from(user);
	}
}
