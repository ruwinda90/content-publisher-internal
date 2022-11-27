package com.example.contentpub.internal.external.service.messaging;

import com.example.contentpub.internal.domain.service.messaging.PublishEventService;
import org.springframework.stereotype.Service;

@Service
public class PublishEventServiceImpl implements PublishEventService {

    @Override
    public void publishNotification(String message) {
        return;
    }
}
