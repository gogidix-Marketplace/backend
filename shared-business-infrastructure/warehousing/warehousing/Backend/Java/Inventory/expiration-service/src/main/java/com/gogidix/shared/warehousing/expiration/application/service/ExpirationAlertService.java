package com.gogidix.shared.warehousing.expiration.application.service;

import com.gogidix.shared.warehousing.expiration.application.dto.ExpirationAlertDTO;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert;
import com.gogidix.shared.warehousing.expiration.domain.exception.ExpirationNotFoundException;
import com.gogidix.shared.warehousing.expiration.domain.repository.ExpirationAlertRepository;
import com.gogidix.shared.warehousing.expiration.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Expiration Alert Application Service
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExpirationAlertService {

    private final ExpirationAlertRepository alertRepository;

    /**
     * Create a new expiration alert
     */
    public ExpirationAlertDTO createAlert(ExpirationAlert alert) {
        log.info("Creating expiration alert for SKU: {}", alert.getSku());

        String tenantId = TenantContext.getCurrentTenantId();
        alert.setTenantId(tenantId);

        // Calculate days until expiry
        if (alert.getExpiryDate() != null) {
            alert.setDaysUntilExpiry((int) java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(), alert.getExpiryDate()));
        }

        // Set default values
        if (alert.getSeverity() == null) {
            alert.setSeverity(calculateSeverity(alert.getDaysUntilExpiry()));
        }
        if (alert.getStatus() == null) {
            alert.setStatus(ExpirationAlert.AlertStatus.PENDING);
        }
        if (alert.getNotificationSent() == null) {
            alert.setNotificationSent(false);
        }

        ExpirationAlert saved = alertRepository.save(alert);
        log.info("Expiration alert created with ID: {}", saved.getId());
        return toDTO(saved);
    }

    /**
     * Get alert by ID
     */
    @Transactional(readOnly = true)
    public ExpirationAlertDTO getAlert(String id) {
        ExpirationAlert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ExpirationNotFoundException("ExpirationAlert", id));
        return toDTO(alert);
    }

    /**
     * Get all alerts for current tenant
     */
    @Transactional(readOnly = true)
    public List<ExpirationAlertDTO> getAllAlerts() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ExpirationAlert> alerts = alertRepository.findByTenantId(tenantId);
        return alerts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get alerts by severity
     */
    @Transactional(readOnly = true)
    public List<ExpirationAlertDTO> getAlertsBySeverity(ExpirationAlert.AlertSeverity severity) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ExpirationAlert> alerts = alertRepository.findByTenantIdAndSeverity(tenantId, severity);
        return alerts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get alerts by status
     */
    @Transactional(readOnly = true)
    public List<ExpirationAlertDTO> getAlertsByStatus(ExpirationAlert.AlertStatus status) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ExpirationAlert> alerts = alertRepository.findByTenantIdAndStatus(tenantId, status);
        return alerts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get expiring items (alerts with items expiring within warning period)
     */
    @Transactional(readOnly = true)
    public List<ExpirationAlertDTO> getExpiringItems(int warningDays) {
        String tenantId = TenantContext.getCurrentTenantId();
        LocalDate warningDate = LocalDate.now().plusDays(warningDays);
        List<ExpirationAlert> alerts = alertRepository.findByTenantIdAndExpiryDateBetween(
            tenantId, LocalDate.now(), warningDate);
        return alerts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get expired items
     */
    @Transactional(readOnly = true)
    public List<ExpirationAlertDTO> getExpiredItems() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<ExpirationAlert> alerts = alertRepository.findByTenantIdAndExpiryDateBefore(
            tenantId, LocalDate.now());
        return alerts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Update alert disposition
     */
    public ExpirationAlertDTO updateDisposition(String id, ExpirationAlert.DispositionAction action,
                                                String notes, LocalDate dispositionDate) {
        log.info("Updating disposition for alert: {}", id);

        ExpirationAlert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ExpirationNotFoundException("ExpirationAlert", id));

        alert.setDispositionAction(action);
        alert.setDispositionNotes(notes);
        alert.setDispositionDate(dispositionDate);
        alert.setStatus(ExpirationAlert.AlertStatus.RESOLVED);

        ExpirationAlert updated = alertRepository.save(alert);
        log.info("Alert disposition updated: {}", id);
        return toDTO(updated);
    }

    /**
     * Mark notification as sent
     */
    public ExpirationAlertDTO markNotificationSent(String id, List<String> recipients) {
        log.info("Marking notification sent for alert: {}", id);

        ExpirationAlert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ExpirationNotFoundException("ExpirationAlert", id));

        alert.setNotificationSent(true);
        alert.setNotificationSentAt(LocalDateTime.now());
        alert.setNotificationRecipients(recipients);
        alert.setStatus(ExpirationAlert.AlertStatus.NOTIFIED);

        ExpirationAlert updated = alertRepository.save(alert);
        return toDTO(updated);
    }

    /**
     * Acknowledge alert
     */
    public ExpirationAlertDTO acknowledgeAlert(String id) {
        log.info("Acknowledging alert: {}", id);

        ExpirationAlert alert = alertRepository.findById(id)
            .orElseThrow(() -> new ExpirationNotFoundException("ExpirationAlert", id));

        alert.setStatus(ExpirationAlert.AlertStatus.ACKNOWLEDGED);

        ExpirationAlert updated = alertRepository.save(alert);
        return toDTO(updated);
    }

    /**
     * Delete alert
     */
    public void deleteAlert(String id) {
        log.info("Deleting alert: {}", id);

        if (!alertRepository.existsById(id)) {
            throw new ExpirationNotFoundException("ExpirationAlert", id);
        }

        alertRepository.deleteById(id);
        log.info("Alert deleted: {}", id);
    }

    private ExpirationAlert.AlertSeverity calculateSeverity(Integer daysUntilExpiry) {
        if (daysUntilExpiry == null) {
            return ExpirationAlert.AlertSeverity.INFO;
        }
        if (daysUntilExpiry < 0) {
            return ExpirationAlert.AlertSeverity.EXPIRED;
        } else if (daysUntilExpiry <= 7) {
            return ExpirationAlert.AlertSeverity.CRITICAL;
        } else if (daysUntilExpiry <= 30) {
            return ExpirationAlert.AlertSeverity.WARNING;
        } else {
            return ExpirationAlert.AlertSeverity.INFO;
        }
    }

    private ExpirationAlertDTO toDTO(ExpirationAlert alert) {
        return ExpirationAlertDTO.builder()
            .id(alert.getId())
            .tenantId(alert.getTenantId())
            .sku(alert.getSku())
            .productName(alert.getProductName())
            .expiryDate(alert.getExpiryDate())
            .batchLotId(alert.getBatchLotId())
            .batchLotNumber(alert.getBatchLotNumber())
            .serializedItemId(alert.getSerializedItemId())
            .serialNumber(alert.getSerialNumber())
            .quantity(alert.getQuantity())
            .locationId(alert.getLocationId())
            .locationName(alert.getLocationName())
            .severity(alert.getSeverity())
            .daysUntilExpiry(alert.getDaysUntilExpiry())
            .status(alert.getStatus())
            .notificationSent(alert.getNotificationSent())
            .notificationSentAt(alert.getNotificationSentAt())
            .notificationRecipients(alert.getNotificationRecipients())
            .dispositionAction(alert.getDispositionAction())
            .dispositionDate(alert.getDispositionDate())
            .dispositionNotes(alert.getDispositionNotes())
            .costValue(alert.getCostValue())
            .createdAt(alert.getCreatedAt())
            .updatedAt(alert.getUpdatedAt())
            .build();
    }
}
