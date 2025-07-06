package com.queuewas.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

	@Bean
	public Map<String, CompletableFuture<Void>> batchFutures() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<String, Set<String>> batchTokenMap() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public Map<String, Integer> originalBatchSizeMap() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public ScheduledExecutorService scheduledExecutorService() {
		return Executors.newScheduledThreadPool(10); // 요청이 몰릴 경우 대비
	}

}
