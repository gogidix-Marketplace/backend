package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.model.CreditMemo;
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
class CreditMemo_CreditMemoApplicationTest {

        @Test
    void testBuilder() {
        CreditMemo.CreditMemoApplication dto = CreditMemo.CreditMemoApplication.builder()
                        .applicationId("test-applicationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .appliedAmount(BigDecimal.TEN)
            .applicationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .appliedBy("test-appliedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-applicationId", dto.getApplicationId());
        assertEquals("test-invoiceId", dto.getInvoiceId());
        assertEquals("test-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.TEN, dto.getAppliedAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getApplicationDate());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-appliedBy", dto.getAppliedBy());
    }

    @Test
    void testSettersAndGetters() {
        CreditMemo.CreditMemoApplication dto = new CreditMemo.CreditMemoApplication();
        dto.setApplicationId("val-applicationId");
        dto.setInvoiceId("val-invoiceId");
        dto.setInvoiceNumber("val-invoiceNumber");
        dto.setAppliedAmount(BigDecimal.ONE);
        dto.setApplicationDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        dto.setAppliedBy("val-appliedBy");
        assertEquals("val-applicationId", dto.getApplicationId());
        assertEquals("val-invoiceId", dto.getInvoiceId());
        assertEquals("val-invoiceNumber", dto.getInvoiceNumber());
        assertEquals(BigDecimal.ONE, dto.getAppliedAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getApplicationDate());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-appliedBy", dto.getAppliedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        CreditMemo.CreditMemoApplication dto1 = CreditMemo.CreditMemoApplication.builder()
                        .applicationId("test-applicationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .appliedAmount(BigDecimal.TEN)
            .applicationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .appliedBy("test-appliedBy")
            .build();
        CreditMemo.CreditMemoApplication dto2 = CreditMemo.CreditMemoApplication.builder()
                        .applicationId("test-applicationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .appliedAmount(BigDecimal.TEN)
            .applicationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .appliedBy("test-appliedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreditMemo.CreditMemoApplication dto = CreditMemo.CreditMemoApplication.builder()
                        .applicationId("test-applicationId")
            .invoiceId("test-invoiceId")
            .invoiceNumber("test-invoiceNumber")
            .appliedAmount(BigDecimal.TEN)
            .applicationDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .appliedBy("test-appliedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}