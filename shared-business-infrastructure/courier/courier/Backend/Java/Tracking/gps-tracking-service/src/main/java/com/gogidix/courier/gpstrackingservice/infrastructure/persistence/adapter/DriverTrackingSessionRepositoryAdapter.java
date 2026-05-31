package com.gogidix.courier.gpstrackingservice.infrastructure.persistence.adapter;

import com.gogidix.courier.gpstrackingservice.domain.entity.DriverTrackingSession;
import com.gogidix.courier.gpstrackingservice.domain.repository.DriverTrackingSessionRepository;
import com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository.MongoDriverTrackingSessionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Adapter implementation for DriverTrackingSessionRepository using MongoDB.
 */
@Repository
public class DriverTrackingSessionRepositoryAdapter implements DriverTrackingSessionRepository {

    private final MongoDriverTrackingSessionRepository mongoRepository;

    public DriverTrackingSessionRepositoryAdapter(MongoDriverTrackingSessionRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DriverTrackingSession save(DriverTrackingSession session) {
        return mongoRepository.save(session);
    }

    @Override
    public Optional<DriverTrackingSession> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<DriverTrackingSession> findBySessionId(String sessionId) {
        return mongoRepository.findBySessionId(sessionId);
    }

    @Override
    public Optional<DriverTrackingSession> findByTenantIdAndSessionId(String tenantId, String sessionId) {
        return mongoRepository.findByTenantIdAndSessionId(tenantId, sessionId);
    }

    @Override
    public Optional<DriverTrackingSession> findActiveByDriverId(String driverId) {
        return mongoRepository.findByDriverIdAndStatus(driverId, DriverTrackingSession.SessionStatus.ACTIVE);
    }

    @Override
    public Optional<DriverTrackingSession> findActiveByTenantIdAndDriverId(String tenantId, String driverId) {
        return mongoRepository.findByTenantIdAndDriverIdAndStatus(tenantId, driverId, DriverTrackingSession.SessionStatus.ACTIVE);
    }

    @Override
    public List<DriverTrackingSession> findByDriverId(String driverId) {
        return mongoRepository.findByDriverIdOrderByStartTimeDesc(driverId);
    }

    @Override
    public List<DriverTrackingSession> findByTenantIdAndDriverId(String tenantId, String driverId) {
        return mongoRepository.findByTenantIdAndDriverIdOrderByStartTimeDesc(tenantId, driverId);
    }

    @Override
    public List<DriverTrackingSession> findByStatus(DriverTrackingSession.SessionStatus status) {
        return mongoRepository.findByStatus(status);
    }

    @Override
    public List<DriverTrackingSession> findAllActive() {
        return mongoRepository.findByStatusOrderByStartTimeDesc(DriverTrackingSession.SessionStatus.ACTIVE);
    }

    @Override
    public List<DriverTrackingSession> findActiveByTenantId(String tenantId) {
        return mongoRepository.findByTenantIdAndStatusOrderByStartTimeDesc(tenantId, DriverTrackingSession.SessionStatus.ACTIVE);
    }

    @Override
    public List<DriverTrackingSession> findByStartTimeBetween(Instant startTime, Instant endTime) {
        return mongoRepository.findByStartTimeBetweenOrderByStartTimeDesc(startTime, endTime);
    }

    @Override
    public List<DriverTrackingSession> findByOrderId(String orderId) {
        return mongoRepository.findByOrderId(orderId);
    }

    @Override
    public List<DriverTrackingSession> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mongoRepository.findAllByOrderByStartTimeDesc(pageable);
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    @Override
    public long countByStatus(DriverTrackingSession.SessionStatus status) {
        return mongoRepository.countByStatus(status);
    }

    @Override
    public long countByDriverId(String driverId) {
        return mongoRepository.countByDriverId(driverId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public long deleteOlderThan(Instant before) {
        return mongoRepository.deleteByEndTimeBefore(before);
    }

    @Override
    public List<DriverTrackingSession> findWithStaleLocation(int staleMinutes) {
        Instant threshold = Instant.now().minusSeconds(staleMinutes * 60L);
        return mongoRepository.findByStatusAndLastLocationTimeBefore(
                DriverTrackingSession.SessionStatus.ACTIVE, threshold);
    }

    @Override
    public List<DriverTrackingSession> findWithStaleLocationByTenantId(String tenantId, int staleMinutes) {
        Instant threshold = Instant.now().minusSeconds(staleMinutes * 60L);
        return mongoRepository.findByTenantIdAndStatusAndLastLocationTimeBefore(
                tenantId, DriverTrackingSession.SessionStatus.ACTIVE, threshold);
    }
}
