package com.alvis.springbootsource.util;

import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CodeUtil {
    public static @Nullable String getBindExceptionMessage(
            BindException ex, String defaultMessage) {
        if (!ex.hasErrors() || ex.getAllErrors().isEmpty()) {
            return defaultMessage;
        }
        return ex.getAllErrors().get(0).getDefaultMessage();
    }

    public static Optional<String> extractTokenFromAuthHeader(String authHeader) {
        final var HEADER_PREFIX = "Bearer ";
        if (!authHeader.startsWith(HEADER_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring(HEADER_PREFIX.length()));
    }

    public static String getConstraintExceptionMessage(
            ConstraintViolationException ex, String defaultMessage) {
        var firstViolation = ex.getConstraintViolations().stream().findFirst();
        if (firstViolation.isEmpty()) {
            return defaultMessage;
        }
        return firstViolation.get().getMessage();
    }


    public static <T> void updateRequiredField(@Nullable T value, Consumer<T> consumer) {
        if (Objects.nonNull(value)) {
            consumer.accept(value);
        }
    }

    public static <T> void updateOptionalField(
            @Nullable T value, Predicate<T> blankPredicate, Consumer<T> consumer) {
        if (Objects.nonNull(value)) {
            if (blankPredicate.test(value)) {
                consumer.accept(null);
            } else {
                consumer.accept(value);
            }
        }
    }
}
