package com.gogidix.sales.onboarding.shared.requestcontext;

import com.gogidix.sales.onboarding.shared.requestcontext.RequestContext;
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
class RequestContextTest {

        @Test
    void testBuilder() {
        RequestContext dto = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .requestId("test-requestId")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-organizationId", dto.getOrganizationId());
        assertEquals("test-requestId", dto.getRequestId());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setCorrelationId("val-correlationId");
        dto.setOrganizationId("val-organizationId");
        dto.setRequestId("val-requestId");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-organizationId", dto.getOrganizationId());
        assertEquals("val-requestId", dto.getRequestId());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .requestId("test-requestId")
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .requestId("test-requestId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RequestContext dto = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .organizationId("test-organizationId")
            .requestId("test-requestId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}