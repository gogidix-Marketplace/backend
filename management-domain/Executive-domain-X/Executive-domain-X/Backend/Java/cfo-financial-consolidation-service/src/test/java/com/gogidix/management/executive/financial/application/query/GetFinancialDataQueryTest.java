package com.gogidix.management.executive.financial.application.query;

import com.gogidix.management.executive.financial.application.query.GetFinancialDataQuery;
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
class GetFinancialDataQueryTest {

        @Test
    void testBuilder() {
        GetFinancialDataQuery dto = GetFinancialDataQuery.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-financialDataId", dto.getFinancialDataId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        GetFinancialDataQuery dto = new GetFinancialDataQuery();
        dto.setFinancialDataId("val-financialDataId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-financialDataId", dto.getFinancialDataId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetFinancialDataQuery dto1 = GetFinancialDataQuery.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .build();
        GetFinancialDataQuery dto2 = GetFinancialDataQuery.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GetFinancialDataQuery dto = GetFinancialDataQuery.builder()
                        .financialDataId("test-financialDataId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}