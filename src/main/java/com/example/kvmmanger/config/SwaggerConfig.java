package com.example.kvmmanger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

import static com.example.kvmmanger.common.contant.SecurityConstant.HEADER;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        //添加head参数配置
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(HEADER)
                .description("令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiW10iLCJleHAiOjE1Njc2NDUzMDJ9.LjFuJg5nP8bOg4COIMRMCzyE70hkXMBLG8eEM8MAgTrQvylGoKwifEkuMJROiCQK27upNWzyDXTGBKiazIYV-Q")
                .required(false).build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.kvmmanger.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);//添加默认参数

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("KVM管理api")
                .description("api")
//                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
