package com.gogidix.ecommerce.paymentgateway.domain.port.out;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import java.util.List;
import java.util.Optional;

public interface PaymentGatewayRepositoryPort {
    PaymentGateway save(PaymentGateway entity);
    Optional<PaymentGateway> findById(String id);
    List<PaymentGateway> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}
