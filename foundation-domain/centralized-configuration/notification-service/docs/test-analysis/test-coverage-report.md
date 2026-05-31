# Test Coverage Report - notification-service

## Overview
- **Service**: notification-service
- **Analysis Date**: 2026-03-01T17:24:00Z
- **Total Classes**: 10
- **Classes with Tests**: 0
- **Classes without Tests**: 10
- **Test Coverage**: 0%

## Test Coverage by Package

### Package: com.gogidix.centralconfiguration.notificationservice

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| NotificationServiceApplication | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.notificationservice.application.service

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| NotificationService | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.notificationservice.domain.model

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| Notification | - | 0 | Not Covered |
| NotificationChannel | - | 0 | Not Covered |
| NotificationStatus | - | 0 | Not Covered |
| NotificationType | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.notificationservice.domain.repository

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| NotificationRepository | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.notificationservice.infrastructure.config

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ApplicationConfig | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.notificationservice.infrastructure.persistence.postgres

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| PostgresNotificationRepository | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.notificationservice.interfaces.rest

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| NotificationController | - | 0 | Not Covered |

## Classes Without Tests

1. **NotificationServiceApplication** (root package)
   - Main Spring Boot application class

2. **NotificationService** (application.service)
   - Service with 5 methods including:
     - sendNotification
     - sendNotificationInternal (private)
     - sendEmail (private)
     - sendWebhook (private)
     - sendSlack (private)
     - getNotifications
     - retryFailedNotifications

3. **Notification** (domain.model)
   - Domain entity with business logic:
     - markAsSent()
     - markAsFailed()
     - incrementRetry()
     - canRetry()

4. **NotificationChannel** (domain.model)
   - Enum with 5 values (EMAIL, WEBHOOK, SLACK, SMS, TEAMS)

5. **NotificationStatus** (domain.model)
   - Enum with 4 values (PENDING, SENT, FAILED, RETRYING)

6. **NotificationType** (domain.model)
   - Enum with 7 values (CONFIG_CREATED, CONFIG_UPDATED, CONFIG_DELETED, FEATURE_FLAG_TOGGLED, ENVIRONMENT_CHANGED, SECURITY_ALERT, SYSTEM_ALERT)

7. **NotificationRepository** (domain.repository)
   - Repository interface with 6 methods

8. **ApplicationConfig** (infrastructure.config)
   - Spring configuration class

9. **PostgresNotificationRepository** (infrastructure.persistence.postgres)
   - PostgreSQL repository implementation with 6 methods

10. **NotificationController** (interfaces.rest)
    - REST controller with 3 endpoints:
      - POST /api/v1/notifications
      - GET /api/v1/notifications
      - POST /api/v1/notifications/retry

## Test Method Details

No test files found.

## Recommendations

1. **High Priority**: Add unit tests for NotificationService
2. **High Priority**: Add unit tests for Notification domain model
3. **High Priority**: Add integration tests for PostgresNotificationRepository
4. **High Priority**: Add REST API tests for NotificationController
5. **Medium Priority**: Add unit tests for enum classes
6. **Low Priority**: Add configuration test for ApplicationConfig
7. **Low Priority**: Add application context test for NotificationServiceApplication
