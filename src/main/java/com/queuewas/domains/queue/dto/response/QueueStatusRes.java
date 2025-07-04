package com.queuewas.domains.queue.dto.response;

import com.queuewas.domains.queue.type.QueueStatus;

public record QueueStatusRes(
	long queueNumber,
	QueueStatus queueStatus
) {
	public static QueueStatusRes from(long queueNumber, QueueStatus queueStatus) {
		return new QueueStatusRes(queueNumber, queueStatus);
	}

}
