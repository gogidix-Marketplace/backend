package com.gogidix.hr.employeeselfservice.domain.model;

import com.gogidix.hr.employeeselfservice.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Self Service Request Domain Entity
 * Multi-tenant self-service request management for HR operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "self_service_requests")
public class SelfServiceRequest extends BaseEntity {

    private String tenantId;

    private String requestNumber;

    private String employeeId;

    private String employeeName;

    private RequestType type;

    private String description;

    private RequestStatus status;

    private LocalDate submittedDate;

    private LocalDate reviewedDate;

    private String reviewedBy;

    private String reviewerComments;

    private Map<String, Object> currentValues = new HashMap<>();

    private Map<String, Object> requestedValues = new HashMap<>();

    private List<String> supportingDocuments = new ArrayList<>();

    private String rejectionReason;

    private Integer priority;

    private String assignedTo;

    private LocalDate dueDate;

    private LocalDate completedDate;

    private String completedBy;

    private Map<String, Object> auditTrail = new HashMap<>();

    private List<String> approvers = new ArrayList<>();

    private List<String> approvalChain = new ArrayList<>();

    private Integer currentApprovalStep;

    private String category;

    private String subCategory;

    private Boolean requiresApproval;

    private String workflowId;

    public enum RequestType {
        ADDRESS_CHANGE,
        BANK_DETAILS_CHANGE,
        PERSONAL_INFO_CHANGE,
        DOCUMENT_UPLOAD,
        TAX_INFO_CHANGE,
        EMERGENCY_CONTACT_CHANGE,
        CONTACT_INFO_CHANGE,
        MARITAL_STATUS_CHANGE,
        DEPENDENT_ADDITION,
        DEPENDENT_REMOVAL,
        BENEFITS_ENROLLMENT,
        BENEFITS_CHANGE,
        LEAVE_REQUEST,
        ATTENDANCE_CORRECTION,
        EXPENSE_CLAIM
    }

    public enum RequestStatus {
        PENDING,
        UNDER_REVIEW,
        AWAITING_APPROVAL,
        APPROVED,
        REJECTED,
        CANCELLED,
        WITHDRAWN,
        COMPLETED,
        ON_HOLD,
        INFO_REQUIRED
    }

    /**
     * Creates a new self-service request
     */
    public static SelfServiceRequest create(String tenantId, String requestNumber,
                                              String employeeId, String employeeName,
                                              RequestType type, String description,
                                              Map<String, Object> currentValues,
                                              Map<String, Object> requestedValues) {
        SelfServiceRequest request = new SelfServiceRequest();
        request.setTenantId(tenantId);
        request.setRequestNumber(requestNumber);
        request.setEmployeeId(employeeId);
        request.setEmployeeName(employeeName);
        request.setType(type);
        request.setDescription(description);
        request.setStatus(RequestStatus.PENDING);
        request.setSubmittedDate(LocalDate.now());
        request.setCurrentValues(currentValues != null ? currentValues : new HashMap<>());
        request.setRequestedValues(requestedValues != null ? requestedValues : new HashMap<>());
        request.setSupportingDocuments(new ArrayList<>());
        request.setAuditTrail(new HashMap<>());
        request.setApprovers(new ArrayList<>());
        request.setApprovalChain(new ArrayList<>());
        request.setCurrentApprovalStep(0);
        request.setRequiresApproval(true);
        request.setPriority(0);

        request.addAuditEntry("REQUEST_CREATED", "Request submitted by " + employeeName);
        return request;
    }

