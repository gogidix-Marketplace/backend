package com.gogidix.monitoring.servicehealthservice.application.service;

import com.gogidix.monitoring.monitoringdataservice.domain.model.ServiceRegistration;
import com.gogidix.monitoring.monitoringdataservice.domain.port.out.ServiceRegistrationRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.domain.model.ServiceHealthStatus;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceHealthStatusRepositoryPort;
import com.gogidix.monitoring.servicehealthservice.domain.port.out.ServiceUptimeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for scheduling health checks.
 * Runs periodic health checks on all registered services.
 */
@Service
public class HealthCheckSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckSchedulerService.class);

    private final ServiceRegistrationRepositoryPort registrationRepository;
    private final ServiceHealthStatusRepositoryPort healthStatusRepository;
    private final ServiceUptimeRepositoryPort uptimeRepository;
    private final RestTemplate restTemplate;

    public HealthCheckSchedulerService(
            ServiceRegistrationRepositoryPort registrationRepository,
            ServiceHealthStatusRepositoryPort healthStatusRepository,
            ServiceUptimeRepositoryPort uptimeRepository,
            RestTemplate restTemplate) {
        this.registrationRepository = registrationRepository;
        this.healthStatusRepository = healthStatusRepository;
        this.uptimeRepository = uptimeRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Run health checks on all registered services.
     * Runs every 30 seconds.
     */
    @Scheduled(fixedDelay = 30000, initialDelay = 10000)
    @Transactional
    public void performHealthChecks() {
        log.debug("Starting health check cycle");

        // Get all unique tenant IDs (in production, this would be more sophisticated)
        List<String> tenantIds = List.of("default");

        for (String tenantId : tenantIds) {
            List<ServiceRegistration> services = registrationRepository.findActiveByTenantId(tenantId);

            for (ServiceRegistration service : services) {
                try {
                    checkServiceHealth(service);
                } catch (Exception e) {
                    log.error("Error checking health for service: {}", service.getServiceName(), e);
                }
            }
        }

        log.debug("Health check cycle completed");
    }

    /**
     * Check health of a single service.
     */
    private void checkServiceHealth(ServiceRegistration service) {
        long startTime = System.currentTimeMillis();
        boolean isHealthy = false;
        String errorMessage = null;

        try {
            String healthUrl = service.getHealthCheckUrl();
            if (healthUrl == null || healthUrl.isBlank()) {
                healthUrl = service.getBaseUrl() + "/actuator/health";
            }

            Map<String, Object> healthResponse = restTemplate.getForObject(healthUrl, Map.class);
            isHealthy = Boolean.TRUE.equals(healthResponse != null ? healthResponse.get("status") : null);

        } catch (Exception e) {
            errorMessage = e.getMessage();
            isHealthy = false;
        }

        long responseTime = System.currentTimeMillis() - startTime;

        // Update health status
        updateHealthStatus(service, isHealthy, responseTime, errorMessage);

        // Track uptime
        trackUptime(service, isHealthy);
    }

    /**
     * Update the health status for a service.
     */
    private void updateHealthStatus(ServiceRegistration service, boolean isHealthy, long responseTime, String errorMessage) {
        healthStatusRepository.findByTenantAndService(service.getTenantId(), service.getServiceName())
                .ifPresentOrElse(
                        status -> {
                            status.setLastCheckAt(Instant.now());
                            status.setAverageResponseTime(calculateAverageResponseTime(status, responseTime));

                            if (isHealthy) {
                                status.setStatus(ServiceHealthStatus.HealthStatus.HEALTHY);
                                status.setHealthScore(calculateHealthScore(status));
                                status.setConsecutiveFailures(0);
                                status.setLastSuccessAt(Instant.now());
                            } else {
                                status.setConsecutiveFailures((status.getConsecutiveFailures() != null ? status.getConsecutiveFailures() : 0) + 1);
                                status.setLastFailureAt(Instant.now());

                                if (status.getConsecutiveFailures() >= 3) {
                                    status.setStatus(ServiceHealthStatus.HealthStatus.DOWN);
                                } else {
                                    status.setStatus(ServiceHealthStatus.HealthStatus.UNHEALTHY);
                                }
                                status.setHealthScore(calculateHealthScore(status));
                            }

                            // Update error rate
                            status.setErrorRate(calculateErrorRate(status));

                            // Update details
                            Map<String, Object> details = new HashMap<>();
                            if (status.getDetails() != null) {
                                details.putAll(status.getDetails());
                            }
                            details.put("lastCheckResponseTime", responseTime);
                            if (errorMessage != null) {
                                details.put("lastError", errorMessage);
                            }
                            status.setDetails(details);

                            healthStatusRepository.save(status);
                        },
                        () -> {
                            // Create new health status
                            ServiceHealthStatus newStatus = ServiceHealthStatus.builder()
                                    .tenantId(service.getTenantId())
                                    .serviceName(service.getServiceName())
                                    .serviceType(service.getServiceType())
                                    .status(isHealthy ? ServiceHealthStatus.HealthStatus.HEALTHY : ServiceHealthStatus.HealthStatus.DOWN)
                                    .healthScore(isHealthy ? 100 : 0)
                                    .lastCheckAt(Instant.now())
                                    .consecutiveFailures(isHealthy ? 0 : 1)
                                    .lastSuccessAt(isHealthy ? Instant.now() : null)
                                    .lastFailureAt(isHealthy ? null : Instant.now())
                                    .averageResponseTime((double) responseTime)
                                    .errorRate(isHealthy ? 0.0 : 1.0)
                                    .uptimePercentage(isHealthy ? 100.0 : 0.0)
                                    .createdAt(Instant.now())
                                    .build();

                            Map<String, Object> details = new HashMap<>();
                            details.put("lastCheckResponseTime", responseTime);
                            if (errorMessage != null) {
                                details.put("lastError", errorMessage);
                            }
                            newStatus.setDetails(details);

                            healthStatusRepository.save(newStatus);
                        }
                );
    }

    /**
     * Track uptime incidents.
     */
    private void trackUptime(ServiceRegistration service, boolean isHealthy) {
        List<com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime> ongoingIncidents =
                uptimeRepository.findOngoingByTenantAndService(service.getTenantId(), service.getServiceName());

        if (!isHealthy && ongoingIncidents.isEmpty()) {
            // Service went down - create new incident
            com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime incident =
                    com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime.builder()
                            .tenantId(service.getTenantId())
                            .serviceName(service.getServiceName())
                            .incidentType(com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime.IncidentType.DOWN)
                            .startedAt(Instant.now())
                            .scheduledMaintenance(false)
                            .createdAt(Instant.now())
                            .build();
            uptimeRepository.save(incident);
            log.warn("Service {} is DOWN - incident created", service.getServiceName());

        } else if (isHealthy && !ongoingIncidents.isEmpty()) {
            // Service recovered - end ongoing incidents
            for (com.gogidix.monitoring.servicehealthservice.domain.model.ServiceUptime incident : ongoingIncidents) {
                incident.endIncident();
                uptimeRepository.save(incident);
                log.info("Service {} is UP - incident ended after {} seconds",
                        service.getServiceName(), incident.getDurationSeconds());
            }
        }
    }

    /**
     * Calculate health score based on various factors.
     */
    private Integer calculateHealthScore(ServiceHealthStatus status) {
        double uptimeScore = status.getUptimePercentage() != null ? status.getUptimePercentage() : 50.0;

        double responseTimeScore = 100.0;
        if (status.getAverageResponseTime() != null) {
            if (status.getAverageResponseTime() < 100) {
                responseTimeScore = 100.0;
            } else if (status.getAverageResponseTime() < 500) {
                responseTimeScore = 80.0;
            } else if (status.getAverageResponseTime() < 1000) {
                responseTimeScore = 60.0;
            } else {
                responseTimeScore = 40.0;
            }
        }

        double errorScore = 100.0;
        if (status.getErrorRate() != null) {
            errorScore = (1.0 - status.getErrorRate()) * 100.0;
        }

        // Weighted average: uptime 40%, response time 30%, error rate 30%
        double finalScore = (uptimeScore * 0.4) + (responseTimeScore * 0.3) + (errorScore * 0.3);

        return (int) Math.max(0, Math.min(100, finalScore));
    }

    /**
     * Calculate average response time using exponential moving average.
     */
    private Double calculateAverageResponseTime(ServiceHealthStatus status, long newResponseTime) {
        double currentAvg = status.getAverageResponseTime() != null ? status.getAverageResponseTime() : newResponseTime;
        return currentAvg * 0.9 + newResponseTime * 0.1;
    }

    /**
     * Calculate error rate.
     */
    private Double calculateErrorRate(ServiceHealthStatus status) {
        if (status.getConsecutiveFailures() == null || status.getConsecutiveFailures() == 0) {
            return 0.0;
        }
        // Simple calculation: consecutive failures / (consecutive failures + 1)
        return (double) status.getConsecutiveFailures() / (status.getConsecutiveFailures() + 1);
    }
}
