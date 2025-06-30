package com.queuewas.domains.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queuewas.common.response.SuccessResponse;
import com.queuewas.domains.auth.dto.request.SignInReq;
import com.queuewas.domains.auth.dto.response.SignInRes;
import com.queuewas.domains.auth.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/sign-in")
	public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq) {
		SignInRes signInRes = authService.signIn(signInReq);
		return ResponseEntity.ok(SuccessResponse.of(signInRes));
	}
}
