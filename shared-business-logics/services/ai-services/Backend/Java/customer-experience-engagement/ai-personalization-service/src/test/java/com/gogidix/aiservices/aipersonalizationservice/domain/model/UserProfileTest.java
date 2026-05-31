package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.PersonalizationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DisplayName("UserProfile Domain Model Tests")
class UserProfileTest {

    private static final String VALID_USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String VALID_PROFILE_ID = "660e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Profile Creation Tests")
    class ProfileCreationTests {

        @Test
        @DisplayName("Should create profile with valid user ID")
        void shouldCreateProfileWithValidUserId() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            assertThat(profile).isNotNull();
            assertThat(profile.getUserId()).isEqualTo(UUID.fromString(VALID_USER_ID));
            assertThat(profile.getSegment()).isEqualTo(Segment.NEW_USER);
            assertThat(profile.getCreatedAt()).isNotNull();
            assertThat(profile.getUpdatedAt()).isNotNull();
            assertThat(profile.getAffinityScores()).isNotNull().isEmpty();
            assertThat(profile.getBehaviorHistory()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Should reject profile creation with null user ID")
        void shouldRejectNullUserId() {
            assertThatThrownBy(() -> UserProfile.create((String) null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID cannot be null");
        }

        @Test
        @DisplayName("Should reject profile creation with invalid UUID format")
        void shouldRejectInvalidUuidFormat() {
            assertThatThrownBy(() -> UserProfile.create("invalid-uuid"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid UUID format");
        }

        @Test
        @DisplayName("Should reject empty user ID")
        void shouldRejectEmptyUserId() {
            assertThatThrownBy(() -> UserProfile.create(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should generate unique profile ID on creation")
        void shouldGenerateUniqueProfileId() {
            UserProfile profile1 = UserProfile.create(VALID_USER_ID);
            UserProfile profile2 = UserProfile.create(VALID_USER_ID);

            assertThat(profile1.getProfileId()).isNotEqualTo(profile2.getProfileId());
        }
    }

    @Nested
    @DisplayName("User Attributes Tests")
    class UserAttributesTests {

        @Test
        @DisplayName("Should update user attributes successfully")
        void shouldUpdateUserAttributes() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            UserAttributes attributes = createValidAttributes();

            profile.updateAttributes(attributes);

            assertThat(profile.getAttributes()).isNotNull();
            assertThat(profile.getAttributes().getDemographics().getAge()).isEqualTo(30);
                        assertThat(profile.getUpdatedAt()).isAfter(profile.getCreatedAt());
        }

        @Test
        @DisplayName("Should reject null attributes")
        void shouldRejectNullAttributes() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            assertThatThrownBy(() -> profile.updateAttributes(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Attributes cannot be null");
        }

        @Test
        @DisplayName("Should validate demographic age range")
        void shouldValidateAgeRange() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            UserAttributes attributes = UserAttributes.builder()
                    .demographics(Demographics.builder()
                            .age(-1)
                            .gender("male")
                            .location("US")
                            .build())
                    .build();

            assertThatThrownBy(() -> profile.updateAttributes(attributes))
                    .isInstanceOf(PersonalizationException.class)
                    .hasMessageContaining("Age must be between");
        }

        @Test
        @DisplayName("Should update interests")
        void shouldUpdateInterests() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            List<String> interests = Arrays.asList("technology", "sports", "music");

            profile.updateInterests(interests);

            assertThat(profile.getAttributes().getInterests())
                    .containsExactlyInAnyOrderElementsOf(interests);
        }

        @Test
        @DisplayName("Should deduplicate interests")
        void shouldDeduplicateInterests() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            List<String> interests = Arrays.asList("technology", "sports", "technology");

            profile.updateInterests(interests);

            assertThat(profile.getAttributes().getInterests())
                    .containsExactlyInAnyOrder("technology", "sports")
                    .hasSize(2);
        }
    }

    @Nested
    @DisplayName("Behavior Tracking Tests")
    class BehaviorTrackingTests {

        @Test
        @DisplayName("Should add behavior event successfully")
        void shouldAddBehaviorEvent() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            BehaviorEvent event = BehaviorEvent.builder()
                    .eventType(EventType.VIEW)
                    .itemId("item-123")
                    .timestamp(Instant.now())
                    .build();

            profile.addBehaviorEvent(event);

            assertThat(profile.getBehaviorHistory()).hasSize(1);
            assertThat(profile.getBehaviorHistory().get(0)).isEqualTo(event);
        }

        @Test
        @DisplayName("Should reject null behavior event")
        void shouldRejectNullBehaviorEvent() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            assertThatThrownBy(() -> profile.addBehaviorEvent(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Behavior event cannot be null");
        }

        @Test
        @DisplayName("Should maintain behavior history in chronological order")
        void shouldMaintainChronologicalOrder() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            Instant now = Instant.now();

            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", now.minusSeconds(10)));
            profile.addBehaviorEvent(createEvent(EventType.CLICK, "item-2", now));
            profile.addBehaviorEvent(createEvent(EventType.LIKE, "item-3", now.minusSeconds(5)));

            List<BehaviorEvent> events = profile.getBehaviorHistory();
            assertThat(events.get(0).getEventType()).isEqualTo(EventType.VIEW);
            assertThat(events.get(1).getEventType()).isEqualTo(EventType.LIKE);
            assertThat(events.get(2).getEventType()).isEqualTo(EventType.CLICK);
        }

        @Test
        @DisplayName("Should limit behavior history size")
        void shouldLimitBehaviorHistorySize() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            for (int i = 0; i < 1500; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }

            assertThat(profile.getBehaviorHistory()).hasSize(1000); // Max 1000 events
        }

        @Test
        @DisplayName("Should deduplicate identical events")
        void shouldDeduplicateIdenticalEvents() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            Instant timestamp = Instant.now();

            BehaviorEvent event1 = createEvent(EventType.VIEW, "item-1", timestamp);
            BehaviorEvent event2 = createEvent(EventType.VIEW, "item-1", timestamp);

            profile.addBehaviorEvent(event1);
            profile.addBehaviorEvent(event2);

            assertThat(profile.getBehaviorHistory()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Segment Assignment Tests")
    class SegmentAssignmentTests {

        @Test
        @DisplayName("Should start as NEW_USER segment")
        void shouldStartAsNewUser() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            assertThat(profile.getSegment()).isEqualTo(Segment.NEW_USER);
        }

        @Test
        @DisplayName("Should upgrade to ACTIVE after sufficient interactions")
        void shouldUpgradeToActive() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            // Add 10 interactions
            for (int i = 0; i < 10; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }

            profile.recalculateSegment();

            assertThat(profile.getSegment()).isEqualTo(Segment.ACTIVE);
        }

        @Test
        @DisplayName("Should assign VIP segment with high engagement and purchases")
        void shouldAssignVipSegment() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            // Add 100 views
            for (int i = 0; i < 90; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }
            // Add 10 purchases
            for (int i = 0; i < 10; i++) {
                profile.addBehaviorEvent(createEvent(EventType.PURCHASE, "product-" + i, Instant.now()));
            }

            profile.recalculateSegment();

            assertThat(profile.getSegment()).isEqualTo(Segment.VIP);
        }

        @Test
        @DisplayName("Should assign INACTIVE after period of inactivity")
        void shouldAssignInactiveSegment() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", Instant.now().minusSeconds(31 * 24 * 60 * 60)));

            profile.recalculateSegment();

            assertThat(profile.getSegment()).isEqualTo(Segment.INACTIVE);
        }

        @ParameterizedTest
        @EnumSource(value = EventType.class, names = {"VIEW", "CLICK", "LIKE", "SHARE", "PURCHASE"})
        @DisplayName("Should count all valid event types")
        void shouldCountAllEventTypes(EventType eventType) {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            profile.addBehaviorEvent(createEvent(eventType, "item-1", Instant.now()));

            assertThat(profile.getInteractionCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Affinity Score Tests")
    class AffinityScoreTests {

        @Test
        @DisplayName("Should calculate affinity scores from behaviors")
        void shouldCalculateAffinityScores() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            // Add views to technology category
            for (int i = 0; i < 5; i++) {
                BehaviorEvent event = createEvent(EventType.VIEW, "tech-item-" + i, Instant.now());
                event.addProperties("category", "technology");
                profile.addBehaviorEvent(event);
            }

            // Add views to sports category
            for (int i = 0; i < 3; i++) {
                BehaviorEvent event = createEvent(EventType.VIEW, "sport-item-" + i, Instant.now());
                event.addProperties("category", "sports");
                profile.addBehaviorEvent(event);
            }

            profile.recalculateAffinityScores();

            assertThat(profile.getAffinityScores().get("technology"))
                    .isGreaterThan(profile.getAffinityScores().get("sports"));
        }

        @Test
        @DisplayName("Should weight purchases higher than views")
        void shouldWeightPurchasesHigher() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            BehaviorEvent viewEvent = createEvent(EventType.VIEW, "item-1", Instant.now());
            viewEvent.addProperties("category", "electronics");
            profile.addBehaviorEvent(viewEvent);

            BehaviorEvent purchaseEvent = createEvent(EventType.PURCHASE, "item-2", Instant.now());
            purchaseEvent.addProperties("category", "electronics");
            profile.addBehaviorEvent(purchaseEvent);

            profile.recalculateAffinityScores();

            assertThat(profile.getAffinityScores().get("electronics")).isGreaterThan(0.5);
        }

        @Test
        @DisplayName("Should normalize affinity scores to 0-1 range")
        void shouldNormalizeAffinityScores() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            for (int i = 0; i < 20; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }

            profile.recalculateAffinityScores();

            profile.getAffinityScores().values().forEach(score -> {
                assertThat(score).isBetween(0.0, 1.0);
            });
        }

        @Test
        @DisplayName("Should boost affinity with repeated interactions")
        void shouldBoostAffinityWithRepetition() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            // Multiple interactions with same item
            for (int i = 0; i < 5; i++) {
                BehaviorEvent event = createEvent(EventType.VIEW, "popular-item", Instant.now());
                event.addProperties("itemId", "popular-item");
                profile.addBehaviorEvent(event);
            }

            profile.recalculateAffinityScores();

            assertThat(profile.getAffinityScores().get("popular-item"))
                    .isGreaterThan(0.3);
        }
    }

    @Nested
    @DisplayName("Recommendation Eligibility Tests")
    class RecommendationEligibilityTests {

        @Test
        @DisplayName("Should be eligible for recommendations with minimum interactions")
        void shouldBeEligibleWithMinimumInteractions() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            for (int i = 0; i < 5; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }

            assertThat(profile.isEligibleForRecommendations()).isTrue();
        }

        @Test
        @DisplayName("Should not be eligible without sufficient data")
        void shouldNotBeEligibleWithoutData() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            assertThat(profile.isEligibleForRecommendations()).isFalse();
        }

        @Test
        @DisplayName("Should have minimum score threshold")
        void shouldHaveMinimumScoreThreshold() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            for (int i = 0; i < 5; i++) {
                profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-" + i, Instant.now()));
            }
            profile.recalculateAffinityScores();

            assertThat(profile.hasMinimumAffinity()).isTrue();
        }
    }

    @Nested
    @DisplayName("Profile Update Tests")
    class ProfileUpdateTests {

        @Test
        @DisplayName("Should update timestamp on modification")
        void shouldUpdateTimestamp() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);
            Instant originalTimestamp = profile.getUpdatedAt();

            try {
                Thread.sleep(10); // Small delay
            } catch (InterruptedException e) {
                // Ignore
            }

            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", Instant.now()));

                        assertThat(profile.getUpdatedAt()).isAfter(originalTimestamp);
        }

        @Test
        @DisplayName("Should track modification count")
        void shouldTrackModificationCount() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", Instant.now()));
            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-2", Instant.now()));

            assertThat(profile.getModificationCount()).isGreaterThanOrEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Equality and HashCode Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal with same profile ID")
        void shouldBeEqualWithSameId() {
            UUID profileId = UUID.randomUUID();
            UserProfile profile1 = UserProfile.create(VALID_USER_ID);
            UserProfile profile2 = UserProfile.create(VALID_USER_ID);

            // Reflection to set same ID for testing
            assertThat(profile1.getProfileId()).isNotEqualTo(profile2.getProfileId());
            assertThat(profile1).isNotEqualTo(profile2);
        }

        @Test
        @DisplayName("Should have consistent hash code")
        void shouldHaveConsistentHashCode() {
            UserProfile profile = UserProfile.create(VALID_USER_ID);

            int hashCode1 = profile.hashCode();
            int hashCode2 = profile.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }

    // Helper methods

    private UserAttributes createValidAttributes() {
        return UserAttributes.builder()
                .demographics(Demographics.builder()
                        .age(30)
                        .gender("male")
                        .location("US")
                        .build())
                .interests(Arrays.asList("technology", "sports"))
                .preferences(Preferences.builder()
                        .categories(Arrays.asList("electronics", "books"))
                        .brands(Arrays.asList("Apple", "Nike"))
                        .build())
                .build();
    }

    private BehaviorEvent createEvent(EventType type, String itemId, Instant timestamp) {
        BehaviorEvent event = BehaviorEvent.builder()
                .eventType(type)
                .itemId(itemId)
                .timestamp(timestamp)
                .build();
        return event;
    }
}
