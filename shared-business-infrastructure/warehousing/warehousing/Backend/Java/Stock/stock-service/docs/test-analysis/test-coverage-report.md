# Test Coverage Report - stock-service

## Overview
- **Service**: stock-service
- **Analysis Date**: 2026-03-06T11:04:00.513Z
- **Total Classes**: 24
- **Classes with Tests**: 3
- **Classes without Tests**: 21
- **Test Coverage**: 12.50%

## Classes Without Tests
- `AdjustStockCommand` (com.gogidix.shared.warehousing.stock.application.command.AdjustStockCommand)
- `AllocateStockCommand` (com.gogidix.shared.warehousing.stock.application.command.AllocateStockCommand)
- `CreateStockCommand` (com.gogidix.shared.warehousing.stock.application.command.CreateStockCommand)
- `ReserveStockCommand` (com.gogidix.shared.warehousing.stock.application.command.ReserveStockCommand)
- `StockLevelDTO` (com.gogidix.shared.warehousing.stock.application.dto.StockLevelDTO)
- `StockMovementDTO` (com.gogidix.shared.warehousing.stock.application.dto.StockMovementDTO)
- `StockMapper` (com.gogidix.shared.warehousing.stock.application.mapper.StockMapper)
- `BaseEntity` (com.gogidix.shared.warehousing.stock.domain.entity.BaseEntity)
- `StockLevel` (com.gogidix.shared.warehousing.stock.domain.entity.StockLevel)
- `StockMovement` (com.gogidix.shared.warehousing.stock.domain.entity.StockMovement)
- `StockLevelRepository` (com.gogidix.shared.warehousing.stock.domain.repository.StockLevelRepository)
- `StockMovementRepository` (com.gogidix.shared.warehousing.stock.domain.repository.StockMovementRepository)
- `KafkaConfig` (com.gogidix.shared.warehousing.stock.infrastructure.config.KafkaConfig)
- `WebConfig` (com.gogidix.shared.warehousing.stock.infrastructure.config.WebConfig)
- `StockEventPublisher` (com.gogidix.shared.warehousing.stock.infrastructure.messaging.StockEventPublisher)
- `TenantContext` (com.gogidix.shared.warehousing.stock.infrastructure.security.TenantContext)
- `TenantInterceptor` (com.gogidix.shared.warehousing.stock.infrastructure.security.TenantInterceptor)
- `ErrorResponse` (com.gogidix.shared.warehousing.stock.interfaces.rest.ErrorResponse)
- `GlobalExceptionHandler` (com.gogidix.shared.warehousing.stock.interfaces.rest.GlobalExceptionHandler)
- `HealthController` (com.gogidix.shared.warehousing.stock.interfaces.rest.HealthController)
- `StockController` (com.gogidix.shared.warehousing.stock.interfaces.rest.StockController)
- `StockServiceApplication` (com.gogidix.shared.warehousing.stock.StockServiceApplication)

## Test Method Details

### StockServiceTest
- **Target Class**: StockService
- **Test Count**: 4
- **File**: `src/test/java/com/gogidix/shared/warehousing/stock/application/service/StockServiceTest.java`

**Test Methods:**
  - `createStock_Success`
  - `createStock_AlreadyExists_ThrowsException`
  - `getStock_Success`
  - `getStock_NotFound`

### StockAllocationServiceTest
- **Target Class**: StockAllocationService
- **Test Count**: 7
- **File**: `src/test/java/com/gogidix/shared/warehousing/stock/application/StockAllocationServiceTest.java`

**Test Methods:**
  - `allocateStock_SufficientAvailable_SuccessfullyAllocates`
  - `allocateStock_InsufficientStock_ThrowsException`
  - `reserveStock_ValidCommand_CreatesReservation`
  - `releaseReservation_ConvertToTrue_ConvertsToAllocation`
  - `releaseReservation_ConvertToFalse_JustReleases`
  - `cleanupExpiredReservations_ExpiredReservations_MarksAsExpired`
  - `allocateStock_FEFOAlgorithm_AllocatesOldestBatchFirst`

### StockControllerIntegrationTest
- **Target Class**: StockControllerIntegration
- **Test Count**: 0
- **File**: `src/test/java/com/gogidix/shared/warehousing/stock/interfaces/rest/StockControllerIntegrationTest.java`


