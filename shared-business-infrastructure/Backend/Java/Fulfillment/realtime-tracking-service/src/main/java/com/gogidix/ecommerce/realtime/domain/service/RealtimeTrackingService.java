package com.gogidix.ecommerce.realtime.domain.service;
import com.gogidix.ecommerce.realtime.shared.requestcontext.RequestContextHolder;

import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking;
import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking.Location;
import com.gogidix.ecommerce.realtime.domain.repository.RealtimeTrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RealtimeTrackingService {

    private final RealtimeTrackingRepository trackingRepository;

    public RealtimeTracking getTracking(String tenantId, String trackingId) {
        return trackingRepository.findByTenantIdAndTrackingId(tenantId, trackingId).orElse(null);
    }

    public RealtimeTracking getTrackingByOrderId(String tenantId, String orderId) {
        return trackingRepository.findByTenantIdAndOrderId(tenantId, orderId).orElse(null);
    }

    @Transactional
    public RealtimeTracking createTracking(RealtimeTracking tracking) {
        tracking.setTrackingId(UUID.randomUUID().toString());
        tracking.setIsActive(true);
        tracking.setCreatedAt(Instant.now());
        tracking.setUpdatedAt(Instant.now());
        tracking.setLastUpdateAt(Instant.now());
        return trackingRepository.save(tracking);
    }

    @Transactional
    public RealtimeTracking updateLocation(String tenantId, String trackingId, Location location) {
        RealtimeTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            location.setTimestamp(Instant.now());
            tracking.setCurrentLocation(location);
            tracking.setLastUpdateAt(Instant.now());
            tracking.setUpdatedAt(Instant.now());
            return trackingRepository.save(tracking);
        }
        return null;
    }

    @Transactional
    public RealtimeTracking updateMetrics(String tenantId, String trackingId,
                                         Double speed, Double heading, Double altitude) {
        RealtimeTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            tracking.setSpeed(speed);
            tracking.setHeading(heading);
            tracking.setAltitude(altitude);
            tracking.setLastUpdateAt(Instant.now());
            tracking.setUpdatedAt(Instant.now());
            return trackingRepository.save(tracking);
        }
        return null;
    }

    @Transactional
    public RealtimeTracking updateDeviceStatus(String tenantId, String trackingId,
                                              RealtimeTracking.BatteryStatus batteryStatus,
                                              RealtimeTracking.SignalStrength signalStrength) {
        RealtimeTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            tracking.setBatteryStatus(batteryStatus);
            tracking.setSignalStrength(signalStrength);
            tracking.setLastUpdateAt(Instant.now());
            tracking.setUpdatedAt(Instant.now());
            return trackingRepository.save(tracking);
        }
        return null;
    }

    @Transactional
    public void deactivateTracking(String tenantId, String trackingId) {
        RealtimeTracking tracking = getTracking(tenantId, trackingId);
        if (tracking != null) {
            tracking.setIsActive(false);
            tracking.setUpdatedAt(Instant.now());
            trackingRepository.save(tracking);
        }
    }
}
