package com.gogidix.aiservices.intelligenceanalysisservice.application.service;

import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AddIntelligenceReportsToAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.AnalyzeAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.CreateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.DeleteAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.RemoveIntelligenceReportsFromAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.command.UpdateAnalysisCommand;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.IntelligenceAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.dto.AnalysisAnalysisResponseDto;
import com.gogidix.aiservices.intelligenceanalysisservice.application.mapper.IntelligenceAnalysisMapper;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.IntelligenceAnalysis;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisCriteria;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.model.AnalysisType;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.port.out.IntelligenceAnalysisRepositoryPort;
import com.gogidix.aiservices.intelligenceanalysisservice.domain.policy.AnalysisBusinessPolicy;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.ValidationException;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.context.RequestContext;
import com.gogidix.aiservices.intelligenceanalysisservice.shared.context.RequestContextHolder;
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
 * Application service for customer segment operations.
 */
@Service
@Transactional(readOnly = true)
public class IntelligenceAnalysisApplicationService {

    private static final Logger log = LoggerFactory.getLogger(IntelligenceAnalysisApplicationService.class);
    private static final String CACHE_NAME = "segments";

    private final IntelligenceAnalysisRepositoryPort repository;
    private final IntelligenceAnalysisMapper mapper;
    private final AnalysisBusinessPolicy businessPolicy;
    private final AnalysisAnalysisService analysisService;

    public IntelligenceAnalysisApplicationService(
            IntelligenceAnalysisRepositoryPort repository,
            IntelligenceAnalysisMapper mapper,
            AnalysisBusinessPolicy businessPolicy,
            AnalysisAnalysisService analysisService) {
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
    public IntelligenceAnalysisResponseDto createAnalysis(CreateAnalysisCommand command) {
        log.info("Creating segment '{}' for tenant: {}", command.name(), command.tenantId());

        businessPolicy.validateAnalysisCreation(command.tenantId());

        // Convert Map<String, Object> criteria to AnalysisCriteria
        AnalysisCriteria criteria = mapToAnalysisCriteria(command.criteria());

        IntelligenceAnalysis segment = new IntelligenceAnalysis(
                command.tenantId(),
                command.name(),
                criteria
        );

        IntelligenceAnalysis saved = repository.save(segment);
        log.info("Analysis created with ID: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Update an existing segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public IntelligenceAnalysisResponseDto updateAnalysis(UpdateAnalysisCommand command) {
        log.info("Updating segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        IntelligenceAnalysis existing = findAnalysisByIdAndTenant(command.segmentId(), command.tenantId());

        if (command.name() != null) {
            existing.updateDetails(command.name(), command.description());
        }

        IntelligenceAnalysis saved = repository.save(existing);
        log.info("Analysis updated: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a segment.
     */
    @Transactional
    public void deleteAnalysis(String segmentId, String tenantId, String userId) {
        log.info("Deleting segment: {} for tenant: {}", segmentId, tenantId);

        IntelligenceAnalysis segment = findAnalysisByIdAndTenant(segmentId, tenantId);
        businessPolicy.validateAnalysisDeletion(segment);

        repository.deleteById(segmentId);
        log.info("Analysis deleted: {}", segmentId);
    }

    /**
     * Get a segment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#segmentId + ':' + #tenantId")
    public IntelligenceAnalysisResponseDto getAnalysisById(String segmentId, String tenantId) {
        log.debug("Fetching segment: {} for tenant: {}", segmentId, tenantId);

        IntelligenceAnalysis segment = findAnalysisByIdAndTenant(segmentId, tenantId);
        return mapper.toResponseDto(segment);
    }

    /**
     * List segments for a tenant with pagination.
     */
    public PagedResponseDto<IntelligenceAnalysisResponseDto> getAnalysissByTenant(
            String tenantId,
            AnalysisType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching segments for tenant: {}", tenantId);

        // For now, return empty page since repository doesn't have this method
        List<IntelligenceAnalysisResponseDto> content = new ArrayList<>();
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
    public IntelligenceAnalysisResponseDto addIntelligenceReportsToAnalysis(AddIntelligenceReportsToAnalysisCommand command) {
        log.info("Adding {} customers to segment: {}", command.customerIds().size(), command.segmentId());

        IntelligenceAnalysis segment = findAnalysisByIdAndTenant(command.segmentId(), command.tenantId());
        businessPolicy.validateIntelligenceReportAddition(segment, command.customerIds().size());

        segment.addIntelligenceReports(command.customerIds());
        IntelligenceAnalysis saved = repository.save(segment);

        log.info("Added {} customers to segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Remove customers from a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public IntelligenceAnalysisResponseDto removeIntelligenceReportsFromAnalysis(RemoveIntelligenceReportsFromAnalysisCommand command) {
        log.info("Removing {} customers from segment: {}", command.customerIds().size(), command.segmentId());

        IntelligenceAnalysis segment = findAnalysisByIdAndTenant(command.segmentId(), command.tenantId());

        segment.removeIntelligenceReports(command.customerIds());
        IntelligenceAnalysis saved = repository.save(segment);

        log.info("Removed {} customers from segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Analyze a segment.
     */
    public AnalysisAnalysisResponseDto analyzeAnalysis(AnalyzeAnalysisCommand command) {
        log.info("Analyzing segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        IntelligenceAnalysis segment = findAnalysisByIdAndTenant(command.segmentId(), command.tenantId());

        if (!segment.isActive()) {
            throw new ValidationException("Cannot analyze inactive segment");
        }

        return analysisService.analyzeAnalysis(segment, command.analysisOptions());
    }

    /**
     * Helper method to find a segment by ID and tenant.
     */
    private IntelligenceAnalysis findAnalysisByIdAndTenant(String segmentId, String tenantId) {
        return repository.findById(segmentId)
                .filter(seg -> seg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        "Analysis not found with ID: " + segmentId + " for tenant: " + tenantId));
    }

    /**
     * Convert Map<String, Object> to AnalysisCriteria.
     */
    private AnalysisCriteria mapToAnalysisCriteria(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }

        String field = (String) criteria.getOrDefault("field", "lifetimeValue");
        String operator = (String) criteria.getOrDefault("operator", ">");
        Object value = criteria.getOrDefault("value", 0);

        return AnalysisCriteria.builder()
                .type(AnalysisCriteria.CriteriaType.CUSTOM)
                .operator(AnalysisCriteria.CriteriaOperator.GREATER_THAN)
                .field(field)
                .value(value)
                .build();
    }
}
