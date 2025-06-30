package com.queuewas.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class CustomTomcatConfig {

	// 🧪 여기서 스레드 수와 큐 크기를 조정하며 테스트
	private static final int MAX_THREADS = 200;
	private static final int QUEUE_CAPACITY = 100;

	@Bean
	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customTomcatConnector() {
		return factory -> factory.addConnectorCustomizers(connector -> {
			ProtocolHandler handler = connector.getProtocolHandler();
			if (handler instanceof AbstractProtocol<?> protocol) {

				protocol.setMaxThreads(MAX_THREADS);
				protocol.setAcceptCount(QUEUE_CAPACITY); // backlog 역할

				ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

				ThreadPoolExecutor executor = new ThreadPoolExecutor(
					MAX_THREADS,         // corePoolSize
					MAX_THREADS,         // maximumPoolSize
					0L, TimeUnit.MILLISECONDS,
					queue,
					new ThreadPoolExecutor.AbortPolicy() {
						@Override
						public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
							log.warn("🚨 요청 초과! 스레드풀/큐가 가득 찼습니다. 현재 상태:\n" +
									" - Active Threads     : {}\n" +
									" - Pool Size          : {}\n" +
									" - Maximum Pool Size  : {}\n" +
									" - Task Count         : {}\n" +
									" - Completed Tasks    : {}\n" +
									" - Queue Size         : {}\n" +
									" - Remaining QueueCap : {}",
								e.getActiveCount(),
								e.getPoolSize(),
								e.getMaximumPoolSize(),
								e.getTaskCount(),
								e.getCompletedTaskCount(),
								e.getQueue().size(),
								e.getQueue().remainingCapacity()
							);
							super.rejectedExecution(r, e);
						}
					}
				);

				protocol.setExecutor(executor);
			}
		});
	}
}