package com.alvis.springbootsource.util;

import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolationException;
import java.util.*;
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

    public static String[] addStringToArray(String[] array, String string) {
        Set<String> set = arrayToMutableSet(array);
        set.add(string);
        return set.toArray(String[]::new);
    }

    public static <T> Set<T> arrayToMutableSet(T[] array) {
        return new HashSet<>(List.of(array));
    }

    public static <T> List<T> arrayToList(T[] array) {
        if (array == null) return List.of();
        return List.of(array);
    }

    public static String[] removeStringFromArray(String[] array, String string) {
        Set<String> set = arrayToMutableSet(array);
        set.remove(string);
        return set.toArray(String[]::new);
    }

    public static String[] removeAllStringsFromArray(String[] array, List<String> strings) {
        Set<String> set = arrayToMutableSet(array);
        strings.forEach(set::remove);
        return set.toArray(String[]::new);
    }
}
