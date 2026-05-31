package com.gogidix.analytics.data.application.service;

import com.gogidix.analytics.data.domain.model.AnalyticsDataset;
import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.model.DataQuery;
import com.gogidix.analytics.data.domain.port.in.ExecuteDataQueryCommand;
import com.gogidix.analytics.data.domain.port.out.DataQueryExecutor;
import com.gogidix.analytics.data.domain.repository.AnalyticsDatasetRepository;
import com.gogidix.analytics.data.domain.repository.DataExportRepository;
import com.gogidix.analytics.data.domain.repository.DataQueryRepository;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataQueryService Tests")
class DataQueryServiceTest {

    @Mock
    private DataQueryRepository queryRepository;

    @Mock
    private DataExportRepository exportRepository;

    @Mock
    private AnalyticsDatasetRepository datasetRepository;

    @Mock
    private DataQueryExecutor queryExecutor;

    @InjectMocks
    private DataQueryService service;

    private MockedStatic<RequestContext> requestContextMock;

    @BeforeEach
    void setUp() {
        requestContextMock = mockStatic(RequestContext.class);
        requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn("tenant-1");
    }

    @AfterEach
    void tearDown() {
        requestContextMock.close();
    }

    private void mockRequestContextWithSubject(String subject) {
        RequestContext ctx = mock(RequestContext.class);
        requestContextMock.when(RequestContext::get).thenReturn(ctx);
        when(ctx.getSubject()).thenReturn(subject);
    }

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

    private AnalyticsDataset buildTestDataset() {
        return AnalyticsDataset.builder()
            .id("dataset-1")
            .datasetName("Test Dataset")
            .tenantId("tenant-1")
            .isPublic(false)
            .isActive(true)
            .build();
    }

    @Nested
    @DisplayName("getQueries Tests")
    class GetQueriesTests {

        @Test
        @DisplayName("Should return paginated queries for tenant")
        void shouldReturnPaginatedQueries() {
            List<DataQuery> queries = List.of(buildTestDataQuery());
            when(queryRepository.findByTenantId("tenant-1")).thenReturn(queries);
            Pageable pageable = PageRequest.of(0, 10);

            Page<DataQuery> result = service.getQueries(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
            verify(queryRepository, times(2)).findByTenantId("tenant-1");
        }

        @Test
        @DisplayName("Should return empty page when no queries")
        void shouldReturnEmptyPageWhenNoQueries() {
            when(queryRepository.findByTenantId("tenant-1")).thenReturn(Collections.emptyList());
            Pageable pageable = PageRequest.of(0, 10);

            Page<DataQuery> result = service.getQueries(pageable);

            assertNotNull(result);
            assertEquals(0, result.getTotalElements());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);
            Pageable pageable = PageRequest.of(0, 10);

            assertThrows(ValidationException.class, () -> service.getQueries(pageable));
        }

        @Test
        @DisplayName("Should handle pagination offset correctly")
        void shouldHandlePaginationOffset() {
            List<DataQuery> queries = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                queries.add(buildTestDataQuery());
            }
            when(queryRepository.findByTenantId("tenant-1")).thenReturn(queries);
            Pageable pageable = PageRequest.of(1, 10);

            Page<DataQuery> result = service.getQueries(pageable);

