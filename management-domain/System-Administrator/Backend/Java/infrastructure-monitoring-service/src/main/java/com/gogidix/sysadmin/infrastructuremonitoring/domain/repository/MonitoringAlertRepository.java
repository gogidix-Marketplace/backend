package com.gogidix.sysadmin.infrastructuremonitoring.domain.repository;

import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.MonitoringAlert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MonitoringAlertRepository extends MongoRepository<MonitoringAlert, String> {

    List<MonitoringAlert> findByTenantId(String tenantId);

    List<MonitoringAlert> findByTenantIdAndStatus(String tenantId, MonitoringAlert.AlertStatus status);

    List<MonitoringAlert> findByTenantIdAndSeverity(String tenantId, MonitoringAlert.AlertSeverity severity);

    List<MonitoringAlert> findByTenantIdAndStatusOrderByCreatedAtDesc(
            String tenantId, MonitoringAlert.AlertStatus status);

    List<MonitoringAlert> findByTenantIdAndInfrastructureId(String tenantId, String infrastructureId);

    List<MonitoringAlert> findByTenantIdAndSeverityAndStatus(
            String tenantId, MonitoringAlert.AlertSeverity severity, MonitoringAlert.AlertStatus status);

    @Query("{ 'tenantId': ?0, 'createdAt': { $gte: ?1, $lte: ?2 } }")
    List<MonitoringAlert> findByTenantIdAndCreatedAtBetween(
            String tenantId, Instant startDate, Instant endDate);

    @Query("{ 'tenantId': ?0, 'status': { $in: ?1 } }")
    List<MonitoringAlert> findByTenantIdAndStatusIn(
            String tenantId, List<MonitoringAlert.AlertStatus> statuses);

    @Query("{ 'tenantId': ?0, '$or': [ " +
           "{ 'title': { $regex: ?1, $options: 'i' } }, " +
           "{ 'description': { $regex: ?1, $options: 'i' } } " +
           "] }")
    List<MonitoringAlert> searchByTenantId(String tenantId, String searchTerm);

    @Query(value = "{ 'tenantId': ?0, 'status': ?1 }", count = true)
    long countByTenantIdAndStatus(String tenantId, MonitoringAlert.AlertStatus status);

    @Query(value = "{ 'tenantId': ?0, 'severity': ?1, 'status': ?2 }", count = true)
    long countByTenantIdAndSeverityAndStatus(
            String tenantId, MonitoringAlert.AlertSeverity severity, MonitoringAlert.AlertStatus status);

    List<MonitoringAlert> findTop10ByTenantIdOrderByCreatedAtDesc(String tenantId);

    void deleteByTenantIdAndResolvedAtBefore(String tenantId, Instant date);
}
