package com.gogidix.foundation.devtools.mapper;

import com.gogidix.foundation.devtools.domain.entity.DevToolLogEntry;
import com.gogidix.foundation.devtools.dto.LogEntryDto;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Mapper for log entry entities and DTOs.
 */
@Component
public class LogEntryMapper {

    public LogEntryDto toDto(DevToolLogEntry entity) {
        if (entity == null) {
            return null;
        }

        return LogEntryDto.builder()
                .uuid(entity.getUuid())
                .level(entity.getLevel())
                .source(entity.getSource())
                .category(entity.getCategory())
                .message(entity.getMessage())
                .stackTrace(entity.getStackTrace())
                .context(parseContext(entity.getContext()))
                .userId(entity.getUserId())
                .sessionId(entity.getSessionId())
                .requestId(entity.getRequestId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public DevToolLogEntry toEntity(LogEntryDto dto) {
        if (dto == null) {
            return null;
        }

        DevToolLogEntry entity = new DevToolLogEntry();
        entity.setLevel(dto.getLevel());
        entity.setSource(dto.getSource());
        entity.setCategory(dto.getCategory());
        entity.setMessage(dto.getMessage());
        entity.setStackTrace(dto.getStackTrace());
        entity.setContext(stringifyContext(dto.getContext()));
        entity.setUserId(dto.getUserId());
        entity.setSessionId(dto.getSessionId());
        entity.setRequestId(dto.getRequestId());

        return entity;
    }

    private Map<String, Object> parseContext(String contextJson) {
        try {
            if (contextJson == null || contextJson.isBlank()) {
                return null;
            }
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(contextJson,
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return null;
        }
    }

    private String stringifyContext(Map<String, Object> context) {
        try {
            if (context == null || context.isEmpty()) {
                return null;
            }
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(context);
        } catch (Exception e) {
            return null;
        }
    }
}