            assertEquals(25, result.getTotalElements());
            assertEquals(10, result.getContent().size());
        }
    }

    @Nested
    @DisplayName("getQuery Tests")
    class GetQueryTests {

        @Test
        @DisplayName("Should return query by id for same tenant")
        void shouldReturnQueryById() {
            DataQuery query = buildTestDataQuery();
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            DataQuery result = service.getQuery("query-1");

            assertNotNull(result);
            assertEquals("query-1", result.getId());
        }

        @Test
        @DisplayName("Should return public query from different tenant")
        void shouldReturnPublicQueryFromDifferentTenant() {
            DataQuery query = buildTestDataQuery();
            query.setTenantId("other-tenant");
            query.setIsPublic(true);
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            DataQuery result = service.getQuery("query-1");

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing query")
        void shouldThrowNotFoundForMissingQuery() {
            when(queryRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getQuery("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for private query from other tenant")
        void shouldThrowForPrivateQueryFromOtherTenant() {
            DataQuery query = buildTestDataQuery();
            query.setTenantId("other-tenant");
            query.setIsPublic(false);
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            assertThrows(ValidationException.class, () -> service.getQuery("query-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getQuery("q1"));
        }
    }

    @Nested
    @DisplayName("searchQueries Tests")
    class SearchQueriesTests {

        @Test
        @DisplayName("Should return search results")
        void shouldReturnSearchResults() {
            List<DataQuery> expected = List.of(buildTestDataQuery());
            when(queryRepository.searchByTenantId("tenant-1", "test")).thenReturn(expected);

            List<DataQuery> result = service.searchQueries("test");

            assertEquals(1, result.size());
            verify(queryRepository).searchByTenantId("tenant-1", "test");
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.searchQueries("test"));
        }
    }

    @Nested
    @DisplayName("getFavoriteQueries Tests")
    class GetFavoriteQueriesTests {

        @Test
        @DisplayName("Should return favorite queries for user")
        void shouldReturnFavoriteQueries() {
            mockRequestContextWithSubject("user-1");
            List<DataQuery> expected = List.of(buildTestDataQuery());
            when(queryRepository.findByTenantIdAndOwnerIdAndIsFavoriteTrue("tenant-1", "user-1"))
                .thenReturn(expected);

            List<DataQuery> result = service.getFavoriteQueries();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getFavoriteQueries());
        }

        @Test
        @DisplayName("Should throw ValidationException when no user")
        void shouldThrowWhenNoUser() {
            requestContextMock.when(RequestContext::get).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getFavoriteQueries());
        }
    }

    @Nested
    @DisplayName("getPublicQueries Tests")
    class GetPublicQueriesTests {

        @Test
        @DisplayName("Should return public queries")
        void shouldReturnPublicQueries() {
            List<DataQuery> expected = List.of(buildTestDataQuery());
            when(queryRepository.findByTenantIdAndIsPublicTrue("tenant-1")).thenReturn(expected);

            List<DataQuery> result = service.getPublicQueries();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getPublicQueries());
        }
    }

    @Nested
    @DisplayName("executeQuery Tests")
    class ExecuteQueryTests {

        @Test
        @DisplayName("Should execute query by id")
        void shouldExecuteQueryById() {
            DataQuery query = buildTestDataQuery();
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));
            Map<String, Object> queryResult = Map.of("rows", 10);
            when(queryExecutor.executeQuery(any(DataQuery.class), any())).thenReturn(queryResult);

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryId("query-1")
                .queryType(DataQuery.QueryType.SQL)
                .parameters(null)
                .build();

            Map<String, Object> result = service.executeQuery(cmd);

            assertNotNull(result);
            assertEquals(10, result.get("rows"));
            verify(queryRepository).save(query);
        }

        @Test
        @DisplayName("Should execute query with inline definition")
        void shouldExecuteQueryWithInlineDefinition() {
            Map<String, Object> queryResult = Map.of("data", "ok");
            when(queryExecutor.executeQuery(any(DataQuery.class), any())).thenReturn(queryResult);

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryDefinition("SELECT 1")
                .queryType(DataQuery.QueryType.SQL)
                .parameters(Map.of("key", "value"))
                .build();

            Map<String, Object> result = service.executeQuery(cmd);

            assertNotNull(result);
            assertEquals("ok", result.get("data"));
        }

        @Test
        @DisplayName("Should use query definition from saved query when not in command")
        void shouldUseQueryDefinitionFromSavedQuery() {
            DataQuery query = buildTestDataQuery();
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));
            Map<String, Object> queryResult = new HashMap<>();
            when(queryExecutor.executeQuery(any(DataQuery.class), any())).thenReturn(queryResult);

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryId("query-1")
                .queryType(DataQuery.QueryType.SQL)
                .build();

            service.executeQuery(cmd);

            verify(queryExecutor).executeQuery(any(DataQuery.class), isNull());
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing query")
        void shouldThrowNotFoundForMissingQuery() {
            when(queryRepository.findById("missing")).thenReturn(Optional.empty());

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryId("missing")
                .queryType(DataQuery.QueryType.SQL)
                .build();

            assertThrows(NotFoundException.class, () -> service.executeQuery(cmd));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant private query")
        void shouldThrowForDifferentTenantPrivateQuery() {
            DataQuery query = buildTestDataQuery();
            query.setTenantId("other-tenant");
            query.setIsPublic(false);
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryId("query-1")
                .queryType(DataQuery.QueryType.SQL)
                .build();

            assertThrows(ValidationException.class, () -> service.executeQuery(cmd));
        }

        @Test
        @DisplayName("Should throw ValidationException when no query definition")
        void shouldThrowWhenNoQueryDefinition() {
            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryType(DataQuery.QueryType.SQL)
                .build();

            assertThrows(ValidationException.class, () -> service.executeQuery(cmd));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryType(DataQuery.QueryType.SQL)
                .build();

            assertThrows(ValidationException.class, () -> service.executeQuery(cmd));
        }

        @Test
        @DisplayName("Should allow public query from different tenant")
        void shouldAllowPublicQueryFromDifferentTenant() {
            DataQuery query = buildTestDataQuery();
            query.setTenantId("other-tenant");
            query.setIsPublic(true);
            query.setQueryDefinition("SELECT 1");
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));
            when(queryExecutor.executeQuery(any(DataQuery.class), any())).thenReturn(new HashMap<>());

            ExecuteDataQueryCommand cmd = ExecuteDataQueryCommand.builder()
                .queryId("query-1")
                .queryType(DataQuery.QueryType.SQL)
                .build();

            Map<String, Object> result = service.executeQuery(cmd);
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("getQuerySchema Tests")
    class GetQuerySchemaTests {

        @Test
        @DisplayName("Should return query schema")
        void shouldReturnQuerySchema() {
            Map<String, Object> schema = Map.of("columns", List.of("id", "name"));
            when(queryExecutor.getQuerySchema("SELECT * FROM users")).thenReturn(schema);

            Map<String, Object> result = service.getQuerySchema("SELECT * FROM users");

            assertEquals(schema, result);
        }
    }

    @Nested
    @DisplayName("getExports Tests")
    class GetExportsTests {

        @Test
        @DisplayName("Should return paginated exports")
        void shouldReturnPaginatedExports() {
            List<DataExport> exports = List.of(buildTestDataExport());
            when(exportRepository.findByTenantIdOrderByCreatedAtDesc("tenant-1")).thenReturn(exports);
            Pageable pageable = PageRequest.of(0, 10);

            Page<DataExport> result = service.getExports(pageable);

            assertNotNull(result);
            assertEquals(1, result.getTotalElements());
        }

        @Test
        @DisplayName("Should return empty page when no exports")
        void shouldReturnEmptyPageWhenNoExports() {
            when(exportRepository.findByTenantIdOrderByCreatedAtDesc("tenant-1"))
                .thenReturn(Collections.emptyList());
            Pageable pageable = PageRequest.of(0, 10);

            Page<DataExport> result = service.getExports(pageable);

            assertEquals(0, result.getTotalElements());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);
            Pageable pageable = PageRequest.of(0, 10);

            assertThrows(ValidationException.class, () -> service.getExports(pageable));
        }
    }

    @Nested
    @DisplayName("getExport Tests")
    class GetExportTests {

        @Test
        @DisplayName("Should return export by id for same tenant")
        void shouldReturnExportById() {
            DataExport export = buildTestDataExport();
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            DataExport result = service.getExport("export-1");

            assertNotNull(result);
            assertEquals("export-1", result.getId());
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing export")
        void shouldThrowNotFoundForMissingExport() {
            when(exportRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getExport("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant export")
        void shouldThrowForDifferentTenantExport() {
            DataExport export = buildTestDataExport();
            export.setTenantId("other-tenant");
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            assertThrows(ValidationException.class, () -> service.getExport("export-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getExport("e1"));
        }

        @Test
        @DisplayName("Should return export even when different user but same tenant")
        void shouldReturnExportForDifferentUserSameTenant() {
            mockRequestContextWithSubject("other-user");
            DataExport export = buildTestDataExport();
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            DataExport result = service.getExport("export-1");

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("getDatasets Tests")
    class GetDatasetsTests {

        @Test
        @DisplayName("Should return active datasets for tenant")
        void shouldReturnActiveDatasets() {
            List<AnalyticsDataset> datasets = List.of(buildTestDataset());
            when(datasetRepository.findByTenantIdAndIsActiveTrue("tenant-1")).thenReturn(datasets);

            List<AnalyticsDataset> result = service.getDatasets();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getDatasets());
        }
    }

    @Nested
    @DisplayName("getDataset Tests")
    class GetDatasetTests {

        @Test
        @DisplayName("Should return dataset by id for same tenant")
        void shouldReturnDatasetById() {
            AnalyticsDataset dataset = buildTestDataset();
            when(datasetRepository.findById("dataset-1")).thenReturn(Optional.of(dataset));

            AnalyticsDataset result = service.getDataset("dataset-1");

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should return public dataset from different tenant")
        void shouldReturnPublicDatasetFromDifferentTenant() {
            AnalyticsDataset dataset = buildTestDataset();
            dataset.setTenantId("other-tenant");
            dataset.setIsPublic(true);
            when(datasetRepository.findById("dataset-1")).thenReturn(Optional.of(dataset));

            AnalyticsDataset result = service.getDataset("dataset-1");

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing dataset")
        void shouldThrowNotFoundForMissingDataset() {
            when(datasetRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getDataset("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for private dataset from other tenant")
        void shouldThrowForPrivateDatasetFromOtherTenant() {
            AnalyticsDataset dataset = buildTestDataset();
            dataset.setTenantId("other-tenant");
            dataset.setIsPublic(false);
            when(datasetRepository.findById("dataset-1")).thenReturn(Optional.of(dataset));

            assertThrows(ValidationException.class, () -> service.getDataset("dataset-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getDataset("d1"));
        }
    }

    @Nested
    @DisplayName("searchDatasets Tests")
    class SearchDatasetsTests {

        @Test
        @DisplayName("Should return search results")
        void shouldReturnSearchResults() {
            List<AnalyticsDataset> expected = List.of(buildTestDataset());
            when(datasetRepository.searchByTenantId("tenant-1", "test")).thenReturn(expected);

            List<AnalyticsDataset> result = service.searchDatasets("test");

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.searchDatasets("test"));
        }
    }

    @Nested
    @DisplayName("getExport RequestContext branch Tests")
    class GetExportRequestContextTests {

        @Test
        @DisplayName("Should return export when ctx is null (userId null path)")
        void shouldReturnExportWhenCtxIsNull() {
            requestContextMock.when(RequestContext::get).thenReturn(null);
            DataExport export = buildTestDataExport();
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            DataExport result = service.getExport("export-1");

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should return export when userId equals requestedBy")
        void shouldReturnExportWhenUserIdEqualsRequestedBy() {
            RequestContext ctx = mock(RequestContext.class);
            requestContextMock.when(RequestContext::get).thenReturn(ctx);
            when(ctx.getSubject()).thenReturn("user-1");
            DataExport export = buildTestDataExport();
            export.setRequestedBy("user-1");
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            DataExport result = service.getExport("export-1");

            assertNotNull(result);
            assertEquals("user-1", result.getRequestedBy());
        }

        @Test
        @DisplayName("Should return export when ctx returns null subject")
        void shouldReturnExportWhenCtxReturnsNullSubject() {
            RequestContext ctx = mock(RequestContext.class);
            requestContextMock.when(RequestContext::get).thenReturn(ctx);
            when(ctx.getSubject()).thenReturn(null);
            DataExport export = buildTestDataExport();
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            DataExport result = service.getExport("export-1");

            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("getFavoriteQueries RequestContext branch Tests")
    class GetFavoriteQueriesContextTests {

        @Test
        @DisplayName("Should throw when ctx returns null subject")
        void shouldThrowWhenCtxReturnsNullSubject() {
            RequestContext ctx = mock(RequestContext.class);
            requestContextMock.when(RequestContext::get).thenReturn(ctx);
            when(ctx.getSubject()).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getFavoriteQueries());
        }
    }
}
