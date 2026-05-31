# Test Coverage Report - config-server

## Overview
- **Service**: config-server
- **Analysis Date**: 2026-03-01T17:24:00Z
- **Total Classes**: 28
- **Classes with Tests**: 5
- **Classes without Tests**: 23
- **Test Coverage**: 17.9%

## Test Coverage by Package

### Package: com.gogidix.centralconfiguration.configserver.application.service

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigCommandService | ConfigCommandServiceTest | 14 | Covered |
| ConfigQueryService | ConfigQueryServiceTest | 14 | Covered |

### Package: com.gogidix.centralconfiguration.configserver.application.dto.request

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| CreateConfigRequestDto | - | 0 | Not Covered |
| UpdateConfigRequestDto | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.application.dto.response

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigHistoryResponseDto | - | 0 | Not Covered |
| ConfigurationResponseDto | - | 0 | Not Covered |
| PagedConfigResponseDto | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.application.mapper

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigMapper | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.domain.model

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| Configuration | ConfigurationTest | 13 | Covered |
| ConfigurationHistory | - | 0 | Not Covered |
| ConfigEnvironment | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.domain.port.in

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| CreateConfigCommand | - | 0 | Not Covered |
| DeleteConfigCommand | - | 0 | Not Covered |
| GetConfigQuery | - | 0 | Not Covered |
| SearchConfigsQuery | - | 0 | Not Covered |
| UpdateConfigCommand | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.domain.repository

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigurationHistoryRepository | - | 0 | Not Covered |
| ConfigurationRepository | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.infrastructure.config

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ApplicationConfig | - | 0 | Not Covered |
| PostgreSQLConfig | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.infrastructure.messaging.kafka

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigEventPublisher | ConfigEventPublisherTest | 7 | Covered |
| KafkaProducerConfig | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.infrastructure.persistence.postgres

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| PostgresConfigurationHistoryRepository | - | 0 | Not Covered |
| PostgresConfigurationRepository | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver.interfaces.rest

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigController | ConfigControllerTest | 14 | Covered |
| HealthController | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.configserver

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ConfigServerApplication | - | 0 | Not Covered |

## Classes Without Tests

1. **CreateConfigRequestDto** (application.dto.request)
2. **UpdateConfigRequestDto** (application.dto.request)
3. **ConfigHistoryResponseDto** (application.dto.response)
4. **ConfigurationResponseDto** (application.dto.response)
5. **PagedConfigResponseDto** (application.dto.response)
6. **ConfigMapper** (application.mapper)
7. **ConfigurationHistory** (domain.model)
8. **ConfigEnvironment** (domain.model)
9. **CreateConfigCommand** (domain.port.in)
10. **DeleteConfigCommand** (domain.port.in)
11. **GetConfigQuery** (domain.port.in)
12. **SearchConfigsQuery** (domain.port.in)
13. **UpdateConfigCommand** (domain.port.in)
14. **ConfigurationHistoryRepository** (domain.repository)
15. **ConfigurationRepository** (domain.repository)
16. **ApplicationConfig** (infrastructure.config)
17. **PostgreSQLConfig** (infrastructure.config)
18. **KafkaProducerConfig** (infrastructure.messaging.kafka)
19. **PostgresConfigurationHistoryRepository** (infrastructure.persistence.postgres)
20. **PostgresConfigurationRepository** (infrastructure.persistence.postgres)
21. **HealthController** (interfaces.rest)
22. **ConfigServerApplication** (root package)

## Test Method Details

### ConfigCommandServiceTest (14 tests)

Tests for configuration command operations:
1. Create configuration tests
2. Update configuration tests
3. Delete configuration tests
4. Encryption handling tests
5. Version management tests
6. Error handling tests
7. Tenant isolation tests

### ConfigQueryServiceTest (14 tests)

Tests for configuration query operations:
1. Find by ID tests
2. Find by application/profile tests
3. Search configuration tests
4. Pagination tests
5. Tenant filtering tests
6. Active/inactive configuration tests

### ConfigurationTest (13 tests)

Tests for Configuration domain model:
1. Builder pattern tests
2. Entity lifecycle tests
3. Version increment tests
4. Activation/deactivation tests
5. Timestamp handling tests
6. Validation tests

### ConfigEventPublisherTest (7 tests)

Tests for Kafka event publishing:
1. Config created event tests
2. Config updated event tests
3. Config deleted event tests
4. Error handling tests

### ConfigControllerTest (14 tests)

Tests for REST API endpoints:
1. POST /api/v1/configurations tests
2. GET /api/v1/configurations tests
3. PUT /api/v1/configurations tests
4. DELETE /api/v1/configurations tests
5. GET /api/v1/configurations/search tests
6. Validation tests

## Recommendations

1. **High Priority**: Add integration tests for PostgresConfigurationRepository
2. **High Priority**: Add integration tests for PostgresConfigurationHistoryRepository
3. **Medium Priority**: Add unit tests for DTOs (Request/Response)
4. **Medium Priority**: Add unit tests for domain ports/queries
5. **Medium Priority**: Add unit tests for ConfigMapper
6. **Low Priority**: Add configuration tests for infrastructure classes
