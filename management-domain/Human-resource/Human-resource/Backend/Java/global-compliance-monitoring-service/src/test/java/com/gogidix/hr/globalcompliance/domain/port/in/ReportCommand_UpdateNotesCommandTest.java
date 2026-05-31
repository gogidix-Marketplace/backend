package com.gogidix.hr.globalcompliance.domain.port.in;

import com.gogidix.hr.globalcompliance.domain.port.in.ReportCommand;
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
class ReportCommand_UpdateNotesCommandTest {

        @Test
    void testSettersAndGetters() {
        ReportCommand.UpdateNotesCommand dto = new ReportCommand.UpdateNotesCommand();
        dto.setTenantId("val-tenantId");
        dto.setReportId("val-reportId");
        dto.setNotes("val-notes");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-reportId", dto.getReportId());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportCommand.UpdateNotesCommand dto1 = new ReportCommand.UpdateNotesCommand();
        ReportCommand.UpdateNotesCommand dto2 = new ReportCommand.UpdateNotesCommand();
        dto1.setTenantId("test");
        dto1.setReportId("test");
        dto1.setNotes("test");
        dto2.setTenantId("test");
        dto2.setReportId("test");
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ReportCommand.UpdateNotesCommand dto = new ReportCommand.UpdateNotesCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ReportCommand.UpdateNotesCommand dto = new ReportCommand.UpdateNotesCommand();
        dto.setTenantId("test");
        dto.setReportId("test");
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}