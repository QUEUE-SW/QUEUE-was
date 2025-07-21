package com.queuewas.domains.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.common.response.SuccessResponse;
import com.queuewas.domains.auth.dto.request.SignInReq;
import com.queuewas.domains.auth.dto.response.SignInRes;
import com.queuewas.domains.auth.service.AuthService;
import com.queuewas.domains.queue.service.SseQueueService;
import com.queuewas.domains.queue.type.QueueStatus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final SseQueueService queueService;

	@PostMapping("/sign-in")
	public ResponseEntity<?> signIn(@Valid @RequestBody SignInReq signInReq) {
		log.info("[SignIn] identifier: {} password: {} uuid: {}", signInReq.identifier(), signInReq.password(),
			signInReq.uuid());
		QueueStatus queueStatus = queueService.readStatus(signInReq.uuid()).queueStatus();
		if (!queueStatus.equals(QueueStatus.ALLOWED)) {
			throw new QueueException(QueueErrorCode.QUEUE_IS_NOT_ALLOWED);
		}
		queueService.notifyLogin(signInReq.uuid());
		queueService.removeQueueInfo(signInReq.uuid());
		SignInRes signInRes = authService.signIn(signInReq);

		log.info("AccessToken: {}", signInRes.accessToken());
		return ResponseEntity.ok(SuccessResponse.of(signInRes));
	}
}
