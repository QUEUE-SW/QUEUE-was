package com.queuewas.common.exception;

import java.util.Objects;

public record ErrorCausedBy(StatusCode statusCode, ReasonCode reasonCode) {

	public ErrorCausedBy {
		Objects.requireNonNull(statusCode);
		Objects.requireNonNull(reasonCode);
	}

	public static ErrorCausedBy of(StatusCode statusCode, ReasonCode reasonCode) {
		return new ErrorCausedBy(statusCode, reasonCode);
	}

	public String getErrorCode() {
		return String.valueOf(statusCode.getCode() * 10 + reasonCode.getCode());
	}
}
