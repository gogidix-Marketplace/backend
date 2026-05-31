# Executive-Dashboard-Service Blueprint Structure

## Service Information
- **Service:** executive-dashboard-service
- **Package:** com.gogidix.management.executive
- **Path:** Executive-domain/Backend/Java/executive-dashboard-service

## Directory Structure (Hexagonal Architecture)

```
src/main/java/com/gogidix/management/executive/
├── application/
│   ├── dto/ [8 files] - Data Transfer Objects
│   ├── mapper/ [2 files] - MapStruct mappers
│   └── service/ [3 files] - Application services
├── domain/
│   ├── model/ [12 files] - Domain entities
│   ├── repository/ [6 files] - Repository interfaces
│   └── service/ [3 files] - Domain services
├── infrastructure/
│   ├── config/ [9 files] - Spring configuration
│   ├── external/ [2 files] - External service clients
│   ├── messaging/ [2 files] - Kafka/Event handlers
│   └── persistence/ [6 files] - MongoDB persistence implementation
└── interfaces/
    └── rest/ [5 files] - REST controllers

src/test/java/ - Test files
src/main/resources/ - Application configuration
```

## Missing Directories (To Create)

1. **application/command/** - Command handlers for CQRS pattern
2. **application/query/** - Query handlers for read operations
3. **shared/** - Shared utilities and common code

## File Inventory

### Domain Models (12 files)
- Dashboard.java
- DashboardWidget.java
- DashboardLayout.java
- KPIWidget.java
- ChartWidget.java
- ReportWidget.java
- AlertWidget.java
- NotificationWidget.java
- UserPreference.java
- WidgetConfiguration.java
- DashboardPermission.java
- BaseEntity.java

### Repositories (6 files)
- DashboardRepository.java
- DashboardWidgetRepository.java
- UserPreferenceRepository.java
- WidgetConfigurationRepository.java
- DashboardPermissionRepository.java
- CustomDashboardRepository.java

### DTOs (8 files)
- DashboardDTO.java
- DashboardWidgetDTO.java
- CreateDashboardRequest.java
- UpdateDashboardRequest.java
- DashboardResponse.java
- WidgetResponse.java
- UserPreferenceDTO.java
- DashboardStatisticsDTO.java

### Services (3 files)
- DashboardService.java
- WidgetService.java
- UserPreferenceService.java

### Controllers (5 files)
- DashboardController.java
- WidgetController.java
- UserPreferenceController.java
- DashboardStatisticsController.java
- DashboardExportController.java

## Next Steps

1. Create application/command/ directory with:
   - CreateDashboardCommand.java
   - UpdateDashboardCommand.java
   - DeleteDashboardCommand.java
   - DashboardCommandService.java

2. Create application/query/ directory with:
   - DashboardQueryService.java
   - GetDashboardQuery.java
   - ListDashboardsQuery.java
   - DashboardStatisticsQuery.java

3. Fix BaseEntity to include:
   - deletedAt field
   - active field
   - Soft delete methods

4. Fix all imports and POM issues
5. Compile and build
