package com.gogidix.shared.warehousing.receiving.domain.repository;

import com.gogidix.shared.warehousing.receiving.domain.entity.ReceivingOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceivingOrderRepository extends MongoRepository<ReceivingOrder, String> {

    List<ReceivingOrder> findByTenantId(String tenantId);

    Optional<ReceivingOrder> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    List<ReceivingOrder> findByTenantIdAndStatus(String tenantId, ReceivingOrder.ReceivingStatus status);
}
