package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Widget Domain Model Tests")
class WidgetTest {

    private static final String VALID_WIDGET_ID = "widget-001";
    private static final String VALID_TITLE = "Test Widget";

    @Nested
    @DisplayName("Widget Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create widget with valid parameters")
        void shouldCreateWithValidParameters() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .config(Map.of("key", "value"))
                    .position(0)
                    .build();

            assertThat(widget).isNotNull();
            assertThat(widget.getWidgetId()).isEqualTo(VALID_WIDGET_ID);
            assertThat(widget.getType()).isEqualTo(WidgetType.CHART);
            assertThat(widget.getTitle()).isEqualTo(VALID_TITLE);
            assertThat(widget.getDataSource()).isEqualTo("metric-1");
            assertThat(widget.getConfig()).isEqualTo(Map.of("key", "value"));
            assertThat(widget.getPosition()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create widget with minimal parameters")
        void shouldCreateWithMinimalParameters() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.METRIC)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build();

            assertThat(widget).isNotNull();
            assertThat(widget.getWidgetId()).isEqualTo(VALID_WIDGET_ID);
            assertThat(widget.getType()).isEqualTo(WidgetType.METRIC);
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", "  "})
        @DisplayName("Should reject invalid widget ID")
        void shouldRejectInvalidWidgetId(String widgetId) {
            assertThatThrownBy(() -> Widget.builder()
                    .widgetId(widgetId)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Widget ID cannot be null or empty");
        }

        @Test
        @DisplayName("Should reject null type")
        void shouldRejectNullType() {
            assertThatThrownBy(() -> Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(null)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Widget type cannot be null");
        }

        @Test
        @DisplayName("Should allow null title")
        void shouldAllowNullTitle() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(null)
                    .dataSource("metric-1")
                    .build();

            assertThat(widget).isNotNull();
            assertThat(widget.getTitle()).isNull();
        }

        @Test
        @DisplayName("Should allow null data source")
        void shouldAllowNullDataSource() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource(null)
                    .build();

            assertThat(widget).isNotNull();
            assertThat(widget.getDataSource()).isNull();
        }

        @Test
        @DisplayName("Should allow null config")
        void shouldAllowNullConfig() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .config(null)
                    .build();

            assertThat(widget).isNotNull();
            assertThat(widget.getConfig()).isNull();
        }

        @Test
        @DisplayName("Should accept all widget types")
        void shouldAcceptAllWidgetTypes() {
            for (WidgetType type : WidgetType.values()) {
                Widget widget = Widget.builder()
                        .widgetId(VALID_WIDGET_ID)
                        .type(type)
                        .title(VALID_TITLE)
                        .dataSource("metric-1")
                        .build();

                assertThat(widget.getType()).isEqualTo(type);
            }
        }
    }

    @Nested
    @DisplayName("Widget Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when widget IDs match")
        void shouldBeEqualWhenIdsMatch() {
            Widget widget1 = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title("Widget 1")
                    .dataSource("metric-1")
                    .build();

            Widget widget2 = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.METRIC)
                    .title("Widget 2")
                    .dataSource("metric-2")
                    .build();

            assertThat(widget1).isEqualTo(widget2);
        }

        @Test
        @DisplayName("Should not be equal when widget IDs differ")
        void shouldNotBeEqualWhenIdsDiffer() {
            Widget widget1 = Widget.builder()
                    .widgetId("widget-1")
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build();

            Widget widget2 = Widget.builder()
                    .widgetId("widget-2")
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build();

            assertThat(widget1).isNotEqualTo(widget2);
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void shouldHaveConsistentHashCode() {
            Widget widget1 = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build();

            Widget widget2 = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .build();

            assertThat(widget1.hashCode()).isEqualTo(widget2.hashCode());
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should support fluent builder pattern")
        void shouldSupportFluentBuilder() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .config(Map.of())
                    .position(5)
                    .build();

            assertThat(widget.getPosition()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should allow setting position")
        void shouldAllowSettingPosition() {
            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .position(10)
                    .build();

            assertThat(widget.getPosition()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should allow setting config")
        void shouldAllowSettingConfig() {
            Map<String, Object> config = Map.of(
                    "color", "blue",
                    "size", 20
            );

            Widget widget = Widget.builder()
                    .widgetId(VALID_WIDGET_ID)
                    .type(WidgetType.CHART)
                    .title(VALID_TITLE)
                    .dataSource("metric-1")
                    .config(config)
                    .build();

            assertThat(widget.getConfig()).isEqualTo(config);
        }
    }
}
