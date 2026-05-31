package com.gogidix.management.executive.operations.application.query;

import com.gogidix.management.executive.operations.application.query.GetOperationsQuery;
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
class GetOperationsQueryTest {

        @Test
    void testBuilder() {
        GetOperationsQuery dto = GetOperationsQuery.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-operationsId", dto.getOperationsId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        GetOperationsQuery dto = new GetOperationsQuery();
        dto.setOperationsId("val-operationsId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-operationsId", dto.getOperationsId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetOperationsQuery dto1 = GetOperationsQuery.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .build();
        GetOperationsQuery dto2 = GetOperationsQuery.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GetOperationsQuery dto = GetOperationsQuery.builder()
                        .operationsId("test-operationsId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}