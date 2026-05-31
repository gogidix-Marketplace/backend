package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DocumentationProject;
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
 * Repository for DocumentationProject entity.
 */
@Repository
public interface DocumentationProjectRepository extends JpaRepository<DocumentationProject, Long> {

    Optional<DocumentationProject> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<DocumentationProject> findByProjectId(String projectId);

    Page<DocumentationProject> findByProjectId(String projectId, Pageable pageable);

    List<DocumentationProject> findByProjectIdAndEnabled(String projectId, Boolean enabled);

    @Query("SELECT p FROM DocumentationProject p WHERE p.projectId = :projectId AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DocumentationProject> searchByProject(@Param("projectId") String projectId,
                                                @Param("search") String search,
                                                Pageable pageable);
}
