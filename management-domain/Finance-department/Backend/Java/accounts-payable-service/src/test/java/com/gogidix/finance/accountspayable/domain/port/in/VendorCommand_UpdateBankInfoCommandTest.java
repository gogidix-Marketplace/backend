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
class VendorCommand_UpdateBankInfoCommandTest {

        @Test
    void testSettersAndGetters() {
        VendorCommand.UpdateBankInfoCommand dto = new VendorCommand.UpdateBankInfoCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setBankAccountNumber("val-bankAccountNumber");
        dto.setBankRoutingNumber("val-bankRoutingNumber");
        dto.setBankName("val-bankName");
        dto.setBankAccountType("val-bankAccountType");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-bankAccountNumber", dto.getBankAccountNumber());
        assertEquals("val-bankRoutingNumber", dto.getBankRoutingNumber());
        assertEquals("val-bankName", dto.getBankName());
        assertEquals("val-bankAccountType", dto.getBankAccountType());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.UpdateBankInfoCommand dto1 = new VendorCommand.UpdateBankInfoCommand();
        VendorCommand.UpdateBankInfoCommand dto2 = new VendorCommand.UpdateBankInfoCommand();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setBankAccountNumber("test");
        dto1.setBankRoutingNumber("test");
        dto1.setBankName("test");
        dto1.setBankAccountType("test");
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setBankAccountNumber("test");
        dto2.setBankRoutingNumber("test");
        dto2.setBankName("test");
        dto2.setBankAccountType("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorCommand.UpdateBankInfoCommand dto = new VendorCommand.UpdateBankInfoCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setBankAccountNumber("test");
        dto.setBankRoutingNumber("test");
        dto.setBankName("test");
        dto.setBankAccountType("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorCommand.UpdateBankInfoCommand dto = new VendorCommand.UpdateBankInfoCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setBankAccountNumber("test");
        dto.setBankRoutingNumber("test");
        dto.setBankName("test");
        dto.setBankAccountType("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}