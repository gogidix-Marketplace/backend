package com.gogidix.sales.territory.domain.port.in;

import com.gogidix.sales.territory.domain.port.in.QuotaCommand;
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
class QuotaCommand_UpdateAchievementCommandTest {

        @Test
    void testSettersAndGetters() {
        QuotaCommand.UpdateAchievementCommand dto = new QuotaCommand.UpdateAchievementCommand();
        dto.setTenantId("val-tenantId");
        dto.setQuotaId("val-quotaId");
        dto.setAchievement(BigDecimal.ONE);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-quotaId", dto.getQuotaId());
        assertEquals(BigDecimal.ONE, dto.getAchievement());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaCommand.UpdateAchievementCommand dto1 = new QuotaCommand.UpdateAchievementCommand();
        QuotaCommand.UpdateAchievementCommand dto2 = new QuotaCommand.UpdateAchievementCommand();
        dto1.setTenantId("test");
        dto1.setQuotaId("test");
        dto1.setAchievement(BigDecimal.TEN);
        dto2.setTenantId("test");
        dto2.setQuotaId("test");
        dto2.setAchievement(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaCommand.UpdateAchievementCommand dto = new QuotaCommand.UpdateAchievementCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        dto.setAchievement(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaCommand.UpdateAchievementCommand dto = new QuotaCommand.UpdateAchievementCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        dto.setAchievement(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}