package com.gogidix.aiservices.aiuserprofilingservice.application.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AddUsersToProfileCommand.
 */
@DisplayName("AddUsersToProfileCommand Tests")
class AddUsersToProfileCommandTest {

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
            AddUsersToProfileCommand command = new AddUsersToProfileCommand(
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
            AddUsersToProfileCommand command = new AddUsersToProfileCommand(
                    SEGMENT_ID, TENANT_ID, null, CUSTOMER_IDS
            );

            assertNull(command.userId());
        }

        @Test
        @DisplayName("Should throw when segmentId is null")
        void shouldThrowWhenProfileIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand(null, TENANT_ID, USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when segmentId is blank")
        void shouldThrowWhenProfileIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand("   ", TENANT_ID, USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand(SEGMENT_ID, null, USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand(SEGMENT_ID, "   ", USER_ID, CUSTOMER_IDS));
        }

        @Test
        @DisplayName("Should throw when customerIds is null")
        void shouldThrowWhenUserIdsIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand(SEGMENT_ID, TENANT_ID, USER_ID, null));
        }

        @Test
        @DisplayName("Should throw when customerIds is empty")
        void shouldThrowWhenUserIdsIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand(SEGMENT_ID, TENANT_ID, USER_ID, List.of()));
        }

        @Test
        @DisplayName("Should throw when customerIds exceeds max size")
        void shouldThrowWhenUserIdsExceedsMaxSize() {
            List<String> tooManyUsers = java.util.stream.IntStream
                    .range(0, 1001)
                    .mapToObj(i -> "cust-" + i)
                    .toList();

            assertThrows(IllegalArgumentException.class,
                    () -> new AddUsersToProfileCommand(SEGMENT_ID, TENANT_ID, USER_ID, tooManyUsers));
        }
    }

    @Nested
    @DisplayName("Boundary Tests")
    class BoundaryTests {

        @Test
        @DisplayName("Should accept exactly 1000 customers")
        void shouldAcceptExactly1000Users() {
            List<String> thousandUsers = java.util.stream.IntStream
                    .range(0, 1000)
                    .mapToObj(i -> "cust-" + i)
                    .toList();

            assertDoesNotThrow(() -> new AddUsersToProfileCommand(
                    SEGMENT_ID, TENANT_ID, USER_ID, thousandUsers));
        }

        @Test
        @DisplayName("Should accept single customer")
        void shouldAcceptSingleUser() {
            AddUsersToProfileCommand command = new AddUsersToProfileCommand(
                    SEGMENT_ID, TENANT_ID, USER_ID, List.of("cust-001")
            );

            assertEquals(1, command.customerIds().size());
        }
    }
}
