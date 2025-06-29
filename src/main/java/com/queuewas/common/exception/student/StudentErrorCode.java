package com.queuewas.common.exception.student;


import com.queuewas.common.exception.BaseErrorCode;
import com.queuewas.common.exception.ErrorCausedBy;
import com.queuewas.common.exception.ReasonCode;
import com.queuewas.common.exception.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentErrorCode implements BaseErrorCode {

	STUDENT_NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "해당 학생이 존재하지 않습니다."),
	STUDENT_POLICY_NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "해당 학생 정책이 존재하지 않습니다."),
	STUDENT_IS_DUPLICATED(StatusCode.CONFLICT, ReasonCode.RESOURCE_ALREADY_EXISTS, "해당 학번의 학생이 이미 존재합니다.");

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
