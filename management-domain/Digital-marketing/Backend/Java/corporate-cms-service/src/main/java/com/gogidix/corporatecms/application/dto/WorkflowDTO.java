package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for Workflow entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Workflow DTO for content approval workflows")
public class WorkflowDTO {

    @Schema(description = "Workflow ID")
    private String id;

    @Schema(description = "Content ID")
    private String contentId;

    @Schema(description = "Content type")
    private String contentType;

    @Schema(description = "Content title")
    private String contentTitle;

    @Schema(description = "Workflow status")
    private WorkflowStatus status;

    @Schema(description = "Requested by user ID")
    private String requestedBy;

    @Schema(description = "Requested by user name")
    private String requestedByName;

    @Schema(description = "Request comment")
    private String requestComment;

    @Schema(description = "Current approver ID")
    private String currentApproverId;

    @Schema(description = "Current approver name")
    private String currentApproverName;

    @Schema(description = "Workflow steps")
    private List<WorkflowStepDTO> steps;

    @Schema(description = "Workflow actions")
    private List<WorkflowActionDTO> actions;

    @Schema(description = "Current step index")
    private Integer currentStepIndex;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Due date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    @Schema(description = "Started at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startedAt;

    @Schema(description = "Completed at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completedAt;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowStepDTO {
        private Integer stepNumber;
        private String stepName;
        private String approverRole;
        private String approverId;
        private String approverName;
        private WorkflowStatus status;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startedAt;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime completedAt;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowActionDTO {
        private String actionType;
        private String actorId;
        private String actorName;
        private String comment;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime timestamp;
        private WorkflowStatus fromStatus;
        private WorkflowStatus toStatus;
    }
}
