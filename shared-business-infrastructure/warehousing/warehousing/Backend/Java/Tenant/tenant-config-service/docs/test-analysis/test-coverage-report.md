# Test Coverage Report - tenant-config-service

## Overview
- **Service**: tenant-config-service
- **Analysis Date**: 2026-03-06T11:04:00.513Z
- **Total Classes**: 18
- **Classes with Tests**: 2
- **Classes without Tests**: 16
- **Test Coverage**: 11.11%

## Classes Without Tests
- `CreateTenantCommand` (com.gogidix.shared.warehousing.tenant.application.command.CreateTenantCommand)
- `SecurityConfig` (com.gogidix.shared.warehousing.tenant.application.command.security.SecurityConfig)
- `UpdateTenantCommand` (com.gogidix.shared.warehousing.tenant.application.command.UpdateTenantCommand)
- `TenantDtoMapper` (com.gogidix.shared.warehousing.tenant.application.mapper.TenantDtoMapper)
- `TenantQuery` (com.gogidix.shared.warehousing.tenant.application.query.TenantQuery)
- `TenantConfigApplicationService` (com.gogidix.shared.warehousing.tenant.application.service.TenantConfigApplicationService)
- `Tenant` (com.gogidix.shared.warehousing.tenant.domain.entity.Tenant)
- `TenantCreatedEvent` (com.gogidix.shared.warehousing.tenant.domain.events.TenantCreatedEvent)
- `TenantRepository` (com.gogidix.shared.warehousing.tenant.domain.repository.TenantRepository)
- `MongoDBConfig` (com.gogidix.shared.warehousing.tenant.infrastructure.config.MongoDBConfig)
- `OpenAPIConfig` (com.gogidix.shared.warehousing.tenant.infrastructure.config.OpenAPIConfig)
- `KafkaProducerConfig` (com.gogidix.shared.warehousing.tenant.infrastructure.messaging.KafkaProducerConfig)
- `TenantEventPublisher` (com.gogidix.shared.warehousing.tenant.infrastructure.messaging.TenantEventPublisher)
- `BusinessRulesDto` (com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.BusinessRulesDto)
- `TenantRequest` (com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantRequest)
- `TenantResponse` (com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantResponse)
- `TenantController` (com.gogidix.shared.warehousing.tenant.interfaces.rest.TenantController)
- `TenantConfigServiceApplication` (com.gogidix.shared.warehousing.tenant.TenantConfigServiceApplication)

## Test Method Details

### TenantRepositoryIntegrationTest
- **Target Class**: TenantRepositoryIntegration
- **Test Count**: 8
- **File**: `src/test/java/integration/TenantRepositoryIntegrationTest.java`

**Test Methods:**
  - `shouldCreateAndRetrieveTenant`
  - `shouldCheckTenantExists`
  - `shouldFindTenantsByStatus`
  - `shouldFindTenantsByTenantType`
  - `shouldUpdateTenant`
  - `shouldDeleteTenant`
  - `shouldStoreBusinessRulesAsJson`

### TenantConfigServiceTest
- **Target Class**: TenantConfigService
- **Test Count**: 9
- **File**: `src/test/java/unit/TenantConfigServiceTest.java`

**Test Methods:**
  - `shouldCreateTenant`
  - `shouldThrowExceptionWhenTenantAlreadyExists`
  - `shouldGetTenantByTenantId`
  - `shouldThrowExceptionWhenTenantNotFound`
  - `shouldUpdateTenant`
  - `shouldDeleteTenant`
  - `shouldGetBusinessRules`
  - `shouldUpdateBusinessRules`
  - `shouldGetAllTenants`

