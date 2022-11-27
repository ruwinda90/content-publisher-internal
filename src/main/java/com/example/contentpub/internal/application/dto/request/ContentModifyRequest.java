package com.example.contentpub.internal.application.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentModifyRequest {

    private String title;

    private String summary;

    private String details;

    private Integer userId;

}