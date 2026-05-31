package com.gogidix.sales.communication.shared.requestcontext;

import com.gogidix.sales.communication.shared.requestcontext.RequestContext;
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
            .requestId("test-requestId")
            .channel("test-channel")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-requestId", dto.getRequestId());
        assertEquals("test-channel", dto.getChannel());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setTenantId("val-tenantId");
        dto.setUserId("val-userId");
        dto.setCorrelationId("val-correlationId");
        dto.setRequestId("val-requestId");
        dto.setChannel("val-channel");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-requestId", dto.getRequestId());
        assertEquals("val-channel", dto.getChannel());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .requestId("test-requestId")
            .channel("test-channel")
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .tenantId("test-tenantId")
            .userId("test-userId")
            .correlationId("test-correlationId")
            .requestId("test-requestId")
            .channel("test-channel")
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
            .requestId("test-requestId")
            .channel("test-channel")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}