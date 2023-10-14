package com.example.contentpub.internal.domain.dto.view;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentItemView {

    private Integer id;

    private String title;

    private String summary;

    private String details;

    private String createdAt;

    private String updatedAt;

    private WriterDto writer;

    private CategoryDto category;

}
