package com.example.contentpub.internal.domain.service.interfaces;

import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.publish.CreatedWriter;
import com.example.contentpub.internal.domain.dto.publish.DomainPublisherRequest;

public interface PublisherService {

    CommonDomainResponse2<CreatedWriter> createPublisher(DomainPublisherRequest createPublisherRequest);

}
