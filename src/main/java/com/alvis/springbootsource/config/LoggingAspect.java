package com.alvis.springbootsource.config;

import com.alvis.springbootsource.component.AuthFacade;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public record LoggingAspect(AuthFacade authFacade) {
    @Before("execution(* com.alvis.springbootsource.controller.*.*(..))")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        var username = authFacade.getOptionalIdentity();
        log.info(
                "{}.{}() - {}",
                joinPoint.getSignature().getDeclaringType().getSimpleName(),
                joinPoint.getSignature().getName(),
                username.isEmpty() ? "Guest" : username.get());
    }
}
