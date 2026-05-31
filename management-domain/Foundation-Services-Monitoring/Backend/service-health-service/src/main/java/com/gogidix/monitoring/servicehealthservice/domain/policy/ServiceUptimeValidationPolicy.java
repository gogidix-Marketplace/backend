package com.gogidix.monitoring.servicehealthservice.domain.policy;

import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime;
import org.springframework.stereotype.Component;

@Component
public class ServiceUptimeValidationPolicy {

    public void validate(ServiceUptime entity) {
    }
}
