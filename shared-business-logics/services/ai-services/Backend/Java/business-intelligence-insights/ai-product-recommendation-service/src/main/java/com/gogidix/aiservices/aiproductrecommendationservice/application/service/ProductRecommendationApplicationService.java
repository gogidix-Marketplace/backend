package com.gogidix.aiservices.aiproductrecommendationservice.application.service;

import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AddProductsToRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.AnalyzeRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.CreateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.DeleteRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.RemoveProductsFromRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.command.UpdateRecommendationCommand;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.ProductRecommendationResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.dto.RecommendationAnalysisResponseDto;
import com.gogidix.aiservices.aiproductrecommendationservice.application.mapper.ProductRecommendationMapper;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRecommendation;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationCriteria;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.RecommendationType;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.port.out.ProductRecommendationRepositoryPort;
import com.gogidix.aiservices.aiproductrecommendationservice.domain.policy.RecommendationBusinessPolicy;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.exception.ValidationException;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.context.RequestContext;
import com.gogidix.aiservices.aiproductrecommendationservice.shared.context.RequestContextHolder;
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
 * Application service for product recommendation operations.
 */
@Service
@Transactional(readOnly = true)
public class ProductRecommendationApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ProductRecommendationApplicationService.class);
    private static final String CACHE_NAME = "segments";

    private final ProductRecommendationRepositoryPort repository;
    private final ProductRecommendationMapper mapper;
    private final RecommendationBusinessPolicy businessPolicy;
    private final RecommendationAnalysisService analysisService;

    public ProductRecommendationApplicationService(
            ProductRecommendationRepositoryPort repository,
            ProductRecommendationMapper mapper,
            RecommendationBusinessPolicy businessPolicy,
            RecommendationAnalysisService analysisService) {
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
    public ProductRecommendationResponseDto createRecommendation(CreateRecommendationCommand command) {
        log.info("Creating segment '{}' for tenant: {}", command.name(), command.tenantId());

        businessPolicy.validateRecommendationCreation(command.tenantId());

        // Convert Map<String, Object> criteria to RecommendationCriteria
        RecommendationCriteria criteria = mapToRecommendationCriteria(command.criteria());

        ProductRecommendation segment = new ProductRecommendation(
                command.tenantId(),
                command.name(),
                criteria
        );

        ProductRecommendation saved = repository.save(segment);
        log.info("Recommendation created with ID: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Update an existing segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public ProductRecommendationResponseDto updateRecommendation(UpdateRecommendationCommand command) {
        log.info("Updating segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        ProductRecommendation existing = findRecommendationByIdAndTenant(command.segmentId(), command.tenantId());

        if (command.name() != null) {
            existing.updateDetails(command.name(), command.description());
        }

        ProductRecommendation saved = repository.save(existing);
        log.info("Recommendation updated: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a segment.
     */
    @Transactional
    public void deleteRecommendation(String segmentId, String tenantId, String userId) {
        log.info("Deleting segment: {} for tenant: {}", segmentId, tenantId);

        ProductRecommendation segment = findRecommendationByIdAndTenant(segmentId, tenantId);
        businessPolicy.validateRecommendationDeletion(segment);

        repository.deleteById(segmentId);
        log.info("Recommendation deleted: {}", segmentId);
    }

    /**
     * Get a segment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#segmentId + ':' + #tenantId")
    public ProductRecommendationResponseDto getRecommendationById(String segmentId, String tenantId) {
        log.debug("Fetching segment: {} for tenant: {}", segmentId, tenantId);

        ProductRecommendation segment = findRecommendationByIdAndTenant(segmentId, tenantId);
        return mapper.toResponseDto(segment);
    }

    /**
     * List segments for a tenant with pagination.
     */
    public PagedResponseDto<ProductRecommendationResponseDto> getRecommendationsByTenant(
            String tenantId,
            RecommendationType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching segments for tenant: {}", tenantId);

        // For now, return empty page since repository doesn't have this method
        List<ProductRecommendationResponseDto> content = new ArrayList<>();
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
    public ProductRecommendationResponseDto addProductsToRecommendation(AddProductsToRecommendationCommand command) {
        log.info("Adding {} customers to segment: {}", command.customerIds().size(), command.segmentId());

        ProductRecommendation segment = findRecommendationByIdAndTenant(command.segmentId(), command.tenantId());
        businessPolicy.validateProductAddition(segment, command.customerIds().size());

        segment.addProducts(command.customerIds());
        ProductRecommendation saved = repository.save(segment);

        log.info("Added {} customers to segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Remove customers from a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public ProductRecommendationResponseDto removeProductsFromRecommendation(RemoveProductsFromRecommendationCommand command) {
        log.info("Removing {} customers from segment: {}", command.customerIds().size(), command.segmentId());

        ProductRecommendation segment = findRecommendationByIdAndTenant(command.segmentId(), command.tenantId());

        segment.removeProducts(command.customerIds());
        ProductRecommendation saved = repository.save(segment);

        log.info("Removed {} customers from segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Analyze a segment.
     */
    public RecommendationAnalysisResponseDto analyzeRecommendation(AnalyzeRecommendationCommand command) {
        log.info("Analyzing segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        ProductRecommendation segment = findRecommendationByIdAndTenant(command.segmentId(), command.tenantId());

        if (!segment.isActive()) {
            throw new ValidationException("Cannot analyze inactive segment");
        }

        return analysisService.analyzeRecommendation(segment, command.analysisOptions());
    }

    /**
     * Helper method to find a segment by ID and tenant.
     */
    private ProductRecommendation findRecommendationByIdAndTenant(String segmentId, String tenantId) {
        return repository.findById(segmentId)
                .filter(seg -> seg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        "Recommendation not found with ID: " + segmentId + " for tenant: " + tenantId));
    }

    /**
     * Convert Map<String, Object> to RecommendationCriteria.
     */
    private RecommendationCriteria mapToRecommendationCriteria(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }

        String field = (String) criteria.getOrDefault("field", "lifetimeValue");
        String operator = (String) criteria.getOrDefault("operator", ">");
        Object value = criteria.getOrDefault("value", 0);

        return RecommendationCriteria.builder()
                .type(RecommendationCriteria.CriteriaType.CUSTOM)
                .operator(RecommendationCriteria.CriteriaOperator.GREATER_THAN)
                .field(field)
                .value(value)
                .build();
    }
}
