package com.gogidix.platform.platform.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.platform.platform.application.dto.FeatureFlagDto;
import com.gogidix.platform.platform.application.service.FeatureFlagService;
import com.gogidix.platform.platform.domain.port.in.CreateFeatureFlagCommand;
import com.gogidix.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureFlagController REST API Tests")
class FeatureFlagControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private FeatureFlagService featureFlagService;

    @InjectMocks
    private FeatureFlagController featureFlagController;

    private FeatureFlagDto testFlagDto;
    private CreateFeatureFlagCommand createCommand;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper = mapper;

        mockMvc = MockMvcBuilders.standaloneSetup(featureFlagController)
            .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
            .build();

        Map<String, Object> rolloutRules = new HashMap<>();
        rolloutRules.put("percentage", 50);

        testFlagDto = FeatureFlagDto.builder()
            .id("flag-id-123")
            .tenantId("tenant-123")
            .featureKey("new_dashboard_v2")
            .featureName("New Dashboard V2")
            .description("New dashboard interface")
            .featureType("FEATURE")
            .enabled(true)
            .allowedTenants(new String[]{"tenant1", "tenant2"})
            .deniedTenants(new String[]{})
            .userSegments(Arrays.asList("beta", "internal"))
            .rolloutRules(rolloutRules)
            .requiresOptIn(false)
            .rolloutPercentage(100)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        createCommand = CreateFeatureFlagCommand.builder()
            .featureKey("new_dashboard_v2")
            .featureName("New Dashboard V2")
            .description("New dashboard interface")
            .featureType(com.gogidix.platform.platform.domain.model.FeatureFlag.FeatureType.FEATURE)
            .enabled(true)
            .userSegments(Arrays.asList("beta", "internal"))
            .rolloutRules(rolloutRules)
            .requiresOptIn(false)
            .build();
    }

    @Test
    @DisplayName("POST /api/v1/platform/feature-flags - Should create feature flag")
    void createFeatureFlag_Success() throws Exception {
        when(featureFlagService.createFeatureFlag(any(CreateFeatureFlagCommand.class)))
            .thenReturn(testFlagDto);

        mockMvc.perform(post("/api/v1/platform/feature-flags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommand)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.featureKey").value("new_dashboard_v2"))
            .andExpect(jsonPath("$.featureName").value("New Dashboard V2"))
            .andExpect(jsonPath("$.enabled").value(true));

        verify(featureFlagService).createFeatureFlag(any(CreateFeatureFlagCommand.class));
    }

    @Test
    @DisplayName("GET /api/v1/platform/feature-flags/{key} - Should get feature flag by key")
    void getFeatureFlag_Success() throws Exception {
        when(featureFlagService.getFeatureFlag("new_dashboard_v2"))
            .thenReturn(testFlagDto);

        mockMvc.perform(get("/api/v1/platform/feature-flags/new_dashboard_v2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.featureKey").value("new_dashboard_v2"))
            .andExpect(jsonPath("$.featureName").value("New Dashboard V2"));

        verify(featureFlagService).getFeatureFlag("new_dashboard_v2");
    }

    @Test
    @DisplayName("GET /api/v1/platform/feature-flags/{key}/enabled - Should check if feature is enabled")
    void isFeatureEnabled_Success() throws Exception {
        when(featureFlagService.isFeatureEnabled("new_dashboard_v2", "user123"))
            .thenReturn(true);

        mockMvc.perform(get("/api/v1/platform/feature-flags/new_dashboard_v2/enabled")
                .param("userId", "user123"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(featureFlagService).isFeatureEnabled("new_dashboard_v2", "user123");
    }

    @Test
    @DisplayName("GET /api/v1/platform/feature-flags/{key}/enabled - Should check enabled without userId")
    void isFeatureEnabled_NoUserId_Success() throws Exception {
        when(featureFlagService.isFeatureEnabled("new_dashboard_v2", null))
            .thenReturn(true);

        mockMvc.perform(get("/api/v1/platform/feature-flags/new_dashboard_v2/enabled"))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(featureFlagService).isFeatureEnabled("new_dashboard_v2", null);
    }

    @Test
    @DisplayName("GET /api/v1/platform/feature-flags - Should get all feature flags")
    void getAllFeatureFlags_Success() throws Exception {
        FeatureFlagDto flag2 = FeatureFlagDto.builder()
            .id("flag-id-456")
            .tenantId("tenant-123")
            .featureKey("another_feature")
            .featureName("Another Feature")
            .enabled(false)
            .rolloutPercentage(0)
            .build();

        when(featureFlagService.getAllFeatureFlags())
            .thenReturn(Arrays.asList(testFlagDto, flag2));

        mockMvc.perform(get("/api/v1/platform/feature-flags"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].featureKey").value("new_dashboard_v2"))
            .andExpect(jsonPath("$[1].featureKey").value("another_feature"));

        verify(featureFlagService).getAllFeatureFlags();
    }

    @Test
    @DisplayName("PUT /api/v1/platform/feature-flags/{id}/toggle - Should toggle feature flag on")
    void toggleFeatureFlag_Enable_Success() throws Exception {
        testFlagDto.setEnabled(true);
        when(featureFlagService.toggleFeatureFlag("flag-id-123", true))
            .thenReturn(testFlagDto);

        mockMvc.perform(put("/api/v1/platform/feature-flags/flag-id-123/toggle")
                .param("enabled", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.enabled").value(true));

        verify(featureFlagService).toggleFeatureFlag("flag-id-123", true);
    }

    @Test
    @DisplayName("PUT /api/v1/platform/feature-flags/{id}/toggle - Should toggle feature flag off")
    void toggleFeatureFlag_Disable_Success() throws Exception {
        testFlagDto.setEnabled(false);
        when(featureFlagService.toggleFeatureFlag("flag-id-123", false))
            .thenReturn(testFlagDto);

        mockMvc.perform(put("/api/v1/platform/feature-flags/flag-id-123/toggle")
                .param("enabled", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.enabled").value(false));

        verify(featureFlagService).toggleFeatureFlag("flag-id-123", false);
    }

    @Test
    @DisplayName("DELETE /api/v1/platform/feature-flags/{id} - Should delete feature flag")
    void deleteFeatureFlag_Success() throws Exception {
        doNothing().when(featureFlagService).deleteFeatureFlag("flag-id-123");

        mockMvc.perform(delete("/api/v1/platform/feature-flags/flag-id-123"))
            .andExpect(status().isNoContent());

        verify(featureFlagService).deleteFeatureFlag("flag-id-123");
    }

    @Test
    @DisplayName("GET /api/v1/platform/feature-flags/{key} - Should propagate NotFoundException when not found")
    void getFeatureFlag_NotFound() throws Exception {
        when(featureFlagService.getFeatureFlag("nonexistent"))
            .thenThrow(new NotFoundException("Feature flag not found: nonexistent"));

        Exception caught = null;
        try {
            mockMvc.perform(get("/api/v1/platform/feature-flags/nonexistent"));
        } catch (Exception e) {
            caught = e;
        }
        assertThat(caught).isNotNull();
        assertThat(caught.getCause()).isInstanceOf(NotFoundException.class);

        verify(featureFlagService).getFeatureFlag("nonexistent");
    }

    @Test
    @DisplayName("DELETE /api/v1/platform/feature-flags/{id} - Should propagate NotFoundException when deleting non-existent flag")
    void deleteFeatureFlag_NotFound() throws Exception {
        doThrow(new NotFoundException("Feature flag not found: nonexistent"))
            .when(featureFlagService).deleteFeatureFlag("nonexistent");

        Exception caught = null;
        try {
            mockMvc.perform(delete("/api/v1/platform/feature-flags/nonexistent"));
        } catch (Exception e) {
            caught = e;
        }
        assertThat(caught).isNotNull();
        assertThat(caught.getCause()).isInstanceOf(NotFoundException.class);

        verify(featureFlagService).deleteFeatureFlag("nonexistent");
    }
}
