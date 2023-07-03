package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.domain.constant.ContentAction;
import com.example.contentpub.internal.domain.dto.messaging.MessageDto;
import com.example.contentpub.internal.domain.service.messaging.MessageUtil;
import com.example.contentpub.internal.domain.dto.messaging.NotifyDto;
import com.example.contentpub.internal.external.service.messaging.RedisMessagePublisher;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final RedisMessagePublisher messagePublisher;

    private final MessageUtil messageUtil;

    public TestController(RedisMessagePublisher messagePublisher, MessageUtil messageUtil) {
        this.messagePublisher = messagePublisher;
        this.messageUtil = messageUtil;
    }

    @GetMapping("/redis-test")
    public String redisTrigger(@RequestParam("name") String name,
                               @RequestParam("desc") String description) {

        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setData(new JSONObject());
        notifyDto.getData().put("description", description);

        messagePublisher.publishNotification(notifyDto);
        return "done";
    }

    @GetMapping("/redis-test2")
    public String redisTrigger2(@RequestParam("name") String name,
                               @RequestParam("desc") String description) {

        NotifyDto notifyDto = new NotifyDto();
        notifyDto.setData(new JSONObject());
        notifyDto.getData().put("description", description);

        MessageDto messageDto = new MessageDto();
        messageDto.setContentId(12);
        messageDto.setActionType(ContentAction.CREATE);
        messageDto.setCategory("hemmlo");

        messageUtil.prepareAndSendMessage(messageDto);
        return "done";
    }

}
