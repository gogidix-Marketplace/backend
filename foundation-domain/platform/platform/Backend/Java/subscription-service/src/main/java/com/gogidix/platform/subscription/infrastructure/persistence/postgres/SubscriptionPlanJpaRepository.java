package com.gogidix.platform.subscription.infrastructure.persistence.postgres;

import com.gogidix.platform.subscription.domain.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for SubscriptionPlan entity.
 */
@Repository
public interface SubscriptionPlanJpaRepository extends JpaRepository<SubscriptionPlan, String> {

    Optional<SubscriptionPlan> findByPlanCode(String planCode);

    List<SubscriptionPlan> findByIsActive(boolean isActive);

    boolean existsByPlanCode(String planCode);
}
