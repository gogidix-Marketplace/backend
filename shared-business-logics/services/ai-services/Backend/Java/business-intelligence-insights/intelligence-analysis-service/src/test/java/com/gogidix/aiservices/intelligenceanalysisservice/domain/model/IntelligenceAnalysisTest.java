package com.gogidix.aiservices.intelligenceanalysisservice.domain.model;

import com.gogidix.aiservices.intelligenceanalysisservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for IntelligenceAnalysis domain model.
 */
@DisplayName("IntelligenceAnalysis Domain Model Tests")
class IntelligenceAnalysisTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String SEGMENT_NAME = "High Value IntelligenceReports";
    private static final String DESCRIPTION = "IntelligenceReports with lifetime value > $10,000";

    private AnalysisCriteria criteria;

    @BeforeEach
    void setUp() {
        criteria = AnalysisCriteria.builder()
                .type(AnalysisCriteria.CriteriaType.CUSTOM)
                .operator(AnalysisCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(10000)
                .build();
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create segment with valid parameters")
        void shouldCreateAnalysisWithValidParameters() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            assertNotNull(segment.getId());
            assertEquals(TENANT_ID, segment.getTenantId());
            assertEquals(SEGMENT_NAME, segment.getName());
            assertEquals(AnalysisStatus.DRAFT, segment.getStatus());
            assertEquals(AnalysisType.CUSTOM, segment.getAnalysisType());
            assertEquals(0L, segment.getIntelligenceReportCount());
            assertNotNull(segment.getCreatedAt());
            assertNotNull(segment.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new IntelligenceAnalysis(null, SEGMENT_NAME, criteria));
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new IntelligenceAnalysis(TENANT_ID, null, criteria));
        }

        @Test
        @DisplayName("Should throw exception when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, null));
        }
    }

    @Nested
    @DisplayName("Activation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate segment when in DRAFT status")
        void shouldActivateAnalysisWhenDraft() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            segment.activate();

            assertEquals(AnalysisStatus.ACTIVE, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when activating segment that is already active")
        void shouldThrowWhenActivatingActiveAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();

            assertThrows(IllegalStateException.class, segment::activate);
        }

        @Test
        @DisplayName("Should throw when activating segment without criteria")
        void shouldThrowWhenActivatingWithoutCriteria() {
            // Create a segment with valid criteria first
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

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
        void shouldDeactivateActiveAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();

            segment.deactivate();

            assertEquals(AnalysisStatus.INACTIVE, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when deactivating draft segment")
        void shouldThrowWhenDeactivatingDraftAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            assertThrows(IllegalStateException.class, segment::deactivate);
        }
    }

    @Nested
    @DisplayName("Archive Tests")
    class ArchiveTests {

        @Test
        @DisplayName("Should archive segment")
        void shouldArchiveAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            segment.archive();

            assertEquals(AnalysisStatus.ARCHIVED, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when archiving already archived segment")
        void shouldThrowWhenArchivingArchivedAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.archive();

            assertThrows(IllegalStateException.class, segment::archive);
        }
    }

    @Nested
    @DisplayName("IntelligenceReport Management Tests")
    class IntelligenceReportManagementTests {

        @Test
        @DisplayName("Should add customers to active segment")
        void shouldAddIntelligenceReportsToActiveAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-003");

            segment.addIntelligenceReports(customerIds);

            assertEquals(3, segment.getIntelligenceReportCount());
            assertEquals(3, segment.getIntelligenceReportIds().size());
        }

        @Test
        @DisplayName("Should not add duplicate customers")
        void shouldNotAddDuplicateIntelligenceReports() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-001");

            segment.addIntelligenceReports(customerIds);

            assertEquals(2, segment.getIntelligenceReportCount());
        }

        @Test
        @DisplayName("Should throw when adding customers to draft segment")
        void shouldThrowWhenAddingIntelligenceReportsToDraftAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            List<String> customerIds = List.of("cust-001");

            assertThrows(IllegalStateException.class, () -> segment.addIntelligenceReports(customerIds));
        }

        @Test
        @DisplayName("Should remove customers from segment")
        void shouldRemoveIntelligenceReportsFromAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-003");
            segment.addIntelligenceReports(customerIds);

            segment.removeIntelligenceReports(List.of("cust-002"));

            assertEquals(2, segment.getIntelligenceReportCount());
            assertFalse(segment.getIntelligenceReportIds().contains("cust-002"));
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update segment details")
        void shouldUpdateAnalysisDetails() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            segment.updateDetails("Updated Name", "Updated Description");

            assertEquals("Updated Name", segment.getName());
            assertEquals("Updated Description", segment.getDescription());
        }

        @Test
        @DisplayName("Should throw when updating archived segment")
        void shouldThrowWhenUpdatingArchivedAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.archive();

            assertThrows(IllegalStateException.class,
                    () -> segment.updateDetails("New Name", "New Description"));
        }

        @Test
        @DisplayName("Should update criteria when in draft status")
        void shouldUpdateCriteriaWhenDraft() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            AnalysisCriteria newCriteria = AnalysisCriteria.builder()
                    .type(AnalysisCriteria.CriteriaType.CUSTOM)
                    .operator(AnalysisCriteria.CriteriaOperator.GREATER_THAN_OR_EQUAL)
                    .field("purchaseCount")
                    .value(5)
                    .build();

            segment.updateCriteria(newCriteria);

            assertEquals(newCriteria, segment.getCriteria());
        }

        @Test
        @DisplayName("Should throw when updating criteria of active segment")
        void shouldThrowWhenUpdatingCriteriaOfActiveAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            AnalysisCriteria newCriteria = AnalysisCriteria.builder()
                    .type(AnalysisCriteria.CriteriaType.CUSTOM)
                    .operator(AnalysisCriteria.CriteriaOperator.GREATER_THAN_OR_EQUAL)
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
        void shouldValidateValidAnalysis() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            assertDoesNotThrow(segment::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis("  ", SEGMENT_NAME, criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when name is blank")
        void shouldThrowWhenNameIsBlank() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, "  ", criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when name exceeds 100 characters")
        void shouldThrowWhenNameExceedsMaxLength() {
            String longName = "a".repeat(101);
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, longName, criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

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
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            assertFalse(segment.isActive());

            segment.activate();

            assertTrue(segment.isActive());
        }

        @Test
        @DisplayName("Should return correct modifiable status")
        void shouldReturnCorrectModifiableStatus() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            assertTrue(segment.isModifiable());

            segment.archive();

            assertFalse(segment.isModifiable());
        }

        @Test
        @DisplayName("Should mark as analyzed")
        void shouldMarkAsAnalyzed() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            segment.markAsAnalyzed();

            assertNotNull(segment.getLastAnalyzedAt());
        }

        @Test
        @DisplayName("Should set customer count")
        void shouldSetIntelligenceReportCount() {
            IntelligenceAnalysis segment = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            segment.setIntelligenceReportCount(500L);

            assertEquals(500L, segment.getIntelligenceReportCount());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build segment using builder")
        void shouldBuildAnalysisUsingBuilder() {
            IntelligenceAnalysis segment = IntelligenceAnalysis.Builder.builder()
                    .tenantId(TENANT_ID)
                    .name(SEGMENT_NAME)
                    .description(DESCRIPTION)
                    .criteria(criteria)
                    .segmentType(AnalysisType.BEHAVIORAL)
                    .status(AnalysisStatus.ACTIVE)
                    .customerCount(100L)
                    .build();

            assertEquals(TENANT_ID, segment.getTenantId());
            assertEquals(SEGMENT_NAME, segment.getName());
            assertEquals(DESCRIPTION, segment.getDescription());
            assertEquals(AnalysisType.BEHAVIORAL, segment.getAnalysisType());
            assertEquals(AnalysisStatus.ACTIVE, segment.getStatus());
            assertEquals(100L, segment.getIntelligenceReportCount());
        }

        @Test
        @DisplayName("Should throw when building without tenantId")
        void shouldThrowWhenBuildingWithoutTenantId() {
            assertThrows(IllegalArgumentException.class,
                    () -> IntelligenceAnalysis.Builder.builder()
                            .name(SEGMENT_NAME)
                            .criteria(criteria)
                            .build());
        }

        @Test
        @DisplayName("Should throw when building without name")
        void shouldThrowWhenBuildingWithoutName() {
            assertThrows(IllegalArgumentException.class,
                    () -> IntelligenceAnalysis.Builder.builder()
                            .tenantId(TENANT_ID)
                            .criteria(criteria)
                            .build());
        }

        @Test
        @DisplayName("Should throw when building without criteria")
        void shouldThrowWhenBuildingWithoutCriteria() {
            assertThrows(IllegalArgumentException.class,
                    () -> IntelligenceAnalysis.Builder.builder()
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
            IntelligenceAnalysis segment1 = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            IntelligenceAnalysis segment2 = IntelligenceAnalysis.Builder.builder()
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
            IntelligenceAnalysis segment1 = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);
            IntelligenceAnalysis segment2 = new IntelligenceAnalysis(TENANT_ID, SEGMENT_NAME, criteria);

            assertNotEquals(segment1, segment2);
        }
    }
}
