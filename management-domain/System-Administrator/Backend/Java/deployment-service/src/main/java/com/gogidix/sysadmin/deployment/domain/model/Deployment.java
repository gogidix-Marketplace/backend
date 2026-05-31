package com.gogidix.sysadmin.deployment.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "deployments")
public class Deployment {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String deploymentNumber;
    private String name;
    private String description;
    private DeploymentType type;
    private DeploymentStatus status;
    private String applicationId;
    private String applicationName;
    private String version;
    private String environmentId;
    private String environmentName;
    private String strategy;
    private List<String> targetResourceIds;
    private Map<String, String> configuration;
    private List<DeploymentStep> steps;
    private String initiatedBy;
    private Instant startedAt;
    private Instant completedAt;
    private String errorMessage;
    private String rollbackToVersion;
    private Boolean isRollback;
    private String parentDeploymentId;
    private List<DeploymentArtifact> artifacts;
    private Map<String, String> tags;
    private Integer currentStep;
    private Integer totalSteps;
    private String approvalStatus;
    private String approvedBy;
    private Instant approvedAt;
    private Instant createdAt;
    private Instant updatedAt;

    public enum DeploymentType {
        BLUE_GREEN, CANARY, ROLLING, IMMEDIATE, SCHEDULED
    }

    public enum DeploymentStatus {
        PENDING_APPROVAL, APPROVED, QUEUED, IN_PROGRESS, PAUSED,
        COMPLETED, FAILED, ROLLED_BACK, CANCELLED, SCHEDULED
    }

    public static class DeploymentStep {
        private String id;
        private String name;
        private String description;
        private StepType type;
        private StepStatus status;
        private Integer order;
        private Instant startedAt;
        private Instant completedAt;
        private String errorMessage;
        private Map<String, Object> parameters;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public StepType getType() { return type; }
        public void setType(StepType type) { this.type = type; }
        public StepStatus getStatus() { return status; }
        public void setStatus(StepStatus status) { this.status = status; }
        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
        public Instant getStartedAt() { return startedAt; }
        public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }
        public Instant getCompletedAt() { return completedAt; }
        public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    public enum StepType {
        PRE_DEPLOY, DEPLOY, POST_DEPLOY, VERIFICATION, CLEANUP
    }

    public enum StepStatus {
        PENDING, IN_PROGRESS, COMPLETED, FAILED, SKIPPED
    }

    public static class DeploymentArtifact {
        private String name;
        private String type;
        private String uri;
        private String checksum;
        private Long size;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getUri() { return uri; }
        public void setUri(String uri) { this.uri = uri; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
        public Long getSize() { return size; }
        public void setSize(Long size) { this.size = size; }
    }

    public Deployment() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = DeploymentStatus.PENDING_APPROVAL;
        this.currentStep = 0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getDeploymentNumber() { return deploymentNumber; }
    public void setDeploymentNumber(String deploymentNumber) { this.deploymentNumber = deploymentNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public DeploymentType getType() { return type; }
    public void setType(DeploymentType type) { this.type = type; }

    public DeploymentStatus getStatus() { return status; }
    public void setStatus(DeploymentStatus status) { this.status = status; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getApplicationName() { return applicationName; }
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getEnvironmentId() { return environmentId; }
    public void setEnvironmentId(String environmentId) { this.environmentId = environmentId; }

    public String getEnvironmentName() { return environmentName; }
    public void setEnvironmentName(String environmentName) { this.environmentName = environmentName; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

    public List<String> getTargetResourceIds() { return targetResourceIds; }
    public void setTargetResourceIds(List<String> targetResourceIds) { this.targetResourceIds = targetResourceIds; }

    public Map<String, String> getConfiguration() { return configuration; }
    public void setConfiguration(Map<String, String> configuration) { this.configuration = configuration; }

    public List<DeploymentStep> getSteps() { return steps; }
    public void setSteps(List<DeploymentStep> steps) { this.steps = steps; }

    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getRollbackToVersion() { return rollbackToVersion; }
    public void setRollbackToVersion(String rollbackToVersion) { this.rollbackToVersion = rollbackToVersion; }

    public Boolean getIsRollback() { return isRollback; }
    public void setIsRollback(Boolean isRollback) { this.isRollback = isRollback; }

    public String getParentDeploymentId() { return parentDeploymentId; }
    public void setParentDeploymentId(String parentDeploymentId) { this.parentDeploymentId = parentDeploymentId; }

    public List<DeploymentArtifact> getArtifacts() { return artifacts; }
    public void setArtifacts(List<DeploymentArtifact> artifacts) { this.artifacts = artifacts; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public Integer getCurrentStep() { return currentStep; }
    public void setCurrentStep(Integer currentStep) { this.currentStep = currentStep; }

    public Integer getTotalSteps() { return totalSteps; }
    public void setTotalSteps(Integer totalSteps) { this.totalSteps = totalSteps; }

    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public Instant getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void start() {
        this.status = DeploymentStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void complete() {
        this.status = DeploymentStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void fail(String errorMessage) {
        this.status = DeploymentStatus.FAILED;
        this.errorMessage = errorMessage;
        this.completedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void rollback(String toVersion) {
        this.status = DeploymentStatus.ROLLED_BACK;
        this.rollbackToVersion = toVersion;
        this.completedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deployment deployment = (Deployment) o;
        return Objects.equals(id, deployment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
