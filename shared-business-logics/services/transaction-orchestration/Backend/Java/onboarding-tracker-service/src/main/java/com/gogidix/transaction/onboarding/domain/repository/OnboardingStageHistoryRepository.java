package com.gogidix.transaction.onboarding.domain.repository;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingStageHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OnboardingStageHistoryRepository extends MongoRepository<OnboardingStageHistory, UUID> {

    List<OnboardingStageHistory> findByTrackerIdOrderByTimestampAsc(UUID trackerId);

    List<OnboardingStageHistory> findTop50ByTrackerIdOrderByTimestampDesc(UUID trackerId);

    void deleteByTrackerId(UUID trackerId);
}
