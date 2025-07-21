package com.queuewas.domains.queue.implement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import com.queuewas.common.annotation.Implementation;
import com.queuewas.common.exception.queue.QueueErrorCode;
import com.queuewas.common.exception.queue.QueueException;
import com.queuewas.domains.queue.domain.QueueUser;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class QueueManager {
	private final Queue<QueueUser> queue;
	private final Map<String, QueueUser> userMap;
	private final AtomicLong globalIndex;

	public void enqueue(String token) {
		if (userMap.containsKey(token)) {
			throw new QueueException(QueueErrorCode.FAILED_JOIN_QUEUE);
		}

		long joinedAt = System.currentTimeMillis();
		long joinQueueNumber = globalIndex.incrementAndGet();
		QueueUser user = QueueUser.create(token, joinedAt, joinQueueNumber);

		queue.add(user);
		userMap.put(token, user);
	}

	public List<QueueUser> popUsers(int allowedCount) {
		List<QueueUser> users = new ArrayList<>();
		for (int i = 0; i < allowedCount; i++) {
			QueueUser user = queue.poll();
			if (user == null) break;
			users.add(user);
		}
		return users;
	}


	public long getQueueNumber(String token) {
		QueueUser user = userMap.get(token);

		long totalIndex = globalIndex.get();
		long enterIndex = totalIndex - queue.size() + 1;
		return user.getJoinQueueNumber() - enterIndex + 1;
	}

	public QueueUser getQueueUser(String token) {
		return userMap.getOrDefault(token, null);
	}

	public List<QueueUser> getAllWaitingUsers() {
		return new ArrayList<>(queue);
	}

	public void remove(String token) {
		userMap.remove(token);
	}

	public void requeue(QueueUser user) {
		queue.add(user);
	}

	public void reset() {
		globalIndex.set(0);
		queue.clear();
		userMap.clear();
	}
}
