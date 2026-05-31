package com.gogidix.analytics.data.application.service;

import com.gogidix.analytics.data.domain.model.DataQuery;
import com.gogidix.analytics.data.domain.port.in.CreateDataQueryCommand;
import com.gogidix.analytics.data.domain.port.out.DataQueryExecutor;
import com.gogidix.analytics.data.domain.repository.DataQueryRepository;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataQueryCommandService Tests")
class DataQueryCommandServiceTest {

    @Mock
    private DataQueryRepository queryRepository;

    @Mock
    private DataQueryExecutor queryExecutor;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private DataQueryCommandService service;

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

    private DataQuery buildTestDataQuery() {
        return DataQuery.builder()
            .id("query-1")
            .queryName("Test Query")
            .queryDefinition("SELECT * FROM users")
            .queryType(DataQuery.QueryType.SQL)
            .ownerId("user-1")
            .tenantId("tenant-1")
            .isPublic(false)
            .isFavorite(false)
            .executionCount(0)
            .build();
    }

    private CreateDataQueryCommand buildTestCommand() {
        return CreateDataQueryCommand.builder()
            .queryName("Test Query")
            .description("A test query")
            .queryDefinition("SELECT * FROM users")
            .queryType(DataQuery.QueryType.SQL)
            .dataSource("main")
            .parametersSchema("{}")
            .category("test")
            .tags("unit")
            .ownerId("user-1")
            .isPublic(false)
            .build();
    }

    @Nested
    @DisplayName("createQuery Tests")
    class CreateQueryTests {

        @Test
        @DisplayName("Should create query successfully")
        void shouldCreateQuery() {
            CreateDataQueryCommand cmd = buildTestCommand();
            DataQuery saved = buildTestDataQuery();
            when(queryRepository.save(any(DataQuery.class))).thenReturn(saved);

            DataQuery result = service.createQuery(cmd);

            assertNotNull(result);
            verify(queryRepository).save(any(DataQuery.class));
            verify(auditService).logEvent(eq("DATA_QUERY_CREATED"), eq("DataQuery"), anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.createQuery(buildTestCommand()));
        }
    }

    @Nested
    @DisplayName("updateQuery Tests")
    class UpdateQueryTests {

        @Test
        @DisplayName("Should update query successfully")
        void shouldUpdateQuery() {
            CreateDataQueryCommand cmd = buildTestCommand();
            DataQuery existing = buildTestDataQuery();
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(existing));
            when(queryRepository.save(any(DataQuery.class))).thenReturn(existing);

            DataQuery result = service.updateQuery("query-1", cmd);

            assertNotNull(result);
            verify(auditService).logEvent(eq("DATA_QUERY_UPDATED"), eq("DataQuery"), anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing query")
        void shouldThrowNotFound() {
            when(queryRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class,
                () -> service.updateQuery("missing", buildTestCommand()));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant")
        void shouldThrowForDifferentTenant() {
            DataQuery existing = buildTestDataQuery();
            existing.setTenantId("other-tenant");
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class,
                () -> service.updateQuery("query-1", buildTestCommand()));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class,
                () -> service.updateQuery("q1", buildTestCommand()));
        }
    }

    @Nested
    @DisplayName("deleteQuery Tests")
    class DeleteQueryTests {

        @Test
        @DisplayName("Should delete query successfully")
        void shouldDeleteQuery() {
            DataQuery query = buildTestDataQuery();
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            service.deleteQuery("query-1");

            verify(queryRepository).delete(query);
            verify(auditService).logEvent(eq("DATA_QUERY_DELETED"), eq("DataQuery"), anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing query")
        void shouldThrowNotFound() {
            when(queryRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.deleteQuery("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant")
        void shouldThrowForDifferentTenant() {
            DataQuery query = buildTestDataQuery();
            query.setTenantId("other-tenant");
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            assertThrows(ValidationException.class, () -> service.deleteQuery("query-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.deleteQuery("q1"));
        }
    }

    @Nested
    @DisplayName("toggleFavorite Tests")
    class ToggleFavoriteTests {

        @Test
        @DisplayName("Should toggle favorite from false to true")
        void shouldToggleToTrue() {
            DataQuery query = buildTestDataQuery();
            query.setIsFavorite(false);
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));
            when(queryRepository.save(any(DataQuery.class))).thenReturn(query);

            DataQuery result = service.toggleFavorite("query-1");

            assertTrue(result.getIsFavorite());
        }

        @Test
        @DisplayName("Should toggle favorite from true to false")
        void shouldToggleToFalse() {
            DataQuery query = buildTestDataQuery();
            query.setIsFavorite(true);
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));
            when(queryRepository.save(any(DataQuery.class))).thenReturn(query);

            DataQuery result = service.toggleFavorite("query-1");

            assertFalse(result.getIsFavorite());
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing query")
        void shouldThrowNotFound() {
            when(queryRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.toggleFavorite("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant")
        void shouldThrowForDifferentTenant() {
            DataQuery query = buildTestDataQuery();
            query.setTenantId("other-tenant");
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));

            assertThrows(ValidationException.class, () -> service.toggleFavorite("query-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.toggleFavorite("q1"));
        }
    }

    @Nested
    @DisplayName("recordExecution Tests")
    class RecordExecutionTests {

        @Test
        @DisplayName("Should record execution for existing query")
        void shouldRecordExecution() {
            DataQuery query = buildTestDataQuery();
            when(queryRepository.findById("query-1")).thenReturn(Optional.of(query));
            when(queryRepository.save(any(DataQuery.class))).thenReturn(query);

            service.recordExecution("query-1", 100L);

            verify(queryRepository).save(query);
            assertEquals(1, query.getExecutionCount());
        }

        @Test
        @DisplayName("Should do nothing for non-existent query")
        void shouldDoNothingForNonExistent() {
            when(queryRepository.findById("missing")).thenReturn(Optional.empty());

            service.recordExecution("missing", 100L);

            verify(queryRepository, never()).save(any());
        }
    }
}
