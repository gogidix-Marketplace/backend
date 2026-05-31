package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxCalculationQuery;
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
class TaxCalculationQuery_GetCalculationsByTransactionQueryTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationQuery.GetCalculationsByTransactionQuery dto = new TaxCalculationQuery.GetCalculationsByTransactionQuery();
        dto.setTenantId("val-tenantId");
        dto.setTransactionId("val-transactionId");
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationQuery.GetCalculationsByTransactionQuery dto1 = new TaxCalculationQuery.GetCalculationsByTransactionQuery();
        TaxCalculationQuery.GetCalculationsByTransactionQuery dto2 = new TaxCalculationQuery.GetCalculationsByTransactionQuery();
        dto1.setTenantId("test");
        dto1.setTransactionId("test");
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setTransactionId("test");
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationQuery.GetCalculationsByTransactionQuery dto = new TaxCalculationQuery.GetCalculationsByTransactionQuery();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationQuery.GetCalculationsByTransactionQuery dto = new TaxCalculationQuery.GetCalculationsByTransactionQuery();
        dto.setTenantId("test");
        dto.setTransactionId("test");
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}