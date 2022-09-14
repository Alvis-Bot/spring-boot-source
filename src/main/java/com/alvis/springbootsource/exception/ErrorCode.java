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
    REQUEST_TOO_LARGE(HttpStatus.BAD_REQUEST, "Request is too large"),


    // Firebase errors
    ID_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Id token is not found"),
    INVALID_ID_TOKEN(HttpStatus.UNAUTHORIZED, "Id token is invalid"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User not found" ),
    USER_LOCKED(HttpStatus.FORBIDDEN, "User is locked"),
    CANNOT_REGISTER_ADMIN(HttpStatus.CONFLICT, "Cannot register new admin"),
    EMAIL_ALREADY_EXIST(HttpStatus.CONFLICT, "Email is adready exists"),
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, "User is already exists"),
    FIREBASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Firebase error"),
    CANNOT_CHANGE_ADMIN_EMAIL(HttpStatus.CONFLICT, "Cannot change admin email"),;
    private final HttpStatus status;
    private final String message;
}
