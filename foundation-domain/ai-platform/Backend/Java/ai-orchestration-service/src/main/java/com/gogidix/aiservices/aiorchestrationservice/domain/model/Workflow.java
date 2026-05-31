package com.gogidix.aiservices.aiorchestrationservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "workflows")
@CompoundIndex(name = "idx_workflow_tenant", def = "{'tenantId': 1, 'workflowId': 1}")
public class Workflow {

    @Id
    private String id;

    @Indexed
    private String workflowId;

    @Indexed
    private String tenantId;

    private String name;

    private String description;

    private List<WorkflowStep> steps;

    private List<String> triggers;

    private WorkflowStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private Instant lastExecutedAt;

    private Integer maxExecutionTime;

    public static final int MAX_STEPS = 50;
    public static final int DEFAULT_MAX_EXECUTION_TIME = 60;

    private Workflow() {
        this.steps = new ArrayList<>();
        this.triggers = new ArrayList<>();
        this.maxExecutionTime = DEFAULT_MAX_EXECUTION_TIME;
    }

    public Workflow(String tenantId, String name, List<WorkflowStep> steps) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.workflowId = "workflow_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.name = Objects.requireNonNull(name, "name is required");
        this.steps = new ArrayList<>(Objects.requireNonNull(steps, "steps is required"));
        this.status = WorkflowStatus.DRAFT;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (this.steps.isEmpty()) {
            throw new IllegalStateException("Cannot activate workflow without steps");
        }
        if (this.steps.size() > MAX_STEPS) {
            throw new IllegalStateException("Workflow cannot have more than " + MAX_STEPS + " steps");
        }
        this.status = WorkflowStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void pause() {
        this.status = WorkflowStatus.PAUSED;
        this.updatedAt = Instant.now();
    }

    public void addStep(WorkflowStep step) {
        Objects.requireNonNull(step, "step is required");
        if (this.steps.size() >= MAX_STEPS) {
            throw new IllegalStateException("Workflow cannot have more than " + MAX_STEPS + " steps");
        }
        if (!this.steps.contains(step)) {
            this.steps.add(step);
            this.updatedAt = Instant.now();
        }
    }

    public void removeStep(String stepId) {
        this.steps.removeIf(step -> step.getStepId().equals(stepId));
        this.updatedAt = Instant.now();
    }

    public void addTrigger(String trigger) {
        Objects.requireNonNull(trigger, "trigger is required");
        if (!this.triggers.contains(trigger)) {
            this.triggers.add(trigger);
            this.updatedAt = Instant.now();
        }
    }

    public void recordExecution() {
        this.lastExecutedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean canExecute() {
        return this.status == WorkflowStatus.ACTIVE && !this.steps.isEmpty();
    }

    public int getEstimatedExecutionTime() {
        return this.steps.stream().mapToInt(WorkflowStep::getEstimatedDuration).sum();
    }

    public String getId() { return id; }
    public String getWorkflowId() { return workflowId; }
    public String getTenantId() { return tenantId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<WorkflowStep> getSteps() { return Collections.unmodifiableList(steps); }
    public List<String> getTriggers() { return Collections.unmodifiableList(triggers); }
    public WorkflowStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getLastExecutedAt() { return lastExecutedAt; }
    public Integer getMaxExecutionTime() { return maxExecutionTime; }

    public void setId(String id) { this.id = id; }
    public void setWorkflowId(String workflowId) { this.workflowId = workflowId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setSteps(List<WorkflowStep> steps) { this.steps = steps; }
    public void setTriggers(List<String> triggers) { this.triggers = triggers; }
    public void setStatus(WorkflowStatus status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public void setLastExecutedAt(Instant lastExecutedAt) { this.lastExecutedAt = lastExecutedAt; }
    public void setMaxExecutionTime(Integer maxExecutionTime) { this.maxExecutionTime = maxExecutionTime; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Workflow instance = new Workflow();

        public Builder id(String id) { instance.id = id; return this; }
        public Builder workflowId(String workflowId) { instance.workflowId = workflowId; return this; }
        public Builder tenantId(String tenantId) { instance.tenantId = tenantId; return this; }
        public Builder name(String name) { instance.name = name; return this; }
        public Builder description(String description) { instance.description = description; return this; }
        public Builder steps(List<WorkflowStep> steps) { instance.steps = steps; return this; }
        public Builder triggers(List<String> triggers) { instance.triggers = triggers; return this; }
        public Builder status(WorkflowStatus status) { instance.status = status; return this; }
        public Builder maxExecutionTime(Integer maxExecutionTime) { instance.maxExecutionTime = maxExecutionTime; return this; }

        public Workflow build() {
            if (instance.tenantId == null) throw new IllegalArgumentException("tenantId required");
            if (instance.name == null) throw new IllegalArgumentException("name required");
            if (instance.status == null) instance.status = WorkflowStatus.DRAFT;
            if (instance.maxExecutionTime == null) instance.maxExecutionTime = DEFAULT_MAX_EXECUTION_TIME;
            if (instance.createdAt == null) instance.createdAt = Instant.now();
            if (instance.updatedAt == null) instance.updatedAt = Instant.now();
            return instance;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Workflow workflow = (Workflow) o;
        return Objects.equals(workflowId, workflow.workflowId) && Objects.equals(tenantId, workflow.tenantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflowId, tenantId);
    }
}
