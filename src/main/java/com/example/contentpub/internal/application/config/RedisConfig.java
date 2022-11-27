package com.example.contentpub.internal.application.config;

import com.example.contentpub.internal.external.dto.NotifyDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("payments");
    }

    @Bean
    public RedisTemplate<String, NotifyDto> redisTemplate() {
        final RedisTemplate<String, NotifyDto> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(NotifyDto.class));
        return template;
    }

}
