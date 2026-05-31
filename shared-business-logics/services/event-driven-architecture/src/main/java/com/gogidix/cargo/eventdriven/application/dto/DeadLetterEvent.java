package com.gogidix.cargo.eventdriven.application.dto;

public class DeadLetterEvent {
    private String id;
    private String originalTopic;
    private String errorMessage;
    private int retryCount;
    private String status;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOriginalTopic() { return originalTopic; }
    public void setOriginalTopic(String originalTopic) { this.originalTopic = originalTopic; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
