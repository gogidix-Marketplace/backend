package com.gogidix.finance.generalledger.interfaces.rest;

import com.gogidix.finance.generalledger.interfaces.rest.GeneralLedgerController;
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
class GeneralLedgerController_ReconcileAccountRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        GeneralLedgerController.ReconcileAccountRequestDto dto = new GeneralLedgerController.ReconcileAccountRequestDto();
        dto.setStatementBalance("val-statementBalance");
        assertEquals("val-statementBalance", dto.getStatementBalance());
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerController.ReconcileAccountRequestDto dto1 = new GeneralLedgerController.ReconcileAccountRequestDto();
        GeneralLedgerController.ReconcileAccountRequestDto dto2 = new GeneralLedgerController.ReconcileAccountRequestDto();
        dto1.setStatementBalance("test");
        dto2.setStatementBalance("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setStatementBalance(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        GeneralLedgerController.ReconcileAccountRequestDto dto = new GeneralLedgerController.ReconcileAccountRequestDto();
        dto.setStatementBalance("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        GeneralLedgerController.ReconcileAccountRequestDto dto = new GeneralLedgerController.ReconcileAccountRequestDto();
        dto.setStatementBalance("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}