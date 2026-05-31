package com.gogidix.management.executive.workflow.application.query;

import com.gogidix.management.executive.workflow.application.query.ListWorkflowQuery;
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
class ListWorkflowQueryTest {

        @Test
    void testBuilder() {
        ListWorkflowQuery dto = ListWorkflowQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListWorkflowQuery.WorkflowStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-ownerId", dto.getOwnerId());
        assertEquals(ListWorkflowQuery.WorkflowStatus.DRAFT, dto.getStatus());
        assertEquals(42, dto.getPage());
        assertEquals(42, dto.getSize());
    }

    @Test
    void testSettersAndGetters() {
        ListWorkflowQuery dto = new ListWorkflowQuery();
        dto.setTenantId("val-tenantId");
        dto.setOwnerId("val-ownerId");
        dto.setStatus(ListWorkflowQuery.WorkflowStatus.DRAFT);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-ownerId", dto.getOwnerId());
        assertEquals(ListWorkflowQuery.WorkflowStatus.DRAFT, dto.getStatus());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ListWorkflowQuery dto1 = ListWorkflowQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListWorkflowQuery.WorkflowStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        ListWorkflowQuery dto2 = ListWorkflowQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListWorkflowQuery.WorkflowStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ListWorkflowQuery dto = ListWorkflowQuery.builder()
                        .tenantId("test-tenantId")
            .ownerId("test-ownerId")
            .status(ListWorkflowQuery.WorkflowStatus.DRAFT)
            .page(42)
            .size(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}