package com.queuewas.domains.queue.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.queuewas.common.response.SuccessResponse;
import com.queuewas.domains.queue.dto.request.QueueJoinReq;
import com.queuewas.domains.queue.dto.request.QueueReceiveReq;
import com.queuewas.domains.queue.dto.response.QueueJoinRes;
import com.queuewas.domains.queue.dto.response.QueueStatusRes;
import com.queuewas.domains.queue.service.QueueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	@GetMapping("/{uuid}")
	public ResponseEntity<?> readQueueStatus(@PathVariable(name = "uuid") String uuid) {
		QueueStatusRes queueStatusRes = queueService.readStatus(uuid);
		log.info("Read queue status: {}", queueStatusRes);
		return ResponseEntity.ok(SuccessResponse.of(queueStatusRes));
	}

	@PostMapping("/notify")
	public CompletableFuture<ResponseEntity<?>> receiveSlotRelease(@RequestBody QueueReceiveReq queueReceiveReq) {
		return queueService.allowQueueStatusWithAck(queueReceiveReq.count())
			.thenApply(v -> ResponseEntity.ok(SuccessResponse.noContent()));
	}

	@GetMapping("/clear")
	public ResponseEntity<?> clearQueue() {
		queueService.reset();
		return ResponseEntity.ok(SuccessResponse.noContent());
	}
}
