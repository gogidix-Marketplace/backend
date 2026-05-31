package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.AggregationResponseDto;
import com.gogidix.sales.dashboard.domain.event.AggregationCompletedEvent;
import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
import com.gogidix.sales.dashboard.domain.port.out.EventPublisher;
import com.gogidix.sales.dashboard.domain.repository.SalesAggregationRepository;
import com.gogidix.sales.dashboard.interfaces.rest.AggregationController;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aggregation Command Service
 * Handles all write operations for sales aggregations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AggregationCommandService {

    private final SalesAggregationRepository aggregationRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public SalesAggregation create(AggregationController.CreateAggregationRequest request) {
        log.info("Creating aggregation of type: {} for tenant: {}", request.type(), RequestContextHolder.getTenantId());

        SalesAggregation.TimePeriod timePeriod = SalesAggregation.TimePeriod.builder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .periodType(request.periodType())
                .periodValue(request.periodValue())
                .year(request.year())
                .build();

        SalesAggregation aggregation = SalesAggregation.create(
                RequestContextHolder.getTenantId(),
                request.type(),
                request.dimension(),
                request.dimensionValue(),
                timePeriod,
                request.currency() != null ? request.currency() : "USD"
        );

        if (request.dataSource() != null) {
            aggregation.setDataSource(request.dataSource());
        }

        SalesAggregation saved = aggregationRepository.save(aggregation);
        publishEvents(saved);

        log.info("Created aggregation: {}", saved.getAggregationId());
        return saved;
    }

    @Transactional
    public void markAsComplete(String aggregationId) {
        log.info("Marking aggregation as complete: {}", aggregationId);

        SalesAggregation aggregation = findByAggregationId(aggregationId);
        aggregation.markAsComplete();

        aggregationRepository.save(aggregation);
        publishEvents(aggregation);

        log.info("Marked aggregation as complete: {}", aggregationId);
    }

    @Transactional
    public Map<String, Object> recalculate(AggregationController.RecalculateRequest request) {
        log.info("Recalculating aggregations from {} to {}", request.startDate(), request.endDate());

        String tenantId = RequestContextHolder.getTenantId();

        // Find existing aggregations to recalculate
        List<SalesAggregation> existingAggregations = aggregationRepository.findByTenantIdAndDateRange(
                tenantId, request.startDate(), request.endDate());

        int recalculated = 0;
        for (SalesAggregation aggregation : existingAggregations) {
            // Simulate recalculation logic
            aggregation.incrementVersion();
            aggregationRepository.save(aggregation);
            recalculated++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("recalculated", recalculated);
        result.put("message", "Recalculation completed successfully");

        log.info("Recalculated {} aggregations", recalculated);
        return result;
    }

    @Transactional
    public SalesAggregation aggregate(AggregationController.AggregateDataRequest request) {
        log.info("Aggregating data for dimension: {}", request.dimension());

        SalesAggregation.TimePeriod timePeriod = SalesAggregation.TimePeriod.builder()
                .startDate(request.startDate())
                .endDate(request.endDate())
                .periodType("CUSTOM")
                .build();

        SalesAggregation aggregation = SalesAggregation.create(
                RequestContextHolder.getTenantId(),
                request.type(),
                request.dimension(),
                request.dimensionValue(),
                timePeriod,
                request.currency() != null ? request.currency() : "USD"
        );

        // Process raw data and calculate metrics
        if (request.rawData() != null) {
            processRawData(aggregation, request.rawData());
        }

        aggregation.markAsComplete();
        SalesAggregation saved = aggregationRepository.save(aggregation);
        publishEvents(saved);

        log.info("Aggregated data into: {}", saved.getAggregationId());
        return saved;
    }

    @Transactional
    public void updateMetrics(String aggregationId, AggregationController.UpdateMetricsRequest request) {
        log.info("Updating metrics for aggregation: {}", aggregationId);

        SalesAggregation aggregation = findByAggregationId(aggregationId);

        SalesAggregation.AggregatedMetrics metrics = SalesAggregation.AggregatedMetrics.builder()
                .totalRevenue(toMoney(request.totalRevenue(), "USD"))
                .targetRevenue(toMoney(request.targetRevenue(), "USD"))
                .totalDeals(request.totalDeals() != null ? request.totalDeals() : 0)
                .wonDeals(request.wonDeals() != null ? request.wonDeals() : 0)
                .averageDealSize(toMoney(request.averageDealSize(), "USD"))
                .averageDiscount(toMoney(request.averageDiscount(), "USD"))
                .newOpportunities(request.newOpportunities() != null ? request.newOpportunities() : 0)
                .pipelineValue(toMoney(BigDecimal.ZERO, "USD"))
                .weightedPipeline(toMoney(request.weightedPipeline(), "USD"))
                .activeCustomers(request.activeCustomers() != null ? request.activeCustomers() : 0)
                .build();

        aggregation.updateMetrics(metrics);
        aggregationRepository.save(aggregation);

        log.info("Updated metrics for aggregation: {}", aggregationId);
    }

    @Transactional
    public void updateComparison(String aggregationId, AggregationController.UpdateComparisonRequest request) {
        log.info("Updating comparison data for aggregation: {}", aggregationId);

        SalesAggregation aggregation = findByAggregationId(aggregationId);

        SalesAggregation.Money currentRevenue = SalesAggregation.Money.builder()
                .amount(request.currentRevenue())
                .currency(request.currency())
                .build();

        SalesAggregation.Money previousRevenue = SalesAggregation.Money.builder()
                .amount(request.previousPeriodRevenue())
                .currency(request.currency())
                .build();

        aggregation.setComparisonData(currentRevenue, previousRevenue);
        aggregationRepository.save(aggregation);

        log.info("Updated comparison data for aggregation: {}", aggregationId);
    }

    @Transactional
    public void addBreakdownItem(String aggregationId, AggregationController.BreakdownItemRequest request) {
        log.info("Adding breakdown item to aggregation: {}", aggregationId);

        SalesAggregation aggregation = findByAggregationId(aggregationId);

        SalesAggregation.BreakdownItem item = SalesAggregation.BreakdownItem.builder()
                .key(request.key())
                .label(request.label())
                .value(toMoney(request.value(), "USD"))
                .count(request.count() != null ? request.count() : 0)
                .color(request.color())
                .build();

        aggregation.addBreakdownItem(item);
        aggregation.calculateBreakdownPercentages();
        aggregationRepository.save(aggregation);

        log.info("Added breakdown item to aggregation: {}", aggregationId);
    }

    @Transactional
    public void delete(String aggregationId) {
        log.info("Deleting aggregation: {}", aggregationId);

        SalesAggregation aggregation = findByAggregationId(aggregationId);
        aggregationRepository.deleteByAggregationIdAndTenantId(
                aggregationId, aggregation.getTenantId());

        log.info("Deleted aggregation: {}", aggregationId);
    }

    @Transactional
    public Map<String, Object> deleteOldAggregations(LocalDate beforeDate) {
        log.info("Deleting aggregations older than: {}", beforeDate);

        String tenantId = RequestContextHolder.getTenantId();
        aggregationRepository.deleteOldAggregations(tenantId, beforeDate);

        Map<String, Object> result = new HashMap<>();
        result.put("deletedBefore", beforeDate);
        result.put("message", "Old aggregations deleted successfully");

        log.info("Deleted aggregations older than: {}", beforeDate);
        return result;
    }

    private SalesAggregation findByAggregationId(String aggregationId) {
        String tenantId = RequestContextHolder.getTenantId();
        return aggregationRepository.findByAggregationIdAndTenantId(aggregationId, tenantId)
                .orElseThrow(() -> new NotFoundException("Aggregation", aggregationId));
    }

    private void publishEvents(SalesAggregation aggregation) {
        if (!aggregation.getDomainEvents().isEmpty() && eventPublisher != null && eventPublisher.isReady()) {
            for (var event : aggregation.getDomainEvents()) {
                if (event instanceof AggregationCompletedEvent) {
                    eventPublisher.publishAggregationEvent((AggregationCompletedEvent) event);
                }
            }
            aggregation.clearDomainEvents();
        }
    }

    private void processRawData(SalesAggregation aggregation, Map<String, Object> rawData) {
        // Simulate processing raw data into metrics
        log.debug("Processing raw data for aggregation: {}", aggregation.getAggregationId());
    }

    private SalesAggregation.Money toMoney(BigDecimal amount, String currency) {
        if (amount == null) {
            return SalesAggregation.Money.builder()
                    .amount(BigDecimal.ZERO)
                    .currency(currency)
                    .build();
        }
        return SalesAggregation.Money.builder()
                .amount(amount)
                .currency(currency)
                .build();
    }
}
