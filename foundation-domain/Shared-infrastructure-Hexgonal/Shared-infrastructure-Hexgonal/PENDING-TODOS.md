# PENDING-TODOS — platform/Shared-infrastructure-Hexgonal

**Status**: MIGRATED — 26 services + core-tenancy, no CI verification  
**Root Path**: `platform/Shared-infrastructure-Hexgonal/`

---

## GAPS

| # | Gap | Severity | Details |
|---|-----|----------|---------|
| 1 | No root pom.xml | BLOCKER | No aggregator pom.xml. CI cannot build this as a unit. |
| 2 | 26 services with individual poms | HIGH | Each service (api-rate-limit, email-sender, event-bus-bridge, etc.) has its own pom.xml but no parent coordination. |
| 3 | No CI verification | BLOCKER | These services have never been compiled or tested in CI. |
| 4 | No Dockerfiles | HIGH | No container definitions for any service. |
| 5 | No tests | HIGH | No test directories found for any service. |
| 6 | `core-tenancy` has src/ and target/ | LOW | Appears to have been built locally but not CI-verified. |

## SERVICES INVENTORY

| Category | Services |
|----------|----------|
| api-management | api-rate-limit-service |
| communication | email-sender, event-bus-bridge, message-broker, message-queue-mgmt, notification, sms-sender, social-media-integration, status-broadcast, webhook-mgmt |
| config | config-server |
| gateway | api-gateway-service, discovery-service |
| infrastructure | caching-service, config-server, message-broker-service |
| observability | audit-service, rate-limiting-service |
| security | dlp-service, mfa-service, secrets-mgmt, security-analytics, security-mgmt, security-orchestration, threat-intelligence |
| storage | file-storage-service |

## PENDING

- [ ] Create root aggregator pom.xml
- [ ] CI-verify compilation of all 26 services
- [ ] Add unit tests for each service
- [ ] Add Dockerfiles
- [ ] Add JaCoCo configuration
- [ ] Verify Spring Boot 3.1.5 compatibility
