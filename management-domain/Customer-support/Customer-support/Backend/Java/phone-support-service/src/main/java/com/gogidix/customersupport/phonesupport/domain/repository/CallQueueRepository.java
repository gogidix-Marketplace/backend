package com.gogidix.customersupport.phonesupport.domain.repository;

import com.gogidix.customersupport.phonesupport.domain.model.CallQueue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CallQueueRepository extends MongoRepository<CallQueue, String> {

    List<CallQueue> findByTenantId(String tenantId);

    Optional<CallQueue> findByTenantIdAndId(String tenantId, String id);

    Optional<CallQueue> findByQueueId(String queueId);

    List<CallQueue> findByTenantIdAndIsActiveTrue(String tenantId);

    List<CallQueue> findByTenantIdAndStatus(String tenantId, CallQueue.QueueStatus status);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByQueueId(String queueId);
}
