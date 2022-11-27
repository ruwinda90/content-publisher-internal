package com.example.contentpub.internal.domain.service.messaging;

import com.example.contentpub.internal.domain.dto.messaging.NotifyDto;

public interface PublishEventService {

    void publishNotification(NotifyDto message);

}
