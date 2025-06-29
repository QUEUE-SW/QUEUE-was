package com.queuewas.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {
	SUCCESS(200),
	BAD_REQUEST(400),
	UNAUTHORIZED(401),
	FORBIDDEN(403),
	NOT_FOUND(404),
	METHOD_NOT_ALLOWED(405),
	NOT_ACCEPTABLE(406),
	REQUEST_TIMEOUT(408),
	CONFLICT(409),
	UNPROCESSABLE_CONTENT(422),
	TOO_MANY_REQUEST(429),
	INTERNAL_SERVER_ERROR(500),
	BAD_GATEWAY_ERROR(502);

	private final int code;
}
