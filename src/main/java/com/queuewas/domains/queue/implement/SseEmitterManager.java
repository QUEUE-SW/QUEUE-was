package com.queuewas.domains.queue.implement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SseEmitterManager {

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	public void addEmitter(String token, SseEmitter emitter) {
		emitters.put(token, emitter);
	}

	public void removeEmitter(String token) {
		emitters.remove(token);
	}

	public SseEmitter getEmitter(String token) {
		return emitters.get(token);
	}

	public boolean hasEmitter(String token) {
		return emitters.containsKey(token);
	}
}
