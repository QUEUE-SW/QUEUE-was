package com.queuewas.domains.queue.service;

import org.springframework.stereotype.Service;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.dto.response.QueueStatusRes;
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

	public QueueStatusRes readStatus(String token) {
		QueueUser queueUser = queueManager.getQueueUser(token);
		if (queueUser == null) {
			throw new QueueException(QueueErrorCode.QUEUE_NOT_FOUND);
		}

		long queueNumber = queueManager.getQueueNumber(token);

		return QueueStatusRes.from(queueNumber, queueUser.getStatus());
	}
}
