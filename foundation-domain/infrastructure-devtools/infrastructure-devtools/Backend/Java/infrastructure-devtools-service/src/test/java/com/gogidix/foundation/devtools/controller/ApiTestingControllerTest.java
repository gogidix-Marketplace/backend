package com.gogidix.foundation.devtools.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.dto.ApiTestCaseDto;
import com.gogidix.foundation.devtools.dto.ApiTestRequest;
import com.gogidix.foundation.devtools.dto.ApiTestResult;
import com.gogidix.foundation.devtools.service.ApiTestingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ApiTestingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ApiTestingService apiTestingService;

    @InjectMocks
    private ApiTestingController apiTestingController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        System.setProperty("cors.allowed-origins", "*");
        mockMvc = MockMvcBuilders.standaloneSetup(apiTestingController).build();
    }

    @Test
    void getTestCase_WhenExists_ShouldReturnTestCase() throws Exception {
        UUID uuid = UUID.randomUUID();
        ApiTestCaseDto dto = ApiTestCaseDto.builder()
                .uuid(uuid)
                .name("Test API")
                .method("GET")
                .url("https://api.example.com/test")
                .build();

        when(apiTestingService.getTestCase(uuid)).thenReturn(dto);

        mockMvc.perform(get("/api-testing/test-cases/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test API"))
                .andExpect(jsonPath("$.method").value("GET"));
    }

    @Test
    void createTestCase_WithValidData_ShouldReturnCreated() throws Exception {
        ApiTestCaseDto dto = ApiTestCaseDto.builder()
                .name("New Test")
                .method("POST")
                .url("https://api.example.com/test")
                .expectedStatusCode(201)
                .build();

        ApiTestCaseDto created = ApiTestCaseDto.builder()
                .uuid(UUID.randomUUID())
                .name("New Test")
                .method("POST")
                .url("https://api.example.com/test")
                .expectedStatusCode(201)
                .build();

        when(apiTestingService.createTestCase(any(ApiTestCaseDto.class))).thenReturn(created);

        mockMvc.perform(post("/api-testing/test-cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Test"));
    }

    @Test
    void updateTestCase_WithValidData_ShouldReturnUpdated() throws Exception {
        UUID uuid = UUID.randomUUID();
        ApiTestCaseDto dto = ApiTestCaseDto.builder()
                .name("Updated Test")
                .method("PUT")
                .url("https://api.example.com/test")
                .build();

        when(apiTestingService.updateTestCase(eq(uuid), any(ApiTestCaseDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api-testing/test-cases/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test"));
    }

    @Test
    void deleteTestCase_WithValidUuid_ShouldReturnNoContent() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/api-testing/test-cases/{uuid}", uuid))
                .andExpect(status().isNoContent());
    }

    @Test
    void executeTestCase_ShouldReturnAccepted() throws Exception {
        Long testId = 1L;
        ApiTestResult result = ApiTestResult.builder()
                .status("PASSED")
                .statusCode(200)
                .build();

        when(apiTestingService.executeTestCase(eq(testId), any()))
                .thenReturn(CompletableFuture.completedFuture(result));

        mockMvc.perform(post("/api-testing/test-cases/{id}/execute", testId))
                .andExpect(status().isOk());
    }

    @Test
    void executeAdHocTest_ShouldReturnAccepted() throws Exception {
        ApiTestRequest request = ApiTestRequest.builder()
                .name("Ad-hoc Test")
                .method("GET")
                .url("https://api.example.com/test")
                .expectedStatusCode(200)
                .build();

        ApiTestResult result = ApiTestResult.builder()
                .status("PASSED")
                .statusCode(200)
                .build();

        when(apiTestingService.executeAdHocTest(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(result));

        mockMvc.perform(post("/api-testing/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getStatistics_ShouldReturnStatistics() throws Exception {
        when(apiTestingService.getTestStatistics("default"))
                .thenReturn(Map.of("totalTests", 10, "enabledTests", 8));

        mockMvc.perform(get("/api-testing/statistics")
                        .param("projectId", "default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTests").value(10))
                .andExpect(jsonPath("$.enabledTests").value(8));
    }
}
