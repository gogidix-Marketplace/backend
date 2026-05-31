package com.gogidix.shared.warehousing.zone.domain.repository;

import com.gogidix.shared.warehousing.zone.domain.entity.Zone;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends MongoRepository<Zone, String> {
    Optional<Zone> findByZoneId(String zoneId);
    List<Zone> findByState(String state);
    List<Zone> findByCountry(String country);
    List<Zone> findByWarehouseId(String warehouseId);
    List<Zone> findByWarehouseIdsContaining(String warehouseId);
}
