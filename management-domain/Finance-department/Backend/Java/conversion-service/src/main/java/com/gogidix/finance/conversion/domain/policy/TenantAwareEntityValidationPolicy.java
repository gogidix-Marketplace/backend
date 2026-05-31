package com.gogidix.finance.conversion.domain.policy;

import com.gogidix.finance.conversion.domain.model.TenantAwareEntity;
import org.springframework.stereotype.Component;

@Component
public class TenantAwareEntityValidationPolicy {

    public void validate(TenantAwareEntity entity) {
    }
}
