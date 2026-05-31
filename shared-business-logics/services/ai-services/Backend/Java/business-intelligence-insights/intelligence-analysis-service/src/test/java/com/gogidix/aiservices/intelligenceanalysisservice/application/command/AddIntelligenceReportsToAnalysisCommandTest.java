package com.gogidix.aiservices.intelligenceanalysisservice.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AddIntelligenceReportsToAnalysisCommand.
 */
@DisplayName("AddIntelligenceReportsToAnalysisCommand Tests")
class AddIntelligenceReportsToAnalysisCommandTest {

    private static final String SEGMENT_ID = "seg-123";
    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-123";
    private static final List<String> CUSTOMER_IDS = List.of("cust-001", "cust-002", "cust-003");

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create command with valid parameters")
        void shouldCreateWithValidParameters() {
            AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                    SEGMENT_ID, TENANT_ID, USER_ID, CUSTOMER_IDS
            );

            assertEquals(SEGMENT_ID, command.segmentId());
            assertEquals(TENANT_ID, command.tenantId());
            assertEquals(USER_ID, command.userId());
            assertEquals(CUSTOMER_IDS, command.customerIds());
        }

        @Test
        @DisplayName("Should accept null userId")
        void shouldAcceptNullUserId() {
            AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                    SEGMENT_ID, TENANT_ID, null, CUSTOMER_IDS
            );

            assertNull(command.userId());
        }

        @Test
        @DisplayName("Should throw when segmentId is null")
        void shouldThrowWhenAnalysisIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand(null, TENANT_ID, USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when segmentId is blank")
        void shouldThrowWhenAnalysisIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand("   ", TENANT_ID, USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand(SEGMENT_ID, null, USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand(SEGMENT_ID, "   ", USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when customerIds is null")
        void shouldThrowWhenIntelligenceReportIdsIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand(SEGMENT_ID, TENANT_ID, USER_ID, null));
        }

        @Test
        @DisplayName("Should throw when customerIds is empty")
        void shouldThrowWhenIntelligenceReportIdsIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand(SEGMENT_ID, TENANT_ID, USER_ID, List.of()));
        }

        @Test
        @DisplayName("Should throw when customerIds exceeds max size")
        void shouldThrowWhenIntelligenceReportIdsExceedsMaxSize() {
            List<String> tooManyIntelligenceReports = java.util.stream.IntStream
                    .range(0, 1001)
                    .mapToObj(i -> "cust-" + i)
                    .toList();

            assertThrows(IllegalArgumentException.class,
                    () -> new AddIntelligenceReportsToAnalysisCommand(SEGMENT_ID, TENANT_ID, USER_ID, tooManyIntelligenceReports));
        }
    }

    @Nested
    @DisplayName("Boundary Tests")
    class BoundaryTests {

        @Test
        @DisplayName("Should accept exactly 1000 customers")
        void shouldAcceptExactly1000IntelligenceReports() {
            List<String> thousandIntelligenceReports = java.util.stream.IntStream
                    .range(0, 1000)
                    .mapToObj(i -> "cust-" + i)
                    .toList();

            assertDoesNotThrow(() -> new AddIntelligenceReportsToAnalysisCommand(
                    SEGMENT_ID, TENANT_ID, USER_ID, thousandIntelligenceReports));
        }

        @Test
        @DisplayName("Should accept single customer")
        void shouldAcceptSingleIntelligenceReport() {
            AddIntelligenceReportsToAnalysisCommand command = new AddIntelligenceReportsToAnalysisCommand(
                    SEGMENT_ID, TENANT_ID, USER_ID, List.of("cust-001")
            );

            assertEquals(1, command.customerIds().size());
        }
    }
}
