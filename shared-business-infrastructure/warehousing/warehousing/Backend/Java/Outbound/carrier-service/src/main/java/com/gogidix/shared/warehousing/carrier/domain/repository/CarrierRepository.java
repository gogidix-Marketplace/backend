package com.gogidix.shared.warehousing.carrier.domain.repository;

import com.gogidix.shared.warehousing.carrier.domain.entity.Carrier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrierRepository extends MongoRepository<Carrier, String> {

    List<Carrier> findByTenantId(String tenantId);

    Optional<Carrier> findByTenantIdAndCarrierCode(String tenantId, String carrierCode);

    List<Carrier> findByTenantIdAndActive(String tenantId, Boolean active);
}
