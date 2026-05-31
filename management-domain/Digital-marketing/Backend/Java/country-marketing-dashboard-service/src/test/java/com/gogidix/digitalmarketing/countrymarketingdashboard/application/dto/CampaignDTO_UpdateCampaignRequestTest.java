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
class CampaignDTO_UpdateCampaignRequestTest {

        @Test
    void testBuilder() {
        CampaignDTO.UpdateCampaignRequest dto = CampaignDTO.UpdateCampaignRequest.builder()
                        .name("test-name")
            .status("test-status")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-status", dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        CampaignDTO.UpdateCampaignRequest dto = new CampaignDTO.UpdateCampaignRequest();
        dto.setName("val-name");
        dto.setStatus("val-status");
        assertEquals("val-name", dto.getName());
        assertEquals("val-status", dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        CampaignDTO.UpdateCampaignRequest dto1 = CampaignDTO.UpdateCampaignRequest.builder()
                        .name("test-name")
            .status("test-status")
            .build();
        CampaignDTO.UpdateCampaignRequest dto2 = CampaignDTO.UpdateCampaignRequest.builder()
                        .name("test-name")
            .status("test-status")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CampaignDTO.UpdateCampaignRequest dto = CampaignDTO.UpdateCampaignRequest.builder()
                        .name("test-name")
            .status("test-status")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}