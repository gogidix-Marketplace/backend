package com.gogidix.courier.assignmentservice.domain.port;

import java.util.List;
import java.util.Map;

public interface DispatchServicePort {

    DispatchInfo getDispatchById(String dispatchId, String tenantId);

    void updateDispatchAssignment(String dispatchId, String assignmentId, String driverId, String tenantId);

    record DispatchInfo(
            String id,
            String tenantId,
            String originAddress,
            String destinationAddress,
            Double originLatitude,
            Double originLongitude,
            Double destinationLatitude,
            Double destinationLongitude,
            String status,
            Map<String, Object> metadata
    ) {}
}
