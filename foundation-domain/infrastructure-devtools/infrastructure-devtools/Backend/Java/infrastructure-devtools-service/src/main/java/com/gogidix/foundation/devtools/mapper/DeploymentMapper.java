package com.gogidix.foundation.devtools.mapper;

import com.gogidix.foundation.devtools.domain.entity.DeploymentJob;
import com.gogidix.foundation.devtools.domain.entity.DeploymentExecution;
import com.gogidix.foundation.devtools.dto.DeploymentJobDto;
import com.gogidix.foundation.devtools.dto.DeploymentExecutionDto;
import org.springframework.stereotype.Component;

/**
 * Mapper for deployment entities and DTOs.
 */
@Component
public class DeploymentMapper {

    public DeploymentJobDto toDto(DeploymentJob entity) {
        if (entity == null) {
            return null;
        }

        return DeploymentJobDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .name(entity.getName())
                .description(entity.getDescription())
                .projectId(entity.getProjectId())
                .type(entity.getType())
                .targetEnvironment(entity.getTargetEnvironment())
                .deploymentScript(entity.getDeploymentScript())
                .preDeploymentScript(entity.getPreDeploymentScript())
                .postDeploymentScript(entity.getPostDeploymentScript())
                .rollbackScript(entity.getRollbackScript())
                .timeout(entity.getTimeout())
                .retryCount(entity.getRetryCount())
                .enabled(entity.getEnabled())
                .tags(entity.getTags())
                .build();
    }

    public DeploymentJob toEntity(DeploymentJobDto dto) {
        if (dto == null) {
            return null;
        }

        return DeploymentJob.builder()
                .uuid(dto.getUuid())
                .name(dto.getName())
                .description(dto.getDescription())
                .projectId(dto.getProjectId())
                .type(dto.getType())
                .targetEnvironment(dto.getTargetEnvironment())
                .deploymentScript(dto.getDeploymentScript())
                .preDeploymentScript(dto.getPreDeploymentScript())
                .postDeploymentScript(dto.getPostDeploymentScript())
                .rollbackScript(dto.getRollbackScript())
                .timeout(dto.getTimeout())
                .retryCount(dto.getRetryCount())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .tags(dto.getTags())
                .build();
    }

    public void updateEntityFromDto(DeploymentJobDto dto, DeploymentJob entity) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getType() != null) entity.setType(dto.getType());
        if (dto.getTargetEnvironment() != null) entity.setTargetEnvironment(dto.getTargetEnvironment());
        if (dto.getDeploymentScript() != null) entity.setDeploymentScript(dto.getDeploymentScript());
        if (dto.getPreDeploymentScript() != null) entity.setPreDeploymentScript(dto.getPreDeploymentScript());
        if (dto.getPostDeploymentScript() != null) entity.setPostDeploymentScript(dto.getPostDeploymentScript());
        if (dto.getRollbackScript() != null) entity.setRollbackScript(dto.getRollbackScript());
        if (dto.getTimeout() != null) entity.setTimeout(dto.getTimeout());
        if (dto.getRetryCount() != null) entity.setRetryCount(dto.getRetryCount());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        if (dto.getTags() != null) entity.setTags(dto.getTags());
    }

    public DeploymentExecutionDto toExecutionDto(DeploymentExecution entity) {
        if (entity == null) {
            return null;
        }

        return DeploymentExecutionDto.builder()
                .id(entity.getId())
                .uuid(entity.getUuid())
                .jobId(entity.getJobId())
                .status(entity.getStatus())
                .version(entity.getDeploymentVersion())
                .commitSha(entity.getCommitSha())
                .outputLog(entity.getOutputLog())
                .errorLog(entity.getErrorLog())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .duration(entity.getDuration())
                .executedBy(entity.getExecutedBy())
                .executedAt(entity.getExecutedAt())
                .build();
    }
}
