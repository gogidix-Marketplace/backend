package com.gogidix.cargo.eventdriven.domain.event;

public class SagaLifecycleEvent extends BaseDomainEvent {
    private String sagaId;
    private String sagaType;
    private String stepName;
    private SagaEventStatus status;
    private String inputData;
    private String outputData;
    private String errorMessage;

    public enum SagaEventStatus {
        SAGA_STARTED, STEP_STARTED, STEP_COMPLETED, STEP_FAILED,
        SAGA_COMPLETED, SAGA_FAILED, COMPENSATION_STARTED, COMPENSATION_COMPLETED
    }

    public SagaLifecycleEvent(String eventType, String tenantId, String correlationId,
                               String sagaId, String sagaType, SagaEventStatus status) {
        super(eventType, tenantId, correlationId);
        this.sagaId = sagaId;
        this.sagaType = sagaType;
        this.status = status;
    }

    public String getSagaId() { return sagaId; }
    public void setSagaId(String sagaId) { this.sagaId = sagaId; }
    public String getSagaType() { return sagaType; }
    public void setSagaType(String sagaType) { this.sagaType = sagaType; }
    public String getStepName() { return stepName; }
    public void setStepName(String stepName) { this.stepName = stepName; }
    public SagaEventStatus getStatus() { return status; }
    public void setStatus(SagaEventStatus status) { this.status = status; }
    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }
    public String getOutputData() { return outputData; }
    public void setOutputData(String outputData) { this.outputData = outputData; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    @Override public String getAggregateType() { return "SAGA"; }
    @Override public String getAggregateId() { return sagaId; }
}
