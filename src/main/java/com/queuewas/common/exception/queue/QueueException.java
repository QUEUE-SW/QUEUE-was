package com.queuewas.common.exception.queue;

import com.queuewas.common.exception.GlobalException;

public class QueueException extends GlobalException {

	public QueueException(QueueErrorCode queueErrorCode) {
		super(queueErrorCode);
	}
}
