# Test Coverage Report - accounts-receivable-service

## Overview
- **Service**: accounts-receivable-service
- **Analysis Date**: 2026-03-06T05:59:35Z
- **Total Source Files**: 57
- **Test Files**: 2
- **Total Classes Analyzed**: 57
- **Classes with Tests**: 2
- **Classes without Tests**: 55
- **Test Coverage**: 3%

## Test Coverage by Package

### Classes With Tests (2)

| Class Name | Status |
|------------|--------|
| InvoiceCommandService | Tested |
| Customer | Tested |

### Classes Without Tests (55)

| Class Name | File Path |
|------------|-----------|
| AccountsReceivableApplication | src/main/java/com/gogidix/finance/accountsreceivable/AccountsReceivableApplication.java |
| CreditMemoResponseDto | src/main/java/com/gogidix/finance/accountsreceivable/application/dto/response/CreditMemoResponseDto.java |
| CustomerResponseDto | src/main/java/com/gogidix/finance/accountsreceivable/application/dto/response/CustomerResponseDto.java |
| ErrorResponseDto | src/main/java/com/gogidix/finance/accountsreceivable/application/dto/response/ErrorResponseDto.java |
| InvoiceResponseDto | src/main/java/com/gogidix/finance/accountsreceivable/application/dto/response/InvoiceResponseDto.java |
| PaymentResponseDto | src/main/java/com/gogidix/finance/accountsreceivable/application/dto/response/PaymentResponseDto.java |
| PaymentScheduleResponseDto | src/main/java/com/gogidix/finance/accountsreceivable/application/dto/response/PaymentScheduleResponseDto.java |
| AccountsReceivableService | src/main/java/com/gogidix/finance/accountsreceivable/application/service/AccountsReceivableService.java |
| CustomerCommandService | src/main/java/com/gogidix/finance/accountsreceivable/application/service/CustomerCommandService.java |
| CustomerQueryService | src/main/java/com/gogidix/finance/accountsreceivable/application/service/CustomerQueryService.java |
| InvoiceQueryService | src/main/java/com/gogidix/finance/accountsreceivable/application/service/InvoiceQueryService.java |
| PaymentCommandService | src/main/java/com/gogidix/finance/accountsreceivable/application/service/PaymentCommandService.java |
| PaymentQueryService | src/main/java/com/gogidix/finance/accountsreceivable/application/service/PaymentQueryService.java |
| CustomerRegisteredEvent | src/main/java/com/gogidix/finance/accountsreceivable/domain/event/CustomerRegisteredEvent.java |
| InvoiceGeneratedEvent | src/main/java/com/gogidix/finance/accountsreceivable/domain/event/InvoiceGeneratedEvent.java |
| PaymentReceivedEvent | src/main/java/com/gogidix/finance/accountsreceivable/domain/event/PaymentReceivedEvent.java |
| CreditMemo | src/main/java/com/gogidix/finance/accountsreceivable/domain/model/CreditMemo.java |
| Invoice | src/main/java/com/gogidix/finance/accountsreceivable/domain/model/Invoice.java |
| Payment | src/main/java/com/gogidix/finance/accountsreceivable/domain/model/Payment.java |
| PaymentSchedule | src/main/java/com/gogidix/finance/accountsreceivable/domain/model/PaymentSchedule.java |
| CustomerCommand | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/in/CustomerCommand.java |
| CustomerQuery | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/in/CustomerQuery.java |
| InvoiceCommand | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/in/InvoiceCommand.java |
| InvoiceQuery | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/in/InvoiceQuery.java |
| PaymentCommand | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/in/PaymentCommand.java |
| PaymentQuery | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/in/PaymentQuery.java |
| EventPublisher | src/main/java/com/gogidix/finance/accountsreceivable/domain/port/out/EventPublisher.java |
| CreditMemoRepository | src/main/java/com/gogidix/finance/accountsreceivable/domain/repository/CreditMemoRepository.java |
| CustomerRepository | src/main/java/com/gogidix/finance/accountsreceivable/domain/repository/CustomerRepository.java |
| InvoiceRepository | src/main/java/com/gogidix/finance/accountsreceivable/domain/repository/InvoiceRepository.java |
| PaymentRepository | src/main/java/com/gogidix/finance/accountsreceivable/domain/repository/PaymentRepository.java |
| PaymentScheduleRepository | src/main/java/com/gogidix/finance/accountsreceivable/domain/repository/PaymentScheduleRepository.java |
| KafkaConfig | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/config/KafkaConfig.java |
| MongoConfig | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/config/MongoConfig.java |
| RedisConfig | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/config/RedisConfig.java |
| WebConfig | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/config/WebConfig.java |
| KafkaEventPublisher | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/messaging/kafka/KafkaEventPublisher.java |
| MongoCreditMemoRepository | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/persistence/mongo/MongoCreditMemoRepository.java |
| MongoCustomerRepository | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/persistence/mongo/MongoCustomerRepository.java |
| MongoInvoiceRepository | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/persistence/mongo/MongoInvoiceRepository.java |
| MongoPaymentRepository | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/persistence/mongo/MongoPaymentRepository.java |
| MongoPaymentScheduleRepository | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/persistence/mongo/MongoPaymentScheduleRepository.java |
| SecurityConfig | src/main/java/com/gogidix/finance/accountsreceivable/infrastructure/security/SecurityConfig.java |
| AccountsReceivableController | src/main/java/com/gogidix/finance/accountsreceivable/interfaces/rest/AccountsReceivableController.java |
| CustomerController | src/main/java/com/gogidix/finance/accountsreceivable/interfaces/rest/CustomerController.java |
| GlobalExceptionHandler | src/main/java/com/gogidix/finance/accountsreceivable/interfaces/rest/GlobalExceptionHandler.java |
| InvoiceController | src/main/java/com/gogidix/finance/accountsreceivable/interfaces/rest/InvoiceController.java |
| PaymentController | src/main/java/com/gogidix/finance/accountsreceivable/interfaces/rest/PaymentController.java |
| ConflictException | src/main/java/com/gogidix/finance/accountsreceivable/shared/exception/ConflictException.java |
| NotFoundException | src/main/java/com/gogidix/finance/accountsreceivable/shared/exception/NotFoundException.java |
| ValidationException | src/main/java/com/gogidix/finance/accountsreceivable/shared/exception/ValidationException.java |

## Test Method Details

### InvoiceCommandServiceTest
- **Tests Class**: InvoiceCommandService
- **Estimated Test Methods**: 16

### CustomerTest
- **Tests Class**: Customer
- **Estimated Test Methods**: 32

