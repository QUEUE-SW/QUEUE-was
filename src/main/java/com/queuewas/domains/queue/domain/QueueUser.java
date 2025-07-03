package com.queuewas.domains.queue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QueueUser {

	private String token;
	private long joinedAt;
	private int joinQueueNumber;

	public static QueueUser create(String token, long joinedAt, int joinQueueNumber) {
		return new QueueUser(token, joinedAt, joinQueueNumber);
	}

}
