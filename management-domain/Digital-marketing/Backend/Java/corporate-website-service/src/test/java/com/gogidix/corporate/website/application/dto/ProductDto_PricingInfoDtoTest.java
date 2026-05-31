package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.ProductDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductDto_PricingInfoDtoTest {

        @Test
    void testBuilder() {
        ProductDto.PricingInfoDto dto = ProductDto.PricingInfoDto.builder()
                        .basePrice(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle("test-billingCycle")
            .displayPricing(true)
            .startingFromText("test-startingFromText")
            .build();
        assertNotNull(dto);
        assertEquals(BigDecimal.TEN, dto.getBasePrice());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-billingCycle", dto.getBillingCycle());
        assertTrue(dto.isDisplayPricing());
        assertEquals("test-startingFromText", dto.getStartingFromText());
    }

    @Test
    void testSettersAndGetters() {
        ProductDto.PricingInfoDto dto = new ProductDto.PricingInfoDto();
        dto.setBasePrice(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setBillingCycle("val-billingCycle");
        dto.setDisplayPricing(true);
        dto.setStartingFromText("val-startingFromText");
        assertEquals(BigDecimal.ONE, dto.getBasePrice());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-billingCycle", dto.getBillingCycle());
        assertTrue(dto.isDisplayPricing());
        assertEquals("val-startingFromText", dto.getStartingFromText());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductDto.PricingInfoDto dto1 = ProductDto.PricingInfoDto.builder()
                        .basePrice(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle("test-billingCycle")
            .displayPricing(true)
            .startingFromText("test-startingFromText")
            .build();
        ProductDto.PricingInfoDto dto2 = ProductDto.PricingInfoDto.builder()
                        .basePrice(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle("test-billingCycle")
            .displayPricing(true)
            .startingFromText("test-startingFromText")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ProductDto.PricingInfoDto dto = ProductDto.PricingInfoDto.builder()
                        .basePrice(BigDecimal.TEN)
            .currency("test-currency")
            .billingCycle("test-billingCycle")
            .displayPricing(true)
            .startingFromText("test-startingFromText")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}