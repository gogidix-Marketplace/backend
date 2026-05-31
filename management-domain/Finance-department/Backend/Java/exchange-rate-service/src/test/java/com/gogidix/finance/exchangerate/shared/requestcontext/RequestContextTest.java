package com.gogidix.finance.exchangerate.shared.requestcontext;

import com.gogidix.finance.exchangerate.shared.requestcontext.RequestContext;
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
                        .correlationId("test-correlationId")
            .userId("test-userId")
            .tenantId("test-tenantId")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .metadata(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-correlationId", dto.getCorrelationId());
        assertEquals("test-userId", dto.getUserId());
        assertEquals("test-tenantId", dto.getTenantId());
    }

    @Test
    void testSettersAndGetters() {
        RequestContext dto = new RequestContext();
        dto.setCorrelationId("val-correlationId");
        dto.setUserId("val-userId");
        dto.setTenantId("val-tenantId");
        assertEquals("val-correlationId", dto.getCorrelationId());
        assertEquals("val-userId", dto.getUserId());
        assertEquals("val-tenantId", dto.getTenantId());
    }

    @Test
    void testEqualsAndHashCode() {
        RequestContext dto1 = RequestContext.builder()
                        .correlationId("test-correlationId")
            .userId("test-userId")
            .tenantId("test-tenantId")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .metadata(Collections.emptyMap())
            .build();
        RequestContext dto2 = RequestContext.builder()
                        .correlationId("test-correlationId")
            .userId("test-userId")
            .tenantId("test-tenantId")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .metadata(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RequestContext dto = RequestContext.builder()
                        .correlationId("test-correlationId")
            .userId("test-userId")
            .tenantId("test-tenantId")
            .timestamp(LocalDateTime.of(2025,1,15,10,0))
            .metadata(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}