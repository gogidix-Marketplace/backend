# Test Coverage Report - inventory-core-service

## Overview
- **Service**: inventory-core-service
- **Analysis Date**: 2026-03-06T11:04:00.513Z
- **Total Classes**: 21
- **Classes with Tests**: 5
- **Classes without Tests**: 16
- **Test Coverage**: 23.81%

## Classes Without Tests
- `CreateInventoryCommand` (com.gogidix.shared.warehousing.inventory.application.command.CreateInventoryCommand)
- `SecurityConfig` (com.gogidix.shared.warehousing.inventory.application.command.security.SecurityConfig)
- `UpdateInventoryCommand` (com.gogidix.shared.warehousing.inventory.application.command.UpdateInventoryCommand)
- `InventoryDTO` (com.gogidix.shared.warehousing.inventory.application.dto.InventoryDTO)
- `InventoryMapper` (com.gogidix.shared.warehousing.inventory.application.mapper.InventoryMapper)
- `Inventory` (com.gogidix.shared.warehousing.inventory.domain.entity.Inventory)
- `InventoryCreatedEvent` (com.gogidix.shared.warehousing.inventory.domain.events.InventoryCreatedEvent)
- `InventoryUpdatedEvent` (com.gogidix.shared.warehousing.inventory.domain.events.InventoryUpdatedEvent)
- `EntityNotFoundException` (com.gogidix.shared.warehousing.inventory.domain.exception.EntityNotFoundException)
- `InventoryRepository` (com.gogidix.shared.warehousing.inventory.domain.repository.InventoryRepository)
- `KafkaConfig` (com.gogidix.shared.warehousing.inventory.infrastructure.config.KafkaConfig)
- `MongoDBConfig` (com.gogidix.shared.warehousing.inventory.infrastructure.config.MongoDBConfig)
- `WebConfig` (com.gogidix.shared.warehousing.inventory.infrastructure.config.WebConfig)
- `InventoryEventPublisher` (com.gogidix.shared.warehousing.inventory.infrastructure.messaging.InventoryEventPublisher)
- `TenantContext` (com.gogidix.shared.warehousing.inventory.infrastructure.security.TenantContext)
- `TenantInterceptor` (com.gogidix.shared.warehousing.inventory.infrastructure.security.TenantInterceptor)
- `ErrorResponse` (com.gogidix.shared.warehousing.inventory.interfaces.rest.ErrorResponse)
- `GlobalExceptionHandler` (com.gogidix.shared.warehousing.inventory.interfaces.rest.GlobalExceptionHandler)
- `InventoryCoreApplication` (com.gogidix.shared.warehousing.inventory.InventoryCoreApplication)

## Test Method Details

### InventoryServiceTest
- **Target Class**: InventoryService
- **Test Count**: 9
- **File**: `src/test/java/com/gogidix/shared/warehousing/inventory/application/InventoryServiceTest.java`

**Test Methods:**
  - `createInventory_Success`
  - `getInventory_Success`
  - `getInventory_NotFound`
  - `getAllInventory_Success`
  - `adjustQuantity_Success`
  - `adjustQuantity_NegativeResult_ThrowsException`
  - `deleteInventory_Success`
  - `checkAvailability_SufficientQuantity_ReturnsTrue`
  - `checkAvailability_InsufficientQuantity_ReturnsFalse`

### InventoryEntityTest
- **Target Class**: InventoryEntity
- **Test Count**: 6
- **File**: `src/test/java/com/gogidix/shared/warehousing/inventory/domain/InventoryEntityTest.java`

**Test Methods:**
  - `inventoryBuilder_CreatesValidEntity`
  - `inventoryWithNoArgsConstructor_CreatesEmptyEntity`
  - `inventoryTenantType_EnumValues`
  - `inventoryLocationType_EnumValues`
  - `inventorySetters_UpdateFieldsCorrectly`
  - `inventoryWithAllArgsConstructor_AllFieldsSet`

### InventoryRepositoryIntegrationTest
- **Target Class**: InventoryRepositoryIntegration
- **Test Count**: 11
- **File**: `src/test/java/com/gogidix/shared/warehousing/inventory/integration/InventoryRepositoryIntegrationTest.java`

**Test Methods:**
  - `shouldCreateAndRetrieveInventory`
  - `shouldFindByTenantIdAndSku`
  - `shouldFindByTenantIdAndLocationId`
  - `shouldFindByTenantIdAndSkuAndLocationId`
  - `shouldFindAvailableInventory`
  - `shouldCountByTenantId`
  - `shouldFindLowStockInventory`
  - `shouldUpdateInventory`
  - `shouldDeleteInventory`
  - `shouldIsolateByTenant`

### InventoryControllerIntegrationTest
- **Target Class**: InventoryControllerIntegration
- **Test Count**: 7
- **File**: `src/test/java/com/gogidix/shared/warehousing/inventory/interfaces/InventoryControllerIntegrationTest.java`

**Test Methods:**
  - `createInventory_Success`
  - `createInventory_InvalidInput_ReturnsBadRequest`
  - `getInventory_Success`
  - `getInventory_NotFound_Returns404`
  - `getAllInventory_Success`
  - `checkAvailability_SufficientQuantity_ReturnsTrue`
  - `deleteInventory_Success`

### InventoryControllerTest
- **Target Class**: InventoryController
- **Test Count**: 12
- **File**: `src/test/java/com/gogidix/shared/warehousing/inventory/interfaces/rest/InventoryControllerTest.java`

**Test Methods:**
  - `createInventory_ValidRequest_ReturnsCreated`
  - `createInventory_MissingSKU_ReturnsBadRequest`
  - `getInventory_ExistingId_ReturnsInventory`
  - `getInventory_NotFound_ThrowsException`
  - `getAllInventory_ReturnsListOfInventories`
  - `getAllInventory_EmptyList_ReturnsEmptyArray`
  - `getInventoryBySku_ValidSku_ReturnsMatchingInventories`
  - `getAvailableInventory_ReturnsOnlyInStockItems`
  - `adjustQuantity_ValidAdjustment_ReturnsUpdatedInventory`
  - `deleteInventory_ExistingInventory_ReturnsNoContent`
  - `checkAvailability_SufficientStock_ReturnsTrue`
  - `checkAvailability_InsufficientStock_ReturnsFalse`

