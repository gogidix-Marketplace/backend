package com.gogidix.aiservices.aisalesforecastingservice.domain.model;

import com.gogidix.aiservices.aisalesforecastingservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SalesForecast domain model.
 */
@DisplayName("SalesForecast Domain Model Tests")
class SalesForecastTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String SEGMENT_NAME = "High Value ForecastModels";
    private static final String DESCRIPTION = "ForecastModels with lifetime value > $10,000";

    private ForecastCriteria criteria;

    @BeforeEach
    void setUp() {
        criteria = ForecastCriteria.builder()
                .type(ForecastCriteria.CriteriaType.CUSTOM)
                .operator(ForecastCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(10000)
                .build();
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create segment with valid parameters")
        void shouldCreateForecastWithValidParameters() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            assertNotNull(segment.getId());
            assertEquals(TENANT_ID, segment.getTenantId());
            assertEquals(SEGMENT_NAME, segment.getName());
            assertEquals(ForecastStatus.DRAFT, segment.getStatus());
            assertEquals(ForecastType.CUSTOM, segment.getForecastType());
            assertEquals(0L, segment.getForecastModelCount());
            assertNotNull(segment.getCreatedAt());
            assertNotNull(segment.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new SalesForecast(null, SEGMENT_NAME, criteria));
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new SalesForecast(TENANT_ID, null, criteria));
        }

        @Test
        @DisplayName("Should throw exception when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new SalesForecast(TENANT_ID, SEGMENT_NAME, null));
        }
    }

    @Nested
    @DisplayName("Activation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate segment when in DRAFT status")
        void shouldActivateForecastWhenDraft() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            segment.activate();

            assertEquals(ForecastStatus.ACTIVE, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when activating segment that is already active")
        void shouldThrowWhenActivatingActiveForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();

            assertThrows(IllegalStateException.class, segment::activate);
        }

        @Test
        @DisplayName("Should throw when activating segment without criteria")
        void shouldThrowWhenActivatingWithoutCriteria() {
            // Create a segment with valid criteria first
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            // Set criteria to null using reflection (simulating deserialization scenario)
            try {
                java.lang.reflect.Field criteriaField = segment.getClass().getDeclaredField("criteria");
                criteriaField.setAccessible(true);
                criteriaField.set(segment, null);
            } catch (Exception e) {
                fail("Failed to set criteria field: " + e.getMessage());
            }

            // Now activate() should throw IllegalStateException
            assertThrows(IllegalStateException.class, segment::activate);
        }
    }

    @Nested
    @DisplayName("Deactivation Tests")
    class DeactivationTests {

        @Test
        @DisplayName("Should deactivate active segment")
        void shouldDeactivateActiveForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();

            segment.deactivate();

            assertEquals(ForecastStatus.INACTIVE, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when deactivating draft segment")
        void shouldThrowWhenDeactivatingDraftForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            assertThrows(IllegalStateException.class, segment::deactivate);
        }
    }

    @Nested
    @DisplayName("Archive Tests")
    class ArchiveTests {

        @Test
        @DisplayName("Should archive segment")
        void shouldArchiveForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            segment.archive();

            assertEquals(ForecastStatus.ARCHIVED, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when archiving already archived segment")
        void shouldThrowWhenArchivingArchivedForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.archive();

            assertThrows(IllegalStateException.class, segment::archive);
        }
    }

    @Nested
    @DisplayName("ForecastModel Management Tests")
    class ForecastModelManagementTests {

        @Test
        @DisplayName("Should add customers to active segment")
        void shouldAddForecastModelsToActiveForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-003");

            segment.addForecastModels(customerIds);

            assertEquals(3, segment.getForecastModelCount());
            assertEquals(3, segment.getForecastModelIds().size());
        }

        @Test
        @DisplayName("Should not add duplicate customers")
        void shouldNotAddDuplicateForecastModels() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-001");

            segment.addForecastModels(customerIds);

            assertEquals(2, segment.getForecastModelCount());
        }

        @Test
        @DisplayName("Should throw when adding customers to draft segment")
        void shouldThrowWhenAddingForecastModelsToDraftForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            List<String> customerIds = List.of("cust-001");

            assertThrows(IllegalStateException.class, () -> segment.addForecastModels(customerIds));
        }

        @Test
        @DisplayName("Should remove customers from segment")
        void shouldRemoveForecastModelsFromForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-003");
            segment.addForecastModels(customerIds);

            segment.removeForecastModels(List.of("cust-002"));

            assertEquals(2, segment.getForecastModelCount());
            assertFalse(segment.getForecastModelIds().contains("cust-002"));
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update segment details")
        void shouldUpdateForecastDetails() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            segment.updateDetails("Updated Name", "Updated Description");

            assertEquals("Updated Name", segment.getName());
            assertEquals("Updated Description", segment.getDescription());
        }

        @Test
        @DisplayName("Should throw when updating archived segment")
        void shouldThrowWhenUpdatingArchivedForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.archive();

            assertThrows(IllegalStateException.class,
                    () -> segment.updateDetails("New Name", "New Description"));
        }

        @Test
        @DisplayName("Should update criteria when in draft status")
        void shouldUpdateCriteriaWhenDraft() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            ForecastCriteria newCriteria = ForecastCriteria.builder()
                    .type(ForecastCriteria.CriteriaType.CUSTOM)
                    .operator(ForecastCriteria.CriteriaOperator.GREATER_THAN_OR_EQUAL)
                    .field("purchaseCount")
                    .value(5)
                    .build();

            segment.updateCriteria(newCriteria);

            assertEquals(newCriteria, segment.getCriteria());
        }

        @Test
        @DisplayName("Should throw when updating criteria of active segment")
        void shouldThrowWhenUpdatingCriteriaOfActiveForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            ForecastCriteria newCriteria = ForecastCriteria.builder()
                    .type(ForecastCriteria.CriteriaType.CUSTOM)
                    .operator(ForecastCriteria.CriteriaOperator.GREATER_THAN_OR_EQUAL)
                    .field("purchaseCount")
                    .value(5)
                    .build();

            assertThrows(IllegalStateException.class,
                    () -> segment.updateCriteria(newCriteria));
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate valid segment")
        void shouldValidateValidForecast() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            assertDoesNotThrow(segment::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            SalesForecast segment = new SalesForecast("  ", SEGMENT_NAME, criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when name is blank")
        void shouldThrowWhenNameIsBlank() {
            SalesForecast segment = new SalesForecast(TENANT_ID, "  ", criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when name exceeds 100 characters")
        void shouldThrowWhenNameExceedsMaxLength() {
            String longName = "a".repeat(101);
            SalesForecast segment = new SalesForecast(TENANT_ID, longName, criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            // updateCriteria throws NullPointerException via Objects.requireNonNull
            assertThrows(NullPointerException.class, () -> segment.updateCriteria(null));
        }
    }

    @Nested
    @DisplayName("Utility Tests")
    class UtilityTests {

        @Test
        @DisplayName("Should return correct active status")
        void shouldReturnCorrectActiveStatus() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            assertFalse(segment.isActive());

            segment.activate();

            assertTrue(segment.isActive());
        }

        @Test
        @DisplayName("Should return correct modifiable status")
        void shouldReturnCorrectModifiableStatus() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            assertTrue(segment.isModifiable());

            segment.archive();

            assertFalse(segment.isModifiable());
        }

        @Test
        @DisplayName("Should mark as analyzed")
        void shouldMarkAsAnalyzed() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            segment.markAsAnalyzed();

            assertNotNull(segment.getLastAnalyzedAt());
        }

        @Test
        @DisplayName("Should set customer count")
        void shouldSetForecastModelCount() {
            SalesForecast segment = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            segment.setForecastModelCount(500L);

            assertEquals(500L, segment.getForecastModelCount());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build segment using builder")
        void shouldBuildForecastUsingBuilder() {
            SalesForecast segment = SalesForecast.Builder.builder()
                    .tenantId(TENANT_ID)
                    .name(SEGMENT_NAME)
                    .description(DESCRIPTION)
                    .criteria(criteria)
                    .segmentType(ForecastType.BEHAVIORAL)
                    .status(ForecastStatus.ACTIVE)
                    .customerCount(100L)
                    .build();

            assertEquals(TENANT_ID, segment.getTenantId());
            assertEquals(SEGMENT_NAME, segment.getName());
            assertEquals(DESCRIPTION, segment.getDescription());
            assertEquals(ForecastType.BEHAVIORAL, segment.getForecastType());
            assertEquals(ForecastStatus.ACTIVE, segment.getStatus());
            assertEquals(100L, segment.getForecastModelCount());
        }

        @Test
        @DisplayName("Should throw when building without tenantId")
        void shouldThrowWhenBuildingWithoutTenantId() {
            assertThrows(IllegalArgumentException.class,
                    () -> SalesForecast.Builder.builder()
                            .name(SEGMENT_NAME)
                            .criteria(criteria)
                            .build());
        }

        @Test
        @DisplayName("Should throw when building without name")
        void shouldThrowWhenBuildingWithoutName() {
            assertThrows(IllegalArgumentException.class,
                    () -> SalesForecast.Builder.builder()
                            .tenantId(TENANT_ID)
                            .criteria(criteria)
                            .build());
        }

        @Test
        @DisplayName("Should throw when building without criteria")
        void shouldThrowWhenBuildingWithoutCriteria() {
            assertThrows(IllegalArgumentException.class,
                    () -> SalesForecast.Builder.builder()
                            .tenantId(TENANT_ID)
                            .name(SEGMENT_NAME)
                            .build());
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when id and tenantId match")
        void shouldBeEqualWhenIdAndTenantIdMatch() {
            SalesForecast segment1 = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            SalesForecast segment2 = SalesForecast.Builder.builder()
                    .id(segment1.getId())
                    .tenantId(TENANT_ID)
                    .name(SEGMENT_NAME)
                    .criteria(criteria)
                    .build();

            assertEquals(segment1, segment2);
            assertEquals(segment1.hashCode(), segment2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when id differs")
        void shouldNotBeEqualWhenIdDiffers() {
            SalesForecast segment1 = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);
            SalesForecast segment2 = new SalesForecast(TENANT_ID, SEGMENT_NAME, criteria);

            assertNotEquals(segment1, segment2);
        }
    }
}
