package com.devwiki.leafy.global.redis.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;

@Configuration
@PropertySource("classpath:application.properties")
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String redisHost;

	@Value("${spring.data.redis.port}")
	private int redisPort;

	@Value("${spring.data.redis.password}")
	private String redisPassword;

	@Bean(destroyMethod = "shutdown")
	public ClientResources clientResources() {
		return DefaultClientResources.create();
	}


	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(redisConnectionFactory(clientResources()));
		return redisTemplate;
	}

	@Bean
	public RedisConnectionFactory redisConnectionFactory(ClientResources clientResources) {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(redisHost);
		config.setPort(redisPort);
		config.setPassword(redisPassword);

		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
			.clientResources(clientResources)
			.commandTimeout(Duration.ofSeconds(10))
			.shutdownTimeout(Duration.ZERO)
			// .useSsl()
			.build();

		return new LettuceConnectionFactory(config, clientConfig);
	}

}

