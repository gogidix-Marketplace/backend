package com.gogidix.shared.courier.pricing.application.service;

import com.gogidix.shared.courier.pricing.application.command.CalculatePriceCommand;
import com.gogidix.shared.courier.pricing.application.command.CreatePricingRuleCommand;
import com.gogidix.shared.courier.pricing.application.command.UpdatePricingRuleCommand;
import com.gogidix.shared.courier.pricing.application.dto.PriceCalculationDTO;
import com.gogidix.shared.courier.pricing.application.dto.PricingRequestDTO;
import com.gogidix.shared.courier.pricing.application.dto.PricingRuleDTO;
import com.gogidix.shared.courier.pricing.application.mapper.PricingDtoMapper;
import com.gogidix.shared.courier.pricing.application.query.PricingQuery;
import com.gogidix.shared.courier.pricing.domain.entity.PricingModel;
import com.gogidix.shared.courier.pricing.domain.entity.PricingRule;
import com.gogidix.shared.courier.pricing.domain.events.PriceCalculatedEvent;
import com.gogidix.shared.courier.pricing.domain.events.PricingRuleCreatedEvent;
import com.gogidix.shared.courier.pricing.domain.events.PricingRuleUpdatedEvent;
import com.gogidix.shared.courier.pricing.domain.repository.PricingRuleRepository;
import com.gogidix.shared.courier.pricing.domain.service.PricingEngineService;
import com.gogidix.shared.courier.pricing.infrastructure.messaging.PricingEventPublisher;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Application Service for Pricing Operations
 * Orchestrates pricing calculations and rule management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PricingApplicationService {

    private final PricingRuleRepository pricingRuleRepository;
    private final PricingEngineService pricingEngineService;
    private final PricingDtoMapper dtoMapper;
    private final PricingEventPublisher eventPublisher;

    /**
     * Create a new pricing rule
     */
    @Transactional
    public PricingRuleDTO createPricingRule(CreatePricingRuleCommand command) {
        log.info("Creating pricing rule for tenant: {}, rule: {}",
                command.getTenantId(), command.getRuleName());

        PricingRule rule = dtoMapper.toEntity(command);
        rule = pricingRuleRepository.save(rule);

        // Publish event
        PricingRuleCreatedEvent event = PricingRuleCreatedEvent.builder()
                .tenantId(rule.getTenantId())
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .ruleType(rule.getRuleType())
                .createdAt(rule.getCreatedAt())
                .build();
        eventPublisher.publishRuleCreated(event);

        return dtoMapper.toDTO(rule);
    }

    /**
     * Get pricing rule by ID
     */
    public PricingRuleDTO getPricingRule(String tenantId, String ruleId) {
        PricingRule rule = pricingRuleRepository.findByTenantIdAndRuleId(tenantId, ruleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pricing rule not found: " + ruleId));
        return dtoMapper.toDTO(rule);
    }

    /**
     * Query pricing rules with filters
     */
    public Page<PricingRuleDTO> queryPricingRules(PricingQuery query) {
        log.debug("Querying pricing rules for tenant: {}", query.getTenantId());

        Sort sort = Sort.by(
                query.getSortDirection() != null && query.getSortDirection().equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.getSortBy() != null ? query.getSortBy() : "priority");

        Pageable pageable = PageRequest.of(
                query.getPage() != null ? query.getPage() : 0,
                query.getSize() != null ? query.getSize() : 20,
                sort);

        return pricingRuleRepository.findByTenantId(query.getTenantId(), pageable)
                .map(dtoMapper::toDTO);
    }

    /**
     * Get active pricing rules for tenant and service type
     */
    public List<PricingRuleDTO> getActiveRules(String tenantId, String serviceType, String vehicleType) {
        return pricingRuleRepository
                .findByTenantIdAndActiveTrueAndServiceTypeAndVehicleType(
                        tenantId, serviceType, vehicleType)
                .stream()
                .map(dtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update pricing rule
     */
    @Transactional
    public PricingRuleDTO updatePricingRule(String tenantId, String ruleId,
                                            UpdatePricingRuleCommand command) {
        log.info("Updating pricing rule: {} for tenant: {}", ruleId, tenantId);

        PricingRule rule = pricingRuleRepository.findByTenantIdAndRuleId(tenantId, ruleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pricing rule not found: " + ruleId));

        if (command.getRuleName() != null) {
            rule.setRuleName(command.getRuleName());
        }
        if (command.getDescription() != null) {
            rule.setDescription(command.getDescription());
        }
        if (command.getRuleType() != null) {
            rule.setRuleType(command.getRuleType());
        }
        if (command.getPriority() != null) {
            rule.setPriority(command.getPriority());
        }
        if (command.getActive() != null) {
            rule.setActive(command.getActive());
        }
        if (command.getVehicleType() != null) {
            rule.setVehicleType(command.getVehicleType());
        }
        if (command.getServiceType() != null) {
            rule.setServiceType(command.getServiceType());
        }
        if (command.getParameters() != null) {
            rule.setParameters(command.getParameters());
        }

        rule.setUpdatedAt(java.time.LocalDateTime.now());
        rule = pricingRuleRepository.save(rule);

        // Publish event
        PricingRuleUpdatedEvent event = PricingRuleUpdatedEvent.builder()
                .tenantId(rule.getTenantId())
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .updatedAt(rule.getUpdatedAt())
                .build();
        eventPublisher.publishRuleUpdated(event);

        return dtoMapper.toDTO(rule);
    }

    /**
     * Delete pricing rule
     */
    @Transactional
    public void deletePricingRule(String tenantId, String ruleId) {
        log.info("Deleting pricing rule: {} for tenant: {}", ruleId, tenantId);

        PricingRule rule = pricingRuleRepository.findByTenantIdAndRuleId(tenantId, ruleId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pricing rule not found: " + ruleId));

        pricingRuleRepository.delete(rule);
    }

    /**
     * Calculate price for delivery
     */
    public PriceCalculationDTO calculatePrice(CalculatePriceCommand command) {
        log.info("Calculating price for tenant: {}, service: {}, vehicle: {}",
                command.getTenantId(), command.getServiceType(), command.getVehicleType());

        // Get applicable rules
        List<PricingRule> applicableRules = pricingRuleRepository
                .findByTenantIdAndActiveTrueAndServiceTypeAndVehicleType(
                        command.getTenantId(),
                        command.getServiceType(),
                        command.getVehicleType());

        if (applicableRules.isEmpty()) {
            throw new IllegalStateException("No active pricing rules found for: "
                    + command.getServiceType() + "/" + command.getVehicleType());
        }

        // Calculate using pricing engine
        PricingModel pricingModel = pricingEngineService.calculatePrice(
                command, applicableRules);

        PriceCalculationDTO result = dtoMapper.toPriceCalculationDTO(pricingModel);

        // Publish event
        PriceCalculatedEvent event = PriceCalculatedEvent.builder()
                .tenantId(command.getTenantId())
                .quoteId(result.getQuoteId())
                .serviceType(command.getServiceType())
                .vehicleType(command.getVehicleType())
                .totalAmount(result.getTotalAmount().toString())
                .currency(result.getCurrency())
                .calculatedAt(result.getCalculatedAt())
                .build();
        eventPublisher.publishPriceCalculated(event);

        return result;
    }

    /**
     * Get price quote by ID
     */
    public PriceCalculationDTO getPriceQuote(String tenantId, String quoteId) {
        // This would typically fetch from a quote repository
        // For now, return a placeholder
        throw new UnsupportedOperationException("Quote retrieval not yet implemented");
    }
}
