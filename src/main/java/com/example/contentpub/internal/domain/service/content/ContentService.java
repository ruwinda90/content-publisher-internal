package com.example.contentpub.internal.domain.service.content;

import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;

public interface ContentService {

    CommonDomainResponse<String> process(ContentDomainRequest domainRequest);

}
