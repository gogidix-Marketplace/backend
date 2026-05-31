package com.gogidix.foundation.devtools.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.domain.entity.ApiTestCase;
import com.gogidix.foundation.devtools.domain.entity.ApiTestExecution;
import com.gogidix.foundation.devtools.domain.repository.ApiTestCaseRepository;
import com.gogidix.foundation.devtools.domain.repository.ApiTestExecutionRepository;
import com.gogidix.foundation.devtools.dto.ApiTestCaseDto;
import com.gogidix.foundation.devtools.dto.ApiTestExecutionDto;
import com.gogidix.foundation.devtools.dto.ApiTestRequest;
import com.gogidix.foundation.devtools.dto.ApiTestResult;
import com.gogidix.foundation.devtools.exception.ApiTestException;
import com.gogidix.foundation.devtools.mapper.ApiTestMapper;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiTestingServiceTest {

    @Mock
    private ApiTestCaseRepository testCaseRepository;

    @Mock
    private ApiTestExecutionRepository executionRepository;

    @Mock
    private ApiTestMapper mapper;

    @InjectMocks
    private ApiTestingService apiTestingService;

    private ApiTestCase testCase;
    private ApiTestCaseDto testCaseDto;
    private ApiTestRequest testRequest;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(apiTestingService, "defaultTimeout", 30000);
        ReflectionTestUtils.setField(apiTestingService, "maxConcurrentTests", 10);

        testCase = ApiTestCase.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .name("Test API")
                .description("Test description")
                .projectId("default")
                .method("GET")
                .url("https://api.example.com/test")
                .expectedStatusCode(200)
                .enabled(true)
                .timeout(30000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testCaseDto = ApiTestCaseDto.builder()
                .uuid(testCase.getUuid())
                .name(testCase.getName())
                .description(testCase.getDescription())
                .projectId(testCase.getProjectId())
                .method(testCase.getMethod())
                .url(testCase.getUrl())
                .expectedStatusCode(testCase.getExpectedStatusCode())
                .enabled(testCase.getEnabled())
                .timeout(testCase.getTimeout())
                .build();

        testRequest = ApiTestRequest.builder()
                .name("Ad-hoc Test")
                .method("POST")
                .url("https://api.example.com/endpoint")
                .expectedStatusCode(201)
                .timeout(30000)
                .build();
    }

    @Test
    void createTestCase_ShouldReturnSavedDto() {
        when(mapper.toEntity(any(ApiTestCaseDto.class))).thenReturn(testCase);
        when(testCaseRepository.save(any(ApiTestCase.class))).thenReturn(testCase);
        when(mapper.toDto(any(ApiTestCase.class))).thenReturn(testCaseDto);

        ApiTestCaseDto result = apiTestingService.createTestCase(testCaseDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test API");
        verify(testCaseRepository).save(any(ApiTestCase.class));
    }

    @Test
    void updateTestCase_WhenExists_ShouldReturnUpdatedDto() {
        UUID uuid = testCase.getUuid();

        when(testCaseRepository.findByUuid(uuid)).thenReturn(Optional.of(testCase));
        when(testCaseRepository.save(any(ApiTestCase.class))).thenReturn(testCase);
        when(mapper.toDto(any(ApiTestCase.class))).thenReturn(testCaseDto);

        ApiTestCaseDto result = apiTestingService.updateTestCase(uuid, testCaseDto);

        assertThat(result).isNotNull();
        verify(testCaseRepository).save(testCase);
    }

    @Test
    void updateTestCase_WhenNotFound_ShouldThrowException() {
        UUID uuid = UUID.randomUUID();

        when(testCaseRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> apiTestingService.updateTestCase(uuid, testCaseDto))
                .isInstanceOf(ApiTestException.class)
                .hasMessageContaining("Test case not found");
    }

    @Test
    void getTestCase_WhenExists_ShouldReturnDto() {
        UUID uuid = testCase.getUuid();

        when(testCaseRepository.findByUuid(uuid)).thenReturn(Optional.of(testCase));
        when(mapper.toDto(any(ApiTestCase.class))).thenReturn(testCaseDto);

        ApiTestCaseDto result = apiTestingService.getTestCase(uuid);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test API");
    }

    @Test
    void getTestCase_WhenNotFound_ShouldThrowException() {
        UUID uuid = UUID.randomUUID();

        when(testCaseRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> apiTestingService.getTestCase(uuid))
                .isInstanceOf(ApiTestException.class)
                .hasMessageContaining("Test case not found");
    }

    @Test
    void getTestCasesByProject_ShouldReturnPage() {
        String projectId = "default";
        Pageable pageable = PageRequest.of(0, 20);
        Page<ApiTestCase> page = new PageImpl<>(List.of(testCase));

        when(testCaseRepository.findByProjectId(projectId, pageable)).thenReturn(page);
        when(mapper.toDto(any(ApiTestCase.class))).thenReturn(testCaseDto);

        var result = apiTestingService.getTestCasesByProject(projectId, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Test API");
    }

    @Test
    void deleteTestCase_ShouldCallRepository() {
        UUID uuid = testCase.getUuid();
        when(testCaseRepository.findByUuid(uuid)).thenReturn(Optional.of(testCase));
        doNothing().when(testCaseRepository).deleteById(testCase.getId());

        apiTestingService.deleteTestCase(uuid);

        verify(testCaseRepository).deleteById(testCase.getId());
    }

    @Test
    void getExecutionHistory_ShouldReturnExecutions() {
        Long testCaseId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        ApiTestExecution execution = ApiTestExecution.builder()
                .id(1L)
                .uuid(UUID.randomUUID())
                .testCaseId(testCaseId)
                .status("COMPLETED")
                .actualStatusCode(200)
                .executedAt(LocalDateTime.now())
                .build();

        Page<ApiTestExecution> page = new PageImpl<>(List.of(execution));

        when(executionRepository.findByTestCaseId(testCaseId, pageable)).thenReturn(page);
        when(mapper.toExecutionDto(any(ApiTestExecution.class))).thenReturn(
                ApiTestExecutionDto.builder()
                        .uuid(execution.getUuid())
                        .status("COMPLETED")
                        .actualStatusCode(200)
                        .build());

        var result = apiTestingService.getExecutionHistory(testCaseId, pageable);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("COMPLETED");
    }

    @Test
    void getTestStatistics_ShouldReturnStatistics() {
        String projectId = "default";

        when(testCaseRepository.findByProjectId(projectId)).thenReturn(List.of(testCase));
        when(executionRepository.countByTestCaseIdAndStatus(anyLong(), eq("PASSED"))).thenReturn(10L);
        when(executionRepository.countByTestCaseIdAndStatus(anyLong(), eq("FAILED"))).thenReturn(2L);

        var result = apiTestingService.getTestStatistics(projectId);

        assertThat(result.get("totalTests")).isEqualTo(1L);
        assertThat(result.get("enabledTests")).isEqualTo(1L);
        assertThat(result.containsKey("executions")).isTrue();
    }

    @Test
    void executeAdHocTest_ShouldReturnFuture() {
        lenient().when(executionRepository.save(any(ApiTestExecution.class)))
                .thenReturn(ApiTestExecution.builder().build());

        var result = apiTestingService.executeAdHocTest(testRequest, "test-user");

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CompletableFuture.class);
    }

    @Test
    void executeBatch_ShouldReturnResults() {
        List<Long> testCaseIds = List.of(1L, 2L);

        when(testCaseRepository.findById(anyLong())).thenReturn(Optional.of(testCase));
        when(executionRepository.save(any(ApiTestExecution.class)))
                .thenReturn(ApiTestExecution.builder().build());

        var results = apiTestingService.executeBatch(testCaseIds, "test-user");

        assertThat(results).hasSize(2);
    }
}
