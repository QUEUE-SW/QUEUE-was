package com.queuewas.domains.queue.implement;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseAsyncSender {

	private final SseEmitterManager emitterManager;

	@Async("sseAsyncExecutor")
	public void send(SseEmitter emitter, String event, Object data, String token) {
		try {
			emitter.send(SseEmitter.event()
				.name(event)
				.data(data));
			emitter.complete();
			log.info("✅ send 성공!!");
			emitterManager.removeEmitter(token);
			log.info("✅ emitter 종료 및 제거 완료: token={}", token);
		} catch (IOException e) {
			log.warn("🚨 SSE 전송 실패: event={}, message={}", event, e.getMessage());
		}
	}
}
