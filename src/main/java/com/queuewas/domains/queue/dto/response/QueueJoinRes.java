package com.queuewas.domains.queue.dto.response;

public record QueueJoinRes(
	long queueNumber
) {
	public static QueueJoinRes from(long queueNumber) {
		return new QueueJoinRes(queueNumber);
	}
}
