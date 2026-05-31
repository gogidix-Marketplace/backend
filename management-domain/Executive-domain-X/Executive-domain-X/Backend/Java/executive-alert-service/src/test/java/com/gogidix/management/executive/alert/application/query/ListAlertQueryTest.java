package com.gogidix.management.executive.alert.application.query;

import com.gogidix.management.executive.alert.application.query.ListAlertQuery;
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
class ListAlertQueryTest {

        @Test
    void testBuilder() {
        ListAlertQuery dto = ListAlertQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListAlertQuery.AlertStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals(ListAlertQuery.AlertStatus.DRAFT, dto.getStatus());
        assertEquals(42, dto.getPage());
        assertEquals(42, dto.getSize());
    }

    @Test
    void testSettersAndGetters() {
        ListAlertQuery dto = new ListAlertQuery();
        dto.setTenantId("val-tenantId");
        dto.setOwnerId("val-ownerId");
        dto.setStatus(ListAlertQuery.AlertStatus.DRAFT);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals(ListAlertQuery.AlertStatus.DRAFT, dto.getStatus());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ListAlertQuery dto1 = ListAlertQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListAlertQuery.AlertStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        ListAlertQuery dto2 = ListAlertQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListAlertQuery.AlertStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ListAlertQuery dto = ListAlertQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListAlertQuery.AlertStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}