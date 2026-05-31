package com.gogidix.centralconfiguration.configserver.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.centralconfiguration.configserver.application.dto.request.CreateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.request.UpdateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigHistoryResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.PagedConfigResponseDto;
import com.gogidix.centralconfiguration.configserver.application.service.ConfigCommandService;
import com.gogidix.centralconfiguration.configserver.application.service.ConfigQueryService;
import com.gogidix.centralconfiguration.configserver.domain.port.in.SearchConfigsQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigController Tests")
class ConfigControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ConfigCommandService commandService;

    @Mock
    private ConfigQueryService queryService;

    @InjectMocks
    private ConfigController configController;

    private ConfigurationResponseDto responseDto;
    private CreateConfigRequestDto createRequestDto;
    private UpdateConfigRequestDto updateRequestDto;
    private ConfigHistoryResponseDto historyResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(configController).build();

        responseDto = ConfigurationResponseDto.builder()
                .id(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .description("Database connection timeout")
                .version(1)
                .isActive(true)
                .createdBy("admin")
                .createdAt(LocalDateTime.now())
                .build();

        createRequestDto = CreateConfigRequestDto.builder()
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .description("Database connection timeout")
                .build();

        updateRequestDto = UpdateConfigRequestDto.builder()
                .configValue("60000")
                .isEncrypted(false)
                .description("Updated timeout")
                .changeReason("Performance optimization")
                .build();

        historyResponseDto = ConfigHistoryResponseDto.builder()
                .id(1L)
                .configurationId(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .oldValue("20000")
                .newValue("30000")
                .version(1)
                .changeType("UPDATE")
                .changedBy("admin")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/configs - Should create configuration successfully")
    void createConfig_Success() throws Exception {
        when(commandService.createConfig(any(), anyString(), anyString())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/configs")
                        .header("X-Tenant-ID", "tenant-1")
                        .header("X-User-ID", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.applicationName").value("payment-service"))
                .andExpect(jsonPath("$.configKey").value("database.timeout"))
                .andExpect(jsonPath("$.configValue").value("30000"));

        verify(commandService).createConfig(any(), eq("tenant-1"), eq("admin"));
    }

    @Test
    @DisplayName("POST /api/v1/configs - Should use default tenant when header not provided")
    void createConfig_NoTenantHeader_UsesDefault() throws Exception {
        when(commandService.createConfig(any(), eq("default"), eq("system"))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated());

        verify(commandService).createConfig(any(), eq("default"), eq("system"));
    }

    @Test
    @DisplayName("GET /api/v1/configs/{configId} - Should get configuration by ID")
    void getConfig_Success() throws Exception {
        when(queryService.getConfigById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/configs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.applicationName").value("payment-service"));

        verify(queryService).getConfigById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/configs/by-key - Should get configuration by composite key")
    void getConfigByKey_Success() throws Exception {
        when(queryService.getConfigByKey(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/configs/by-key")
                        .param("application", "payment-service")
                        .param("profile", "prod")
                        .param("key", "database.timeout")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configKey").value("database.timeout"));

        verify(queryService).getConfigByKey("tenant-1", "payment-service", "prod", "database.timeout");
    }

    @Test
    @DisplayName("GET /api/v1/configs/application/{application}/profile/{profile} - Should get all configs")
    void getConfigsByApplicationAndProfile_Success() throws Exception {
        List<ConfigurationResponseDto> configs = Arrays.asList(
                responseDto,
                ConfigurationResponseDto.builder()
                        .id(2L)
                        .applicationName("payment-service")
                        .profile("prod")
                        .configKey("another.key")
                        .configValue("value2")
                        .build()
        );
        when(queryService.getConfigsByApplicationAndProfile(anyString(), anyString(), anyString()))
                .thenReturn(configs);

        mockMvc.perform(get("/api/v1/configs/application/payment-service/profile/prod")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(queryService).getConfigsByApplicationAndProfile("tenant-1", "payment-service", "prod");
    }

    @Test
    @DisplayName("GET /api/v1/configs - Should search configurations")
    void searchConfigs_Success() throws Exception {
        PagedConfigResponseDto<ConfigurationResponseDto> pagedResponse = PagedConfigResponseDto.<ConfigurationResponseDto>builder()
                .items(Arrays.asList(responseDto))
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .isFirst(true)
                .isLast(true)
                .build();
        when(queryService.searchConfigs(any(SearchConfigsQuery.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/configs")
                        .param("application", "payment-service")
                        .param("profile", "prod")
                        .param("isActive", "true")
                        .param("page", "0")
                        .param("size", "20")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.isFirst").value(true))
                .andExpect(jsonPath("$.isLast").value(true));

        verify(queryService).searchConfigs(any(SearchConfigsQuery.class));
    }

    @Test
    @DisplayName("PUT /api/v1/configs/{configId} - Should update configuration")
    void updateConfig_Success() throws Exception {
        ConfigurationResponseDto updatedResponse = ConfigurationResponseDto.builder()
                .id(1L)
                .applicationName("payment-service")
                .configKey("database.timeout")
                .configValue("60000")
                .build();
        when(commandService.updateConfig(anyLong(), any(), anyString(), anyString())).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/configs/1")
                        .header("X-Tenant-ID", "tenant-1")
                        .header("X-User-ID", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.configValue").value("60000"));

        verify(commandService).updateConfig(eq(1L), any(), eq("tenant-1"), eq("admin"));
    }

    @Test
    @DisplayName("DELETE /api/v1/configs/{configId} - Should delete configuration")
    void deleteConfig_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/configs/1")
                        .header("X-Tenant-ID", "tenant-1")
                        .header("X-User-ID", "admin"))
                .andExpect(status().isNoContent());

        verify(commandService).deleteConfig(1L, "tenant-1", "admin");
    }

    @Test
    @DisplayName("GET /api/v1/configs/{configId}/history - Should get configuration history")
    void getConfigHistory_Success() throws Exception {
        List<ConfigHistoryResponseDto> historyList = Arrays.asList(
                historyResponseDto,
                ConfigHistoryResponseDto.builder()
                        .id(2L)
                        .configurationId(1L)
                        .changeType("CREATE")
                        .changedBy("admin")
                        .build()
        );
        when(queryService.getConfigHistory(1L)).thenReturn(historyList);

        mockMvc.perform(get("/api/v1/configs/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].changeType").value("UPDATE"));

        verify(queryService).getConfigHistory(1L);
    }

    @Test
    @DisplayName("GET /api/v1/configs - Should use default pagination values")
    void searchConfigs_DefaultPagination() throws Exception {
        PagedConfigResponseDto<ConfigurationResponseDto> pagedResponse = PagedConfigResponseDto.<ConfigurationResponseDto>builder()
                .items(List.of())
                .page(0)
                .size(20)
                .totalElements(0L)
                .totalPages(0)
                .isFirst(true)
                .isLast(true)
                .build();
        when(queryService.searchConfigs(any(SearchConfigsQuery.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/configs")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    @DisplayName("GET /api/v1/configs - Should filter by application name")
    void searchConfigs_FilterByApplication() throws Exception {
        PagedConfigResponseDto<ConfigurationResponseDto> pagedResponse = PagedConfigResponseDto.<ConfigurationResponseDto>builder()
                .items(List.of(responseDto))
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .isFirst(true)
                .isLast(true)
                .build();
        when(queryService.searchConfigs(any(SearchConfigsQuery.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/configs")
                        .param("application", "payment-service")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk());

        verify(queryService).searchConfigs(any(SearchConfigsQuery.class));
    }

    @Test
    @DisplayName("POST /api/v1/configs - Should set location header correctly")
    void createConfig_SetsLocationHeader() throws Exception {
        when(commandService.createConfig(any(), anyString(), anyString())).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/configs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/configs/1"));
    }
}
