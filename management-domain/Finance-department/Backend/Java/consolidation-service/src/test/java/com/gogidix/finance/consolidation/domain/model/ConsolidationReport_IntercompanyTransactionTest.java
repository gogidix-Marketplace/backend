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
class ConsolidationReport_IntercompanyTransactionTest {

        @Test
    void testBuilder() {
        ConsolidationReport.IntercompanyTransaction dto = ConsolidationReport.IntercompanyTransaction.builder()
                        .transactionId("test-transactionId")
            .fromEntityId("test-fromEntityId")
            .fromEntityName("test-fromEntityName")
            .toEntityId("test-toEntityId")
            .toEntityName("test-toEntityName")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isEliminated(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-transactionId", dto.getTransactionId());
        assertEquals("test-fromEntityId", dto.getFromEntityId());
        assertEquals("test-fromEntityName", dto.getFromEntityName());
        assertEquals("test-toEntityId", dto.getToEntityId());
        assertEquals("test-toEntityName", dto.getToEntityName());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,1,15), dto.getTransactionDate());
        assertEquals("test-description", dto.getDescription());
        assertTrue(dto.getIsEliminated());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.IntercompanyTransaction dto = new ConsolidationReport.IntercompanyTransaction();
        dto.setTransactionId("val-transactionId");
        dto.setFromEntityId("val-fromEntityId");
        dto.setFromEntityName("val-fromEntityName");
        dto.setToEntityId("val-toEntityId");
        dto.setToEntityName("val-toEntityName");
        dto.setAccountCode("val-accountCode");
        dto.setAccountName("val-accountName");
        dto.setAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setTransactionDate(LocalDate.of(2025,6,1));
        dto.setDescription("val-description");
        dto.setIsEliminated(true);
        assertEquals("val-transactionId", dto.getTransactionId());
        assertEquals("val-fromEntityId", dto.getFromEntityId());
        assertEquals("val-fromEntityName", dto.getFromEntityName());
        assertEquals("val-toEntityId", dto.getToEntityId());
        assertEquals("val-toEntityName", dto.getToEntityName());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(LocalDate.of(2025,6,1), dto.getTransactionDate());
        assertEquals("val-description", dto.getDescription());
        assertTrue(dto.getIsEliminated());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.IntercompanyTransaction dto1 = ConsolidationReport.IntercompanyTransaction.builder()
                        .transactionId("test-transactionId")
            .fromEntityId("test-fromEntityId")
            .fromEntityName("test-fromEntityName")
            .toEntityId("test-toEntityId")
            .toEntityName("test-toEntityName")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isEliminated(true)
            .build();
        ConsolidationReport.IntercompanyTransaction dto2 = ConsolidationReport.IntercompanyTransaction.builder()
                        .transactionId("test-transactionId")
            .fromEntityId("test-fromEntityId")
            .fromEntityName("test-fromEntityName")
            .toEntityId("test-toEntityId")
            .toEntityName("test-toEntityName")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isEliminated(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.IntercompanyTransaction dto = ConsolidationReport.IntercompanyTransaction.builder()
                        .transactionId("test-transactionId")
            .fromEntityId("test-fromEntityId")
            .fromEntityName("test-fromEntityName")
            .toEntityId("test-toEntityId")
            .toEntityName("test-toEntityName")
            .accountCode("test-accountCode")
            .accountName("test-accountName")
            .amount(BigDecimal.TEN)
            .currency("test-currency")
            .transactionDate(LocalDate.of(2025,1,15))
            .description("test-description")
            .isEliminated(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}