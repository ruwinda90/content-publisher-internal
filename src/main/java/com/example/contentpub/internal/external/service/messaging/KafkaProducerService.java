package com.example.contentpub.internal.external.service.messaging;

import com.example.contentpub.internal.domain.service.messaging.PublishEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
public class KafkaProducerService implements PublishEventService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${messaging.topic-names.notification}")
    private String topicName;

    @Override
    public void publishNotification(String message) {

        ListenableFuture<SendResult<String, String>> future =  kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Message produced to broker successfully. Topic: '{}' Offset: {} Partition: {}",
                        topicName, result.getRecordMetadata().offset(), result.getRecordMetadata().partition());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Message not produced to broker. Topic: '{}' Cause : {}", topicName, ex.getMessage());
            }
        });
    }
}





