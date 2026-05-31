package com.gogidix.finance.forecasting.domain.port.in;

import com.gogidix.finance.forecasting.domain.port.in.ForecastCommand;
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
class ForecastCommand_ArchiveForecastCommandTest {

        @Test
    void testSettersAndGetters() {
        ForecastCommand.ArchiveForecastCommand dto = new ForecastCommand.ArchiveForecastCommand();
        dto.setTenantId("val-tenantId");
        dto.setForecastId("val-forecastId");
        dto.setArchivedBy("val-archivedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-forecastId", dto.getForecastId());
        assertEquals("val-archivedBy", dto.getArchivedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastCommand.ArchiveForecastCommand dto1 = new ForecastCommand.ArchiveForecastCommand();
        ForecastCommand.ArchiveForecastCommand dto2 = new ForecastCommand.ArchiveForecastCommand();
        dto1.setTenantId("test");
        dto1.setForecastId("test");
        dto1.setArchivedBy("test");
        dto2.setTenantId("test");
        dto2.setForecastId("test");
        dto2.setArchivedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastCommand.ArchiveForecastCommand dto = new ForecastCommand.ArchiveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setArchivedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastCommand.ArchiveForecastCommand dto = new ForecastCommand.ArchiveForecastCommand();
        dto.setTenantId("test");
        dto.setForecastId("test");
        dto.setArchivedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}