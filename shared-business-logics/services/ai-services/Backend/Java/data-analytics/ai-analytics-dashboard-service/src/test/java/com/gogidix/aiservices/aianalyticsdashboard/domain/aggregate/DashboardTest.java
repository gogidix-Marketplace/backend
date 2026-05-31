package com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate;

import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Widget;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.WidgetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dashboard Aggregate Domain Model Tests")
class DashboardTest {

    private static final String VALID_NAME = "Sales Dashboard";
    private static final String VALID_USER_ID = "550e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Dashboard Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create dashboard with valid parameters")
        void shouldCreateWithValidParameters() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            assertThat(dashboard).isNotNull();
            assertThat(dashboard.getName()).isEqualTo(VALID_NAME);
            assertThat(dashboard.getUserId()).isEqualTo(VALID_USER_ID);
            assertThat(dashboard.getDashboardId()).isNotNull();
            assertThat(dashboard.getCreatedAt()).isNotNull();
            assertThat(dashboard.getUpdatedAt()).isNotNull();
            assertThat(dashboard.getWidgets()).isEmpty();
            assertThat(dashboard.isPublic()).isFalse();
            assertThat(dashboard.getTheme()).isEqualTo("default");
            assertThat(dashboard.getRefreshInterval()).isEqualTo(300);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "  "})
        @DisplayName("Should reject invalid name")
        void shouldRejectInvalidName(String name) {
            assertThatThrownBy(() -> Dashboard.create(name, VALID_USER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Dashboard name cannot be null or empty");
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "  "})
        @DisplayName("Should reject invalid user ID")
        void shouldRejectInvalidUserId(String userId) {
            assertThatThrownBy(() -> Dashboard.create(VALID_NAME, userId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Dashboard Restore Tests")
    class RestoreTests {

        @Test
        @DisplayName("Should restore dashboard from persisted state")
        void shouldRestoreDashboard() {
            Widget widget = Widget.builder()
                    .widgetId("widget-1")
                    .type(WidgetType.CHART)
                    .title("Test Chart")
                    .dataSource("data-1")
                    .config(Map.of("key", "value"))
                    .build();

            Dashboard dashboard = Dashboard.restore(
                    "dash-1",
                    VALID_USER_ID,
                    VALID_NAME,
                    "Description",
                    List.of(widget),
                    600,
                    Instant.now(),
                    Instant.now(),
                    true,
                    "dark"
            );

            assertThat(dashboard.getDashboardId()).isEqualTo("dash-1");
            assertThat(dashboard.getName()).isEqualTo(VALID_NAME);
            assertThat(dashboard.getDescription()).isEqualTo("Description");
            assertThat(dashboard.getWidgets()).hasSize(1);
            assertThat(dashboard.getRefreshInterval()).isEqualTo(600);
            assertThat(dashboard.isPublic()).isTrue();
            assertThat(dashboard.getTheme()).isEqualTo("dark");
        }

        @Test
        @DisplayName("Should restore dashboard with null description")
        void shouldRestoreWithNullDescription() {
            Dashboard dashboard = Dashboard.restore(
                    "dash-1",
                    VALID_USER_ID,
                    VALID_NAME,
                    null,
                    List.of(),
                    300,
                    Instant.now(),
                    Instant.now(),
                    false,
                    "default"
            );

            assertThat(dashboard.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should restore dashboard with empty widgets")
        void shouldRestoreWithEmptyWidgets() {
            Dashboard dashboard = Dashboard.restore(
                    "dash-1",
                    VALID_USER_ID,
                    VALID_NAME,
                    null,
                    List.of(),
                    300,
                    Instant.now(),
                    Instant.now(),
                    false,
                    "default"
            );

            assertThat(dashboard.getWidgets()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Widget Management Tests")
    class WidgetManagementTests {

        @Test
        @DisplayName("Should add widget successfully")
        void shouldAddWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget = createTestWidget("widget-1");

            dashboard.addWidget(widget);

            assertThat(dashboard.getWidgets()).hasSize(1);
            assertThat(dashboard.getWidgetCount()).isEqualTo(1);
            assertThat(dashboard.isFull()).isFalse();
        }

        @Test
        @DisplayName("Should reject null widget")
        void shouldRejectNullWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            assertThatThrownBy(() -> dashboard.addWidget(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Widget cannot be null");
        }

        @Test
        @DisplayName("Should reject duplicate widget ID")
        void shouldRejectDuplicateWidgetId() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget1 = createTestWidget("widget-1");
            Widget widget2 = createTestWidget("widget-1");

            dashboard.addWidget(widget1);

            assertThatThrownBy(() -> dashboard.addWidget(widget2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already exists");
        }

        @Test
        @DisplayName("Should enforce maximum widgets limit")
        void shouldEnforceMaxWidgets() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            for (int i = 0; i < 20; i++) {
                dashboard.addWidget(createTestWidget("widget-" + i));
            }

            assertThat(dashboard.isFull()).isTrue();

            assertThatThrownBy(() -> dashboard.addWidget(createTestWidget("widget-21")))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("maximum");
        }

        @Test
        @DisplayName("Should remove widget by ID")
        void shouldRemoveWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget = createTestWidget("widget-1");
            dashboard.addWidget(widget);

            dashboard.removeWidget("widget-1");

            assertThat(dashboard.getWidgets()).isEmpty();
            assertThat(dashboard.getWidgetCount()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle removing non-existent widget")
        void shouldHandleRemovingNonExistentWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            // Should not throw, just do nothing
            dashboard.removeWidget("non-existent");

            assertThat(dashboard.getWidgets()).isEmpty();
        }

        @Test
        @DisplayName("Should update widget")
        void shouldUpdateWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget1 = createTestWidget("widget-1");
            dashboard.addWidget(widget1);

            Widget widget2 = Widget.builder()
                    .widgetId("widget-1")
                    .type(WidgetType.METRIC)
                    .title("Updated Widget")
                    .dataSource("new-data")
                    .config(Map.of("new", "config"))
                    .position(5)
                    .build();

            dashboard.updateWidget(widget2);

            assertThat(dashboard.getWidgets()).hasSize(1);
            assertThat(dashboard.getWidget("widget-1").getTitle()).isEqualTo("Updated Widget");
        }

        @Test
        @DisplayName("Should reject updating non-existent widget")
        void shouldRejectUpdatingNonExistentWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget = createTestWidget("widget-1");

            assertThatThrownBy(() -> dashboard.updateWidget(widget))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Widget not found");
        }

        @Test
        @DisplayName("Should get widget by ID")
        void shouldGetWidgetById() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget = createTestWidget("widget-1");
            dashboard.addWidget(widget);

            Widget found = dashboard.getWidget("widget-1");

            assertThat(found).isNotNull();
            assertThat(found.getWidgetId()).isEqualTo("widget-1");
        }

        @Test
        @DisplayName("Should return null when widget not found")
        void shouldReturnNullWhenWidgetNotFound() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            Widget found = dashboard.getWidget("non-existent");

            assertThat(found).isNull();
        }

        @Test
        @DisplayName("Should reorder widgets")
        void shouldReorderWidgets() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            dashboard.addWidget(createTestWidget("widget-1"));
            dashboard.addWidget(createTestWidget("widget-2"));
            dashboard.addWidget(createTestWidget("widget-3"));

            List<String> newOrder = List.of("widget-3", "widget-1", "widget-2");
            dashboard.reorderWidgets(newOrder);

            assertThat(dashboard.getWidgets().get(0).getWidgetId()).isEqualTo("widget-3");
            assertThat(dashboard.getWidgets().get(1).getWidgetId()).isEqualTo("widget-1");
            assertThat(dashboard.getWidgets().get(2).getWidgetId()).isEqualTo("widget-2");
        }

        @Test
        @DisplayName("Should reject reordering with mismatched size")
        void shouldRejectReorderingWithMismatchedSize() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            dashboard.addWidget(createTestWidget("widget-1"));
            dashboard.addWidget(createTestWidget("widget-2"));

            List<String> newOrder = List.of("widget-1");

            assertThatThrownBy(() -> dashboard.reorderWidgets(newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should reject reordering with non-existent widget ID")
        void shouldRejectReorderingWithNonExistentWidgetId() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            dashboard.addWidget(createTestWidget("widget-1"));
            dashboard.addWidget(createTestWidget("widget-2"));

            List<String> newOrder = List.of("widget-1", "non-existent");

            assertThatThrownBy(() -> dashboard.reorderWidgets(newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Dashboard Properties Tests")
    class PropertiesTests {

        @Test
        @DisplayName("Should set description")
        void shouldSetDescription() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            String description = "Sales analytics dashboard";

            dashboard.setDescription(description);

            assertThat(dashboard.getDescription()).isEqualTo(description);
        }

        @Test
        @DisplayName("Should set null description")
        void shouldSetNullDescription() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            dashboard.setDescription("test");

            dashboard.setDescription(null);

            assertThat(dashboard.getDescription()).isNull();
        }

        @Test
        @DisplayName("Should set valid refresh interval")
        void shouldSetRefreshInterval() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            dashboard.setRefreshInterval(60);

            assertThat(dashboard.getRefreshInterval()).isEqualTo(60);
        }

        @ParameterizedTest
        @ValueSource(ints = {10, 20, 29})
        @DisplayName("Should reject refresh interval below minimum")
        void shouldRejectLowRefreshInterval(int interval) {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            assertThatThrownBy(() -> dashboard.setRefreshInterval(interval))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("at least");
        }

        @Test
        @DisplayName("Should reject refresh interval above maximum")
        void shouldRejectHighRefreshInterval() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            assertThatThrownBy(() -> dashboard.setRefreshInterval(86401))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot exceed");
        }

        @Test
        @DisplayName("Should accept minimum refresh interval")
        void shouldAcceptMinimumRefreshInterval() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            dashboard.setRefreshInterval(30);

            assertThat(dashboard.getRefreshInterval()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should accept maximum refresh interval")
        void shouldAcceptMaximumRefreshInterval() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            dashboard.setRefreshInterval(86400);

            assertThat(dashboard.getRefreshInterval()).isEqualTo(86400);
        }

        @Test
        @DisplayName("Should set public visibility")
        void shouldSetPublicVisibility() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            dashboard.setPublic(true);

            assertThat(dashboard.isPublic()).isTrue();
        }

        @Test
        @DisplayName("Should set theme")
        void shouldSetTheme() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            dashboard.setTheme("dark");

            assertThat(dashboard.getTheme()).isEqualTo("dark");
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "  "})
        @DisplayName("Should reject invalid theme")
        void shouldRejectInvalidTheme(String theme) {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            assertThatThrownBy(() -> dashboard.setTheme(theme))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Theme cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Dashboard Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should track modification time")
        void shouldTrackModificationTime() throws InterruptedException {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Instant originalTime = dashboard.getUpdatedAt();

            Thread.sleep(10);
            dashboard.setDescription("Updated");

            assertThat(dashboard.getUpdatedAt()).isAfter(originalTime);
        }

        @Test
        @DisplayName("Should keep creation time constant")
        void shouldKeepCreationTimeConstant() throws InterruptedException {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Instant createdAt = dashboard.getCreatedAt();

            Thread.sleep(10);
            dashboard.setDescription("Updated");

            assertThat(dashboard.getCreatedAt()).isEqualTo(createdAt);
        }

        @Test
        @DisplayName("Should update timestamp on widget add")
        void shouldUpdateTimestampOnWidgetAdd() throws InterruptedException {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Instant originalTime = dashboard.getUpdatedAt();

            Thread.sleep(10);
            dashboard.addWidget(createTestWidget("widget-1"));

            assertThat(dashboard.getUpdatedAt()).isAfter(originalTime);
        }

        @Test
        @DisplayName("Should update timestamp on widget remove")
        void shouldUpdateTimestampOnWidgetRemove() throws InterruptedException {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget = createTestWidget("widget-1");
            dashboard.addWidget(widget);

            Instant originalTime = dashboard.getUpdatedAt();

            Thread.sleep(10);
            dashboard.removeWidget("widget-1");

            assertThat(dashboard.getUpdatedAt()).isAfter(originalTime);
        }
    }

    @Nested
    @DisplayName("Dashboard Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when dashboard IDs match")
        void shouldBeEqualWhenIdsMatch() {
            Dashboard dashboard1 = Dashboard.restore(
                    "dash-1",
                    "user-1",
                    "Dashboard 1",
                    null,
                    List.of(),
                    300,
                    Instant.now(),
                    Instant.now(),
                    false,
                    "default"
            );

            Dashboard dashboard2 = Dashboard.restore(
                    "dash-1",
                    "user-2",
                    "Dashboard 2",
                    null,
                    List.of(),
                    300,
                    Instant.now(),
                    Instant.now(),
                    false,
                    "default"
            );

            assertThat(dashboard1).isEqualTo(dashboard2);
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            Dashboard dashboard1 = Dashboard.restore(
                    "dash-1",
                    VALID_USER_ID,
                    VALID_NAME,
                    null,
                    List.of(),
                    300,
                    Instant.now(),
                    Instant.now(),
                    false,
                    "default"
            );

            Dashboard dashboard2 = Dashboard.restore(
                    "dash-1",
                    VALID_USER_ID,
                    VALID_NAME,
                    null,
                    List.of(),
                    300,
                    Instant.now(),
                    Instant.now(),
                    false,
                    "default"
            );

            assertThat(dashboard1.hashCode()).isEqualTo(dashboard2.hashCode());
        }
    }

    private Widget createTestWidget(String widgetId) {
        return Widget.builder()
                .widgetId(widgetId)
                .type(WidgetType.CHART)
                .title("Test Widget")
                .dataSource("data-source")
                .config(Map.of("key", "value"))
                .build();
    }
}
