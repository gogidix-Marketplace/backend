package com.gogidix.globalbusinessmanagement.regionalaggregation.domain.repository;

import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.CountryContribution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryContributionRepository extends MongoRepository<CountryContribution, String> {

    List<CountryContribution> findByRegionCodeAndPeriodId(String regionCode, String periodId);

    List<CountryContribution> findByRegionCodeAndPeriodIdOrderByRevenueContributionDesc(
            String regionCode, String periodId, int limit);

    List<CountryContribution> findByCountryCode(String countryCode);

    void deleteByRegionCodeAndPeriodId(String regionCode, String periodId);
}
