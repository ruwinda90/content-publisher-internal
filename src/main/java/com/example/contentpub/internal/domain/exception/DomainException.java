package com.example.contentpub.internal.domain.exception;

import com.example.contentpub.internal.domain.constant.ErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends Exception {

    private final Integer httpStatusCode;

    public DomainException(String message, Integer httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public DomainException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.httpStatusCode = errorCode.getHttpStatusCode();
    }

}
