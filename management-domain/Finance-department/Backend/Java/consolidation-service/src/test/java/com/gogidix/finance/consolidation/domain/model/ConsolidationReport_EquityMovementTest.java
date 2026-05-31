package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationReport;
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
class ConsolidationReport_EquityMovementTest {

        @Test
    void testBuilder() {
        ConsolidationReport.EquityMovement dto = ConsolidationReport.EquityMovement.builder()
                        .movementType("test-movementType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .movementDate(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-movementType", dto.getMovementType());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getMovementDate());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.EquityMovement dto = new ConsolidationReport.EquityMovement();
        dto.setMovementType("val-movementType");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setMovementDate(LocalDate.of(2025,6,1));
        assertEquals("val-movementType", dto.getMovementType());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getMovementDate());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.EquityMovement dto1 = ConsolidationReport.EquityMovement.builder()
                        .movementType("test-movementType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .movementDate(LocalDate.of(2025,1,15))
            .build();
        ConsolidationReport.EquityMovement dto2 = ConsolidationReport.EquityMovement.builder()
                        .movementType("test-movementType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .movementDate(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.EquityMovement dto = ConsolidationReport.EquityMovement.builder()
                        .movementType("test-movementType")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .movementDate(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}