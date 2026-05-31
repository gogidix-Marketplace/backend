package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.infrastructure.persistence.adapter.EcommerceAssignmentRepositoryAdapter;
import com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository.MongoEcommerceAssignmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EcommerceAssignmentRepositoryAdapterTest {

    @Mock private MongoEcommerceAssignmentRepository mongoRepository;
    @InjectMocks private EcommerceAssignmentRepositoryAdapter adapter;

    @Test
    void shouldSave() {
        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment("o", "s", "v", DeliveryType.TYPE_A, "z");
        when(mongoRepository.save(assignment)).thenReturn(assignment);

        EcommerceCourierAssignment result = adapter.save(assignment);
        assertEquals(assignment, result);
    }

    @Test
    void shouldFindById() {
        when(mongoRepository.findById("id1")).thenReturn(Optional.empty());
        assertTrue(adapter.findById("id1").isEmpty());
    }

    @Test
    void shouldFindByOrderIdAndSubOrderId() {
        when(mongoRepository.findByOrderIdAndSubOrderId("o", "s")).thenReturn(Optional.empty());
        assertTrue(adapter.findByOrderIdAndSubOrderId("o", "s").isEmpty());
    }

    @Test
    void shouldFindByTrackingId() {
        when(mongoRepository.findByTrackingId("trk")).thenReturn(Optional.empty());
        assertTrue(adapter.findByTrackingId("trk").isEmpty());
    }

    @Test
    void shouldFindByOrderId() {
        when(mongoRepository.findByOrderId("o")).thenReturn(List.of());
        assertTrue(adapter.findByOrderId("o").isEmpty());
    }

    @Test
    void shouldFindByPickupZoneIdAndStatus() {
        when(mongoRepository.findByPickupZoneIdAndStatus("z", "ASSIGNED")).thenReturn(List.of());
        assertTrue(adapter.findByPickupZoneIdAndStatus("z", EcommerceCourierAssignment.AssignmentStatus.ASSIGNED).isEmpty());
    }

    @Test
    void shouldFindByDeliveryZoneIdAndStatus() {
        when(mongoRepository.findByDeliveryZoneIdAndStatus("z", "DELIVERED")).thenReturn(List.of());
        assertTrue(adapter.findByDeliveryZoneIdAndStatus("z", EcommerceCourierAssignment.AssignmentStatus.DELIVERED).isEmpty());
    }
}
