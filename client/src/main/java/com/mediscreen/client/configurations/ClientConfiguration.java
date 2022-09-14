package com.mediscreen.client.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class ClientConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket patientApi() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.basePackage(
                                                              "com.mediscreen.client.controllers."))
                                                      .build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addRedirectViewController("/", "/app/patients");
    }
}
