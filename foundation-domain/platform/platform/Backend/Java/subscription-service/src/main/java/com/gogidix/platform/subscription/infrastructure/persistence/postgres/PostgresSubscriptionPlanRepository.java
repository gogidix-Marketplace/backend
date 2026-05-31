package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.SubscriptionPlan;
import com.gogidix.platform.subscription.domain.repository.SubscriptionPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of SubscriptionPlanRepository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PostgresSubscriptionPlanRepository {

    private final SubscriptionPlanJpaRepository jpaRepository;

    public SubscriptionPlan save(SubscriptionPlan plan) {
        log.debug("Saving subscription plan: id={}", plan.getId());
        return jpaRepository.save(plan);
    }

    public Optional<SubscriptionPlan> findById(String id) {
        return jpaRepository.findById(id);
    }

    public Optional<SubscriptionPlan> findByPlanCode(String planCode) {
        return jpaRepository.findByPlanCode(planCode);
    }

    public List<SubscriptionPlan> findByIsActive(boolean isActive) {
        return jpaRepository.findByIsActive(isActive);
    }

    public List<SubscriptionPlan> findAll() {
        return jpaRepository.findAll();
    }

    public void delete(SubscriptionPlan plan) {
        jpaRepository.delete(plan);
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public boolean existsByPlanCode(String planCode) {
        return jpaRepository.existsByPlanCode(planCode);
    }
}
