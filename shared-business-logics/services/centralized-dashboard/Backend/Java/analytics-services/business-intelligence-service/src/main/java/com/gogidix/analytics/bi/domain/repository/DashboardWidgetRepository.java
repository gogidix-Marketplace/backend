package com.gogidix.analytics.bi.domain.repository;

import com.gogidix.analytics.bi.domain.model.DashboardWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for DashboardWidget entity.
 */
@Repository
public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, String> {

    List<DashboardWidget> findByDashboardIdOrderByPositionAsc(String dashboardId);

    List<DashboardWidget> findByDashboardIdAndWidgetType(String dashboardId, DashboardWidget.WidgetType widgetType);

    void deleteByDashboardId(String dashboardId);
}
