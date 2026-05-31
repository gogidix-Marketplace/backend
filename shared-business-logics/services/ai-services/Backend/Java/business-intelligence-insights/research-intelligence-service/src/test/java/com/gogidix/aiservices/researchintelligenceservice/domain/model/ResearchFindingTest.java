package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResearchFinding Domain Model Tests")
class ResearchFindingTest {

    @Test
    @DisplayName("Should create research finding successfully")
    void shouldCreateResearchFinding() {
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Novel Discovery",
                "Important research finding",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.95,
                "researcher@example.com"
        );

        assertNotNull(finding);
        assertEquals("find-001", finding.getFindingId());
        assertEquals("proj-001", finding.getProjectId());
        assertEquals("Novel Discovery", finding.getTitle());
        assertEquals("Important research finding", finding.getDescription());
        assertEquals(ResearchFinding.FindingType.NOVEL_DISCOVERY, finding.getType());
        assertEquals(0.95, finding.getSignificanceScore());
        assertEquals("researcher@example.com", finding.getDiscoveredBy());
        assertEquals(ResearchFinding.FindingStatus.PENDING_REVIEW, finding.getStatus());
        assertEquals(0, finding.getCitationCount());
    }

    @Test
    @DisplayName("Should clamp significance score to maximum 1.0")
    void shouldClampSignificanceToMax() {
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Discovery",
                "Description",
                ResearchFinding.FindingType.DATA_INSIGHT,
                1.5,
                "researcher@example.com"
        );

        assertEquals(1.0, finding.getSignificanceScore());
    }

    @Test
    @DisplayName("Should clamp significance score to minimum 0.0")
    void shouldClampSignificanceToMin() {
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Discovery",
                "Description",
                ResearchFinding.FindingType.DATA_INSIGHT,
                -0.5,
                "researcher@example.com"
        );

        assertEquals(0.0, finding.getSignificanceScore());
    }

    @Test
    @DisplayName("Should throw exception when findingId is null")
    void shouldThrowWhenFindingIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchFinding(
                        null,
                        "proj-001",
                        "Title",
                        "Description",
                        ResearchFinding.FindingType.DATA_INSIGHT,
                        0.5,
                        "researcher@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when projectId is null")
    void shouldThrowWhenProjectIdIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchFinding(
                        "find-001",
                        null,
                        "Title",
                        "Description",
                        ResearchFinding.FindingType.DATA_INSIGHT,
                        0.5,
                        "researcher@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowWhenTitleIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchFinding(
                        "find-001",
                        "proj-001",
                        null,
                        "Description",
                        ResearchFinding.FindingType.DATA_INSIGHT,
                        0.5,
                        "researcher@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should throw exception when type is null")
    void shouldThrowWhenTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
                new ResearchFinding(
                        "find-001",
                        "proj-001",
                        "Title",
                        "Description",
                        null,
                        0.5,
                        "researcher@example.com"
                )
        );
    }

    @Test
    @DisplayName("Should allow null description")
    void shouldAllowNullDescription() {
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Title",
                null,
                ResearchFinding.FindingType.DATA_INSIGHT,
                0.5,
                "researcher@example.com"
        );

        assertNull(finding.getDescription());
    }

    @Test
    @DisplayName("Should allow null discoveredBy")
    void shouldAllowNullDiscoveredBy() {
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Title",
                "Description",
                ResearchFinding.FindingType.DATA_INSIGHT,
                0.5,
                null
        );

        assertNull(finding.getDiscoveredBy());
    }

    @Test
    @DisplayName("Should verify discoveredAt is set to current time")
    void shouldVerifyDiscoveredAt() {
        LocalDateTime before = LocalDateTime.now();
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Title",
                "Description",
                ResearchFinding.FindingType.DATA_INSIGHT,
                0.5,
                "researcher@example.com"
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(finding.getDiscoveredAt());
        assertTrue(finding.getDiscoveredAt().isBefore(after) || finding.getDiscoveredAt().isEqual(after));
        assertTrue(finding.getDiscoveredAt().isAfter(before) || finding.getDiscoveredAt().isEqual(before));
    }

    @Test
    @DisplayName("Should verify recordedAt is set to current time")
    void shouldVerifyRecordedAt() {
        LocalDateTime before = LocalDateTime.now();
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Title",
                "Description",
                ResearchFinding.FindingType.DATA_INSIGHT,
                0.5,
                "researcher@example.com"
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(finding.getRecordedAt());
        assertTrue(finding.getRecordedAt().isBefore(after) || finding.getRecordedAt().isEqual(after));
        assertTrue(finding.getRecordedAt().isAfter(before) || finding.getRecordedAt().isEqual(before));
    }

    @Test
    @DisplayName("Should verify equals based on findingId")
    void shouldVerifyEquals() {
        ResearchFinding finding1 = new ResearchFinding(
                "find-001",
                "proj-001",
                "Title 1",
                "Description 1",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.9,
                "researcher1@example.com"
        );

        ResearchFinding finding2 = new ResearchFinding(
                "find-001",
                "proj-002",
                "Title 2",
                "Description 2",
                ResearchFinding.FindingType.DATA_INSIGHT,
                0.5,
                "researcher2@example.com"
        );

        assertEquals(finding1, finding2);
        assertEquals(finding1.hashCode(), finding2.hashCode());
    }

    @Test
    @DisplayName("Should not equal different findings")
    void shouldNotEqualDifferentFindings() {
        ResearchFinding finding1 = new ResearchFinding(
                "find-001",
                "proj-001",
                "Title",
                "Description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.9,
                "researcher@example.com"
        );

        ResearchFinding finding2 = new ResearchFinding(
                "find-002",
                "proj-001",
                "Title",
                "Description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.9,
                "researcher@example.com"
        );

        assertNotEquals(finding1, finding2);
    }

    @Test
    @DisplayName("Should verify toString contains key fields")
    void shouldVerifyToString() {
        ResearchFinding finding = new ResearchFinding(
                "find-001",
                "proj-001",
                "Important Discovery",
                "Description",
                ResearchFinding.FindingType.NOVEL_DISCOVERY,
                0.95,
                "researcher@example.com"
        );

        String result = finding.toString();

        assertTrue(result.contains("find-001"));
        assertTrue(result.contains("proj-001"));
        assertTrue(result.contains("Important Discovery"));
        assertTrue(result.contains("NOVEL_DISCOVERY"));
        assertTrue(result.contains("0.95"));
    }

    @Test
    @DisplayName("Should create finding with all types")
    void shouldCreateWithAllTypes() {
        ResearchFinding[] findings = {
                new ResearchFinding("f1", "p1", "T", "D", ResearchFinding.FindingType.HYPOTHESIS_CONFIRMED, 0.5, "r"),
                new ResearchFinding("f2", "p2", "T", "D", ResearchFinding.FindingType.HYPOTHESIS_REJECTED, 0.5, "r"),
                new ResearchFinding("f3", "p3", "T", "D", ResearchFinding.FindingType.NOVEL_DISCOVERY, 0.5, "r"),
                new ResearchFinding("f4", "p4", "T", "D", ResearchFinding.FindingType.METHOD_IMPROVEMENT, 0.5, "r"),
                new ResearchFinding("f5", "p5", "T", "D", ResearchFinding.FindingType.DATA_INSIGHT, 0.5, "r"),
                new ResearchFinding("f6", "p6", "T", "D", ResearchFinding.FindingType.STATISTICAL_CORRELATION, 0.5, "r"),
                new ResearchFinding("f7", "p7", "T", "D", ResearchFinding.FindingType.PATTERN_RECOGNITION, 0.5, "r"),
                new ResearchFinding("f8", "p8", "T", "D", ResearchFinding.FindingType.ANOMALY_DETECTED, 0.5, "r")
        };

        assertEquals(ResearchFinding.FindingType.HYPOTHESIS_CONFIRMED, findings[0].getType());
        assertEquals(ResearchFinding.FindingType.ANOMALY_DETECTED, findings[7].getType());
    }
}
