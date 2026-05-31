package com.gogidix.analytics.bi.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DashboardWidgetTest {

    private DashboardWidget widget;
    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        dashboard = Dashboard.builder()
            .name("Test Dashboard")
            .ownerId("user-1")
            .tenantId("tenant-1")
            .build();

        widget = DashboardWidget.builder()
            .widgetName("Revenue Chart")
            .widgetType(DashboardWidget.WidgetType.BAR_CHART)
            .position(0)
            .dashboard(dashboard)
            .build();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void builderCreatesWidgetWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            DashboardWidget w = DashboardWidget.builder()
                .id("id-1")
                .widgetName("Test Widget")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .position(1)
                .rowIndex(0)
                .columnIndex(1)
                .rowSpan(2)
                .columnSpan(3)
                .dataSource("{\"db\":\"analytics\"}")
                .visualizationConfig("{\"color\":\"blue\"}")
                .queryDefinition("SELECT SUM(revenue) FROM sales")
                .refreshIntervalSeconds(30)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .dashboard(dashboard)
                .build();

            assertEquals("id-1", w.getId());
            assertEquals("Test Widget", w.getWidgetName());
            assertEquals(DashboardWidget.WidgetType.LINE_CHART, w.getWidgetType());
            assertEquals(1, w.getPosition());
            assertEquals(0, w.getRowIndex());
            assertEquals(1, w.getColumnIndex());
            assertEquals(2, w.getRowSpan());
            assertEquals(3, w.getColumnSpan());
            assertEquals(30, w.getRefreshIntervalSeconds());
            assertTrue(w.getEnabled());
        }

        @Test
        void builderDefaults() {
            DashboardWidget w = DashboardWidget.builder()
                .widgetName("Test")
                .widgetType(DashboardWidget.WidgetType.TABLE)
                .position(0)
                .dashboard(dashboard)
                .build();

            assertEquals(1, w.getRowSpan());
            assertEquals(1, w.getColumnSpan());
            assertTrue(w.getEnabled());
        }
    }

    @Test
    void noArgsConstructor() {
        DashboardWidget w = new DashboardWidget();
        assertNotNull(w);
        assertNull(w.getWidgetName());
    }

    @Test
    void allArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        DashboardWidget w = new DashboardWidget("id", "name", DashboardWidget.WidgetType.PIE_CHART,
            0, 0, 0, 1, 1, "ds", "viz", "query", 60, true, now, now, dashboard);
        assertEquals("id", w.getId());
        assertEquals("name", w.getWidgetName());
    }

    @Test
    void gettersAndSetters() {
        widget.setId("new-id");
        assertEquals("new-id", widget.getId());

        widget.setWidgetName("New Name");
        assertEquals("New Name", widget.getWidgetName());

        widget.setWidgetType(DashboardWidget.WidgetType.PIE_CHART);
        assertEquals(DashboardWidget.WidgetType.PIE_CHART, widget.getWidgetType());

        widget.setPosition(5);
        assertEquals(5, widget.getPosition());

        widget.setRowIndex(2);
        assertEquals(2, widget.getRowIndex());

        widget.setColumnIndex(3);
        assertEquals(3, widget.getColumnIndex());

        widget.setRowSpan(4);
        assertEquals(4, widget.getRowSpan());

        widget.setColumnSpan(5);
        assertEquals(5, widget.getColumnSpan());

        widget.setDataSource("new-ds");
        assertEquals("new-ds", widget.getDataSource());

        widget.setVisualizationConfig("new-viz");
        assertEquals("new-viz", widget.getVisualizationConfig());

        widget.setQueryDefinition("SELECT 1");
        assertEquals("SELECT 1", widget.getQueryDefinition());

        widget.setRefreshIntervalSeconds(120);
        assertEquals(120, widget.getRefreshIntervalSeconds());

        widget.setEnabled(false);
        assertFalse(widget.getEnabled());

        LocalDateTime now = LocalDateTime.now();
        widget.setCreatedAt(now);
        assertEquals(now, widget.getCreatedAt());

        widget.setUpdatedAt(now);
        assertEquals(now, widget.getUpdatedAt());

        Dashboard newDashboard = Dashboard.builder().name("New").ownerId("o").tenantId("t").build();
        widget.setDashboard(newDashboard);
        assertEquals(newDashboard, widget.getDashboard());
    }

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        void onCreateSetsTimestamps() {
            assertNull(widget.getCreatedAt());
            assertNull(widget.getUpdatedAt());
            widget.onCreate();
            assertNotNull(widget.getCreatedAt());
            assertNotNull(widget.getUpdatedAt());
        }

        @Test
        void onCreateGeneratesIdWhenNull() {
            widget.setId(null);
            widget.onCreate();
            assertNotNull(widget.getId());
        }

        @Test
        void onCreatePreservesExistingId() {
            widget.setId("existing-id");
            widget.onCreate();
            assertEquals("existing-id", widget.getId());
        }

        @Test
        void onUpdateSetsUpdatedAt() {
            widget.onUpdate();
            assertNotNull(widget.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    class EnumTests {

        @Test
        void widgetTypeValues() {
            DashboardWidget.WidgetType[] types = DashboardWidget.WidgetType.values();
            assertEquals(16, types.length);

            assertEquals(DashboardWidget.WidgetType.LINE_CHART, DashboardWidget.WidgetType.valueOf("LINE_CHART"));
            assertEquals(DashboardWidget.WidgetType.BAR_CHART, DashboardWidget.WidgetType.valueOf("BAR_CHART"));
            assertEquals(DashboardWidget.WidgetType.PIE_CHART, DashboardWidget.WidgetType.valueOf("PIE_CHART"));
            assertEquals(DashboardWidget.WidgetType.DONUT_CHART, DashboardWidget.WidgetType.valueOf("DONUT_CHART"));
            assertEquals(DashboardWidget.WidgetType.AREA_CHART, DashboardWidget.WidgetType.valueOf("AREA_CHART"));
            assertEquals(DashboardWidget.WidgetType.SCATTER_PLOT, DashboardWidget.WidgetType.valueOf("SCATTER_PLOT"));
            assertEquals(DashboardWidget.WidgetType.TABLE, DashboardWidget.WidgetType.valueOf("TABLE"));
            assertEquals(DashboardWidget.WidgetType.SINGLE_VALUE, DashboardWidget.WidgetType.valueOf("SINGLE_VALUE"));
            assertEquals(DashboardWidget.WidgetType.GAUGE, DashboardWidget.WidgetType.valueOf("GAUGE"));
            assertEquals(DashboardWidget.WidgetType.HEATMAP, DashboardWidget.WidgetType.valueOf("HEATMAP"));
            assertEquals(DashboardWidget.WidgetType.FUNNEL, DashboardWidget.WidgetType.valueOf("FUNNEL"));
            assertEquals(DashboardWidget.WidgetType.TREEMAP, DashboardWidget.WidgetType.valueOf("TREEMAP"));
            assertEquals(DashboardWidget.WidgetType.GEOMAP, DashboardWidget.WidgetType.valueOf("GEOMAP"));
            assertEquals(DashboardWidget.WidgetType.NUMBER_CARD, DashboardWidget.WidgetType.valueOf("NUMBER_CARD"));
            assertEquals(DashboardWidget.WidgetType.PROGRESS_BAR, DashboardWidget.WidgetType.valueOf("PROGRESS_BAR"));
            assertEquals(DashboardWidget.WidgetType.SPARKLINE, DashboardWidget.WidgetType.valueOf("SPARKLINE"));
        }
    }

    @Nested
    @DisplayName("Equals Branch Coverage Tests")
    class EqualsBranchTests {

        private DashboardWidget createFullyPopulated() {
            LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30);
            Dashboard db = Dashboard.builder().id("db-1").name("DB").ownerId("o").tenantId("t").build();
            return DashboardWidget.builder()
                .id("id-1")
                .widgetName("Widget")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(0)
                .rowIndex(1)
                .columnIndex(2)
                .rowSpan(3)
                .columnSpan(4)
                .dataSource("ds")
                .visualizationConfig("vc")
                .queryDefinition("qd")
                .refreshIntervalSeconds(60)
                .enabled(true)
                .createdAt(now)
                .updatedAt(now)
                .dashboard(db)
                .build();
        }

        @Test
        void equalsSameInstance() {
            DashboardWidget w = createFullyPopulated();
            assertEquals(w, w);
        }

        @Test
        void equalsNull() {
            assertNotEquals(createFullyPopulated(), null);
        }

        @Test
        void equalsDifferentType() {
            assertNotEquals(createFullyPopulated(), "string");
        }

        @Test
        void equalsIdenticalObjects() {
            assertEquals(createFullyPopulated(), createFullyPopulated());
        }

        @Test
        void equalsDifferentId() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentWidgetName() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setWidgetName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentWidgetType() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setWidgetType(DashboardWidget.WidgetType.PIE_CHART);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentPosition() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setPosition(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRowIndex() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setRowIndex(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentColumnIndex() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setColumnIndex(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRowSpan() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setRowSpan(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentColumnSpan() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setColumnSpan(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDataSource() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentVisualizationConfig() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setVisualizationConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentQueryDefinition() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRefreshIntervalSeconds() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setRefreshIntervalSeconds(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentEnabled() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setEnabled(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCreatedAt() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentUpdatedAt() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setUpdatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDashboard() {
            DashboardWidget a = createFullyPopulated();
            DashboardWidget b = createFullyPopulated();
            b.setDashboard(Dashboard.builder().id("other").name("X").ownerId("o").tenantId("t").build());
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            DashboardWidget a = new DashboardWidget();
            DashboardWidget b = new DashboardWidget();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            DashboardWidget w = createFullyPopulated();
            int h1 = w.hashCode();
            int h2 = w.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            DashboardWidget w = new DashboardWidget();
            assertNotNull(w.hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            DashboardWidget w = createFullyPopulated();
            assertNotNull(w.toString());
            assertTrue(w.toString().contains("Widget"));
        }

        @Test
        void toStringWithNullFields() {
            DashboardWidget w = new DashboardWidget();
            assertNotNull(w.toString());
        }
    }
}
