package com.example.contentpub.internal.domain.dto.view;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentItem {

    private Integer id;

    private String title;

    private String summary;

    private String details;

    private String createdAt;

    private String updatedAt;

    private Writer writer;

}
