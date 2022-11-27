package com.example.contentpub.internal.domain.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND("User not found in DB", 400),
    WRITER_NOT_REGISTERED("User not registered as a writer in DB", 400),
    COUNTRY_NOT_FOUND("Country not found in DB", 400),
    CATEGORY_NOT_FOUND("Category not found in DB", 400),
    CONTENT_NOT_FOUND("Requested content not found in DB", 404),
    CONTENT_NOT_BELONG_TO_USER("User is not the owner of the content", 400),
    PAGE_TOO_LARGE("Page size is larger than allowed amount of %s", 400);

    private final String description;
    private final Integer httpStatusCode;

    ErrorCode(String description, Integer httpStatusCode) {
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }

}
