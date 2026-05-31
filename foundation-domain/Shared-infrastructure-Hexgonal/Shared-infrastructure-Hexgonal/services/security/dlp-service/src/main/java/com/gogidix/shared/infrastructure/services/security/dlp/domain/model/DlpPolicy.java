package com.gogidix.shared.infrastructure.services.security.dlp.domain.model;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
@Document(collection = "dlp_policies")
public class DlpPolicy {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String policyId;
    @NotBlank
    @Indexed
    private String policyName;
    private String description;
    @Indexed
    private PolicyStatus status = PolicyStatus.ACTIVE;
    private List<String> sensitiveDataPatterns;
    private String action;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    protected DlpPolicy() {}
    public DlpPolicy(TenantId tenantId, String policyName) {
        this.tenantId = tenantId;
        this.policyName = policyName;
        this.policyId = java.util.UUID.randomUUID().toString();
    }
    // Getters and Setters
    public TenantId getTenantId() { return tenantId; }
    public void setTenantId(TenantId tenantId) { this.tenantId = tenantId; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPolicyId() { return policyId; }
    public void setPolicyId(String policyId) { this.policyId = policyId; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public PolicyStatus getStatus() { return status; }
    public void setStatus(PolicyStatus status) { this.status = status; }
    public List<String> getSensitiveDataPatterns() { return sensitiveDataPatterns; }
    public void setSensitiveDataPatterns(List<String> sensitiveDataPatterns) { this.sensitiveDataPatterns = sensitiveDataPatterns; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public enum PolicyStatus {
        ACTIVE, INACTIVE, DRAFT
    }
}
