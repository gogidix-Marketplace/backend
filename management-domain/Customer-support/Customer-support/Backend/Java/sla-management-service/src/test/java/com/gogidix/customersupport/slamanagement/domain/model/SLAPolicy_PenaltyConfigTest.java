package com.gogidix.customersupport.slamanagement.domain.model;

import com.gogidix.customersupport.slamanagement.domain.model.SLAPolicy;
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
class SLAPolicy_PenaltyConfigTest {

        @Test
    void testBuilder() {
        SLAPolicy.PenaltyConfig dto = SLAPolicy.PenaltyConfig.builder()
                        .enabled(true)
            .penaltyPerBreach(Collections.emptyMap())
            .maxPenaltyPerTicket(null)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEnabled());
    }

    @Test
    void testSettersAndGetters() {
        SLAPolicy.PenaltyConfig dto = new SLAPolicy.PenaltyConfig();
        dto.setEnabled(true);
        assertTrue(dto.getEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicy.PenaltyConfig dto1 = SLAPolicy.PenaltyConfig.builder()
                        .enabled(true)
            .penaltyPerBreach(Collections.emptyMap())
            .maxPenaltyPerTicket(null)
            .build();
        SLAPolicy.PenaltyConfig dto2 = SLAPolicy.PenaltyConfig.builder()
                        .enabled(true)
            .penaltyPerBreach(Collections.emptyMap())
            .maxPenaltyPerTicket(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SLAPolicy.PenaltyConfig dto = SLAPolicy.PenaltyConfig.builder()
                        .enabled(true)
            .penaltyPerBreach(Collections.emptyMap())
            .maxPenaltyPerTicket(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}