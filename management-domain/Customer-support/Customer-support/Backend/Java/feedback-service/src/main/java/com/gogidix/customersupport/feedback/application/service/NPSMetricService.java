package com.gogidix.customersupport.feedback.application.service;

import com.gogidix.customersupport.feedback.application.dto.response.NPSMetricResponseDto;
import com.gogidix.customersupport.feedback.application.mapper.FeedbackMapper;
import com.gogidix.customersupport.feedback.domain.model.NPSMetric;
import com.gogidix.customersupport.feedback.domain.repository.FeedbackRepository;
import com.gogidix.customersupport.feedback.domain.repository.NPSMetricRepository;
import com.gogidix.customersupport.feedback.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Application service for NPS Metric management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NPSMetricService {

    private final NPSMetricRepository metricRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public List<NPSMetricResponseDto> getAllMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all NPS metrics for tenant: {}", tenantId);
        return metricRepository.findByTenantId(tenantId).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<NPSMetricResponseDto> getMetricsByPeriodType(NPSMetric.PeriodType periodType) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching NPS metrics with period type: {} for tenant: {}", periodType, tenantId);
        return metricRepository.findByTenantIdAndPeriodType(tenantId, periodType).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public NPSMetricResponseDto getMetricById(String id) {
        log.debug("Fetching NPS metric with id: {}", id);
        NPSMetric metric = metricRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NPS Metric not found with id: " + id));
        return feedbackMapper.toResponseDto(metric);
    }

    public NPSMetricResponseDto getLatestMetric(NPSMetric.PeriodType periodType) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest NPS metric for period type: {} and tenant: {}", periodType, tenantId);
        return metricRepository.findFirstByTenantIdAndPeriodTypeOrderByPeriodEndDesc(tenantId, periodType)
                .map(feedbackMapper::toResponseDto)
                .orElse(null);
    }

    public List<NPSMetricResponseDto> getMetricsByCountry(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching NPS metrics for country: {} and tenant: {}", countryCode, tenantId);
        return metricRepository.findByTenantIdAndCountryCode(tenantId, countryCode).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<NPSMetricResponseDto> getMetricsByAgent(String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching NPS metrics for agent: {} and tenant: {}", agentId, tenantId);
        return metricRepository.findByTenantIdAndAgentId(tenantId, agentId).stream()
                .map(feedbackMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NPSMetricResponseDto createMetric(NPSMetric.PeriodType periodType, Instant periodStart, Instant periodEnd) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new NPS metric for tenant: {} and period: {} to {}", tenantId, periodStart, periodEnd);

        NPSMetric metric = NPSMetric.create(tenantId, periodStart, periodEnd, periodType);

        NPSMetric saved = metricRepository.save(metric);

        log.info("Created NPS metric with metricId: {} for tenant: {}", saved.getMetricId(), tenantId);
        return feedbackMapper.toResponseDto(saved);
    }

    @Transactional
    public NPSMetricResponseDto calculateMetric(String metricId) {
        log.debug("Calculating NPS metric: {}", metricId);

        NPSMetric metric = metricRepository.findByMetricId(metricId)
                .orElseThrow(() -> new IllegalArgumentException("NPS Metric not found with metricId: " + metricId));

        List<com.gogidix.customersupport.feedback.domain.model.Feedback> feedbackList =
                feedbackRepository.findByTenantIdAndCreatedAtBetween(
                        metric.getTenantId(),
                        metric.getPeriodStart(),
                        metric.getPeriodEnd()
                );

        metric.setPromotersCount(0);
        metric.setPassivesCount(0);
        metric.setDetractorsCount(0);
        metric.setTotalResponses(0);

        for (com.gogidix.customersupport.feedback.domain.model.Feedback feedback : feedbackList) {
            if (feedback.getNpsScore() != null) {
                metric.addResponse(feedback.getNpsScore());
            }
        }

        metric.calculateNPS();

        NPSMetric updated = metricRepository.save(metric);
        log.info("Calculated NPS metric: {} with score: {}", metricId, updated.getNpsScore());
        return feedbackMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteMetric(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting NPS metric with id: {} for tenant: {}", id, tenantId);

        NPSMetric metric = metricRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("NPS Metric not found with id: " + id));

        metricRepository.delete(metric);

        log.info("Deleted NPS metric: {} for tenant: {}", metric.getMetricId(), tenantId);
    }

    @Transactional
    public NPSMetricResponseDto generateDailyMetric(String countryCode, String agentId) {
        String tenantId = RequestContextHolder.getTenantId();
        LocalDate today = LocalDate.now();
        Instant periodStart = today.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        Instant periodEnd = today.plusDays(1).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();

        NPSMetric metric = NPSMetric.create(tenantId, periodStart, periodEnd, NPSMetric.PeriodType.DAILY);
        metric.setCountryCode(countryCode);
        metric.setAgentId(agentId);

        NPSMetric saved = metricRepository.save(metric);
        return calculateMetric(saved.getMetricId());
    }

    @Transactional
    public NPSMetricResponseDto generateWeeklyMetric(String countryCode, String teamId) {
        String tenantId = RequestContextHolder.getTenantId();
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(today.getDayOfWeek().getValue() - 1);
        Instant periodStart = weekStart.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        Instant periodEnd = weekStart.plusWeeks(1).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();

        NPSMetric metric = NPSMetric.create(tenantId, periodStart, periodEnd, NPSMetric.PeriodType.WEEKLY);
        metric.setCountryCode(countryCode);
        metric.setTeamId(teamId);

        NPSMetric saved = metricRepository.save(metric);
        return calculateMetric(saved.getMetricId());
    }

    @Transactional
    public NPSMetricResponseDto generateMonthlyMetric(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        Instant periodStart = monthStart.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();
        Instant periodEnd = monthStart.plusMonths(1).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant();

        NPSMetric metric = NPSMetric.create(tenantId, periodStart, periodEnd, NPSMetric.PeriodType.MONTHLY);
        metric.setCountryCode(countryCode);

        NPSMetric saved = metricRepository.save(metric);
        return calculateMetric(saved.getMetricId());
    }
}
