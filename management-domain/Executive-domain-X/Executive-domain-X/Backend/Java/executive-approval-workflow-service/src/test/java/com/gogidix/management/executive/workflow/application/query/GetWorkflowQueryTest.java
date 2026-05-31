package com.gogidix.management.executive.workflow.application.query;

import com.gogidix.management.executive.workflow.application.query.GetWorkflowQuery;
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
class GetWorkflowQueryTest {

        @Test
    void testBuilder() {
        GetWorkflowQuery dto = GetWorkflowQuery.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .build();
        assertNotNull(dto);
        assertEquals("test-workflowId", dto.getWorkflowId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        GetWorkflowQuery dto = new GetWorkflowQuery();
        dto.setWorkflowId("val-workflowId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-workflowId", dto.getWorkflowId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        GetWorkflowQuery dto1 = GetWorkflowQuery.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .build();
        GetWorkflowQuery dto2 = GetWorkflowQuery.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GetWorkflowQuery dto = GetWorkflowQuery.builder()
                        .workflowId("test-workflowId")
            .tenantId("test-tenantId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}