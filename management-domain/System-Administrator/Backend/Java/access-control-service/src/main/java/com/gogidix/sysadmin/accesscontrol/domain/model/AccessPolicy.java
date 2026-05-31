package com.gogidix.sysadmin.accesscontrol.domain.model;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "access_policies")
@Builder(toBuilder = true)
@AllArgsConstructor
public class AccessPolicy {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String name;
    private String description;
    private PolicyType type;
    private PolicyStatus status;
    private List<PolicyStatement> statements;
    private List<String> principals;
    private List<String> resources;
    private Map<String, String> conditions;
    private Integer priority;
    private Instant effectFrom;
    private Instant effectTo;
    private String createdBy;
    private String lastModifiedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, String> tags;

    public enum PolicyType {
        ALLOW, DENY, ROLE_BASED, ATTRIBUTE_BASED
    }

    public enum PolicyStatus {
        ACTIVE, INACTIVE, DRAFT, ARCHIVED
    }

    @lombok.Builder(toBuilder = true)
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class PolicyStatement {
        private String action;
        private String effect;
        private List<String> conditions;

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }
        public String getEffect() { return effect; }
        public void setEffect(String effect) { this.effect = effect; }
        public List<String> getConditions() { return conditions; }
        public void setConditions(List<String> conditions) { this.conditions = conditions; }
    }

    public AccessPolicy() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = PolicyStatus.ACTIVE;
        this.priority = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public PolicyType getType() { return type; }
    public void setType(PolicyType type) { this.type = type; }

    public PolicyStatus getStatus() { return status; }
    public void setStatus(PolicyStatus status) { this.status = status; }

    public List<PolicyStatement> getStatements() { return statements; }
    public void setStatements(List<PolicyStatement> statements) { this.statements = statements; }

    public List<String> getPrincipals() { return principals; }
    public void setPrincipals(List<String> principals) { this.principals = principals; }

    public List<String> getResources() { return resources; }
    public void setResources(List<String> resources) { this.resources = resources; }

    public Map<String, String> getConditions() { return conditions; }
    public void setConditions(Map<String, String> conditions) { this.conditions = conditions; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Instant getEffectFrom() { return effectFrom; }
    public void setEffectFrom(Instant effectFrom) { this.effectFrom = effectFrom; }

    public Instant getEffectTo() { return effectTo; }
    public void setEffectTo(Instant effectTo) { this.effectTo = effectTo; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public void updateTimestamp() {
        this.updatedAt = Instant.now();
    }

    public static AccessPolicy create(String tenantId, String name, String description, PolicyType type, String createdBy) {
        AccessPolicy policy = new AccessPolicy();
        policy.tenantId = tenantId;
        policy.name = name;
        policy.description = description;
        policy.type = type;
        policy.createdBy = createdBy;
        policy.lastModifiedBy = createdBy;
        policy.id = java.util.UUID.randomUUID().toString();
        policy.status = PolicyStatus.ACTIVE;
        policy.priority = 0;
        policy.createdAt = Instant.now();
        policy.updatedAt = Instant.now();
        policy.statements = new java.util.ArrayList<>();
        policy.principals = new java.util.ArrayList<>();
        policy.resources = new java.util.ArrayList<>();
        policy.conditions = new java.util.HashMap<>();
        policy.tags = new java.util.HashMap<>();
        return policy;
    }

    public AccessPolicy activate(String modifiedBy) {
        this.status = PolicyStatus.ACTIVE;
        this.lastModifiedBy = modifiedBy;
        updateTimestamp();
        return this;
    }

    public AccessPolicy deactivate(String modifiedBy) {
        this.status = PolicyStatus.INACTIVE;
        this.lastModifiedBy = modifiedBy;
        updateTimestamp();
        return this;
    }

    public AccessPolicy archive(String modifiedBy) {
        this.status = PolicyStatus.ARCHIVED;
        this.lastModifiedBy = modifiedBy;
        updateTimestamp();
        return this;
    }

    public boolean isEffective() {
        Instant now = Instant.now();
        return this.status == PolicyStatus.ACTIVE
            && (effectFrom == null || !now.isBefore(effectFrom))
            && (effectTo == null || !now.isAfter(effectTo));
    }

    public AccessPolicy updatePriority(int priority, String modifiedBy) {
        this.priority = priority;
        this.lastModifiedBy = modifiedBy;
        updateTimestamp();
        return this;
    }

    public AccessPolicy addStatement(PolicyStatement statement) {
        if (this.statements == null) this.statements = new java.util.ArrayList<>();
        this.statements.add(statement);
        updateTimestamp();
        return this;
    }

    public AccessPolicy addResource(String resource) {
        if (this.resources == null) this.resources = new java.util.ArrayList<>();
        this.resources.add(resource);
        updateTimestamp();
        return this;
    }

    public AccessPolicy removeResource(String resource) {
        if (this.resources != null) this.resources.remove(resource);
        updateTimestamp();
        return this;
    }

    public AccessPolicy addPrincipal(String principal) {
        if (this.principals == null) this.principals = new java.util.ArrayList<>();
        this.principals.add(principal);
        updateTimestamp();
        return this;
    }

    public AccessPolicy removePrincipal(String principal) {
        if (this.principals != null) this.principals.remove(principal);
        updateTimestamp();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessPolicy that = (AccessPolicy) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
