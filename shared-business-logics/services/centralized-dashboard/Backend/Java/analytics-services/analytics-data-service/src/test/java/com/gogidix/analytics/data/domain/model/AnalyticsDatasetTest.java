package com.gogidix.analytics.data.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AnalyticsDataset Tests")
class AnalyticsDatasetTest {

    private AnalyticsDataset buildFullDataset() {
        return AnalyticsDataset.builder()
            .id("id-1")
            .datasetName("Dataset 1")
            .description("Description")
            .tableName("table_1")
            .viewName("view_1")
            .schemaDefinition("{}")
            .columnMappings("{}")
            .filters("{\"key\":\"val\"}")
            .relationships("[]")
            .dataSource("ds-1")
            .datasetType(AnalyticsDataset.DatasetType.TABLE)
            .refreshStrategy("AUTO")
            .refreshIntervalSeconds(300)
            .lastRefreshedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .ownerId("owner-1")
            .tenantId("tenant-1")
            .isPublic(true)
            .isActive(true)
            .tags("tag1,tag2")
            .createdAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .updatedAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .build();
    }

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        AnalyticsDataset ds = AnalyticsDataset.builder()
            .datasetName("Test Dataset").description("A test dataset")
            .tableName("test_table").tenantId("t1").ownerId("u1")
            .isPublic(true).isActive(true).build();
        assertEquals("Test Dataset", ds.getDatasetName());
        assertEquals("A test dataset", ds.getDescription());
        assertEquals("test_table", ds.getTableName());
        assertEquals("t1", ds.getTenantId());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() {
        AnalyticsDataset ds = new AnalyticsDataset();
        assertNull(ds.getId());
        assertNull(ds.getDatasetName());
    }

