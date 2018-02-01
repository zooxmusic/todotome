package com.todotome.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.Contact;

import java.util.ArrayList;


@Configuration
@EnableSwagger2
@EnableAutoConfiguration
public class Swagger2Config {



    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pl.piomin.microservices.advanced.account.api"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }




    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "ToDoToMe.com REST API",
                "The long awaited targeted event matcher",
                "0.1.0",
                "Terms of service",
                new Contact("John Thompson", "https://springframework.guru/about/", "john@springfrmework.guru"),
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>());
        return apiInfo;
    }


     /*@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                //.paths(PathSelectors.regex("/user.*|/register.*|/oauth/token.*")) //PathSelectors.any() for all
                .build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "aTitle",
                "aDescription",
                "aVersion",
                "a url to terms and services",
                "aContact",
                "a License of API",
                "a license URL");
        return apiInfo;
    }
    */
}