package com.example.contentpub.internal.domain.service.interfaces;

import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.view.ContentItemView;
import com.example.contentpub.internal.domain.dto.view.ContentListView;
import com.example.contentpub.internal.domain.dto.view.ViewDomainRequest;

public interface ViewService {

    CommonDomainResponse2<ContentListView> getContentList(ViewDomainRequest requestEntity);

    CommonDomainResponse<ContentItemView> getSingleContentItem(ViewDomainRequest requestEntity);

}
