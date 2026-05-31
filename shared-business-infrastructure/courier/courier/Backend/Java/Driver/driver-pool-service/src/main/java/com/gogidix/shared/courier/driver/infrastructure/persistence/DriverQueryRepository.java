package com.gogidix.shared.courier.driver.infrastructure.persistence;

import com.gogidix.shared.courier.driver.domain.entity.DriverProfile;
import com.gogidix.shared.courier.driver.domain.entity.DriverStatus;
import com.gogidix.shared.courier.driver.domain.repository.DriverProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Custom query repository implementation for Driver Profile
 * Provides complex queries beyond standard MongoRepository
 */
@Repository
public class DriverQueryRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DriverProfileRepository driverProfileRepository;

    /**
     * Find available drivers within radius matching vehicle type
     */
    public List<DriverProfile> findAvailableDriversByVehicleType(
            String tenantId, String vehicleType, GeoJsonPoint location, double maxDistanceMeters) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("status").is(DriverStatus.ONLINE));
        query.addCriteria(Criteria.where("vehicleType").is(vehicleType));
        query.addCriteria(Criteria.where("currentLocation").near(location).maxDistance(maxDistanceMeters));

        return mongoTemplate.find(query, DriverProfile.class);
    }

    /**
     * Find drivers with rating above minimum
     */
    public List<DriverProfile> findDriversByMinimumRating(String tenantId, Double minRating) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("rating").gte(minRating));

        return mongoTemplate.find(query, DriverProfile.class);
    }

    /**
     * Find top-rated drivers for tenant
     */
    public List<DriverProfile> findTopRatedDrivers(String tenantId, int limit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("rating").gt(0.0));
        query.limit(limit);

        return mongoTemplate.find(query, DriverProfile.class);
    }
}
