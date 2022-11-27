package com.example.contentpub.internal.external.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONObject;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifyDto {

    private String id;

    private String eventName;

    private JSONObject data;

}
