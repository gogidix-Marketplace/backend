package com.gogidix.finance.accountspayable.domain.port.in;

import com.gogidix.finance.accountspayable.domain.port.in.VendorCommand;
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
class VendorCommand_RemoveTagCommandTest {

        @Test
    void testSettersAndGetters() {
        VendorCommand.RemoveTagCommand dto = new VendorCommand.RemoveTagCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setTag("val-tag");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-tag", dto.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.RemoveTagCommand dto1 = new VendorCommand.RemoveTagCommand();
        VendorCommand.RemoveTagCommand dto2 = new VendorCommand.RemoveTagCommand();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setTag("test");
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setTag("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorCommand.RemoveTagCommand dto = new VendorCommand.RemoveTagCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setTag("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorCommand.RemoveTagCommand dto = new VendorCommand.RemoveTagCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setTag("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}