package com.alvis.springbootsource.component;

import com.alvis.springbootsource.exception.ApiException;
import com.alvis.springbootsource.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFacade {
    public String getIdentity() {
        return getOptionalIdentity().orElseThrow(() -> {
            log.error("Retrieve identity but not logged in");
            return new ApiException(ErrorCode.UNAUTHORIZED);
        });
    }

    public @NonNull Optional<String> getOptionalIdentity() {
        var auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        if (auth.isEmpty() || auth.get() instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(auth.get().getName());
    }
}
