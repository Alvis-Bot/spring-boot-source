package com.alvis.springbootsource.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Default Spring MVC errors
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "Not Acceptable"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type"),

    // Business errors
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access Denied"),
    REQUEST_TOO_LARGE(HttpStatus.BAD_REQUEST, "Request is too large")

    // Application errors


    ;
    private final HttpStatus status;
    private final String message;
}
