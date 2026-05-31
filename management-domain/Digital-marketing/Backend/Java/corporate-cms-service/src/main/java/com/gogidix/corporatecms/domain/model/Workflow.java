package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing content approval workflow.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "workflows")
public class Workflow {

    @Id
    private String id;

    @Indexed
    private String contentId;

    @Indexed
    private String contentType;

    private String contentTitle;

    @Indexed
    private WorkflowStatus status;

    @Indexed
    private String requestedBy;

    private String requestedByName;

    private String requestComment;

    @Indexed
    private String currentApproverId;

    private String currentApproverName;

    @Builder.Default
    private List<WorkflowStep> steps = new ArrayList<>();

    @Builder.Default
    private List<WorkflowAction> actions = new ArrayList<>();

    @Indexed
    private Integer currentStepIndex;

    @Indexed
    private String tenantId;

    private Map<String, Object> metadata;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDateTime dueDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowStep {
        private Integer stepNumber;
        private String stepName;
        private String approverRole;
        private String approverId;
        private String approverName;
        private WorkflowStatus status;
        private LocalDateTime startedAt;
        private LocalDateTime completedAt;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkflowAction {
        private String actionType;
        private String actorId;
        private String actorName;
        private String comment;
        private LocalDateTime timestamp;
        private WorkflowStatus fromStatus;
        private WorkflowStatus toStatus;
    }

    public void addAction(WorkflowAction action) {
        this.actions.add(action);
    }

    public WorkflowStep getCurrentStep() {
        if (currentStepIndex == null || currentStepIndex >= steps.size()) {
            return null;
        }
        return steps.get(currentStepIndex);
    }

    public boolean isCompleted() {
        return status != null && status.isFinal();
    }

    public boolean canApprove(String userId) {
        WorkflowStep currentStep = getCurrentStep();
        return currentStep != null
                && userId != null
                && userId.equals(currentStep.getApproverId())
                && status == WorkflowStatus.PENDING;
    }
}
