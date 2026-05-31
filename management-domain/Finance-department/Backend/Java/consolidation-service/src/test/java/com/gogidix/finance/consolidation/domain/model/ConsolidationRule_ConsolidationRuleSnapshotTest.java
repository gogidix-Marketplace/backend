package com.gogidix.finance.consolidation.domain.model;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;
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
class ConsolidationRule_ConsolidationRuleSnapshotTest {

        @Test
    void testBuilder() {
        ConsolidationRule.ConsolidationRuleSnapshot dto = ConsolidationRule.ConsolidationRuleSnapshot.builder()
                        .snapshotId("test-snapshotId")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .capturedBy("test-capturedBy")
            .version("test-version")
            .data("test-data")
            .build();
        assertNotNull(dto);
        assertEquals("test-snapshotId", dto.getSnapshotId());
        assertEquals("test-capturedBy", dto.getCapturedBy());
        assertEquals("test-version", dto.getVersion());
        assertEquals("test-data", dto.getData());
    }

    @Test
    void testSettersAndGetters() {
        ConsolidationRule.ConsolidationRuleSnapshot dto = new ConsolidationRule.ConsolidationRuleSnapshot();
        dto.setSnapshotId("val-snapshotId");
        dto.setCapturedBy("val-capturedBy");
        dto.setVersion("val-version");
        dto.setData("val-data");
        assertEquals("val-snapshotId", dto.getSnapshotId());
        assertEquals("val-capturedBy", dto.getCapturedBy());
        assertEquals("val-version", dto.getVersion());
        assertEquals("val-data", dto.getData());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationRule.ConsolidationRuleSnapshot dto1 = ConsolidationRule.ConsolidationRuleSnapshot.builder()
                        .snapshotId("test-snapshotId")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .capturedBy("test-capturedBy")
            .version("test-version")
            .data("test-data")
            .build();
        ConsolidationRule.ConsolidationRuleSnapshot dto2 = ConsolidationRule.ConsolidationRuleSnapshot.builder()
                        .snapshotId("test-snapshotId")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .capturedBy("test-capturedBy")
            .version("test-version")
            .data("test-data")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConsolidationRule.ConsolidationRuleSnapshot dto = ConsolidationRule.ConsolidationRuleSnapshot.builder()
                        .snapshotId("test-snapshotId")
            .capturedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .capturedBy("test-capturedBy")
            .version("test-version")
            .data("test-data")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}