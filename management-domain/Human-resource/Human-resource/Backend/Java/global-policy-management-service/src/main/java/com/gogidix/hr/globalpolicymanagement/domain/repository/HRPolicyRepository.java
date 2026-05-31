package com.gogidix.hr.globalpolicymanagement.domain.repository;

import com.gogidix.hr.globalpolicymanagement.domain.model.HRPolicy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRPolicyRepository extends MongoRepository<HRPolicy, String> {
    List<HRPolicy> findByTenantId(String tenantId);
}
