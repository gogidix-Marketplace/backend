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
class VendorCommand_UpdatePaymentTermsCommandTest {

        @Test
    void testSettersAndGetters() {
        VendorCommand.UpdatePaymentTermsCommand dto = new VendorCommand.UpdatePaymentTermsCommand();
        dto.setTenantId("val-tenantId");
        dto.setVendorId("val-vendorId");
        dto.setPaymentTerms("val-paymentTerms");
        dto.setPaymentDays(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-vendorId", dto.getVendorId());
        assertEquals("val-paymentTerms", dto.getPaymentTerms());
        assertEquals(99, dto.getPaymentDays());
    }

    @Test
    void testEqualsAndHashCode() {
        VendorCommand.UpdatePaymentTermsCommand dto1 = new VendorCommand.UpdatePaymentTermsCommand();
        VendorCommand.UpdatePaymentTermsCommand dto2 = new VendorCommand.UpdatePaymentTermsCommand();
        dto1.setTenantId("test");
        dto1.setVendorId("test");
        dto1.setPaymentTerms("test");
        dto1.setPaymentDays(42);
        dto2.setTenantId("test");
        dto2.setVendorId("test");
        dto2.setPaymentTerms("test");
        dto2.setPaymentDays(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        VendorCommand.UpdatePaymentTermsCommand dto = new VendorCommand.UpdatePaymentTermsCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setPaymentTerms("test");
        dto.setPaymentDays(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        VendorCommand.UpdatePaymentTermsCommand dto = new VendorCommand.UpdatePaymentTermsCommand();
        dto.setTenantId("test");
        dto.setVendorId("test");
        dto.setPaymentTerms("test");
        dto.setPaymentDays(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}