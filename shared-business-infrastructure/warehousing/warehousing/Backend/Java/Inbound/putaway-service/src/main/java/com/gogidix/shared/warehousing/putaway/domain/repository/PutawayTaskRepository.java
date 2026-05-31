package com.gogidix.shared.warehousing.putaway.domain.repository;

import com.gogidix.shared.warehousing.putaway.domain.entity.PutawayTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PutawayTaskRepository extends MongoRepository<PutawayTask, String> {

    List<PutawayTask> findByTenantId(String tenantId);

    List<PutawayTask> findByTenantIdAndStatus(String tenantId, PutawayTask.TaskStatus status);

    List<PutawayTask> findByTenantIdAndReceiptId(String tenantId, String receiptId);

    List<PutawayTask> findByTenantIdAndAssignedTo(String tenantId, String assignedTo);
}
