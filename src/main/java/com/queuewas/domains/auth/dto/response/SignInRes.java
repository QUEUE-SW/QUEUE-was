package com.queuewas.domains.auth.dto.response;

public record SignInRes(
	Long id,
	String accessToken
) {
	public static SignInRes of(Long id, String accessToken) {
		return new SignInRes(id, accessToken);
	}
}
