package com.gogidix.shared.courier.dispatch.application.service;

import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import com.gogidix.shared.courier.dispatch.domain.repository.DispatchOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispatchOrderServiceTest {

    @Mock
    private DispatchOrderRepository dispatchOrderRepository;

    @InjectMocks
    private DispatchOrderService dispatchOrderService;

    private DispatchOrder testOrder;

    @BeforeEach
    void setUp() {
        testOrder = DispatchOrder.builder()
                .id("12345")
                .tenantId("TENANT001")
                .dispatchId("DISP001")
                .orderId("ORDER001")
                .customerId("CUSTOMER001")
                .status(DispatchStatus.PENDING)
                .priority(5)
                .estimatedPickupTime(LocalDateTime.now().plusHours(1))
                .estimatedDeliveryTime(LocalDateTime.now().plusHours(3))
                .totalAmount(25.50)
                .currency("USD")
                .build();
    }

    @Test
    void shouldCreateDispatchOrder() {
        when(dispatchOrderRepository.existsByTenantIdAndDispatchId("TENANT001", "DISP001")).thenReturn(false);
        when(dispatchOrderRepository.save(any(DispatchOrder.class))).thenReturn(testOrder);

        DispatchOrder result = dispatchOrderService.createDispatchOrder(testOrder);

        assertThat(result).isNotNull();
        assertThat(result.getDispatchId()).isEqualTo("DISP001");
        verify(dispatchOrderRepository).save(testOrder);
    }

    @Test
    void shouldThrowExceptionWhenCreatingDuplicateDispatchOrder() {
        when(dispatchOrderRepository.existsByTenantIdAndDispatchId("TENANT001", "DISP001")).thenReturn(true);

        assertThatThrownBy(() -> dispatchOrderService.createDispatchOrder(testOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(dispatchOrderRepository, never()).save(any());
    }

    @Test
    void shouldGetAllDispatchOrders() {
        when(dispatchOrderRepository.findByTenantId("TENANT001")).thenReturn(List.of(testOrder));

        List<DispatchOrder> result = dispatchOrderService.getAllDispatchOrders("TENANT001");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDispatchId()).isEqualTo("DISP001");
    }

    @Test
    void shouldUpdateDispatchOrder() {
        DispatchOrder update = DispatchOrder.builder()
                .status(DispatchStatus.ASSIGNED)
                .assignedDriverId("DRIVER001")
                .priority(7)
                .build();

        when(dispatchOrderRepository.findByTenantIdAndDispatchId("TENANT001", "DISP001"))
                .thenReturn(Optional.of(testOrder));
        when(dispatchOrderRepository.save(any(DispatchOrder.class))).thenReturn(testOrder);

        DispatchOrder result = dispatchOrderService.updateDispatchOrder("TENANT001", "DISP001", update);

        assertThat(result.getStatus()).isEqualTo(DispatchStatus.ASSIGNED);
        assertThat(result.getAssignedDriverId()).isEqualTo("DRIVER001");
        assertThat(result.getPriority()).isEqualTo(7);
        verify(dispatchOrderRepository).save(testOrder);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentOrder() {
        when(dispatchOrderRepository.findByTenantIdAndDispatchId("TENANT001", "NON_EXISTENT"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> dispatchOrderService.updateDispatchOrder("TENANT001", "NON_EXISTENT", testOrder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldDeleteDispatchOrder() {
        when(dispatchOrderRepository.existsByTenantIdAndDispatchId("TENANT001", "DISP001")).thenReturn(true);
        doNothing().when(dispatchOrderRepository).deleteByTenantIdAndDispatchId("TENANT001", "DISP001");

        dispatchOrderService.deleteDispatchOrder("TENANT001", "DISP001");

        verify(dispatchOrderRepository).deleteByTenantIdAndDispatchId("TENANT001", "DISP001");
    }

    @Test
    void shouldGetDispatchOrdersByStatus() {
        when(dispatchOrderRepository.findByTenantIdAndStatus("TENANT001", DispatchStatus.PENDING))
                .thenReturn(List.of(testOrder));

        List<DispatchOrder> result = dispatchOrderService.getDispatchOrdersByStatus("TENANT001", DispatchStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(DispatchStatus.PENDING);
    }

    @Test
    void shouldGetDriverDispatchOrders() {
        when(dispatchOrderRepository.findByTenantIdAndAssignedDriverId("TENANT001", "DRIVER001"))
                .thenReturn(List.of(testOrder));

        List<DispatchOrder> result = dispatchOrderService.getDriverDispatchOrders("TENANT001", "DRIVER001");

        assertThat(result).hasSize(1);
    }
}
