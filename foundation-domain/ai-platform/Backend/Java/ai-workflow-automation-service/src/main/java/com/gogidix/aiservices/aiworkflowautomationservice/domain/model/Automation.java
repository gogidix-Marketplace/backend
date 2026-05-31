package com.gogidix.aiservices.aiworkflowautomationservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Document(collection = "automations")
@CompoundIndex(name = "idx_auto_tenant", def = "{'tenantId': 1, 'automationId': 1}")
public class Automation {
    @Id private String id;
    @Indexed private String automationId;
    @Indexed private String tenantId;
    private String name;
    private TriggerType triggerType;
    private String triggerConfig;
    private List<AutomationAction> actions;
    private AutomationStatus status;
    private String schedule;
    private Instant createdAt;
    private Instant nextRun;
    private Instant lastRunAt;
    private Long totalRuns;

    public enum TriggerType { SCHEDULE, EVENT, WEBHOOK }
    public enum AutomationStatus { ACTIVE, PAUSED, DISABLED }

    private Automation() { this.actions = new ArrayList<>(); this.totalRuns = 0L; }

    public Automation(String tenantId, String name, TriggerType triggerType, List<AutomationAction> actions) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.automationId = "auto_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.name = Objects.requireNonNull(name);
        this.triggerType = Objects.requireNonNull(triggerType);
        this.actions = new ArrayList<>(Objects.requireNonNull(actions));
        this.status = AutomationStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public void activate() { this.status = AutomationStatus.ACTIVE; }
    public void pause() { this.status = AutomationStatus.PAUSED; }
    public void recordExecution() { this.totalRuns++; this.lastRunAt = Instant.now(); }
    public boolean canExecute() { return this.status == AutomationStatus.ACTIVE; }

    public String getId() { return id; }
    public String getAutomationId() { return automationId; }
    public String getTenantId() { return tenantId; }
    public String getName() { return name; }
    public TriggerType getTriggerType() { return triggerType; }
    public String getTriggerConfig() { return triggerConfig; }
    public List<AutomationAction> getActions() { return Collections.unmodifiableList(actions); }
    public AutomationStatus getStatus() { return status; }
    public String getSchedule() { return schedule; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getNextRun() { return nextRun; }
    public Instant getLastRunAt() { return lastRunAt; }
    public Long getTotalRuns() { return totalRuns; }

    protected void setId(String id) { this.id = id; }
    protected void setAutomationId(String automationId) { this.automationId = automationId; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setName(String name) { this.name = name; }
    protected void setTriggerType(TriggerType triggerType) { this.triggerType = triggerType; }
    protected void setTriggerConfig(String triggerConfig) { this.triggerConfig = triggerConfig; }
    protected void setActions(List<AutomationAction> actions) { this.actions = actions; }
    protected void setStatus(AutomationStatus status) { this.status = status; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setNextRun(Instant nextRun) { this.nextRun = nextRun; }
    protected void setLastRunAt(Instant lastRunAt) { this.lastRunAt = lastRunAt; }
    protected void setTotalRuns(Long totalRuns) { this.totalRuns = totalRuns; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Automation instance = new Automation();
        public Builder id(String id) { instance.id = id; return this; }
        public Builder automationId(String automationId) { instance.automationId = automationId; return this; }
        public Builder tenantId(String tenantId) { instance.tenantId = tenantId; return this; }
        public Builder name(String name) { instance.name = name; return this; }
        public Builder triggerType(TriggerType triggerType) { instance.triggerType = triggerType; return this; }
        public Builder actions(List<AutomationAction> actions) { instance.actions = actions; return this; }
        public Builder status(AutomationStatus status) { instance.status = status; return this; }
        public Builder schedule(String schedule) { instance.schedule = schedule; return this; }
        public Automation build() {
            if (instance.tenantId == null) throw new IllegalArgumentException("tenantId required");
            if (instance.name == null) throw new IllegalArgumentException("name required");
            if (instance.status == null) instance.status = AutomationStatus.ACTIVE;
            if (instance.createdAt == null) instance.createdAt = Instant.now();
            return instance;
        }
    }
}
