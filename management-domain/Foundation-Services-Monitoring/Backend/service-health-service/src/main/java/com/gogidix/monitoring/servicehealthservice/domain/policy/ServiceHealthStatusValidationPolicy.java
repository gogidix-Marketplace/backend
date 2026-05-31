package com.gogidix.monitoring.servicehealthservice.domain.policy;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus;
import org.springframework.stereotype.Component;

@Component
public class ServiceHealthStatusValidationPolicy {

    public void validate(ServiceHealthStatus entity) {
    }
}
