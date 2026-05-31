package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.AgentQualityProfile;
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
class AgentQualityProfile_ChannelStatsTest {

        @Test
    void testBuilder() {
        AgentQualityProfile.ChannelStats dto = AgentQualityProfile.ChannelStats.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-channel", dto.getChannel());
        assertEquals(42L, dto.getReviewCount());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfile.ChannelStats dto = new AgentQualityProfile.ChannelStats();
        dto.setChannel("val-channel");
        assertEquals("val-channel", dto.getChannel());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfile.ChannelStats dto1 = AgentQualityProfile.ChannelStats.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend(null)
            .build();
        AgentQualityProfile.ChannelStats dto2 = AgentQualityProfile.ChannelStats.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfile.ChannelStats dto = AgentQualityProfile.ChannelStats.builder()
                        .channel("test-channel")
            .reviewCount(42L)
            .averageScore(null)
            .passRate(null)
            .trend(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}