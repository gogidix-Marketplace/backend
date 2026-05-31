package com.gogidix.courier.assignmentservice.shared.constants;

public final class AssignmentConstants {

    private AssignmentConstants() {
    }

    public static final String SERVICE_NAME = "assignment-service";
    public static final String CACHE_NAME = "assignments";
    public static final String KAFKA_TOPIC_ASSIGNMENT_EVENTS = "assignment-events";
    public static final String KAFKA_TOPIC_ASSIGNMENT_COMMANDS = "assignment-commands";
    public static final String KAFKA_GROUP_ID = "assignment-service-group";

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_TIMEOUT_MINUTES = 15;

    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_USER_ID = "X-User-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";

    public static final String MONGO_COLLECTION_ASSIGNMENTS = "driver_assignments";
    public static final String MONGO_COLLECTION_HISTORY = "assignment_history";
}
