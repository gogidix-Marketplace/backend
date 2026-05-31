package com.gogidix.analytics.bi.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardTest {

    private Dashboard dashboard;

    @BeforeEach
    void setUp() {
        dashboard = Dashboard.builder()
            .name("Sales Dashboard")
            .description("Monthly sales metrics")
            .category("Sales")
            .ownerId("user-1")
            .tenantId("tenant-1")
            .build();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void builderCreatesDashboardWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            Dashboard db = Dashboard.builder()
                .id("id-1")
                .name("Test")
                .description("Desc")
                .slug("test-slug")
                .category("Cat")
                .ownerId("owner-1")
                .tenantId("tenant-1")
                .isPublic(true)
                .isFavorite(true)
                .refreshIntervalSeconds(60)
                .layoutConfig("{\"layout\":\"grid\"}")
                .theme("dark")
                .tags("tag1,tag2")
                .createdAt(now)
                .updatedAt(now)
                .lastViewedAt(now)
                .viewCount(10)
                .widgets(new ArrayList<>())
                .build();

            assertEquals("id-1", db.getId());
            assertEquals("Test", db.getName());
            assertEquals("Desc", db.getDescription());
            assertEquals("test-slug", db.getSlug());
            assertEquals("Cat", db.getCategory());
            assertEquals("owner-1", db.getOwnerId());
            assertEquals("tenant-1", db.getTenantId());
            assertTrue(db.getIsPublic());
            assertTrue(db.getIsFavorite());
            assertEquals(60, db.getRefreshIntervalSeconds());
            assertEquals("{\"layout\":\"grid\"}", db.getLayoutConfig());
            assertEquals("dark", db.getTheme());
            assertEquals("tag1,tag2", db.getTags());
            assertEquals(10, db.getViewCount());
            assertNotNull(db.getWidgets());
        }

        @Test
        void builderDefaults() {
            Dashboard db = Dashboard.builder()
                .name("Test")
                .ownerId("owner")
                .tenantId("tenant")
                .build();

            assertFalse(db.getIsPublic());
            assertFalse(db.getIsFavorite());
            assertEquals("default", db.getTheme());
            assertEquals(0, db.getViewCount());
            assertNotNull(db.getWidgets());
            assertTrue(db.getWidgets().isEmpty());
        }
    }

    @Test
    void noArgsConstructorCreatesEmptyDashboard() {
        Dashboard db = new Dashboard();
        assertNotNull(db);
        assertNull(db.getName());
        assertNull(db.getId());
    }

    @Test
    void allArgsConstructorCreatesDashboard() {
        Dashboard db = new Dashboard("id", "name", "desc", "slug", "cat",
            "owner", "tenant", true, false, 30, "layout", "dark",
            "tags", null, null, null, 5, new ArrayList<>());
        assertEquals("id", db.getId());
        assertEquals("name", db.getName());
    }

    @Test
    void gettersAndSettersWork() {
        dashboard.setId("new-id");
        assertEquals("new-id", dashboard.getId());

        dashboard.setName("New Name");
        assertEquals("New Name", dashboard.getName());

        dashboard.setDescription("New Desc");
        assertEquals("New Desc", dashboard.getDescription());

        dashboard.setSlug("new-slug");
        assertEquals("new-slug", dashboard.getSlug());

        dashboard.setCategory("New Cat");
        assertEquals("New Cat", dashboard.getCategory());

        dashboard.setOwnerId("new-owner");
        assertEquals("new-owner", dashboard.getOwnerId());

        dashboard.setTenantId("new-tenant");
        assertEquals("new-tenant", dashboard.getTenantId());

        dashboard.setIsPublic(true);
        assertTrue(dashboard.getIsPublic());

        dashboard.setIsFavorite(true);
        assertTrue(dashboard.getIsFavorite());

        dashboard.setRefreshIntervalSeconds(120);
        assertEquals(120, dashboard.getRefreshIntervalSeconds());

        dashboard.setLayoutConfig("new-layout");
        assertEquals("new-layout", dashboard.getLayoutConfig());

        dashboard.setTheme("blue");
        assertEquals("blue", dashboard.getTheme());

        dashboard.setTags("new-tags");
        assertEquals("new-tags", dashboard.getTags());

        LocalDateTime now = LocalDateTime.now();
        dashboard.setCreatedAt(now);
        assertEquals(now, dashboard.getCreatedAt());

        dashboard.setUpdatedAt(now);
        assertEquals(now, dashboard.getUpdatedAt());

        dashboard.setLastViewedAt(now);
        assertEquals(now, dashboard.getLastViewedAt());

        dashboard.setViewCount(42);
        assertEquals(42, dashboard.getViewCount());

        List<DashboardWidget> widgets = new ArrayList<>();
        dashboard.setWidgets(widgets);
        assertEquals(widgets, dashboard.getWidgets());
    }

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        void onCreateSetsTimestamps() {
            Dashboard db = Dashboard.builder()
                .name("Test")
                .ownerId("owner")
                .tenantId("tenant")
                .build();

            assertNull(db.getCreatedAt());
            assertNull(db.getUpdatedAt());

            db.onCreate();

            assertNotNull(db.getCreatedAt());
            assertNotNull(db.getUpdatedAt());
        }

        @Test
        void onCreateGeneratesIdWhenNull() {
            dashboard.setId(null);
            dashboard.onCreate();
            assertNotNull(dashboard.getId());
        }

        @Test
        void onCreatePreservesExistingId() {
            dashboard.setId("existing-id");
            dashboard.onCreate();
            assertEquals("existing-id", dashboard.getId());
        }

        @Test
        void onCreateGeneratesSlugWhenNull() {
            dashboard.setSlug(null);
            dashboard.onCreate();
            assertNotNull(dashboard.getSlug());
            assertTrue(dashboard.getSlug().startsWith("sales-dashboard-"));
        }

        @Test
        void onCreatePreservesExistingSlug() {
            dashboard.setSlug("existing-slug");
            dashboard.onCreate();
            assertEquals("existing-slug", dashboard.getSlug());
        }

        @Test
        void onUpdateSetsUpdatedAt() {
            LocalDateTime before = LocalDateTime.now().minusSeconds(1);
            dashboard.setUpdatedAt(before);
            dashboard.onUpdate();
            assertNotNull(dashboard.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Business Method Tests")
    class BusinessMethodTests {

        @Test
        void addWidgetAddsWidgetToList() {
            DashboardWidget widget = DashboardWidget.builder()
                .widgetName("Chart 1")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(0)
                .build();

            dashboard.addWidget(widget);

            assertEquals(1, dashboard.getWidgets().size());
            assertEquals(dashboard, widget.getDashboard());
        }

        @Test
        void removeWidgetRemovesWidgetFromList() {
            DashboardWidget widget = DashboardWidget.builder()
                .widgetName("Chart 1")
                .widgetType(DashboardWidget.WidgetType.BAR_CHART)
                .position(0)
                .build();

            dashboard.addWidget(widget);
            assertEquals(1, dashboard.getWidgets().size());

            dashboard.removeWidget(widget);
            assertEquals(0, dashboard.getWidgets().size());
            assertNull(widget.getDashboard());
        }

        @Test
        void incrementViewCountIncrementsAndSetsLastViewed() {
            assertNull(dashboard.getLastViewedAt());
            assertEquals(0, dashboard.getViewCount());

            dashboard.incrementViewCount();

            assertEquals(1, dashboard.getViewCount());
            assertNotNull(dashboard.getLastViewedAt());

            dashboard.incrementViewCount();
            assertEquals(2, dashboard.getViewCount());
        }
    }

    @Test
    void generateSlugHandlesSpecialCharacters() {
        dashboard.setSlug(null);
        dashboard.setName("My Awesome Dashboard!!! Test @#$");
        dashboard.onCreate();
        assertNotNull(dashboard.getSlug());
        assertFalse(dashboard.getSlug().contains("!!!"));
    }

    @Nested
    @DisplayName("Equals Branch Coverage Tests")
    class EqualsBranchTests {

        private Dashboard createFullyPopulated() {
            LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30);
            return Dashboard.builder()
                .id("id-1")
                .name("Name")
                .description("Desc")
                .slug("slug-1")
                .category("Cat")
                .ownerId("owner-1")
                .tenantId("tenant-1")
                .isPublic(true)
                .isFavorite(false)
                .refreshIntervalSeconds(60)
                .layoutConfig("{\"layout\":\"grid\"}")
                .theme("dark")
                .tags("tag1")
                .createdAt(now)
                .updatedAt(now)
                .lastViewedAt(now)
                .viewCount(5)
                .widgets(new ArrayList<>())
                .build();
        }

        @Test
        void equalsSameInstance() {
            Dashboard d = createFullyPopulated();
            assertEquals(d, d);
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
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentName() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDescription() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentSlug() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setSlug("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCategory() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setCategory("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOwnerId() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTenantId() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setTenantId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentIsPublic() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setIsPublic(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentIsFavorite() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setIsFavorite(true);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRefreshIntervalSeconds() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setRefreshIntervalSeconds(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentLayoutConfig() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setLayoutConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTheme() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setTheme("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTags() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setTags("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCreatedAt() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentUpdatedAt() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setUpdatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentLastViewedAt() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setLastViewedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentViewCount() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            b.setViewCount(999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentWidgets() {
            Dashboard a = createFullyPopulated();
            Dashboard b = createFullyPopulated();
            List<DashboardWidget> widgets = new ArrayList<>();
            widgets.add(DashboardWidget.builder().widgetName("w").widgetType(DashboardWidget.WidgetType.TABLE).position(0).build());
            b.setWidgets(widgets);
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            Dashboard a = Dashboard.builder().build();
            Dashboard b = Dashboard.builder().build();
            assertEquals(a, b);
        }

        @Test
        void equalsOneNullFieldOtherNonNull() {
            Dashboard a = Dashboard.builder().name("x").build();
            Dashboard b = Dashboard.builder().build();
            assertNotEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            Dashboard d = createFullyPopulated();
            int h1 = d.hashCode();
            int h2 = d.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            Dashboard d = Dashboard.builder().build();
            assertNotNull(d.hashCode());
        }

        @Test
        void toStringContainsName() {
            String str = dashboard.toString();
            assertNotNull(str);
            assertTrue(str.contains("Sales Dashboard"));
        }

        @Test
        void toStringWithNullFields() {
            Dashboard d = Dashboard.builder().build();
            String str = d.toString();
            assertNotNull(str);
        }
    }
}
