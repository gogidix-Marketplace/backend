package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dashboard Domain Model Tests")
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
            assertThat(dashboard.getWidgets()).isEmpty();
        }

        @Test
        @DisplayName("Should reject null name")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> Dashboard.create(null, VALID_USER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be null");
        }

        @Test
        @DisplayName("Should reject empty name")
        void shouldRejectEmptyName() {
            assertThatThrownBy(() -> Dashboard.create("", VALID_USER_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be empty");
        }

        @Test
        @DisplayName("Should reject null user ID")
        void shouldRejectNullUserId() {
            assertThatThrownBy(() -> Dashboard.create(VALID_NAME, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("User ID cannot be null");
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
            assertThat(dashboard.getWidgets().get(0)).isEqualTo(widget);
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
        @DisplayName("Should enforce maximum widgets limit")
        void shouldEnforceMaxWidgets() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            for (int i = 0; i < 20; i++) {
                dashboard.addWidget(createTestWidget("widget-" + i));
            }

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
        }

        @Test
        @DisplayName("Should update widget")
        void shouldUpdateWidget() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Widget widget = createTestWidget("widget-1");
            dashboard.addWidget(widget);

            Widget updatedWidget = createTestWidget("widget-1");
            dashboard.updateWidget(updatedWidget);

            assertThat(dashboard.getWidgets()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Refresh Interval Tests")
    class RefreshIntervalTests {

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
                    .hasMessageContaining("minimum");
        }

        @Test
        @DisplayName("Should reject refresh interval above maximum")
        void shouldRejectHighRefreshInterval() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);

            assertThatThrownBy(() -> dashboard.setRefreshInterval(86401))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("maximum");
        }
    }

    @Nested
    @DisplayName("Dashboard Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should set description")
        void shouldSetDescription() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            String description = "Sales analytics dashboard";

            dashboard.setDescription(description);

            assertThat(dashboard.getDescription()).isEqualTo(description);
        }

        @Test
        @DisplayName("Should track modification time")
        void shouldTrackModificationTime() {
            Dashboard dashboard = Dashboard.create(VALID_NAME, VALID_USER_ID);
            Instant originalTime = dashboard.getUpdatedAt();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // ignore
            }

            dashboard.setDescription("Updated description");

            assertThat(dashboard.getUpdatedAt()).isAfter(originalTime);
        }
    }

    private Widget createTestWidget(String widgetId) {
        return Widget.builder()
                .widgetId(widgetId)
                .type(WidgetType.CHART)
                .title("Test Widget")
                .dataSource("metric-1")
                .config(Map.of("chartType", "line"))
                .build();
    }
}
