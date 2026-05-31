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
class ConsolidationReport_ValidationErrorTest {

        @Test
    void testBuilder() {
        ConsolidationReport.ValidationError dto = ConsolidationReport.ValidationError.builder()
                        .errorId("test-errorId")
            .severity("test-severity")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .affectedAccount("test-affectedAccount")
            .affectedAmount(BigDecimal.TEN)
            .recommendation("test-recommendation")
            .build();
        assertNotNull(dto);
        assertEquals("test-errorId", dto.getErrorId());
        assertEquals("test-severity", dto.getSeverity());
        assertEquals("test-errorCode", dto.getErrorCode());
        assertEquals("test-errorMessage", dto.getErrorMessage());
        assertEquals("test-affectedAccount", dto.getAffectedAccount());
        assertEquals(BigDecimal.TEN, dto.getAffectedAmount());
        assertEquals("test-recommendation", dto.getRecommendation());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationReport.ValidationError dto = new ConsolidationReport.ValidationError();
        dto.setErrorId("val-errorId");
        dto.setSeverity("val-severity");
        dto.setErrorCode("val-errorCode");
        dto.setErrorMessage("val-errorMessage");
        dto.setAffectedAccount("val-affectedAccount");
        dto.setAffectedAmount(BigDecimal.ONE);
        dto.setRecommendation("val-recommendation");
        assertEquals("val-errorId", dto.getErrorId());
        assertEquals("val-severity", dto.getSeverity());
        assertEquals("val-errorCode", dto.getErrorCode());
        assertEquals("val-errorMessage", dto.getErrorMessage());
        assertEquals("val-affectedAccount", dto.getAffectedAccount());
        assertEquals(BigDecimal.ONE, dto.getAffectedAmount());
        assertEquals("val-recommendation", dto.getRecommendation());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationReport.ValidationError dto1 = ConsolidationReport.ValidationError.builder()
                        .errorId("test-errorId")
            .severity("test-severity")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .affectedAccount("test-affectedAccount")
            .affectedAmount(BigDecimal.TEN)
            .recommendation("test-recommendation")
            .build();
        ConsolidationReport.ValidationError dto2 = ConsolidationReport.ValidationError.builder()
                        .errorId("test-errorId")
            .severity("test-severity")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .affectedAccount("test-affectedAccount")
            .affectedAmount(BigDecimal.TEN)
            .recommendation("test-recommendation")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationReport.ValidationError dto = ConsolidationReport.ValidationError.builder()
                        .errorId("test-errorId")
            .severity("test-severity")
            .errorCode("test-errorCode")
            .errorMessage("test-errorMessage")
            .affectedAccount("test-affectedAccount")
            .affectedAmount(BigDecimal.TEN)
            .recommendation("test-recommendation")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}