package com.gogidix.ecommerce.vendor.onboarding.domain.repository;

import com.gogidix.ecommerce.vendor.onboarding.domain.model.Onboarding;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OnboardingRepository extends MongoRepository<Onboarding, String> {
    List<Onboarding> findByTenantId(String tenantId);
    Optional<Onboarding> findByTenantIdAndId(String tenantId, String id);
}