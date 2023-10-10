package com.example.contentpub.internal.domain.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {

    SUCCESS("1000", "Success", HttpStatus.OK),
    CREATED("1000", "Created", HttpStatus.CREATED),
    USER_NOT_FOUND("2001", "User not found in DB", HttpStatus.BAD_REQUEST),
    WRITER_NOT_REGISTERED("2100","User not registered as a writer in DB", HttpStatus.BAD_REQUEST),
    COUNTRY_NOT_FOUND("2200","Country not found in DB", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND("2201","Category not found in DB", HttpStatus.BAD_REQUEST),
    CONTENT_NOT_FOUND("2202", "Requested content not found in DB", HttpStatus.BAD_REQUEST),
    CONTENT_NOT_BELONG_TO_USER("2203", "User is not the owner of the content", HttpStatus.BAD_REQUEST),
    PAGE_TOO_LARGE("2204", "Page size is larger than allowed amount of %s", HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR("5000", "Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    StatusCode(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

}
