package com.mediscreen.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
@EnableFeignClients
public class ClientApplication {

    public static void main(String[] args) {

        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public Docket patientApi() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.basePackage(
                                                              "com.mediscreen.report"))
                                                      .build();
    }
}