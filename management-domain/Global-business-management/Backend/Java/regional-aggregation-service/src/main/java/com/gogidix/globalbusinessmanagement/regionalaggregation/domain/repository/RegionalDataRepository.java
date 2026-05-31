package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.repository;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.RegionalData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalDataRepository extends MongoRepository<RegionalData, String> {

    List<RegionalData> findByRegionCode(String regionCode);

    Optional<RegionalData> findByRegionCodeAndPeriodId(String regionCode, String periodId);

    List<RegionalData> findByAggregationType(RegionalData.AggregationType aggregationType);

    List<RegionalData> findByStatus(RegionalData.AggregationStatus status);

    void deleteByRegionCodeAndPeriodId(String regionCode, String periodId);
}
