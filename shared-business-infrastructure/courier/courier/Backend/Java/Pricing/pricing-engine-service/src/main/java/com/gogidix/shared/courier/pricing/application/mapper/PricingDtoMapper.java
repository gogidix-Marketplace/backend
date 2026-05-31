package com.gogidix.shared.courier.pricing.application.mapper;

import com.gogidix.shared.courier.pricing.application.command.CreatePricingRuleCommand;
import com.gogidix.shared.courier.pricing.application.dto.PriceCalculationDTO;
import com.gogidix.shared.courier.pricing.application.dto.PricingRequestDTO;
import com.gogidix.shared.courier.pricing.application.dto.PricingRuleDTO;
import com.gogidix.shared.courier.pricing.domain.entity.PricingModel;
import com.gogidix.shared.courier.pricing.domain.entity.PricingRule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Mapper for converting between pricing entities and DTOs
 */
@Component
public class PricingDtoMapper {

    /**
     * Convert PricingRule entity to PricingRuleDTO
     */
    public PricingRuleDTO toDTO(PricingRule rule) {
        return PricingRuleDTO.builder()
                .id(rule.getId())
                .tenantId(rule.getTenantId())
                .ruleId(rule.getRuleId())
                .ruleName(rule.getRuleName())
                .description(rule.getDescription())
                .ruleType(rule.getRuleType())
                .priority(rule.getPriority())
                .active(rule.getActive())
                .vehicleType(rule.getVehicleType())
                .serviceType(rule.getServiceType())
                .parameters(rule.getParameters())
                .createdAt(rule.getCreatedAt())
                .updatedAt(rule.getUpdatedAt())
                .createdBy(rule.getCreatedBy())
                .updatedBy(rule.getUpdatedBy())
                .build();
    }

    /**
     * Convert PricingRuleDTO to PricingRule entity
     */
    public PricingRule toEntity(PricingRuleDTO dto) {
        PricingRule rule = new PricingRule();
        rule.setId(dto.getId());
        rule.setTenantId(dto.getTenantId());
        rule.setRuleId(dto.getRuleId());
        rule.setRuleName(dto.getRuleName());
        rule.setDescription(dto.getDescription());
        rule.setRuleType(dto.getRuleType());
        rule.setPriority(dto.getPriority());
        rule.setActive(dto.getActive());
        rule.setVehicleType(dto.getVehicleType());
        rule.setServiceType(dto.getServiceType());
        rule.setParameters(dto.getParameters());
        rule.setCreatedAt(dto.getCreatedAt());
        rule.setUpdatedAt(dto.getUpdatedAt());
        rule.setCreatedBy(dto.getCreatedBy());
        rule.setUpdatedBy(dto.getUpdatedBy());
        return rule;
    }

    /**
     * Convert CreatePricingRuleCommand to PricingRule entity
     */
    public PricingRule toEntity(CreatePricingRuleCommand command) {
        PricingRule rule = new PricingRule();
        rule.setTenantId(command.getTenantId());
        rule.setRuleId(command.getRuleName() != null
                ? command.getRuleName().toLowerCase().replaceAll("\\s+", "-")
                : UUID.randomUUID().toString());
        rule.setRuleName(command.getRuleName());
        rule.setDescription(command.getDescription());
        rule.setRuleType(command.getRuleType());
        rule.setPriority(command.getPriority());
        rule.setActive(command.getActive() != null ? command.getActive() : true);
        rule.setVehicleType(command.getVehicleType());
        rule.setServiceType(command.getServiceType());
        rule.setParameters(command.getParameters());
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        return rule;
    }

    /**
     * Convert PricingModel to PriceCalculationDTO
     */
    public PriceCalculationDTO toPriceCalculationDTO(PricingModel model) {
        return PriceCalculationDTO.builder()
                .quoteId(model.getQuoteId())
                .tenantId(model.getTenantId())
                .baseFare(model.getBaseFare())
                .distanceCharge(model.getDistanceCharge())
                .timeCharge(model.getTimeCharge())
                .weightCharge(model.getWeightCharge())
                .surcharge(model.getSurcharge())
                .discount(model.getDiscount())
                .tax(model.getTax())
                .subtotal(model.getSubtotal())
                .totalAmount(model.getTotalAmount())
                .currency(model.getCurrency())
                .validityMinutes(model.getValidityMinutes())
                .calculatedAt(model.getCalculatedAt())
                .expiresAt(model.getExpiresAt())
                .breakdown(model.getBreakdown())
                .metadata(model.getMetadata())
                .build();
    }

    /**
     * Create PricingModel with default values
     */
    public PricingModel createPricingModel(String tenantId) {
        PricingModel model = new PricingModel();
        model.setTenantId(tenantId);
        model.setQuoteId(UUID.randomUUID().toString());
        model.setBaseFare(BigDecimal.ZERO);
        model.setDistanceCharge(BigDecimal.ZERO);
        model.setTimeCharge(BigDecimal.ZERO);
        model.setWeightCharge(BigDecimal.ZERO);
        model.setSurcharge(BigDecimal.ZERO);
        model.setDiscount(BigDecimal.ZERO);
        model.setTax(BigDecimal.ZERO);
        model.setSubtotal(BigDecimal.ZERO);
        model.setTotalAmount(BigDecimal.ZERO);
        model.setCurrency("USD");
        model.setValidityMinutes(15);
        model.setCalculatedAt(LocalDateTime.now());
        model.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        model.setBreakdown(new HashMap<>());
        model.setMetadata(new HashMap<>());
        return model;
    }

    /**
     * Create PricingRequestDTO from command
     */
    public PricingRequestDTO toRequestDTO(
            String tenantId,
            String serviceType,
            String vehicleType,
            Double distanceKm,
            Integer estimatedDurationMinutes) {
        return PricingRequestDTO.builder()
                .tenantId(tenantId)
                .requestId(UUID.randomUUID().toString())
                .serviceType(serviceType)
                .vehicleType(vehicleType)
                .distanceKm(distanceKm)
                .estimatedDurationMinutes(estimatedDurationMinutes)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();
    }

    /**
     * Convert UpdatePricingRuleRequest to UpdatePricingRuleCommand
     */
    public com.gogidix.shared.courier.pricing.application.command.UpdatePricingRuleCommand toCommand(
            com.gogidix.shared.courier.pricing.interfaces.rest.PricingController.UpdatePricingRuleRequest request) {
        return com.gogidix.shared.courier.pricing.application.command.UpdatePricingRuleCommand.builder()
                .ruleId(request.getRuleName() != null
                        ? request.getRuleName().toLowerCase().replaceAll("\\s+", "-")
                        : null)
                .ruleName(request.getRuleName())
                .description(request.getDescription())
                .ruleType(request.getRuleType())
                .priority(request.getPriority())
                .active(request.getActive())
                .vehicleType(request.getVehicleType())
                .serviceType(request.getServiceType())
                .parameters(request.getParameters())
                .build();
    }
}
