package com.gogidix.centralconfiguration.featureflagservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.centralconfiguration.featureflagservice.application.service.FeatureFlagService;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlag;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlagEvaluation;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.RolloutStrategy;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureFlagController REST API Tests")
class FeatureFlagControllerTest {

    @Mock
    private FeatureFlagService featureFlagService;

    private MockMvc mockMvc;
    private FeatureFlagController controller;
    private ObjectMapper objectMapper;

    private static final String TENANT_ID = "tenant-001";
    private static final Long FLAG_ID = 1L;
    private static final String FLAG_KEY = "new-ui-feature";

    @BeforeEach
    void setUp() {
        controller = new FeatureFlagController(featureFlagService);
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler() {})
                .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(objectMapper))
                .setValidator(new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean())
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(featureFlagService);
    }

    @Nested
    @DisplayName("POST /api/v1/feature-flags")
    class CreateFlagTests {

        @Test
        @DisplayName("Should create feature flag successfully")
        void shouldCreateFeatureFlagSuccessfully() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);

            mockMvc.perform(post("/api/v1/feature-flags")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.flagKey").value(FLAG_KEY));

            verify(featureFlagService).createFeatureFlag(any());
        }

        @Test
        @DisplayName("Should return 201 with Location header")
        void shouldReturn201WithLocationHeader() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);

            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(header().string("Location", "/api/v1/feature-flags/1"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"ALL_USERS", "PERCENTAGE", "WHITELIST", "GRADUAL", "BETA_TESTERS", "INTERNAL"})
        @DisplayName("Should accept all rollout strategies")
        void shouldAcceptAllRolloutStrategies(String strategy) throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);

            Map<String, Object> request = validCreateRequest();
            request.put("rolloutStrategy", strategy);

            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should use default tenant ID header")
        void shouldUseDefaultTenantIdHeader() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);

            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated());

            verify(featureFlagService).createFeatureFlag(argThat(cmd ->
                    ((String) cmd.tenantId()).equals("default")
            ));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/feature-flags/evaluate")
    class EvaluateFlagTests {

        @Test
        @DisplayName("Should evaluate flag successfully")
        void shouldEvaluateFlagSuccessfully() throws Exception {
            FeatureFlagEvaluation evaluation = FeatureFlagEvaluation.builder()
                    .flagKey(FLAG_KEY)
                    .enabled(true)
                    .reason("Feature flag is enabled")
                    .userId("user-001")
                    .tenantId(TENANT_ID)
                    .build();

            when(featureFlagService.evaluateFlag(any())).thenReturn(evaluation);

            Map<String, Object> request = Map.of(
                    "flagKey", FLAG_KEY,
                    "userId", "user-001",
                    "context", Map.of("role", "admin")
            );

            mockMvc.perform(post("/api/v1/feature-flags/evaluate")
                            .header("X-Tenant-ID", TENANT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.enabled").value(true))
                    .andExpect(jsonPath("$.flagKey").value(FLAG_KEY));

            verify(featureFlagService).evaluateFlag(any());
        }

        @Test
        @DisplayName("Should return evaluation with disabled status")
        void shouldReturnEvaluationWithDisabledStatus() throws Exception {
            FeatureFlagEvaluation evaluation = FeatureFlagEvaluation.builder()
                    .flagKey(FLAG_KEY)
                    .enabled(false)
                    .reason("Flag is disabled")
                    .userId("user-001")
                    .tenantId(TENANT_ID)
                    .build();

            when(featureFlagService.evaluateFlag(any())).thenReturn(evaluation);

            Map<String, Object> request = Map.of(
                    "flagKey", FLAG_KEY,
                    "userId", "user-001"
            );

            mockMvc.perform(post("/api/v1/feature-flags/evaluate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.enabled").value(false));
        }

        @Test
        @DisplayName("Should accept null context")
        void shouldAcceptNullContext() throws Exception {
            FeatureFlagEvaluation evaluation = FeatureFlagEvaluation.builder()
                    .flagKey(FLAG_KEY)
                    .enabled(true)
                    .userId("user-001")
                    .tenantId(TENANT_ID)
                    .build();

            when(featureFlagService.evaluateFlag(any())).thenReturn(evaluation);

            Map<String, Object> request = new HashMap<>();
            request.put("flagKey", FLAG_KEY);
            request.put("userId", "user-001");
            request.put("context", null);

            mockMvc.perform(post("/api/v1/feature-flags/evaluate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/feature-flags")
    class GetFlagsTests {

        @Test
        @DisplayName("Should get all flags")
        void shouldGetAllFlags() throws Exception {
            List<FeatureFlag> flags = List.of(
                    createMockFeatureFlag(1L, "feature-1"),
                    createMockFeatureFlag(2L, "feature-2")
            );
            when(featureFlagService.getFeatureFlags(eq(TENANT_ID))).thenReturn(flags);

            mockMvc.perform(get("/api/v1/feature-flags")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());

            verify(featureFlagService).getFeatureFlags(eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should return empty list when no flags")
        void shouldReturnEmptyListWhenNoFlags() throws Exception {
            when(featureFlagService.getFeatureFlags(anyString())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/feature-flags")
                            .header("X-Tenant-ID", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should use default tenant ID")
        void shouldUseDefaultTenantId() throws Exception {
            when(featureFlagService.getFeatureFlags(eq("default"))).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/feature-flags"))
                    .andExpect(status().isOk());

            verify(featureFlagService).getFeatureFlags(eq("default"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/feature-flags/{flagId}")
    class GetFlagTests {

        @Test
        @DisplayName("Should get flag by ID")
        void shouldGetFlagById() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.getFeatureFlag(eq(FLAG_ID))).thenReturn(flag);

            mockMvc.perform(get("/api/v1/feature-flags/" + FLAG_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.flagKey").value(FLAG_KEY));

            verify(featureFlagService).getFeatureFlag(eq(FLAG_ID));
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 100L, 999L})
        @DisplayName("Should accept various flag IDs")
        void shouldAcceptVariousFlagIds(Long flagId) throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.getFeatureFlag(eq(flagId))).thenReturn(flag);

            mockMvc.perform(get("/api/v1/feature-flags/" + flagId))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/feature-flags/{flagId}/toggle")
    class ToggleFlagTests {

        @Test
        @DisplayName("Should enable flag")
        void shouldEnableFlag() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            flag.setIsEnabled(true);
            when(featureFlagService.toggleFlag(eq(FLAG_ID), eq(true), eq("admin"))).thenReturn(flag);

            mockMvc.perform(put("/api/v1/feature-flags/" + FLAG_ID + "/toggle")
                            .param("enabled", "true")
                            .header("X-User-ID", "admin"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isEnabled").value(true));

            verify(featureFlagService).toggleFlag(eq(FLAG_ID), eq(true), eq("admin"));
        }

        @Test
        @DisplayName("Should disable flag")
        void shouldDisableFlag() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            flag.setIsEnabled(false);
            when(featureFlagService.toggleFlag(eq(FLAG_ID), eq(false), anyString())).thenReturn(flag);

            mockMvc.perform(put("/api/v1/feature-flags/" + FLAG_ID + "/toggle")
                            .param("enabled", "false"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isEnabled").value(false));

            verify(featureFlagService).toggleFlag(eq(FLAG_ID), eq(false), anyString());
        }

        @Test
        @DisplayName("Should use default user ID")
        void shouldUseDefaultUserId() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.toggleFlag(eq(FLAG_ID), eq(true), eq("system"))).thenReturn(flag);

            mockMvc.perform(put("/api/v1/feature-flags/" + FLAG_ID + "/toggle")
                            .param("enabled", "true"))
                    .andExpect(status().isOk());

            verify(featureFlagService).toggleFlag(eq(FLAG_ID), eq(true), eq("system"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/feature-flags/{flagId}")
    class DeleteFlagTests {

        @Test
        @DisplayName("Should delete flag successfully")
        void shouldDeleteFlagSuccessfully() throws Exception {
            doNothing().when(featureFlagService).deleteFeatureFlag(eq(FLAG_ID));

            mockMvc.perform(delete("/api/v1/feature-flags/" + FLAG_ID))
                    .andExpect(status().isNoContent());

            verify(featureFlagService).deleteFeatureFlag(eq(FLAG_ID));
        }

        @Test
        @DisplayName("Should return 204 No Content")
        void shouldReturn204NoContent() throws Exception {
            doNothing().when(featureFlagService).deleteFeatureFlag(anyLong());

            mockMvc.perform(delete("/api/v1/feature-flags/1"))
                    .andExpect(status().isNoContent());
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 100L})
        @DisplayName("Should accept various flag IDs")
        void shouldAcceptVariousFlagIds(Long flagId) throws Exception {
            doNothing().when(featureFlagService).deleteFeatureFlag(eq(flagId));

            mockMvc.perform(delete("/api/v1/feature-flags/" + flagId))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle create then evaluate workflow")
        void shouldHandleCreateThenEvaluateWorkflow() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            FeatureFlagEvaluation evaluation = FeatureFlagEvaluation.builder()
                    .flagKey(FLAG_KEY)
                    .enabled(true)
                    .reason("Enabled")
                    .userId("user-001")
                    .tenantId(TENANT_ID)
                    .build();

            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);
            when(featureFlagService.evaluateFlag(any())).thenReturn(evaluation);

            // Create
            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated());

            // Evaluate
            Map<String, Object> evaluateRequest = Map.of(
                    "flagKey", FLAG_KEY,
                    "userId", "user-001"
            );

            mockMvc.perform(post("/api/v1/feature-flags/evaluate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(evaluateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.enabled").value(true));

            verify(featureFlagService).createFeatureFlag(any());
            verify(featureFlagService).evaluateFlag(any());
        }

        @Test
        @DisplayName("Should handle create then toggle workflow")
        void shouldHandleCreateThenToggleWorkflow() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);
            when(featureFlagService.toggleFlag(eq(FLAG_ID), eq(true), anyString())).thenReturn(flag);

            // Create
            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated());

            // Toggle
            mockMvc.perform(put("/api/v1/feature-flags/" + FLAG_ID + "/toggle")
                            .param("enabled", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isEnabled").value(true));

            verify(featureFlagService).createFeatureFlag(any());
            verify(featureFlagService).toggleFlag(eq(FLAG_ID), eq(true), anyString());
        }

        @Test
        @DisplayName("Should handle create then delete workflow")
        void shouldHandleCreateThenDeleteWorkflow() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);
            doNothing().when(featureFlagService).deleteFeatureFlag(eq(FLAG_ID));

            // Create
            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated());

            // Delete
            mockMvc.perform(delete("/api/v1/feature-flags/" + FLAG_ID))
                    .andExpect(status().isNoContent());

            verify(featureFlagService).createFeatureFlag(any());
            verify(featureFlagService).deleteFeatureFlag(eq(FLAG_ID));
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept application/json")
        void shouldAcceptApplicationJson() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);

            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateRequest())))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should return application/json")
        void shouldReturnApplicationJson() throws Exception {
            List<FeatureFlag> flags = List.of(createMockFeatureFlag());
            when(featureFlagService.getFeatureFlags(anyString())).thenReturn(flags);

            mockMvc.perform(get("/api/v1/feature-flags"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long flag key")
        void shouldHandleVeryLongFlagKey() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.createFeatureFlag(any())).thenReturn(flag);

            Map<String, Object> request = validCreateRequest();
            request.put("flagKey", "feature-" + "x".repeat(200));

            mockMvc.perform(post("/api/v1/feature-flags")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle special characters in flag key")
        void shouldHandleSpecialCharactersInFlagKey() throws Exception {
            FeatureFlag flag = createMockFeatureFlag();
            when(featureFlagService.getFeatureFlag(anyLong())).thenReturn(flag);

            mockMvc.perform(get("/api/v1/feature-flags/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle null context in evaluation")
        void shouldHandleNullContextInEvaluation() throws Exception {
            FeatureFlagEvaluation evaluation = FeatureFlagEvaluation.builder()
                    .flagKey(FLAG_KEY)
                    .enabled(true)
                    .build();

            when(featureFlagService.evaluateFlag(any())).thenReturn(evaluation);

            Map<String, Object> request = new HashMap<>();
            request.put("flagKey", FLAG_KEY);
            request.put("userId", "user-001");
            request.put("context", null);

            mockMvc.perform(post("/api/v1/feature-flags/evaluate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    private Map<String, Object> validCreateRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("tenantId", "default");
        request.put("flagKey", FLAG_KEY);
        request.put("name", "Test Feature");
        request.put("isEnabled", true);
        request.put("rolloutPercentage", 100);
        request.put("rolloutStrategy", "ALL_USERS");
        return request;
    }

    private FeatureFlag createMockFeatureFlag() {
        return createMockFeatureFlag(FLAG_ID, FLAG_KEY);
    }

    private FeatureFlag createMockFeatureFlag(Long id, String key) {
        return FeatureFlag.builder()
                .id(id)
                .tenantId(TENANT_ID)
                .flagKey(key)
                .name("Feature " + key)
                .description("Description for " + key)
                .isEnabled(true)
                .rolloutPercentage(100)
                .rolloutStrategy(RolloutStrategy.ALL_USERS)
                .isSticky(false)
                .build();
    }
}
