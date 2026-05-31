package com.gogidix.foundation.devtools.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.domain.entity.ApiTestCase;
import com.gogidix.foundation.devtools.domain.entity.ApiTestExecution;
import com.gogidix.foundation.devtools.dto.ApiTestCaseDto;
import com.gogidix.foundation.devtools.dto.ApiTestExecutionDto;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mapper for API testing entities and DTOs.
 */
@Component
public class ApiTestMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ApiTestCaseDto toDto(ApiTestCase entity) {
        if (entity == null) {
            return null;
        }

        return ApiTestCaseDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .projectId(entity.getProjectId())
                .method(entity.getMethod())
                .url(entity.getUrl())
                .headers(parseHeaders(entity.getHeaders()))
                .requestBody(entity.getRequestBody())
                .expectedStatusCode(entity.getExpectedStatusCode())
                .expectedResponseBody(entity.getExpectedResponseBody())
                .validationScript(entity.getValidationScript())
                .enabled(entity.getEnabled())
                .environment(entity.getEnvironment())
                .tags(entity.getTags())
                .timeout(entity.getTimeout())
                .createdBy(entity.getCreatedBy())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }

    public ApiTestCase toEntity(ApiTestCaseDto dto) {
        if (dto == null) {
            return null;
        }

        return ApiTestCase.builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .description(dto.getDescription())
                .projectId(dto.getProjectId())
                .method(dto.getMethod())
                .url(dto.getUrl())
                .headers(stringifyHeaders(dto.getHeaders()))
                .requestBody(dto.getRequestBody())
                .expectedStatusCode(dto.getExpectedStatusCode())
                .expectedResponseBody(dto.getExpectedResponseBody())
                .validationScript(dto.getValidationScript())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .environment(dto.getEnvironment())
                .tags(dto.getTags())
                .timeout(dto.getTimeout() != null ? dto.getTimeout() : 30000)
                .build();
    }

    public void updateEntityFromDto(ApiTestCaseDto dto, ApiTestCase entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getMethod() != null) entity.setMethod(dto.getMethod());
        if (dto.getUrl() != null) entity.setUrl(dto.getUrl());
        if (dto.getHeaders() != null) entity.setHeaders(stringifyHeaders(dto.getHeaders()));
        if (dto.getRequestBody() != null) entity.setRequestBody(dto.getRequestBody());
        if (dto.getExpectedStatusCode() != null) entity.setExpectedStatusCode(dto.getExpectedStatusCode());
        if (dto.getExpectedResponseBody() != null) entity.setExpectedResponseBody(dto.getExpectedResponseBody());
        if (dto.getValidationScript() != null) entity.setValidationScript(dto.getValidationScript());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        if (dto.getEnvironment() != null) entity.setEnvironment(dto.getEnvironment());
        if (dto.getTags() != null) entity.setTags(dto.getTags());
        if (dto.getTimeout() != null) entity.setTimeout(dto.getTimeout());
    }

    public ApiTestExecutionDto toExecutionDto(ApiTestExecution entity) {
        if (entity == null) {
            return null;
        }

        return ApiTestExecutionDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .testCaseId(entity.getTestCaseId())
                .status(entity.getStatus())
                .actualStatusCode(entity.getActualStatusCode())
                .responseBody(entity.getResponseBody())
                .errorMessage(entity.getErrorMessage())
                .responseTime(entity.getResponseTime())
                .executionTime(entity.getExecutionTime())
                .executedBy(entity.getExecutedBy())
                .executedAt(entity.getExecutedAt())
                .environment(entity.getEnvironment())
                .build();
    }

    private Map<String, String> parseHeaders(String headersJson) {
        try {
            if (headersJson == null || headersJson.isBlank()) {
                return null;
            }
            return objectMapper.readValue(headersJson,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
        } catch (Exception e) {
            return null;
        }
    }

    private String stringifyHeaders(Map<String, String> headers) {
        try {
            if (headers == null || headers.isEmpty()) {
                return null;
            }
            return objectMapper.writeValueAsString(headers);
        } catch (Exception e) {
            return null;
        }
    }
}
