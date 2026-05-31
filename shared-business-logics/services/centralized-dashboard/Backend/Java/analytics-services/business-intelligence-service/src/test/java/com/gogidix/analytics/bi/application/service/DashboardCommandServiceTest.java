package com.gogidix.analytics.bi.application.service;

import com.gogidix.analytics.bi.domain.model.Dashboard;
import com.gogidix.analytics.bi.domain.model.DashboardWidget;
import com.gogidix.analytics.bi.domain.port.in.CreateDashboardCommand;
import com.gogidix.analytics.bi.domain.repository.DashboardRepository;
import com.gogidix.analytics.bi.domain.repository.DashboardWidgetRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardCommandServiceTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @Mock
    private DashboardWidgetRepository widgetRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private DashboardCommandService service;

    private MockedStatic<RequestContext> requestContextMock;

    @BeforeEach
    void setUp() {
        requestContextMock = mockStatic(RequestContext.class);
    }

    @AfterEach
    void tearDown() {
        requestContextMock.close();
    }

    private void setTenantId(String tenantId) {
        requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(tenantId);
    }

    private Dashboard createTestDashboard() {
        return Dashboard.builder()
            .id("dash-1")
            .name("Test Dashboard")
            .description("A test dashboard")
            .category("Analytics")
            .ownerId("user-1")
            .tenantId("tenant-1")
            .isPublic(false)
            .isFavorite(false)
            .viewCount(0)
            .widgets(new ArrayList<>())
            .build();
    }

    private CreateDashboardCommand createTestCommand() {
        return CreateDashboardCommand.builder()
            .name("New Dashboard")
            .description("A new dashboard")
            .category("Sales")
            .ownerId("user-1")
            .isPublic(true)
            .refreshIntervalSeconds(60)
            .layoutConfig("{\"layout\":\"grid\"}")
            .theme("dark")
            .tags("tag1,tag2")
            .build();
    }

    @Nested
    @DisplayName("createDashboard Tests")
    class CreateDashboardTests {

        @Test
        void createsDashboardSuccessfully() {
            setTenantId("tenant-1");
            when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(inv -> inv.getArgument(0));

            Dashboard result = service.createDashboard(createTestCommand());

            assertNotNull(result);
            assertEquals("New Dashboard", result.getName());
            assertEquals("tenant-1", result.getTenantId());
            verify(dashboardRepository, times(2)).save(any(Dashboard.class));
            verify(auditService).logEvent(eq("DASHBOARD_CREATED"), eq("Dashboard"), any(), contains("Created dashboard"));
        }

        @Test
        void createsDashboardWithWidgets() {
            setTenantId("tenant-1");
            when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart 1")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(0)
                .rowIndex(0)
                .columnIndex(0)
                .dataSource("{\"db\":\"main\"}")
                .visualizationConfig("{\"color\":\"blue\"}")
                .enabled(true)
                .build();

            CreateDashboardCommand cmd = CreateDashboardCommand.builder()
                .name("Dashboard With Widgets")
                .ownerId("user-1")
                .widgets(Arrays.asList(wc))
                .build();

            Dashboard result = service.createDashboard(cmd);

            assertNotNull(result);
            assertEquals(1, result.getWidgets().size());
            assertEquals("Chart 1", result.getWidgets().get(0).getWidgetName());
        }

        @Test
        void createsDashboardWithWidgetNullPosition() {
            setTenantId("tenant-1");
            when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .position(null)
                .rowSpan(null)
                .columnSpan(null)
                .enabled(true)
                .build();

            CreateDashboardCommand cmd = CreateDashboardCommand.builder()
                .name("Test")
                .ownerId("user-1")
                .widgets(Arrays.asList(wc))
                .build();

            Dashboard result = service.createDashboard(cmd);
            assertEquals(0, result.getWidgets().get(0).getPosition());
            assertEquals(1, result.getWidgets().get(0).getRowSpan());
            assertEquals(1, result.getWidgets().get(0).getColumnSpan());
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);

            assertThrows(ValidationException.class, () -> service.createDashboard(createTestCommand()));
        }
    }

    @Nested
    @DisplayName("updateDashboard Tests")
    class UpdateDashboardTests {

        @Test
        void updatesDashboardSuccessfully() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            Dashboard result = service.updateDashboard("dash-1", createTestCommand());

            assertNotNull(result);
            assertEquals("New Dashboard", result.getName());
            assertEquals("A new dashboard", result.getDescription());
            assertEquals("Sales", result.getCategory());
            verify(auditService).logEvent(eq("DASHBOARD_UPDATED"), eq("Dashboard"), eq("dash-1"), anyString());
        }

        @Test
        void throwsWhenDashboardNotFound() {
            setTenantId("tenant-1");
            when(dashboardRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                () -> service.updateDashboard("nonexistent", createTestCommand()));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class,
                () -> service.updateDashboard("dash-1", createTestCommand()));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);

            assertThrows(ValidationException.class,
                () -> service.updateDashboard("dash-1", createTestCommand()));
        }
    }

    @Nested
    @DisplayName("deleteDashboard Tests")
    class DeleteDashboardTests {

        @Test
        void deletesDashboardSuccessfully() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            service.deleteDashboard("dash-1");

            verify(widgetRepository).deleteByDashboardId("dash-1");
            verify(dashboardRepository).delete(existing);
            verify(auditService).logEvent(eq("DASHBOARD_DELETED"), eq("Dashboard"), eq("dash-1"), anyString());
        }

        @Test
        void throwsWhenDashboardNotFound() {
            setTenantId("tenant-1");
            when(dashboardRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.deleteDashboard("nonexistent"));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class, () -> service.deleteDashboard("dash-1"));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.deleteDashboard("dash-1"));
        }
    }

    @Nested
    @DisplayName("addWidget Tests")
    class AddWidgetTests {

        @Test
        void addsWidgetSuccessfully() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("New Chart")
                .widgetType(DashboardWidget.WidgetType.PIE_CHART)
                .position(2)
                .rowIndex(0)
                .columnIndex(1)
                .rowSpan(2)
                .columnSpan(3)
                .dataSource("ds")
                .visualizationConfig("vc")
                .queryDefinition("qd")
                .refreshIntervalSeconds(30)
                .enabled(true)
                .build();

            DashboardWidget result = service.addWidget("dash-1", wc);

            assertNotNull(result);
            assertEquals("New Chart", result.getWidgetName());
            assertEquals(existing, result.getDashboard());
            verify(auditService).logEvent(eq("WIDGET_ADDED"), eq("DashboardWidget"), any(), anyString());
        }

        @Test
        void addsWidgetWithNullPositionUsesWidgetsSize() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            DashboardWidget existingWidget = DashboardWidget.builder()
                .id("w-1").widgetName("Old").widgetType(DashboardWidget.WidgetType.TABLE).position(0).build();
            existing.addWidget(existingWidget);

            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("New")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .position(null)
                .rowSpan(null)
                .columnSpan(null)
                .enabled(true)
                .build();

            DashboardWidget result = service.addWidget("dash-1", wc);
            assertEquals(1, result.getPosition());
            assertEquals(1, result.getRowSpan());
            assertEquals(1, result.getColumnSpan());
        }

        @Test
        void throwsWhenDashboardNotFound() {
            setTenantId("tenant-1");
            when(dashboardRepository.findById("nonexistent")).thenReturn(Optional.empty());

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();

            assertThrows(NotFoundException.class, () -> service.addWidget("nonexistent", wc));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();

            assertThrows(ValidationException.class, () -> service.addWidget("dash-1", wc));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();
            assertThrows(ValidationException.class, () -> service.addWidget("dash-1", wc));
        }
    }

    @Nested
    @DisplayName("updateWidget Tests")
    class UpdateWidgetTests {

        @Test
        void updatesWidgetSuccessfully() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            DashboardWidget widget = DashboardWidget.builder()
                .id("w-1").widgetName("Old Chart").widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(0).rowIndex(0).columnIndex(0).rowSpan(1).columnSpan(1).enabled(true).build();
            existing.addWidget(widget);

            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Updated Chart")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .position(5)
                .rowIndex(1)
                .columnIndex(2)
                .rowSpan(3)
                .columnSpan(4)
                .dataSource("new-ds")
                .visualizationConfig("new-vc")
                .queryDefinition("new-qd")
                .refreshIntervalSeconds(60)
                .enabled(false)
                .build();

            DashboardWidget result = service.updateWidget("dash-1", "w-1", wc);

            assertEquals("Updated Chart", result.getWidgetName());
            assertEquals(DashboardWidget.WidgetType.LINE_CHART, result.getWidgetType());
            assertEquals(5, result.getPosition());
            assertEquals(1, result.getRowIndex());
            assertEquals(2, result.getColumnIndex());
            assertEquals(3, result.getRowSpan());
            assertEquals(4, result.getColumnSpan());
            assertFalse(result.getEnabled());
            verify(auditService).logEvent(eq("WIDGET_UPDATED"), eq("DashboardWidget"), eq("w-1"), anyString());
        }

        @Test
        void updatesWidgetWithNullOptionals() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            DashboardWidget widget = DashboardWidget.builder()
                .id("w-1").widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE)
                .position(0).rowIndex(0).columnIndex(0).rowSpan(1).columnSpan(1).enabled(true).build();
            existing.addWidget(widget);

            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Updated")
                .widgetType(DashboardWidget.WidgetType.TABLE)
                .position(null)
                .rowIndex(null)
                .columnIndex(null)
                .rowSpan(null)
                .columnSpan(null)
                .enabled(true)
                .build();

            DashboardWidget result = service.updateWidget("dash-1", "w-1", wc);
            assertEquals(0, result.getPosition());
            assertEquals(0, result.getRowIndex());
            assertEquals(0, result.getColumnIndex());
            assertEquals(1, result.getRowSpan());
            assertEquals(1, result.getColumnSpan());
        }

        @Test
        void throwsWhenWidgetNotFound() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();

            assertThrows(NotFoundException.class, () -> service.updateWidget("dash-1", "w-missing", wc));
        }

        @Test
        void throwsWhenDashboardNotFound() {
            setTenantId("tenant-1");
            when(dashboardRepository.findById("nonexistent")).thenReturn(Optional.empty());

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();

            assertThrows(NotFoundException.class, () -> service.updateWidget("nonexistent", "w-1", wc));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();

            assertThrows(ValidationException.class, () -> service.updateWidget("dash-1", "w-1", wc));
        }
    }

    @Nested
    @DisplayName("deleteWidget Tests")
    class DeleteWidgetTests {

        @Test
        void deletesWidgetSuccessfully() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            DashboardWidget widget = DashboardWidget.builder()
                .id("w-1").widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).position(0).build();
            existing.addWidget(widget);

            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            service.deleteWidget("dash-1", "w-1");

            assertTrue(existing.getWidgets().isEmpty());
            verify(auditService).logEvent(eq("WIDGET_DELETED"), eq("DashboardWidget"), eq("w-1"), anyString());
        }

        @Test
        void throwsWhenWidgetNotFound() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            assertThrows(NotFoundException.class, () -> service.deleteWidget("dash-1", "w-missing"));
        }

        @Test
        void throwsWhenDashboardNotFound() {
            setTenantId("tenant-1");
            when(dashboardRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.deleteWidget("nonexistent", "w-1"));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class, () -> service.deleteWidget("dash-1", "w-1"));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.deleteWidget("dash-1", "w-1"));
        }
    }

    @Nested
    @DisplayName("toggleFavorite Tests")
    class ToggleFavoriteTests {

        @Test
        void togglesFavoriteFromFalseToTrue() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            existing.setIsFavorite(false);
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            Dashboard result = service.toggleFavorite("dash-1");

            assertTrue(result.getIsFavorite());
        }

        @Test
        void togglesFavoriteFromTrueToFalse() {
            setTenantId("tenant-1");
            Dashboard existing = createTestDashboard();
            existing.setIsFavorite(true);
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));
            when(dashboardRepository.save(any(Dashboard.class))).thenReturn(existing);

            Dashboard result = service.toggleFavorite("dash-1");

            assertFalse(result.getIsFavorite());
        }

        @Test
        void throwsWhenDashboardNotFound() {
            setTenantId("tenant-1");
            when(dashboardRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.toggleFavorite("nonexistent"));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            Dashboard existing = createTestDashboard();
            when(dashboardRepository.findById("dash-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class, () -> service.toggleFavorite("dash-1"));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.toggleFavorite("dash-1"));
        }
    }

    @Nested
    @DisplayName("updateWidget Null Tenant Tests")
    class UpdateWidgetNullTenantTests {

        @Test
        void throwsWhenTenantIdIsNullOnUpdateWidget() {
            setTenantId(null);
            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart").widgetType(DashboardWidget.WidgetType.TABLE).build();
            assertThrows(ValidationException.class, () -> service.updateWidget("dash-1", "w-1", wc));
        }
    }

    @Nested
    @DisplayName("createDashboard Empty Widgets Tests")
    class CreateDashboardEmptyWidgetsTests {

        @Test
        void createsDashboardWithEmptyWidgetsList() {
            setTenantId("tenant-1");
            when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateDashboardCommand cmd = CreateDashboardCommand.builder()
                .name("Test")
                .ownerId("user-1")
                .widgets(Arrays.asList())
                .build();

            Dashboard result = service.createDashboard(cmd);
            assertNotNull(result);
            assertTrue(result.getWidgets().isEmpty());
        }

        @Test
        void createsDashboardWithNullWidgets() {
            setTenantId("tenant-1");
            when(dashboardRepository.save(any(Dashboard.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateDashboardCommand cmd = CreateDashboardCommand.builder()
                .name("Test")
                .ownerId("user-1")
                .widgets(null)
                .build();

            Dashboard result = service.createDashboard(cmd);
            assertNotNull(result);
        }
    }
}
