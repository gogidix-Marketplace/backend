package com.gogidix.courier.assignmentservice.domain.port;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;

import java.util.List;

public interface DriverNotificationPort {

    void notifyDriverAssigned(String driverId, String assignmentId, String dispatchId);

    void notifyDriverReassigned(String previousDriverId, String newDriverId, String assignmentId);

    void notifyAssignmentCancelled(String driverId, String assignmentId, String reason);

    void notifyAssignmentCompleted(String driverId, String assignmentId);
}
