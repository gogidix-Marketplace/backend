package com.gogidix.management.executive.technology.application.query;

import com.gogidix.management.executive.technology.application.query.GetTechnologyQuery;
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
class GetTechnologyQueryTest {

        @Test
    void testBuilder() {
        GetTechnologyQuery dto = GetTechnologyQuery.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-technologyId", dto.getTechnologyId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        GetTechnologyQuery dto = new GetTechnologyQuery();
        dto.setTechnologyId("val-technologyId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-technologyId", dto.getTechnologyId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetTechnologyQuery dto1 = GetTechnologyQuery.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        GetTechnologyQuery dto2 = GetTechnologyQuery.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GetTechnologyQuery dto = GetTechnologyQuery.builder()
                        .technologyId("test-technologyId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}