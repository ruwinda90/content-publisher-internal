package com.example.contentpub.internal.external.service.messaging;


import com.example.contentpub.internal.domain.service.messaging.PublishEventService;
import com.example.contentpub.internal.domain.dto.messaging.NotifyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher implements PublishEventService {

    @Autowired
    private RedisTemplate<String, NotifyDto> redisTemplate;

    @Autowired
    private ChannelTopic topic;

    @Override
    public void publishNotification(NotifyDto message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
