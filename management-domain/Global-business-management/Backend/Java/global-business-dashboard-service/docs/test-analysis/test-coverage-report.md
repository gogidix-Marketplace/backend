# Test Coverage Report - global-business-dashboard-service

## Overview
- **Service**: global-business-dashboard-service
- **Domain**: Global Business Management
- **Analysis Date**: 2026-03-06T06:05:02Z
- **Total Classes**: 27
- **Test Files**: 6
- **Classes with Tests**: 6
- **Test Coverage**: 22%

## Source Files
- `com.gogidix.globalbusiness.globalbusinessdashboard.GlobalBusinessDashboardApplication`
- `com.gogidix.globalbusiness.globalbusinessdashboard.infrastructure.config.MongoConfig`
- `com.gogidix.globalbusiness.globalbusinessdashboard.infrastructure.security.SecurityConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.CountryMetricsDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.RegionalSummaryDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.CountryMetricsMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.GlobalBusinessMetricsMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.KPIBoardMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.RegionalSummaryMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.service.GlobalBusinessMetricsService`
- `com.gogidix.globalbusinessmanagement.dashboard.application.service.RegionalSummaryService`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.GlobalBusinessMetrics`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.KPIBoard`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.CountryMetricsRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.GlobalBusinessMetricsRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.KPIBoardRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.RegionalSummaryRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.GlobalBusinessDashboardApplication`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.CacheConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.MongoConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.OpenApiConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.WebConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.interfaces.rest.GlobalBusinessMetricsController`

## Test Files
- `GlobalBusinessMetricsServiceTest` (28 test methods) - `src/test/java/com/gogidix/globalbusinessmanagement/dashboard/application/service/GlobalBusinessMetricsServiceTest.java`
- `RegionalSummaryServiceTest` (28 test methods) - `src/test/java/com/gogidix/globalbusinessmanagement/dashboard/application/service/RegionalSummaryServiceTest.java`
- `GlobalBusinessMetricsModelTest` (19 test methods) - `src/test/java/com/gogidix/globalbusinessmanagement/dashboard/domain/model/GlobalBusinessMetricsModelTest.java`
- `GlobalBusinessMetricsTest` (7 test methods) - `src/test/java/com/gogidix/globalbusinessmanagement/dashboard/domain/model/GlobalBusinessMetricsTest.java`
- `RegionalSummaryModelTest` (20 test methods) - `src/test/java/com/gogidix/globalbusinessmanagement/dashboard/domain/model/RegionalSummaryModelTest.java`
- `GlobalBusinessMetricsControllerTest` (17 test methods) - `src/test/java/com/gogidix/globalbusinessmanagement/dashboard/interfaces/rest/GlobalBusinessMetricsControllerTest.java`

## Classes Without Tests
- `com.gogidix.globalbusiness.globalbusinessdashboard.GlobalBusinessDashboardApplication`
- `com.gogidix.globalbusiness.globalbusinessdashboard.infrastructure.config.MongoConfig`
- `com.gogidix.globalbusiness.globalbusinessdashboard.infrastructure.security.SecurityConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.CountryMetricsDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.dto.RegionalSummaryDto`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.CountryMetricsMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.GlobalBusinessMetricsMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.KPIBoardMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.application.mapper.RegionalSummaryMapper`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.CountryMetrics`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.KPIBoard`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.model.RegionalSummary`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.CountryMetricsRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.GlobalBusinessMetricsRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.KPIBoardRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.domain.repository.RegionalSummaryRepository`
- `com.gogidix.globalbusinessmanagement.dashboard.GlobalBusinessDashboardApplication`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.CacheConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.MongoConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.OpenApiConfig`
- `com.gogidix.globalbusinessmanagement.dashboard.infrastructure.config.WebConfig`
