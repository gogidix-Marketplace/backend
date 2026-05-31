package com.gogidix.ecommerce.paymentmethod.domain.port.out;

import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PaymentMethodRepositoryPort {

    PaymentMethod save(PaymentMethod entity);

    Optional<PaymentMethod> findByIdAndTenantId(String id, String tenantId);

    Page<PaymentMethod> findByTenantId(String tenantId, Pageable pageable);

    void deleteById(String id);
}