package com.example.off.common.config;

import com.example.off.common.annotation.CustomExceptionDescription;
import com.example.off.common.response.ResponseCode;
import com.example.off.common.swagger.ExampleHolder;
import com.example.off.common.swagger.SwaggerResponseDescription;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .components(components);
    }
    private Info apiInfo() {
        return new Info()
                .title("OFF API 명세서") // API의 제목
                .description("OFF의 API 명세서 입니다!") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            CustomExceptionDescription customExceptionDescription = handlerMethod.getMethodAnnotation(
                    CustomExceptionDescription.class);

            // CustomExceptionDescription 어노테이션 단 메소드 적용
            if (customExceptionDescription != null) {
                generateErrorCodeResponseExample(operation, customExceptionDescription.value());
            }

            return operation;
        };
    }


    private void generateErrorCodeResponseExample(
            Operation operation, SwaggerResponseDescription type) {

        ApiResponses responses = operation.getResponses();

        Set<ResponseCode> responseCodeSet = type.getResponseCodeSet();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders =
                responseCodeSet.stream()
                        .map(
                                responseCode -> {
                                    return ExampleHolder.builder()
                                            .holder(
                                                    getSwaggerExample(responseCode))
                                            .code(responseCode.getCode())
                                            .name(responseCode.toString())
                                            .build();
                                }
                        ).collect(groupingBy(ExampleHolder::getCode));
        addExamplesToResponses(responses, statusWithExampleHolders);
    }


    private Example getSwaggerExample(ResponseCode responseCode) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("success", responseCode.isSuccess());
        responseMap.put("code", responseCode.getCode());
        responseMap.put("message", responseCode.getMessage());
        responseMap.put("result", null); // null 안전하게 허용됨

        Example example = new Example();
        example.description(responseCode.getMessage());
        example.setValue(responseMap);

        return example;
    }


    private void addExamplesToResponses(
            ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();
                    v.forEach(
                            exampleHolder -> {
                                mediaType.addExamples(
                                        exampleHolder.getName(), exampleHolder.getHolder());
                            });
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setDescription("");
                    apiResponse.setContent(content);
                    responses.addApiResponse(status.toString(), apiResponse);
                });
    }
}