package com.example.contentpub.internal.domain.dto.publish;

import lombok.Data;

@Data
public class DomainPublisherRequest {

    private Integer userId;

    private String name;

    private String description;

    private Integer countryId;

}
