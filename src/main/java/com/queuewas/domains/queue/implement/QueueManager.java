package com.queuewas.domains.queue.implement;

import java.util.Map;
import java.util.Queue;

import org.springframework.stereotype.Component;

import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.type.QueueStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QueueManager {
	private final Queue<QueueUser> queue;
	private final Map<String, QueueStatus> statusMap;

	public void enqueue(QueueUser user) {
		if (statusMap.containsKey(user.getToken())) {
			throw new QueueException(QueueErrorCode.FAILED_JOIN_QUEUE);
		}
		queue.add(user);
		statusMap.put(user.getToken(), QueueStatus.WAITING);
	}

	public int getQueueNumber(String token) {
		int index = 1;
		for (QueueUser user : queue) {
			if (user.getToken().equals(token)) {
				return index;
			}
			index++;
		}
		return index;
	}

	public QueueStatus getStatus(String token) {
		return statusMap.getOrDefault(token, QueueStatus.NONE);
	}

	public void remove(String token) {
		statusMap.remove(token);
	}

}
