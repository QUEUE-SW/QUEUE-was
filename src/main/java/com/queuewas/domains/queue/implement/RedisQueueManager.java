package com.queuewas.domains.queue.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisQueueManager {

	private final StringRedisTemplate redisTemplate;
	private final static String QUEUE_KEY = "queue:waiting";

	public void enqueue(String token) {
		redisTemplate.opsForList().rightPush(QUEUE_KEY, token);
	}

	public List<String> popTokens(int count) {
		List<String> tokens = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			String token = redisTemplate.opsForList().leftPop(QUEUE_KEY);
			if (token == null)
				break;
			tokens.add(token);
		}
		return tokens;
	}

	public List<String> getAllWaitingTokens() {
		Long size = redisTemplate.opsForList().size(QUEUE_KEY);
		if (size == null)
			return List.of();
		return redisTemplate.opsForList().range(QUEUE_KEY, 0, size - 1);
	}

	public long getQueueNumber(String token) {
		Long index = redisTemplate.execute((RedisCallback<Long>)connection ->
			connection.listCommands().lPos("queue:waiting".getBytes(), token.getBytes()));

		if (index == null) {
			throw new QueueException(QueueErrorCode.QUEUE_NOT_FOUND);
		}
		return index + 1;
	}

	public void remove(String token) {
		redisTemplate.opsForList().remove(QUEUE_KEY, 1, token);
	}

	public void reset() {
		redisTemplate.delete(QUEUE_KEY);
	}
}
