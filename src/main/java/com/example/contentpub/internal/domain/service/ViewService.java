package com.example.contentpub.internal.domain.service;

import com.example.contentpub.internal.domain.db.projection.ContentDbView;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.view.*;
import com.example.contentpub.internal.domain.db.projection.CompactContentDbView;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.db.repository.CategoryRepository;
import com.example.contentpub.internal.domain.db.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.contentpub.internal.domain.constant.DomainConstants.FAILURE;
import static com.example.contentpub.internal.domain.constant.DomainConstants.SUCCESS;
import static com.example.contentpub.internal.domain.constant.ErrorCode.*;

@Service
public class ViewService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${view.content-list.page-size.max:200}")
    private Integer maximumPageSize;

    public CommonDomainResponse<ContentListView> getContentList(ViewDomainRequest requestEntity) {

        CommonDomainResponse<ContentListView> response = new CommonDomainResponse<>();
        response.setDescription(new ContentListView());

        try {

            if (!categoryRepository.existsById(requestEntity.getCategoryId())) {
                throw new DomainException(CATEGORY_NOT_FOUND);
            }

            if (requestEntity.getPageSize() > maximumPageSize) {
                throw new DomainException(String.format(PAGE_TOO_LARGE.getDescription(), maximumPageSize),
                                PAGE_TOO_LARGE.getHttpStatusCode());
            }

            Integer totalCount = contentRepository.findContentCountByCategory(requestEntity.getCategoryId());

            List<CompactContentDbView> contentList;
            if (totalCount > 0) {
                Integer offset = requestEntity.getPage() * requestEntity.getPageSize();
                contentList = contentRepository.findContentByCategoryWithPagination(requestEntity.getCategoryId(),
                                requestEntity.getPageSize(), offset);
            } else {
                contentList = new ArrayList<>();
            }

            response.getDescription().setTotalCount(totalCount);
            response.getDescription().setCurrentPage(requestEntity.getPage());

            List<ContentListItemView> contentListItemViewList = contentList
                    .stream().map(item -> ContentListItemView.builder()
                            .id(item.getId())
                            .title(item.getTitle())
                            .createdAt(item.getCreatedAt().toString())
                            .updatedAt(item.getUpdatedAt().toString())
                            .writer(Writer.builder().id(item.getWriterId()).name(item.getWriterName()).build())
                            .build()) .collect(Collectors.toList());


            response.getDescription().setContentList(contentListItemViewList);
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus(SUCCESS);

        } catch (DomainException ex) {
            response.setStatusCode(ex.getHttpStatusCode());
            response.setStatus(FAILURE);
            response.getDescription().setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatus(FAILURE);
        }

        return response;
    }

    public CommonDomainResponse<ContentItemView> getSingleContentItem(ViewDomainRequest requestEntity) {

        CommonDomainResponse<ContentItemView> response = new CommonDomainResponse<>();
        response.setDescription(new ContentItemView());

        try {
            ContentDbView contentItemRawData = contentRepository.findByIdWithDetails(requestEntity.getContentId())
                    .orElseThrow(() -> new DomainException(CONTENT_NOT_FOUND));

            ContentItem contentItem = ContentItem.builder()
                    .id(contentItemRawData.getId())
                    .title(contentItemRawData.getTitle())
                    .summary(contentItemRawData.getSummary())
                    .details(contentItemRawData.getDetails())
                    .createdAt(contentItemRawData.getCreatedAt().toString())
                    .updatedAt(contentItemRawData.getUpdatedAt().toString())
                    .writer(Writer.builder()
                            .id(contentItemRawData.getWriterId())
                            .name(contentItemRawData.getWriterName())
                            .build())
                    .build();

            response.getDescription().setContentItem(contentItem);
            response.setStatusCode(HttpStatus.OK.value());
            response.setStatus(SUCCESS);

        } catch (DomainException ex) {
            response.setStatusCode(ex.getHttpStatusCode());
            response.setStatus(FAILURE);
            response.getDescription().setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setStatus(FAILURE);
        }

        return response;
    }
}
