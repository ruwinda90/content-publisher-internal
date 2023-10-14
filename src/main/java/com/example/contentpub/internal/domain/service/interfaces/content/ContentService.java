package com.example.contentpub.internal.domain.service.interfaces.content;

import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.domain.dto.publish.CreatedContent;

public interface ContentService {

    CommonDomainResponse2<CreatedContent> createContent(ContentDomainRequest domainRequest);

    CommonDomainResponse2<String> editContent(ContentDomainRequest domainRequest);

    CommonDomainResponse2<String> deleteContent(ContentDomainRequest domainRequest);

}
