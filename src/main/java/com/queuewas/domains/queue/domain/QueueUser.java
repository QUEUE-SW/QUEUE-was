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
	private String batchId;

	public void updateStatus(QueueStatus status) {
		this.status = status;
	}

	public void updateBatchId(String batchId) {
		this.batchId = batchId;
	}

	public static QueueUser create(String token, long joinedAt, long joinQueueNumber) {
		return new QueueUser(token, joinedAt, joinQueueNumber, QueueStatus.WAITING, "0");
	}

}
