package com.gogidix.shared.warehousing.stock.domain.repository;

import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends MongoRepository<StockMovement, String> {

    List<StockMovement> findBySkuOrderByTimestampDesc(String sku);

    Page<StockMovement> findBySkuOrderByTimestampDesc(String sku, Pageable pageable);

    List<StockMovement> findBySkuAndLocationIdOrderByTimestampDesc(String sku, String locationId);

    List<StockMovement> findByReferenceId(String referenceId);

    List<StockMovement> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);

    List<StockMovement> findByMovementTypeOrderByTimestampDesc(StockMovement.MovementType movementType);
}
