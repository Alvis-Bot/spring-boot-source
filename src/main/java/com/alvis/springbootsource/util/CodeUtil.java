package com.alvis.springbootsource.util;

import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolationException;

public class CodeUtil {
    public static @Nullable String getBindExceptionMessage(
            BindException ex, String defaultMessage) {
        if (!ex.hasErrors() || ex.getAllErrors().isEmpty()) {
            return defaultMessage;
        }
        return ex.getAllErrors().get(0).getDefaultMessage();
    }

    public static String getConstraintExceptionMessage(
            ConstraintViolationException ex, String defaultMessage) {
        var firstViolation = ex.getConstraintViolations().stream().findFirst();
        if (firstViolation.isEmpty()) {
            return defaultMessage;
        }
        return firstViolation.get().getMessage();
    }
}
