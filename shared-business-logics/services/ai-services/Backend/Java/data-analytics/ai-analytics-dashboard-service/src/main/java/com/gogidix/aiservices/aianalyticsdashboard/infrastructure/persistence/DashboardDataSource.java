package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.persistence;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DashboardDataSource {
    DashboardEntity save(DashboardEntity entity);
    Optional<DashboardEntity> findById(String dashboardId);
    List<DashboardEntity> findByUserId(String userId);
    List<DashboardEntity> findPublicDashboards();
    List<DashboardEntity> findUpdatedAfter(Instant timestamp);
    void delete(String dashboardId);
}
