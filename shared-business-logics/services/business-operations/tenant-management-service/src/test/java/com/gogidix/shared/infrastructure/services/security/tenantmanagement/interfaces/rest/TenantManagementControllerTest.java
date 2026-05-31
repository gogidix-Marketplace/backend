package com.gogidix.shared.infrastructure.services.security.tenantmanagement.interfaces.rest;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.UpdateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.service.TenantManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TenantManagementController Tests")
class TenantManagementControllerTest {

    @Mock private TenantManagementService service;
    @InjectMocks private TenantManagementController controller;

    private final TenantResponseDto dto = TenantResponseDto.builder().id("1").tenantId("t1").build();

    @Test void shouldCreateTenant() {
        when(service.createTenant(any())).thenReturn(dto);
        assertEquals(HttpStatus.CREATED, controller.createTenant(CreateTenantRequestDto.builder().build()).getStatusCode());
    }
    @Test void shouldGetTenant() {
        when(service.getTenant("t1")).thenReturn(Optional.of(dto));
        assertEquals(HttpStatus.OK, controller.getTenant("t1").getStatusCode());
    }
    @Test void shouldReturn404WhenTenantMissing() {
        when(service.getTenant("x")).thenReturn(Optional.empty());
        assertEquals(HttpStatus.NOT_FOUND, controller.getTenant("x").getStatusCode());
    }
    @Test void shouldGetTenantByDomain() {
        when(service.getTenantByDomain("test.com")).thenReturn(Optional.of(dto));
        assertEquals(HttpStatus.OK, controller.getTenantByDomain("test.com").getStatusCode());
    }
    @Test void shouldUpdateTenant() {
        when(service.updateTenant(any(), any())).thenReturn(dto);
        assertEquals(HttpStatus.OK, controller.updateTenant("t1", UpdateTenantRequestDto.builder().build()).getStatusCode());
    }
    @Test void shouldActivateTenant() {
        assertEquals(HttpStatus.NO_CONTENT, controller.activateTenant("t1").getStatusCode());
        verify(service).activateTenant("t1");
    }
    @Test void shouldSuspendTenant() {
        assertEquals(HttpStatus.NO_CONTENT, controller.suspendTenant("t1").getStatusCode());
        verify(service).suspendTenant("t1");
    }
    @Test void shouldDeleteTenant() {
        assertEquals(HttpStatus.NO_CONTENT, controller.deleteTenant("t1").getStatusCode());
        verify(service).deleteTenant("t1");
    }
    @Test void shouldListTenants() {
        when(service.listTenants()).thenReturn(List.of(dto));
        ResponseEntity<List<TenantResponseDto>> result = controller.listTenants();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }
}
