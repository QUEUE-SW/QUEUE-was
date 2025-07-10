package com.queuewas.domains.queue.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.queuewas.domains.queue.implement.SseEmitterManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseQueueService {

	private final SseEmitterManager emitterManager;

	public SseEmitter subscribe(String token) {
		SseEmitter emitter = new SseEmitter(60_000L);
		emitterManager.addEmitter(token, emitter);

		emitter.onCompletion(() -> emitterManager.removeEmitter(token));
		emitter.onTimeout(() -> emitterManager.removeEmitter(token));
		emitter.onError((e) -> emitterManager.removeEmitter(token));

		return emitter;
	}
}
