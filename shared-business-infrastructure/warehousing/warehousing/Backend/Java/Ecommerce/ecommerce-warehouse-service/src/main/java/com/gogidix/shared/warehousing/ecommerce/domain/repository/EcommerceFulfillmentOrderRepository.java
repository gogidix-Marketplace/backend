package com.gogidix.shared.warehousing.ecommerce.domain.repository;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.EcommerceFulfillmentOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EcommerceFulfillmentOrderRepository extends MongoRepository<EcommerceFulfillmentOrder, String> {
    Optional<EcommerceFulfillmentOrder> findByFulfillmentId(String fulfillmentId);
    Optional<EcommerceFulfillmentOrder> findByOrderIdAndSubOrderId(String orderId, String subOrderId);
    List<EcommerceFulfillmentOrder> findByWarehouseIdAndStatus(String warehouseId, String status);
    List<EcommerceFulfillmentOrder> findByVendorId(String vendorId);
    List<EcommerceFulfillmentOrder> findByWarehouseId(String warehouseId);
}
