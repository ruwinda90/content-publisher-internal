package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.application.dto.request.ContentCreateRequest;
import com.example.contentpub.internal.application.dto.request.ContentModifyRequest;
import com.example.contentpub.internal.application.dto.response.CommonApiResponse;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.domain.dto.publish.CreatedContent;
import com.example.contentpub.internal.domain.service.interfaces.content.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The ContentController contains Create, Update and Delete method APIs that relevant to content.
 */
@RestController
@RequestMapping("/publisher")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * Create new content.
     *
     * @param contentCreateRequest the new content details.
     * @return the response indicating the status of the request.
     */
    @PostMapping("/content")
    public ResponseEntity<CommonApiResponse<CreatedContent>> createContent(
            @RequestBody ContentCreateRequest contentCreateRequest) {

        ContentDomainRequest requestEntity = ContentDomainRequest.builder()
                .title(contentCreateRequest.getTitle())
                .summary(contentCreateRequest.getSummary())
                .details(contentCreateRequest.getDetails())
                .userId(contentCreateRequest.getUserId())
                .categoryId(contentCreateRequest.getCategoryId())
                .build();

        CommonDomainResponse<CreatedContent> domainResponse = contentService.createContent(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(new CommonApiResponse<>(domainResponse.getCode(), domainResponse.getDescription(), domainResponse.getData()));

    }

    /**
     * Update an existing content. Only the owner of the content can edit the content.
     *
     * @param contentModifyRequest the request body containing updated fields of the content.
     * @param contentId            the ID of the content as a URL path variable.
     * @return the response indicating the status of the request.
     */
    @PutMapping("/content/{id}")
    public ResponseEntity<CommonApiResponse<String>> updateContent(
            @RequestBody ContentModifyRequest contentModifyRequest, @PathVariable("id") Integer contentId) {

        ContentDomainRequest requestEntity = ContentDomainRequest.builder()
                .contentId(contentId)
                .title(contentModifyRequest.getTitle())
                .summary(contentModifyRequest.getSummary())
                .details(contentModifyRequest.getDetails())
                .userId(contentModifyRequest.getUserId())
                .build();

        CommonDomainResponse<String> domainResponse = contentService.editContent(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(new CommonApiResponse<>(domainResponse.getCode(), domainResponse.getDescription(), domainResponse.getData()));

    }

    /**
     * Delete an existing content. Only the content owner can delete the content.
     *
     * @param userId    the user ID as a request parameter.
     * @param contentId the content ID to be deleted.
     * @return the response indicating the status of the request.
     */
    @DeleteMapping("/content/{id}")
    public ResponseEntity<CommonApiResponse<String>> deleteContent(@RequestParam(name = "userId") Integer userId,
                                                                   @PathVariable("id") Integer contentId) {

        ContentDomainRequest requestEntity = ContentDomainRequest.builder()
                .contentId(contentId)
                .userId(userId)
                .build();

        CommonDomainResponse<String> domainResponse = contentService.deleteContent(requestEntity);

        return ResponseEntity.status(domainResponse.getHttpStatusCode())
                .body(new CommonApiResponse<>(domainResponse.getCode(), domainResponse.getDescription(), domainResponse.getData()));

    }

}
