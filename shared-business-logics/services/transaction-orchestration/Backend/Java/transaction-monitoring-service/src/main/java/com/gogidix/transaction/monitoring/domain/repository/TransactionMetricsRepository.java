package com.gogidix.transaction.monitoring.domain.repository;

import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics;
import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionMetricsRepository extends JpaRepository<TransactionMetrics, UUID> {

    List<TransactionMetrics> findByTransactionIdOrderByTimestampDesc(UUID transactionId);

    List<TransactionMetrics> findByMetricTypeAndTimestampBetweenOrderByTimestampDesc(
        MetricType metricType, LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM TransactionMetrics m WHERE m.transactionId = :transactionId " +
           "AND m.metricType = :metricType AND m.timestamp >= :since ORDER BY m.timestamp DESC")
    List<TransactionMetrics> findRecentMetrics(
        @Param("transactionId") UUID transactionId,
        @Param("metricType") MetricType metricType,
        @Param("since") LocalDateTime since
    );

    @Query("SELECT m FROM TransactionMetrics m WHERE m.transactionId = :transactionId " +
           "AND m.metricType = :metricType AND m.timestamp BETWEEN :start AND :end")
    List<TransactionMetrics> findMetricsForAverage(
        @Param("transactionId") UUID transactionId,
        @Param("metricType") MetricType metricType,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );

    @Query("SELECT m FROM TransactionMetrics m WHERE m.metricType = :metricType " +
           "ORDER BY m.timestamp DESC")
    List<TransactionMetrics> findTop50ByMetricTypeOrderByTimestampDesc(@Param("metricType") MetricType metricType);

    void deleteByTransactionId(UUID transactionId);

    long countByTransactionId(UUID transactionId);

    @Query("SELECT AVG(m.metricValue) FROM TransactionMetrics m WHERE m.transactionId = :transactionId " +
           "AND m.metricType = :metricType AND m.timestamp BETWEEN :start AND :end")
    Double getAverageMetricValue(
        @Param("transactionId") UUID transactionId,
        @Param("metricType") MetricType metricType,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}
