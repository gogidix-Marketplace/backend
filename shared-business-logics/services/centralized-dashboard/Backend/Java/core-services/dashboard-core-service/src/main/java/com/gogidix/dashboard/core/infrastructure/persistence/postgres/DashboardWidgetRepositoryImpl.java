package com.gogidix.dashboard.core.infrastructure.persistence.postgres;

import com.gogidix.dashboard.core.domain.model.DashboardWidget;
import com.gogidix.dashboard.core.domain.port.out.DashboardWidgetRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of DashboardWidgetRepository.
 */
@Slf4j
@Repository
public class DashboardWidgetRepositoryImpl implements DashboardWidgetRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public DashboardWidgetRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public DashboardWidget save(DashboardWidget widget) {
        if (widget.getId() == null) {
            entityManager.persist(widget);
            return widget;
        } else {
            return entityManager.merge(widget);
        }
    }

    @Override
    public Optional<DashboardWidget> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(DashboardWidget.class, id));
    }

    @Override
    public List<DashboardWidget> findByDashboardId(UUID dashboardId) {
        return entityManager.createQuery(
                        "SELECT w FROM DashboardWidget w WHERE w.dashboardId = :dashboardId ORDER BY w.positionY, w.positionX", DashboardWidget.class)
                .setParameter("dashboardId", dashboardId)
                .getResultList();
    }

    @Override
    public List<DashboardWidget> findByTenantId(String tenantId) {
        return entityManager.createQuery(
                        "SELECT w FROM DashboardWidget w WHERE w.tenantId = :tenantId ORDER BY w.dashboardId, w.positionY, w.positionX", DashboardWidget.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }

    @Override
    public List<DashboardWidget> findByDashboardIdAndIsVisible(UUID dashboardId, Boolean isVisible) {
        return entityManager.createQuery(
                        "SELECT w FROM DashboardWidget w WHERE w.dashboardId = :dashboardId AND w.isVisible = :isVisible ORDER BY w.positionY, w.positionX", DashboardWidget.class)
                .setParameter("dashboardId", dashboardId)
                .setParameter("isVisible", isVisible)
                .getResultList();
    }

    @Override
    public void delete(DashboardWidget widget) {
        if (entityManager.contains(widget)) {
            entityManager.remove(widget);
        } else {
            DashboardWidget managed = entityManager.find(DashboardWidget.class, widget.getId());
            if (managed != null) {
                entityManager.remove(managed);
            }
        }
    }

    @Override
    public void deleteByDashboardId(UUID dashboardId) {
        entityManager.createQuery(
                        "DELETE FROM DashboardWidget w WHERE w.dashboardId = :dashboardId")
                .setParameter("dashboardId", dashboardId)
                .executeUpdate();
    }

    @Override
    public long countByDashboardId(UUID dashboardId) {
        return entityManager.createQuery(
                        "SELECT COUNT(w) FROM DashboardWidget w WHERE w.dashboardId = :dashboardId", Long.class)
                .setParameter("dashboardId", dashboardId)
                .getSingleResult();
    }
}
