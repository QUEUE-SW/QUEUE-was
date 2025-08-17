package com.queuewas.config;

import org.springframework.stereotype.Component;

import com.queuewas.domains.queue.implement.RedisQueueManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class CommonInitializer {

	private final RedisQueueManager redisQueueManager;

	@PostConstruct
	public void init() {
		redisQueueManager.reset();
		System.out.println("✅ Redis 대기열 자리 초기화 완료");
	}
}
