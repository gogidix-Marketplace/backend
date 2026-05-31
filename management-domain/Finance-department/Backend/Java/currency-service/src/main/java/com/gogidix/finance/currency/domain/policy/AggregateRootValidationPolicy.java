package com.gogidix.finance.currency.domain.policy;

import com.gogidix.finance.currency.domain.model.AggregateRoot;
import org.springframework.stereotype.Component;

@Component
public class AggregateRootValidationPolicy {

    public void validate(AggregateRoot entity) {
    }
}
