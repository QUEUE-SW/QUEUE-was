package com.queuewas.config;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.type.QueueStatus;

@Configuration
public class QueueConfig {

	@Bean
	public Queue<QueueUser> queue() {
		return new ConcurrentLinkedQueue<>();
	}

	@Bean
	public Map<String, QueueStatus> statusMap() {
		return new ConcurrentHashMap<>();
	}
}
