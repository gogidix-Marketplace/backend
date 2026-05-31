package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.repository;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.RegionalDashboard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalDashboardRepository extends MongoRepository<RegionalDashboard, String> {
    Optional<RegionalDashboard> findByDashboardId(String dashboardId);
    List<RegionalDashboard> findByRegionCode(String regionCode);
    List<RegionalDashboard> findByOwner(String owner);
}
