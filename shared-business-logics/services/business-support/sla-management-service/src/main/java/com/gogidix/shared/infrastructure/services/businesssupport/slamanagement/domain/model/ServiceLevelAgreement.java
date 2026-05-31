package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model;
import com.gogidix.shared.multitenancy.model.TenantId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
/**
 * Service Level Agreement entity with multi-tenant support.
 */
@Document(collection = "service_level_agreements")
public class ServiceLevelAgreement {
    @Indexed
    private TenantId tenantId;
    @Id
    private String id;
    @Indexed
    private String name;
    private String description;
    @Indexed
    private String serviceType;
    private double responseTimeThreshold; // in milliseconds
    private double uptimePercentage; // e.g., 99.9 for 99.9%
    private int penaltyPercentage;
    @Indexed
    private String status; // ACTIVE, INACTIVE, EXPIRED
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    protected ServiceLevelAgreement() {
        // MongoDB
    }
    public ServiceLevelAgreement(TenantId tenantId, String name, String description, String serviceType,
                                 double responseTimeThreshold, double uptimePercentage,
                                 int penaltyPercentage, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.tenantId = tenantId;
        this.name = name;
        this.description = description;
        this.serviceType = serviceType;
        this.responseTimeThreshold = responseTimeThreshold;
        this.uptimePercentage = uptimePercentage;
        this.penaltyPercentage = penaltyPercentage;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.status = "ACTIVE";
    }
    // Getters and setters
    public TenantId getTenantId() {
        return tenantId;
    }
    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getServiceType() {
        return serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
    public double getResponseTimeThreshold() {
        return responseTimeThreshold;
    }
    public void setResponseTimeThreshold(double responseTimeThreshold) {
        this.responseTimeThreshold = responseTimeThreshold;
    }
    public double getUptimePercentage() {
        return uptimePercentage;
    }
    public void setUptimePercentage(double uptimePercentage) {
        this.uptimePercentage = uptimePercentage;
    }
    public int getPenaltyPercentage() {
        return penaltyPercentage;
    }
    public void setPenaltyPercentage(int penaltyPercentage) {
        this.penaltyPercentage = penaltyPercentage;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getValidFrom() {
        return validFrom;
    }
    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }
    public LocalDateTime getValidUntil() {
        return validUntil;
    }
    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
