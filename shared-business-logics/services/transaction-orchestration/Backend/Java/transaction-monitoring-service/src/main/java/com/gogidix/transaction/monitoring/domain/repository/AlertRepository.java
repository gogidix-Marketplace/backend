package com.gogidix.transaction.monitoring.domain.repository;

import com.gogidix.transaction.monitoring.domain.entity.Alert;
import com.gogidix.transaction.monitoring.domain.entity.Alert.AlertSeverity;
import com.gogidix.transaction.monitoring.domain.entity.Alert.AlertStatus;
import com.gogidix.transaction.monitoring.domain.entity.Alert.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {

    List<Alert> findByTransactionIdOrderByCreatedAtDesc(UUID transactionId);

    List<Alert> findByStatusOrderByCreatedAtDesc(AlertStatus status);

    List<Alert> findBySeverityAndStatusOrderByCreatedAtDesc(AlertSeverity severity, AlertStatus status);

    List<Alert> findByAlertTypeAndStatusOrderByCreatedAtDesc(AlertType alertType, AlertStatus status);

    @Query("SELECT a FROM Alert a WHERE a.status IN :statuses ORDER BY a.createdAt DESC")
    List<Alert> findByStatusInOrderByCreatedAtDesc(@Param("statuses") List<AlertStatus> statuses);

    @Query("SELECT a FROM Alert a WHERE a.severity = :severity AND a.status = :status " +
           "AND a.createdAt >= :since ORDER BY a.createdAt DESC")
    List<Alert> findRecentAlertsBySeverityAndStatus(
        @Param("severity") AlertSeverity severity,
        @Param("status") AlertStatus status,
        @Param("since") LocalDateTime since
    );

    long countByStatus(AlertStatus status);

    long countBySeverityAndStatus(AlertSeverity severity, AlertStatus status);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.createdAt >= :since")
    long countRecentAlerts(@Param("since") LocalDateTime since);

    List<Alert> findBySeverityOrderByCreatedAtDesc(AlertSeverity severity);
}
