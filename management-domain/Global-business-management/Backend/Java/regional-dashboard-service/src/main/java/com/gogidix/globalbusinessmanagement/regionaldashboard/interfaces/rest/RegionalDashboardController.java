package com.gogidix.globalbusinessmanagement.regionaldashboard.interfaces.rest;

import com.gogidix.globalbusinessmanagement.regionaldashboard.application.service.RegionalDashboardService;
import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.RegionalDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regional-dashboard")
@RequiredArgsConstructor
public class RegionalDashboardController {

    private final RegionalDashboardService service;

    @PostMapping
    public ResponseEntity<RegionalDashboard> create(@RequestBody RegionalDashboard dashboard) {
        return ResponseEntity.ok(service.create(dashboard));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionalDashboard> getById(@PathVariable String id) {
        RegionalDashboard result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/dashboard/{dashboardId}")
    public ResponseEntity<RegionalDashboard> getByDashboardId(@PathVariable String dashboardId) {
        RegionalDashboard result = service.getByDashboardId(dashboardId);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/region/{regionCode}")
    public ResponseEntity<List<RegionalDashboard>> getByRegion(@PathVariable String regionCode) {
        return ResponseEntity.ok(service.getByRegion(regionCode));
    }

    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<RegionalDashboard>> getByOwner(@PathVariable String owner) {
        return ResponseEntity.ok(service.getByOwner(owner));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionalDashboard> update(@PathVariable String id, @RequestBody RegionalDashboard dashboard) {
        return ResponseEntity.ok(service.update(id, dashboard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
