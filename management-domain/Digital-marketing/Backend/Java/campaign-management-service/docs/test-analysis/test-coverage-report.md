# Test Coverage Report - analytics-service

## Overview
- **Service**: analytics-service
- **Analysis Date**: 2026-03-06T06:15:03Z
- **Total Classes**: 25
- **Test Files**: 10
- **Total Test Methods**: 275
- **Test Coverage**: 40%

## Source Classes

- `AnalyticsServiceApplication.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/AnalyticsServiceApplication.java`)
- `AnalyticsReport.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/AnalyticsReport.java`)
- `AttributionData.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/AttributionData.java`)
- `BaseEntity.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/BaseEntity.java`)
- `CampaignAnalytics.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/CampaignAnalytics.java`)
- `CampaignMetric.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/CampaignMetric.java`)
- `ChannelAnalytics.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/ChannelAnalytics.java`)
- `ChannelMetric.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/ChannelMetric.java`)
- `MarketingMetric.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/MarketingMetric.java`)
- `Metric.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/domain/model/Metric.java`)
- `MongoConfig.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/infrastructure/config/MongoConfig.java`)
- `SecurityConfig.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/infrastructure/security/SecurityConfig.java`)
- `RequestContext.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/shared/requestcontext/RequestContext.java`)
- `RequestContextHolder.java` (`src/main/java/com/gogidix/digitalmarketing/analytics/shared/requestcontext/RequestContextHolder.java`)
- `BaseEntity.java` (`src/main/java/com/gogidix/digitalmarketing/shared/domain/BaseEntity.java`)
- `ConflictException.java` (`src/main/java/com/gogidix/digitalmarketing/shared/exception/ConflictException.java`)
- `DomainException.java` (`src/main/java/com/gogidix/digitalmarketing/shared/exception/DomainException.java`)
- `NotFoundException.java` (`src/main/java/com/gogidix/digitalmarketing/shared/exception/NotFoundException.java`)
- `ValidationException.java` (`src/main/java/com/gogidix/digitalmarketing/shared/exception/ValidationException.java`)
- `BaseRepository.java` (`src/main/java/com/gogidix/digitalmarketing/shared/infrastructure/persistence/BaseRepository.java`)
- `JwtTokenUtil.java` (`src/main/java/com/gogidix/digitalmarketing/shared/infrastructure/security/JwtTokenUtil.java`)
- `TenantContextFilter.java` (`src/main/java/com/gogidix/digitalmarketing/shared/infrastructure/security/TenantContextFilter.java`)
- `RequestContext.java` (`src/main/java/com/gogidix/digitalmarketing/shared/requestcontext/RequestContext.java`)
- `RequestContextHolder.java` (`src/main/java/com/gogidix/digitalmarketing/shared/requestcontext/RequestContextHolder.java`)
- `AnalyticsServiceApplication.java` (`src/main/java/com/gogidix/marketing/analytics/AnalyticsServiceApplication.java`)

## Test Files

- `AnalyticsServiceApplicationTest.java` (`src/test/java/com/gogidix/digitalmarketing/analytics/AnalyticsServiceApplicationTest.java`) - **1 test methods**
- `MarketingAnalyticsServiceTest.java` (`src/test/java/com/gogidix/digitalmarketing/analytics/application/service/MarketingAnalyticsServiceTest.java`) - **3 test methods**
- `AnalyticsReportTest.java` (`src/test/java/com/gogidix/digitalmarketing/analytics/domain/model/AnalyticsReportTest.java`) - **61 test methods**
- `CampaignAnalyticsTest.java` (`src/test/java/com/gogidix/digitalmarketing/analytics/domain/model/CampaignAnalyticsTest.java`) - **46 test methods**
- `ChannelAnalyticsTest.java` (`src/test/java/com/gogidix/digitalmarketing/analytics/domain/model/ChannelAnalyticsTest.java`) - **52 test methods**
- `MarketingMetricTest.java` (`src/test/java/com/gogidix/digitalmarketing/analytics/domain/model/MarketingMetricTest.java`) - **31 test methods**
- `BaseEntityTest.java` (`src/test/java/com/gogidix/digitalmarketing/shared/domain/BaseEntityTest.java`) - **25 test methods**
- `DomainExceptionsTest.java` (`src/test/java/com/gogidix/digitalmarketing/shared/exception/DomainExceptionsTest.java`) - **15 test methods**
- `JwtTokenUtilTest.java` (`src/test/java/com/gogidix/digitalmarketing/shared/infrastructure/security/JwtTokenUtilTest.java`) - **24 test methods**
- `TenantContextFilterTest.java` (`src/test/java/com/gogidix/digitalmarketing/shared/infrastructure/security/TenantContextFilterTest.java`) - **17 test methods**

## Classes Without Tests

- AttributionData
- CampaignMetric
- ChannelMetric
- MongoConfig
- SecurityConfig
- RequestContext
- RequestContextHolder
- ConflictException
- DomainException
- NotFoundException
- ValidationException
- BaseRepository
- RequestContext
- RequestContextHolder
All classes have tests!
