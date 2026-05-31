package com.gogidix.sysadmin.infrastructuremonitoring.domain.repository;

import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.InfrastructureMonitoring;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface InfrastructureMonitoringRepository extends MongoRepository<InfrastructureMonitoring, String> {

    List<InfrastructureMonitoring> findByTenantId(String tenantId);

    List<InfrastructureMonitoring> findByTenantIdAndStatus(String tenantId, InfrastructureMonitoring.MonitoringStatus status);

    Optional<InfrastructureMonitoring> findByTenantIdAndId(String tenantId, String id);

    List<InfrastructureMonitoring> findByTenantIdAndType(String tenantId, InfrastructureMonitoring.InfrastructureType type);

    List<InfrastructureMonitoring> findByTenantIdAndEnvironment(String tenantId, String environment);

    List<InfrastructureMonitoring> findByTenantIdAndRegion(String tenantId, String region);

    @Query("{ 'tenantId': ?0, 'tags.?1': ?2 }")
    List<InfrastructureMonitoring> findByTenantIdAndTag(String tenantId, String tagKey, String tagValue);

    @Query("{ 'tenantId': ?0, 'lastCheckedAt': { $lt: ?1 } }")
    List<InfrastructureMonitoring> findByTenantIdAndLastCheckedBefore(String tenantId, Instant before);

    List<InfrastructureMonitoring> findByTenantIdAndNameContainingIgnoreCase(String tenantId, String name);

    @Query("{ 'tenantId': ?0, '$or': [ " +
           "{ 'name': { $regex: ?1, $options: 'i' } }, " +
           "{ 'host': { $regex: ?1, $options: 'i' } } " +
           "] }")
    List<InfrastructureMonitoring> searchByTenantId(String tenantId, String searchTerm);

    @Query(value = "{ 'tenantId': ?0, 'status': ?1 }", count = true)
    long countByTenantIdAndStatus(String tenantId, InfrastructureMonitoring.MonitoringStatus status);

    boolean existsByTenantIdAndHost(String tenantId, String host);

    boolean existsByTenantIdAndName(String tenantId, String name);
}
