# Test Coverage Report - data-validation-service

## Overview
- **Service**: data-validation-service
- **Domain**: Global Business Management
- **Analysis Date**: 2026-03-06T06:02:43Z
- **Total Classes**: 26
- **Test Files**: 3
- **Classes with Tests**: 3
- **Test Coverage**: 11%

## Source Files
- `com.gogidix.globalbusiness.datavalidation.application.service.DataQualityService`
- `com.gogidix.globalbusiness.datavalidation.application.service.ValidationEngineService`
- `com.gogidix.globalbusiness.datavalidation.application.service.ValidationRuleService`
- `com.gogidix.globalbusiness.datavalidation.DataValidationServiceApplication`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.BatchValidationRequestDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.DataQualityReportDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationRequestDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationResponseDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationResultDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationRuleDTO`
- `com.gogidix.service.domain.model.BaseEntity`
- `com.gogidix.globalbusiness.datavalidation.domain.model.DataQualityReport`
- `com.gogidix.globalbusiness.datavalidation.domain.model.ValidationResult`
- `com.gogidix.globalbusiness.datavalidation.domain.model.ValidationRule`
- `com.gogidix.globalbusiness.datavalidation.domain.repository.DataQualityReportRepository`
- `com.gogidix.globalbusiness.datavalidation.domain.repository.ValidationResultRepository`
- `com.gogidix.globalbusiness.datavalidation.domain.repository.ValidationRuleRepository`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.config.MongoConfig`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.config.ValidationConfig`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.mapper.DataQualityReportMapper`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.mapper.ValidationResultMapper`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.mapper.ValidationRuleMapper`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.security.SecurityConfig`
- `com.gogidix.globalbusiness.datavalidation.interfaces.rest.DataQualityController`
- `com.gogidix.globalbusiness.datavalidation.interfaces.rest.ValidationController`
- `com.gogidix.globalbusiness.datavalidation.interfaces.rest.ValidationRuleController`

## Test Files
- `ValidationEngineServiceTest` (15 test methods) - `src/test/java/com/gogidix/globalbusiness/datavalidation/application/service/ValidationEngineServiceTest.java`
- `DataValidationServiceApplicationTest` (3 test methods) - `src/test/java/com/gogidix/globalbusiness/datavalidation/DataValidationServiceApplicationTest.java`
- `ValidationRuleModelTest` (22 test methods) - `src/test/java/com/gogidix/globalbusiness/datavalidation/domain/model/ValidationRuleModelTest.java`

## Classes Without Tests
- `com.gogidix.globalbusiness.datavalidation.application.service.DataQualityService`
- `com.gogidix.globalbusiness.datavalidation.application.service.ValidationRuleService`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.BatchValidationRequestDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.DataQualityReportDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationRequestDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationResponseDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationResultDTO`
- `com.gogidix.globalbusiness.datavalidation.domain.dto.ValidationRuleDTO`
- `com.gogidix.service.domain.model.BaseEntity`
- `com.gogidix.globalbusiness.datavalidation.domain.model.DataQualityReport`
- `com.gogidix.globalbusiness.datavalidation.domain.model.ValidationResult`
- `com.gogidix.globalbusiness.datavalidation.domain.model.ValidationRule`
- `com.gogidix.globalbusiness.datavalidation.domain.repository.DataQualityReportRepository`
- `com.gogidix.globalbusiness.datavalidation.domain.repository.ValidationResultRepository`
- `com.gogidix.globalbusiness.datavalidation.domain.repository.ValidationRuleRepository`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.config.MongoConfig`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.config.ValidationConfig`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.mapper.DataQualityReportMapper`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.mapper.ValidationResultMapper`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.mapper.ValidationRuleMapper`
- `com.gogidix.globalbusiness.datavalidation.infrastructure.security.SecurityConfig`
- `com.gogidix.globalbusiness.datavalidation.interfaces.rest.DataQualityController`
- `com.gogidix.globalbusiness.datavalidation.interfaces.rest.ValidationController`
- `com.gogidix.globalbusiness.datavalidation.interfaces.rest.ValidationRuleController`
