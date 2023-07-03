package com.example.contentpub.internal.domain.service.impl.messaging;

import com.example.contentpub.internal.domain.dto.messaging.MessageDto;
import com.example.contentpub.internal.domain.dto.messaging.NotifyDto;
import com.example.contentpub.internal.domain.service.interfaces.messaging.MessageService;
import com.example.contentpub.internal.domain.boundary.service.PublishEventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private PublishEventService publishEventService;

    @Value("${messaging.is-enabled:true}")
    private boolean isNotificationSendEnabled;

    @Async("messagingThreadPool")
    @Override
    public void prepareAndSendMessage(MessageDto messageDto) {

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            org.json.JSONObject message = new org.json.JSONObject(ow.writeValueAsString(messageDto));
            JSONObject messageJson = new JSONObject(message.toMap());
            messageJson.put("timestamp", (new Date()).toString());

            NotifyDto notification = NotifyDto.builder()
                    .id(UUID.randomUUID().toString())
                    .data(messageJson)
                    .build();

            if (isNotificationSendEnabled) {
                publishEventService.publishNotification(notification);
            }

        } catch (JsonProcessingException ex) {
            log.error("Error parsing message object, error message: {}", ex.getMessage());
        }

    }
}
