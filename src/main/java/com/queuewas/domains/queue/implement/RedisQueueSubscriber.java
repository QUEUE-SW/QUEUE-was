package com.queuewas.domains.queue.implement;

import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.queuewas.domains.queue.service.SseQueueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisQueueSubscriber implements MessageListener {

	private final SseQueueService sseQueueService;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String msg = new String(message.getBody(), StandardCharsets.UTF_8);
		log.info("📥 Redis 메시지 수신: {}", msg);

		try {
			int count = Integer.parseInt(msg);
			sseQueueService.notifyEntranceToUsers(count);
		} catch (NumberFormatException e) {
			log.warn("❗ 메시지 형식이 잘못되었습니다: {}", msg);
		}
	}
}
