package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DeploymentJob;
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
 * Repository for DeploymentJob entity.
 */
@Repository
public interface DeploymentJobRepository extends JpaRepository<DeploymentJob, Long> {

    Optional<DeploymentJob> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<DeploymentJob> findByProjectId(String projectId);

    Page<DeploymentJob> findByProjectId(String projectId, Pageable pageable);

    List<DeploymentJob> findByProjectIdAndEnabled(String projectId, Boolean enabled);

    List<DeploymentJob> findByTargetEnvironment(String environment);

    @Query("SELECT j FROM DeploymentJob j WHERE j.projectId = :projectId AND " +
           "(LOWER(j.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(j.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DeploymentJob> searchByProject(@Param("projectId") String projectId,
                                         @Param("search") String search,
                                         Pageable pageable);

    List<DeploymentJob> findByType(String type);
}
