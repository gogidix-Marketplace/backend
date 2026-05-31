package com.gogidix.shared.courier.dispatch.application.service;

import com.gogidix.shared.courier.dispatch.application.command.AssignDriverCommand;
import com.gogidix.shared.courier.dispatch.application.command.CancelDispatchCommand;
import com.gogidix.shared.courier.dispatch.application.command.CompleteDispatchCommand;
import com.gogidix.shared.courier.dispatch.application.command.CreateDispatchOrderCommand;
import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.application.mapper.DispatchOrderDtoMapper;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import com.gogidix.shared.courier.dispatch.infrastructure.messaging.DispatchEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchApplicationServiceTest {

    @Mock
    private DispatchOrderRepository dispatchOrderRepository;

    @Mock
    private DispatchOrderDtoMapper dispatchOrderDtoMapper;

    @Mock
    private DispatchEventPublisher eventPublisher;

    @InjectMocks
    private DispatchApplicationService dispatchApplicationService;

    private DispatchOrder testDispatch;
    private DispatchOrderDTO testDispatchDTO;

    @BeforeEach
    void setUp() {
        testDispatch = DispatchOrder.builder()
                .id("dispatch-doc-001")
                .tenantId("tenant-001")
                .dispatchId("dispatch-001")
                .orderId("order-001")
                .customerId("customer-001")
                .pickupLocation(new GeoJsonPoint(-73.9857, 40.7484))
                .deliveryLocation(new GeoJsonPoint(-74.0060, 40.7128))
                .pickupAddress("123 Pickup St")
                .deliveryAddress("456 Delivery Ave")
                .status(DispatchStatus.PENDING)
                .priority(2)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testDispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .tenantId("tenant-001")
                .dispatchId("dispatch-001")
                .orderId("order-001")
                .customerId("customer-001")
                .pickupLocation(DispatchOrderDTO.LocationDto.builder()
                        .latitude(40.7484)
                        .longitude(-73.9857)
                        .build())
                .deliveryLocation(DispatchOrderDTO.LocationDto.builder()
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .pickupAddress("123 Pickup St")
                .deliveryAddress("456 Delivery Ave")
                .status("PENDING")
                .priority(2)
                .build();
    }

    @Test
    void shouldCreateDispatch() {
        CreateDispatchOrderCommand command = CreateDispatchOrderCommand.builder()
                .dispatchId("dispatch-001")
                .orderId("order-001")
                .customerId("customer-001")
                .pickupLocation(CreateDispatchOrderCommand.LocationCommand.builder()
                        .latitude(40.7484)
                        .longitude(-73.9857)
                        .build())
                .deliveryLocation(CreateDispatchOrderCommand.LocationCommand.builder()
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .pickupAddress("123 Pickup St")
                .deliveryAddress("456 Delivery Ave")
                .priority("MEDIUM")
                .build();

        when(dispatchOrderRepository.existsByTenantIdAndDispatchId(anyString(), anyString())).thenReturn(false);
        when(dispatchOrderDtoMapper.toEntity(any())).thenReturn(testDispatch);
        when(dispatchOrderRepository.save(any())).thenReturn(testDispatch);
        when(dispatchOrderDtoMapper.toDTO(any())).thenReturn(testDispatchDTO);

        DispatchOrderDTO result = dispatchApplicationService.createDispatch("tenant-001", command);

        assertThat(result).isNotNull();
        assertThat(result.getDispatchId()).isEqualTo("dispatch-001");
        verify(dispatchOrderRepository).save(any(DispatchOrder.class));
        verify(eventPublisher).publishDispatchCreated(any());
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateDispatch() {
        CreateDispatchOrderCommand command = CreateDispatchOrderCommand.builder()
                .dispatchId("dispatch-001")
                .build();

        when(dispatchOrderRepository.existsByTenantIdAndDispatchId(anyString(), anyString())).thenReturn(true);

        assertThatThrownBy(() -> dispatchApplicationService.createDispatch("tenant-001", command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void shouldGetDispatch() {
        when(dispatchOrderRepository.findByTenantIdAndDispatchId("tenant-001", "dispatch-001"))
                .thenReturn(Optional.of(testDispatch));
        when(dispatchOrderDtoMapper.toDTO(any())).thenReturn(testDispatchDTO);

        DispatchOrderDTO result = dispatchApplicationService.getDispatch("tenant-001", "dispatch-001");

        assertThat(result).isNotNull();
        assertThat(result.getDispatchId()).isEqualTo("dispatch-001");
    }

    @Test
    void shouldThrowExceptionWhenGettingNonExistentDispatch() {
        when(dispatchOrderRepository.findByTenantIdAndDispatchId("tenant-001", "dispatch-001"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> dispatchApplicationService.getDispatch("tenant-001", "dispatch-001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldAssignDriver() {
        AssignDriverCommand command = AssignDriverCommand.builder()
                .driverId("driver-001")
                .vehicleId("vehicle-001")
                .build();

        when(dispatchOrderRepository.findByTenantIdAndDispatchId("tenant-001", "dispatch-001"))
                .thenReturn(Optional.of(testDispatch));
        when(dispatchOrderRepository.save(any())).thenReturn(testDispatch);
        when(dispatchOrderDtoMapper.toDTO(any())).thenReturn(testDispatchDTO);

        DispatchOrderDTO result = dispatchApplicationService.assignDriver("tenant-001", "dispatch-001", command);

        assertThat(result).isNotNull();
        verify(dispatchOrderRepository).save(any(DispatchOrder.class));
        verify(eventPublisher).publishDispatchAssigned(any());
    }

    @Test
    void shouldCancelDispatch() {
        CancelDispatchCommand command = CancelDispatchCommand.builder()
                .cancelledBy("admin")
                .cancellationReason("Customer request")
                .build();

        when(dispatchOrderRepository.findByTenantIdAndDispatchId("tenant-001", "dispatch-001"))
                .thenReturn(Optional.of(testDispatch));
        when(dispatchOrderRepository.save(any())).thenReturn(testDispatch);
        when(dispatchOrderDtoMapper.toDTO(any())).thenReturn(testDispatchDTO);

        DispatchOrderDTO result = dispatchApplicationService.cancelDispatch("tenant-001", "dispatch-001", command);

        assertThat(result).isNotNull();
        verify(dispatchOrderRepository).save(any(DispatchOrder.class));
        verify(eventPublisher).publishDispatchCancelled(any());
    }

    @Test
    void shouldThrowExceptionWhenCancellingDeliveredDispatch() {
        testDispatch.setStatus(DispatchStatus.DELIVERED);

        CancelDispatchCommand command = CancelDispatchCommand.builder()
                .cancelledBy("admin")
                .build();

        when(dispatchOrderRepository.findByTenantIdAndDispatchId("tenant-001", "dispatch-001"))
                .thenReturn(Optional.of(testDispatch));

        assertThatThrownBy(() -> dispatchApplicationService.cancelDispatch("tenant-001", "dispatch-001", command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot cancel a delivered dispatch");
    }

    @Test
    void shouldCompleteDispatch() {
        CompleteDispatchCommand command = CompleteDispatchCommand.builder()
                .actualDistanceMeters(5000.0)
                .actualDurationMinutes(30L)
                .build();

        when(dispatchOrderRepository.findByTenantIdAndDispatchId("tenant-001", "dispatch-001"))
                .thenReturn(Optional.of(testDispatch));
        when(dispatchOrderRepository.save(any())).thenReturn(testDispatch);
        when(dispatchOrderDtoMapper.toDTO(any())).thenReturn(testDispatchDTO);

        DispatchOrderDTO result = dispatchApplicationService.completeDispatch("tenant-001", "dispatch-001", command);

        assertThat(result).isNotNull();
        verify(dispatchOrderRepository).save(any(DispatchOrder.class));
        verify(eventPublisher).publishDispatchCompleted(any());
    }

    @Test
    void shouldGetDashboard() {
        when(dispatchOrderRepository.findByTenantId("tenant-001"))
                .thenReturn(List.of(testDispatch));

        var result = dispatchApplicationService.getDashboard("tenant-001");

        assertThat(result).isNotNull();
        assertThat(result).containsKey("totalDispatches");
        assertThat(result.get("totalDispatches")).isEqualTo(1L);
    }

    @Test
    void shouldFindNearbyPickups() {
        when(dispatchOrderRepository.findNearbyPickups(anyString(), any(GeoJsonPoint.class), anyDouble()))
                .thenReturn(List.of(testDispatch));
        when(dispatchOrderDtoMapper.toDTOList(any())).thenReturn(List.of(testDispatchDTO));

        List<DispatchOrderDTO> result = dispatchApplicationService.findNearbyPickups(
                "tenant-001", 40.7484, -73.9857, 5.0);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFindNearbyDeliveries() {
        when(dispatchOrderRepository.findNearbyDeliveries(anyString(), any(GeoJsonPoint.class), anyDouble()))
                .thenReturn(List.of(testDispatch));
        when(dispatchOrderDtoMapper.toDTOList(any())).thenReturn(List.of(testDispatchDTO));

        List<DispatchOrderDTO> result = dispatchApplicationService.findNearbyDeliveries(
                "tenant-001", 40.7484, -73.9857, 5.0);

        assertThat(result).hasSize(1);
    }
}
