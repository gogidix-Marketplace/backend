package com.gogidix.centralconfiguration.environmentservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.centralconfiguration.environmentservice.application.service.EnvironmentService;
import com.gogidix.centralconfiguration.environmentservice.domain.model.Environment;
import com.gogidix.centralconfiguration.environmentservice.domain.model.EnvironmentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnvironmentController REST API Tests")
class EnvironmentControllerTest {

    @Mock
    private EnvironmentService environmentService;

    private MockMvc mockMvc;
    private EnvironmentController controller;
    private ObjectMapper objectMapper;

    private static final String TENANT_ID = "tenant-001";
    private static final String USER_ID = "admin";
    private static final Long ENVIRONMENT_ID = 1L;
    private static final String ENVIRONMENT_NAME = "production";

    @BeforeEach
    void setUp() {
        controller = new EnvironmentController(environmentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
        reset(environmentService);
    }

    @Nested
    @DisplayName("POST /api/v1/environments")
    class CreateEnvironmentTests {

        @Test
        @DisplayName("Should create environment successfully")
        void shouldCreateEnvironmentSuccessfully() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of(
                    "environmentName", ENVIRONMENT_NAME,
                    "displayName", "Production Environment",
                    "description", "Production environment",
                    "type", "PRODUCTION"
            );

            mockMvc.perform(post("/api/v1/environments")
                            .header("X-Tenant-ID", TENANT_ID)
                            .header("X-User-ID", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.environmentName").value(ENVIRONMENT_NAME));

            verify(environmentService).createEnvironment(eq(TENANT_ID), eq(ENVIRONMENT_NAME), any(), any(), any(), eq(USER_ID));
        }

        @Test
        @DisplayName("Should return 201 with Location header")
        void shouldReturn201WithLocationHeader() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of(
                    "environmentName", ENVIRONMENT_NAME,
                    "displayName", "Production",
                    "description", "Production env",
                    "type", "PRODUCTION"
            );

            mockMvc.perform(post("/api/v1/environments")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", "/api/v1/environments/1"));
        }

        @Test
        @DisplayName("Should accept DEVELOPMENT as default type")
        void shouldAcceptDEVELOPMENTAsDefaultType() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of(
                    "environmentName", "dev",
                    "displayName", "Development"
            );

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(environmentService).createEnvironment(any(), any(), any(), any(), eq("DEVELOPMENT"), any());
        }

        @ParameterizedTest
        @ValueSource(strings = {"DEVELOPMENT", "STAGING", "QA", "UAT", "PRODUCTION", "DR"})
        @DisplayName("Should accept all environment types")
        void shouldAcceptAllEnvironmentTypes(String type) throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of(
                    "environmentName", "env",
                    "displayName", "Environment",
                    "type", type
            );

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should use default tenant ID header")
        void shouldUseDefaultTenantIdHeader() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of("environmentName", "test", "displayName", "Test");

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(environmentService).createEnvironment(eq("default"), any(), any(), any(), any(), eq("system"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/environments")
    class GetEnvironmentsTests {

        @Test
        @DisplayName("Should get all environments")
        void shouldGetAllEnvironments() throws Exception {
            List<Environment> environments = List.of(
                    createMockEnvironment(1L, "production"),
                    createMockEnvironment(2L, "staging")
            );
            when(environmentService.getEnvironments(eq(TENANT_ID))).thenReturn(environments);

            mockMvc.perform(get("/api/v1/environments")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isArray());

            verify(environmentService).getEnvironments(eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200Ok() throws Exception {
            when(environmentService.getEnvironments(anyString())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/environments")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return empty list when no environments")
        void shouldReturnEmptyListWhenNoEnvironments() throws Exception {
            when(environmentService.getEnvironments(anyString())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/environments")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should use default tenant ID")
        void shouldUseDefaultTenantId() throws Exception {
            when(environmentService.getEnvironments(eq("default"))).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/environments"))
                    .andExpect(status().isOk());

            verify(environmentService).getEnvironments(eq("default"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/environments/{environmentName}")
    class GetEnvironmentTests {

        @Test
        @DisplayName("Should get environment by name")
        void shouldGetEnvironmentByName() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.getEnvironment(eq(ENVIRONMENT_NAME))).thenReturn(environment);

            mockMvc.perform(get("/api/v1/environments/" + ENVIRONMENT_NAME))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.environmentName").value(ENVIRONMENT_NAME));

            verify(environmentService).getEnvironment(eq(ENVIRONMENT_NAME));
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200OkForSingleEnvironment() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.getEnvironment(anyString())).thenReturn(environment);

            mockMvc.perform(get("/api/v1/environments/test-env"))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @ValueSource(strings = {"dev", "staging", "qa", "uat", "production", "dr"})
        @DisplayName("Should accept various environment names")
        void shouldAcceptVariousEnvironmentNames(String envName) throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.getEnvironment(eq(envName))).thenReturn(environment);

            mockMvc.perform(get("/api/v1/environments/" + envName))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/environments/{environmentId}")
    class UpdateEnvironmentTests {

        @Test
        @DisplayName("Should update environment successfully")
        void shouldUpdateEnvironmentSuccessfully() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.updateEnvironment(eq(ENVIRONMENT_ID), anyString(), anyString(), anyBoolean()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of(
                    "displayName", "Updated Display",
                    "description", "Updated description",
                    "isActive", true
            );

            mockMvc.perform(put("/api/v1/environments/" + ENVIRONMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));

            verify(environmentService).updateEnvironment(eq(ENVIRONMENT_ID), eq("Updated Display"), eq("Updated description"), eq(true));
        }

        @Test
        @DisplayName("Should accept partial update")
        void shouldAcceptPartialUpdate() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.updateEnvironment(eq(ENVIRONMENT_ID), eq("New Name"), isNull(), isNull()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of("displayName", "New Name");

            mockMvc.perform(put("/api/v1/environments/" + ENVIRONMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept isActive only update")
        void shouldAcceptIsActiveOnlyUpdate() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.updateEnvironment(eq(ENVIRONMENT_ID), isNull(), isNull(), eq(false)))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of("isActive", false);

            mockMvc.perform(put("/api/v1/environments/" + ENVIRONMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(environmentService).updateEnvironment(eq(ENVIRONMENT_ID), isNull(), isNull(), eq(false));
        }

        @ParameterizedTest
        @ValueSource(booleans = {true, false})
        @DisplayName("Should accept both isActive values")
        void shouldAcceptBothIsActiveValues(Boolean isActive) throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.updateEnvironment(eq(ENVIRONMENT_ID), isNull(), isNull(), eq(isActive)))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of("isActive", isActive);

            mockMvc.perform(put("/api/v1/environments/" + ENVIRONMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/environments/{environmentId}")
    class DeleteEnvironmentTests {

        @Test
        @DisplayName("Should delete environment successfully")
        void shouldDeleteEnvironmentSuccessfully() throws Exception {
            doNothing().when(environmentService).deleteEnvironment(eq(ENVIRONMENT_ID));

            mockMvc.perform(delete("/api/v1/environments/" + ENVIRONMENT_ID))
                    .andExpect(status().isNoContent());

            verify(environmentService).deleteEnvironment(eq(ENVIRONMENT_ID));
        }

        @Test
        @DisplayName("Should return 204 No Content")
        void shouldReturn204NoContent() throws Exception {
            doNothing().when(environmentService).deleteEnvironment(anyLong());

            mockMvc.perform(delete("/api/v1/environments/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should accept various environment IDs")
        void shouldAcceptVariousEnvironmentIds() throws Exception {
            doNothing().when(environmentService).deleteEnvironment(anyLong());

            Long[] ids = {1L, 2L, 100L, 999L};

            for (Long id : ids) {
                mockMvc.perform(delete("/api/v1/environments/" + id))
                        .andExpect(status().isNoContent());
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle create then read workflow")
        void shouldHandleCreateThenReadWorkflow() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);
            when(environmentService.getEnvironment(eq(ENVIRONMENT_NAME))).thenReturn(environment);

            Map<String, Object> createRequest = Map.of(
                    "environmentName", ENVIRONMENT_NAME,
                    "displayName", "Production",
                    "type", "PRODUCTION"
            );

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated());

            mockMvc.perform(get("/api/v1/environments/" + ENVIRONMENT_NAME))
                    .andExpect(status().isOk());

            verify(environmentService).createEnvironment(any(), any(), any(), any(), any(), any());
            verify(environmentService).getEnvironment(eq(ENVIRONMENT_NAME));
        }

        @Test
        @DisplayName("Should handle create then update workflow")
        void shouldHandleCreateThenUpdateWorkflow() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);
            lenient().when(environmentService.updateEnvironment(any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> createRequest = Map.of(
                    "environmentName", ENVIRONMENT_NAME,
                    "displayName", "Production"
            );

            Map<String, Object> updateRequest = Map.of("displayName", "Updated Production");

            mockMvc.perform(post("/api/v1/environments")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated());

            mockMvc.perform(put("/api/v1/environments/" + ENVIRONMENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk());

            verify(environmentService).createEnvironment(any(), any(), any(), any(), any(), any());
            verify(environmentService).updateEnvironment(eq(ENVIRONMENT_ID), any(), any(), any());
        }

        @Test
        @DisplayName("Should handle create then delete workflow")
        void shouldHandleCreateThenDeleteWorkflow() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);
            doNothing().when(environmentService).deleteEnvironment(eq(ENVIRONMENT_ID));

            Map<String, Object> createRequest = Map.of(
                    "environmentName", ENVIRONMENT_NAME,
                    "displayName", "Production"
            );

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated());

            mockMvc.perform(delete("/api/v1/environments/" + ENVIRONMENT_ID))
                    .andExpect(status().isNoContent());

            verify(environmentService).createEnvironment(any(), any(), any(), any(), any(), any());
            verify(environmentService).deleteEnvironment(eq(ENVIRONMENT_ID));
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept application/json")
        void shouldAcceptApplicationJson() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            Map<String, Object> request = Map.of("environmentName", "test", "displayName", "Test");

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should return application/json")
        void shouldReturnApplicationJson() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.getEnvironments(anyString())).thenReturn(List.of(environment));

            mockMvc.perform(get("/api/v1/environments"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long environment name")
        void shouldHandleVeryLongEnvironmentName() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            String longName = "environment-" + "x".repeat(200);
            Map<String, Object> request = Map.of(
                    "environmentName", longName,
                    "displayName", "Long Name Environment"
            );

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long display name")
        void shouldHandleVeryLongDisplayName() throws Exception {
            Environment environment = createMockEnvironment();
            lenient().when(environmentService.createEnvironment(any(), any(), any(), any(), any(), any()))
                    .thenReturn(environment);

            String longDisplayName = "Very Long Display Name ".repeat(10);
            Map<String, Object> request = Map.of(
                    "environmentName", "test",
                    "displayName", longDisplayName
            );

            mockMvc.perform(post("/api/v1/environments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle special characters in environment name")
        void shouldHandleSpecialCharactersInEnvironmentName() throws Exception {
            Environment environment = createMockEnvironment();
            when(environmentService.getEnvironment(eq("prod-env-v2"))).thenReturn(environment);

            mockMvc.perform(get("/api/v1/environments/prod-env-v2"))
                    .andExpect(status().isOk());
        }
    }

    private Environment createMockEnvironment() {
        return createMockEnvironment(ENVIRONMENT_ID, ENVIRONMENT_NAME);
    }

    private Environment createMockEnvironment(Long id, String name) {
        return Environment.builder()
                .id(id)
                .tenantId(TENANT_ID)
                .environmentName(name)
                .displayName("Environment " + name)
                .environmentType(EnvironmentType.PRODUCTION)
                .isActive(true)
                .priority(100)
                .build();
    }
}
