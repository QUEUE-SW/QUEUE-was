package com.queuewas.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
public class AsyncExecutorConfig {

	private static final int CORE_POOL_SIZE = 16;
	private static final int MAX_POOL_SIZE = 64;
	private static final int QUEUE_CAPACITY = 800;
	private static final String THREAD_NAME_PREFIX = "SSE-Async-";

	@Bean(name = "sseAsyncExecutor")
	public ThreadPoolTaskExecutor sseAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(CORE_POOL_SIZE);
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		executor.setQueueCapacity(QUEUE_CAPACITY);
		executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
				log.warn("""
						❌ SSE 비동기 스레드풀 요청 초과!
						├─ Active Threads     : {}
						├─ Pool Size          : {}
						├─ Maximum Pool Size  : {}
						├─ Queue Size         : {}
						├─ Remaining Capacity : {}
						└─ Task Count         : {}
						""",
					e.getActiveCount(),
					e.getPoolSize(),
					e.getMaximumPoolSize(),
					e.getQueue().size(),
					e.getQueue().remainingCapacity(),
					e.getTaskCount()
				);
				super.rejectedExecution(r, e);
			}
		});

		executor.initialize();

		log.info("""
				✅ 비동기 스레드풀 초기화 완료
				├─ Core Pool Size    : {}
				├─ Max Pool Size     : {}
				├─ Queue Capacity    : {}
				└─ Thread Name Prefix: {}
				""",
			CORE_POOL_SIZE,
			MAX_POOL_SIZE,
			QUEUE_CAPACITY,
			THREAD_NAME_PREFIX
		);
		return executor;
	}
}
