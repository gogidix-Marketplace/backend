package com.gogidix.finance.bankreconciliation.domain.policy;

import com.gogidix.finance.bankreconciliation.domain.model.BankTransaction;
import org.springframework.stereotype.Component;

@Component
public class BankTransactionValidationPolicy {

    public void validate(BankTransaction entity) {
    }
}
