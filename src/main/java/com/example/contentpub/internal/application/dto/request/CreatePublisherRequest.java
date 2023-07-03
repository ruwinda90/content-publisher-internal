package com.example.contentpub.internal.application.dto.request;

import lombok.Data;

@Data
public class CreatePublisherRequest {

    private Integer userId;

    private String name;

    private String description;

    private Integer countryId;

}