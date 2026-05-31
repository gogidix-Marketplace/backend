package com.gogidix.management.executive.technology.application.query;

import com.gogidix.management.executive.technology.application.query.ListTechnologyQuery;
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
class ListTechnologyQueryTest {

        @Test
    void testBuilder() {
        ListTechnologyQuery dto = ListTechnologyQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListTechnologyQuery.TechnologyStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals(ListTechnologyQuery.TechnologyStatus.DRAFT, dto.getStatus());
        assertEquals(42, dto.getPage());
        assertEquals(42, dto.getSize());
    }

    @Test
    void testSettersAndGetters() {
        ListTechnologyQuery dto = new ListTechnologyQuery();
        dto.setTenantId("val-tenantId");
        dto.setOwnerId("val-ownerId");
        dto.setStatus(ListTechnologyQuery.TechnologyStatus.DRAFT);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals(ListTechnologyQuery.TechnologyStatus.DRAFT, dto.getStatus());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ListTechnologyQuery dto1 = ListTechnologyQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListTechnologyQuery.TechnologyStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        ListTechnologyQuery dto2 = ListTechnologyQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListTechnologyQuery.TechnologyStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ListTechnologyQuery dto = ListTechnologyQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListTechnologyQuery.TechnologyStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}