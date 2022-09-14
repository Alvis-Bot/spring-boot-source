package com.alvis.springbootsource.exception;

import com.alvis.springbootsource.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        if (e.getIsLoggingEnabled() == Boolean.TRUE)
            log.error("Ops!", e);
        var response = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.status(e.getCode().getStatus()).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException() {
        return new ErrorResponse(ErrorCode.ACCESS_DENIED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnwantedException(Exception e) {
        if (e instanceof ConversionNotSupportedException ||
                e instanceof HttpMessageNotWritableException)
            log.warn("Hmm!", e);
        else
            log.error("Ops!", e);
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // Spring MVC default exceptions
    // https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html#mvc-ann-rest-spring-mvc-exceptions
    // NoSuchRequestHandlingMethodException is missing

    @ExceptionHandler({
            BindException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            MissingServletRequestPartException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            TypeMismatchException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Exception e) {
        String message = "Bad request";

        // Validation failed, missing request param or part
        if (e instanceof BindException ex)
            message = CodeUtil.getBindExceptionMessage(ex, "Validation failed");
        else if (e instanceof ConstraintViolationException ex)
            message = CodeUtil.getConstraintExceptionMessage(ex, "Validation failed");
        else if (e instanceof MissingServletRequestParameterException ex)
            message = "Missing " + ex.getParameterName() + " request param";
        else if (e instanceof MissingServletRequestPartException ex)
            message = "Missing " + ex.getRequestPartName() + " request part";

        return new ErrorResponse(ErrorCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleMethodNotSupportedException() {
        return new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleMediaTypeNotAcceptableException() {
        return new ErrorResponse(ErrorCode.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleMediaTypeNotSupportedException() {
        return new ErrorResponse(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }
}
