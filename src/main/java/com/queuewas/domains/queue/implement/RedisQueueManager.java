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
	private static final String KEY_WAITING = "queue:waiting";
	private static final String KEY_ALLOWED = "queue:allowed";

	public void enqueue(String token) {
		redisTemplate.opsForList().rightPush(KEY_WAITING, token);
	}

	public List<String> popTokens(int count) {
		List<String> tokens = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			String token = redisTemplate.opsForList().leftPop(KEY_WAITING);
			if (token == null)
				break;
			tokens.add(token);
		}
		return tokens;
	}

	public long getQueueNumber(String token) {
		Long index = redisTemplate.execute((RedisCallback<Long>)connection ->
			connection.listCommands().lPos("queue:waiting".getBytes(), token.getBytes()));

		if (index == null) {
			throw new QueueException(QueueErrorCode.QUEUE_NOT_FOUND);
		}
		return index + 1;
	}

	public boolean isAllowed(String token) {
		Boolean isMember = redisTemplate.opsForSet().isMember(KEY_ALLOWED, token);
		return Boolean.TRUE.equals(isMember);
	}

	public void markAllowed(List<String> tokens) {
		redisTemplate.opsForSet().add(KEY_ALLOWED, tokens.toArray(new String[0]));
	}

	public void unmarkAllowed(String token) {
		redisTemplate.opsForSet().remove(KEY_ALLOWED, token);
	}

	public void remove(String token) {
		redisTemplate.opsForList().remove(KEY_WAITING, 1, token);
	}

	public void reset() {
		redisTemplate.delete(KEY_WAITING);
		redisTemplate.delete(KEY_ALLOWED);
	}
}
