# Test Coverage Report - space-service

## Overview
- **Service**: space-service
- **Analysis Date**: 2026-03-06T11:04:00.513Z
- **Total Classes**: 19
- **Classes with Tests**: 2
- **Classes without Tests**: 17
- **Test Coverage**: 10.53%

## Classes Without Tests
- `AllocateSpaceRequest` (com.gogidix.shared.warehousing.storage.application.dto.AllocateSpaceRequest)
- `CreateSpaceRequest` (com.gogidix.shared.warehousing.storage.application.dto.CreateSpaceRequest)
- `StorageSpaceResponse` (com.gogidix.shared.warehousing.storage.application.dto.StorageSpaceResponse)
- `UtilizationReport` (com.gogidix.shared.warehousing.storage.application.dto.UtilizationReport)
- `SecurityConfig` (com.gogidix.shared.warehousing.storage.application.security.SecurityConfig)
- `StorageSpaceService` (com.gogidix.shared.warehousing.storage.application.service.StorageSpaceService)
- `StorageSpace` (com.gogidix.shared.warehousing.storage.domain.entity.StorageSpace)
- `HealthController` (com.gogidix.shared.warehousing.storage.interfaces.rest.HealthController)
- `StorageSpaceController` (com.gogidix.shared.warehousing.storage.interfaces.rest.StorageSpaceController)
- `StorageUtilizationController` (com.gogidix.shared.warehousing.storage.interfaces.rest.StorageUtilizationController)
- `StorageSpaceService` (com.gogidix.shared.warehousing.storage.space.application.service.StorageSpaceService)
- `StorageSpace` (com.gogidix.shared.warehousing.storage.space.domain.entity.StorageSpace)
- `MongoDBConfig` (com.gogidix.shared.warehousing.storage.space.infrastructure.config.MongoDBConfig)
- `WebConfig` (com.gogidix.shared.warehousing.storage.space.infrastructure.config.WebConfig)
- `GlobalExceptionHandler` (com.gogidix.shared.warehousing.storage.space.infrastructure.exception.GlobalExceptionHandler)
- `StorageSpaceController` (com.gogidix.shared.warehousing.storage.space.interfaces.rest.StorageSpaceController)
- `SpaceServiceApplication` (com.gogidix.shared.warehousing.storage.SpaceServiceApplication)

## Test Method Details

### StorageSpaceServiceIntegrationTest
- **Target Class**: StorageSpaceServiceIntegration
- **Test Count**: 12
- **File**: `src/test/java/com/gogidix/shared/warehousing/storage/application/service/StorageSpaceServiceIntegrationTest.java`

**Test Methods:**
  - `shouldCreateStorageSpace`
  - `shouldNotCreateDuplicateSpaceCode`
  - `shouldGetSpacesByTenant`
  - `shouldGetAvailableSpacesByType`
  - `shouldUpdateSpace`
  - `shouldAllocateSpace`
  - `shouldFullyOccupySpaceWhenCapacityExhausted`
  - `shouldNotAllocateInsufficientCapacity`
  - `shouldOptimizeSpace`
  - `shouldGenerateUtilizationReport`
  - `shouldPerformFullWorkflow`

### StorageSpaceRepositoryTest
- **Target Class**: StorageSpaceRepository
- **Test Count**: 10
- **File**: `src/test/java/com/gogidix/shared/warehousing/storage/domain/repository/StorageSpaceRepositoryTest.java`

**Test Methods:**
  - `shouldCreateStorageSpace`
  - `shouldFindStorageSpaceById`
  - `shouldFindByTenantId`
  - `shouldFindByTenantIdAndStatusAndSpaceType`
  - `shouldFindByTenantIdAndSpaceCode`
  - `shouldCheckExistenceByTenantIdAndSpaceCode`
  - `shouldFindOptimizableSpaces`
  - `shouldUpdateStorageSpace`
  - `shouldDeleteStorageSpace`
  - `shouldCalculateUtilizationPercentage`

