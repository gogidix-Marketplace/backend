package com.gogidix.transaction.onboarding.domain.repository;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker.EntityType;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker.OnboardingStatus;
import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker.OnboardingStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingTrackerRepository extends MongoRepository<OnboardingTracker, UUID> {

    // Find by transaction ID
    Optional<OnboardingTracker> findByTransactionId(UUID transactionId);

    // Find by entity
    Optional<OnboardingTracker> findByEntityTypeAndEntityId(EntityType entityType, String entityId);

    // Find by merchant ID
    Page<OnboardingTracker> findByMerchantIdOrderByCreatedAtDesc(String merchantId, Pageable pageable);

    // Find by status
    Page<OnboardingTracker> findByCurrentStatusOrderByCreatedAtDesc(OnboardingStatus status, Pageable pageable);

    // Find by stage
    Page<OnboardingTracker> findByCurrentStageOrderByCreatedAtDesc(OnboardingStage stage, Pageable pageable);

    // Find by status and stage
    Page<OnboardingTracker> findByCurrentStatusAndCurrentStageOrderByCreatedAtDesc(
        OnboardingStatus status, OnboardingStage stage, Pageable pageable);

    // Find by idempotency key
    Optional<OnboardingTracker> findByIdempotencyKey(String idempotencyKey);

    // Find stuck onboardings (in progress for too long)
    @Query("{ 'currentStatus': ?0, 'updatedAt': { $lt: ?1 } }")
    List<OnboardingTracker> findStuckOnboardings(
        @Param("status") OnboardingStatus status,
        @Param("staleThreshold") LocalDateTime staleThreshold
    );

    // Count by status
    long countByCurrentStatus(OnboardingStatus status);

    // Count by stage
    long countByCurrentStage(OnboardingStage stage);

    // Find by assigned user
    Page<OnboardingTracker> findByAssignedToOrderByCreatedAtDesc(String assignedTo, Pageable pageable);

    // Find pending verification
    @Query("{ 'currentStatus': { $in: [ 'PENDING_VERIFICATION', 'UNDER_REVIEW' ] } }")
    List<OnboardingTracker> findPendingOnboardings();

    // Find high priority
    @Query("{ 'priority': { $in: [ 'HIGH', 'URGENT' ] }, 'currentStatus': { $nin: [ 'COMPLETED', 'CANCELLED' ] } }")
    List<OnboardingTracker> findHighPriorityOnboardings();

    // Find recent completions
    @Query("{ 'currentStatus': 'COMPLETED', 'completedAt': { $gte: ?0 } }")
    List<OnboardingTracker> findRecentCompletions(@Param("since") LocalDateTime since);

    // Search onboardings with multiple filters
    @Query("{ $and: [ " +
           "{ $or: [ " +
           "  { 'transactionId': ?0 }, " +
           "  { ?0: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'entityType': ?1 }, " +
           "  { ?1: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'entityId': ?2 }, " +
           "  { ?2: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'merchantId': ?3 }, " +
           "  { ?3: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'currentStatus': ?4 }, " +
           "  { ?4: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'currentStage': ?5 }, " +
           "  { ?5: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'assignedTo': ?6 }, " +
           "  { ?6: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'createdAt': { $gte: ?7 } }, " +
           "  { ?7: null } " +
           "] }, " +
           "{ $or: [ " +
           "  { 'createdAt': { $lte: ?8 } }, " +
           "  { ?8: null } " +
           "] } " +
           "] }")
    Page<OnboardingTracker> searchOnboardings(
        UUID transactionId,
        EntityType entityType,
        String entityId,
        String merchantId,
        OnboardingStatus status,
        OnboardingStage stage,
        String assignedTo,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pageable pageable
    );
}
