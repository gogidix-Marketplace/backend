package com.gogidix.shared.warehousing.pricing.application.service;

import com.gogidix.shared.warehousing.pricing.application.command.CalculatePriceCommand;
import com.gogidix.shared.warehousing.pricing.application.command.CreatePriceTierCommand;
import com.gogidix.shared.warehousing.pricing.application.command.CreatePricingRuleCommand;
import com.gogidix.shared.warehousing.pricing.application.command.UpdatePricingRuleCommand;
import com.gogidix.shared.warehousing.pricing.application.dto.*;
import com.gogidix.shared.warehousing.pricing.application.mapper.PricingMapper;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceQuote;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceTier;
import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import com.gogidix.shared.warehousing.pricing.domain.events.PriceCalculatedEvent;
import com.gogidix.shared.warehousing.pricing.domain.events.PricingRuleCreatedEvent;
import com.gogidix.shared.warehousing.pricing.domain.events.PricingRuleUpdatedEvent;
import com.gogidix.shared.warehousing.pricing.domain.exception.InvalidPricingRequestException;
import com.gogidix.shared.warehousing.pricing.domain.exception.PricingNotFoundException;
import com.gogidix.shared.warehousing.pricing.domain.repository.PriceQuoteRepository;
import com.gogidix.shared.warehousing.pricing.domain.repository.PriceTierRepository;
import com.gogidix.shared.warehousing.pricing.domain.repository.PricingRuleRepository;
import com.gogidix.shared.warehousing.pricing.infrastructure.messaging.PricingEventPublisher;
import com.gogidix.shared.warehousing.pricing.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Pricing Application Service
 *
 * Handles pricing calculations and pricing rule management
 * Multi-tenant with MongoDB support
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PricingService {

    private final PricingRuleRepository pricingRuleRepository;
    private final PriceQuoteRepository priceQuoteRepository;
    private final PriceTierRepository priceTierRepository;
    private final PricingMapper pricingMapper;
    private final PricingEventPublisher eventPublisher;

    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("0.10");

    /**
     * Create a new pricing rule
     */
    public PricingRuleDTO createPricingRule(CreatePricingRuleCommand command) {
        log.info("Creating pricing rule: {} for tenant", command.getName());

        String tenantId = TenantContext.getCurrentTenantId();

        PricingRule pricingRule = pricingMapper.toEntity(command);
        pricingRule.setTenantId(tenantId);

        PricingRule savedRule = pricingRuleRepository.save(pricingRule);

        // Publish domain event
        PricingRuleCreatedEvent event = PricingRuleCreatedEvent.builder()
            .pricingRuleId(savedRule.getId())
            .tenantId(savedRule.getTenantId())
            .name(savedRule.getName())
            .serviceType(savedRule.getServiceType().name())
            .storageType(savedRule.getStorageType().name())
            .basePrice(savedRule.getBasePrice())
            .priceUnit(savedRule.getPriceUnit().name())
            .currency(savedRule.getCurrency())
            .createdAt(LocalDateTime.now())
            .warehouseId(savedRule.getWarehouseId())
            .build();
        eventPublisher.publishPricingRuleCreated(event);

        log.info("Pricing rule created with ID: {}", savedRule.getId());
        return pricingMapper.toDTO(savedRule);
    }

    /**
     * Get pricing rule by ID
     */
    @Transactional(readOnly = true)
    public PricingRuleDTO getPricingRule(String id) {
        String tenantId = TenantContext.getCurrentTenantId();
        PricingRule pricingRule = pricingRuleRepository.findById(id)
            .filter(rule -> rule.getTenantId().equals(tenantId))
            .orElseThrow(() -> new PricingNotFoundException(id, tenantId));
        return pricingMapper.toDTO(pricingRule);
    }

    /**
     * Get all pricing rules for current tenant
     */
    @Transactional(readOnly = true)
    public List<PricingRuleDTO> getAllPricingRules() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PricingRule> rules = pricingRuleRepository.findByTenantIdAndActiveTrue(tenantId);
        return pricingMapper.toPricingRuleDTOList(rules);
    }

    /**
     * Get pricing rules by service type
     */
    @Transactional(readOnly = true)
    public List<PricingRuleDTO> getPricingRulesByServiceType(PricingRule.ServiceType serviceType) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PricingRule> rules = pricingRuleRepository.findByTenantIdAndServiceTypeAndActiveTrue(tenantId, serviceType);
        return pricingMapper.toPricingRuleDTOList(rules);
    }

    /**
     * Get pricing rules by warehouse
     */
    @Transactional(readOnly = true)
    public List<PricingRuleDTO> getPricingRulesByWarehouse(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PricingRule> rules = pricingRuleRepository.findByTenantIdAndWarehouseIdAndActiveTrue(tenantId, warehouseId);
        return pricingMapper.toPricingRuleDTOList(rules);
    }

    /**
     * Update pricing rule
     */
    public PricingRuleDTO updatePricingRule(String id, UpdatePricingRuleCommand command) {
        log.info("Updating pricing rule: {}", id);

        String tenantId = TenantContext.getCurrentTenantId();
        PricingRule pricingRule = pricingRuleRepository.findById(id)
            .filter(rule -> rule.getTenantId().equals(tenantId))
            .orElseThrow(() -> new PricingNotFoundException(id, tenantId));

        pricingMapper.updateEntity(pricingRule, command);
        PricingRule updatedRule = pricingRuleRepository.save(pricingRule);

        // Publish domain event
        PricingRuleUpdatedEvent event = PricingRuleUpdatedEvent.builder()
            .pricingRuleId(updatedRule.getId())
            .tenantId(updatedRule.getTenantId())
            .name(updatedRule.getName())
            .serviceType(updatedRule.getServiceType().name())
            .newBasePrice(updatedRule.getBasePrice())
            .currency(updatedRule.getCurrency())
            .updatedAt(LocalDateTime.now())
            .warehouseId(updatedRule.getWarehouseId())
            .build();
        eventPublisher.publishPricingRuleUpdated(event);

        log.info("Pricing rule updated: {}", id);
        return pricingMapper.toDTO(updatedRule);
    }

    /**
     * Delete pricing rule
     */
    public void deletePricingRule(String id) {
        log.info("Deleting pricing rule: {}", id);

        String tenantId = TenantContext.getCurrentTenantId();
        PricingRule pricingRule = pricingRuleRepository.findById(id)
            .filter(rule -> rule.getTenantId().equals(tenantId))
            .orElseThrow(() -> new PricingNotFoundException(id, tenantId));

        pricingRuleRepository.deleteById(id);
        log.info("Pricing rule deleted: {}", id);
    }

    /**
     * Calculate price for storage service
     */
    public PriceCalculationResultDTO calculatePrice(CalculatePriceCommand command) {
        log.info("Calculating price for service type: {}, storage type: {}, quantity: {}",
            command.getServiceType(), command.getStorageType(), command.getQuantity());

        String tenantId = TenantContext.getCurrentTenantId();

        // Find applicable pricing rule
        PricingRule pricingRule = findApplicablePricingRule(tenantId, command);

        // Calculate base price
        BigDecimal basePrice = calculateBasePrice(pricingRule, command);

        // Apply tier discounts if applicable
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal discountPercentage = BigDecimal.ZERO;
        List<PriceTier> applicableTiers = findApplicableTiers(tenantId, pricingRule.getId(), command.getQuantity());

        for (PriceTier tier : applicableTiers) {
            if (tier.getDiscountPercentage() != null) {
                BigDecimal tierDiscount = basePrice.multiply(tier.getDiscountPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                discountAmount = discountAmount.add(tierDiscount);
                discountPercentage = discountPercentage.add(tier.getDiscountPercentage());
            }
        }

        BigDecimal discountedPrice = basePrice.subtract(discountAmount);

        // Calculate tax
        BigDecimal taxAmount = discountedPrice.multiply(DEFAULT_TAX_RATE).setScale(2, RoundingMode.HALF_UP);

        // Calculate total
        BigDecimal totalAmount = discountedPrice.add(taxAmount);

        // Create price quote
        PriceQuote quote = createPriceQuote(tenantId, command, pricingRule, basePrice, discountAmount, taxAmount, totalAmount);
        quote = priceQuoteRepository.save(quote);

        // Build breakdown
        Map<String, Object> breakdown = new HashMap<>();
        breakdown.put("basePrice", basePrice);
        breakdown.put("unitPrice", pricingRule.getBasePrice());
        breakdown.put("quantity", command.getQuantity());
        breakdown.put("discountAmount", discountAmount);
        breakdown.put("discountPercentage", discountPercentage);
        breakdown.put("taxAmount", taxAmount);
        breakdown.put("taxRate", DEFAULT_TAX_RATE);
        breakdown.put("appliedTiers", applicableTiers.stream().map(PriceTier::getName).collect(Collectors.toList()));

        // Publish event
        PriceCalculatedEvent event = PriceCalculatedEvent.builder()
            .quoteId(quote.getId())
            .quoteNumber(quote.getQuoteNumber())
            .tenantId(tenantId)
            .requestId(command.getRequestId())
            .serviceType(command.getServiceType().name())
            .storageType(command.getStorageType().name())
            .quantity(command.getQuantity())
            .totalAmount(totalAmount)
            .currency(quote.getCurrency())
            .calculatedAt(LocalDateTime.now())
            .validUntil(quote.getValidUntil())
            .build();
        eventPublisher.publishPriceCalculated(event);

        log.info("Price calculated: quoteNumber={}, totalAmount={}", quote.getQuoteNumber(), totalAmount);

        return PriceCalculationResultDTO.builder()
            .quoteId(quote.getId())
            .quoteNumber(quote.getQuoteNumber())
            .serviceType(command.getServiceType())
            .storageType(command.getStorageType())
            .quantity(command.getQuantity())
            .unitPrice(pricingRule.getBasePrice())
            .priceUnit(pricingRule.getPriceUnit())
            .baseAmount(basePrice)
            .discountAmount(discountAmount)
            .discountPercentage(discountPercentage)
            .taxAmount(taxAmount)
            .taxRate(DEFAULT_TAX_RATE)
            .totalAmount(totalAmount)
            .currency(quote.getCurrency())
            .validUntil(quote.getValidUntil())
            .breakdown(breakdown)
            .appliedRuleDescription(pricingRule.getName())
            .build();
    }

    /**
     * Get price quote by quote number
     */
    @Transactional(readOnly = true)
    public PriceQuoteDTO getQuoteByNumber(String quoteNumber) {
        String tenantId = TenantContext.getCurrentTenantId();
        PriceQuote quote = priceQuoteRepository.findByTenantIdAndQuoteNumber(tenantId, quoteNumber)
            .orElseThrow(() -> new PricingNotFoundException("Quote not found: " + quoteNumber, tenantId));
        return pricingMapper.toPriceQuoteDTO(quote);
    }

    /**
     * Get all quotes for tenant
     */
    @Transactional(readOnly = true)
    public List<PriceQuoteDTO> getAllQuotes() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PriceQuote> quotes = priceQuoteRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);
        return pricingMapper.toPriceQuoteDTOList(quotes);
    }

    /**
     * Create price tier
     */
    public PriceTierDTO createPriceTier(CreatePriceTierCommand command) {
        log.info("Creating price tier for pricing rule: {}", command.getPricingRuleId());

        String tenantId = TenantContext.getCurrentTenantId();

        // Validate pricing rule exists
        PricingRule pricingRule = pricingRuleRepository.findById(command.getPricingRuleId())
            .filter(rule -> rule.getTenantId().equals(tenantId))
            .orElseThrow(() -> new PricingNotFoundException("Pricing rule not found", tenantId));

        PriceTier priceTier = pricingMapper.toEntity(command);
        priceTier.setTenantId(tenantId);

        PriceTier savedTier = priceTierRepository.save(priceTier);
        log.info("Price tier created with ID: {}", savedTier.getId());

        return pricingMapper.toPriceTierDTO(savedTier);
    }

    /**
     * Get price tiers for a pricing rule
     */
    @Transactional(readOnly = true)
    public List<PriceTierDTO> getPriceTiersForRule(String pricingRuleId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<PriceTier> tiers = priceTierRepository.findByTenantIdAndPricingRuleIdAndActiveTrueOrderByPriorityAsc(
            tenantId, pricingRuleId);
        return pricingMapper.toPriceTierDTOList(tiers);
    }

    /**
     * Find applicable pricing rule for calculation
     */
    private PricingRule findApplicablePricingRule(String tenantId, CalculatePriceCommand command) {
        // First try to find warehouse-specific rule
        if (command.getWarehouseId() != null) {
            List<PricingRule> warehouseRules = pricingRuleRepository
                .findByTenantIdAndServiceTypeAndStorageTypeAndActiveTrue(
                    tenantId, command.getServiceType(), command.getStorageType());

            Optional<PricingRule> warehouseSpecificRule = warehouseRules.stream()
                .filter(rule -> command.getWarehouseId().equals(rule.getWarehouseId()))
                .max(Comparator.comparingInt(PricingRule::getPriority));

            if (warehouseSpecificRule.isPresent()) {
                return warehouseSpecificRule.get();
            }
        }

        // Fall back to default rules
        return pricingRuleRepository
            .findByTenantIdAndServiceTypeAndStorageTypeAndWarehouseIdIsNullAndActiveTrue(
                tenantId, command.getServiceType(), command.getStorageType())
            .orElseThrow(() -> new InvalidPricingRequestException(
                "No applicable pricing rule found for service type: " + command.getServiceType() +
                ", storage type: " + command.getStorageType()));
    }

    /**
     * Calculate base price based on rule and command
     */
    private BigDecimal calculateBasePrice(PricingRule pricingRule, CalculatePriceCommand command) {
        BigDecimal unitPrice = pricingRule.getBasePrice();
        BigDecimal quantity = BigDecimal.valueOf(command.getQuantity());

        // Apply volume or weight overrides if present
        if (command.getVolume() != null && pricingRule.getVolumePriceOverride() != null) {
            if (command.getVolume() >= pricingRule.getVolumeThreshold()) {
                unitPrice = pricingRule.getVolumePriceOverride();
            }
        } else if (command.getWeight() != null && pricingRule.getWeightPriceOverride() != null) {
            if (command.getWeight() >= pricingRule.getWeightThreshold()) {
                unitPrice = pricingRule.getWeightPriceOverride();
            }
        }

        BigDecimal basePrice = unitPrice.multiply(quantity);

        // Apply minimum charge
        if (pricingRule.getMinimumCharge() != null && basePrice.compareTo(pricingRule.getMinimumCharge()) < 0) {
            basePrice = pricingRule.getMinimumCharge();
        }

        return basePrice.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Find applicable pricing tiers for quantity
     */
    private List<PriceTier> findApplicableTiers(String tenantId, String pricingRuleId, Integer quantity) {
        return priceTierRepository.findApplicableTiersForQuantity(tenantId, pricingRuleId, quantity)
            .stream()
            .filter(tier -> tier.getActive())
            .sorted(Comparator.comparingInt(PriceTier::getPriority))
            .collect(Collectors.toList());
    }

    /**
     * Create price quote entity
     */
    private PriceQuote createPriceQuote(String tenantId, CalculatePriceCommand command,
                                       PricingRule pricingRule, BigDecimal baseAmount,
                                       BigDecimal discountAmount, BigDecimal taxAmount,
                                       BigDecimal totalAmount) {
        LocalDateTime validUntil = LocalDateTime.now().plusDays(command.getValidityDays() != null ? command.getValidityDays() : 30);

        return PriceQuote.builder()
            .tenantId(tenantId)
            .quoteNumber(generateQuoteNumber())
            .requestId(command.getRequestId())
            .serviceType(command.getServiceType())
            .storageType(command.getStorageType())
            .quantity(command.getQuantity())
            .volume(command.getVolume())
            .weight(command.getWeight())
            .unitPrice(pricingRule.getBasePrice())
            .priceUnit(pricingRule.getPriceUnit())
            .baseAmount(baseAmount)
            .discountAmount(discountAmount)
            .taxAmount(taxAmount)
            .totalAmount(totalAmount)
            .currency(command.getCurrency() != null ? command.getCurrency() : pricingRule.getCurrency())
            .validityDays(command.getValidityDays() != null ? command.getValidityDays() : 30)
            .status(PriceQuote.QuoteStatus.CALCULATED)
            .appliedRuleIds(pricingRule.getId())
            .validUntil(validUntil)
            .expiresAt(validUntil)
            .build();
    }

    /**
     * Generate unique quote number
     */
    private String generateQuoteNumber() {
        return "QT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
