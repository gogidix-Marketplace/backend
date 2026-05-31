package com.gogidix.dashboard.core.infrastructure.persistence.postgres;

import com.gogidix.dashboard.core.domain.model.DashboardKPI;
import com.gogidix.dashboard.core.domain.model.SourceDomain;
import com.gogidix.dashboard.core.domain.port.out.DashboardKPIRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of DashboardKPIRepository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class DashboardKPIRepositoryImpl implements DashboardKPIRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public DashboardKPI save(DashboardKPI kpi) {
        if (kpi.getId() == null) {
            entityManager.persist(kpi);
            return kpi;
        } else {
            return entityManager.merge(kpi);
        }
    }

    @Override
    public Optional<DashboardKPI> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(DashboardKPI.class, id));
    }

    @Override
    public Optional<DashboardKPI> findByCode(String code) {
        List<DashboardKPI> results = entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.code = :code", DashboardKPI.class)
                .setParameter("code", code)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<DashboardKPI> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public List<DashboardKPI> findByTenantIdAndCategory(String tenantId, String category) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId AND k.category = :category ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<DashboardKPI> findByTenantIdAndSourceDomain(String tenantId, SourceDomain sourceDomain) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId AND k.sourceDomain = :sourceDomain ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .setParameter("sourceDomain", sourceDomain)
                .getResultList();
    }

    @Override
    public List<DashboardKPI> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId AND k.isActive = :isActive ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .setParameter("isActive", isActive)
                .getResultList();
    }

    @Override
    public List<DashboardKPI> findByTenantIdAndIsRealTime(String tenantId, Boolean isRealTime) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId AND k.isRealTime = :isRealTime ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .setParameter("isRealTime", isRealTime)
                .getResultList();
    }

    @Override
    public List<DashboardKPI> findByTenantIdAndCodeIn(String tenantId, List<String> codes) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId AND k.code IN :codes ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .setParameter("codes", codes)
                .getResultList();
    }

    @Override
    public List<DashboardKPI> searchByTenantIdAndSearchQuery(String tenantId, String searchQuery) {
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.tenantId = :tenantId AND (LOWER(k.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(k.description) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(k.code) LIKE LOWER(CONCAT('%', :query, '%'))) ORDER BY k.name", DashboardKPI.class)
                .setParameter("tenantId", tenantId)
                .setParameter("query", searchQuery)
                .getResultList();
    }

    @Override
    public void delete(DashboardKPI kpi) {
        if (entityManager.contains(kpi)) {
            entityManager.remove(kpi);
        } else {
            DashboardKPI managed = entityManager.find(DashboardKPI.class, kpi.getId());
            if (managed != null) {
                entityManager.remove(managed);
            }
        }
    }

    @Override
    public void deleteByTenantId(String tenantId) {
        entityManager.createQuery(
                        "DELETE FROM DashboardKPI k WHERE k.tenantId = :tenantId")
                .setParameter("tenantId", tenantId)
                .executeUpdate();
    }

    @Override
    public boolean existsByCode(String code) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(k) FROM DashboardKPI k WHERE k.code = :code", Long.class)
                .setParameter("code", code)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public long countByTenantId(String tenantId) {
        return entityManager.createQuery(
                        "SELECT COUNT(k) FROM DashboardKPI k WHERE k.tenantId = :tenantId", Long.class)
                .setParameter("tenantId", tenantId)
                .getSingleResult();
    }

    @Override
    public List<DashboardKPI> findKPIsNeedingRefresh(int minutesThreshold) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(minutesThreshold);
        return entityManager.createQuery(
                        "SELECT k FROM DashboardKPI k WHERE k.isRealTime = true AND (k.lastCalculatedAt IS NULL OR k.lastCalculatedAt < :threshold)", DashboardKPI.class)
                .setParameter("threshold", threshold)
                .getResultList();
    }
}
