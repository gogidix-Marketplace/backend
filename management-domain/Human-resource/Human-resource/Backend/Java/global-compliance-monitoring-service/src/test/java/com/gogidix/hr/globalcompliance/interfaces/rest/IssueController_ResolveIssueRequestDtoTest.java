package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.IssueController;
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
class IssueController_ResolveIssueRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        IssueController.ResolveIssueRequestDto dto = new IssueController.ResolveIssueRequestDto();
        dto.setResolution("val-resolution");
        dto.setRootCause("val-rootCause");
        assertEquals("val-resolution", dto.getResolution());
        assertEquals("val-rootCause", dto.getRootCause());
    }

    @Test
    void testEqualsAndHashCode() {
        IssueController.ResolveIssueRequestDto dto1 = new IssueController.ResolveIssueRequestDto();
        IssueController.ResolveIssueRequestDto dto2 = new IssueController.ResolveIssueRequestDto();
        dto1.setResolution("test");
        dto1.setRootCause("test");
        dto2.setResolution("test");
        dto2.setRootCause("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setResolution(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        IssueController.ResolveIssueRequestDto dto = new IssueController.ResolveIssueRequestDto();
        dto.setResolution("test");
        dto.setRootCause("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        IssueController.ResolveIssueRequestDto dto = new IssueController.ResolveIssueRequestDto();
        dto.setResolution("test");
        dto.setRootCause("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}