package com.gogidix.hr.employeeselfservice.domain.model;

import com.gogidix.hr.employeeselfservice.domain.model.SelfServiceRequest;
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
class SelfServiceRequest_AuditEntryTest {

        @Test
    void testSettersAndGetters() {
        SelfServiceRequest.AuditEntry dto = new SelfServiceRequest.AuditEntry();
        dto.setAction("val-action");
        dto.setDescription("val-description");
        dto.setTimestamp("val-timestamp");
        assertEquals("val-action", dto.getAction());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-timestamp", dto.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        SelfServiceRequest.AuditEntry dto1 = new SelfServiceRequest.AuditEntry();
        SelfServiceRequest.AuditEntry dto2 = new SelfServiceRequest.AuditEntry();
        dto1.setAction("test");
        dto1.setDescription("test");
        dto1.setTimestamp("test");
        dto2.setAction("test");
        dto2.setDescription("test");
        dto2.setTimestamp("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setAction(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        SelfServiceRequest.AuditEntry dto = new SelfServiceRequest.AuditEntry();
        dto.setAction("test");
        dto.setDescription("test");
        dto.setTimestamp("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        SelfServiceRequest.AuditEntry dto = new SelfServiceRequest.AuditEntry();
        dto.setAction("test");
        dto.setDescription("test");
        dto.setTimestamp("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}