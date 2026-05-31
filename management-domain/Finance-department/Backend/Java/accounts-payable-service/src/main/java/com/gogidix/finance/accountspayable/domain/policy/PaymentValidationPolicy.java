package com.gogidix.finance.accountspayable.domain.policy;

import com.gogidix.finance.accountspayable.domain.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidationPolicy {

    public void validate(Payment entity) {
    }
}
