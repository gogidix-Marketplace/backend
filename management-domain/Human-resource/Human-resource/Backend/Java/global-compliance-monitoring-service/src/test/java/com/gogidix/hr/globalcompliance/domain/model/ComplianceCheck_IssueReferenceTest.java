package com.gogidix.hr.globalcompliance.domain.model;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;
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
class ComplianceCheck_IssueReferenceTest {

        @Test
    void testBuilder() {
        ComplianceCheck.IssueReference dto = ComplianceCheck.IssueReference.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .severity("test-severity")
            .build();
        assertNotNull(dto);
        assertEquals("test-issueId", dto.getIssueId());
        assertEquals("test-issueNumber", dto.getIssueNumber());
        assertEquals("test-severity", dto.getSeverity());
    }

    @Test
    void testSettersAndGetters() {
        ComplianceCheck.IssueReference dto = new ComplianceCheck.IssueReference();
        dto.setIssueId("val-issueId");
        dto.setIssueNumber("val-issueNumber");
        dto.setSeverity("val-severity");
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-issueNumber", dto.getIssueNumber());
        assertEquals("val-severity", dto.getSeverity());
    }

    @Test
    void testEqualsAndHashCode() {
        ComplianceCheck.IssueReference dto1 = ComplianceCheck.IssueReference.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .severity("test-severity")
            .build();
        ComplianceCheck.IssueReference dto2 = ComplianceCheck.IssueReference.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .severity("test-severity")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComplianceCheck.IssueReference dto = ComplianceCheck.IssueReference.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .severity("test-severity")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}