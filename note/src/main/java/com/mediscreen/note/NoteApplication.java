package com.mediscreen.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class NoteApplication {

    public static void main(String[] args) {

        SpringApplication.run(NoteApplication.class, args);
    }

    @Bean
    public Docket patientApi() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                                                      .apis(RequestHandlerSelectors.basePackage(
                                                              "com.mediscreen.note.controllers"))
                                                      .build();
    }
}
