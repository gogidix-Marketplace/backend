package com.gogidix.customersupport.countrysupportdashboard.domain.repository;

import com.gogidix.customersupport.countrysupportdashboard.domain.model.CountrySpecificMetrics;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CountrySpecificMetricsRepository extends MongoRepository<CountrySpecificMetrics, String> {

    List<CountrySpecificMetrics> findByTenantId(String tenantId);

    Optional<CountrySpecificMetrics> findByTenantIdAndId(String tenantId, String id);

    List<CountrySpecificMetrics> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<CountrySpecificMetrics> findByTenantIdAndCountryCodeAndMetricDateBetween(
            String tenantId, String countryCode, LocalDate startDate, LocalDate endDate);

    List<CountrySpecificMetrics> findByTenantIdAndMetricDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    Optional<CountrySpecificMetrics> findFirstByTenantIdAndCountryCodeOrderByMetricDateDesc(
            String tenantId, String countryCode);

    List<CountrySpecificMetrics> findByTenantIdAndRegion(String tenantId, String region);

    void deleteByTenantIdAndId(String tenantId, String id);

    List<String> findDistinctCountryCodesByTenantId(String tenantId);

    List<String> findDistinctRegionsByTenantId(String tenantId);
}
