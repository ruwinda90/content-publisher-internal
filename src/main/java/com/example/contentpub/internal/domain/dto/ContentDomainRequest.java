package com.example.contentpub.internal.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentDomainRequest {

    private Integer contentId;

    private String title;

    private String summary;

    private String details;

    private Integer userId;

    private Integer categoryId;

}
