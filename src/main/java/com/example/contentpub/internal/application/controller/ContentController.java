package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.application.dto.request.ContentCreateRequest;
import com.example.contentpub.internal.application.dto.request.ContentModifyRequest;
import com.example.contentpub.internal.application.dto.response.CommonApiResponse;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.ContentDomainRequest;
import com.example.contentpub.internal.domain.service.interfaces.content.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.contentpub.internal.domain.constant.DomainConstants.*;

/**
 * The ContentController contains Create, Update and Delete method APIs that relevant to content.
 */
@RestController
@RequestMapping("/publisher")
public class ContentController {

    private final Map<String, ContentService> contentServiceMap;

    public ContentController(Map<String, ContentService> contentServiceMap) {
        this.contentServiceMap = contentServiceMap;
    }

    /**
     * Create new content.
     *
     * @param contentCreateRequest the new content details.
     * @return the response indicating the status of the request.
     */
    @PostMapping("/content")
    public ResponseEntity<CommonApiResponse<String>> createContent(
            @RequestBody ContentCreateRequest contentCreateRequest) {

        ContentDomainRequest requestEntity = ContentDomainRequest.builder()
                .title(contentCreateRequest.getTitle())
                .summary(contentCreateRequest.getSummary())
                .details(contentCreateRequest.getDetails())
                .userId(contentCreateRequest.getUserId())
                .categoryId(contentCreateRequest.getCategoryId())
                .build();

        ContentService processor = contentServiceMap.get(CREATE_SERVICE);
        CommonDomainResponse<String> commonDomainResponse = processor.process(requestEntity);

        return ResponseEntity.status(commonDomainResponse.getStatusCode())
                .body(new CommonApiResponse<>(commonDomainResponse.getStatus(), commonDomainResponse.getDescription()));

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

        ContentService processor = contentServiceMap.get(EDIT_SERVICE);
        CommonDomainResponse<String> commonDomainResponse = processor.process(requestEntity);

        return ResponseEntity.status(commonDomainResponse.getStatusCode())
                .body(new CommonApiResponse<>(commonDomainResponse.getStatus(), commonDomainResponse.getDescription()));

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

        ContentService processor = contentServiceMap.get(DELETE_SERVICE);
        CommonDomainResponse<String> commonDomainResponse = processor.process(requestEntity);

        return ResponseEntity.status(commonDomainResponse.getStatusCode())
                .body(new CommonApiResponse<>(commonDomainResponse.getStatus(), commonDomainResponse.getDescription()));

    }

}
