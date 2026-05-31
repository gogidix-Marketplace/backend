package com.gogidix.globalbusinessmanagement.dashboard.domain.repository;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.KPIBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for KPIBoard domain model.
 * Provides data access operations for KPI board configurations.
 */
@Repository
public interface KPIBoardRepository extends MongoRepository<KPIBoard, String> {

    /**
     * Find KPI board by name.
     *
     * @param name the board name
     * @return the KPI board
     */
    Optional<KPIBoard> findByName(String name);

    /**
     * Find KPI boards by owner.
     *
     * @param owner the owner username
     * @return list of KPI boards owned by the user
     */
    List<KPIBoard> findByOwner(String owner);

    /**
     * Find KPI boards where user is a viewer.
     *
     * @param viewer the viewer username
     * @return list of KPI boards visible to the user
     */
    List<KPIBoard> findByViewersContaining(String viewer);

    /**
     * Find KPI boards by owner or viewer.
     *
     * @param user the username
     * @return list of KPI boards owned by or visible to the user
     */
    @Query("{ $or: [ { 'owner': ?0 }, { 'viewers': ?0 } ] }")
    List<KPIBoard> findByOwnerOrViewer(String user);

    /**
     * Find KPI boards by status.
     *
     * @param status the board status
     * @return list of KPI boards with the given status
     */
    List<KPIBoard> findByStatus(KPIBoard.BoardStatus status);

    /**
     * Find KPI boards by scope.
     *
     * @param scope the board scope
     * @return list of KPI boards with the given scope
     */
    List<KPIBoard> findByScope(KPIBoard.BoardScope scope);

    /**
     * Find KPI boards by scope and scope ID.
     *
     * @param scope   the board scope
     * @param scopeId the scope identifier
     * @return list of KPI boards
     */
    List<KPIBoard> findByScopeAndScopeId(KPIBoard.BoardScope scope, String scopeId);

    /**
     * Find KPI boards by template ID.
     *
     * @param templateId the template identifier
     * @return list of KPI boards based on the template
     */
    List<KPIBoard> findByTemplateId(String templateId);

    /**
     * Find KPI boards with pagination.
     *
     * @param pageable the pagination information
     * @return page of KPI boards
     */
    Page<KPIBoard> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    /**
     * Find active KPI boards by owner.
     *
     * @param owner the owner username
     * @return list of active KPI boards owned by the user
     */
    List<KPIBoard> findByOwnerAndStatus(String owner, KPIBoard.BoardStatus status);

    /**
     * Find KPI boards by scope and status.
     *
     * @param scope  the board scope
     * @param status the board status
     * @return list of KPI boards
     */
    List<KPIBoard> findByScopeAndStatus(KPIBoard.BoardScope scope, KPIBoard.BoardStatus status);

    /**
     * Check if KPI board exists by name.
     *
     * @param name the board name
     * @return true if board exists, false otherwise
     */
    boolean existsByName(String name);

    /**
     * Delete KPI board by name.
     *
     * @param name the board name
     */
    void deleteByName(String name);

    /**
     * Count KPI boards by owner.
     *
     * @param owner the owner username
     * @return count of boards owned by the user
     */
    long countByOwner(String owner);

    /**
     * Count KPI boards by status.
     *
     * @param status the board status
     * @return count of boards with the given status
     */
    long countByStatus(KPIBoard.BoardStatus status);

    /**
     * Find KPI boards containing a specific KPI.
     *
     * @param kpiId the KPI identifier
     * @return list of KPI boards containing the KPI
     */
    @Query("{ 'kpis.kpiId': ?0 }")
    List<KPIBoard> findByKpiId(String kpiId);

    /**
     * Find KPI boards of a specific type.
     *
     * @param type the KPI type
     * @return list of KPI boards containing KPIs of the specified type
     */
    @Query("{ 'kpis.type': ?0 }")
    List<KPIBoard> findByKpiType(KPIBoard.KPIType type);

    /**
     * Find KPI boards with auto-refresh enabled.
     *
     * @return list of KPI boards with auto-refresh
     */
    @Query("{ 'preferences.autoRefresh': true }")
    List<KPIBoard> findWithAutoRefresh();

    /**
     * Find global scope KPI boards.
     *
     * @return list of global KPI boards
     */
    List<KPIBoard> findByScopeAndStatusOrderByUpdatedAtDesc(
        KPIBoard.BoardScope scope, KPIBoard.BoardStatus status);

    /**
     * Find KPI boards updated after a specific time.
     *
     * @param timestamp the timestamp
     * @return list of KPI boards updated after the timestamp
     */
    List<KPIBoard> findByUpdatedAtAfterOrderByUpdatedAtDesc(java.time.Instant timestamp);

    /**
     * Find KPI boards by multiple viewers.
     *
     * @param viewers list of viewer usernames
     * @return list of KPI boards visible to any of the viewers
     */
    @Query("{ 'viewers': { $in: ?0 } }")
    List<KPIBoard> findByViewersIn(List<String> viewers);

    /**
     * Get distinct board scopes.
     *
     * @return list of unique scopes
     */
    @Query("{ 'scope': { $exists: true } }")
    List<KPIBoard> findAllDistinctScopes();

    /**
     * Find KPI boards with refresh schedule.
     *
     * @return list of KPI boards with refresh schedules
     */
    @Query("{ 'refreshSchedule': { $ne: null } }")
    List<KPIBoard> findWithRefreshSchedule();

    /**
     * Find draft KPI boards by owner.
     *
     * @param owner the owner username
     * @return list of draft KPI boards
     */
    List<KPIBoard> findByOwnerAndStatusOrderByUpdatedAtDesc(
        String owner, KPIBoard.BoardStatus status);

    /**
     * Search KPI boards by name pattern.
     *
     * @param namePattern the name pattern (supports regex)
     * @return list of matching KPI boards
     */
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<KPIBoard> findByNameRegex(String namePattern);

    /**
     * Find KPI boards by description pattern.
     *
     * @param descriptionPattern the description pattern
     * @return list of matching KPI boards
     */
    @Query("{ 'description': { $regex: ?0, $options: 'i' } }")
    List<KPIBoard> findByDescriptionRegex(String descriptionPattern);

    /**
     * Find KPI boards with pagination for a specific user.
     *
     * @param user     the username
     * @param status   the board status
     * @param pageable the pagination information
     * @return page of KPI boards for the user
     */
    @Query("{ $and: [ { $or: [ { 'owner': ?0 }, { 'viewers': ?0 } ] }, { 'status': ?1 } ] }")
    Page<KPIBoard> findByUserAndStatus(String user, KPIBoard.BoardStatus status, Pageable pageable);

    /**
     * Find KPI boards by version.
     *
     * @param version the version number
     * @return list of KPI boards with the specified version
     */
    List<KPIBoard> findByVersion(Integer version);
}
