package com.gogidix.aiservices.aiorchestrationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aiorchestrationservice.application.dto.CreateWorkflowRequestDto;
import com.gogidix.aiservices.aiorchestrationservice.application.dto.WorkflowExecutionResponseDto;
import com.gogidix.aiservices.aiorchestrationservice.application.dto.WorkflowResponseDto;
import com.gogidix.aiservices.aiorchestrationservice.application.service.WorkflowApplicationService;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.Workflow;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowExecution;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStatus;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowStep;
import com.gogidix.aiservices.aiorchestrationservice.shared.requestcontext.RequestContext;
import com.gogidix.aiservices.aiorchestrationservice.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrchestrationController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@DisplayName("OrchestrationController REST API Tests")
class OrchestrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WorkflowApplicationService workflowService;

    private CreateWorkflowRequestDto validRequest;
    private WorkflowResponseDto workflowResponse;
    private WorkflowExecutionResponseDto executionResponse;
    private WorkflowExecution workflowExecution;

    @BeforeEach
    void setUp() {
        RequestContext context = new RequestContext("tenant-001", "req-123", "user-001", Instant.now());
        RequestContextHolder.setContext(context);

        List<WorkflowStep> steps = List.of(
                new WorkflowStep("step1", "service1", "action1", null, 5, 3),
                new WorkflowStep("step2", "service2", "action2", null, 5, 3),
                new WorkflowStep("step3", "service3", "action3", null, 5, 3)
        );

        validRequest = new CreateWorkflowRequestDto(
                "Data Processing Workflow",
                "A test workflow for data processing",
                steps,
                List.of("trigger1"),
                60
        );

        workflowResponse = new WorkflowResponseDto(
                UUID.randomUUID().toString(),
                "workflow-" + UUID.randomUUID(),
                "tenant-001",
                "Data Processing Workflow",
                "A test workflow for data processing",
                steps,
                List.of("trigger1"),
                WorkflowStatus.DRAFT,
                Instant.now(),
                Instant.now(),
                null,
                60
        );

        executionResponse = new WorkflowExecutionResponseDto(
                UUID.randomUUID().toString(),
                workflowResponse.workflowId(),
                "tenant-001",
                WorkflowExecution.ExecutionStatus.RUNNING,
                Instant.now(),
                null,
                null,
                0
        );

        // Create WorkflowExecution entity for executeWorkflow mock
        workflowExecution = new WorkflowExecution(
                workflowResponse.workflowId(),
                "tenant-001"
        );
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.setContext(null);
    }

    @Nested
    @DisplayName("POST /api/v1/orchestration/workflows - Create Workflow")
    class CreateWorkflowTests {

        @Test
        @DisplayName("Should create workflow successfully")
        void shouldCreateWorkflow() throws Exception {
            when(workflowService.createWorkflow(eq("tenant-001"), any(CreateWorkflowRequestDto.class)))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.workflowId").exists())
                    .andExpect(jsonPath("$.name").value("Data Processing Workflow"))
                    .andExpect(jsonPath("$.status").value("DRAFT"));

            verify(workflowService).createWorkflow(eq("tenant-001"), any(CreateWorkflowRequestDto.class));
        }

        @Test
        @DisplayName("Should accept workflow with single step")
        void shouldAcceptWorkflowWithSingleStep() throws Exception {
            List<WorkflowStep> singleStep = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );
            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "Single Step Workflow",
                    "Single step workflow",
                    singleStep,
                    List.of(),
                    null
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept workflow with multiple steps")
        void shouldAcceptWorkflowWithMultipleSteps() throws Exception {
            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept workflow with no steps")
        void shouldAcceptWorkflowWithNoSteps() throws Exception {
            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "Empty Workflow",
                    "Empty workflow",
                    Collections.emptyList(),
                    List.of(),
                    null
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/orchestration/workflows/{workflowId} - Get Workflow")
    class GetWorkflowTests {

        @Test
        @DisplayName("Should return workflow by ID")
        void shouldReturnWorkflowById() throws Exception {
            when(workflowService.getWorkflowById(eq(workflowResponse.workflowId()), eq("tenant-001")))
                    .thenReturn(workflowResponse);

            mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", workflowResponse.workflowId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.workflowId").exists())
                    .andExpect(jsonPath("$.name").value("Data Processing Workflow"));

            verify(workflowService).getWorkflowById(workflowResponse.workflowId(), "tenant-001");
        }

        @Test
        @DisplayName("Should return workflow with DRAFT status")
        void shouldReturnWorkflowWithDraftStatus() throws Exception {
            when(workflowService.getWorkflowById(anyString(), eq("tenant-001")))
                    .thenReturn(workflowResponse);

            mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", "wf-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("DRAFT"));
        }

        @Test
        @DisplayName("Should return workflow with ACTIVE status")
        void shouldReturnWorkflowWithActiveStatus() throws Exception {
            WorkflowResponseDto activeResponse = new WorkflowResponseDto(
                    workflowResponse.id(),
                    workflowResponse.workflowId(),
                    "tenant-001",
                    "Active Workflow",
                    "Active workflow",
                    workflowResponse.steps(),
                    workflowResponse.triggers(),
                    WorkflowStatus.ACTIVE,
                    Instant.now().minusSeconds(60),
                    Instant.now(),
                    Instant.now(),
                    60
            );

            when(workflowService.getWorkflowById(anyString(), eq("tenant-001")))
                    .thenReturn(activeResponse);

            mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", "wf-456"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Should return workflow with COMPLETED status")
        void shouldReturnWorkflowWithCompletedStatus() throws Exception {
            WorkflowResponseDto completedResponse = new WorkflowResponseDto(
                    workflowResponse.id(),
                    workflowResponse.workflowId(),
                    "tenant-001",
                    "Completed Workflow",
                    "Completed workflow",
                    workflowResponse.steps(),
                    workflowResponse.triggers(),
                    WorkflowStatus.COMPLETED,
                    Instant.now().minusSeconds(300),
                    Instant.now(),
                    Instant.now(),
                    60
            );

            when(workflowService.getWorkflowById(anyString(), eq("tenant-001")))
                    .thenReturn(completedResponse);

            mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", "wf-789"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("COMPLETED"));
        }

        @Test
        @DisplayName("Should return workflow with FAILED status")
        void shouldReturnWorkflowWithFailedStatus() throws Exception {
            WorkflowResponseDto failedResponse = new WorkflowResponseDto(
                    workflowResponse.id(),
                    workflowResponse.workflowId(),
                    "tenant-001",
                    "Failed Workflow",
                    "Failed workflow",
                    workflowResponse.steps(),
                    workflowResponse.triggers(),
                    WorkflowStatus.FAILED,
                    Instant.now().minusSeconds(30),
                    Instant.now(),
                    Instant.now(),
                    60
            );

            when(workflowService.getWorkflowById(anyString(), eq("tenant-001")))
                    .thenReturn(failedResponse);

            mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", "wf-failed"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("FAILED"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/orchestration/workflows - List Workflows")
    class ListWorkflowsTests {

        @Test
        @DisplayName("Should return list of workflows")
        void shouldReturnListOfWorkflows() throws Exception {
            List<WorkflowResponseDto> workflows = List.of(workflowResponse);

            when(workflowService.getWorkflowsByTenant("tenant-001"))
                    .thenReturn(workflows);

            mockMvc.perform(get("/api/v1/orchestration/workflows"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].workflowId").exists())
                    .andExpect(jsonPath("$[0].name").value("Data Processing Workflow"));

            verify(workflowService).getWorkflowsByTenant("tenant-001");
        }

        @Test
        @DisplayName("Should return empty list when no workflows")
        void shouldReturnEmptyList() throws Exception {
            when(workflowService.getWorkflowsByTenant("tenant-001"))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/orchestration/workflows"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should return multiple workflows")
        void shouldReturnMultipleWorkflows() throws Exception {
            WorkflowResponseDto workflow2 = new WorkflowResponseDto(
                    UUID.randomUUID().toString(),
                    "workflow-" + UUID.randomUUID(),
                    "tenant-001",
                    "Second Workflow",
                    "Second workflow",
                    workflowResponse.steps(),
                    workflowResponse.triggers(),
                    WorkflowStatus.DRAFT,
                    Instant.now(),
                    Instant.now(),
                    null,
                    60
            );

            List<WorkflowResponseDto> workflows = List.of(workflowResponse, workflow2);

            when(workflowService.getWorkflowsByTenant("tenant-001"))
                    .thenReturn(workflows);

            mockMvc.perform(get("/api/v1/orchestration/workflows"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/orchestration/workflows/{workflowId} - Delete Workflow")
    class DeleteWorkflowTests {

        @Test
        @DisplayName("Should delete workflow successfully")
        void shouldDeleteWorkflow() throws Exception {
            doNothing().when(workflowService).deleteWorkflow(eq("wf-123"), eq("tenant-001"));

            mockMvc.perform(delete("/api/v1/orchestration/workflows/{workflowId}", "wf-123"))
                    .andExpect(status().isNoContent());

            verify(workflowService).deleteWorkflow("wf-123", "tenant-001");
        }

        @Test
        @DisplayName("Should handle UUID as workflow ID")
        void shouldHandleUuidAsWorkflowId() throws Exception {
            String uuid = UUID.randomUUID().toString();
            doNothing().when(workflowService).deleteWorkflow(eq(uuid), eq("tenant-001"));

            mockMvc.perform(delete("/api/v1/orchestration/workflows/{workflowId}", uuid))
                    .andExpect(status().isNoContent());

            verify(workflowService).deleteWorkflow(uuid, "tenant-001");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/orchestration/workflows/{workflowId}/execute - Execute Workflow")
    class ExecuteWorkflowTests {

        @Test
        @DisplayName("Should execute workflow successfully")
        void shouldExecuteWorkflow() throws Exception {
            when(workflowService.executeWorkflow(eq("wf-123"), eq("tenant-001")))
                    .thenReturn(workflowExecution);

            when(workflowService.getWorkflowById(eq("wf-123"), eq("tenant-001")))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows/{workflowId}/execute", "wf-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.executionId").exists())
                    .andExpect(jsonPath("$.workflowId").exists())
                    .andExpect(jsonPath("$.status").value("RUNNING"));

            verify(workflowService).executeWorkflow("wf-123", "tenant-001");
        }

        @Test
        @DisplayName("Should accept different workflow IDs")
        void shouldAcceptDifferentWorkflowIds() throws Exception {
            when(workflowService.executeWorkflow(anyString(), eq("tenant-001")))
                    .thenReturn(workflowExecution);

            String[] workflowIds = {"wf-1", "wf-2", "wf-data-processing"};

            for (String workflowId : workflowIds) {
                mockMvc.perform(post("/api/v1/orchestration/workflows/{workflowId}/execute", workflowId))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("GET /api/v1/orchestration/health - Health Check")
    class HealthCheckTests {

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealthStatus() throws Exception {
            mockMvc.perform(get("/api/v1/orchestration/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.message").value("AI Orchestration Service is running"));
        }
    }

    @Nested
    @DisplayName("WorkflowStatus Enum Tests")
    class WorkflowStatusTests {

        @Test
        @DisplayName("Should handle all WorkflowStatus values")
        void shouldHandleAllWorkflowStatusValues() throws Exception {
            WorkflowStatus[] statuses = {
                    WorkflowStatus.DRAFT,
                    WorkflowStatus.ACTIVE,
                    WorkflowStatus.PAUSED,
                    WorkflowStatus.COMPLETED,
                    WorkflowStatus.FAILED
            };

            for (WorkflowStatus status : statuses) {
                WorkflowResponseDto response = new WorkflowResponseDto(
                        workflowResponse.id(),
                        workflowResponse.workflowId(),
                        "tenant-001",
                        "Test Workflow",
                        "Test workflow description",
                        workflowResponse.steps(),
                        workflowResponse.triggers(),
                        status,
                        Instant.now(),
                        Instant.now(),
                        null,
                        60
                );

                when(workflowService.getWorkflowById(anyString(), eq("tenant-001")))
                        .thenReturn(response);

                mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", "test-wf"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value(status.name()));
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in workflow name")
        void shouldHandleUnicodeInWorkflowName() throws Exception {
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );
            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "ワークフロー",
                    "Japanese workflow",
                    steps,
                    List.of(),
                    null
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long workflow name")
        void shouldHandleVeryLongWorkflowName() throws Exception {
            String longName = "a".repeat(200);
            List<WorkflowStep> steps = List.of(
                    new WorkflowStep("step1", "service1", "action1", null, 5, 3)
            );
            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    longName,
                    "Long name workflow",
                    steps,
                    List.of(),
                    null
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle special characters in workflow ID")
        void shouldHandleSpecialCharactersInWorkflowId() throws Exception {
            when(workflowService.getWorkflowById(anyString(), eq("tenant-001")))
                    .thenReturn(workflowResponse);

            mockMvc.perform(get("/api/v1/orchestration/workflows/{workflowId}", "workflow-with_special.chars"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for workflow response")
        void shouldReturnProperJsonStructureForWorkflowResponse() throws Exception {
            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.workflowId").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.steps").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.updatedAt").exists());
        }

        @Test
        @DisplayName("Should return proper JSON structure for execution response")
        void shouldReturnProperJsonStructureForExecutionResponse() throws Exception {
            when(workflowService.executeWorkflow(anyString(), eq("tenant-001")))
                    .thenReturn(workflowExecution);

            mockMvc.perform(post("/api/v1/orchestration/workflows/{workflowId}/execute", "wf-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.executionId").exists())
                    .andExpect(jsonPath("$.workflowId").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.startedAt").exists());
        }
    }

    @Nested
    @DisplayName("Steps Tests")
    class StepsTests {

        @Test
        @DisplayName("Should create workflow with no steps")
        void shouldCreateWorkflowWithNoSteps() throws Exception {
            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "Empty Workflow",
                    "Empty workflow",
                    Collections.emptyList(),
                    List.of(),
                    null
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should create workflow with single step")
        void shouldCreateWorkflowWithSingleStep() throws Exception {
            List<WorkflowStep> singleStep = List.of(
                    new WorkflowStep("data-ingestion", "ingestion-service", "ingest", null, 10, 3)
            );
            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "Single Step Workflow",
                    "Single step workflow",
                    singleStep,
                    List.of(),
                    null
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should create workflow with many steps")
        void shouldCreateWorkflowWithManySteps() throws Exception {
            List<WorkflowStep> manySteps = new java.util.ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                manySteps.add(new WorkflowStep("step-" + i, "service-" + i, "action-" + i, null, 5, 3));
            }

            CreateWorkflowRequestDto request = new CreateWorkflowRequestDto(
                    "Complex Workflow",
                    "Complex workflow with many steps",
                    manySteps,
                    List.of(),
                    120
            );

            when(workflowService.createWorkflow(eq("tenant-001"), any()))
                    .thenReturn(workflowResponse);

            mockMvc.perform(post("/api/v1/orchestration/workflows")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }
}
