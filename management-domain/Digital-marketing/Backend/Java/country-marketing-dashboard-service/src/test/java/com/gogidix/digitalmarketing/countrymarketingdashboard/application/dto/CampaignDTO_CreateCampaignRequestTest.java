package com.gogidix.digitalmarketing.countrymarketingdashboard.application.dto;

import com.gogidix.digitalmarketing.countrymarketingdashboard.application.dto.CampaignDTO;
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
class CampaignDTO_CreateCampaignRequestTest {

        @Test
    void testBuilder() {
        CampaignDTO.CreateCampaignRequest dto = CampaignDTO.CreateCampaignRequest.builder()
                        .name("test-name")
            .type("test-type")
            .budget(BigDecimal.TEN)
            .country("test-country")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
        assertEquals(BigDecimal.TEN, dto.getBudget());
        assertEquals("test-country", dto.getCountry());
    }

    @Test
    void testSettersAndGetters() {
        CampaignDTO.CreateCampaignRequest dto = new CampaignDTO.CreateCampaignRequest();
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setBudget(BigDecimal.ONE);
        dto.setCountry("val-country");
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
        assertEquals(BigDecimal.ONE, dto.getBudget());
        assertEquals("val-country", dto.getCountry());
    }

    @Test
    void testEqualsAndHashCode() {
        CampaignDTO.CreateCampaignRequest dto1 = CampaignDTO.CreateCampaignRequest.builder()
                        .name("test-name")
            .type("test-type")
            .budget(BigDecimal.TEN)
            .country("test-country")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        CampaignDTO.CreateCampaignRequest dto2 = CampaignDTO.CreateCampaignRequest.builder()
                        .name("test-name")
            .type("test-type")
            .budget(BigDecimal.TEN)
            .country("test-country")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CampaignDTO.CreateCampaignRequest dto = CampaignDTO.CreateCampaignRequest.builder()
                        .name("test-name")
            .type("test-type")
            .budget(BigDecimal.TEN)
            .country("test-country")
            .startDate(Instant.parse("2025-01-15T10:00:00Z"))
            .endDate(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}