package com.queuewas.common.exception.queue;

import com.queuewas.common.exception.BaseErrorCode;
import com.queuewas.common.exception.ErrorCausedBy;
import com.queuewas.common.exception.ReasonCode;
import com.queuewas.common.exception.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QueueErrorCode implements BaseErrorCode {

	FAILED_JOIN_QUEUE(StatusCode.BAD_REQUEST, ReasonCode.INVALID_REQUEST, "대기열 진입에 실패했습니다."),
	QUEUE_NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "해당하는 대기열 정보가 없습니다.");

	private final StatusCode statusCode;
	private final ReasonCode reasonCode;
	private final String message;

	@Override
	public ErrorCausedBy errorCausedBy() {
		return ErrorCausedBy.of(statusCode, reasonCode);
	}

	@Override
	public String getErrorMessage() {
		return message;
	}
}
