package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.interfaces.rest.controller;

import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.in.IServiceLevelAgreementUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceLevelAgreementController Tests")
class ServiceLevelAgreementControllerTest {

    @Mock
    private IServiceLevelAgreementUseCase slaUseCase;

    private ServiceLevelAgreementController controller;

    private ServiceLevelAgreementResponseDto testResponse;

    @BeforeEach
    void setUp() {
        controller = new ServiceLevelAgreementController(slaUseCase);
        LocalDateTime now = LocalDateTime.now();
        testResponse = new ServiceLevelAgreementResponseDto(
                "sla-1", "tenant-1", "Premium SLA", "Desc", "API",
                500.0, 99.9, 10, "ACTIVE", now, now.plusYears(1), now, now
        );
    }

    @Test
    @DisplayName("Should create SLA and return 201")
    void shouldCreate() {
        CreateServiceLevelAgreementRequestDto dto = new CreateServiceLevelAgreementRequestDto(
                "SLA", "Desc", "API", 500.0, 99.9, 10,
                LocalDateTime.now(), LocalDateTime.now().plusYears(1)
        );
        when(slaUseCase.create(any())).thenReturn(testResponse);

        ResponseEntity<ServiceLevelAgreementResponseDto> result = controller.create(dto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("sla-1", result.getBody().id());
        verify(slaUseCase).create(any());
    }

    @Test
    @DisplayName("Should get by ID and return 200")
    void shouldGetById() {
        when(slaUseCase.findById("sla-1")).thenReturn(testResponse);

        ResponseEntity<ServiceLevelAgreementResponseDto> result = controller.getById("sla-1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("sla-1", result.getBody().id());
    }

    @Test
    @DisplayName("Should get all and return 200")
    void shouldGetAll() {
        when(slaUseCase.findAll()).thenReturn(Collections.singletonList(testResponse));

        ResponseEntity<List<ServiceLevelAgreementResponseDto>> result = controller.getAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    @DisplayName("Should get by status and return 200")
    void shouldGetByStatus() {
        when(slaUseCase.findByStatus("ACTIVE")).thenReturn(Collections.singletonList(testResponse));

        ResponseEntity<List<ServiceLevelAgreementResponseDto>> result = controller.getByStatus("ACTIVE");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    @DisplayName("Should get by service type and return 200")
    void shouldGetByServiceType() {
        when(slaUseCase.findByServiceType("API")).thenReturn(Collections.singletonList(testResponse));

        ResponseEntity<List<ServiceLevelAgreementResponseDto>> result = controller.getByServiceType("API");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    @DisplayName("Should get active and return 200")
    void shouldGetActive() {
        when(slaUseCase.findActive()).thenReturn(Collections.singletonList(testResponse));

        ResponseEntity<List<ServiceLevelAgreementResponseDto>> result = controller.getActive();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
    }

    @Test
    @DisplayName("Should update and return 200")
    void shouldUpdate() {
        UpdateServiceLevelAgreementRequestDto dto = new UpdateServiceLevelAgreementRequestDto(
                "Updated", "Desc", "API", 300.0, 99.8, 8,
                LocalDateTime.now(), LocalDateTime.now().plusMonths(6), "ACTIVE"
        );
        when(slaUseCase.update(anyString(), any())).thenReturn(testResponse);

        ResponseEntity<ServiceLevelAgreementResponseDto> result = controller.update("sla-1", dto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    @DisplayName("Should delete and return 204")
    void shouldDelete() {
        ResponseEntity<Void> result = controller.delete("sla-1");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(slaUseCase).delete("sla-1");
    }

    @Test
    @DisplayName("Should deactivate and return 204")
    void shouldDeactivate() {
        ResponseEntity<Void> result = controller.deactivate("sla-1");

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(slaUseCase).deactivate("sla-1");
    }
}
