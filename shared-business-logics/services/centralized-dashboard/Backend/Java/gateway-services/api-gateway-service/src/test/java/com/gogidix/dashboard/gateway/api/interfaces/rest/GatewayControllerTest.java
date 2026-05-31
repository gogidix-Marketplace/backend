package com.gogidix.dashboard.gateway.api.interfaces.rest;

import com.gogidix.dashboard.gateway.api.application.dto.response.AggregatedDashboardDataDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.AggregateResponseDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.ServiceHealthDto;
import com.gogidix.dashboard.gateway.api.application.service.GatewayService;
import com.gogidix.dashboard.gateway.api.domain.model.ServiceRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayControllerTest {

    @Mock
    private GatewayService gatewayService;

    private GatewayController controller;

    @BeforeEach
    void setUp() {
        controller = new GatewayController(gatewayService);
    }

    @Nested
    @DisplayName("getDashboardData tests")
    class DashboardTests {
        @Test
        void getDashboardData_returnsOk() {
            AggregatedDashboardDataDto data = AggregatedDashboardDataDto.builder().tenantId("t1").build();
            when(gatewayService.getAggregatedDashboardData("t1")).thenReturn(data);

            ResponseEntity<AggregatedDashboardDataDto> response = controller.getDashboardData("t1");
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Nested
    @DisplayName("proxyRequest tests")
    class ProxyTests {
        @Test
        void proxyRequest_returnsResponse() {
            AggregateResponseDto proxyResponse = AggregateResponseDto.builder()
                    .success(true).statusCode(200).build();
            when(gatewayService.proxyRequest(eq("svc"), anyString(), eq("GET"), any()))
                    .thenReturn(proxyResponse);

            ResponseEntity<AggregateResponseDto> response = controller.proxyRequest("svc", null, "GET");
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void proxyRequest_serviceUnavailable_returns503() {
            AggregateResponseDto proxyResponse = AggregateResponseDto.builder()
                    .success(false).statusCode(503).error("Not found").build();
            when(gatewayService.proxyRequest(eq("missing"), anyString(), eq("GET"), any()))
                    .thenReturn(proxyResponse);

            ResponseEntity<AggregateResponseDto> response = controller.proxyRequest("missing", null, "GET");
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("getServicesHealth tests")
    class HealthTests {
        @Test
        void getServicesHealth_returnsOk() {
            when(gatewayService.checkAllServicesHealth()).thenReturn(Map.of());

            ResponseEntity<Map<String, ServiceHealthDto>> response = controller.getServicesHealth();
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("registerService tests")
    class RegisterTests {
        @Test
        void registerService_returns201() {
            ServiceRegistry reg = ServiceRegistry.builder().serviceName("svc").build();
            when(gatewayService.registerService(any())).thenReturn(reg);

            ResponseEntity<ServiceRegistry> response = controller.registerService(reg);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }

    @Nested
    @DisplayName("getAllServices tests")
    class GetAllServicesTests {
        @Test
        void getAllServices_returnsOk() {
            when(gatewayService.getAllServices()).thenReturn(List.of());

            ResponseEntity<List<ServiceRegistry>> response = controller.getAllServices();
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("getSagaStatistics tests")
    class SagaStatisticsTests {
        @Test
        void getSagaStatistics_returnsOk() {
            when(gatewayService.fetchSagaStatistics()).thenReturn(Map.of("total", 50));

            ResponseEntity<Map<String, Object>> response = controller.getSagaStatistics();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(50, response.getBody().get("total"));
        }
    }

    @Nested
    @DisplayName("getChartData tests")
    class ChartDataTests {
        @Test
        void getChartData_returnsOk() {
            when(gatewayService.fetchChartData()).thenReturn(Map.of("charts", 10));

            ResponseEntity<Map<String, Object>> response = controller.getChartData();
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(10, response.getBody().get("charts"));
        }
    }
}
