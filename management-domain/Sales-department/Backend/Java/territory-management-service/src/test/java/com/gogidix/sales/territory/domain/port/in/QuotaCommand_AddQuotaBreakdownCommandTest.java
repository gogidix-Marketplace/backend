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
class QuotaCommand_AddQuotaBreakdownCommandTest {

        @Test
    void testSettersAndGetters() {
        QuotaCommand.AddQuotaBreakdownCommand dto = new QuotaCommand.AddQuotaBreakdownCommand();
        dto.setTenantId("val-tenantId");
        dto.setQuotaId("val-quotaId");
        dto.setCategory("val-category");
        dto.setAmount(BigDecimal.ONE);
        dto.setDescription("val-description");
        dto.setProductId("val-productId");
        dto.setProductCategoryId("val-productCategoryId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-quotaId", dto.getQuotaId());
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productCategoryId", dto.getProductCategoryId());
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaCommand.AddQuotaBreakdownCommand dto1 = new QuotaCommand.AddQuotaBreakdownCommand();
        QuotaCommand.AddQuotaBreakdownCommand dto2 = new QuotaCommand.AddQuotaBreakdownCommand();
        dto1.setTenantId("test");
        dto1.setQuotaId("test");
        dto1.setCategory("test");
        dto1.setAmount(BigDecimal.TEN);
        dto1.setDescription("test");
        dto1.setProductId("test");
        dto1.setProductCategoryId("test");
        dto2.setTenantId("test");
        dto2.setQuotaId("test");
        dto2.setCategory("test");
        dto2.setAmount(BigDecimal.TEN);
        dto2.setDescription("test");
        dto2.setProductId("test");
        dto2.setProductCategoryId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        QuotaCommand.AddQuotaBreakdownCommand dto = new QuotaCommand.AddQuotaBreakdownCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        dto.setCategory("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setProductId("test");
        dto.setProductCategoryId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        QuotaCommand.AddQuotaBreakdownCommand dto = new QuotaCommand.AddQuotaBreakdownCommand();
        dto.setTenantId("test");
        dto.setQuotaId("test");
        dto.setCategory("test");
        dto.setAmount(BigDecimal.TEN);
        dto.setDescription("test");
        dto.setProductId("test");
        dto.setProductCategoryId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}