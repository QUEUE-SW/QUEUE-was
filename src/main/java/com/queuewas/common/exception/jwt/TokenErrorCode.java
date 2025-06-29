package com.queuewas.common.exception.jwt;



import com.queuewas.common.exception.BaseErrorCode;
import com.queuewas.common.exception.ErrorCausedBy;
import com.queuewas.common.exception.ReasonCode;
import com.queuewas.common.exception.StatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements BaseErrorCode {

	TOKEN_NOT_FOUND(StatusCode.NOT_FOUND, ReasonCode.REQUESTED_RESOURCE_NOT_FOUND, "토큰을 찾을 수 없습니다."),
	TOKEN_IS_EXPIRED(StatusCode.UNAUTHORIZED, ReasonCode.EXPIRED_OR_REVOKED_AUTHENTICATION_TOKEN, "만료된 토큰입니다."),
	TOKEN_IS_MALFORMED(StatusCode.UNAUTHORIZED, ReasonCode.TAMPERED_TOKEN, "유효하지 않은 형식의 토큰입니다."),
	TOKEN_IS_TEMPERED(StatusCode.UNAUTHORIZED, ReasonCode.MALFORMED_TOKEN, "조작된 토큰입니다."),
	INVALID_TOKEN(StatusCode.UNAUTHORIZED, ReasonCode.INVALID_REQUEST, "사용할 수 없는 토큰입니다.");

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
