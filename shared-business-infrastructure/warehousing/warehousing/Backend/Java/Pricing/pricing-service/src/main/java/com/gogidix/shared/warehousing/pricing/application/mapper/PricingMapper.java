package com.gogidix.shared.warehousing.pricing.application.mapper;

import com.gogidix.shared.warehousing.pricing.application.command.CreatePricingRuleCommand;
import com.gogidix.shared.warehousing.pricing.application.command.CreatePriceTierCommand;
import com.gogidix.shared.warehousing.pricing.application.command.UpdatePricingRuleCommand;
import com.gogidix.shared.warehousing.pricing.application.dto.*;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceQuote;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceTier;
import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for pricing entity/DTO conversions
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface PricingMapper {

    // PricingRule mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PricingRule toEntity(CreatePricingRuleCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget PricingRule pricingRule, UpdatePricingRuleCommand command);

    PricingRuleDTO toDTO(PricingRule pricingRule);

    List<PricingRuleDTO> toPricingRuleDTOList(List<PricingRule> pricingRules);

    // PriceQuote mappings
    PriceQuoteDTO toPriceQuoteDTO(PriceQuote priceQuote);

    List<PriceQuoteDTO> toPriceQuoteDTOList(List<PriceQuote> priceQuotes);

    // PriceTier mappings
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PriceTier toEntity(CreatePriceTierCommand command);

    PriceTierDTO toPriceTierDTO(PriceTier priceTier);

    List<PriceTierDTO> toPriceTierDTOList(List<PriceTier> priceTiers);

    // PriceCalculationResult mapping
    @Mapping(source = "priceQuote.id", target = "quoteId")
    @Mapping(source = "priceQuote.quoteNumber", target = "quoteNumber")
    @Mapping(source = "priceQuote.validUntil", target = "validUntil")
    @Mapping(source = "priceQuote.breakdown", target = "breakdown")
    PriceCalculationResultDTO toPriceCalculationResultDTO(PriceQuote priceQuote);
}
