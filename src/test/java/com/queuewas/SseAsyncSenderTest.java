package com.queuewas;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.queuewas.domains.queue.implement.SseAsyncSender;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class SseAsyncSenderTest extends TestDatabaseConfig {

	@Autowired
	private SseAsyncSender asyncSender;

	@Test
	@DisplayName("비동기 전송이 실제로 별도 스레드에서 실행되는지 테스트")
	void asyncSend_shouldRunInAsyncThread() throws InterruptedException {
		SseEmitter emitter = new SseEmitter();

		asyncSender.send(emitter, "test-event", Map.of("message", "hello async"));

		System.out.println("[Main Thread] 테스트 종료 직전 - 비동기 로그를 기다립니다");

		// 로그 출력을 위해 기다림 (비동기 스레드는 이후 실행됨)
		Thread.sleep(500);
	}

	@Test
	@DisplayName("이미 완료된 Emitter에 전송하면 실패 로그가 출력되어야함")
	void asyncSend_shouldFailIfEmitterIsClosed() throws InterruptedException {
		SseEmitter emitter = new SseEmitter();
		emitter.complete(); // 이미 종료

		asyncSender.send(emitter, "test-fail", Map.of("message", "will fail"));

		Thread.sleep(500);
	}
}
