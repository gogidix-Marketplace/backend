package com.gogidix.foundation.devtools.service;

import com.gogidix.foundation.devtools.domain.entity.DatabaseQuery;
import com.gogidix.foundation.devtools.domain.entity.DatabaseQueryExecution;
import com.gogidix.foundation.devtools.domain.repository.DatabaseQueryRepository;
import com.gogidix.foundation.devtools.domain.repository.DatabaseQueryExecutionRepository;
import com.gogidix.foundation.devtools.dto.DatabaseQueryDto;
import com.gogidix.foundation.devtools.dto.QueryResult;
import com.gogidix.foundation.devtools.exception.DatabaseQueryException;
import com.gogidix.foundation.devtools.mapper.DatabaseQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseQueryServiceTest {

    @Mock
    private DatabaseQueryRepository queryRepository;

    @Mock
    private DatabaseQueryExecutionRepository executionRepository;

    @Mock
    private DatabaseQueryMapper mapper;

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private DatabaseQueryService databaseQueryService;

    private DatabaseQuery query;
    private DatabaseQueryDto queryDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(databaseQueryService, "queryTimeout", 60000);
        ReflectionTestUtils.setField(databaseQueryService, "maxRows", 1000);

        query = DatabaseQuery.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .name("Test Query")
                .description("Test description")
                .projectId("default")
                .databaseName("testdb")
                .query("SELECT * FROM users LIMIT 10")
                .queryType("SELECT")
                .enabled(true)
                .maxRows(100)
                .timeoutSeconds(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        queryDto = DatabaseQueryDto.builder()
                .uuid(query.getUuid())
                .name(query.getName())
                .description(query.getDescription())
                .projectId(query.getProjectId())
                .databaseName(query.getDatabaseName())
                .query(query.getQuery())
                .queryType(query.getQueryType())
                .enabled(query.getEnabled())
                .maxRows(query.getMaxRows())
                .timeoutSeconds(query.getTimeoutSeconds())
                .build();
    }

    @Test
    void createQuery_ShouldReturnSavedDto() {
        when(mapper.toEntity(any(DatabaseQueryDto.class))).thenReturn(query);
        when(queryRepository.save(any(DatabaseQuery.class))).thenReturn(query);
        when(mapper.toDto(any(DatabaseQuery.class))).thenReturn(queryDto);

        DatabaseQueryDto result = databaseQueryService.createQuery(queryDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Query");
        verify(queryRepository).save(any(DatabaseQuery.class));
    }

    @Test
    void updateQuery_WhenExists_ShouldReturnUpdatedDto() {
        UUID uuid = query.getUuid();

        when(queryRepository.findByUuid(uuid)).thenReturn(Optional.of(query));
        when(queryRepository.save(any(DatabaseQuery.class))).thenReturn(query);
        when(mapper.toDto(any(DatabaseQuery.class))).thenReturn(queryDto);

        DatabaseQueryDto result = databaseQueryService.updateQuery(uuid, queryDto);

        assertThat(result).isNotNull();
        verify(queryRepository).save(query);
    }

    @Test
    void updateQuery_WhenNotFound_ShouldThrowException() {
        UUID uuid = UUID.randomUUID();

        when(queryRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> databaseQueryService.updateQuery(uuid, queryDto))
                .isInstanceOf(DatabaseQueryException.class)
                .hasMessageContaining("Query not found");
    }

    @Test
    void getQuery_WhenExists_ShouldReturnDto() {
        UUID uuid = query.getUuid();

        when(queryRepository.findByUuid(uuid)).thenReturn(Optional.of(query));
        when(mapper.toDto(any(DatabaseQuery.class))).thenReturn(queryDto);

        DatabaseQueryDto result = databaseQueryService.getQuery(uuid);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Query");
    }

    @Test
    void getQuery_WhenNotFound_ShouldThrowException() {
        UUID uuid = UUID.randomUUID();

        when(queryRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> databaseQueryService.getQuery(uuid))
                .isInstanceOf(DatabaseQueryException.class)
                .hasMessageContaining("Query not found");
    }

    @Test
    void getQueriesByProject_ShouldReturnPage() {
        String projectId = "default";
        Pageable pageable = PageRequest.of(0, 20);
        Page<DatabaseQuery> page = new PageImpl<>(List.of(query));

        when(queryRepository.findByProjectId(projectId, pageable)).thenReturn(page);
        when(mapper.toDto(any(DatabaseQuery.class))).thenReturn(queryDto);

        var result = databaseQueryService.getQueriesByProject(projectId, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test Query");
    }

    @Test
    void deleteQuery_ShouldCallRepository() {
        UUID uuid = query.getUuid();
        when(queryRepository.findByUuid(uuid)).thenReturn(Optional.of(query));
        doNothing().when(queryRepository).deleteById(query.getId());

        databaseQueryService.deleteQuery(uuid);

        verify(queryRepository).deleteById(query.getId());
    }

    @Test
    void validateQuery_WhenValidSelect_ShouldReturnValid() {
        String sql = "SELECT * FROM users WHERE id = 1";
        String databaseName = "testdb";

        var result = databaseQueryService.validateQuery(sql, databaseName);

        assertThat(result.get("valid")).isEqualTo(true);
        assertThat(result.get("queryType")).isEqualTo("SELECT");
    }

    @Test
    void validateQuery_WhenEmpty_ShouldReturnInvalid() {
        String sql = "";
        String databaseName = "testdb";

        var result = databaseQueryService.validateQuery(sql, databaseName);

        assertThat(result.get("valid")).isEqualTo(false);
        assertThat(result.get("error")).isNotNull();
    }

    @Test
    void validateQuery_WhenDangerousKeyword_ShouldReturnWarning() {
        String sql = "DROP TABLE users";
        String databaseName = "testdb";

        var result = databaseQueryService.validateQuery(sql, databaseName);

        assertThat(result.get("valid")).isEqualTo(false);
        assertThat(result.get("warning")).isNotNull();
    }

    @Test
    void getQueryStatistics_ShouldReturnStatistics() {
        String projectId = "default";

        when(queryRepository.findByProjectId(projectId)).thenReturn(List.of(query));

        var result = databaseQueryService.getQueryStatistics(projectId);

        assertThat(result.get("totalQueries")).isEqualTo(1L);
        assertThat(result.get("enabledQueries")).isEqualTo(1L);
        assertThat(result.containsKey("queryTypes")).isTrue();
    }

    @Test
    void executeQuery_ShouldReturnFuture() {
        when(queryRepository.findById(1L)).thenReturn(Optional.of(query));
        lenient().when(executionRepository.save(any(DatabaseQueryExecution.class)))
                .thenReturn(DatabaseQueryExecution.builder().build());

        var result = databaseQueryService.executeQuery(1L, Map.of(), "test-user");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CompletableFuture.class);
    }

    @Test
    void executeAdHocQuery_ShouldReturnFuture() {
        String sql = "SELECT 1";

        lenient().when(executionRepository.save(any(DatabaseQueryExecution.class)))
                .thenReturn(DatabaseQueryExecution.builder().build());

        var result = databaseQueryService.executeAdHocQuery(sql, "default", Map.of(), "test-user");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CompletableFuture.class);
    }
}
