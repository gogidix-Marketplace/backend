package com.gogidix.aiservices.aianalyticsdashboard.application.service;

import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.CreateDashboardRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.UpdateDashboardRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.AddWidgetRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.DashboardResponse;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.WidgetResponse;
import com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate.Dashboard;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Widget;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;
import com.gogidix.aiservices.aianalyticsdashboard.domain.port.out.DashboardRepository;
import com.gogidix.aiservices.aianalyticsdashboard.shared.exception.DashboardNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Dashboard Service Application Tests")
class DashboardServiceTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String DASHBOARD_ID = "660e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Dashboard Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create dashboard successfully")
        void shouldCreateDashboard() {
            CreateDashboardRequest request = CreateDashboardRequest.builder()
                    .name("Sales Dashboard")
                    .description("Sales analytics")
                    .refreshInterval(300)
                    .build();

            Dashboard dashboard = Dashboard.create("Sales Dashboard", USER_ID);
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);

            DashboardResponse response = dashboardService.createDashboard(request, USER_ID);

            assertThat(response).isNotNull();
            assertThat(response.getName()).isEqualTo("Sales Dashboard");
            verify(dashboardRepository).save(any(Dashboard.class));
        }

        @Test
        @DisplayName("Should create dashboard with widgets")
        void shouldCreateDashboardWithWidgets() {
            AddWidgetRequest widgetRequest = AddWidgetRequest.builder()
                    .type(WidgetType.CHART)
                    .title("Revenue Chart")
                    .dataSource("revenue-metric")
                    .build();

            CreateDashboardRequest request = CreateDashboardRequest.builder()
                    .name("Analytics")
                    .widgets(List.of(widgetRequest))
                    .build();

            Dashboard dashboard = Dashboard.create("Analytics", USER_ID);
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);

            DashboardResponse response = dashboardService.createDashboard(request, USER_ID);

            assertThat(response).isNotNull();
            verify(dashboardRepository).save(any(Dashboard.class));
        }

        @Test
        @DisplayName("Should reject creation with invalid refresh interval")
        void shouldRejectInvalidRefreshInterval() {
            CreateDashboardRequest request = CreateDashboardRequest.builder()
                    .name("Test")
                    .refreshInterval(10) // Below minimum
                    .build();

            assertThatThrownBy(() -> dashboardService.createDashboard(request, USER_ID))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(dashboardRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Dashboard Retrieval Tests")
    class RetrievalTests {

        @Test
        @DisplayName("Should get dashboard by ID")
        void shouldGetDashboard() {
            Dashboard dashboard = Dashboard.create("Sales", USER_ID);
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));

            DashboardResponse response = dashboardService.getDashboard(DASHBOARD_ID);

            assertThat(response).isNotNull();
            verify(dashboardRepository).findById(DASHBOARD_ID);
        }

        @Test
        @DisplayName("Should throw exception when dashboard not found")
        void shouldThrowWhenNotFound() {
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> dashboardService.getDashboard(DASHBOARD_ID))
                    .isInstanceOf(DashboardNotFoundException.class);
        }

        @Test
        @DisplayName("Should get dashboards by user ID")
        void shouldGetByUserId() {
            List<Dashboard> dashboards = List.of(
                    Dashboard.create("Dashboard 1", USER_ID),
                    Dashboard.create("Dashboard 2", USER_ID)
            );
            when(dashboardRepository.findByUserId(USER_ID)).thenReturn(dashboards);

            List<DashboardResponse> responses = dashboardService.getUserDashboards(USER_ID);

            assertThat(responses).hasSize(2);
            verify(dashboardRepository).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should get public dashboards")
        void shouldGetPublicDashboards() {
            List<Dashboard> dashboards = List.of(Dashboard.create("Public", USER_ID));
            when(dashboardRepository.findPublicDashboards()).thenReturn(dashboards);

            List<DashboardResponse> responses = dashboardService.getPublicDashboards();

            assertThat(responses).hasSize(1);
            verify(dashboardRepository).findPublicDashboards();
        }
    }

    @Nested
    @DisplayName("Dashboard Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update dashboard successfully")
        void shouldUpdateDashboard() {
            Dashboard dashboard = Dashboard.create("Old Name", USER_ID);
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);

            UpdateDashboardRequest request = UpdateDashboardRequest.builder()
                    .name("New Name")
                    .description("Updated description")
                    .build();

            DashboardResponse response = dashboardService.updateDashboard(DASHBOARD_ID, request);

            assertThat(response).isNotNull();
            verify(dashboardRepository).save(any(Dashboard.class));
        }

        @Test
        @DisplayName("Should update dashboard visibility")
        void shouldUpdateVisibility() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);

            dashboardService.setDashboardVisibility(DASHBOARD_ID, true);

            verify(dashboardRepository).save(any(Dashboard.class));
        }

        @Test
        @DisplayName("Should throw when updating non-existent dashboard")
        void shouldThrowWhenUpdatingNonExistent() {
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.empty());

            UpdateDashboardRequest request = UpdateDashboardRequest.builder()
                    .name("New Name")
                    .build();

            assertThatThrownBy(() -> dashboardService.updateDashboard(DASHBOARD_ID, request))
                    .isInstanceOf(DashboardNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Widget Management Tests")
    class WidgetManagementTests {

        @Test
        @DisplayName("Should add widget to dashboard")
        void shouldAddWidget() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);

            AddWidgetRequest request = AddWidgetRequest.builder()
                    .type(WidgetType.METRIC)
                    .title("Total Revenue")
                    .dataSource("revenue")
                    .build();

            WidgetResponse response = dashboardService.addWidget(DASHBOARD_ID, request);

            assertThat(response).isNotNull();
            verify(dashboardRepository).save(any(Dashboard.class));
        }

        @Test
        @DisplayName("Should remove widget from dashboard")
        void shouldRemoveWidget() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            Widget widget = Widget.builder()
                    .widgetId("widget-1")
                    .type(WidgetType.METRIC)
                    .title("Test")
                    .dataSource("test")
                    .build();
            dashboard.addWidget(widget);

            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(dashboard);

            dashboardService.removeWidget(DASHBOARD_ID, "widget-1");

            verify(dashboardRepository).save(argThat(d -> d.getWidgetCount() == 0));
        }

        @Test
        @DisplayName("Should reject adding widget to full dashboard")
        void shouldRejectAddingToFullDashboard() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            // Add max widgets
            for (int i = 0; i < 20; i++) {
                try {
                    Widget widget = Widget.builder()
                            .widgetId("widget-" + i)
                            .type(WidgetType.METRIC)
                            .title("Widget " + i)
                            .dataSource("source-" + i)
                            .build();
                    dashboard.addWidget(widget);
                } catch (Exception e) {
                    // Ignore
                }
            }

            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));

            AddWidgetRequest request = AddWidgetRequest.builder()
                    .type(WidgetType.METRIC)
                    .title("Extra Widget")
                    .dataSource("extra")
                    .build();

            assertThatThrownBy(() -> dashboardService.addWidget(DASHBOARD_ID, request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("maximum");
        }
    }

    @Nested
    @DisplayName("Dashboard Deletion Tests")
    class DeletionTests {

        @Test
        @DisplayName("Should delete dashboard")
        void shouldDeleteDashboard() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));
            doNothing().when(dashboardRepository).delete(DASHBOARD_ID);

            dashboardService.deleteDashboard(DASHBOARD_ID);

            verify(dashboardRepository).delete(DASHBOARD_ID);
        }

        @Test
        @DisplayName("Should throw when deleting non-existent dashboard")
        void shouldThrowWhenDeletingNonExistent() {
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> dashboardService.deleteDashboard(DASHBOARD_ID))
                    .isInstanceOf(DashboardNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Dashboard Export Tests")
    class ExportTests {

        @Test
        @DisplayName("Should export dashboard configuration")
        void shouldExportDashboard() {
            Dashboard dashboard = Dashboard.create("Test", USER_ID);
            when(dashboardRepository.findById(DASHBOARD_ID)).thenReturn(Optional.of(dashboard));

            Map<String, Object> exported = dashboardService.exportDashboard(DASHBOARD_ID);

            assertThat(exported).isNotNull();
            assertThat(exported.get("name")).isEqualTo("Test");
            assertThat(exported.get("userId")).isEqualTo(USER_ID);
        }

        @Test
        @DisplayName("Should import dashboard configuration")
        void shouldImportDashboard() {
            Map<String, Object> config = new HashMap<>();
            config.put("name", "Imported Dashboard");
            config.put("description", "Imported");
            config.put("refreshInterval", 300);

            when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(i -> i.getArgument(0));

            DashboardResponse response = dashboardService.importDashboard(config, USER_ID);

            assertThat(response).isNotNull();
            assertThat(response.getName()).isEqualTo("Imported Dashboard");
            verify(dashboardRepository).save(any(Dashboard.class));
        }
    }
}
