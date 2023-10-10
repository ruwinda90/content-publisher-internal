package com.example.contentpub.internal.domain.exception;

import com.example.contentpub.internal.domain.constant.ErrorCode;
import com.example.contentpub.internal.domain.constant.StatusCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends Exception { // todo

    private String code;
    private HttpStatus httpStatus;
    private Integer httpStatusCode;

    public DomainException(String message, Integer httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public DomainException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.httpStatusCode = errorCode.getHttpStatusCode();
    }

    public DomainException(String code, String description, HttpStatus httpStatus) {
        super(description);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public DomainException(StatusCode statusCode) {
        super(statusCode.getDescription());
        this.code = statusCode.getCode();
        this.httpStatus = statusCode.getHttpStatus();
    }

}
// todo - add mongo db
// add reactive
