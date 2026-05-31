package com.gogidix.globalbusinessmanagement.regionaldashboard.application.service;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.RegionalDashboard;
import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.repository.RegionalDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegionalDashboardService {

    private final RegionalDashboardRepository repository;

    public RegionalDashboard create(RegionalDashboard dashboard) {
        dashboard.setDashboardId(UUID.randomUUID().toString());
        dashboard.setCreatedAt(Instant.now());
        dashboard.setUpdatedAt(Instant.now());
        if (dashboard.getStatus() == null) dashboard.setStatus("ACTIVE");
        return repository.save(dashboard);
    }

    public RegionalDashboard getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public RegionalDashboard getByDashboardId(String dashboardId) {
        return repository.findByDashboardId(dashboardId).orElse(null);
    }

    public List<RegionalDashboard> getByRegion(String regionCode) {
        return repository.findByRegionCode(regionCode);
    }

    public List<RegionalDashboard> getByOwner(String owner) {
        return repository.findByOwner(owner);
    }

    public RegionalDashboard update(String id, RegionalDashboard dashboard) {
        dashboard.setId(id);
        dashboard.setUpdatedAt(Instant.now());
        return repository.save(dashboard);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
