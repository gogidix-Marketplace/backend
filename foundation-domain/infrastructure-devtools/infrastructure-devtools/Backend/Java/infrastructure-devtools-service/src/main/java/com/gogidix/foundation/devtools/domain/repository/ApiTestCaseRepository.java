package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.ApiTestCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for ApiTestCase entity.
 */
@Repository
public interface ApiTestCaseRepository extends JpaRepository<ApiTestCase, Long> {

    Optional<ApiTestCase> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<ApiTestCase> findByProjectId(String projectId);

    Page<ApiTestCase> findByProjectId(String projectId, Pageable pageable);

    List<ApiTestCase> findByProjectIdAndEnabled(String projectId, Boolean enabled);

    @Query("SELECT t FROM ApiTestCase t WHERE t.projectId = :projectId AND " +
           "(LOWER(t.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ApiTestCase> searchByProject(@Param("projectId") String projectId,
                                       @Param("search") String search,
                                       Pageable pageable);

    List<ApiTestCase> findByEnvironment(String environment);

    @Query("SELECT t FROM ApiTestCase t WHERE t.tags LIKE CONCAT('%', :tag, '%')")
    List<ApiTestCase> findByTag(@Param("tag") String tag);
}
