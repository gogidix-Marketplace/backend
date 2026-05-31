package com.gogidix.customersupport.slamanagement.application.dto;

import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
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
class SLAPolicyResponseDto_EscalationRuleDtoTest {

        @Test
    void testBuilder() {
        SLAPolicyResponseDto.EscalationRuleDto dto = SLAPolicyResponseDto.EscalationRuleDto.builder()
                        .level(42)
            .name("test-name")
            .triggerAfterMinutes(42L)
            .escalateTo("test-escalateTo")
            .notifyUsers("test-notifyUsers")
            .notifyTeams("test-notifyTeams")
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getLevel());
        assertEquals("test-name", dto.getName());
        assertEquals(42L, dto.getTriggerAfterMinutes());
        assertEquals("test-escalateTo", dto.getEscalateTo());
        assertEquals("test-notifyUsers", dto.getNotifyUsers());
        assertEquals("test-notifyTeams", dto.getNotifyTeams());
    }

    @Test
    void testSettersAndGetters() {
        SLAPolicyResponseDto.EscalationRuleDto dto = new SLAPolicyResponseDto.EscalationRuleDto();
        dto.setLevel(99);
        dto.setName("val-name");
        dto.setEscalateTo("val-escalateTo");
        dto.setNotifyUsers("val-notifyUsers");
        dto.setNotifyTeams("val-notifyTeams");
        assertEquals(99, dto.getLevel());
        assertEquals("val-name", dto.getName());
        assertEquals("val-escalateTo", dto.getEscalateTo());
        assertEquals("val-notifyUsers", dto.getNotifyUsers());
        assertEquals("val-notifyTeams", dto.getNotifyTeams());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicyResponseDto.EscalationRuleDto dto1 = SLAPolicyResponseDto.EscalationRuleDto.builder()
                        .level(42)
            .name("test-name")
            .triggerAfterMinutes(42L)
            .escalateTo("test-escalateTo")
            .notifyUsers("test-notifyUsers")
            .notifyTeams("test-notifyTeams")
            .build();
        SLAPolicyResponseDto.EscalationRuleDto dto2 = SLAPolicyResponseDto.EscalationRuleDto.builder()
                        .level(42)
            .name("test-name")
            .triggerAfterMinutes(42L)
            .escalateTo("test-escalateTo")
            .notifyUsers("test-notifyUsers")
            .notifyTeams("test-notifyTeams")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SLAPolicyResponseDto.EscalationRuleDto dto = SLAPolicyResponseDto.EscalationRuleDto.builder()
                        .level(42)
            .name("test-name")
            .triggerAfterMinutes(42L)
            .escalateTo("test-escalateTo")
            .notifyUsers("test-notifyUsers")
            .notifyTeams("test-notifyTeams")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}