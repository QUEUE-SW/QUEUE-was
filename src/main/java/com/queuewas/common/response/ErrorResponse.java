package com.queuewas.common.response;

import java.util.Collections;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse<T> {
	private String code;
	private String message;
	private T errors;

	public static <T> ErrorResponse<T> of(final String code, final String message) {
		return ErrorResponse.<T>builder()
			.message(message)
			.code(code)
			.errors((T)Collections.emptyList())
			.build();
	}

	public static <T> ErrorResponse<T> of(final String code, final String message, final T errors) {
		return ErrorResponse.<T>builder()
			.message(message)
			.code(code)
			.errors(errors)
			.build();
	}
}
