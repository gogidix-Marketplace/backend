package com.gogidix.ecommerce.paymentgateway.domain.port.out;

import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PaymentGatewayRepositoryPort {

    PaymentGateway save(PaymentGateway entity);

    Optional<PaymentGateway> findByIdAndTenantId(String id, String tenantId);

    Page<PaymentGateway> findByTenantId(String tenantId, Pageable pageable);

    void deleteById(String id);
}