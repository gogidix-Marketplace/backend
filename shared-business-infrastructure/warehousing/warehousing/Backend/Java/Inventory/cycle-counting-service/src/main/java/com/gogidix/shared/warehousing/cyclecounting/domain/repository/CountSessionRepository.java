package com.gogidix.shared.warehousing.cyclecounting.domain.repository;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CountSession;
import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CountSession.SessionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Count Session Repository
 */
@Repository
public interface CountSessionRepository extends MongoRepository<CountSession, String> {

    /**
     * Find sessions by cycle count
     */
    List<CountSession> findByTenantIdAndCycleCountIdOrderByStartedAtDesc(
            String tenantId, String cycleCountId);

    /**
     * Find sessions by counter
     */
    List<CountSession> findByTenantIdAndCounterIdOrderByStartedAtDesc(
            String tenantId, String counterId);

    /**
     * Find active sessions
     */
    List<CountSession> findByTenantIdAndStatus(String tenantId, SessionStatus status);

    /**
     * Find by session number
     */
    List<CountSession> findByTenantIdAndSessionNumber(String tenantId, String sessionNumber);
}
