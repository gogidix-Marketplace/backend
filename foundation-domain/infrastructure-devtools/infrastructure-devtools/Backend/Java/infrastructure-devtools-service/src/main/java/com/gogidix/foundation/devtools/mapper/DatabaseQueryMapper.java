package com.gogidix.foundation.devtools.mapper;

import com.gogidix.foundation.devtools.domain.entity.DatabaseQuery;
import com.gogidix.foundation.devtools.domain.entity.DatabaseQueryExecution;
import com.gogidix.foundation.devtools.dto.DatabaseQueryDto;
import com.gogidix.foundation.devtools.dto.DatabaseQueryExecutionDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for database query entities and DTOs.
 */
@Component
public class DatabaseQueryMapper {

    public DatabaseQueryDto toDto(DatabaseQuery entity) {
        if (entity == null) {
            return null;
        }

        return DatabaseQueryDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .projectId(entity.getProjectId())
                .databaseName(entity.getDatabaseName())
                .query(entity.getQuery())
                .queryType(entity.getQueryType())
                .enabled(entity.getEnabled())
                .tags(entity.getTags())
                .maxRows(entity.getMaxRows())
                .timeoutSeconds(entity.getTimeoutSeconds())
                .build();
    }

    public DatabaseQuery toEntity(DatabaseQueryDto dto) {
        if (dto == null) {
            return null;
        }

        return DatabaseQuery.builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .description(dto.getDescription())
                .projectId(dto.getProjectId())
                .databaseName(dto.getDatabaseName())
                .query(dto.getQuery())
                .queryType(dto.getQueryType())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .tags(dto.getTags())
                .maxRows(dto.getMaxRows())
                .timeoutSeconds(dto.getTimeoutSeconds())
                .build();
    }

    public void updateEntityFromDto(DatabaseQueryDto dto, DatabaseQuery entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getDatabaseName() != null) entity.setDatabaseName(dto.getDatabaseName());
        if (dto.getQuery() != null) entity.setQuery(dto.getQuery());
        if (dto.getQueryType() != null) entity.setQueryType(dto.getQueryType());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        if (dto.getTags() != null) entity.setTags(dto.getTags());
        if (dto.getMaxRows() != null) entity.setMaxRows(dto.getMaxRows());
        if (dto.getTimeoutSeconds() != null) entity.setTimeoutSeconds(dto.getTimeoutSeconds());
    }

    public DatabaseQueryExecutionDto toExecutionDto(DatabaseQueryExecution entity) {
        if (entity == null) {
            return null;
        }

        return DatabaseQueryExecutionDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .queryId(entity.getQueryId())
                .status(entity.getStatus())
                .rowsAffected(entity.getRowsAffected())
                .rowsReturned(entity.getRowsReturned())
                .errorMessage(entity.getErrorMessage())
                .executionTime(entity.getExecutionTime())
                .executedBy(entity.getExecutedBy())
                .executedAt(entity.getExecutedAt())
                .build();
    }
}
