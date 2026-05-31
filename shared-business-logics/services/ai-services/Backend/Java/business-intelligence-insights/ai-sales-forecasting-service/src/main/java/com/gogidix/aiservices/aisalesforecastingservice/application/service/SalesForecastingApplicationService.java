package com.gogidix.aiservices.aisalesforecastingservice.application.service;

import com.gogidix.aiservices.aisalesforecastingservice.application.command.AddForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.AnalyzeForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.CreateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.DeleteForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.RemoveForecastModelsCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.command.UpdateForecastCommand;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.SalesForecastingResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.dto.ForecastAnalysisResponseDto;
import com.gogidix.aiservices.aisalesforecastingservice.application.mapper.SalesForecastingMapper;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.SalesForecast;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastCriteria;
import com.gogidix.aiservices.aisalesforecastingservice.domain.model.ForecastType;
import com.gogidix.aiservices.aisalesforecastingservice.domain.port.out.SalesForecastingRepositoryPort;
import com.gogidix.aiservices.aisalesforecastingservice.domain.policy.ForecastBusinessPolicy;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.ValidationException;
import com.gogidix.aiservices.aisalesforecastingservice.shared.context.RequestContext;
import com.gogidix.aiservices.aisalesforecastingservice.shared.context.RequestContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Application service for sales forecast operations.
 */
@Service
@Transactional(readOnly = true)
public class SalesForecastingApplicationService {

    private static final Logger log = LoggerFactory.getLogger(SalesForecastingApplicationService.class);
    private static final String CACHE_NAME = "segments";

    private final SalesForecastingRepositoryPort repository;
    private final SalesForecastingMapper mapper;
    private final ForecastBusinessPolicy businessPolicy;
    private final ForecastAnalysisService analysisService;

    public SalesForecastingApplicationService(
            SalesForecastingRepositoryPort repository,
            SalesForecastingMapper mapper,
            ForecastBusinessPolicy businessPolicy,
            ForecastAnalysisService analysisService) {
        this.repository = repository;
        this.mapper = mapper;
        this.businessPolicy = businessPolicy;
        this.analysisService = analysisService;
    }

    /**
     * Create a new segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public SalesForecastingResponseDto createForecast(CreateForecastCommand command) {
        log.info("Creating segment '{}' for tenant: {}", command.name(), command.tenantId());

        businessPolicy.validateForecastCreation(command.tenantId());

        // Convert Map<String, Object> criteria to ForecastCriteria
        ForecastCriteria criteria = mapToForecastCriteria(command.criteria());

        SalesForecast segment = new SalesForecast(
                command.tenantId(),
                command.name(),
                criteria
        );

        SalesForecast saved = repository.save(segment);
        log.info("Forecast created with ID: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Update an existing segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public SalesForecastingResponseDto updateForecast(UpdateForecastCommand command) {
        log.info("Updating segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        SalesForecast existing = findForecastByIdAndTenant(command.segmentId(), command.tenantId());

        if (command.name() != null) {
            existing.updateDetails(command.name(), command.description());
        }

        SalesForecast saved = repository.save(existing);
        log.info("Forecast updated: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a segment.
     */
    @Transactional
    public void deleteForecast(String segmentId, String tenantId, String userId) {
        log.info("Deleting segment: {} for tenant: {}", segmentId, tenantId);

        SalesForecast segment = findForecastByIdAndTenant(segmentId, tenantId);
        businessPolicy.validateForecastDeletion(segment);

        repository.deleteById(segmentId);
        log.info("Forecast deleted: {}", segmentId);
    }

    /**
     * Get a segment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#segmentId + ':' + #tenantId")
    public SalesForecastingResponseDto getForecastById(String segmentId, String tenantId) {
        log.debug("Fetching segment: {} for tenant: {}", segmentId, tenantId);

        SalesForecast segment = findForecastByIdAndTenant(segmentId, tenantId);
        return mapper.toResponseDto(segment);
    }

    /**
     * List segments for a tenant with pagination.
     */
    public PagedResponseDto<SalesForecastingResponseDto> getForecastsByTenant(
            String tenantId,
            ForecastType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching segments for tenant: {}", tenantId);

        // For now, return empty page since repository doesn't have this method
        List<SalesForecastingResponseDto> content = new ArrayList<>();
        return new PagedResponseDto<>(
                content,
                page,
                size,
                0,
                0,
                true,
                true
        );
    }

    /**
     * Add customers to a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public SalesForecastingResponseDto addForecastModelsToForecast(AddForecastModelsCommand command) {
        log.info("Adding {} customers to segment: {}", command.customerIds().size(), command.segmentId());

        SalesForecast segment = findForecastByIdAndTenant(command.segmentId(), command.tenantId());
        businessPolicy.validateForecastModelAddition(segment, command.customerIds().size());

        segment.addForecastModels(command.customerIds());
        SalesForecast saved = repository.save(segment);

        log.info("Added {} customers to segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Remove customers from a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public SalesForecastingResponseDto removeForecastModelsFromForecast(RemoveForecastModelsCommand command) {
        log.info("Removing {} customers from segment: {}", command.customerIds().size(), command.segmentId());

        SalesForecast segment = findForecastByIdAndTenant(command.segmentId(), command.tenantId());

        segment.removeForecastModels(command.customerIds());
        SalesForecast saved = repository.save(segment);

        log.info("Removed {} customers from segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Analyze a segment.
     */
    public ForecastAnalysisResponseDto analyzeForecast(AnalyzeForecastCommand command) {
        log.info("Analyzing segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        SalesForecast segment = findForecastByIdAndTenant(command.segmentId(), command.tenantId());

        if (!segment.isActive()) {
            throw new ValidationException("Cannot analyze inactive segment");
        }

        return analysisService.analyzeForecast(segment, command.analysisOptions());
    }

    /**
     * Helper method to find a segment by ID and tenant.
     */
    private SalesForecast findForecastByIdAndTenant(String segmentId, String tenantId) {
        return repository.findById(segmentId)
                .filter(seg -> seg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        "Forecast not found with ID: " + segmentId + " for tenant: " + tenantId));
    }

    /**
     * Convert Map<String, Object> to ForecastCriteria.
     */
    private ForecastCriteria mapToForecastCriteria(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }

        String field = (String) criteria.getOrDefault("field", "lifetimeValue");
        String operator = (String) criteria.getOrDefault("operator", ">");
        Object value = criteria.getOrDefault("value", 0);

        return ForecastCriteria.builder()
                .type(ForecastCriteria.CriteriaType.CUSTOM)
                .operator(ForecastCriteria.CriteriaOperator.GREATER_THAN)
                .field(field)
                .value(value)
                .build();
    }
}
