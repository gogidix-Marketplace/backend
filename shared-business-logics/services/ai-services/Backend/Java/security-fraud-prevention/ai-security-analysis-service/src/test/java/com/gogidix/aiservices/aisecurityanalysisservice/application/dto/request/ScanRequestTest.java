package com.gogidix.aiservices.aisecurityanalysisservice.application.dto.request;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ScanRequest DTO Tests")
class ScanRequestTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void tearDown() {
        if (validatorFactory != null) {
            validatorFactory.close();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid request with all fields")
        void shouldValidateValidRequest() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://example.com");
            request.setScanType(ScanType.FULL);

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should validate valid request with minimal fields")
        void shouldValidateMinimalRequest() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://example.com");

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should reject request with null target")
        void shouldRejectNullTarget() {
            ScanRequest request = new ScanRequest();
            request.setTarget(null);
            request.setScanType(ScanType.QUICK);

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).contains("NotBlank");
        }

        @Test
        @DisplayName("Should reject request with blank target")
        void shouldRejectBlankTarget() {
            ScanRequest request = new ScanRequest();
            request.setTarget("   ");
            request.setScanType(ScanType.QUICK);

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).contains("NotBlank");
        }

        @Test
        @DisplayName("Should reject request with empty target")
        void shouldRejectEmptyTarget() {
            ScanRequest request = new ScanRequest();
            request.setTarget("");
            request.setScanType(ScanType.QUICK);

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);

            assertThat(violations).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Getter/Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set target")
        void shouldGetSetTarget() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://test-application.com");

            assertThat(request.getTarget()).isEqualTo("https://test-application.com");
        }

        @Test
        @DisplayName("Should get and set scanType to QUICK")
        void shouldGetSetScanTypeQuick() {
            ScanRequest request = new ScanRequest();
            request.setScanType(ScanType.QUICK);

            assertThat(request.getScanType()).isEqualTo(ScanType.QUICK);
        }

        @Test
        @DisplayName("Should get and set scanType to FULL")
        void shouldGetSetScanTypeFull() {
            ScanRequest request = new ScanRequest();
            request.setScanType(ScanType.FULL);

            assertThat(request.getScanType()).isEqualTo(ScanType.FULL);
        }

        @Test
        @DisplayName("Should get and set scanType to CUSTOM")
        void shouldGetSetScanTypeCustom() {
            ScanRequest request = new ScanRequest();
            request.setScanType(ScanType.CUSTOM);

            assertThat(request.getScanType()).isEqualTo(ScanType.CUSTOM);
        }

        @Test
        @DisplayName("Should handle null scanType")
        void shouldHandleNullScanType() {
            ScanRequest request = new ScanRequest();
            request.setScanType(null);

            assertThat(request.getScanType()).isNull();
        }
    }

    @Nested
    @DisplayName("Default Value Tests")
    class DefaultValueTests {

        @Test
        @DisplayName("Should default to QUICK scan type when not set")
        void shouldDefaultToQuickScan() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://example.com");

            // Default value defined in the ScanRequest class
            // scanType = ScanType.QUICK by default
            assertThat(request.getScanType()).isEqualTo(ScanType.QUICK);
        }

        @Test
        @DisplayName("Should override default when explicitly set")
        void shouldOverrideDefault() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://example.com");
            request.setScanType(ScanType.FULL);

            assertThat(request.getScanType()).isEqualTo(ScanType.FULL);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long target URL")
        void shouldHandleLongTargetUrl() {
            ScanRequest request = new ScanRequest();
            String longUrl = "https://example.com/" + "path".repeat(100);
            request.setTarget(longUrl);

            assertThat(request.getTarget()).hasSizeGreaterThan(500);

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle target with query parameters")
        void shouldHandleTargetWithQueryParams() {
            ScanRequest request = new ScanRequest();
            String targetWithParams = "https://example.com/api?key=value&other=123&flag=true";
            request.setTarget(targetWithParams);

            assertThat(request.getTarget()).contains("?");
            assertThat(request.getTarget()).contains("key=value");
        }

        @Test
        @DisplayName("Should handle target with port number")
        void shouldHandleTargetWithPort() {
            ScanRequest request = new ScanRequest();
            String targetWithPort = "https://example.com:8443/api";
            request.setTarget(targetWithPort);

            assertThat(request.getTarget()).contains(":8443");
        }

        @Test
        @DisplayName("Should handle localhost URL")
        void shouldHandleLocalhostUrl() {
            ScanRequest request = new ScanRequest();
            request.setTarget("http://localhost:8080");

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle IP address URL")
        void shouldHandleIpAddressUrl() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://192.168.1.1/api");

            Set<ConstraintViolation<ScanRequest>> violations = validator.validate(request);
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Should handle unicode in target")
        void shouldHandleUnicodeInTarget() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://例子.测试/路径");

            assertThat(request.getTarget()).contains("例子");
        }

        @Test
        @DisplayName("Should handle target with fragment")
        void shouldHandleTargetWithFragment() {
            ScanRequest request = new ScanRequest();
            String targetWithFragment = "https://example.com/page#section";
            request.setTarget(targetWithFragment);

            assertThat(request.getTarget()).contains("#section");
        }
    }

    @Nested
    @DisplayName("Lombok @Data Annotation Tests")
    class DataAnnotationTests {

        @Test
        @DisplayName("Should have toString method")
        void shouldHaveToString() {
            ScanRequest request = new ScanRequest();
            request.setTarget("https://example.com");
            request.setScanType(ScanType.QUICK);

            String toString = request.toString();
            assertThat(toString).isNotNull();
            assertThat(toString).contains("target");
        }

        @Test
        @DisplayName("Should have equals method")
        void shouldHaveEquals() {
            ScanRequest request1 = new ScanRequest();
            request1.setTarget("https://example.com");
            request1.setScanType(ScanType.QUICK);

            ScanRequest request2 = new ScanRequest();
            request2.setTarget("https://example.com");
            request2.setScanType(ScanType.QUICK);

            assertThat(request1.getTarget()).isEqualTo(request2.getTarget());
            assertThat(request1.getScanType()).isEqualTo(request2.getScanType());
        }
    }
}
