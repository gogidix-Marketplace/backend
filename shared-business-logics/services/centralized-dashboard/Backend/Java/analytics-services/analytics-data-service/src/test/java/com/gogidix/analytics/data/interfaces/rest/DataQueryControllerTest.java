package com.gogidix.analytics.data.interfaces.rest;

import com.gogidix.analytics.data.application.service.DataExportCommandService;
import com.gogidix.analytics.data.application.service.DataQueryCommandService;
import com.gogidix.analytics.data.application.service.DataQueryService;
import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.model.DataQuery;
import com.gogidix.analytics.data.domain.port.in.CreateDataQueryCommand;
import com.gogidix.analytics.data.domain.port.in.CreateExportCommand;
import com.gogidix.analytics.data.domain.port.in.ExecuteDataQueryCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataQueryController Tests")
class DataQueryControllerTest {

    @Mock
    private DataQueryCommandService commandService;

    @Mock
    private DataExportCommandService exportCommandService;

    @Mock
    private DataQueryService queryService;

    @InjectMocks
    private DataQueryController controller;

    private DataQuery buildTestDataQuery() {
        return DataQuery.builder()
            .id("query-1")
            .queryName("Test Query")
            .description("A test query")
            .queryDefinition("SELECT * FROM users")
            .queryType(DataQuery.QueryType.SQL)
            .ownerId("user-1")
            .tenantId("tenant-1")
            .isPublic(false)
            .isFavorite(false)
            .executionCount(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    private DataExport buildTestDataExport() {
        return DataExport.builder()
            .id("export-1")
            .exportName("Test Export")
            .exportFormat(DataExport.ExportFormat.CSV)
            .tenantId("tenant-1")
            .requestedBy("user-1")
            .status(DataExport.ExportStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Nested
    @DisplayName("POST /queries")
    class CreateQueryTests {

        @Test
        @DisplayName("Should create query and return 201")
        void shouldCreateQuery() {
            CreateDataQueryCommand cmd = CreateDataQueryCommand.builder()
                .queryName("New Query")
                .queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL)
                .ownerId("user-1")
                .build();

            DataQuery created = buildTestDataQuery();
            when(commandService.createQuery(any(CreateDataQueryCommand.class))).thenReturn(created);

            ResponseEntity<DataQuery> response = controller.createQuery(cmd);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("query-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("GET /queries")
    class GetQueriesTests {

        @Test
        @DisplayName("Should return paginated queries")
        void shouldReturnPaginatedQueries() {
            Page<DataQuery> page = new PageImpl<>(List.of(buildTestDataQuery()));
            when(queryService.getQueries(any())).thenReturn(page);

            ResponseEntity<Page<DataQuery>> response = controller.getQueries(PageRequest.of(0, 10));

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().getContent().size());
        }
    }

    @Nested
    @DisplayName("GET /queries/{queryId}")
    class GetQueryTests {

        @Test
        @DisplayName("Should return query by id")
        void shouldReturnQueryById() {
            when(queryService.getQuery("query-1")).thenReturn(buildTestDataQuery());

            ResponseEntity<DataQuery> response = controller.getQuery("query-1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("query-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("GET /queries/search")
    class SearchQueriesTests {

        @Test
        @DisplayName("Should return search results")
        void shouldReturnSearchResults() {
            when(queryService.searchQueries("test")).thenReturn(List.of(buildTestDataQuery()));

            ResponseEntity<List<DataQuery>> response = controller.searchQueries("test");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("GET /queries/favorites")
    class GetFavoriteQueriesTests {

        @Test
        @DisplayName("Should return favorite queries")
        void shouldReturnFavoriteQueries() {
            when(queryService.getFavoriteQueries()).thenReturn(List.of(buildTestDataQuery()));

            ResponseEntity<List<DataQuery>> response = controller.getFavoriteQueries();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("GET /queries/public")
    class GetPublicQueriesTests {

        @Test
        @DisplayName("Should return public queries")
        void shouldReturnPublicQueries() {
            when(queryService.getPublicQueries()).thenReturn(List.of(buildTestDataQuery()));

            ResponseEntity<List<DataQuery>> response = controller.getPublicQueries();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("PUT /queries/{queryId}")
    class UpdateQueryTests {

        @Test
        @DisplayName("Should update query and return 200")
        void shouldUpdateQuery() {
            CreateDataQueryCommand cmd = CreateDataQueryCommand.builder()
                .queryName("Updated")
                .queryDefinition("SELECT 2")
                .queryType(DataQuery.QueryType.SQL)
                .ownerId("user-1")
                .build();

            DataQuery updated = buildTestDataQuery();
            updated.setQueryName("Updated");
            when(commandService.updateQuery(eq("query-1"), any(CreateDataQueryCommand.class)))
                .thenReturn(updated);

            ResponseEntity<DataQuery> response = controller.updateQuery("query-1", cmd);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("DELETE /queries/{queryId}")
    class DeleteQueryTests {

        @Test
        @DisplayName("Should delete query")
        void shouldDeleteQuery() {
            controller.deleteQuery("query-1");
            verify(commandService).deleteQuery("query-1");
        }
    }

    @Nested
    @DisplayName("PUT /queries/{queryId}/favorite")
    class ToggleFavoriteTests {

        @Test
        @DisplayName("Should toggle favorite")
        void shouldToggleFavorite() {
            DataQuery toggled = buildTestDataQuery();
            toggled.setIsFavorite(true);
            when(commandService.toggleFavorite("query-1")).thenReturn(toggled);

            ResponseEntity<DataQuery> response = controller.toggleFavorite("query-1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().getIsFavorite());
        }
    }

    @Nested
    @DisplayName("POST /execute")
    class ExecuteQueryTests {

        @Test
        @DisplayName("Should execute query and return result")
        void shouldExecuteQuery() {
            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryId("query-1")
                .queryType(DataQuery.QueryType.SQL)
                .build();

            Map<String, Object> result = Map.of("rows", 10);
            when(queryService.executeQuery(any(ExecuteDataQueryCommand.class))).thenReturn(result);

            ResponseEntity<Map<String, Object>> response = controller.executeQuery(cmd);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(10, response.getBody().get("rows"));
        }
    }

    @Nested
    @DisplayName("POST /queries/{queryId}/execute")
    class ExecuteSavedQueryTests {

        @Test
        @DisplayName("Should execute saved query with parameters")
        void shouldExecuteSavedQueryWithParameters() {
            Map<String, Object> params = Map.of("limit", 100);
            Map<String, Object> result = Map.of("data", "ok");
            when(queryService.executeQuery(any(ExecuteDataQueryCommand.class))).thenReturn(result);

            ResponseEntity<Map<String, Object>> response = controller.executeSavedQuery("query-1", params);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("ok", response.getBody().get("data"));
        }

        @Test
        @DisplayName("Should execute saved query without parameters")
        void shouldExecuteSavedQueryWithoutParameters() {
            Map<String, Object> result = Map.of("data", "ok");
            when(queryService.executeQuery(any(ExecuteDataQueryCommand.class))).thenReturn(result);

            ResponseEntity<Map<String, Object>> response = controller.executeSavedQuery("query-1", null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("GET /queries/schema")
    class GetQuerySchemaTests {

        @Test
        @DisplayName("Should return query schema")
        void shouldReturnQuerySchema() {
            Map<String, Object> schema = Map.of("columns", List.of("id", "name"));
            when(queryService.getQuerySchema("SELECT * FROM users")).thenReturn(schema);

            ResponseEntity<Map<String, Object>> response = controller.getQuerySchema("SELECT * FROM users");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Nested
    @DisplayName("POST /exports")
    class CreateExportTests {

        @Test
        @DisplayName("Should create export and return 201")
        void shouldCreateExport() {
            CreateExportCommand cmd = CreateExportCommand.builder()
                .exportName("Export 1")
                .exportFormat(DataExport.ExportFormat.CSV)
                .build();

            DataExport created = buildTestDataExport();
            when(exportCommandService.createExport(any(CreateExportCommand.class))).thenReturn(created);

            ResponseEntity<DataExport> response = controller.createExport(cmd);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("export-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("GET /exports")
    class GetExportsTests {

        @Test
        @DisplayName("Should return paginated exports")
        void shouldReturnPaginatedExports() {
            Page<DataExport> page = new PageImpl<>(List.of(buildTestDataExport()));
            when(queryService.getExports(any())).thenReturn(page);

            ResponseEntity<Page<DataExport>> response = controller.getExports(PageRequest.of(0, 10));

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().getContent().size());
        }
    }

    @Nested
    @DisplayName("GET /exports/{exportId}")
    class GetExportTests {

        @Test
        @DisplayName("Should return export by id")
        void shouldReturnExportById() {
            when(queryService.getExport("export-1")).thenReturn(buildTestDataExport());

            ResponseEntity<DataExport> response = controller.getExport("export-1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("export-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("POST /exports/{exportId}/cancel")
    class CancelExportTests {

        @Test
        @DisplayName("Should cancel export")
        void shouldCancelExport() {
            controller.cancelExport("export-1");
            verify(exportCommandService).cancelExport("export-1");
        }
    }

    @Nested
    @DisplayName("DELETE /exports/{exportId}")
    class DeleteExportTests {

        @Test
        @DisplayName("Should delete export")
        void shouldDeleteExport() {
            controller.deleteExport("export-1");
            verify(exportCommandService).deleteExport("export-1");
        }
    }

    @Nested
    @DisplayName("GET /datasets")
    class GetDatasetsTests {

        @Test
        @DisplayName("Should return datasets")
        void shouldReturnDatasets() {
            when(queryService.getDatasets()).thenReturn(Collections.emptyList());

            ResponseEntity<List<com.gogidix.analytics.data.domain.model.AnalyticsDataset>> response = controller.getDatasets();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(0, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("GET /datasets/{datasetId}")
    class GetDatasetTests {

        @Test
        @DisplayName("Should return dataset by id")
        void shouldReturnDatasetById() {
            com.gogidix.analytics.data.domain.model.AnalyticsDataset dataset =
                com.gogidix.analytics.data.domain.model.AnalyticsDataset.builder()
                    .id("dataset-1")
                    .datasetName("Test Dataset")
                    .tenantId("tenant-1")
                    .build();
            when(queryService.getDataset("dataset-1")).thenReturn(dataset);

            var response = controller.getDataset("dataset-1");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("dataset-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("GET /datasets/search")
    class SearchDatasetsTests {

        @Test
        @DisplayName("Should search datasets")
        void shouldSearchDatasets() {
            when(queryService.searchDatasets("test")).thenReturn(Collections.emptyList());

            var response = controller.searchDatasets("test");

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(0, response.getBody().size());
        }
    }
}
