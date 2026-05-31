package com.gogidix.shared.warehousing.label.domain.repository;

import com.gogidix.shared.warehousing.label.domain.entity.ShippingLabel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingLabelRepository extends MongoRepository<ShippingLabel, String> {

    List<ShippingLabel> findByTenantId(String tenantId);

    Optional<ShippingLabel> findByTenantIdAndLabelNumber(String tenantId, String labelNumber);

    List<ShippingLabel> findByTenantIdAndShipmentId(String tenantId, String shipmentId);
}
