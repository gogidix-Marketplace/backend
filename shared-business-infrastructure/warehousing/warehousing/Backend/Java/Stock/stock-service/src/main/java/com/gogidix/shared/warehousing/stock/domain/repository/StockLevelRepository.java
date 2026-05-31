package com.gogidix.shared.warehousing.stock.domain.repository;

import com.gogidix.shared.warehousing.stock.domain.entity.StockLevel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockLevelRepository extends MongoRepository<StockLevel, String> {

    Optional<StockLevel> findBySkuAndLocationId(String sku, String locationId);

    List<StockLevel> findBySku(String sku);

    List<StockLevel> findByLocationId(String locationId);

    @Query("{ 'availableQuantity': { $lte: '$reorderPoint' } }")
    List<StockLevel> findBelowReorderPoint();

    List<StockLevel> findByAvailableQuantityGreaterThan(Integer quantity);

    boolean existsBySkuAndLocationId(String sku, String locationId);

    List<StockLevel> findBySkuIn(List<String> skus);

    @Query(value = "{ 'sku': ?0 }", fields = "{ 'availableQuantity': 1 }")
    Integer getTotalAvailableQuantity(String sku);
}
