package com.example.contentpub.internal.domain.service.interfaces;

import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.publish.DomainPublisherRequest;

public interface PublisherService {

    CommonDomainResponse<String> createPublisher(DomainPublisherRequest createPublisherRequest);

}
