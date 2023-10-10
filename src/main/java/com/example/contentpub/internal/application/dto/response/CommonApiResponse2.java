package com.example.contentpub.internal.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonApiResponse2<T> {

    private String code;

    private String description;

    private T data;

}
