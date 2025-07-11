package com.queuewas.domains.queue.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.implement.QueueManager;
import com.queuewas.domains.queue.implement.SseEmitterManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseQueueService {

	private final SseEmitterManager emitterManager;
	private final QueueManager queueManager;

	public SseEmitter subscribe(String token) {
		SseEmitter emitter = new SseEmitter(60_000L);
		emitterManager.addEmitter(token, emitter);

		emitter.onCompletion(() -> emitterManager.removeEmitter(token));
		emitter.onTimeout(() -> emitterManager.removeEmitter(token));
		emitter.onError((e) -> emitterManager.removeEmitter(token));

		return emitter;
	}

	public void notifyEntranceToUsers(int count) {
		List<QueueUser> users = queueManager.popUsers(count);
		if (users.isEmpty()) {
			return;
		}

		for (QueueUser user : users) {
			String token = user.getToken();
			SseEmitter emitter = emitterManager.getEmitter(token);
			if (emitter != null) {
				try {
					emitter.send(SseEmitter.event()
						.name("entrance")
						.data("입장 가능합니다."));
				} catch (IOException e) {
					emitterManager.removeEmitter(token);
				}
			}
		}
	}
}
