package com.gogidix.courier.etaservice.infrastructure.persistence.repository;

import com.gogidix.courier.etaservice.domain.entity.TrafficFactor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for TrafficFactor.
 */
@Repository
public interface MongoTrafficFactorRepository extends MongoRepository<TrafficFactor, String> {

    List<TrafficFactor> findByAreaCodeOrderByDayOfWeekAscHourOfDayAsc(String areaCode);

    List<TrafficFactor> findByAreaCodeAndTenantIdOrderByDayOfWeekAscHourOfDayAsc(String areaCode, String tenantId);

    Optional<TrafficFactor> findByAreaCodeAndDayOfWeekAndHourOfDay(String areaCode, int dayOfWeek, int hourOfDay);

    Optional<TrafficFactor> findByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
            String areaCode, String tenantId, int dayOfWeek, int hourOfDay);

    List<TrafficFactor> findByDayOfWeekAndHourOfDay(int dayOfWeek, int hourOfDay);

    List<TrafficFactor> findByTenantIdOrderByAreaCodeAscDayOfWeekAscHourOfDayAsc(String tenantId);

    List<TrafficFactor> findByTenantIdAndIsPeakHourTrue(String tenantId);

    boolean existsByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
            String areaCode, String tenantId, int dayOfWeek, int hourOfDay);

    long countByTenantId(String tenantId);

    List<TrafficFactor> findByLastObservedAtBeforeAndTenantId(Instant before, String tenantId);
}
