package com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.adapter;

import com.gogidix.aiservices.aigatewayservice.domain.model.GatewayRoute;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.document.GatewayRouteDocument;
import com.gogidix.aiservices.aigatewayservice.infrastructure.persistence.repository.SpringDataGatewayRouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GatewayRouteRepositoryAdapter.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GatewayRouteRepositoryAdapter Tests")
class GatewayRouteRepositoryAdapterTest {

    @Mock
    private SpringDataGatewayRouteRepository springRepository;

    private GatewayRouteRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GatewayRouteRepositoryAdapter(springRepository);
    }

    @Test
    @DisplayName("Should save route")
    void shouldSaveRoute() {
        GatewayRoute route = GatewayRoute.builder()
                .routeId("route-123")
                .tenantId("tenant-123")
                .path("/api/v1/test")
                .targetService("test-service")
                .targetUrls(List.of("http://localhost:8080"))
                .build();

        // Return a document with all required fields set
        GatewayRouteDocument savedDocument = new GatewayRouteDocument();
        savedDocument.setId("id-123");
        savedDocument.setRouteId("route-123");
        savedDocument.setTenantId("tenant-123");
        savedDocument.setPath("/api/v1/test");
        savedDocument.setTargetService("test-service");
        savedDocument.setTargetUrls(List.of("http://localhost:8080"));
        savedDocument.setStatus(RouteStatus.ACTIVE);

        when(springRepository.save(any())).thenReturn(savedDocument);

        GatewayRoute saved = adapter.save(route);

        verify(springRepository).save(any(GatewayRouteDocument.class));
        assertNotNull(saved);
        assertEquals("route-123", saved.getRouteId());
    }

    @Test
    @DisplayName("Should find by ID")
    void shouldFindById() {
        GatewayRouteDocument document = new GatewayRouteDocument();
        document.setId("id-123");
        document.setRouteId("route-123");
        document.setTenantId("tenant-123");
        document.setPath("/api/v1/test");
        document.setTargetService("test-service");
        document.setTargetUrls(List.of("http://localhost:8080"));
        document.setStatus(RouteStatus.ACTIVE);

        when(springRepository.findById("id-123")).thenReturn(Optional.of(document));

        Optional<GatewayRoute> result = adapter.findById("id-123");

        assertTrue(result.isPresent());
        assertEquals("route-123", result.get().getRouteId());
    }

    @Test
    @DisplayName("Should find by route ID and tenant ID")
    void shouldFindByRouteIdAndTenantId() {
        GatewayRouteDocument document = new GatewayRouteDocument();
        document.setId("id-123");
        document.setRouteId("route-123");
        document.setTenantId("tenant-123");
        document.setPath("/api/v1/test");
        document.setTargetService("test-service");
        document.setTargetUrls(List.of("http://localhost:8080"));
        document.setStatus(RouteStatus.ACTIVE);

        when(springRepository.findByRouteIdAndTenantId("route-123", "tenant-123"))
                .thenReturn(document);

        Optional<GatewayRoute> result = adapter.findByRouteIdAndTenantId("route-123", "tenant-123");

        assertTrue(result.isPresent());
        assertEquals("route-123", result.get().getRouteId());
    }

    @Test
    @DisplayName("Should find by tenant ID")
    void shouldFindByTenantId() {
        GatewayRouteDocument doc1 = new GatewayRouteDocument();
        doc1.setRouteId("route-1");
        doc1.setTenantId("tenant-123");
        doc1.setPath("/api/v1/test1");
        doc1.setTargetService("test-service-1");
        doc1.setTargetUrls(List.of("http://localhost:8081"));
        doc1.setStatus(RouteStatus.ACTIVE);

        GatewayRouteDocument doc2 = new GatewayRouteDocument();
        doc2.setRouteId("route-2");
        doc2.setTenantId("tenant-123");
        doc2.setPath("/api/v1/test2");
        doc2.setTargetService("test-service-2");
        doc2.setTargetUrls(List.of("http://localhost:8082"));
        doc2.setStatus(RouteStatus.ACTIVE);

        when(springRepository.findByTenantId("tenant-123"))
                .thenReturn(List.of(doc1, doc2));

        List<GatewayRoute> result = adapter.findByTenantId("tenant-123");

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should find by tenant ID and status")
    void shouldFindByTenantIdAndStatus() {
        GatewayRouteDocument doc = new GatewayRouteDocument();
        doc.setRouteId("route-1");
        doc.setTenantId("tenant-123");
        doc.setPath("/api/v1/test");
        doc.setTargetService("test-service");
        doc.setTargetUrls(List.of("http://localhost:8080"));
        doc.setStatus(RouteStatus.ACTIVE);

        when(springRepository.findByTenantIdAndStatus("tenant-123", RouteStatus.ACTIVE))
                .thenReturn(List.of(doc));

        List<GatewayRoute> result = adapter.findByTenantIdAndStatus("tenant-123", RouteStatus.ACTIVE);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should delete by ID")
    void shouldDeleteById() {
        adapter.deleteById("id-123");

        verify(springRepository).deleteById("id-123");
    }

    @Test
    @DisplayName("Should check existence")
    void shouldCheckExistence() {
        when(springRepository.existsByRouteIdAndTenantId("route-123", "tenant-123"))
                .thenReturn(true);

        boolean exists = adapter.existsByRouteIdAndTenantId("route-123", "tenant-123");

        assertTrue(exists);
    }
}
