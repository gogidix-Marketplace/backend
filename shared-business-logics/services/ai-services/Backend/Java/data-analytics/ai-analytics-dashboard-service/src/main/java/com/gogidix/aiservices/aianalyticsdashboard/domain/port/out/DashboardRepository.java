package com.gogidix.aiservices.aianalyticsdashboard.domain.port.out;

import com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate.Dashboard;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DashboardRepository {
    Dashboard save(Dashboard dashboard);
    Optional<Dashboard> findById(String dashboardId);
    List<Dashboard> findByUserId(String userId);
    List<Dashboard> findPublicDashboards();
    void delete(String dashboardId);
    List<Dashboard> findUpdatedAfter(Instant timestamp);
}
