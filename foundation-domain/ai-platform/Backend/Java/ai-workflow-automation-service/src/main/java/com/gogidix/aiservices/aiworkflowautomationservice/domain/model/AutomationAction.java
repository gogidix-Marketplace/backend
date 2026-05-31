package com.gogidix.aiservices.aiworkflowautomationservice.domain.model;

import java.util.Map;

public record AutomationAction(
    String actionId,
    String service,
    String operation,
    Map<String, Object> parameters
) {
}
