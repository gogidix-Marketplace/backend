package com.gogidix.aiservices.aianalyticsdashboard.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.CreateDashboardRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.UpdateDashboardRequest;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.DashboardResponse;
import com.gogidix.aiservices.aianalyticsdashboard.application.service.DashboardService;
import com.gogidix.aiservices.aianalyticsdashboard.shared.exception.DashboardNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@DisplayName("Dashboard Controller Interface Tests")
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DashboardService dashboardService;

    private static final String DASHBOARD_ID = "660e8400-e29b-41d4-a716-446655440000";
    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Dashboard CRUD Endpoints")
    class CrudEndpoints {

        @Test
        @DisplayName("POST /api/v1/analytics/dashboards - Should create dashboard")
        void shouldCreateDashboard() throws Exception {
            CreateDashboardRequest request = CreateDashboardRequest.builder()
                    .name("Sales Dashboard")
                    .description("Sales analytics")
                    .build();

            DashboardResponse response = DashboardResponse.builder()
                    .dashboardId(DASHBOARD_ID)
                    .name("Sales Dashboard")
                    .build();

            when(dashboardService.createDashboard(any(), eq(USER_ID))).thenReturn(response);

            mockMvc.perform(post("/api/v1/analytics/dashboards")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.dashboardId").exists())
                    .andExpect(jsonPath("$.name").value("Sales Dashboard"));

            verify(dashboardService).createDashboard(any(), eq(USER_ID));
        }

        @Test
        @DisplayName("POST /api/v1/analytics/dashboards - Should reject invalid request")
        void shouldRejectInvalidRequest() throws Exception {
            Map<String, Object> request = Map.of("description", "No name provided");

            mockMvc.perform(post("/api/v1/analytics/dashboards")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(dashboardService, never()).createDashboard(any(), any());
        }

        @Test
        @DisplayName("GET /api/v1/analytics/dashboards/{id} - Should get dashboard")
        void shouldGetDashboard() throws Exception {
            DashboardResponse response = DashboardResponse.builder()
                    .dashboardId(DASHBOARD_ID)
                    .name("Test Dashboard")
                    .build();

            when(dashboardService.getDashboard(DASHBOARD_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/analytics/dashboards/{id}", DASHBOARD_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.dashboardId").value(DASHBOARD_ID))
                    .andExpect(jsonPath("$.name").value("Test Dashboard"));
        }

        @Test
        @DisplayName("GET /api/v1/analytics/dashboards/{id} - Should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(dashboardService.getDashboard(DASHBOARD_ID))
                    .thenThrow(new DashboardNotFoundException(DASHBOARD_ID));

            mockMvc.perform(get("/api/v1/analytics/dashboards/{id}", DASHBOARD_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("PUT /api/v1/analytics/dashboards/{id} - Should update dashboard")
        void shouldUpdateDashboard() throws Exception {
            UpdateDashboardRequest request = UpdateDashboardRequest.builder()
                    .name("Updated Name")
                    .build();

            DashboardResponse response = DashboardResponse.builder()
                    .dashboardId(DASHBOARD_ID)
                    .name("Updated Name")
                    .build();

            when(dashboardService.updateDashboard(eq(DASHBOARD_ID), any())).thenReturn(response);

            mockMvc.perform(put("/api/v1/analytics/dashboards/{id}", DASHBOARD_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Name"));
        }

        @Test
        @DisplayName("DELETE /api/v1/analytics/dashboards/{id} - Should delete dashboard")
        void shouldDeleteDashboard() throws Exception {
            doNothing().when(dashboardService).deleteDashboard(DASHBOARD_ID);

            mockMvc.perform(delete("/api/v1/analytics/dashboards/{id}", DASHBOARD_ID))
                    .andExpect(status().isNoContent());

            verify(dashboardService).deleteDashboard(DASHBOARD_ID);
        }
    }

    @Nested
    @DisplayName("Dashboard Query Endpoints")
    class QueryEndpoints {

        @Test
        @DisplayName("GET /api/v1/analytics/dashboards - Should get user dashboards")
        void shouldGetUserDashboards() throws Exception {
            List<DashboardResponse> dashboards = List.of(
                    DashboardResponse.builder().dashboardId(DASHBOARD_ID).name("Dashboard 1").build()
            );

            when(dashboardService.getUserDashboards(USER_ID)).thenReturn(dashboards);

            mockMvc.perform(get("/api/v1/analytics/dashboards")
                            .header("X-User-Id", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].name").value("Dashboard 1"));
        }

        @Test
        @DisplayName("GET /api/v1/analytics/dashboards/public - Should get public dashboards")
        void shouldGetPublicDashboards() throws Exception {
            List<DashboardResponse> dashboards = List.of(
                    DashboardResponse.builder().dashboardId(DASHBOARD_ID).name("Public Dashboard").build()
            );

            when(dashboardService.getPublicDashboards()).thenReturn(dashboards);

            mockMvc.perform(get("/api/v1/analytics/dashboards/public"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Public Dashboard"));
        }
    }

    @Nested
    @DisplayName("Widget Management Endpoints")
    class WidgetEndpoints {

        @Test
        @DisplayName("POST /api/v1/analytics/dashboards/{id}/widgets - Should add widget")
        void shouldAddWidget() throws Exception {
            Map<String, Object> request = Map.of(
                    "type", "METRIC",
                    "title", "Total Revenue",
                    "dataSource", "revenue-metric"
            );

            when(dashboardService.addWidget(eq(DASHBOARD_ID), any()))
                    .thenReturn(any());

            mockMvc.perform(post("/api/v1/analytics/dashboards/{id}/widgets", DASHBOARD_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(dashboardService).addWidget(eq(DASHBOARD_ID), any());
        }

        @Test
        @DisplayName("DELETE /api/v1/analytics/dashboards/{id}/widgets/{widgetId} - Should remove widget")
        void shouldRemoveWidget() throws Exception {
            doNothing().when(dashboardService).removeWidget(DASHBOARD_ID, "widget-1");

            mockMvc.perform(delete("/api/v1/analytics/dashboards/{id}/widgets/{widgetId}",
                            DASHBOARD_ID, "widget-1"))
                    .andExpect(status().isNoContent());

            verify(dashboardService).removeWidget(DASHBOARD_ID, "widget-1");
        }
    }

    @Nested
    @DisplayName("Dashboard Export/Import Endpoints")
    class ExportImportEndpoints {

        @Test
        @DisplayName("GET /api/v1/analytics/dashboards/{id}/export - Should export dashboard")
        void shouldExportDashboard() throws Exception {
            when(dashboardService.exportDashboard(DASHBOARD_ID))
                    .thenReturn(Map.of("name", "Test", "widgets", List.of()));

            mockMvc.perform(get("/api/v1/analytics/dashboards/{id}/export", DASHBOARD_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").exists());
        }

        @Test
        @DisplayName("POST /api/v1/analytics/dashboards/import - Should import dashboard")
        void shouldImportDashboard() throws Exception {
            Map<String, Object> config = Map.of(
                    "name", "Imported Dashboard",
                    "description", "Imported"
            );

            when(dashboardService.importDashboard(any(), eq(USER_ID)))
                    .thenReturn(any());

            mockMvc.perform(post("/api/v1/analytics/dashboards/import")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(config)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle dashboard not found")
        void shouldHandleNotFound() throws Exception {
            when(dashboardService.getDashboard(DASHBOARD_ID))
                    .thenThrow(new DashboardNotFoundException(DASHBOARD_ID));

            mockMvc.perform(get("/api/v1/analytics/dashboards/{id}", DASHBOARD_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }
    }
}
