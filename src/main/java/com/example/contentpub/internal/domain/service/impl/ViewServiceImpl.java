package com.example.contentpub.internal.domain.service.impl;

import com.example.contentpub.internal.domain.boundary.repository.CategoryRepository;
import com.example.contentpub.internal.domain.boundary.repository.ContentRepository;
import com.example.contentpub.internal.domain.constant.StatusCode;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse2;
import com.example.contentpub.internal.domain.dto.view.*;
import com.example.contentpub.internal.domain.exception.DomainException;
import com.example.contentpub.internal.domain.service.interfaces.ViewService;
import com.example.contentpub.internal.external.entity.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.contentpub.internal.domain.constant.ErrorCode.CONTENT_NOT_FOUND;

@Service
public class ViewServiceImpl implements ViewService {

    private final ContentRepository contentRepository;

    private final CategoryRepository categoryRepository;

    private final Integer maximumPageSize;

    public ViewServiceImpl(ContentRepository contentRepository, CategoryRepository categoryRepository,
                           @Value("${view.content-list.page-size.max:200}") Integer maximumPageSize) {
        this.contentRepository = contentRepository;
        this.categoryRepository = categoryRepository;
        this.maximumPageSize = maximumPageSize;
    }

    @Override
    public CommonDomainResponse2<ContentListView> getContentList(ViewDomainRequest requestEntity) {

        CommonDomainResponse2<ContentListView> response = new CommonDomainResponse2<>();
        response.setData(new ContentListView());

        try {

            if (!categoryRepository.existsById(requestEntity.getCategoryId())) {
                throw new DomainException(StatusCode.CATEGORY_NOT_FOUND);
            }

            if (requestEntity.getPageSize() > maximumPageSize) {
                throw new DomainException(StatusCode.PAGE_TOO_LARGE.getCode(), StatusCode.PAGE_TOO_LARGE.getDescription(), StatusCode.PAGE_TOO_LARGE.getHttpStatus());
            }

            Page<CompactContentItemData> contentPage = contentRepository
                    .findContentByCategoryWithPagination(requestEntity.getCategoryId(),
                            PageRequest.of(requestEntity.getPage() - 1, requestEntity.getPageSize(), Sort.Direction.DESC,
                                    "updatedAt"));

            List<CompactContentItemData> contentList = contentPage.getContent();

            response.getData().setTotalCount(contentPage.getTotalElements());
            response.getData().setCurrentPage(requestEntity.getPage());

            List<ContentListItemView> contentListItemViewList = contentList
                    .stream().map(item -> ContentListItemView.builder()
                            .id(item.getId())
                            .title(item.getTitle())
                            .summary(item.getSummary())
                            .createdAt(item.getCreatedAt().toString())
                            .updatedAt(item.getUpdatedAt().toString())
                            .writer(WriterDto.builder().id(item.getWriterId()).name(item.getWriterName()).build())
                            .category(CategoryDto.builder().id(item.getCategoryId()).name(item.getCategoryName()).build())
                            .build()).collect(Collectors.toList());


            response.getData().setContentList(contentListItemViewList);
            response.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
            response.setCode(StatusCode.SUCCESS.getCode());
            response.setDescription(StatusCode.SUCCESS.getDescription());

        } catch (DomainException ex) {
            response.setHttpStatusCode(ex.getHttpStatusCode());
            response.setCode(ex.getCode());
            response.setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCode(StatusCode.INTERNAL_ERROR.getCode());
            response.setDescription(StatusCode.INTERNAL_ERROR.getDescription());
        }

        return response;
    }

    @Override
    public CommonDomainResponse2<ContentItemView> getSingleContentItem(ViewDomainRequest requestEntity) {

        CommonDomainResponse2<ContentItemView> response = new CommonDomainResponse2<>();

        try {
            ContentItemData contentItemRawData = contentRepository.findByIdWithDetails(requestEntity.getContentId())
                    .orElseThrow(() -> new DomainException(CONTENT_NOT_FOUND));

            ContentItemView contentItem = ContentItemView.builder()
                    .id(contentItemRawData.getId())
                    .title(contentItemRawData.getTitle())
                    .summary(contentItemRawData.getSummary())
                    .details(contentItemRawData.getDetails())
                    .createdAt(contentItemRawData.getCreatedAt().toString())
                    .updatedAt(contentItemRawData.getUpdatedAt().toString())
                    .writer(WriterDto.builder()
                            .id(contentItemRawData.getWriterId())
                            .name(contentItemRawData.getWriterName())
                            .build())
                    .category(CategoryDto.builder().id(contentItemRawData.getCategoryId())
                            .name(contentItemRawData.getCategoryName()).build())
                    .build();

            response.setData(contentItem);
            response.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
            response.setCode(StatusCode.SUCCESS.getCode());
            response.setDescription(StatusCode.SUCCESS.getDescription());

        } catch (DomainException ex) {
            response.setHttpStatusCode(ex.getHttpStatusCode());
            response.setCode(ex.getCode());
            response.setDescription(ex.getMessage());
        } catch (Exception ex) {
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCode(StatusCode.INTERNAL_ERROR.getCode());
            response.setDescription(StatusCode.INTERNAL_ERROR.getDescription());
        }

        return response;
    }

    @Override
    public CommonDomainResponse2<CategoryListView> getCategoryList() {

        CommonDomainResponse2<CategoryListView> response = new CommonDomainResponse2<>();

        try {
            List<Category> categoryRawDataList = categoryRepository.findAll();

            List<CategoryView> categoryDataList = categoryRawDataList.stream()
                    .map(item -> new CategoryView(item.getId(), item.getName())).collect(Collectors.toList());

            CategoryListView categoryListView = new CategoryListView();
            categoryListView.setCategoryList(categoryDataList);

            response.setData(categoryListView);
            response.setHttpStatusCode(StatusCode.SUCCESS.getHttpStatus().value());
            response.setCode(StatusCode.SUCCESS.getCode());
            response.setDescription(StatusCode.SUCCESS.getDescription());

        } catch (Exception ex) {
            response.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCode(StatusCode.INTERNAL_ERROR.getCode());
            response.setDescription(StatusCode.INTERNAL_ERROR.getDescription());
        }

        return response;
    }
}
