package com.example.contentpub.internal.domain.service.interfaces.content;

import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.domain.dto.publish.CreatedContent;

public interface ContentService {

    CommonDomainResponse<CreatedContent> createContent(ContentDomainRequest domainRequest);

    CommonDomainResponse<String> editContent(ContentDomainRequest domainRequest);

    CommonDomainResponse<String> deleteContent(ContentDomainRequest domainRequest);

}
