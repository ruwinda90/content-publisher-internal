package com.example.contentpub.internal.domain.dto.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CompactContentItemData {

    private Integer id;

    private String title;

    private Date createdAt;

    private Date updatedAt;

    private Integer writerId;

    private String writerName;

}
