package com.gogidix.finance.generalledger.application.service;

import com.gogidix.finance.generalledger.application.service.GeneralLedgerService;
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
class GeneralLedgerService_LedgerValidationResultTest {

        @Test
    void testBuilder() {
        GeneralLedgerService.LedgerValidationResult dto = GeneralLedgerService.LedgerValidationResult.builder()
                        .isValid(true)
            .errors(Collections.emptyList())
            .warnings(Collections.emptyList())
            .validatedAt(null)
            .build();
        assertNotNull(dto);
        assertTrue(dto.isValid());
    }

    @Test
    void testBuilderWithValues() {
        GeneralLedgerService.LedgerValidationResult dto = GeneralLedgerService.LedgerValidationResult.builder()
            .isValid(true)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        GeneralLedgerService.LedgerValidationResult dto1 = GeneralLedgerService.LedgerValidationResult.builder()
                        .isValid(true)
            .errors(Collections.emptyList())
            .warnings(Collections.emptyList())
            .validatedAt(null)
            .build();
        GeneralLedgerService.LedgerValidationResult dto2 = GeneralLedgerService.LedgerValidationResult.builder()
                        .isValid(true)
            .errors(Collections.emptyList())
            .warnings(Collections.emptyList())
            .validatedAt(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeneralLedgerService.LedgerValidationResult dto = GeneralLedgerService.LedgerValidationResult.builder()
                        .isValid(true)
            .errors(Collections.emptyList())
            .warnings(Collections.emptyList())
            .validatedAt(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}