package com.gogidix.shared.warehousing.storage.interfaces.rest;

import com.gogidix.shared.warehousing.storage.application.dto.UtilizationReport;
import com.gogidix.shared.warehousing.storage.application.service.StorageSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Storage Utilization Reports
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
public class StorageUtilizationController {

    private final StorageSpaceService service;

    /**
     * Get utilization report for tenant
     */
    @GetMapping("/utilization")
    public ResponseEntity<UtilizationReport> getUtilizationReport(
            @RequestParam String tenantId) {

        log.info("REST request to get utilization report for tenant: {}", tenantId);
        return ResponseEntity.ok(service.getUtilizationReport(tenantId));
    }
}
