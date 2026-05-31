package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.ApiTestExecution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ApiTestExecution entity.
 */
@Repository
public interface ApiTestExecutionRepository extends JpaRepository<ApiTestExecution, Long> {

    Optional<ApiTestExecution> findByUuid(UUID uuid);

    List<ApiTestExecution> findByTestCaseId(Long testCaseId);

    Page<ApiTestExecution> findByTestCaseId(Long testCaseId, Pageable pageable);

    List<ApiTestExecution> findByStatus(String status);

    Page<ApiTestExecution> findByStatus(String status, Pageable pageable);

    @Query("SELECT e FROM ApiTestExecution e WHERE e.testCaseId = :testCaseId AND " +
           "e.executedAt BETWEEN :startDate AND :endDate ORDER BY e.executedAt DESC")
    List<ApiTestExecution> findByTestCaseIdAndDateRange(
        @Param("testCaseId") Long testCaseId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM ApiTestExecution e WHERE e.testCaseId = :testCaseId AND e.status = :status")
    Long countByTestCaseIdAndStatus(@Param("testCaseId") Long testCaseId,
                                     @Param("status") String status);

    List<ApiTestExecution> findTop10ByTestCaseIdOrderByExecutedAtDesc(Long testCaseId);
}
