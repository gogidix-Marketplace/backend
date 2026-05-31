# Test Coverage Report - fulfillment-core-service

## Overview
- **Service**: fulfillment-core-service
- **Analysis Date**: 2026-03-06T11:04:00.513Z
- **Total Classes**: 15
- **Classes with Tests**: 2
- **Classes without Tests**: 13
- **Test Coverage**: 13.33%

## Classes Without Tests
- `CreateOrderRequest` (com.gogidix.shared.warehousing.fulfillment.application.dto.CreateOrderRequest)
- `FulfillmentOrderResponse` (com.gogidix.shared.warehousing.fulfillment.application.dto.FulfillmentOrderResponse)
- `UpdateOrderStatusRequest` (com.gogidix.shared.warehousing.fulfillment.application.dto.UpdateOrderStatusRequest)
- `SecurityConfig` (com.gogidix.shared.warehousing.fulfillment.application.security.SecurityConfig)
- `FulfillmentOrderService` (com.gogidix.shared.warehousing.fulfillment.application.service.FulfillmentOrderService)
- `MongoDBConfig` (com.gogidix.shared.warehousing.fulfillment.core.infrastructure.config.MongoDBConfig)
- `OpenAPIConfig` (com.gogidix.shared.warehousing.fulfillment.core.infrastructure.config.OpenAPIConfig)
- `WebConfig` (com.gogidix.shared.warehousing.fulfillment.core.infrastructure.config.WebConfig)
- `GlobalExceptionHandler` (com.gogidix.shared.warehousing.fulfillment.core.infrastructure.exception.GlobalExceptionHandler)
- `BaseEntity` (com.gogidix.shared.warehousing.fulfillment.domain.entity.BaseEntity)
- `FulfillmentOrder` (com.gogidix.shared.warehousing.fulfillment.domain.entity.FulfillmentOrder)
- `FulfillmentCoreServiceApplication` (com.gogidix.shared.warehousing.fulfillment.FulfillmentCoreServiceApplication)
- `FulfillmentOrderController` (com.gogidix.shared.warehousing.fulfillment.interfaces.rest.FulfillmentOrderController)
- `HealthController` (com.gogidix.shared.warehousing.fulfillment.interfaces.rest.HealthController)

## Test Method Details

### FulfillmentOrderServiceIntegrationTest
- **Target Class**: FulfillmentOrderServiceIntegration
- **Test Count**: 13
- **File**: `src/test/java/com/gogidix/shared/warehousing/fulfillment/application/service/FulfillmentOrderServiceIntegrationTest.java`

**Test Methods:**
  - `shouldCreateFulfillmentOrder`
  - `shouldNotCreateDuplicateOrderNumber`
  - `shouldGetOrdersByTenant`
  - `shouldGetOrdersByCustomer`
  - `shouldGetOrdersByStatus`
  - `shouldGetOrderByNumber`
  - `shouldUpdateOrderStatus`
  - `shouldUpdateOrderStatusWithTracking`
  - `shouldGetPendingOrders`
  - `shouldGetHighPriorityOrders`
  - `shouldCountOrdersByStatus`
  - `shouldPerformFullWorkflow`

### FulfillmentOrderRepositoryTest
- **Target Class**: FulfillmentOrderRepository
- **Test Count**: 10
- **File**: `src/test/java/com/gogidix/shared/warehousing/fulfillment/domain/repository/FulfillmentOrderRepositoryTest.java`

**Test Methods:**
  - `shouldCreateFulfillmentOrder`
  - `shouldFindFulfillmentOrderById`
  - `shouldFindByTenantId`
  - `shouldFindByTenantIdAndCustomerId`
  - `shouldFindByTenantIdAndStatus`
  - `shouldFindByTenantIdAndOrderNumber`
  - `shouldCheckExistenceByTenantIdAndOrderNumber`
  - `shouldFindPendingOrders`
  - `shouldUpdateFulfillmentOrder`
  - `shouldCountByTenantIdAndStatus`

