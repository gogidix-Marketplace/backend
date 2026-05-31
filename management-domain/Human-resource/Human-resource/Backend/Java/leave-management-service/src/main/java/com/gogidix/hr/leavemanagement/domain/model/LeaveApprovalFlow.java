package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.enums.ApprovalStatus;
import com.gogidix.hr.leavemanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Leave Approval Flow
 * Tracks the approval workflow for leave requests
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApprovalFlow extends BaseEntity {

    private String tenantId;
    private String flowId;
    private String requestId;
    private String requestIdEmployeeId;
    private String leaveType;
    private Integer currentLevel;
    private List<ApprovalLevel> approvalLevels = new ArrayList<>();
    private List<ApprovalHistory> history = new ArrayList<>();
    private String flowStatus;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private String initiatedBy;
    private String completedBy;
    private Boolean requiresReapproval;
    private String reapprovalReason;

    /**
     * Initialize approval flow with levels
     */
    public void initializeFlow(List<ApprovalLevelConfig> configs) {
        this.approvalLevels = new ArrayList<>();
        this.currentLevel = 0;
        this.initiatedAt = LocalDateTime.now();

        int level = 1;
        for (ApprovalLevelConfig config : configs) {
            ApprovalLevel approvalLevel = ApprovalLevel.builder()
                    .level(level++)
                    .approverId(config.getApproverId())
                    .approverName(config.getApproverName())
                    .approverRole(config.getApproverRole())
                    .status(ApprovalStatus.PENDING)
                    .required(config.getRequired())
                    .build();
            this.approvalLevels.add(approvalLevel);
        }

        this.flowStatus = "INITIATED";
    }

    /**
     * Approve current level
     */
    public void approveLevel(String approverId, String comments) {
        ApprovalLevel current = getCurrentApprovalLevel();
        if (current == null) {
            throw new IllegalStateException("No pending approval level found");
        }

        if (!current.getApproverId().equals(approverId)) {
            throw new IllegalStateException("User is not the approver for this level");
        }

        current.setStatus(ApprovalStatus.APPROVED);
        current.setApprovedAt(LocalDateTime.now());
        current.setComments(comments);

        // Add to history
        addToHistory(current);

        // Move to next level or complete
        if (currentLevel >= approvalLevels.size() || allRequiredLevelsApproved()) {
            this.flowStatus = "APPROVED";
            this.completedAt = LocalDateTime.now();
        } else {
            this.currentLevel++;
        }
    }

    /**
     * Reject at current level
     */
    public void rejectLevel(String approverId, String reason) {
        ApprovalLevel current = getCurrentApprovalLevel();
        if (current == null) {
            throw new IllegalStateException("No pending approval level found");
        }

        if (!current.getApproverId().equals(approverId)) {
            throw new IllegalStateException("User is not the approver for this level");
        }

        current.setStatus(ApprovalStatus.REJECTED);
        current.setApprovedAt(LocalDateTime.now());
        current.setComments(reason);

        // Add to history
        addToHistory(current);

        this.flowStatus = "REJECTED";
        this.completedAt = LocalDateTime.now();
    }

    /**
     * Get current approval level
     */
    public ApprovalLevel getCurrentApprovalLevel() {
        if (currentLevel == null || currentLevel >= approvalLevels.size()) {
            return null;
        }
        return approvalLevels.get(currentLevel);
    }

    /**
     * Check if all required levels are approved
     */
    public boolean allRequiredLevelsApproved() {
        return approvalLevels.stream()
                .filter(ApprovalLevel::getRequired)
                .allMatch(l -> l.getStatus() == ApprovalStatus.APPROVED);
    }

    /**
     * Check if flow is completed
     */
    public boolean isCompleted() {
        return "APPROVED".equals(flowStatus) || "REJECTED".equals(flowStatus);
    }

    /**
     * Check if user can approve
     */
    public boolean canApprove(String userId) {
        ApprovalLevel current = getCurrentApprovalLevel();
        return current != null &&
                current.getApproverId().equals(userId) &&
                current.getStatus() == ApprovalStatus.PENDING;
    }

    /**
     * Get next pending approver
     */
    public ApprovalLevel getNextPendingApprover() {
        return approvalLevels.stream()
                .filter(l -> l.getStatus() == ApprovalStatus.PENDING)
                .findFirst()
                .orElse(null);
    }

    /**
     * Add to approval history
     */
    private void addToHistory(ApprovalLevel level) {
        if (this.history == null) {
            this.history = new ArrayList<>();
        }

        ApprovalHistory historyItem = ApprovalHistory.builder()
                .level(level.getLevel())
                .approverId(level.getApproverId())
                .approverName(level.getApproverName())
                .status(level.getStatus())
                .timestamp(level.getApprovedAt())
                .comments(level.getComments())
                .build();

        this.history.add(historyItem);
    }

    /**
     * Reset flow for reapproval
     */
    public void resetForReapproval(String reason) {
        this.requiresReapproval = true;
        this.reapprovalReason = reason;
        this.currentLevel = 0;
        this.flowStatus = "PENDING";

        // Reset all levels to pending
        approvalLevels.forEach(l -> {
            l.setStatus(ApprovalStatus.PENDING);
            l.setApprovedAt(null);
            l.setComments(null);
        });
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalLevel {
        private Integer level;
        private String approverId;
        private String approverName;
        private String approverRole;
        private ApprovalStatus status;
        private Boolean required;
        private LocalDateTime approvedAt;
        private String comments;
        private Boolean skipIfUnavailable;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalHistory {
        private Integer level;
        private String approverId;
        private String approverName;
        private ApprovalStatus status;
        private LocalDateTime timestamp;
        private String comments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApprovalLevelConfig {
        private String approverId;
        private String approverName;
        private String approverRole;
        private Boolean required;
    }
}
