package com.gogidix.aiservices.aicustomersegmentationservice.application.service;

import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AddCustomersToSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.AnalyzeSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.CreateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.DeleteSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.RemoveCustomersFromSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.command.UpdateSegmentCommand;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.CustomerSegmentResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.dto.SegmentAnalysisResponseDto;
import com.gogidix.aiservices.aicustomersegmentationservice.application.mapper.CustomerSegmentMapper;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.CustomerSegment;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentCriteria;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.model.SegmentType;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.port.out.CustomerSegmentRepositoryPort;
import com.gogidix.aiservices.aicustomersegmentationservice.domain.policy.SegmentBusinessPolicy;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.exception.ValidationException;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.context.RequestContext;
import com.gogidix.aiservices.aicustomersegmentationservice.shared.context.RequestContextHolder;
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
public class CustomerSegmentApplicationService {

    private static final Logger log = LoggerFactory.getLogger(CustomerSegmentApplicationService.class);
    private static final String CACHE_NAME = "segments";

    private final CustomerSegmentRepositoryPort repository;
    private final CustomerSegmentMapper mapper;
    private final SegmentBusinessPolicy businessPolicy;
    private final SegmentAnalysisService analysisService;

    public CustomerSegmentApplicationService(
            CustomerSegmentRepositoryPort repository,
            CustomerSegmentMapper mapper,
            SegmentBusinessPolicy businessPolicy,
            SegmentAnalysisService analysisService) {
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
    public CustomerSegmentResponseDto createSegment(CreateSegmentCommand command) {
        log.info("Creating segment '{}' for tenant: {}", command.name(), command.tenantId());

        businessPolicy.validateSegmentCreation(command.tenantId());

        // Convert Map<String, Object> criteria to SegmentCriteria
        SegmentCriteria criteria = mapToSegmentCriteria(command.criteria());

        CustomerSegment segment = new CustomerSegment(
                command.tenantId(),
                command.name(),
                criteria
        );

        CustomerSegment saved = repository.save(segment);
        log.info("Segment created with ID: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Update an existing segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public CustomerSegmentResponseDto updateSegment(UpdateSegmentCommand command) {
        log.info("Updating segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        CustomerSegment existing = findSegmentByIdAndTenant(command.segmentId(), command.tenantId());

        if (command.name() != null) {
            existing.updateDetails(command.name(), command.description());
        }

        CustomerSegment saved = repository.save(existing);
        log.info("Segment updated: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a segment.
     */
    @Transactional
    public void deleteSegment(String segmentId, String tenantId, String userId) {
        log.info("Deleting segment: {} for tenant: {}", segmentId, tenantId);

        CustomerSegment segment = findSegmentByIdAndTenant(segmentId, tenantId);
        businessPolicy.validateSegmentDeletion(segment);

        repository.deleteById(segmentId);
        log.info("Segment deleted: {}", segmentId);
    }

    /**
     * Get a segment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#segmentId + ':' + #tenantId")
    public CustomerSegmentResponseDto getSegmentById(String segmentId, String tenantId) {
        log.debug("Fetching segment: {} for tenant: {}", segmentId, tenantId);

        CustomerSegment segment = findSegmentByIdAndTenant(segmentId, tenantId);
        return mapper.toResponseDto(segment);
    }

    /**
     * List segments for a tenant with pagination.
     */
    public PagedResponseDto<CustomerSegmentResponseDto> getSegmentsByTenant(
            String tenantId,
            SegmentType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching segments for tenant: {}", tenantId);

        // For now, return empty page since repository doesn't have this method
        List<CustomerSegmentResponseDto> content = new ArrayList<>();
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
    public CustomerSegmentResponseDto addCustomersToSegment(AddCustomersToSegmentCommand command) {
        log.info("Adding {} customers to segment: {}", command.customerIds().size(), command.segmentId());

        CustomerSegment segment = findSegmentByIdAndTenant(command.segmentId(), command.tenantId());
        businessPolicy.validateCustomerAddition(segment, command.customerIds().size());

        segment.addCustomers(command.customerIds());
        CustomerSegment saved = repository.save(segment);

        log.info("Added {} customers to segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Remove customers from a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public CustomerSegmentResponseDto removeCustomersFromSegment(RemoveCustomersFromSegmentCommand command) {
        log.info("Removing {} customers from segment: {}", command.customerIds().size(), command.segmentId());

        CustomerSegment segment = findSegmentByIdAndTenant(command.segmentId(), command.tenantId());

        segment.removeCustomers(command.customerIds());
        CustomerSegment saved = repository.save(segment);

        log.info("Removed {} customers from segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Analyze a segment.
     */
    public SegmentAnalysisResponseDto analyzeSegment(AnalyzeSegmentCommand command) {
        log.info("Analyzing segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        CustomerSegment segment = findSegmentByIdAndTenant(command.segmentId(), command.tenantId());

        if (!segment.isActive()) {
            throw new ValidationException("Cannot analyze inactive segment");
        }

        return analysisService.analyzeSegment(segment, command.analysisOptions());
    }

    /**
     * Helper method to find a segment by ID and tenant.
     */
    private CustomerSegment findSegmentByIdAndTenant(String segmentId, String tenantId) {
        return repository.findById(segmentId)
                .filter(seg -> seg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        "Segment not found with ID: " + segmentId + " for tenant: " + tenantId));
    }

    /**
     * Convert Map<String, Object> to SegmentCriteria.
     */
    private SegmentCriteria mapToSegmentCriteria(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }

        String field = (String) criteria.getOrDefault("field", "lifetimeValue");
        String operator = (String) criteria.getOrDefault("operator", ">");
        Object value = criteria.getOrDefault("value", 0);

        return SegmentCriteria.builder()
                .type(SegmentCriteria.CriteriaType.CUSTOM)
                .operator(SegmentCriteria.CriteriaOperator.GREATER_THAN)
                .field(field)
                .value(value)
                .build();
    }
}
