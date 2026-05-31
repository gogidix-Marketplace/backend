package com.gogidix.analytics.bi.interfaces.rest;

import com.gogidix.analytics.bi.application.service.DashboardCommandService;
import com.gogidix.analytics.bi.domain.model.Dashboard;
import com.gogidix.analytics.bi.domain.model.DashboardWidget;
import com.gogidix.analytics.bi.domain.port.in.CreateDashboardCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardCommandService commandService;

    @InjectMocks
    private DashboardController controller;

    private Dashboard testDashboard;
    private CreateDashboardCommand testCommand;

    @BeforeEach
    void setUp() {
        testDashboard = Dashboard.builder()
            .id("dash-1")
            .name("Test Dashboard")
            .ownerId("user-1")
            .tenantId("tenant-1")
            .widgets(new ArrayList<>())
            .build();

        testCommand = CreateDashboardCommand.builder()
            .name("Test Dashboard")
            .ownerId("user-1")
            .build();
    }

    @Nested
    @DisplayName("POST /api/v1/dashboards")
    class CreateDashboardTests {

        @Test
        void createsDashboardAndReturns201() {
            when(commandService.createDashboard(any(CreateDashboardCommand.class))).thenReturn(testDashboard);

            ResponseEntity<Dashboard> response = controller.createDashboard(testCommand);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("dash-1", response.getBody().getId());
            verify(commandService).createDashboard(testCommand);
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/dashboards/{dashboardId}")
    class UpdateDashboardTests {

        @Test
        void updatesDashboardAndReturns200() {
            when(commandService.updateDashboard(eq("dash-1"), any(CreateDashboardCommand.class))).thenReturn(testDashboard);

            ResponseEntity<Dashboard> response = controller.updateDashboard("dash-1", testCommand);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("dash-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/dashboards/{dashboardId}")
    class DeleteDashboardTests {

        @Test
        void deletesDashboard() {
            controller.deleteDashboard("dash-1");
            verify(commandService).deleteDashboard("dash-1");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/dashboards/{dashboardId}/widgets")
    class AddWidgetTests {

        @Test
        void addsWidgetAndReturns201() {
            DashboardWidget widget = DashboardWidget.builder()
                .id("w-1")
                .widgetName("Chart")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(0)
                .build();

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .build();

            when(commandService.addWidget(eq("dash-1"), any(CreateDashboardCommand.WidgetCommand.class))).thenReturn(widget);

            ResponseEntity<DashboardWidget> response = controller.addWidget("dash-1", wc);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("w-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/dashboards/{dashboardId}/widgets/{widgetId}")
    class UpdateWidgetTests {

        @Test
        void updatesWidgetAndReturns200() {
            DashboardWidget widget = DashboardWidget.builder()
                .id("w-1")
                .widgetName("Updated Chart")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .position(0)
                .build();

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Updated Chart")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .build();

            when(commandService.updateWidget(eq("dash-1"), eq("w-1"), any(CreateDashboardCommand.WidgetCommand.class))).thenReturn(widget);

            ResponseEntity<DashboardWidget> response = controller.updateWidget("dash-1", "w-1", wc);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Updated Chart", response.getBody().getWidgetName());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/dashboards/{dashboardId}/widgets/{widgetId}")
    class DeleteWidgetTests {

        @Test
        void deletesWidget() {
            controller.deleteWidget("dash-1", "w-1");
            verify(commandService).deleteWidget("dash-1", "w-1");
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/dashboards/{dashboardId}/favorite")
    class ToggleFavoriteTests {

        @Test
        void togglesFavoriteAndReturns200() {
            testDashboard.setIsFavorite(true);
            when(commandService.toggleFavorite("dash-1")).thenReturn(testDashboard);

            ResponseEntity<Dashboard> response = controller.toggleFavorite("dash-1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().getIsFavorite());
        }
    }
}
