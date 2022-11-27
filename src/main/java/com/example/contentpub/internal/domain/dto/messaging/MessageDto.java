package com.example.contentpub.internal.domain.dto.messaging;

import com.example.contentpub.internal.domain.constant.ContentAction;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private String category;

    private Integer contentId;

    private ContentAction actionType;

}
