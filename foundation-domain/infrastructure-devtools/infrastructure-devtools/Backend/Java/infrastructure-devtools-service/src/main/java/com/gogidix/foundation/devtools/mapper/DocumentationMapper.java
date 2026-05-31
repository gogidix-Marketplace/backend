package com.gogidix.foundation.devtools.mapper;

import com.gogidix.foundation.devtools.domain.entity.DocumentationProject;
import com.gogidix.foundation.devtools.domain.entity.DocumentationGeneration;
import com.gogidix.foundation.devtools.dto.DocumentationProjectDto;
import com.gogidix.foundation.devtools.dto.DocumentationGenerationDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for documentation entities and DTOs.
 */
@Component
public class DocumentationMapper {

    public DocumentationProjectDto toDto(DocumentationProject entity) {
        if (entity == null) {
            return null;
        }

        return DocumentationProjectDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .projectId(entity.getProjectId())
                .sourceUrl(entity.getSourceUrl())
                .sourcePath(entity.getSourcePath())
                .outputFormat(entity.getOutputFormat())
                .enabled(entity.getEnabled())
                .autoGenerateInterval(entity.getAutoGenerateInterval())
                .build();
    }

    public DocumentationProject toEntity(DocumentationProjectDto dto) {
        if (dto == null) {
            return null;
        }

        return DocumentationProject.builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .description(dto.getDescription())
                .projectId(dto.getProjectId())
                .sourceUrl(dto.getSourceUrl())
                .sourcePath(dto.getSourcePath())
                .outputFormat(dto.getOutputFormat())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .autoGenerateInterval(dto.getAutoGenerateInterval())
                .build();
    }

    public void updateEntityFromDto(DocumentationProjectDto dto, DocumentationProject entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getSourceUrl() != null) entity.setSourceUrl(dto.getSourceUrl());
        if (dto.getSourcePath() != null) entity.setSourcePath(dto.getSourcePath());
        if (dto.getOutputFormat() != null) entity.setOutputFormat(dto.getOutputFormat());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        if (dto.getAutoGenerateInterval() != null) entity.setAutoGenerateInterval(dto.getAutoGenerateInterval());
    }

    public DocumentationGenerationDto toGenerationDto(DocumentationGeneration entity) {
        if (entity == null) {
            return null;
        }

        return DocumentationGenerationDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .projectId(entity.getProjectId())
                .status(entity.getStatus())
                .outputPath(entity.getOutputPath())
                .pageCount(entity.getPageCount())
                .errorMessage(entity.getErrorMessage())
                .generationTime(entity.getGenerationTime())
                .generatedBy(entity.getGeneratedBy())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
