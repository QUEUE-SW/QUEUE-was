package com.queuewas.domains.queue.dto.response;

import com.queuewas.domains.queue.domain.QueueUser;

public record QueueJoinRes(
	int queueNumber
) {
	public static QueueJoinRes from(QueueUser queueUser) {
		return new QueueJoinRes(queueUser.getJoinQueueNumber());
	}
}
