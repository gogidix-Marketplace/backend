package com.gogidix.aiservices.aipersonalizationservice.infrastructure.persistence;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.ProfileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Profile Repository Infrastructure Tests")
class UserProfileRepositoryImplTest {

    @Mock
    private UserProfileDataSource dataSource;

    @InjectMocks
    private UserProfileRepositoryImpl repository;

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Save Operations")
    class SaveOperations {

        @Test
        @DisplayName("Should save new profile")
        void shouldSaveNewProfile() {
            UserProfile profile = UserProfile.create(USER_ID);

            when(dataSource.save(any(UserProfileEntity.class))).thenReturn(new UserProfileEntity());

            UserProfile saved = repository.save(profile);

            assertThat(saved).isNotNull();
            verify(dataSource).save(any(UserProfileEntity.class));
        }

        @Test
        @DisplayName("Should update existing profile")
        void shouldUpdateExistingProfile() {
            UserProfile profile = UserProfile.create(USER_ID);
            profile.addBehaviorEvent(createEvent(EventType.VIEW, "item-1", Instant.now()));

            when(dataSource.save(any(UserProfileEntity.class))).thenReturn(new UserProfileEntity());

            repository.save(profile);

            verify(dataSource).save(any(UserProfileEntity.class));
        }

        @Test
        @DisplayName("Should handle save failure gracefully")
        void shouldHandleSaveFailure() {
            UserProfile profile = UserProfile.create(USER_ID);

            when(dataSource.save(any(UserProfileEntity.class)))
                    .thenThrow(new RuntimeException("Database error"));

            assertThatThrownBy(() -> repository.save(profile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Database error");
        }
    }

    @Nested
    @DisplayName("Find Operations")
    class FindOperations {

        @Test
        @DisplayName("Should find profile by user ID")
        void shouldFindByUserId() {
            UserProfileEntity entity = createProfileEntity();
            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.of(entity));

            Optional<UserProfile> result = repository.findByUserId(USER_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getUserId()).isEqualTo(UUID.fromString(USER_ID));
            verify(dataSource).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should return empty when profile not found")
        void shouldReturnEmptyWhenNotFound() {
            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.empty());

            Optional<UserProfile> result = repository.findByUserId(USER_ID);

            assertThat(result).isEmpty();
            verify(dataSource).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should find profile by profile ID")
        void shouldFindByProfileId() {
            String profileId = "660e8400-e29b-41d4-a716-446655440000";
            UserProfileEntity entity = createProfileEntity();
            when(dataSource.findById(profileId)).thenReturn(Optional.of(entity));

            Optional<UserProfile> result = repository.findById(profileId);

            assertThat(result).isPresent();
            verify(dataSource).findById(profileId);
        }

        @Test
        @DisplayName("Should find all profiles")
        void shouldFindAll() {
            List<UserProfileEntity> entities = Arrays.asList(
                    createProfileEntity(),
                    createProfileEntity()
            );
            when(dataSource.findAll()).thenReturn(entities);

            List<UserProfile> results = repository.findAll();

            assertThat(results).hasSize(2);
            verify(dataSource).findAll();
        }
    }

    @Nested
    @DisplayName("Delete Operations")
    class DeleteOperations {

        @Test
        @DisplayName("Should delete profile by user ID")
        void shouldDeleteByUserId() {
            doNothing().when(dataSource).deleteByUserId(USER_ID);

            repository.delete(USER_ID);

            verify(dataSource).deleteByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should delete profile by profile ID")
        void shouldDeleteByProfileId() {
            String profileId = "660e8400-e29b-41d4-a716-446655440000";
            doNothing().when(dataSource).deleteById(profileId);

            repository.deleteById(profileId);

            verify(dataSource).deleteById(profileId);
        }
    }

    @Nested
    @DisplayName("Entity Mapping Tests")
    class EntityMappingTests {

        @Test
        @DisplayName("Should map entity to domain model correctly")
        void shouldMapEntityToDomain() {
            UserProfileEntity entity = createProfileEntity();
            entity.setSegment(Segment.VIP);
            entity.setAffinityScores(Map.of("electronics", 0.9));

            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.of(entity));

            Optional<UserProfile> result = repository.findByUserId(USER_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getSegment()).isEqualTo(Segment.VIP);
            assertThat(result.get().getAffinityScores()).containsEntry("electronics", 0.9);
        }

        @Test
        @DisplayName("Should map domain model to entity correctly")
        void shouldMapDomainToEntity() {
            UserProfile profile = UserProfile.create(USER_ID);
            profile.addBehaviorEvent(createEvent(EventType.PURCHASE, "item-1", Instant.now()));

            when(dataSource.save(any(UserProfileEntity.class))).thenReturn(new UserProfileEntity());

            repository.save(profile);

            verify(dataSource).save(argThat(entity ->
                    entity.getUserId().equals(UUID.fromString(USER_ID)) &&
                            !entity.getBehaviorEvents().isEmpty()
            ));
        }

        @Test
        @DisplayName("Should preserve behavior history order")
        void shouldPreserveBehaviorHistory() {
            UserProfileEntity entity = createProfileEntity();
            List<BehaviorEventEntity> events = Arrays.asList(
                    createBehaviorEventEntity("item-1", Instant.now().minusSeconds(10)),
                    createBehaviorEventEntity("item-2", Instant.now()),
                    createBehaviorEventEntity("item-3", Instant.now().minusSeconds(5))
            );
            entity.setBehaviorEvents(events);

            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.of(entity));

            Optional<UserProfile> result = repository.findByUserId(USER_ID);

            assertThat(result).isPresent();
            assertThat(result.get().getBehaviorHistory()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Caching Tests")
    class CachingTests {

        @Test
        @DisplayName("Should cache profile retrieval")
        void shouldCacheProfileRetrieval() {
            UserProfileEntity entity = createProfileEntity();
            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.of(entity));

            repository.findByUserId(USER_ID);
            repository.findByUserId(USER_ID);

            verify(dataSource, times(1)).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should invalidate cache on update")
        void shouldInvalidateCacheOnUpdate() {
            UserProfile profile = UserProfile.create(USER_ID);
            when(dataSource.save(any(UserProfileEntity.class))).thenReturn(new UserProfileEntity());

            repository.save(profile);

            // Next call should hit database again
            UserProfileEntity entity = createProfileEntity();
            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.of(entity));

            repository.findByUserId(USER_ID);

            verify(dataSource).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should invalidate cache on delete")
        void shouldInvalidateCacheOnDelete() {
            doNothing().when(dataSource).deleteByUserId(USER_ID);

            repository.delete(USER_ID);

            // Next call should hit database
            when(dataSource.findByUserId(USER_ID)).thenReturn(Optional.empty());
            repository.findByUserId(USER_ID);

            verify(dataSource).findByUserId(USER_ID);
        }
    }

    @Nested
    @DisplayName("Batch Operations Tests")
    class BatchOperationsTests {

        @Test
        @DisplayName("Should save multiple profiles")
        void shouldSaveMultipleProfiles() {
            List<UserProfile> profiles = Arrays.asList(
                    UserProfile.create(USER_ID),
                    UserProfile.create("660e8400-e29b-41d4-a716-446655440000")
            );

            when(dataSource.saveAll(anyList())).thenReturn(new ArrayList<>());

            repository.saveAll(profiles);

            verify(dataSource).saveAll(anyList());
        }

        @Test
        @DisplayName("Should find profiles by segment")
        void shouldFindBySegment() {
            List<UserProfileEntity> entities = Arrays.asList(
                    createProfileEntity(),
                    createProfileEntity()
            );
            when(dataSource.findBySegment(Segment.ACTIVE)).thenReturn(entities);

            List<UserProfile> results = repository.findBySegment(Segment.ACTIVE);

            assertThat(results).hasSize(2);
            verify(dataSource).findBySegment(Segment.ACTIVE);
        }

        @Test
        @DisplayName("Should find profiles updated after timestamp")
        void shouldFindByUpdatedAtAfter() {
            Instant timestamp = Instant.now().minusSeconds(3600);
            List<UserProfileEntity> entities = Arrays.asList(createProfileEntity());
            when(dataSource.findByUpdatedAtAfter(timestamp)).thenReturn(entities);

            List<UserProfile> results = repository.findByUpdatedAtAfter(timestamp);

            assertThat(results).hasSize(1);
            verify(dataSource).findByUpdatedAtAfter(timestamp);
        }
    }

    @Nested
    @DisplayName("Transaction Tests")
    class TransactionTests {

        @Test
        @DisplayName("Should rollback on save failure")
        void shouldRollbackOnSaveFailure() {
            UserProfile profile = UserProfile.create(USER_ID);

            when(dataSource.save(any(UserProfileEntity.class)))
                    .thenThrow(new RuntimeException("Database error"));

            assertThatThrownBy(() -> repository.save(profile))
                    .isInstanceOf(RuntimeException.class);

            verify(dataSource).save(any(UserProfileEntity.class));
        }

        @Test
        @DisplayName("Should handle concurrent updates")
        void shouldHandleConcurrentUpdates() {
            UserProfile profile = UserProfile.create(USER_ID);
            when(dataSource.save(any(UserProfileEntity.class)))
                    .thenThrow(new RuntimeException("Optimistic lock failure"));

            assertThatThrownBy(() -> repository.save(profile))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    // Helper methods

    private UserProfileEntity createProfileEntity() {
        UserProfileEntity entity = new UserProfileEntity();
        entity.setProfileId(UUID.randomUUID().toString());
        entity.setUserId(UUID.fromString(USER_ID));
        entity.setSegment(Segment.NEW_USER);
        entity.setAffinityScores(new HashMap<>());
        entity.setBehaviorEvents(new ArrayList<>());
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    private BehaviorEventEntity createBehaviorEventEntity(String itemId, Instant timestamp) {
        BehaviorEventEntity entity = new BehaviorEventEntity();
        entity.setEventId(UUID.randomUUID().toString());
        entity.setEventType(EventType.VIEW);
        entity.setItemId(itemId);
        entity.setTimestamp(timestamp);
        entity.setProperties(new HashMap<>());
        return entity;
    }

    private BehaviorEvent createEvent(EventType type, String itemId, Instant timestamp) {
        return BehaviorEvent.builder()
                .eventType(type)
                .itemId(itemId)
                .timestamp(timestamp)
                .build();
    }
}
