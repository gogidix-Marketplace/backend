package com.gogidix.sales.territory.interfaces.rest;

import com.gogidix.sales.territory.interfaces.rest.QuotaController;
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
class QuotaController_UpdateAchievementRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        QuotaController.UpdateAchievementRequestDto dto = new QuotaController.UpdateAchievementRequestDto();
        dto.setAchievement(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getAchievement());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaController.UpdateAchievementRequestDto dto1 = new QuotaController.UpdateAchievementRequestDto();
        QuotaController.UpdateAchievementRequestDto dto2 = new QuotaController.UpdateAchievementRequestDto();
        dto1.setAchievement(BigDecimal.TEN);
        dto2.setAchievement(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAchievement(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaController.UpdateAchievementRequestDto dto = new QuotaController.UpdateAchievementRequestDto();
        dto.setAchievement(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaController.UpdateAchievementRequestDto dto = new QuotaController.UpdateAchievementRequestDto();
        dto.setAchievement(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}