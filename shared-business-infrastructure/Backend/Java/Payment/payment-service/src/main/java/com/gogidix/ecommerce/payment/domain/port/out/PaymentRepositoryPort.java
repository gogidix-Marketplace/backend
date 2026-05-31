package com.gogidix.ecommerce.payment.domain.port.out;

import com.gogidix.ecommerce.payment.domain.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Payment save(Payment entity);
    Optional<Payment> findById(String id);
    List<Payment> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
