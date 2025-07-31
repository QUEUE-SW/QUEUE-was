package com.queuewas.domains.queue.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.queuewas.domains.queue.implement.BatchManager;
import com.queuewas.domains.queue.implement.RedisQueueManager;
import com.queuewas.domains.queue.implement.SseAsyncSender;
import com.queuewas.domains.queue.implement.SseEmitterManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseQueueService {

	private final SseEmitterManager emitterManager;
	private final RedisQueueManager redisQueueManager;
	private final BatchManager batchManager;
	private final ScheduledExecutorService scheduler;
	private final SseAsyncSender asyncSender;

	public SseEmitter subscribe(String token) {
		// 대기열 등록
		redisQueueManager.enqueue(token);
		long queueNumber = redisQueueManager.getQueueNumber(token);

		// SSEEmitter 생성 및 저장
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		emitterManager.addEmitter(token, emitter);

		// 초기 응답 전송 (WAITING + 순번)
		asyncSender.send(emitter, "waiting", Map.of(
				"status", "WAITING",
				"number", queueNumber),
			token
		);

		// 연결 종료 핸들링
		emitter.onCompletion(() -> emitterManager.removeEmitter(token));
		emitter.onTimeout(() -> emitterManager.removeEmitter(token));
		emitter.onError((e) -> emitterManager.removeEmitter(token));

		return emitter;
	}

	public void notifyEntranceToUsers(int count) {
		List<String> allowedTokens = redisQueueManager.popTokens(count);
		if (allowedTokens.isEmpty()) {
			return;
		}

		String batchId = batchManager.registerBatch(allowedTokens);

		// ALLOWED 사용자에게 전송 및 sse 종료
		for (String token : allowedTokens) {
			SseEmitter emitter = emitterManager.getEmitter(token);
			if (emitter != null) {
				asyncSender.send(emitter, "allowed", Map.of("status", "ALLOWED"), token);
			}
		}

		// 일정 시간 후 배치 완료 처리
		scheduler.schedule(() -> batchManager.completeBatchPartially(batchId), 10, TimeUnit.SECONDS);

		// 대기 사용자에게 순번 변경 알림
		List<String> waitingTokens = redisQueueManager.getAllWaitingTokens();
		for (String token : waitingTokens) {
			long queueNumber = redisQueueManager.getQueueNumber(token);
			SseEmitter emitter = emitterManager.getEmitter(token);
			if (emitter != null) {
				asyncSender.send(emitter, "waiting", Map.of(
					"status", "WAITING",
					"number", queueNumber), token);
			}
		}
	}

	public void notifyLogin(String token) {
		batchManager.notifyUserLogin(token);
		redisQueueManager.remove(token);
		emitterManager.removeEmitter(token);
	}

	public void removeQueueInfo(String token) {
		redisQueueManager.remove(token);
		emitterManager.removeEmitter(token);
	}
}
