package com.gogidix.digitalmarketing.emailmarketing.infrastructure.governance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataGovernanceService {

    private static final Logger log = LoggerFactory.getLogger(DataGovernanceService.class);

    private final Map<String, Instant> lastAuditTimestamps = new ConcurrentHashMap<>();

    public void recordAccess(String tenantId, String resourceType, String resourceId, String action) {
        log.debug("Data access recorded: tenant={}, resource={}/{}, action={}",
                tenantId, resourceType, resourceId, action);
        lastAuditTimestamps.put(tenantId + ":" + resourceType, Instant.now());
    }

    public boolean isCompliant(String tenantId, String resourceType) {
        Instant lastAudit = lastAuditTimestamps.getOrDefault(tenantId + ":" + resourceType, null);
        return lastAudit != null && lastAudit.isAfter(Instant.now().minusSeconds(86400));
    }
}
