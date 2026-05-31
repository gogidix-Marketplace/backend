package com.gogidix.transaction.progress.domain.repository;

import com.gogidix.transaction.progress.domain.entity.ProgressStep;
import com.gogidix.transaction.progress.domain.entity.ProgressStep.StepStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA Repository for ProgressStep entities.
 * Converted from MongoDB to PostgreSQL.
 */
@Repository
public interface ProgressStepRepository extends JpaRepository<ProgressStep, UUID> {

    List<ProgressStep> findByTransactionIdOrderByStepOrderAsc(UUID transactionId);

    List<ProgressStep> findByTransactionIdAndStatus(UUID transactionId, StepStatus status);

    List<ProgressStep> findByParentStepId(UUID parentStepId);

    List<ProgressStep> findByTransactionIdAndStepOrder(UUID transactionId, Integer stepOrder);

    List<ProgressStep> findByTransactionIdAndStatusNot(UUID transactionId, StepStatus status);

    List<ProgressStep> findByStatusAndStartedAtBefore(StepStatus status, LocalDateTime threshold);

    void deleteByTransactionId(UUID transactionId);

    long countByTransactionId(UUID transactionId);

    long countByTransactionIdAndStatus(UUID transactionId, StepStatus status);

    @Query("SELECT s FROM ProgressStep s WHERE s.transactionId = :transactionId " +
           "AND s.status = :status AND s.startedAt < :threshold")
    List<ProgressStep> findTimedOutSteps(@Param("transactionId") UUID transactionId,
                                          @Param("status") StepStatus status,
                                          @Param("threshold") LocalDateTime threshold);

    @Query("SELECT s FROM ProgressStep s WHERE s.transactionId = :transactionId " +
           "AND s.stepOrder BETWEEN :startOrder AND :endOrder ORDER BY s.stepOrder ASC")
    List<ProgressStep> findByTransactionIdAndStepOrderBetween(@Param("transactionId") UUID transactionId,
                                                                @Param("startOrder") Integer startOrder,
                                                                @Param("endOrder") Integer endOrder);

    @Query("SELECT COUNT(s) FROM ProgressStep s WHERE s.transactionId = :transactionId " +
           "AND s.status IN :statuses")
    long countByTransactionIdAndStatusIn(@Param("transactionId") UUID transactionId,
                                          @Param("statuses") List<StepStatus> statuses);
}
