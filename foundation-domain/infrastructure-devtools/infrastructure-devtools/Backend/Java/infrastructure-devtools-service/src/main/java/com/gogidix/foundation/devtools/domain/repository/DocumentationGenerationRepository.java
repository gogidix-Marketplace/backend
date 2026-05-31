package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DocumentationGeneration;
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
 * Repository for DocumentationGeneration entity.
 */
@Repository
public interface DocumentationGenerationRepository extends JpaRepository<DocumentationGeneration, Long> {

    Optional<DocumentationGeneration> findByUuid(UUID uuid);

    List<DocumentationGeneration> findByProjectId(Long projectId);

    Page<DocumentationGeneration> findByProjectId(Long projectId, Pageable pageable);

    List<DocumentationGeneration> findByStatus(String status);

    @Query("SELECT g FROM DocumentationGeneration g WHERE g.projectId = :projectId AND " +
           "g.createdAt BETWEEN :startDate AND :endDate ORDER BY g.createdAt DESC")
    List<DocumentationGeneration> findByProjectIdAndDateRange(
        @Param("projectId") Long projectId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(g) FROM DocumentationGeneration g WHERE g.projectId = :projectId AND g.status = :status")
    Long countByProjectIdAndStatus(@Param("projectId") Long projectId,
                                     @Param("status") String status);

    List<DocumentationGeneration> findTop10ByProjectIdOrderByCreatedAtDesc(Long projectId);
}
