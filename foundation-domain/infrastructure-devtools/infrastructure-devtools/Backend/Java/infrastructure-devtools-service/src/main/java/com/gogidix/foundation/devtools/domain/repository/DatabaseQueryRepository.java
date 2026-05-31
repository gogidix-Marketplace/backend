package com.gogidix.foundation.devtools.domain.repository;

import com.gogidix.foundation.devtools.domain.entity.DatabaseQuery;
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
 * Repository for DatabaseQuery entity.
 */
@Repository
public interface DatabaseQueryRepository extends JpaRepository<DatabaseQuery, Long> {

    Optional<DatabaseQuery> findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);

    List<DatabaseQuery> findByProjectId(String projectId);

    Page<DatabaseQuery> findByProjectId(String projectId, Pageable pageable);

    List<DatabaseQuery> findByProjectIdAndEnabled(String projectId, Boolean enabled);

    @Query("SELECT q FROM DatabaseQuery q WHERE q.projectId = :projectId AND " +
           "(LOWER(q.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(q.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<DatabaseQuery> searchByProject(@Param("projectId") String projectId,
                                         @Param("search") String search,
                                         Pageable pageable);

    List<DatabaseQuery> findByDatabaseName(String databaseName);

    List<DatabaseQuery> findByQueryType(String queryType);

    @Query("SELECT q FROM DatabaseQuery q WHERE q.tags LIKE CONCAT('%', :tag, '%')")
    List<DatabaseQuery> findByTag(@Param("tag") String tag);
}
