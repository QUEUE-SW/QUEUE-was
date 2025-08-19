/*
package com.queuewas.domains.queue.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.queuewas.common.response.SuccessResponse;
import com.queuewas.domains.queue.dto.request.QueueReceiveReq;
import com.queuewas.domains.queue.service.SseQueueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/queue/sse")
@RequiredArgsConstructor
public class SseQueueController {

	private final SseQueueService sseQueueService;

	@GetMapping(value = "/{uuid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe(@PathVariable String uuid) {
		log.info("Subscribe SSE connection request: uuid = {}", uuid);
		return sseQueueService.subscribe(uuid);
	}

	@PostMapping("/notify")
	public ResponseEntity<?> notifyEntrance(@RequestBody QueueReceiveReq request) {
		sseQueueService.notifyEntranceToUsers(request.count());
		return ResponseEntity.ok(SuccessResponse.noContent());
	}
}
*/
