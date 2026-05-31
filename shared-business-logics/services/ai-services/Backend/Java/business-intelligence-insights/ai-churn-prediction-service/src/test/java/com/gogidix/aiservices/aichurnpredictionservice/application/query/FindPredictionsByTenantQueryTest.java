package com.gogidix.aiservices.aichurnpredictionservice.application.query;

import com.gogidix.aiservices.aichurnpredictionservice.domain.model.PredictionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FindPredictionsByTenantQuery.
 */
@DisplayName("FindPredictionsByTenantQuery Tests")
class FindPredictionsByTenantQueryTest {

    private static final String TENANT_ID = "tenant-123";

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create query with required parameters only")
        void shouldCreateWithRequiredParametersOnly() {
            FindPredictionsByTenantQuery query = FindPredictionsByTenantQuery.create(TENANT_ID);

            assertEquals(TENANT_ID, query.tenantId());
            assertNull(query.segmentType());
            assertNull(query.active());
            assertEquals(0, query.page());
            assertEquals(20, query.size());
            assertEquals("createdAt", query.sortBy());
            assertEquals("DESC", query.sortDirection());
        }

        @Test
        @DisplayName("Should create query with all parameters")
        void shouldCreateWithAllParameters() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, PredictionType.BEHAVIORAL, true, 1, 50, "name", "ASC"
            );

            assertEquals(TENANT_ID, query.tenantId());
            assertEquals(PredictionType.BEHAVIORAL, query.segmentType());
            assertTrue(query.active());
            assertEquals(1, query.page());
            assertEquals(50, query.size());
            assertEquals("name", query.sortBy());
            assertEquals("ASC", query.sortDirection());
        }

        @Test
        @DisplayName("Should apply defaults for null sortBy")
        void shouldApplyDefaultsForNullSortBy() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 20, null, "DESC"
            );

            assertEquals("createdAt", query.sortBy());
        }

        @Test
        @DisplayName("Should apply defaults for null sortDirection")
        void shouldApplyDefaultsForNullSortDirection() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 20, "createdAt", null
            );

            assertEquals("DESC", query.sortDirection());
        }

        @Test
        @DisplayName("Should normalize invalid sortDirection")
        void shouldNormalizeInvalidSortDirection() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 20, "createdAt", "INVALID"
            );

            assertEquals("DESC", query.sortDirection());
        }

        @Test
        @DisplayName("Should accept valid sortDirection values")
        void shouldAcceptValidSortDirectionValues() {
            FindPredictionsByTenantQuery ascQuery = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 20, "createdAt", "ASC"
            );
            FindPredictionsByTenantQuery descQuery = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 20, "createdAt", "DESC"
            );

            assertEquals("ASC", ascQuery.sortDirection());
            assertEquals("DESC", descQuery.sortDirection());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FindPredictionsByTenantQuery(null, null, null, 0, 20, "createdAt", "DESC"));
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FindPredictionsByTenantQuery("   ", null, null, 0, 20, "createdAt", "DESC"));
        }

        @Test
        @DisplayName("Should throw when page is negative")
        void shouldThrowWhenPageIsNegative() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FindPredictionsByTenantQuery(TENANT_ID, null, null, -1, 20, "createdAt", "DESC"));
        }

        @Test
        @DisplayName("Should throw when size is less than 1")
        void shouldThrowWhenSizeIsLessThan1() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FindPredictionsByTenantQuery(TENANT_ID, null, null, 0, 0, "createdAt", "DESC"));
        }

        @Test
        @DisplayName("Should throw when size exceeds maximum")
        void shouldThrowWhenSizeExceedsMaximum() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FindPredictionsByTenantQuery(TENANT_ID, null, null, 0, 101, "createdAt", "DESC"));
        }
    }

    @Nested
    @DisplayName("Boundary Tests")
    class BoundaryTests {

        @Test
        @DisplayName("Should accept page zero")
        void shouldAcceptPageZero() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 20, "createdAt", "DESC"
            );

            assertEquals(0, query.page());
        }

        @Test
        @DisplayName("Should accept minimum size of 1")
        void shouldAcceptMinimumSizeOf1() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 1, "createdAt", "DESC"
            );

            assertEquals(1, query.size());
        }

        @Test
        @DisplayName("Should accept maximum size of 100")
        void shouldAcceptMaximumSizeOf100() {
            FindPredictionsByTenantQuery query = new FindPredictionsByTenantQuery(
                    TENANT_ID, null, null, 0, 100, "createdAt", "DESC"
            );

            assertEquals(100, query.size());
        }
    }
}
