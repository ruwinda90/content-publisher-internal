package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.application.dto.request.CreatePublisherRequest;
import com.example.contentpub.internal.application.dto.response.CommonApiResponse;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.dto.publish.DomainPublisherRequest;
import com.example.contentpub.internal.domain.service.impl.PublisherServiceImpl;
import com.example.contentpub.internal.domain.service.interfaces.PublisherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The PublisherController contains the endpoint used to change a READER user into WRITER user.
 */
@RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherServiceImpl publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * The endpoint to change the user role from READER to WRITER.
     *
     * @param createPublisherRequest the request body containing details of the new writer.
     * @return the response indicating the status of the request.
     */
    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse<String>> createPublisher(
            @RequestBody CreatePublisherRequest createPublisherRequest) {

        DomainPublisherRequest request = new DomainPublisherRequest();
        request.setUserId(createPublisherRequest.getUserId());
        request.setName(createPublisherRequest.getName());
        request.setDescription(createPublisherRequest.getDescription());
        request.setCountryId(createPublisherRequest.getCountryId());

        CommonDomainResponse<String> commonDomainResponse = publisherService.createPublisher(request);

        return ResponseEntity.status(commonDomainResponse.getStatusCode())
                .body(new CommonApiResponse<>(commonDomainResponse.getStatus(), commonDomainResponse.getDescription()));

    }

}
