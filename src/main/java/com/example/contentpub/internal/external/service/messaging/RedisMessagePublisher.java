package com.example.contentpub.internal.external.service.messaging;

import com.example.contentpub.internal.domain.boundary.service.PublishEventService;
import com.example.contentpub.internal.domain.dto.messaging.NotifyDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements PublishEventService {

    private final RedisTemplate<String, NotifyDto> redisTemplate;

    private final ChannelTopic topic;

    public RedisMessagePublisher(RedisTemplate<String, NotifyDto> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publishNotification(NotifyDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
