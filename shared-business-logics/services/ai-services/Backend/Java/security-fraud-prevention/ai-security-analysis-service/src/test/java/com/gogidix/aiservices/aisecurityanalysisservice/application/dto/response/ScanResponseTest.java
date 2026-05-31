package com.gogidix.aiservices.aisecurityanalysisservice.application.dto.response;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanStatus;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanType;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.SeverityLevel;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.Vulnerability;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.VulnerabilityScan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ScanResponse DTO Tests")
class ScanResponseTest {

    @Nested
    @DisplayName("fromDomain() - Factory Method Tests")
    class FromDomainTests {

        @Test
        @DisplayName("Should convert VulnerabilityScan to ScanResponse")
        void shouldConvertFromDomain() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.RUNNING)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(Instant.parse("2024-03-07T10:00:00Z"))
                    .estimatedCompletion(Instant.parse("2024-03-07T10:15:00Z"))
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getScanId()).isEqualTo("scan-123");
            assertThat(response.getTarget()).isEqualTo("https://example.com");
            assertThat(response.getScanType()).isEqualTo(ScanType.QUICK);
            assertThat(response.getStatus()).isEqualTo(ScanStatus.RUNNING);
            assertThat(response.getRiskScore()).isZero();
            assertThat(response.getStartTime()).isEqualTo(Instant.parse("2024-03-07T10:00:00Z"));
            assertThat(response.getEstimatedCompletion()).isEqualTo(Instant.parse("2024-03-07T10:15:00Z"));
        }

        @Test
        @DisplayName("Should convert completed scan with vulnerabilities")
        void shouldConvertCompletedScanWithVulnerabilities() {
            Vulnerability vulnerability = Vulnerability.builder()
                    .id("VULN-001")
                    .name("SQL Injection")
                    .description("SQL injection vulnerability")
                    .severity(SeverityLevel.HIGH)
                    .affectedComponent("/api/users")
                    .recommendation("Use parameterized queries")
                    .build();

            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-456")
                    .target("https://example.com")
                    .scanType(ScanType.FULL)
                    .status(ScanStatus.COMPLETED)
                    .riskScore(85)
                    .vulnerabilities(List.of(vulnerability))
                    .startTime(Instant.now().minusSeconds(3600))
                    .estimatedCompletion(Instant.now().minusSeconds(1800))
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getStatus()).isEqualTo(ScanStatus.COMPLETED);
            assertThat(response.getRiskScore()).isEqualTo(85);
            assertThat(response.getVulnerabilities()).hasSize(1);
            assertThat(response.getVulnerabilities().get(0).getName()).isEqualTo("SQL Injection");
        }

        @Test
        @DisplayName("Should convert scan with multiple vulnerabilities")
        void shouldConvertScanWithMultipleVulnerabilities() {
            Vulnerability vuln1 = Vulnerability.builder()
                    .id("VULN-001")
                    .name("XSS")
                    .severity(SeverityLevel.HIGH)
                    .build();

            Vulnerability vuln2 = Vulnerability.builder()
                    .id("VULN-002")
                    .name("CSRF")
                    .severity(SeverityLevel.MEDIUM)
                    .build();

            Vulnerability vuln3 = Vulnerability.builder()
                    .id("VULN-003")
                    .name("Info Disclosure")
                    .severity(SeverityLevel.LOW)
                    .build();

            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-789")
                    .target("https://example.com")
                    .scanType(ScanType.FULL)
                    .status(ScanStatus.COMPLETED)
                    .riskScore(70)
                    .vulnerabilities(List.of(vuln1, vuln2, vuln3))
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now())
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getVulnerabilities()).hasSize(3);
        }

        @Test
        @DisplayName("Should convert failed scan")
        void shouldConvertFailedScan() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-failed")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.FAILED)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(Instant.now().minusSeconds(60))
                    .estimatedCompletion(Instant.now().minusSeconds(30))
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getStatus()).isEqualTo(ScanStatus.FAILED);
        }

        @Test
        @DisplayName("Should convert pending scan")
        void shouldConvertPendingScan() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-pending")
                    .target("https://example.com")
                    .scanType(ScanType.CUSTOM)
                    .status(ScanStatus.PENDING)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now().plusSeconds(1800))
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getStatus()).isEqualTo(ScanStatus.PENDING);
            assertThat(response.getScanType()).isEqualTo(ScanType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Getter/Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set scanId")
        void shouldGetSetScanId() {
            ScanResponse response = new ScanResponse();
            response.setScanId("scan-xyz");

            assertThat(response.getScanId()).isEqualTo("scan-xyz");
        }

        @Test
        @DisplayName("Should get and set target")
        void shouldGetSetTarget() {
            ScanResponse response = new ScanResponse();
            response.setTarget("https://updated.com");

            assertThat(response.getTarget()).isEqualTo("https://updated.com");
        }

        @Test
        @DisplayName("Should get and set scanType")
        void shouldGetSetScanType() {
            ScanResponse response = new ScanResponse();
            response.setScanType(ScanType.FULL);

            assertThat(response.getScanType()).isEqualTo(ScanType.FULL);
        }

        @Test
        @DisplayName("Should get and set status")
        void shouldGetSetStatus() {
            ScanResponse response = new ScanResponse();
            response.setStatus(ScanStatus.COMPLETED);

            assertThat(response.getStatus()).isEqualTo(ScanStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should get and set riskScore")
        void shouldGetSetRiskScore() {
            ScanResponse response = new ScanResponse();
            response.setRiskScore(95);

            assertThat(response.getRiskScore()).isEqualTo(95);
        }

        @Test
        @DisplayName("Should get and set vulnerabilities")
        void shouldGetSetVulnerabilities() {
            Vulnerability vuln = Vulnerability.builder().id("VULN-001").build();
            ScanResponse response = new ScanResponse();
            response.setVulnerabilities(List.of(vuln));

            assertThat(response.getVulnerabilities()).hasSize(1);
        }

        @Test
        @DisplayName("Should get and set startTime")
        void shouldGetSetStartTime() {
            Instant time = Instant.parse("2024-03-07T08:00:00Z");
            ScanResponse response = new ScanResponse();
            response.setStartTime(time);

            assertThat(response.getStartTime()).isEqualTo(time);
        }

        @Test
        @DisplayName("Should get and set estimatedCompletion")
        void shouldGetSetEstimatedCompletion() {
            Instant time = Instant.parse("2024-03-07T10:00:00Z");
            ScanResponse response = new ScanResponse();
            response.setEstimatedCompletion(time);

            assertThat(response.getEstimatedCompletion()).isEqualTo(time);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null vulnerabilities list")
        void shouldHandleNullVulnerabilities() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.RUNNING)
                    .riskScore(0)
                    .vulnerabilities(null)
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now())
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            // Null list is passed through as-is
            assertThat(response.getVulnerabilities()).isNull();
        }

        @Test
        @DisplayName("Should handle empty vulnerabilities list")
        void shouldHandleEmptyVulnerabilities() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.COMPLETED)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now())
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getVulnerabilities()).isEmpty();
        }

        @Test
        @DisplayName("Should handle maximum risk score")
        void shouldHandleMaxRiskScore() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.FULL)
                    .status(ScanStatus.COMPLETED)
                    .riskScore(100)
                    .vulnerabilities(List.of())
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now())
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getRiskScore()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should handle null timestamps")
        void shouldHandleNullTimestamps() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.PENDING)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(null)
                    .estimatedCompletion(null)
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getStartTime()).isNull();
            assertThat(response.getEstimatedCompletion()).isNull();
        }

        @Test
        @DisplayName("Should handle very long target URL")
        void shouldHandleLongTargetUrl() {
            String longUrl = "https://example.com/" + "path".repeat(100);

            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target(longUrl)
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.RUNNING)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now())
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getTarget()).hasSizeGreaterThan(500);
        }
    }

    @Nested
    @DisplayName("All ScanType Values Tests")
    class ScanTypeValuesTests {

        @Test
        @DisplayName("Should convert QUICK scan type")
        void shouldConvertQuickScanType() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.RUNNING)
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getScanType()).isEqualTo(ScanType.QUICK);
        }

        @Test
        @DisplayName("Should convert FULL scan type")
        void shouldConvertFullScanType() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.FULL)
                    .status(ScanStatus.RUNNING)
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getScanType()).isEqualTo(ScanType.FULL);
        }

        @Test
        @DisplayName("Should convert CUSTOM scan type")
        void shouldConvertCustomScanType() {
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("scan-123")
                    .target("https://example.com")
                    .scanType(ScanType.CUSTOM)
                    .status(ScanStatus.RUNNING)
                    .build();

            ScanResponse response = ScanResponse.fromDomain(scan);

            assertThat(response.getScanType()).isEqualTo(ScanType.CUSTOM);
        }
    }

    @Nested
    @DisplayName("Lombok @Data Annotation Tests")
    class DataAnnotationTests {

        @Test
        @DisplayName("Should have toString method")
        void shouldHaveToString() {
            ScanResponse response = new ScanResponse();
            response.setScanId("scan-123");
            response.setTarget("https://example.com");

            String toString = response.toString();
            assertThat(toString).isNotNull();
            assertThat(toString).contains("scan-123");
        }

        @Test
        @DisplayName("Should create new instance via builder pattern (if applicable)")
        void shouldSupportDomainConversion() {
            // Test that the factory method creates a new instance
            VulnerabilityScan scan = VulnerabilityScan.builder()
                    .scanId("original")
                    .target("https://example.com")
                    .scanType(ScanType.QUICK)
                    .status(ScanStatus.RUNNING)
                    .riskScore(0)
                    .vulnerabilities(List.of())
                    .startTime(Instant.now())
                    .estimatedCompletion(Instant.now())
                    .build();

            ScanResponse response1 = ScanResponse.fromDomain(scan);
            ScanResponse response2 = ScanResponse.fromDomain(scan);

            // Each call should create a new instance
            assertThat(response1).isNotSameAs(response2);
            assertThat(response1.getScanId()).isEqualTo(response2.getScanId());
        }
    }
}
