package com.alvis.springbootsource.exception;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode code;
    private Boolean isLoggingEnabled = false;

    public ApiException(ErrorCode code) {
        this(code, code.getMessage(), null);
    }

    public ApiException(ErrorCode code, String message) {
        this(code, message, null);
    }

    public ApiException(ErrorCode code, @Nullable Throwable cause) {
        this(code, code.getMessage(), cause);
    }

    public ApiException(ErrorCode code, String message, @Nullable Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ApiException setLoggingEnabled(boolean enabled) {
        this.isLoggingEnabled = enabled;
        return this;
    }
}
