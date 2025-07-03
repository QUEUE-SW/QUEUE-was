package com.queuewas.domains.queue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queuewas.common.response.SuccessResponse;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.service.QueueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

	private final QueueService queueService;

	@PostMapping("/join")
	public ResponseEntity<?> joinQueue(@RequestBody QueueJoinReq queueJoinReq) {
		QueueJoinRes queueJoinRes = queueService.join(queueJoinReq);
		return ResponseEntity.ok(SuccessResponse.of(queueJoinRes));
	}
}
