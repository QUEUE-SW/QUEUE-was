package com.queuewas.domains.auth.dto.request;


import jakarta.validation.constraints.NotBlank;

public record SignInReq(

	@NotBlank(message = "학번을 입력해주세요.")
	String identifier,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password
) {
}
