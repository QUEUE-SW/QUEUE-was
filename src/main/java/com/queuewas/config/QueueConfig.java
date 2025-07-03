package com.queuewas.config;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.queuewas.domains.queue.domain.QueueUser;

@Configuration
public class QueueConfig {

	@Bean
	public Queue<QueueUser> queue() {
		return new ConcurrentLinkedQueue<>();
	}

	@Bean
	public Map<String, QueueUser> userMap() {
		return new ConcurrentHashMap<>();
	}

	@Bean
	public AtomicLong globalIndex() {
		return new AtomicLong(0);
	}
}
