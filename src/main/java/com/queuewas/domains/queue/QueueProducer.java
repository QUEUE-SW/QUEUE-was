package com.queuewas.domains.queue;

import org.springframework.stereotype.Component;

import com.queuewas.domains.queue.domain.QueueUser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QueueProducer {

	private final QueueManager queueManager;

	public QueueUser submitUser(String token) {
		long joinedAt = System.currentTimeMillis();
		int queueNumber = queueManager.getQueueNumber(token);

		QueueUser user = new QueueUser(token, joinedAt, queueNumber);
		queueManager.enqueue(user);

		return user;
	}
}
