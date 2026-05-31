package com.gogidix.universal.tracking.infrastructure.persistence.postgres;

import com.gogidix.universal.tracking.domain.model.TrackingMetric;
import com.gogidix.universal.tracking.domain.port.out.TrackingMetricRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for TrackingMetric entities.
 */
@Repository
public interface TrackingMetricRepository extends JpaRepository<TrackingMetric, UUID>, TrackingMetricRepositoryPort {

    @Override
    TrackingMetric save(TrackingMetric metric);

    @Override
    List<TrackingMetric> saveAll(List<TrackingMetric> metrics);

    @Override
    Optional<TrackingMetric> findById(UUID id);

    @Override
    Page<TrackingMetric> findByTenantId(String tenantId, Pageable pageable);

    @Override
    List<TrackingMetric> findByMetricNameAndTenantId(String metricName, String tenantId);

    @Override
    @Query("SELECT m FROM TrackingMetric m WHERE " +
           "(:tenantId IS NULL OR m.tenantId = :tenantId) AND " +
           "(:metricName IS NULL OR m.metricName = :metricName) AND " +
           "(:metricType IS NULL OR m.metricType = :metricType) AND " +
           "(:eventType IS NULL OR m.eventType = :eventType) AND " +
           "(:source IS NULL OR m.source = :source) AND " +
           "(:startDate IS NULL OR m.metricDate >= :startDate) AND " +
           "(:endDate IS NULL OR m.metricDate <= :endDate)")
    Page<TrackingMetric> findByFilters(
        @Param("tenantId") String tenantId,
        @Param("metricName") String metricName,
        @Param("metricType") String metricType,
        @Param("eventType") String eventType,
        @Param("source") String source,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );

    @Override
    @Query("SELECT m FROM TrackingMetric m WHERE " +
           "m.metricName = :metricName AND " +
           "m.metricDate = :metricDate AND " +
           "(:metricHour IS NULL OR m.metricHour = :metricHour) AND " +
           "m.tenantId = :tenantId")
    Optional<TrackingMetric> findByUniqueKey(
        @Param("metricName") String metricName,
        @Param("metricDate") LocalDate metricDate,
        @Param("metricHour") Integer metricHour,
        @Param("tenantId") String tenantId
    );

    @Override
    List<TrackingMetric> findByMetricDateBetweenAndTenantId(LocalDate startDate, LocalDate endDate, String tenantId);

    @Override
    void deleteById(UUID id);

    @Override
    @Query("DELETE FROM TrackingMetric m WHERE m.metricDate < :date")
    void deleteByMetricDateBefore(@Param("date") LocalDate date);
}
