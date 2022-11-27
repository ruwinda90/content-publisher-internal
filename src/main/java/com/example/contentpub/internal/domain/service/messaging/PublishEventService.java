package com.example.contentpub.internal.domain.service.messaging;

public interface PublishEventService<T> {

    void publishNotification(T message);

}
