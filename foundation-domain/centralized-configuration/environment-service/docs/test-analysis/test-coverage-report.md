# Test Coverage Report - environment-service

## Overview
- **Service**: environment-service
- **Analysis Date**: 2026-03-01T17:24:00Z
- **Total Classes**: 9
- **Classes with Tests**: 0
- **Classes without Tests**: 9
- **Test Coverage**: 0%

## Test Coverage by Package

### Package: com.gogidix.centralconfiguration.environmentservice

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| EnvironmentServiceApplication | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.environmentservice.application.service

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| EnvironmentService | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.environmentservice.domain.model

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| Environment | - | 0 | Not Covered |
| EnvironmentType | - | 0 | Not Covered |
| EnvironmentVariable | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.environmentservice.domain.repository

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| EnvironmentRepository | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.environmentservice.infrastructure.config

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| ApplicationConfig | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.environmentservice.infrastructure.persistence.postgres

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| PostgresEnvironmentRepository | - | 0 | Not Covered |

### Package: com.gogidix.centralconfiguration.environmentservice.interfaces.rest

| Source Class | Test Class | Test Methods | Status |
|-------------|------------|--------------|--------|
| EnvironmentController | - | 0 | Not Covered |

## Classes Without Tests

1. **EnvironmentServiceApplication** (root package)
   - Main Spring Boot application class

2. **EnvironmentService** (application.service)
   - Service with 6 methods including:
     - createEnvironment
     - getEnvironments
     - getEnvironment
     - updateEnvironment
     - deleteEnvironment

3. **Environment** (domain.model)
   - Domain entity with JPA annotations
   - OneToMany relationship with EnvironmentVariable
   - Lifecycle methods (activate, deactivate)

4. **EnvironmentType** (domain.model)
   - Enum with 6 values (DEVELOPMENT, STAGING, QA, UAT, PRODUCTION, DR)

5. **EnvironmentVariable** (domain.model)
   - JPA entity with ManyToOne relationship to Environment
   - Supports encryption and sensitive data flags

6. **EnvironmentRepository** (domain.repository)
   - Repository interface with 6 methods

7. **ApplicationConfig** (infrastructure.config)
   - Spring configuration class

8. **PostgresEnvironmentRepository** (infrastructure.persistence.postgres)
   - PostgreSQL repository implementation with 6 methods

9. **EnvironmentController** (interfaces.rest)
   - REST controller with 5 endpoints:
     - POST /api/v1/environments
     - GET /api/v1/environments
     - GET /api/v1/environments/{environmentName}
     - PUT /api/v1/environments/{environmentId}
     - DELETE /api/v1/environments/{environmentId}

## Test Method Details

No test files found.

## Recommendations

1. **High Priority**: Add unit tests for EnvironmentService
2. **High Priority**: Add unit tests for Environment domain model
3. **High Priority**: Add integration tests for PostgresEnvironmentRepository
4. **High Priority**: Add REST API tests for EnvironmentController
5. **Medium Priority**: Add unit tests for EnvironmentVariable
6. **Low Priority**: Add configuration test for ApplicationConfig
7. **Low Priority**: Add application context test for EnvironmentServiceApplication
