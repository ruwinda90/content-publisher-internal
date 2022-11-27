package com.example.contentpub.internal.domain.dto.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContentListView {

    private List<ContentListItemView> contentList;

    private Integer totalCount;

    private Integer currentPage;

    private String description;

}
