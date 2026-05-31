package com.gogidix.customersupport.countrysupportdashboard.domain.repository;

import com.gogidix.customersupport.countrysupportdashboard.domain.model.RegionalTicketStats;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalTicketStatsRepository extends MongoRepository<RegionalTicketStats, String> {

    List<RegionalTicketStats> findByTenantId(String tenantId);

    Optional<RegionalTicketStats> findByTenantIdAndId(String tenantId, String id);

    List<RegionalTicketStats> findByTenantIdAndRegionName(String tenantId, String regionName);

    List<RegionalTicketStats> findByTenantIdAndStatDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<RegionalTicketStats> findByTenantIdAndRegionNameAndStatDateBetween(
            String tenantId, String regionName, LocalDate startDate, LocalDate endDate);

    Optional<RegionalTicketStats> findFirstByTenantIdAndRegionNameOrderByStatDateDesc(
            String tenantId, String regionName);

    List<RegionalTicketStats> findByTenantIdOrderByStatDateDesc(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    List<String> findDistinctRegionNamesByTenantId(String tenantId);
}
