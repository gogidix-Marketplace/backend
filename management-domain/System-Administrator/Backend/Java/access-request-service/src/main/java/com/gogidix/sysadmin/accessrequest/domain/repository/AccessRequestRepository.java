package com.gogidix.sysadmin.accessrequest.domain.repository;

import com.gogidix.sysadmin.accessrequest.domain.model.AccessRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessRequestRepository extends MongoRepository<AccessRequest, String> {
    List<AccessRequest> findByTenantId(String tenantId);
    List<AccessRequest> findByRequestedFor(String userId);
}
