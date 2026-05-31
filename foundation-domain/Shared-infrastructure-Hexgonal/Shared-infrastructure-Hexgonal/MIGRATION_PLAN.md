# Service Migration Plan - Phase 1

## Overview

Migrating 8 production-ready services from existing shared-infrastructure folders to Shared-infrastructure-Hexgonal with full package refactoring.

## Services to Migrate

### Phase 1 (Current)
1. **social-media-integration-service** → `services/communication/social-media-integration-service`
2. **sla-management-service** → `services/business/sla-management-service`

### Phase 2 (Next)
3. **dlp-service** → `services/security/dlp-service`
4. **secrets-management-service** → `services/security/secrets-management-service`
5. **security-analytics-service** → `services/security/analytics-service`

### Phase 3 (Final)
6. **security-management-service** → `services/security/management-service`
7. **security-orchestration-service** → `services/security/orchestration-service`
8. **threat-intelligence-service** → `services/security/threat-intelligence-service`

## Package Mappings

| Old Package | New Package |
|-------------|-------------|
| `com.gogidix.sharedinfrastructure.socialmedia` | `com.gogidix.shared.infrastructure.services.communication.socialmedia` |
| `com.gogidix.shared.business.support.slamanagement` | `com.gogidix.shared.infrastructure.services.business.sla` |
| `com.gogidix.infrastructure.secretsmanagementservice` | `com.gogidix.shared.infrastructure.services.security.secrets` |
| `com.gogidix.infrastructure.dlpservice` | `com.gogidix.shared.infrastructure.services.security.dlp` |
| `com.gogidix.infrastructure.securityanalyticsservice` | `com.gogidix.shared.infrastructure.services.security.analytics` |
| `com.gogidix.infrastructure.securitymanagementservice` | `com.gogidix.shared.infrastructure.services.security.management` |
| `com.gogidix.infrastructure.securityorchestrationservice` | `com.gogidix.shared.infrastructure.services.security.orchestration` |
| `com.gogidix.infrastructure.threatintelligenceservice` | `com.gogidix.shared.infrastructure.services.security.threat` |

## Steps for Each Service

### 1. Create Directory Structure
```bash
services/[category]/[service-name]/
├── src/main/java/com/gogidix/shared/infrastructure/services/[category]/[service]/
│   ├── domain/
│   │   ├── model/
│   │   ├── port/in/
│   │   └── port/out/
│   ├── application/
│   │   ├── dto/request/
│   │   ├── dto/response/
│   │   ├── mapper/
│   │   └── service/
│   ├── infrastructure/
│   │   ├── config/
│   │   ├── persistence/
│   │   └── context/
│   └── interfaces/
│       └── rest/
```

### 2. Copy Source Files
```bash
cp -r [old-service-path]/src/* [new-service-path]/src/
```

### 3. Update Package Declarations
- Update all Java files with new package declarations
- Update import statements

### 4. Update pom.xml
- Change parent to use Shared-infrastructure-Hexgonal
- Update dependencies

### 5. Update Application Class
- Update `@SpringBootApplication` scanBasePackages
- Update component scan paths

## Parent POM Updates

Add to Shared-infrastructure-Hexgonal/pom.xml:

```xml
<modules>
    <!-- Core Modules -->
    <module>core-tenancy</module>

    <!-- Security Services -->
    <module>services/security/auth-service</module>
    <module>services/security/user-management-service</module>
    <module>services/security/tenant-management-service</module>
    <module>services/security/dlp-service</module>
    <module>services/security/secrets-management-service</module>
    <module>services/security/analytics-service</module>
    <module>services/security/management-service</module>
    <module>services/security/orchestration-service</module>
    <module>services/security/threat-intelligence-service</module>

    <!-- Gateway Services -->
    <module>services/gateway/api-gateway-service</module>
    <module>services/gateway/discovery-service</module>

    <!-- Configuration Services -->
    <module>services/config/config-server</module>

    <!-- Communication Services -->
    <module>services/communication/notification-service</module>
    <module>services/communication/social-media-integration-service</module>

    <!-- Storage Services -->
    <module>services/storage/file-storage-service</module>

    <!-- Observability Services -->
    <module>services/observability/audit-service</module>
    <module>services/observability/rate-limiting-service</module>

    <!-- Business Services -->
    <module>services/business/sla-management-service</module>
</modules>
```

## Automated Migration Script

Use IDE refactoring tools or the provided migrate-services.sh script to automate package renaming.

## Status

- [x] Social-media-integration-service files copied (package updates pending)
- [ ] SLA-management-service
- [ ] DLP service
- [ ] Secrets-management-service
- [ ] Security-analytics-service
- [ ] Security-management-service
- [ ] Security-orchestration-service
- [ ] Threat-intelligence-service
