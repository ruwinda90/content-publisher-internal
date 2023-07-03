package com.example.contentpub.internal.application.dto.request;

import lombok.Data;

@Data
public class ContentCreateRequest {

    private String title;

    private String summary;

    private String details;

    private Integer userId;

    private Integer categoryId;

}