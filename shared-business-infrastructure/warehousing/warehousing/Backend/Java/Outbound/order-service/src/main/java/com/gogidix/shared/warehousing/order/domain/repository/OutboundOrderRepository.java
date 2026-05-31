package com.gogidix.shared.warehousing.order.domain.repository;

import com.gogidix.shared.warehousing.order.domain.entity.OutboundOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboundOrderRepository extends MongoRepository<OutboundOrder, String> {

    List<OutboundOrder> findByTenantId(String tenantId);

    Optional<OutboundOrder> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    List<OutboundOrder> findByTenantIdAndStatus(String tenantId, OutboundOrder.OrderStatus status);

    List<OutboundOrder> findByTenantIdAndCustomerOrderId(String tenantId, String customerOrderId);
}
