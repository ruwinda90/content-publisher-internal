package com.example.contentpub.internal.domain.dto.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ContentItemData {

    private Integer id;

    private String title;

    private String summary;

    private String details;

    private Date createdAt;

    private Date updatedAt;

    private Integer writerId;

    private String writerName;

}