    /**
     * Submits the request
     */
    public void submit() {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Can only submit pending requests");
        }
        this.status = RequestStatus.UNDER_REVIEW;
        this.submittedDate = LocalDate.now();
        addAuditEntry("REQUEST_SUBMITTED", "Request submitted for review");
    }

    /**
     * Approves the request
     */
    public void approve(String reviewedBy, String comments) {
        if (this.status == RequestStatus.APPROVED || this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Request is already approved or completed");
        }
        if (this.status == RequestStatus.CANCELLED || this.status == RequestStatus.REJECTED) {
            throw new IllegalStateException("Cannot approve cancelled or rejected request");
        }

        this.status = RequestStatus.APPROVED;
        this.reviewedDate = LocalDate.now();
        this.reviewedBy = reviewedBy;
        this.reviewerComments = comments;
        addAuditEntry("REQUEST_APPROVED", "Request approved by " + reviewedBy + (comments != null ? ": " + comments : ""));
    }

    /**
     * Rejects the request
     */
    public void reject(String reviewedBy, String reason) {
        if (this.status == RequestStatus.APPROVED || this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot reject approved or completed request");
        }
        if (this.status == RequestStatus.CANCELLED) {
            throw new IllegalStateException("Request is already cancelled");
        }

        this.status = RequestStatus.REJECTED;
        this.reviewedDate = LocalDate.now();
        this.reviewedBy = reviewedBy;
        this.rejectionReason = reason;
        addAuditEntry("REQUEST_REJECTED", "Request rejected by " + reviewedBy + ": " + reason);
    }

    /**
     * Cancels the request
     */
    public void cancel(String cancelledBy, String reason) {
        if (this.status == RequestStatus.APPROVED || this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel approved or completed request");
        }
        if (this.status == RequestStatus.CANCELLED) {
            throw new IllegalStateException("Request is already cancelled");
        }

        this.status = RequestStatus.CANCELLED;
        addAuditEntry("REQUEST_CANCELLED", "Request cancelled by " + cancelledBy + (reason != null ? ": " + reason : ""));
    }

    /**
     * Withdraws the request
     */
    public void withdraw(String withdrawnBy, String reason) {
        if (this.status == RequestStatus.APPROVED || this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot withdraw approved or completed request");
        }
        if (this.status == RequestStatus.CANCELLED || this.status == RequestStatus.WITHDRAWN) {
            throw new IllegalStateException("Request is already cancelled or withdrawn");
        }

        this.status = RequestStatus.WITHDRAWN;
        addAuditEntry("REQUEST_WITHDRAWN", "Request withdrawn by " + withdrawnBy + (reason != null ? ": " + reason : ""));
    }

    /**
     * Puts the request on hold
     */
    public void putOnHold(String reviewedBy, String reason) {
        if (this.status == RequestStatus.CANCELLED || this.status == RequestStatus.REJECTED ||
                this.status == RequestStatus.WITHDRAWN || this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot put request in current status on hold");
        }

        this.status = RequestStatus.ON_HOLD;
        addAuditEntry("REQUEST_ON_HOLD", "Request put on hold by " + reviewedBy + (reason != null ? ": " + reason : ""));
    }

    /**
     * Resumes the request from hold
     */
    public void resume(String resumedBy) {
        if (this.status != RequestStatus.ON_HOLD) {
            throw new IllegalStateException("Can only resume requests that are on hold");
        }

        this.status = RequestStatus.UNDER_REVIEW;
        addAuditEntry("REQUEST_RESUMED", "Request resumed by " + resumedBy);
    }

    /**
     * Requests additional information
     */
    public void requestInformation(String reviewedBy, String informationRequired) {
        if (this.status == RequestStatus.CANCELLED || this.status == RequestStatus.REJECTED ||
                this.status == RequestStatus.WITHDRAWN || this.status == RequestStatus.COMPLETED) {
            throw new IllegalStateException("Cannot request information for request in current status");
        }

        this.status = RequestStatus.INFO_REQUIRED;
        addAuditEntry("INFO_REQUESTED", "Additional information requested by " + reviewedBy + ": " + informationRequired);
    }

    /**
     * Submits additional information
     */
    public void submitInformation(String submittedBy, String information) {
        if (this.status != RequestStatus.INFO_REQUIRED) {
            throw new IllegalStateException("Can only submit information when info is required");
        }

        this.status = RequestStatus.UNDER_REVIEW;
        addAuditEntry("INFO_SUBMITTED", "Additional information submitted by " + submittedBy + ": " + information);
    }

    /**
     * Completes the request
     */
    public void complete(String completedBy) {
        if (this.status != RequestStatus.APPROVED) {
            throw new IllegalStateException("Can only complete approved requests");
        }

        this.status = RequestStatus.COMPLETED;
        this.completedDate = LocalDate.now();
        this.completedBy = completedBy;
        addAuditEntry("REQUEST_COMPLETED", "Request completed by " + completedBy);
    }

    /**
     * Assigns the request to someone
     */
    public void assignTo(String assignedTo, String assignedBy) {
        this.assignedTo = assignedTo;
        addAuditEntry("REQUEST_ASSIGNED", "Request assigned to " + assignedTo + " by " + assignedBy);
    }

    /**
     * Sets the priority
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * Sets the due date
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Adds a supporting document
     */
    public void addSupportingDocument(String documentUrl) {
        if (this.supportingDocuments == null) {
            this.supportingDocuments = new ArrayList<>();
        }
        if (!this.supportingDocuments.contains(documentUrl)) {
            this.supportingDocuments.add(documentUrl);
        }
    }

    /**
     * Removes a supporting document
     */
    public void removeSupportingDocument(String documentUrl) {
        if (this.supportingDocuments != null) {
            this.supportingDocuments.remove(documentUrl);
        }
    }

    /**
     * Adds an approver to the approval chain
     */
    public void addApprover(String approver) {
        if (this.approvalChain == null) {
            this.approvalChain = new ArrayList<>();
        }
        if (!this.approvalChain.contains(approver)) {
            this.approvalChain.add(approver);
        }
    }

    /**
     * Sets the approval chain
     */
    public void setApprovalChain(List<String> approvers) {
        this.approvalChain = approvers != null ? new ArrayList<>(approvers) : new ArrayList<>();
        this.currentApprovalStep = 0;
    }

    /**
     * Advances to next approval step
     */
    public void advanceApprovalStep() {
        if (this.approvalChain != null && this.currentApprovalStep < this.approvalChain.size() - 1) {
            this.currentApprovalStep++;
            if (this.currentApprovalStep >= this.approvalChain.size()) {
                this.status = RequestStatus.APPROVED;
            }
        }
    }

    /**
     * Adds an audit entry
     */
    public void addAuditEntry(String action, String description) {
        if (this.auditTrail == null) {
            this.auditTrail = new HashMap<>();
        }
        String key = action + "_" + System.currentTimeMillis();
        this.auditTrail.put(key, new AuditEntry(action, description, LocalDate.now().toString()));
    }

    /**
     * Checks if request is pending
     */
    public boolean isPending() {
        return this.status == RequestStatus.PENDING;
    }

    /**
     * Checks if request is approved
     */
    public boolean isApproved() {
        return this.status == RequestStatus.APPROVED || this.status == RequestStatus.COMPLETED;
    }

    /**
     * Checks if request is rejected
     */
    public boolean isRejected() {
        return this.status == RequestStatus.REJECTED;
    }

    /**
     * Checks if request can be modified
     */
    public boolean canBeModified() {
        return this.status == RequestStatus.PENDING ||
                this.status == RequestStatus.UNDER_REVIEW ||
                this.status == RequestStatus.INFO_REQUIRED;
    }

    /**
     * Checks if request is overdue
     */
    public boolean isOverdue() {
        return this.dueDate != null && LocalDate.now().isAfter(this.dueDate) &&
                !this.isCompleted();
    }

    /**
     * Checks if request is completed
     */
    public boolean isCompleted() {
        return this.status == RequestStatus.COMPLETED;
    }

    /**
     * Gets the category based on request type
     */
    public String getCategory() {
        if (this.category != null) {
            return this.category;
        }
        return deriveCategoryFromType(this.type);
    }

    /**
     * Derives category from request type
     */
    private String deriveCategoryFromType(RequestType type) {
        if (type == null) {
            return "GENERAL";
        }
        return switch (type) {
            case ADDRESS_CHANGE, CONTACT_INFO_CHANGE, PERSONAL_INFO_CHANGE, EMERGENCY_CONTACT_CHANGE, MARITAL_STATUS_CHANGE -> "PERSONAL";
            case BANK_DETAILS_CHANGE -> "PAYROLL";
            case DOCUMENT_UPLOAD -> "DOCUMENTS";
            case TAX_INFO_CHANGE -> "TAX";
            case DEPENDENT_ADDITION, DEPENDENT_REMOVAL, BENEFITS_ENROLLMENT, BENEFITS_CHANGE -> "BENEFITS";
            case LEAVE_REQUEST, ATTENDANCE_CORRECTION -> "LEAVE";
            case EXPENSE_CLAIM -> "EXPENSE";
        };
    }

    /**
     * Sets the workflow
     */
    public void setWorkflow(String workflowId, List<String> approvers, Boolean requiresApproval) {
        this.workflowId = workflowId;
        this.approvalChain = approvers != null ? new ArrayList<>(approvers) : new ArrayList<>();
        this.requiresApproval = requiresApproval != null ? requiresApproval : true;
        this.currentApprovalStep = 0;
    }

    /**
     * Audit entry inner class
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditEntry {
        private String action;
        private String description;
        private String timestamp;
    }
}
