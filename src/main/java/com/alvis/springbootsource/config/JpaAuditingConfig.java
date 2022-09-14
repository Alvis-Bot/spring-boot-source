package com.alvis.springbootsource.config;

import com.alvis.springbootsource.component.AuthFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class JpaAuditingConfig {
    private final AuthFacade authFacade;

    @Bean
    public AuditorAware<String> auditorAwareBean() {
        return authFacade::getOptionalIdentity;
    }
}