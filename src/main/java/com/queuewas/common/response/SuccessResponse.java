package com.queuewas.common.response;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuccessResponse<T> {

	private final String code = "2000";
	private final String message = "요청에 성공하였습니다.";
	private T data;

	@Builder
	public SuccessResponse(T data) {
		this.data = data;
	}

	public static SuccessResponse noContent() {
		return SuccessResponse.builder()
			.data(Map.of())
			.build();
	}

	public static <T> SuccessResponse<T> of(T data) {
		return SuccessResponse.<T>builder()
			.data(data)
			.build();
	}

	public static <T> SuccessResponse<Map<String, T>> of(String key, T data) {
		return SuccessResponse.<Map<String, T>>builder()
			.data(Map.of(key, data))
			.build();
	}

}
