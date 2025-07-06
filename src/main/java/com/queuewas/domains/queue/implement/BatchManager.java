package com.queuewas.domains.queue.implement;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.queuewas.common.annotation.Implementation;
import com.queuewas.domains.queue.domain.QueueUser;
import com.queuewas.domains.queue.type.QueueStatus;

import lombok.RequiredArgsConstructor;

@Implementation
@RequiredArgsConstructor
public class BatchManager {
	private final Map<String, CompletableFuture<Void>> batchFutures;
	private final Map<String, Set<String>> batchTokenMap;

	public String registerBatch(List<QueueUser> users) {
		String batchId = UUID.randomUUID().toString();
		Set<String> tokens = new HashSet<>();

		for (QueueUser user : users) {
			user.updateStatus(QueueStatus.ALLOWED);
			user.updateBatchId(batchId);
			tokens.add(user.getToken());
		}

		CompletableFuture<Void> future = new CompletableFuture<>();
		batchFutures.put(batchId, future);
		batchTokenMap.put(batchId, tokens);

		return batchId;
	}

	public void notifyUserLogin(String token, QueueUser user) {
		if (user == null) return;

		String batchId = user.getBatchId();
		if (batchId == null) return;

		Set<String> tokens = batchTokenMap.get(batchId);
		if (tokens != null) {
			tokens.remove(token);
			if (tokens.isEmpty()) {
				batchFutures.get(batchId).complete(null);
				removeBatch(batchId);
			}
		}
	}

	public CompletableFuture<Void> getFuture(String batchId) {
		return batchFutures.get(batchId);
	}

	public Set<String> getTokens(String batchId) {
		return batchTokenMap.get(batchId);
	}

	public void removeBatch(String batchId) {
		batchFutures.remove(batchId);
		batchTokenMap.remove(batchId);
	}

	public void completeBatchPartially(String batchId) {
		CompletableFuture<Void> future = batchFutures.get(batchId);
		if (future != null && !future.isDone()) {
			future.complete(null); // 남은 인원이 있더라도 그냥 완료시킴
		}
	}
}
