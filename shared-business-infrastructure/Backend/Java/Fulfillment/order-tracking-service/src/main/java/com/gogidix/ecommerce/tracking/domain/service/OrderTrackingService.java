package com.gogidix.ecommerce.tracking.domain.service;
import com.gogidix.ecommerce.tracking.shared.requestcontext.RequestContextHolder;

import com.gogidix.ecommerce.tracking.domain.model.OrderTracking;
import com.gogidix.ecommerce.tracking.domain.model.OrderTracking.TrackingEvent;
import com.gogidix.ecommerce.tracking.domain.repository.OrderTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderTrackingService {

    private final OrderTrackingRepository trackingRepository;

    public OrderTracking getTracking(String tenantId, String trackingId) {
        return trackingRepository.findByTenantIdAndTrackingId(tenantId, trackingId).orElse(null);
    }

    public OrderTracking getTrackingByOrderId(String tenantId, String orderId) {
        return trackingRepository.findByTenantIdAndOrderId(tenantId, orderId).orElse(null);
    }

    public List<OrderTracking> getTrackingByStatus(String tenantId, OrderTracking.TrackingStatus status) {
        return trackingRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Transactional
    public OrderTracking createTracking(OrderTracking tracking) {
        tracking.setTrackingId(UUID.randomUUID().toString());
        tracking.setStatus(OrderTracking.TrackingStatus.PENDING);
        tracking.setCreatedAt(Instant.now());
        tracking.setUpdatedAt(Instant.now());
        return trackingRepository.save(tracking);
    }

    @Transactional
    public OrderTracking addTrackingEvent(String tenantId, String trackingId, TrackingEvent event) {
        OrderTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            event.setEventId(UUID.randomUUID().toString());
            event.setTimestamp(Instant.now());
            tracking.getTrackingEvents().add(event);
            tracking.setStatus(event.getStatus());
            tracking.setUpdatedAt(Instant.now());

            if (event.getStatus() == OrderTracking.TrackingStatus.SHIPPED) {
                tracking.setShippedAt(event.getTimestamp());
            } else if (event.getStatus() == OrderTracking.TrackingStatus.IN_TRANSIT) {
                tracking.setInTransitAt(event.getTimestamp());
            } else if (event.getStatus() == OrderTracking.TrackingStatus.DELIVERED) {
                tracking.setDeliveredAt(event.getTimestamp());
            } else if (event.getStatus() == OrderTracking.TrackingStatus.FAILED) {
                tracking.setFailedAt(event.getTimestamp());
            }

            return trackingRepository.save(tracking);
        }
        return null;
    }

    @Transactional
    public OrderTracking updateStatus(String tenantId, String trackingId, OrderTracking.TrackingStatus status) {
        OrderTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            tracking.setStatus(status);
            tracking.setUpdatedAt(Instant.now());

            TrackingEvent event = new TrackingEvent();
            event.setEventId(UUID.randomUUID().toString());
            event.setStatus(status);
            event.setDescription("Status updated to: " + status);
            event.setTimestamp(Instant.now());
            tracking.getTrackingEvents().add(event);

            return trackingRepository.save(tracking);
        }
        return null;
    }

    @Transactional
    public OrderTracking updateCarrierInfo(String tenantId, String trackingId, String carrier, String carrierTrackingNumber) {
        OrderTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            tracking.setCarrier(carrier);
            tracking.setCarrierTrackingNumber(carrierTrackingNumber);
            tracking.setUpdatedAt(Instant.now());
            return trackingRepository.save(tracking);
        }
        return null;
    }
}
