package com.queuewas.domains.queue.service;

import org.springframework.stereotype.Service;

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
		queueManager.enqueue(token);

		long queueNumber = queueManager.getQueueNumber(token);

		return QueueJoinRes.from(queueNumber);
	}
}
