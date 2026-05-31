package com.gogidix.courier.etaservice.infrastructure.persistence.adapter;

import com.gogidix.courier.etaservice.domain.entity.TrafficFactor;
import com.gogidix.courier.etaservice.domain.repository.TrafficFactorRepository;
import com.gogidix.courier.etaservice.infrastructure.persistence.repository.MongoTrafficFactorRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of TrafficFactorRepository.
 */
@Repository
public class TrafficFactorRepositoryAdapter implements TrafficFactorRepository {

    private final MongoTrafficFactorRepository mongoRepository;

    public TrafficFactorRepositoryAdapter(MongoTrafficFactorRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public TrafficFactor save(TrafficFactor factor) {
        return mongoRepository.save(factor);
    }

    @Override
    public List<TrafficFactor> saveAll(List<TrafficFactor> factors) {
        return mongoRepository.saveAll(factors);
    }

    @Override
    public Optional<TrafficFactor> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public List<TrafficFactor> findByAreaCode(String areaCode) {
        return mongoRepository.findByAreaCodeOrderByDayOfWeekAscHourOfDayAsc(areaCode);
    }

    @Override
    public List<TrafficFactor> findByAreaCodeAndTenantId(String areaCode, String tenantId) {
        return mongoRepository.findByAreaCodeAndTenantIdOrderByDayOfWeekAscHourOfDayAsc(areaCode, tenantId);
    }

    @Override
    public Optional<TrafficFactor> findByAreaCodeAndDayOfWeekAndHourOfDay(
            String areaCode, int dayOfWeek, int hourOfDay) {
        return mongoRepository.findByAreaCodeAndDayOfWeekAndHourOfDay(areaCode, dayOfWeek, hourOfDay);
    }

    @Override
    public Optional<TrafficFactor> findByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
            String areaCode, String tenantId, int dayOfWeek, int hourOfDay) {
        return mongoRepository.findByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
                areaCode, tenantId, dayOfWeek, hourOfDay);
    }

    @Override
    public List<TrafficFactor> findByDayOfWeekAndHourOfDay(int dayOfWeek, int hourOfDay) {
        return mongoRepository.findByDayOfWeekAndHourOfDay(dayOfWeek, hourOfDay);
    }

    @Override
    public List<TrafficFactor> findNearLocation(Double latitude, Double longitude, Double radiusKm) {
        // Simplified implementation - in production, use MongoDB geospatial queries
        return mongoRepository.findAll().stream()
                .filter(f -> {
                    if (f.getLatitude() == null || f.getLongitude() == null) {
                        return false;
                    }
                    double distance = calculateDistance(
                            latitude, longitude,
                            f.getLatitude(), f.getLongitude()
                    );
                    return distance <= radiusKm;
                })
                .toList();
    }

    @Override
    public List<TrafficFactor> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantIdOrderByAreaCodeAscDayOfWeekAscHourOfDayAsc(tenantId);
    }

    @Override
    public List<TrafficFactor> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<TrafficFactor> findPeakHoursByTenantId(String tenantId) {
        return mongoRepository.findByTenantIdAndIsPeakHourTrue(tenantId);
    }

    @Override
    public boolean existsByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
            String areaCode, String tenantId, int dayOfWeek, int hourOfDay) {
        return mongoRepository.existsByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
                areaCode, tenantId, dayOfWeek, hourOfDay);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public long count() {
        return mongoRepository.count();
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId(tenantId);
    }

    @Override
    public long deleteByLastObservedAtBeforeAndTenantId(Instant before, String tenantId) {
        List<TrafficFactor> toDelete = mongoRepository.findByLastObservedAtBeforeAndTenantId(before, tenantId);
        mongoRepository.deleteAll(toDelete);
        return toDelete.size();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth radius in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
