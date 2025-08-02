package com.queuewas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import com.queuewas.domains.queue.implement.RedisQueueSubscriber;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisSubscriberConfig {

	private final RedisConnectionFactory connectionFactory;
	private final RedisQueueSubscriber redisQueueSubscriber;

	private static final String CHANNEL_NAME = "entrance-channel";

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(redisQueueSubscriber, new ChannelTopic(CHANNEL_NAME));
		return container;
	}
}
