# Test Coverage Report - accounts-payable-service

## Overview
- **Service**: accounts-payable-service
- **Analysis Date**: 2026-03-06T05:59:35Z
- **Total Source Files**: 55
- **Test Files**: 10
- **Total Classes Analyzed**: 55
- **Classes with Tests**: 10
- **Classes without Tests**: 45
- **Test Coverage**: 18%

## Test Coverage by Package

### Classes With Tests (10)

| Class Name | Status |
|------------|--------|
| InvoiceCommandService | Tested |
| InvoiceQueryService | Tested |
| PaymentCommandService | Tested |
| PaymentQueryService | Tested |
| VendorCommandService | Tested |
| VendorQueryService | Tested |
| Invoice | Tested |
| Payment | Tested |
| Vendor | Tested |
| InvoiceController | Tested |

### Classes Without Tests (45)

| Class Name | File Path |
|------------|-----------|
| AccountsPayableApplication | src/main/java/com/gogidix/finance/accountspayable/AccountsPayableApplication.java |
| APSummaryDto | src/main/java/com/gogidix/finance/accountspayable/application/dto/response/APSummaryDto.java |
| ErrorResponseDto | src/main/java/com/gogidix/finance/accountspayable/application/dto/response/ErrorResponseDto.java |
| InvoiceResponseDto | src/main/java/com/gogidix/finance/accountspayable/application/dto/response/InvoiceResponseDto.java |
| PaymentResponseDto | src/main/java/com/gogidix/finance/accountspayable/application/dto/response/PaymentResponseDto.java |
| PaymentScheduleResponseDto | src/main/java/com/gogidix/finance/accountspayable/application/dto/response/PaymentScheduleResponseDto.java |
| VendorResponseDto | src/main/java/com/gogidix/finance/accountspayable/application/dto/response/VendorResponseDto.java |
| AccountsPayableService | src/main/java/com/gogidix/finance/accountspayable/application/service/AccountsPayableService.java |
| InvoiceApprovedEvent | src/main/java/com/gogidix/finance/accountspayable/domain/event/InvoiceApprovedEvent.java |
| InvoiceCreatedEvent | src/main/java/com/gogidix/finance/accountspayable/domain/event/InvoiceCreatedEvent.java |
| PaymentProcessedEvent | src/main/java/com/gogidix/finance/accountspayable/domain/event/PaymentProcessedEvent.java |
| VendorRegisteredEvent | src/main/java/com/gogidix/finance/accountspayable/domain/event/VendorRegisteredEvent.java |
| PaymentSchedule | src/main/java/com/gogidix/finance/accountspayable/domain/model/PaymentSchedule.java |
| VendorTerm | src/main/java/com/gogidix/finance/accountspayable/domain/model/VendorTerm.java |
| InvoiceCommand | src/main/java/com/gogidix/finance/accountspayable/domain/port/in/InvoiceCommand.java |
| InvoiceQuery | src/main/java/com/gogidix/finance/accountspayable/domain/port/in/InvoiceQuery.java |
| PaymentCommand | src/main/java/com/gogidix/finance/accountspayable/domain/port/in/PaymentCommand.java |
| PaymentQuery | src/main/java/com/gogidix/finance/accountspayable/domain/port/in/PaymentQuery.java |
| VendorCommand | src/main/java/com/gogidix/finance/accountspayable/domain/port/in/VendorCommand.java |
| VendorQuery | src/main/java/com/gogidix/finance/accountspayable/domain/port/in/VendorQuery.java |
| EventPublisher | src/main/java/com/gogidix/finance/accountspayable/domain/port/out/EventPublisher.java |
| InvoiceRepository | src/main/java/com/gogidix/finance/accountspayable/domain/repository/InvoiceRepository.java |
| PaymentRepository | src/main/java/com/gogidix/finance/accountspayable/domain/repository/PaymentRepository.java |
| VendorRepository | src/main/java/com/gogidix/finance/accountspayable/domain/repository/VendorRepository.java |
| KafkaConfig | src/main/java/com/gogidix/finance/accountspayable/infrastructure/config/KafkaConfig.java |
| MongoConfig | src/main/java/com/gogidix/finance/accountspayable/infrastructure/config/MongoConfig.java |
| RedisConfig | src/main/java/com/gogidix/finance/accountspayable/infrastructure/config/RedisConfig.java |
| WebConfig | src/main/java/com/gogidix/finance/accountspayable/infrastructure/config/WebConfig.java |
| KafkaEventPublisher | src/main/java/com/gogidix/finance/accountspayable/infrastructure/messaging/kafka/KafkaEventPublisher.java |
| MongoInvoiceRepository | src/main/java/com/gogidix/finance/accountspayable/infrastructure/persistence/mongo/MongoInvoiceRepository.java |
| MongoPaymentRepository | src/main/java/com/gogidix/finance/accountspayable/infrastructure/persistence/mongo/MongoPaymentRepository.java |
| MongoVendorRepository | src/main/java/com/gogidix/finance/accountspayable/infrastructure/persistence/mongo/MongoVendorRepository.java |
| SecurityConfig | src/main/java/com/gogidix/finance/accountspayable/infrastructure/security/SecurityConfig.java |
| TenantInterceptor | src/main/java/com/gogidix/finance/accountspayable/infrastructure/security/TenantInterceptor.java |
| AccountsPayableController | src/main/java/com/gogidix/finance/accountspayable/interfaces/rest/AccountsPayableController.java |
| GlobalExceptionHandler | src/main/java/com/gogidix/finance/accountspayable/interfaces/rest/GlobalExceptionHandler.java |
| PaymentController | src/main/java/com/gogidix/finance/accountspayable/interfaces/rest/PaymentController.java |
| VendorController | src/main/java/com/gogidix/finance/accountspayable/interfaces/rest/VendorController.java |
| ConflictException | src/main/java/com/gogidix/finance/accountspayable/shared/exception/ConflictException.java |
| NotFoundException | src/main/java/com/gogidix/finance/accountspayable/shared/exception/NotFoundException.java |
| ValidationException | src/main/java/com/gogidix/finance/accountspayable/shared/exception/ValidationException.java |
| RequestContext | src/main/java/com/gogidix/finance/accountspayable/shared/requestcontext/RequestContext.java |
| RequestContextHolder | src/main/java/com/gogidix/finance/accountspayable/shared/requestcontext/RequestContextHolder.java |

## Test Method Details

### InvoiceCommandServiceTest
- **Tests Class**: InvoiceCommandService
- **Estimated Test Methods**: 20

### InvoiceQueryServiceTest
- **Tests Class**: InvoiceQueryService
- **Estimated Test Methods**: 18

### PaymentCommandServiceTest
- **Tests Class**: PaymentCommandService
- **Estimated Test Methods**: 15

### PaymentQueryServiceTest
- **Tests Class**: PaymentQueryService
- **Estimated Test Methods**: 20

### VendorCommandServiceTest
- **Tests Class**: VendorCommandService
- **Estimated Test Methods**: 17

### VendorQueryServiceTest
- **Tests Class**: VendorQueryService
- **Estimated Test Methods**: 18

### InvoiceTest
- **Tests Class**: Invoice
- **Estimated Test Methods**: 49

### PaymentTest
- **Tests Class**: Payment
- **Estimated Test Methods**: 49

### VendorTest
- **Tests Class**: Vendor
- **Estimated Test Methods**: 43

### InvoiceControllerTest
- **Tests Class**: InvoiceController
- **Estimated Test Methods**: 19

