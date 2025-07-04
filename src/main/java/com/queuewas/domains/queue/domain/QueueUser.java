package com.queuewas.domains.queue.domain;

import com.queuewas.domains.queue.type.QueueStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueUser {

	private String token;
	private long joinedAt;
	private long joinQueueNumber;
	private QueueStatus status;

	public void updateStatus(QueueStatus status) {
		this.status = status;
	}

	public static QueueUser create(String token, long joinedAt, long joinQueueNumber) {
		return new QueueUser(token, joinedAt, joinQueueNumber, QueueStatus.WAITING);
	}

}