    @Test
    @DisplayName("Should use all-args constructor")
    void shouldUseAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AnalyticsDataset ds = new AnalyticsDataset(
            "id1", "name", "desc", "table", "view",
            "schema", "mappings", "filters", "relations",
            "source", AnalyticsDataset.DatasetType.VIEW, "AUTO",
            60, now, "owner", "tenant",
            true, false, "tags", now, now
        );
        assertEquals("id1", ds.getId());
        assertEquals("name", ds.getDatasetName());
        assertEquals("desc", ds.getDescription());
        assertEquals("table", ds.getTableName());
        assertEquals("view", ds.getViewName());
        assertEquals("schema", ds.getSchemaDefinition());
        assertEquals("mappings", ds.getColumnMappings());
        assertEquals("filters", ds.getFilters());
        assertEquals("relations", ds.getRelationships());
        assertEquals("source", ds.getDataSource());
        assertEquals(AnalyticsDataset.DatasetType.VIEW, ds.getDatasetType());
        assertEquals("AUTO", ds.getRefreshStrategy());
        assertEquals(60, ds.getRefreshIntervalSeconds());
        assertEquals(now, ds.getLastRefreshedAt());
        assertEquals("owner", ds.getOwnerId());
        assertEquals("tenant", ds.getTenantId());
        assertTrue(ds.getIsPublic());
        assertFalse(ds.getIsActive());
        assertEquals("tags", ds.getTags());
        assertNotNull(ds.getCreatedAt());
    }

    @Test
    @DisplayName("Should use builder defaults")
    void shouldUseBuilderDefaults() {
        AnalyticsDataset ds = AnalyticsDataset.builder()
            .datasetName("name").tenantId("t1").ownerId("o1").build();
        assertEquals(AnalyticsDataset.DatasetType.CUSTOM, ds.getDatasetType());
        assertEquals("MANUAL", ds.getRefreshStrategy());
        assertFalse(ds.getIsPublic());
        assertTrue(ds.getIsActive());
    }

    @Nested
    @DisplayName("Setter/Getter Tests")
    class SetterGetterTests {

        private AnalyticsDataset ds;

        @BeforeEach
        void setUp() {
            ds = new AnalyticsDataset();
        }

        @Test
        void shouldSetAndGetId() {
            ds.setId("id1");
            assertEquals("id1", ds.getId());
        }

        @Test
        void shouldSetAndGetDatasetName() {
            ds.setDatasetName("name");
            assertEquals("name", ds.getDatasetName());
        }

        @Test
        void shouldSetAndGetDescription() {
            ds.setDescription("desc");
            assertEquals("desc", ds.getDescription());
        }

        @Test
        void shouldSetAndGetTableName() {
            ds.setTableName("table");
            assertEquals("table", ds.getTableName());
        }

        @Test
        void shouldSetAndGetViewName() {
            ds.setViewName("view");
            assertEquals("view", ds.getViewName());
        }

        @Test
        void shouldSetAndGetSchemaDefinition() {
            ds.setSchemaDefinition("{\"type\":\"object\"}");
            assertEquals("{\"type\":\"object\"}", ds.getSchemaDefinition());
        }

        @Test
        void shouldSetAndGetColumnMappings() {
            ds.setColumnMappings("[{\"col\":\"val\"}]");
            assertEquals("[{\"col\":\"val\"}]", ds.getColumnMappings());
        }

        @Test
        void shouldSetAndGetFilters() {
            ds.setFilters("{\"status\":\"active\"}");
            assertEquals("{\"status\":\"active\"}", ds.getFilters());
        }

        @Test
        void shouldSetAndGetRelationships() {
            ds.setRelationships("[{\"rel\":\"has\"}]");
            assertEquals("[{\"rel\":\"has\"}]", ds.getRelationships());
        }

        @Test
        void shouldSetAndGetDataSource() {
            ds.setDataSource("postgres-main");
            assertEquals("postgres-main", ds.getDataSource());
        }

        @Test
        void shouldSetAndGetDatasetType() {
            ds.setDatasetType(AnalyticsDataset.DatasetType.AGGREGATED);
            assertEquals(AnalyticsDataset.DatasetType.AGGREGATED, ds.getDatasetType());
        }

        @Test
        void shouldSetAndGetRefreshStrategy() {
            ds.setRefreshStrategy("SCHEDULED");
            assertEquals("SCHEDULED", ds.getRefreshStrategy());
        }

        @Test
        void shouldSetAndGetRefreshIntervalSeconds() {
            ds.setRefreshIntervalSeconds(600);
            assertEquals(600, ds.getRefreshIntervalSeconds());
        }

        @Test
        void shouldSetAndGetLastRefreshedAt() {
            LocalDateTime now = LocalDateTime.now();
            ds.setLastRefreshedAt(now);
            assertEquals(now, ds.getLastRefreshedAt());
        }

        @Test
        void shouldSetAndGetOwnerId() {
            ds.setOwnerId("owner-1");
            assertEquals("owner-1", ds.getOwnerId());
        }

        @Test
        void shouldSetAndGetTenantId() {
            ds.setTenantId("tenant-1");
            assertEquals("tenant-1", ds.getTenantId());
        }

        @Test
        void shouldSetAndGetIsPublic() {
            ds.setIsPublic(true);
            assertTrue(ds.getIsPublic());
        }

        @Test
        void shouldSetAndGetIsActive() {
            ds.setIsActive(false);
            assertFalse(ds.getIsActive());
        }

        @Test
        void shouldSetAndGetTags() {
            ds.setTags("alpha,beta");
            assertEquals("alpha,beta", ds.getTags());
        }

        @Test
        void shouldSetAndGetCreatedAt() {
            LocalDateTime now = LocalDateTime.now();
            ds.setCreatedAt(now);
            assertEquals(now, ds.getCreatedAt());
        }

        @Test
        void shouldSetAndGetUpdatedAt() {
            LocalDateTime now = LocalDateTime.now();
            ds.setUpdatedAt(now);
            assertEquals(now, ds.getUpdatedAt());
        }
    }

    @Test
    @DisplayName("Should mark as refreshed")
    void shouldMarkAsRefreshed() {
        AnalyticsDataset ds = new AnalyticsDataset();
        assertNull(ds.getLastRefreshedAt());
        ds.markAsRefreshed();
        assertNotNull(ds.getLastRefreshedAt());
    }

    @Test
    @DisplayName("Should activate and deactivate")
    void shouldActivateAndDeactivate() {
        AnalyticsDataset ds = new AnalyticsDataset();
        ds.deactivate();
        assertFalse(ds.getIsActive());
        ds.activate();
        assertTrue(ds.getIsActive());
    }

    @Test
    @DisplayName("Should test DatasetType enum")
    void shouldTestDatasetTypeEnum() {
        assertEquals(6, AnalyticsDataset.DatasetType.values().length);
        assertNotNull(AnalyticsDataset.DatasetType.valueOf("TABLE"));
        assertNotNull(AnalyticsDataset.DatasetType.valueOf("VIEW"));
        assertNotNull(AnalyticsDataset.DatasetType.valueOf("MATERIALIZED_VIEW"));
        assertNotNull(AnalyticsDataset.DatasetType.valueOf("CUSTOM"));
        assertNotNull(AnalyticsDataset.DatasetType.valueOf("JOINED"));
        assertNotNull(AnalyticsDataset.DatasetType.valueOf("AGGREGATED"));
    }

    @Nested
    @DisplayName("Lifecycle Tests")
    class LifecycleTests {

        @Test
        void onCreateShouldSetIdWhenNull() {
            AnalyticsDataset ds = new AnalyticsDataset();
            ds.onCreate();
            assertNotNull(ds.getId());
            assertNotNull(ds.getCreatedAt());
            assertNotNull(ds.getUpdatedAt());
        }

        @Test
        void onCreateShouldNotOverwriteExistingId() {
            AnalyticsDataset ds = new AnalyticsDataset();
            ds.setId("custom-id");
            ds.onCreate();
            assertEquals("custom-id", ds.getId());
        }

        @Test
        void onUpdateShouldSetUpdatedAt() {
            AnalyticsDataset ds = new AnalyticsDataset();
            ds.onUpdate();
            assertNotNull(ds.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("equals/hashCode/toString Tests")
    class EqualsHashCodeToStringTests {

        @Test
        @DisplayName("equals with same instance returns true")
        void equalsSameInstance() {
            AnalyticsDataset ds = new AnalyticsDataset();
            assertEquals(ds, ds);
        }

        @Test
        @DisplayName("equals with null returns false")
        void equalsNull() {
            AnalyticsDataset ds = new AnalyticsDataset();
            assertNotEquals(null, ds);
        }

        @Test
        @DisplayName("equals with different type returns false")
        void equalsDifferentType() {
            AnalyticsDataset ds = new AnalyticsDataset();
            assertNotEquals("string", ds);
        }

        @Test
        @DisplayName("equals with equal full objects returns true")
        void equalsEqualFullObjects() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            assertEquals(a, b);
        }

        @Test
        @DisplayName("equals with all null fields returns true")
        void equalsAllNullFields() {
            AnalyticsDataset a = new AnalyticsDataset();
            AnalyticsDataset b = new AnalyticsDataset();
            assertEquals(a, b);
        }

        @Test
        @DisplayName("hashCode equal for equal objects")
        void hashCodeEqual() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("hashCode consistent")
        void hashCodeConsistent() {
            AnalyticsDataset ds = buildFullDataset();
            int h1 = ds.hashCode();
            int h2 = ds.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        @DisplayName("toString contains fields")
        void toStringContainsFields() {
            AnalyticsDataset ds = buildFullDataset();
            String str = ds.toString();
            assertNotNull(str);
            assertTrue(str.contains("Dataset 1"));
        }

        @Test
        @DisplayName("equals differs by id")
        void equalsDiffersById() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setId("different-id");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by datasetName")
        void equalsDiffersByDatasetName() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setDatasetName("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by description")
        void equalsDiffersByDescription() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by tableName")
        void equalsDiffersByTableName() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setTableName("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by viewName")
        void equalsDiffersByViewName() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setViewName("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by schemaDefinition")
        void equalsDiffersBySchemaDefinition() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setSchemaDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by columnMappings")
        void equalsDiffersByColumnMappings() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setColumnMappings("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by filters")
        void equalsDiffersByFilters() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setFilters("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by relationships")
        void equalsDiffersByRelationships() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setRelationships("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by dataSource")
        void equalsDiffersByDataSource() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by datasetType")
        void equalsDiffersByDatasetType() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setDatasetType(AnalyticsDataset.DatasetType.VIEW);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by refreshStrategy")
        void equalsDiffersByRefreshStrategy() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setRefreshStrategy("SCHEDULED");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by refreshIntervalSeconds")
        void equalsDiffersByRefreshIntervalSeconds() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setRefreshIntervalSeconds(999);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by lastRefreshedAt")
        void equalsDiffersByLastRefreshedAt() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setLastRefreshedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by ownerId")
        void equalsDiffersByOwnerId() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by tenantId")
        void equalsDiffersByTenantId() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setTenantId("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by isPublic")
        void equalsDiffersByIsPublic() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setIsPublic(false);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by isActive")
        void equalsDiffersByIsActive() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setIsActive(false);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by tags")
        void equalsDiffersByTags() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setTags("different");
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by createdAt")
        void equalsDiffersByCreatedAt() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals differs by updatedAt")
        void equalsDiffersByUpdatedAt() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setUpdatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals with null vs non-null id")
        void equalsNullVsNonNullId() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setId(null);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals with null vs non-null datasetName")
        void equalsNullVsNonNullDatasetName() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setDatasetName(null);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals with null vs non-null description")
        void equalsNullVsNonNullDescription() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setDescription(null);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("equals with null vs non-null tenantId")
        void equalsNullVsNonNullTenantId() {
            AnalyticsDataset a = buildFullDataset();
            AnalyticsDataset b = buildFullDataset();
            b.setTenantId(null);
            assertNotEquals(a, b);
        }

        @Test
        @DisplayName("hashCode with null fields does not throw")
        void hashCodeWithNullFields() {
            AnalyticsDataset ds = new AnalyticsDataset();
            assertDoesNotThrow(() -> ds.hashCode());
        }

        @Test
        @DisplayName("toString with null fields does not throw")
        void toStringWithNullFields() {
            AnalyticsDataset ds = new AnalyticsDataset();
            assertDoesNotThrow(() -> ds.toString());
        }
    }
}
