package com.gogidix.shared.warehousing.returns.domain.repository;

import com.gogidix.shared.warehousing.returns.domain.entity.Return;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnRepository extends MongoRepository<Return, String> {

    List<Return> findByTenantId(String tenantId);

    Optional<Return> findByTenantIdAndRmaNumber(String tenantId, String rmaNumber);

    List<Return> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    List<Return> findByTenantIdAndStatus(String tenantId, Return.ReturnStatus status);

    List<Return> findByTenantIdAndCustomerId(String tenantId, String customerId);

    long countByTenantIdAndStatus(String tenantId, Return.ReturnStatus status);
}
