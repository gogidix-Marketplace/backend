package com.gogidix.courier.gpstrackingservice.infrastructure.persistence.adapter;

import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import com.gogidix.courier.gpstrackingservice.domain.entity.LocationHistory;
import com.gogidix.courier.gpstrackingservice.domain.repository.GpsLocationRepository;
import com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository.MongoGpsLocationRepository;
import com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository.MongoLocationHistoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Adapter implementation for GpsLocationRepository using MongoDB.
 */
@Repository
public class GpsLocationRepositoryAdapter implements GpsLocationRepository {

    private final MongoGpsLocationRepository mongoRepository;
    private final MongoLocationHistoryRepository historyRepository;

    public GpsLocationRepositoryAdapter(
            MongoGpsLocationRepository mongoRepository,
            MongoLocationHistoryRepository historyRepository) {
        this.mongoRepository = mongoRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public GpsLocation save(GpsLocation location) {
        return mongoRepository.save(location);
    }

    @Override
    public Optional<GpsLocation> findMostRecentByDriverId(String driverId) {
        return Optional.ofNullable(mongoRepository.findFirstByDriverIdOrderByTimestampDesc(driverId));
    }

    @Override
    public Optional<GpsLocation> findMostRecentByDriverIdAndTenantId(String tenantId, String driverId) {
        return Optional.ofNullable(mongoRepository.findFirstByTenantIdAndDriverIdOrderByTimestampDesc(tenantId, driverId));
    }

    @Override
    public List<GpsLocation> findByDriverIdAndTimestampBetween(String driverId, Instant startTime, Instant endTime) {
        return mongoRepository.findByDriverIdAndTimestampBetweenOrderByTimestampDesc(driverId, startTime, endTime);
    }

    @Override
    public List<GpsLocation> findByTenantIdAndDriverIdAndTimestampBetween(String tenantId, String driverId, Instant startTime, Instant endTime) {
        return mongoRepository.findByTenantIdAndDriverIdAndTimestampBetweenOrderByTimestampDesc(tenantId, driverId, startTime, endTime);
    }

    @Override
    public List<GpsLocation> findByOrderId(String orderId) {
        return mongoRepository.findByOrderIdOrderByTimestampDesc(orderId);
    }

    @Override
    public List<GpsLocation> findByTenantIdAndOrderId(String tenantId, String orderId) {
        return mongoRepository.findByTenantIdAndOrderIdOrderByTimestampDesc(tenantId, orderId);
    }

    @Override
    public List<GpsLocation> findByDriverIdPaginated(String driverId, int page, int size) {
        return mongoRepository.findByDriverIdOrderByTimestampDesc(driverId, PageRequest.of(page, size));
    }

    @Override
    public List<GpsLocation> findNearby(double latitude, double longitude, double radiusMeters) {
        return mongoRepository.findNearby(longitude, latitude, radiusMeters);
    }

    @Override
    public List<GpsLocation> findByTenantIdNearby(String tenantId, double latitude, double longitude, double radiusMeters) {
        return mongoRepository.findByTenantIdNearby(longitude, latitude, radiusMeters, tenantId);
    }

    @Override
    public List<GpsLocation> findRecentByDriverId(String driverId, int minutes) {
        Instant since = Instant.now().minusSeconds(minutes * 60L);
        return mongoRepository.findByDriverIdAndTimestampAfterOrderByTimestampDesc(driverId, since);
    }

    @Override
    public List<GpsLocation> findRecentByTenantIdAndDriverId(String tenantId, String driverId, int minutes) {
        Instant since = Instant.now().minusSeconds(minutes * 60L);
        return mongoRepository.findByTenantIdAndDriverIdAndTimestampAfterOrderByTimestampDesc(tenantId, driverId, since);
    }

    @Override
    public long deleteOlderThan(Instant before) {
        return mongoRepository.deleteByTimestampBefore(before);
    }

    @Override
    public long deleteByDriverIdOlderThan(String driverId, Instant before) {
        return mongoRepository.deleteByDriverIdAndTimestampBefore(driverId, before);
    }

    @Override
    public long countByDriverId(String driverId) {
        return mongoRepository.countByDriverId(driverId);
    }

    @Override
    public long countByTenantIdAndDriverId(String tenantId, String driverId) {
        return mongoRepository.countByTenantIdAndDriverId(tenantId, driverId);
    }

    @Override
    public LocationHistory saveHistory(LocationHistory history) {
        return historyRepository.save(history);
    }

    @Override
    public Optional<LocationHistory> findHistoryByDriverIdAndDate(String driverId, String date) {
        return historyRepository.findByDriverIdAndDate(driverId, date);
    }

    @Override
    public Optional<LocationHistory> findHistoryByTenantIdAndDriverIdAndDate(String tenantId, String driverId, String date) {
        return historyRepository.findByTenantIdAndDriverIdAndDate(tenantId, driverId, date);
    }

    @Override
    public List<LocationHistory> findHistoryByDriverIdAndDateBetween(String driverId, String startDate, String endDate) {
        return historyRepository.findByDriverIdAndDateBetweenOrderByDateDesc(driverId, startDate, endDate);
    }

    @Override
    public List<LocationHistory> findHistoryByOrderId(String orderId) {
        return historyRepository.findByOrderId(orderId);
    }

    @Override
    public long deleteHistoryOlderThan(String date) {
        return historyRepository.deleteByDateBefore(date);
    }
}
