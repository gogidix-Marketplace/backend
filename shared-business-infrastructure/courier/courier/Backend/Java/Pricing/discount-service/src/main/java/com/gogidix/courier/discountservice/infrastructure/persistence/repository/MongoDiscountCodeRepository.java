package com.gogidix.courier.discountservice.infrastructure.persistence.repository;

import com.gogidix.courier.discountservice.domain.entity.DiscountCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoDiscountCodeRepository extends MongoRepository<DiscountCode, String> {

    Optional<DiscountCode> findByTenantIdAndCode(String tenantId, String code);

    List<DiscountCode> findByTenantId(String tenantId);

    List<DiscountCode> findByTenantIdAndStatus(String tenantId, DiscountCode.DiscountStatus status);

    boolean existsByTenantIdAndCode(String tenantId, String code);
}
