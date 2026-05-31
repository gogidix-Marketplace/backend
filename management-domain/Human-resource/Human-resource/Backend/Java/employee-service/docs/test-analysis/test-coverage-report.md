# Test Coverage Report - employee-service

## Overview
- **Service**: employee-service
- **Domain**: Human Resource
- **Analysis Date**: 2026-03-06T06:13:50Z
- **Total Classes**: 36
- **Test Files**: 3
- **Classes with Tests**: 3
- **Test Coverage**: 8%

## Source Files
- `.EmployeeDto`
- `.EmployeeResponseDto`
- `com.gogidix.hr.employee.application.service.EmployeeService`
- `.DepartmentType`
- `.EmployeeStatus`
- `.EmploymentType`
- `com.gogidix.hr.employee.domain.event.EmployeeCreatedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeePromotedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeeStatusChangedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeeTerminatedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeeTransferredEvent`
- `.EmployeeUpdatedEvent`
- `com.gogidix.hr.employee.domain.event.SalaryChangedEvent`
- `com.gogidix.service.domain.model.BaseEntity`
- `com.gogidix.hr.employee.domain.model.Employee`
- `com.gogidix.hr.employee.domain.model.EmployeeAddress`
- `com.gogidix.hr.employee.domain.model.EmployeeContact`
- `com.gogidix.hr.employee.domain.model.EmploymentRecord`
- `.Command`
- `.QueryPort`
- `.EventPublisher`
- `com.gogidix.hr.employee.domain.repository.EmployeeAddressRepository`
- `com.gogidix.hr.employee.domain.repository.EmployeeContactRepository`
- `com.gogidix.hr.employee.domain.repository.EmployeeRepository`
- `com.gogidix.hr.employee.domain.repository.EmploymentRecordRepository`
- `.KafkaConfig`
- `.RedisConfig`
- `.KafkaEventPublisher`
- `com.gogidix.hr.employee.infrastructure.security.SecurityConfig`
- `com.gogidix.hr.employee.shared.base.BaseEntity`
- `com.gogidix.hr.employee.shared.exception.BusinessRuleException`
- `com.gogidix.hr.employee.shared.exception.ConflictException`
- `com.gogidix.hr.employee.shared.exception.NotFoundException`
- `com.gogidix.hr.employee.shared.exception.ValidationException`
- `com.gogidix.hr.employee.shared.requestcontext.RequestContext`
- `com.gogidix.hr.employee.shared.requestcontext.RequestContextHolder`

## Test Files
- `EmployeeQueryServiceTest` (5 test methods) - `src/test/java/com/gogidix/hr/employee/application/service/EmployeeQueryServiceTest.java`
- `EmployeeServiceTest` (39 test methods) - `src/test/java/com/gogidix/hr/employee/application/service/EmployeeServiceTest.java`
- `EmployeeTest` (135 test methods) - `src/test/java/com/gogidix/hr/employee/domain/model/EmployeeTest.java`

## Classes Without Tests
- `.EmployeeDto`
- `.EmployeeResponseDto`
- `.DepartmentType`
- `.EmployeeStatus`
- `.EmploymentType`
- `com.gogidix.hr.employee.domain.event.EmployeeCreatedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeePromotedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeeStatusChangedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeeTerminatedEvent`
- `com.gogidix.hr.employee.domain.event.EmployeeTransferredEvent`
- `.EmployeeUpdatedEvent`
- `com.gogidix.hr.employee.domain.event.SalaryChangedEvent`
- `com.gogidix.service.domain.model.BaseEntity`
- `com.gogidix.hr.employee.domain.model.EmployeeAddress`
- `com.gogidix.hr.employee.domain.model.EmployeeContact`
- `com.gogidix.hr.employee.domain.model.EmploymentRecord`
- `.Command`
- `.QueryPort`
- `.EventPublisher`
- `com.gogidix.hr.employee.domain.repository.EmployeeAddressRepository`
- `com.gogidix.hr.employee.domain.repository.EmployeeContactRepository`
- `com.gogidix.hr.employee.domain.repository.EmployeeRepository`
- `com.gogidix.hr.employee.domain.repository.EmploymentRecordRepository`
- `.KafkaConfig`
- `.RedisConfig`
- `.KafkaEventPublisher`
- `com.gogidix.hr.employee.infrastructure.security.SecurityConfig`
- `com.gogidix.hr.employee.shared.base.BaseEntity`
- `com.gogidix.hr.employee.shared.exception.BusinessRuleException`
- `com.gogidix.hr.employee.shared.exception.ConflictException`
- `com.gogidix.hr.employee.shared.exception.NotFoundException`
- `com.gogidix.hr.employee.shared.exception.ValidationException`
- `com.gogidix.hr.employee.shared.requestcontext.RequestContext`
- `com.gogidix.hr.employee.shared.requestcontext.RequestContextHolder`
