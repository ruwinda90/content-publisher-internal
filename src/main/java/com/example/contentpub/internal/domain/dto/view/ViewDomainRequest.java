package com.example.contentpub.internal.domain.dto.view;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewDomainRequest {

    private Integer contentId;

    private Integer categoryId;

    private Integer page;

    private Integer pageSize;

}
