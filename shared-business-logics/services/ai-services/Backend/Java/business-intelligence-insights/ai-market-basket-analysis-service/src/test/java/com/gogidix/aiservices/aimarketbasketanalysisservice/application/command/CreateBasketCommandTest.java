package com.gogidix.aiservices.aimarketbasketanalysisservice.application.command;

import com.gogidix.aiservices.aimarketbasketanalysisservice.domain.model.BasketType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CreateBasketCommand.
 */
@DisplayName("CreateBasketCommand Tests")
class CreateBasketCommandTest {

    private static final String NAME = "High Value Customers";
    private static final String DESCRIPTION = "Customers with high lifetime value";
    private static final BasketType SEGMENT_TYPE = BasketType.BEHAVIORAL;
    private static final Map<String, Object> CRITERIA = Map.of("minLifetimeValue", 10000);
    private static final String TENANT_ID = "tenant-123";
    private static final String USER_ID = "user-123";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create command with all valid parameters")
        void shouldCreateWithAllValidParameters() {
            CreateBasketCommand command = new CreateBasketCommand(
                    NAME, DESCRIPTION, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID
            );

            assertEquals(NAME, command.name());
            assertEquals(DESCRIPTION, command.description());
            assertEquals(SEGMENT_TYPE, command.segmentType());
            assertEquals(CRITERIA, command.criteria());
            assertEquals(TENANT_ID, command.tenantId());
            assertEquals(USER_ID, command.userId());
        }

        @Test
        @DisplayName("Should accept null description")
        void shouldAcceptNullDescription() {
            CreateBasketCommand command = new CreateBasketCommand(
                    NAME, null, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID
            );

            assertNull(command.description());
        }

        @Test
        @DisplayName("Should throw when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(null, DESCRIPTION, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when name is blank")
        void shouldThrowWhenNameIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand("   ", DESCRIPTION, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when name exceeds max length")
        void shouldThrowWhenNameExceedsMaxLength() {
            String longName = "a".repeat(101);

            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(longName, DESCRIPTION, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when description exceeds max length")
        void shouldThrowWhenDescriptionExceedsMaxLength() {
            String longDescription = "a".repeat(501);

            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(NAME, longDescription, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when segmentType is null")
        void shouldThrowWhenBasketTypeIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(NAME, DESCRIPTION, null, CRITERIA, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(NAME, DESCRIPTION, SEGMENT_TYPE, null, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when criteria is empty")
        void shouldThrowWhenCriteriaIsEmpty() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(NAME, DESCRIPTION, SEGMENT_TYPE, Map.of(), TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(NAME, DESCRIPTION, SEGMENT_TYPE, CRITERIA, null, USER_ID));
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new CreateBasketCommand(NAME, DESCRIPTION, SEGMENT_TYPE, CRITERIA, "   ", USER_ID));
        }

        @Test
        @DisplayName("Should accept null userId")
        void shouldAcceptNullUserId() {
            CreateBasketCommand command = new CreateBasketCommand(
                    NAME, DESCRIPTION, SEGMENT_TYPE, CRITERIA, TENANT_ID, null
            );

            assertNull(command.userId());
        }
    }

    @Nested
    @DisplayName("Boundary Tests")
    class BoundaryTests {

        @Test
        @DisplayName("Should accept name with exactly 100 characters")
        void shouldAcceptNameWithExactly100Characters() {
            String name100 = "a".repeat(100);

            assertDoesNotThrow(() -> new CreateBasketCommand(
                    name100, DESCRIPTION, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID));
        }

        @Test
        @DisplayName("Should accept description with exactly 500 characters")
        void shouldAcceptDescriptionWithExactly500Characters() {
            String description500 = "a".repeat(500);

            assertDoesNotThrow(() -> new CreateBasketCommand(
                    NAME, description500, SEGMENT_TYPE, CRITERIA, TENANT_ID, USER_ID));
        }
    }
}
