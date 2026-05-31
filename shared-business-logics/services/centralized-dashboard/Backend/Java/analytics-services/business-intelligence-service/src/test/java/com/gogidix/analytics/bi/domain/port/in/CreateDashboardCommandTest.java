package com.gogidix.analytics.bi.domain.port.in;

import com.gogidix.analytics.bi.domain.model.DashboardWidget;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreateDashboardCommandTest {

    @Nested
    @DisplayName("CreateDashboardCommand Tests")
    class CommandTests {

        @Test
        void builderCreatesCommandWithAllFields() {
            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Chart 1")
                .widgetType(DashboardWidget.WidgetType.LINE_CHART)
                .position(0)
                .rowIndex(0)
                .columnIndex(0)
                .rowSpan(2)
                .columnSpan(3)
                .dataSource("{\"db\":\"main\"}")
                .visualizationConfig("{\"color\":\"red\"}")
                .queryDefinition("SELECT * FROM data")
                .refreshIntervalSeconds(60)
                .enabled(true)
                .build();

            CreateDashboardCommand cmd = CreateDashboardCommand.builder()
                .name("Test Dashboard")
                .description("A test dashboard")
                .category("Analytics")
                .ownerId("user-1")
                .isPublic(true)
                .refreshIntervalSeconds(30)
                .layoutConfig("{\"layout\":\"grid\"}")
                .theme("dark")
                .tags("tag1,tag2")
                .widgets(Arrays.asList(wc))
                .build();

            assertEquals("Test Dashboard", cmd.getName());
            assertEquals("A test dashboard", cmd.getDescription());
            assertEquals("Analytics", cmd.getCategory());
            assertEquals("user-1", cmd.getOwnerId());
            assertTrue(cmd.getIsPublic());
            assertEquals(30, cmd.getRefreshIntervalSeconds());
            assertEquals("{\"layout\":\"grid\"}", cmd.getLayoutConfig());
            assertEquals("dark", cmd.getTheme());
            assertEquals("tag1,tag2", cmd.getTags());
            assertEquals(1, cmd.getWidgets().size());
        }

        @Test
        void builderDefaults() {
            CreateDashboardCommand cmd = CreateDashboardCommand.builder()
                .name("Test")
                .ownerId("owner")
                .build();

            assertFalse(cmd.getIsPublic());
            assertEquals("default", cmd.getTheme());
        }

        @Test
        void noArgsConstructor() {
            CreateDashboardCommand cmd = new CreateDashboardCommand();
            assertNotNull(cmd);
            assertNull(cmd.getName());
        }

        @Test
        void allArgsConstructor() {
            CreateDashboardCommand cmd = new CreateDashboardCommand("name", "desc", "cat",
                "owner", true, 30, "layout", "dark", "tags", null);
            assertEquals("name", cmd.getName());
        }

        @Test
        void gettersAndSetters() {
            CreateDashboardCommand cmd = new CreateDashboardCommand();
            cmd.setName("New Name");
            assertEquals("New Name", cmd.getName());

            cmd.setDescription("Desc");
            assertEquals("Desc", cmd.getDescription());

            cmd.setCategory("Cat");
            assertEquals("Cat", cmd.getCategory());

            cmd.setOwnerId("owner-2");
            assertEquals("owner-2", cmd.getOwnerId());

            cmd.setIsPublic(true);
            assertTrue(cmd.getIsPublic());

            cmd.setRefreshIntervalSeconds(120);
            assertEquals(120, cmd.getRefreshIntervalSeconds());

            cmd.setLayoutConfig("layout");
            assertEquals("layout", cmd.getLayoutConfig());

            cmd.setTheme("blue");
            assertEquals("blue", cmd.getTheme());

            cmd.setTags("new-tags");
            assertEquals("new-tags", cmd.getTags());

            List<CreateDashboardCommand.WidgetCommand> widgets = Arrays.asList(
                CreateDashboardCommand.WidgetCommand.builder().widgetName("w").widgetType(DashboardWidget.WidgetType.TABLE).build()
            );
            cmd.setWidgets(widgets);
            assertEquals(1, cmd.getWidgets().size());
        }
    }

    @Nested
    @DisplayName("WidgetCommand Tests")
    class WidgetCommandTests {

        @Test
        void widgetCommandBuilderCreatesWithAllFields() {
            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Test Widget")
                .widgetType(DashboardWidget.WidgetType.GAUGE)
                .position(3)
                .rowIndex(1)
                .columnIndex(2)
                .rowSpan(2)
                .columnSpan(3)
                .dataSource("ds")
                .visualizationConfig("vc")
                .queryDefinition("qd")
                .refreshIntervalSeconds(45)
                .enabled(false)
                .build();

            assertEquals("Test Widget", wc.getWidgetName());
            assertEquals(DashboardWidget.WidgetType.GAUGE, wc.getWidgetType());
            assertEquals(3, wc.getPosition());
            assertEquals(1, wc.getRowIndex());
            assertEquals(2, wc.getColumnIndex());
            assertEquals(2, wc.getRowSpan());
            assertEquals(3, wc.getColumnSpan());
            assertEquals("ds", wc.getDataSource());
            assertEquals("vc", wc.getVisualizationConfig());
            assertEquals("qd", wc.getQueryDefinition());
            assertEquals(45, wc.getRefreshIntervalSeconds());
            assertFalse(wc.getEnabled());
        }

        @Test
        void widgetCommandDefaults() {
            CreateDashboardCommand.WidgetCommand wc = CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Test")
                .widgetType(DashboardWidget.WidgetType.TABLE)
                .build();

            assertTrue(wc.getEnabled());
        }

        @Test
        void widgetCommandNoArgsAndAllArgs() {
            CreateDashboardCommand.WidgetCommand wc = new CreateDashboardCommand.WidgetCommand();
            assertNull(wc.getWidgetName());

            CreateDashboardCommand.WidgetCommand wc2 = new CreateDashboardCommand.WidgetCommand(
                "name", DashboardWidget.WidgetType.PIE_CHART, 0, 0, 0, 1, 1,
                "ds", "vc", "qd", 60, true);
            assertEquals("name", wc2.getWidgetName());
        }

        @Test
        void widgetCommandGettersAndSetters() {
            CreateDashboardCommand.WidgetCommand wc = new CreateDashboardCommand.WidgetCommand();
            wc.setWidgetName("Name");
            assertEquals("Name", wc.getWidgetName());

            wc.setWidgetType(DashboardWidget.WidgetType.HEATMAP);
            assertEquals(DashboardWidget.WidgetType.HEATMAP, wc.getWidgetType());

            wc.setPosition(5);
            assertEquals(5, wc.getPosition());

            wc.setRowIndex(2);
            assertEquals(2, wc.getRowIndex());

            wc.setColumnIndex(3);
            assertEquals(3, wc.getColumnIndex());

            wc.setRowSpan(4);
            assertEquals(4, wc.getRowSpan());

            wc.setColumnSpan(5);
            assertEquals(5, wc.getColumnSpan());

            wc.setDataSource("ds");
            assertEquals("ds", wc.getDataSource());

            wc.setVisualizationConfig("vc");
            assertEquals("vc", wc.getVisualizationConfig());

            wc.setQueryDefinition("qd");
            assertEquals("qd", wc.getQueryDefinition());

            wc.setRefreshIntervalSeconds(30);
            assertEquals(30, wc.getRefreshIntervalSeconds());

            wc.setEnabled(false);
            assertFalse(wc.getEnabled());
        }
    }

    @Nested
    @DisplayName("CreateDashboardCommand Equals Branch Coverage")
    class CommandEqualsTests {

        private CreateDashboardCommand createFullyPopulated() {
            return CreateDashboardCommand.builder()
                .name("Name")
                .description("Desc")
                .category("Cat")
                .ownerId("owner-1")
                .isPublic(true)
                .refreshIntervalSeconds(60)
                .layoutConfig("layout")
                .theme("dark")
                .tags("tags")
                .widgets(Arrays.asList())
                .build();
        }

        @Test
        void equalsSameInstance() {
            CreateDashboardCommand c = createFullyPopulated();
            assertEquals(c, c);
        }

        @Test
        void equalsNull() {
            assertNotEquals(createFullyPopulated(), null);
        }

        @Test
        void equalsDifferentType() {
            assertNotEquals(createFullyPopulated(), "str");
        }

        @Test
        void equalsIdenticalObjects() {
            assertEquals(createFullyPopulated(), createFullyPopulated());
        }

        @Test
        void equalsDifferentName() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDescription() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCategory() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setCategory("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOwnerId() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentIsPublic() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setIsPublic(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRefreshIntervalSeconds() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setRefreshIntervalSeconds(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentLayoutConfig() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setLayoutConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTheme() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setTheme("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTags() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setTags("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentWidgets() {
            CreateDashboardCommand a = createFullyPopulated();
            CreateDashboardCommand b = createFullyPopulated();
            b.setWidgets(Arrays.asList(CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("w").widgetType(DashboardWidget.WidgetType.TABLE).build()));
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            CreateDashboardCommand a = new CreateDashboardCommand();
            CreateDashboardCommand b = new CreateDashboardCommand();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            CreateDashboardCommand c = createFullyPopulated();
            assertEquals(c.hashCode(), c.hashCode());
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            assertNotNull(new CreateDashboardCommand().hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            assertNotNull(createFullyPopulated().toString());
        }

        @Test
        void toStringWithNullFields() {
            assertNotNull(new CreateDashboardCommand().toString());
        }
    }

    @Nested
    @DisplayName("WidgetCommand Equals Branch Coverage")
    class WidgetCommandEqualsTests {

        private CreateDashboardCommand.WidgetCommand createFullyPopulated() {
            return CreateDashboardCommand.WidgetCommand.builder()
                .widgetName("Name")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(1)
                .rowIndex(2)
                .columnIndex(3)
                .rowSpan(4)
                .columnSpan(5)
                .dataSource("ds")
                .visualizationConfig("vc")
                .queryDefinition("qd")
                .refreshIntervalSeconds(60)
                .enabled(true)
                .build();
        }

        @Test
        void equalsSameInstance() {
            CreateDashboardCommand.WidgetCommand w = createFullyPopulated();
            assertEquals(w, w);
        }

        @Test
        void equalsNull() {
            assertNotEquals(createFullyPopulated(), null);
        }

        @Test
        void equalsDifferentType() {
            assertNotEquals(createFullyPopulated(), 42);
        }

        @Test
        void equalsIdenticalObjects() {
            assertEquals(createFullyPopulated(), createFullyPopulated());
        }

        @Test
        void equalsDifferentWidgetName() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setWidgetName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentWidgetType() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setWidgetType(DashboardWidget.WidgetType.PIE_CHART);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentPosition() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setPosition(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRowIndex() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setRowIndex(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentColumnIndex() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setColumnIndex(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRowSpan() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setRowSpan(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentColumnSpan() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setColumnSpan(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDataSource() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentVisualizationConfig() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setVisualizationConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentQueryDefinition() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRefreshIntervalSeconds() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setRefreshIntervalSeconds(99);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentEnabled() {
            CreateDashboardCommand.WidgetCommand a = createFullyPopulated();
            CreateDashboardCommand.WidgetCommand b = createFullyPopulated();
            b.setEnabled(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            CreateDashboardCommand.WidgetCommand a = new CreateDashboardCommand.WidgetCommand();
            CreateDashboardCommand.WidgetCommand b = new CreateDashboardCommand.WidgetCommand();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            CreateDashboardCommand.WidgetCommand w = createFullyPopulated();
            assertEquals(w.hashCode(), w.hashCode());
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            assertNotNull(new CreateDashboardCommand.WidgetCommand().hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            assertNotNull(createFullyPopulated().toString());
        }

        @Test
        void toStringWithNullFields() {
            assertNotNull(new CreateDashboardCommand.WidgetCommand().toString());
        }
    }
}
