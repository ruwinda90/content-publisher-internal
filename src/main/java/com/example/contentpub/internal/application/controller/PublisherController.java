package com.example.contentpub.internal.application.controller;

import com.example.contentpub.internal.application.dto.request.CreatePublisherRequest;
import com.example.contentpub.internal.application.dto.response.CommonApiResponse;
import com.example.contentpub.internal.domain.dto.CommonDomainResponse;
import com.example.contentpub.internal.domain.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PublisherService publisherService;

    /**
     * The endpoint to change the user role from READER to WRITER.
     *
     * @param createPublisherRequest the request body containing details of the new writer.
     * @return the response indicating the status of the request.
     */
    @PostMapping("/register")
    public ResponseEntity<CommonApiResponse<String>> createPublisher(@RequestBody CreatePublisherRequest createPublisherRequest) {

        CommonDomainResponse<String> commonDomainResponse = publisherService.createPublisher(createPublisherRequest);

        return ResponseEntity.status(commonDomainResponse.getStatusCode())
                .body(new CommonApiResponse<>(commonDomainResponse.getStatus(), commonDomainResponse.getDescription()));

    }

}
