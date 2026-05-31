package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.RollupResponseDto;
import com.gogidix.sales.dashboard.domain.event.RollupCompletedEvent;
import com.gogidix.sales.dashboard.domain.model.MetricRollup;
import com.gogidix.sales.dashboard.domain.port.out.EventPublisher;
import com.gogidix.sales.dashboard.domain.repository.MetricRollupRepository;
import com.gogidix.sales.dashboard.interfaces.rest.RollupController;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rollup Command Service
 * Handles all write operations for metric rollups
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RollupCommandService {

    private final MetricRollupRepository rollupRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public MetricRollup create(RollupController.CreateRollupRequest request) {
        log.info("Creating rollup of type: {} for key: {}", request.type(), request.key());

        MetricRollup.TimePeriod timePeriod = MetricRollup.TimePeriod.builder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .periodType(request.periodType())
                .periodValue(request.periodValue())
                .year(request.year())
                .quarter(request.quarter())
                .build();

        MetricRollup rollup = MetricRollup.create(
                RequestContextHolder.getTenantId(),
                request.type(),
                request.key(),
                request.name(),
                timePeriod,
                request.currency() != null ? request.currency() : "USD"
        );

        if (request.parentRollupId() != null) {
            rollup.setParentRollupId(request.parentRollupId());
        }

        MetricRollup saved = rollupRepository.save(rollup);
        publishEvents(saved);

        log.info("Created rollup: {}", saved.getRollupId());
        return saved;
    }

    @Transactional
    public void aggregateFromChildren(String parentRollupId) {
        log.info("Aggregating from children for rollup: {}", parentRollupId);

        MetricRollup parentRollup = findByRollupId(parentRollupId);
        List<MetricRollup> children = rollupRepository.findChildRollups(parentRollupId, parentRollup.getTenantId());

        if (!children.isEmpty()) {
            parentRollup.aggregateFromChildren(children);
            rollupRepository.save(parentRollup);
            publishEvents(parentRollup);

            log.info("Aggregated {} children into rollup: {}", children.size(), parentRollupId);
        } else {
            log.warn("No children found for rollup: {}", parentRollupId);
        }
    }

    @Transactional
    public List<MetricRollup> createBatch(List<RollupController.CreateRollupRequest> requests) {
        log.info("Creating batch of {} rollups", requests.size());

        List<MetricRollup> rollups = requests.stream()
                .map(this::create)
                .toList();

        log.info("Created {} rollups in batch", rollups.size());
        return rollups;
    }

    @Transactional
    public void updateMetrics(String rollupId, RollupController.UpdateMetricsRequest request) {
        log.info("Updating metrics for rollup: {}", rollupId);

        MetricRollup rollup = findByRollupId(rollupId);
        String currency = "USD"; // Default, could be retrieved from rollup

        MetricRollup.RollupMetrics metrics = MetricRollup.RollupMetrics.builder()
                .revenue(toMoney(request.revenue(), currency))
                .deals(request.deals() != null ? request.deals() : 0)
                .winRate(request.winRate() != null ? request.winRate() : BigDecimal.ZERO)
                .pipelineValue(toMoney(request.pipelineValue(), currency))
                .opportunities(request.opportunities() != null ? request.opportunities() : 0)
                .averageDealSize(toMoney(request.averageDealSize(), currency))
                .growthRate(request.growthRate() != null ? request.growthRate() : BigDecimal.ZERO)
                .newCustomers(request.newCustomers() != null ? request.newCustomers() : 0)
                .margin(toMoney(request.margin(), currency))
                .build();

        rollup.updateMetrics(metrics);
        rollupRepository.save(rollup);

        log.info("Updated metrics for rollup: {}", rollupId);
    }

    @Transactional
    public void updateTarget(String rollupId, RollupController.UpdateTargetRequest request) {
        log.info("Updating target {} for rollup: {}", request.targetType(), rollupId);

        MetricRollup rollup = findByRollupId(rollupId);
        String currency = "USD";

        rollup.updateTarget(
                request.targetType(),
                toMoney(request.targetValue(), currency),
                toMoney(request.currentValue(), currency)
        );

        rollupRepository.save(rollup);

        log.info("Updated target for rollup: {}", rollupId);
    }

    @Transactional
    public MetricRollup refresh(String rollupId) {
        log.info("Refreshing rollup: {}", rollupId);

        MetricRollup rollup = findByRollupId(rollupId);

        // Simulate refresh by incrementing version
        rollup.incrementVersion();
        MetricRollup saved = rollupRepository.save(rollup);

        log.info("Refreshed rollup: {}", rollupId);
        return saved;
    }

    @Transactional
    public Map<String, Object> refreshAll() {
        log.info("Refreshing all rollups for tenant: {}", RequestContextHolder.getTenantId());

        List<MetricRollup> rollups = rollupRepository.findByTenantId(RequestContextHolder.getTenantId());
        int refreshed = 0;

        for (MetricRollup rollup : rollups) {
            rollup.incrementVersion();
            rollupRepository.save(rollup);
            refreshed++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("refreshed", refreshed);
        result.put("message", "All rollups refreshed successfully");

        log.info("Refreshed {} rollups", refreshed);
        return result;
    }

    @Transactional
    public void recalculateScore(String rollupId) {
        log.info("Recalculating score for rollup: {}", rollupId);

        MetricRollup rollup = findByRollupId(rollupId);
        rollup.updateMetrics(rollup.getMetrics());
        rollupRepository.save(rollup);

        log.info("Recalculated score for rollup: {}", rollupId);
    }

    @Transactional
    public void delete(String rollupId) {
        log.info("Deleting rollup: {}", rollupId);

        MetricRollup rollup = findByRollupId(rollupId);
        rollupRepository.deleteByRollupIdAndTenantId(rollupId, rollup.getTenantId());

        log.info("Deleted rollup: {}", rollupId);
    }

    @Transactional
    public Map<String, Object> deleteOldRollups(LocalDate beforeDate) {
        log.info("Deleting rollups older than: {}", beforeDate);

        String tenantId = RequestContextHolder.getTenantId();
        rollupRepository.deleteOldRollups(tenantId, beforeDate);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedBefore", beforeDate);
        result.put("message", "Old rollups deleted successfully");

        log.info("Deleted rollups older than: {}", beforeDate);
        return result;
    }

    private MetricRollup findByRollupId(String rollupId) {
        String tenantId = RequestContextHolder.getTenantId();
        return rollupRepository.findByRollupIdAndTenantId(rollupId, tenantId)
                .orElseThrow(() -> new NotFoundException("Rollup", rollupId));
    }

    private void publishEvents(MetricRollup rollup) {
        if (!rollup.getDomainEvents().isEmpty() && eventPublisher != null && eventPublisher.isReady()) {
            for (var event : rollup.getDomainEvents()) {
                if (event instanceof RollupCompletedEvent) {
                    eventPublisher.publishRollupEvent((RollupCompletedEvent) event);
                }
            }
            rollup.clearDomainEvents();
        }
    }

    private MetricRollup.Money toMoney(BigDecimal amount, String currency) {
        if (amount == null) {
            return MetricRollup.Money.builder()
                    .amount(BigDecimal.ZERO)
                    .currency(currency)
                    .build();
        }
        return MetricRollup.Money.builder()
                .amount(amount)
                .currency(currency)
                .build();
    }
}
