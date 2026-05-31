package com.gogidix.ecommerce.paymentmethod.domain.port.out;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepositoryPort {
    PaymentMethod save(PaymentMethod entity);
    Optional<PaymentMethod> findById(String id);
    List<PaymentMethod> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
