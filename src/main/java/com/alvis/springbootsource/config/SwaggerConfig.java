package com.alvis.springbootsource.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Value("${app.JWT_AUTH_HEADER}")
    private String JWT_AUTH_HEADER;

    @Bean
    public UiConfiguration uiConfigurationBean() {
        return UiConfigurationBuilder.builder().operationsSorter(OperationsSorter.METHOD).build();
    }

    @Bean
    public Docket docketBean() {
        return new Docket(DocumentationType.OAS_30).useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(List.of(apiKey()))
                .securityContexts(List.of(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Web Xem Phim",
                "Web Xem Phim APIs",
                "0.0.1",
                "/",
                new Contact("Alvis", "", "vietvd@thomi.com.vn"),
                "",
                "",
                Collections.emptyList());
    }

    private ApiKey apiKey() {
        return new ApiKey(JWT_AUTH_HEADER, "Bearer", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultSecurityReference())
                .build();
    }

    private List<SecurityReference> defaultSecurityReference() {
        var authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "Access everything");
        return List.of(new SecurityReference(JWT_AUTH_HEADER, authorizationScopes));
    }
}
