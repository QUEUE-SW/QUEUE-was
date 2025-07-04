package com.queuewas.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

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

}
