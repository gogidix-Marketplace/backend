package com.gogidix.management.executive.domain.policy;

import com.gogidix.management.executive.audit.domain.model.Audit;
import org.springframework.stereotype.Component;

@Component
public class AuditValidationPolicy {

    public void validate(Audit entity) {
    }
}
