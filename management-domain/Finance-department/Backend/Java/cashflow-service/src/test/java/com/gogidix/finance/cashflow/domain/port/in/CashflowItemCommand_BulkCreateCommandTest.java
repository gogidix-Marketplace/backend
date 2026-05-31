package com.gogidix.finance.cashflow.domain.port.in;

import com.gogidix.finance.cashflow.domain.port.in.CashflowItemCommand;
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
class CashflowItemCommand_BulkCreateCommandTest {

        @Test
    void testSettersAndGetters() {
        CashflowItemCommand.BulkCreateCommand dto = new CashflowItemCommand.BulkCreateCommand();
        dto.setTenantId("val-tenantId");
        dto.setRecordedBy("val-recordedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-recordedBy", dto.getRecordedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowItemCommand.BulkCreateCommand dto1 = new CashflowItemCommand.BulkCreateCommand();
        CashflowItemCommand.BulkCreateCommand dto2 = new CashflowItemCommand.BulkCreateCommand();
        dto1.setTenantId("test");
        dto1.setRecordedBy("test");
        dto1.setItems(Collections.emptyList());
        dto2.setTenantId("test");
        dto2.setRecordedBy("test");
        dto2.setItems(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowItemCommand.BulkCreateCommand dto = new CashflowItemCommand.BulkCreateCommand();
        dto.setTenantId("test");
        dto.setRecordedBy("test");
        dto.setItems(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowItemCommand.BulkCreateCommand dto = new CashflowItemCommand.BulkCreateCommand();
        dto.setTenantId("test");
        dto.setRecordedBy("test");
        dto.setItems(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}