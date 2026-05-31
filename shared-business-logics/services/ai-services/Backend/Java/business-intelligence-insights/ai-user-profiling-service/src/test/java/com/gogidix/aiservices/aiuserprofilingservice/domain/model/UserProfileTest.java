package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

import com.gogidix.aiservices.aiuserprofilingservice.shared.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserProfile domain model.
 */
@DisplayName("UserProfile Domain Model Tests")
class UserProfileTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String SEGMENT_NAME = "High Value Users";
    private static final String DESCRIPTION = "Users with lifetime value > $10,000";

    private ProfileCriteria criteria;

    @BeforeEach
    void setUp() {
        criteria = ProfileCriteria.builder()
                .type(ProfileCriteria.CriteriaType.CUSTOM)
                .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN)
                .field("lifetimeValue")
                .value(10000)
                .build();
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create segment with valid parameters")
        void shouldCreateProfileWithValidParameters() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            assertNotNull(segment.getId());
            assertEquals(TENANT_ID, segment.getTenantId());
            assertEquals(SEGMENT_NAME, segment.getName());
            assertEquals(ProfileStatus.DRAFT, segment.getStatus());
            assertEquals(ProfileType.CUSTOM, segment.getProfileType());
            assertEquals(0L, segment.getUserCount());
            assertNotNull(segment.getCreatedAt());
            assertNotNull(segment.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when tenantId is null")
        void shouldThrowWhenTenantIdIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new UserProfile(null, SEGMENT_NAME, criteria));
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new UserProfile(TENANT_ID, null, criteria));
        }

        @Test
        @DisplayName("Should throw exception when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new UserProfile(TENANT_ID, SEGMENT_NAME, null));
        }
    }

    @Nested
    @DisplayName("Activation Tests")
    class ActivationTests {

        @Test
        @DisplayName("Should activate segment when in DRAFT status")
        void shouldActivateProfileWhenDraft() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            segment.activate();

            assertEquals(ProfileStatus.ACTIVE, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when activating segment that is already active")
        void shouldThrowWhenActivatingActiveProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();

            assertThrows(IllegalStateException.class, segment::activate);
        }

        @Test
        @DisplayName("Should throw when activating segment without criteria")
        void shouldThrowWhenActivatingWithoutCriteria() {
            // Create a segment with valid criteria first
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

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
        void shouldDeactivateActiveProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();

            segment.deactivate();

            assertEquals(ProfileStatus.INACTIVE, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when deactivating draft segment")
        void shouldThrowWhenDeactivatingDraftProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            assertThrows(IllegalStateException.class, segment::deactivate);
        }
    }

    @Nested
    @DisplayName("Archive Tests")
    class ArchiveTests {

        @Test
        @DisplayName("Should archive segment")
        void shouldArchiveProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            segment.archive();

            assertEquals(ProfileStatus.ARCHIVED, segment.getStatus());
        }

        @Test
        @DisplayName("Should throw when archiving already archived segment")
        void shouldThrowWhenArchivingArchivedProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.archive();

            assertThrows(IllegalStateException.class, segment::archive);
        }
    }

    @Nested
    @DisplayName("User Management Tests")
    class UserManagementTests {

        @Test
        @DisplayName("Should add customers to active segment")
        void shouldAddUsersToActiveProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-003");

            segment.addUsers(customerIds);

            assertEquals(3, segment.getUserCount());
            assertEquals(3, segment.getUserIds().size());
        }

        @Test
        @DisplayName("Should not add duplicate customers")
        void shouldNotAddDuplicateUsers() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-001");

            segment.addUsers(customerIds);

            assertEquals(2, segment.getUserCount());
        }

        @Test
        @DisplayName("Should throw when adding customers to draft segment")
        void shouldThrowWhenAddingUsersToDraftProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            List<String> customerIds = List.of("cust-001");

            assertThrows(IllegalStateException.class, () -> segment.addUsers(customerIds));
        }

        @Test
        @DisplayName("Should remove customers from segment")
        void shouldRemoveUsersFromProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            List<String> customerIds = List.of("cust-001", "cust-002", "cust-003");
            segment.addUsers(customerIds);

            segment.removeUsers(List.of("cust-002"));

            assertEquals(2, segment.getUserCount());
            assertFalse(segment.getUserIds().contains("cust-002"));
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update segment details")
        void shouldUpdateProfileDetails() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            segment.updateDetails("Updated Name", "Updated Description");

            assertEquals("Updated Name", segment.getName());
            assertEquals("Updated Description", segment.getDescription());
        }

        @Test
        @DisplayName("Should throw when updating archived segment")
        void shouldThrowWhenUpdatingArchivedProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.archive();

            assertThrows(IllegalStateException.class,
                    () -> segment.updateDetails("New Name", "New Description"));
        }

        @Test
        @DisplayName("Should update criteria when in draft status")
        void shouldUpdateCriteriaWhenDraft() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            ProfileCriteria newCriteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN_OR_EQUAL)
                    .field("purchaseCount")
                    .value(5)
                    .build();

            segment.updateCriteria(newCriteria);

            assertEquals(newCriteria, segment.getCriteria());
        }

        @Test
        @DisplayName("Should throw when updating criteria of active segment")
        void shouldThrowWhenUpdatingCriteriaOfActiveProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            segment.activate();
            ProfileCriteria newCriteria = ProfileCriteria.builder()
                    .type(ProfileCriteria.CriteriaType.CUSTOM)
                    .operator(ProfileCriteria.CriteriaOperator.GREATER_THAN_OR_EQUAL)
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
        void shouldValidateValidProfile() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            assertDoesNotThrow(segment::validate);
        }

        @Test
        @DisplayName("Should throw when tenantId is blank")
        void shouldThrowWhenTenantIdIsBlank() {
            UserProfile segment = new UserProfile("  ", SEGMENT_NAME, criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when name is blank")
        void shouldThrowWhenNameIsBlank() {
            UserProfile segment = new UserProfile(TENANT_ID, "  ", criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when name exceeds 100 characters")
        void shouldThrowWhenNameExceedsMaxLength() {
            String longName = "a".repeat(101);
            UserProfile segment = new UserProfile(TENANT_ID, longName, criteria);

            assertThrows(ValidationException.class, segment::validate);
        }

        @Test
        @DisplayName("Should throw when criteria is null")
        void shouldThrowWhenCriteriaIsNull() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

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
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            assertFalse(segment.isActive());

            segment.activate();

            assertTrue(segment.isActive());
        }

        @Test
        @DisplayName("Should return correct modifiable status")
        void shouldReturnCorrectModifiableStatus() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            assertTrue(segment.isModifiable());

            segment.archive();

            assertFalse(segment.isModifiable());
        }

        @Test
        @DisplayName("Should mark as analyzed")
        void shouldMarkAsAnalyzed() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            segment.markAsAnalyzed();

            assertNotNull(segment.getLastAnalyzedAt());
        }

        @Test
        @DisplayName("Should set customer count")
        void shouldSetUserCount() {
            UserProfile segment = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            segment.setUserCount(500L);

            assertEquals(500L, segment.getUserCount());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build segment using builder")
        void shouldBuildProfileUsingBuilder() {
            UserProfile segment = UserProfile.Builder.builder()
                    .tenantId(TENANT_ID)
                    .name(SEGMENT_NAME)
                    .description(DESCRIPTION)
                    .criteria(criteria)
                    .segmentType(ProfileType.BEHAVIORAL)
                    .status(ProfileStatus.ACTIVE)
                    .customerCount(100L)
                    .build();

            assertEquals(TENANT_ID, segment.getTenantId());
            assertEquals(SEGMENT_NAME, segment.getName());
            assertEquals(DESCRIPTION, segment.getDescription());
            assertEquals(ProfileType.BEHAVIORAL, segment.getProfileType());
            assertEquals(ProfileStatus.ACTIVE, segment.getStatus());
            assertEquals(100L, segment.getUserCount());
        }

        @Test
        @DisplayName("Should throw when building without tenantId")
        void shouldThrowWhenBuildingWithoutTenantId() {
            assertThrows(IllegalArgumentException.class,
                    () -> UserProfile.Builder.builder()
                            .name(SEGMENT_NAME)
                            .criteria(criteria)
                            .build());
        }

        @Test
        @DisplayName("Should throw when building without name")
        void shouldThrowWhenBuildingWithoutName() {
            assertThrows(IllegalArgumentException.class,
                    () -> UserProfile.Builder.builder()
                            .tenantId(TENANT_ID)
                            .criteria(criteria)
                            .build());
        }

        @Test
        @DisplayName("Should throw when building without criteria")
        void shouldThrowWhenBuildingWithoutCriteria() {
            assertThrows(IllegalArgumentException.class,
                    () -> UserProfile.Builder.builder()
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
            UserProfile segment1 = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            UserProfile segment2 = UserProfile.Builder.builder()
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
            UserProfile segment1 = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);
            UserProfile segment2 = new UserProfile(TENANT_ID, SEGMENT_NAME, criteria);

            assertNotEquals(segment1, segment2);
        }
    }
}
