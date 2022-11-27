package com.example.contentpub.internal.domain.dto.view;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentItemView {

    private String description;

    private ContentItem contentItem;

}
