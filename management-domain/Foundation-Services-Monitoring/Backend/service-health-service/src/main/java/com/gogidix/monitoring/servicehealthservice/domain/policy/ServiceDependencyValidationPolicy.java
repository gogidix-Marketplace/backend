package com.gogidix.monitoring.servicehealthservice.domain.policy;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceDependency;
import org.springframework.stereotype.Component;

@Component
public class ServiceDependencyValidationPolicy {

    public void validate(ServiceDependency entity) {
    }
}
