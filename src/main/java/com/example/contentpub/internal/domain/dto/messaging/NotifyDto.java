package com.example.contentpub.internal.domain.dto.messaging;

import lombok.*;
import net.minidev.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotifyDto {

    private String id;

    private JSONObject data;

}
