package com.gogidix.courier.discountservice.domain.repository;

import com.gogidix.courier.discountservice.domain.entity.DiscountValidation;

import java.util.Optional;

public interface DiscountValidationRepository {

    DiscountValidation save(DiscountValidation validation);

    Optional<DiscountValidation> findByTenantIdAndDiscountCodeAndUserId(
            String tenantId, String discountCode, String userId);

    void deleteExpiredValidations();

    void deleteById(String id);
}
