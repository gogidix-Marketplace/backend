package com.gogidix.aiservices.aimarketbasketanalysisservice.application.service;

import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AddCustomersToBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.AnalyzeBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.CreateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.DeleteBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.RemoveCustomersFromBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.command.UpdateBasketCommand;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.MarketBasketResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.dto.BasketAnalysisResponseDto;
import com.gogidix.aiservices.aimarketbasketanalysisservice.application.mapper.MarketBasketMapper;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.MarketBasket;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketCriteria;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.port.out.MarketBasketRepositoryPort;
import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.policy.BasketBusinessPolicy;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.NotFoundException;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.exception.ValidationException;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.context.RequestContext;
import com.gogidix.aiservices.aimarketbasketanalysisservice.shared.context.RequestContextHolder;
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
 * Application service for market basket operations.
 */
@Service
@Transactional(readOnly = true)
public class MarketBasketApplicationService {

    private static final Logger log = LoggerFactory.getLogger(MarketBasketApplicationService.class);
    private static final String CACHE_NAME = "segments";

    private final MarketBasketRepositoryPort repository;
    private final MarketBasketMapper mapper;
    private final BasketBusinessPolicy businessPolicy;
    private final BasketAnalysisService analysisService;

    public MarketBasketApplicationService(
            MarketBasketRepositoryPort repository,
            MarketBasketMapper mapper,
            BasketBusinessPolicy businessPolicy,
            BasketAnalysisService analysisService) {
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
    public MarketBasketResponseDto createBasket(CreateBasketCommand command) {
        log.info("Creating segment '{}' for tenant: {}", command.name(), command.tenantId());

        businessPolicy.validateBasketCreation(command.tenantId());

        // Convert Map<String, Object> criteria to BasketCriteria
        BasketCriteria criteria = mapToBasketCriteria(command.criteria());

        MarketBasket segment = new MarketBasket(
                command.tenantId(),
                command.name(),
                criteria
        );

        MarketBasket saved = repository.save(segment);
        log.info("Basket created with ID: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Update an existing segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public MarketBasketResponseDto updateBasket(UpdateBasketCommand command) {
        log.info("Updating segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        MarketBasket existing = findBasketByIdAndTenant(command.segmentId(), command.tenantId());

        if (command.name() != null) {
            existing.updateDetails(command.name(), command.description());
        }

        MarketBasket saved = repository.save(existing);
        log.info("Basket updated: {}", saved.getId());

        return mapper.toResponseDto(saved);
    }

    /**
     * Delete a segment.
     */
    @Transactional
    public void deleteBasket(String segmentId, String tenantId, String userId) {
        log.info("Deleting segment: {} for tenant: {}", segmentId, tenantId);

        MarketBasket segment = findBasketByIdAndTenant(segmentId, tenantId);
        businessPolicy.validateBasketDeletion(segment);

        repository.deleteById(segmentId);
        log.info("Basket deleted: {}", segmentId);
    }

    /**
     * Get a segment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#segmentId + ':' + #tenantId")
    public MarketBasketResponseDto getBasketById(String segmentId, String tenantId) {
        log.debug("Fetching segment: {} for tenant: {}", segmentId, tenantId);

        MarketBasket segment = findBasketByIdAndTenant(segmentId, tenantId);
        return mapper.toResponseDto(segment);
    }

    /**
     * List segments for a tenant with pagination.
     */
    public PagedResponseDto<MarketBasketResponseDto> getBasketsByTenant(
            String tenantId,
            BasketType segmentType,
            Boolean active,
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        log.debug("Fetching segments for tenant: {}", tenantId);

        // For now, return empty page since repository doesn't have this method
        List<MarketBasketResponseDto> content = new ArrayList<>();
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
    public MarketBasketResponseDto addCustomersToBasket(AddCustomersToBasketCommand command) {
        log.info("Adding {} customers to segment: {}", command.customerIds().size(), command.segmentId());

        MarketBasket segment = findBasketByIdAndTenant(command.segmentId(), command.tenantId());
        businessPolicy.validateCustomerAddition(segment, command.customerIds().size());

        segment.addCustomers(command.customerIds());
        MarketBasket saved = repository.save(segment);

        log.info("Added {} customers to segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Remove customers from a segment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.segmentId()")
    public MarketBasketResponseDto removeCustomersFromBasket(RemoveCustomersFromBasketCommand command) {
        log.info("Removing {} customers from segment: {}", command.customerIds().size(), command.segmentId());

        MarketBasket segment = findBasketByIdAndTenant(command.segmentId(), command.tenantId());

        segment.removeCustomers(command.customerIds());
        MarketBasket saved = repository.save(segment);

        log.info("Removed {} customers from segment: {}", command.customerIds().size(), saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Analyze a segment.
     */
    public BasketAnalysisResponseDto analyzeBasket(AnalyzeBasketCommand command) {
        log.info("Analyzing segment: {} for tenant: {}", command.segmentId(), command.tenantId());

        MarketBasket segment = findBasketByIdAndTenant(command.segmentId(), command.tenantId());

        if (!segment.isActive()) {
            throw new ValidationException("Cannot analyze inactive segment");
        }

        return analysisService.analyzeBasket(segment, command.analysisOptions());
    }

    /**
     * Helper method to find a segment by ID and tenant.
     */
    private MarketBasket findBasketByIdAndTenant(String segmentId, String tenantId) {
        return repository.findById(segmentId)
                .filter(seg -> seg.getTenantId().equals(tenantId))
                .orElseThrow(() -> new NotFoundException(
                        "Basket not found with ID: " + segmentId + " for tenant: " + tenantId));
    }

    /**
     * Convert Map<String, Object> to BasketCriteria.
     */
    private BasketCriteria mapToBasketCriteria(Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria cannot be null or empty");
        }

        String field = (String) criteria.getOrDefault("field", "lifetimeValue");
        String operator = (String) criteria.getOrDefault("operator", ">");
        Object value = criteria.getOrDefault("value", 0);

        return BasketCriteria.builder()
                .type(BasketCriteria.CriteriaType.CUSTOM)
                .operator(BasketCriteria.CriteriaOperator.GREATER_THAN)
                .field(field)
                .value(value)
                .build();
    }
}
