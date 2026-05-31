package com.gogidix.management.executive.alert.application.query;

import com.gogidix.management.executive.alert.application.query.GetAlertQuery;
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
class GetAlertQueryTest {

        @Test
    void testBuilder() {
        GetAlertQuery dto = GetAlertQuery.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-alertId", dto.getAlertId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        GetAlertQuery dto = new GetAlertQuery();
        dto.setAlertId("val-alertId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-alertId", dto.getAlertId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetAlertQuery dto1 = GetAlertQuery.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .build();
        GetAlertQuery dto2 = GetAlertQuery.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GetAlertQuery dto = GetAlertQuery.builder()
                        .alertId("test-alertId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}