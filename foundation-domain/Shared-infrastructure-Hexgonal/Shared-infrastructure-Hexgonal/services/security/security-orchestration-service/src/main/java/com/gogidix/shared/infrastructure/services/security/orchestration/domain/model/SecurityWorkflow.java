package com.gogidix.shared.infrastructure.services.security.orchestration.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@Document(collection = "security_workflows")
public class SecurityWorkflow {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String workflowId;
    @NotBlank
    @Indexed
    private String workflowName;
    private String description;
    @Indexed
    private WorkflowStatus status = WorkflowStatus.ACTIVE;
    private String triggerType;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    public SecurityWorkflow() {}
    public SecurityWorkflow(TenantId tenantId, String workflowName) {
        this.tenantId = tenantId;
        this.workflowName = workflowName;
        this.workflowId = java.util.UUID.randomUUID().toString();
    }
    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getWorkflowId() { return workflowId; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public String getWorkflowName() { return workflowName; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public WorkflowStatus getStatus() { return status; }
    public void setStatus(WorkflowStatus status) { this.status = status; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public enum WorkflowStatus {
        ACTIVE, INACTIVE, DRAFT
    }
}
