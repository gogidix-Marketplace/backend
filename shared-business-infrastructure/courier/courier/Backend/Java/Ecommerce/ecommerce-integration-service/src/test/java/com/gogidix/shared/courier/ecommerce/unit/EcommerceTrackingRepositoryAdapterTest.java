package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.infrastructure.persistence.adapter.EcommerceTrackingRepositoryAdapter;
import com.gogidix.shared.courier.ecommerce.infrastructure.persistence.repository.MongoEcommerceTrackingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EcommerceTrackingRepositoryAdapterTest {

    @Mock private MongoEcommerceTrackingRepository mongoRepository;
    @InjectMocks private EcommerceTrackingRepositoryAdapter adapter;

    @Test
    void shouldSave() {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("o", "s", DeliveryType.TYPE_A);
        when(mongoRepository.save(tracking)).thenReturn(tracking);
        assertEquals(tracking, adapter.save(tracking));
    }

    @Test
    void shouldFindById() {
        when(mongoRepository.findById("id")).thenReturn(Optional.empty());
        assertTrue(adapter.findById("id").isEmpty());
    }

    @Test
    void shouldFindByOrderId() {
        when(mongoRepository.findByOrderId("o")).thenReturn(Optional.empty());
        assertTrue(adapter.findByOrderId("o").isEmpty());
    }

    @Test
    void shouldFindBySubOrderId() {
        when(mongoRepository.findBySubOrderId("s")).thenReturn(Optional.empty());
        assertTrue(adapter.findBySubOrderId("s").isEmpty());
    }

    @Test
    void shouldFindByTrackingId() {
        when(mongoRepository.findByTrackingId("trk")).thenReturn(Optional.empty());
        assertTrue(adapter.findByTrackingId("trk").isEmpty());
    }
}
