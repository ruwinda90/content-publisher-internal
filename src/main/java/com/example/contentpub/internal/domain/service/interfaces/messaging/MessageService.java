package com.example.contentpub.internal.domain.service.interfaces.messaging;

import com.example.contentpub.internal.domain.dto.messaging.MessageDto;

public interface MessageService {

    void prepareAndSendMessage(MessageDto messageDto);

}
