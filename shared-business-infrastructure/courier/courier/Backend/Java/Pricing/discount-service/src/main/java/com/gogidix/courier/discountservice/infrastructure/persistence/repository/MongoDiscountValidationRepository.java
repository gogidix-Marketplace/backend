package com.gogidix.courier.discountservice.infrastructure.persistence.repository;

import com.gogidix.courier.discountservice.domain.entity.DiscountValidation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface MongoDiscountValidationRepository extends MongoRepository<DiscountValidation, String> {

    Optional<DiscountValidation> findByTenantIdAndDiscountCodeAndUserId(
            String tenantId, String discountCode, String userId);

    void deleteByExpiresAtBefore(Instant now);
}
