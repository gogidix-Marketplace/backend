package com.gogidix.transaction.onboarding.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OnboardingTracker Tests")
class OnboardingTrackerTest {

    @Test
    @DisplayName("Should create with builder and defaults")
    void shouldCreateWithBuilder() {
        OnboardingTracker t = OnboardingTracker.builder()
            .transactionId(UUID.randomUUID()).entityType(OnboardingTracker.EntityType.MERCHANT)
            .entityId("e1").merchantId("m1").build();
        assertEquals(OnboardingTracker.OnboardingStatus.INITIATED, t.getCurrentStatus());
        assertEquals(OnboardingTracker.OnboardingStage.REGISTRATION, t.getCurrentStage());
        assertEquals(OnboardingTracker.Priority.NORMAL, t.getPriority());
        assertEquals(0, t.getProgressPercentage());
        assertNotNull(t.getId());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        OnboardingTracker t = new OnboardingTracker();
        assertNotNull(t.getId());
    }

    @Test
    @DisplayName("Should set and get all fields")
    void shouldSetAndGetAllFields() {
        OnboardingTracker t = new OnboardingTracker();
        t.setTransactionId(UUID.randomUUID()); t.setEntityType(OnboardingTracker.EntityType.USER);
        t.setCurrentStatus(OnboardingTracker.OnboardingStatus.IN_PROGRESS);
        t.setCurrentStage(OnboardingTracker.OnboardingStage.KYC_VERIFICATION);
        t.setProgressPercentage(50); t.setCompletedSteps(5); t.setTotalSteps(10);
        t.setErrorMessage("err"); t.setRetryCount(1);
        assertEquals(OnboardingTracker.OnboardingStatus.IN_PROGRESS, t.getCurrentStatus());
        assertEquals(50, t.getProgressPercentage());
        assertEquals(1, t.getRetryCount());
    }

    @Test
    @DisplayName("Should test EntityType enum")
    void shouldTestEntityType() { assertEquals(5, OnboardingTracker.EntityType.values().length); }

    @Test
    @DisplayName("Should test OnboardingStatus enum")
    void shouldTestOnboardingStatus() { assertEquals(10, OnboardingTracker.OnboardingStatus.values().length); }

    @Test
    @DisplayName("Should test OnboardingStage enum")
    void shouldTestOnboardingStage() { assertEquals(11, OnboardingTracker.OnboardingStage.values().length); }

    @Test
    @DisplayName("Should test Priority enum")
    void shouldTestPriority() { assertEquals(4, OnboardingTracker.Priority.values().length); }
}

@DisplayName("OnboardingStageHistory Tests")
class OnboardingStageHistoryTest {

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        OnboardingStageHistory h = OnboardingStageHistory.builder()
            .trackerId(UUID.randomUUID())
            .fromStage(OnboardingTracker.OnboardingStage.REGISTRATION)
            .toStage(OnboardingTracker.OnboardingStage.KYC_VERIFICATION)
            .status("SUCCESS").build();
        assertEquals("SUCCESS", h.getStatus());
        assertNotNull(h.getId());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        OnboardingStageHistory h = new OnboardingStageHistory();
        assertNotNull(h.getId());
    }
}
