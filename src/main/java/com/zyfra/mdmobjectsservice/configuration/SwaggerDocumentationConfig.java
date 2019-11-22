package com.zyfra.mdmobjectsservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2019-10-22T07:32:14.959Z")

@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("mdm-objects microservice")
            .description("This is mdm-objects microservice.")
            .license("unlicense.org")
            .licenseUrl("http://unlicense.org")
            .termsOfServiceUrl("")
            .version("0.1")
            .contact(new Contact("","", ""))
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("com.zyfra.mdmobjectsservice"))
                    .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .genericModelSubstitutes(CompletableFuture.class, CompletionStage.class, ResponseEntity.class)
                .forCodeGeneration(true)
                .apiInfo(apiInfo());
    }

}
