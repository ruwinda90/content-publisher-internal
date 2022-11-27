package com.example.contentpub.internal.domain.service.messaging;

import com.example.contentpub.internal.domain.dto.messaging.MessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class MessageUtil {

    @Autowired
    private PublishEventService publishEventService;

    @Value("${messaging.is-enabled:true}")
    private boolean isNotificationSendEnabled;

    @Async("messagingThreadPool")
    public void prepareAndSendMessage(MessageDto messageDto) {

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject messageJson = new JSONObject(ow.writeValueAsString(messageDto));
            messageJson.put("timestamp", (new Date()).toString());

            if (isNotificationSendEnabled) {
                publishEventService.publishNotification(messageJson.toString());
            }

        } catch (JsonProcessingException ex) {
            log.error("Error parsing message object, error message: {}", ex.getMessage());
        }

    }
}
