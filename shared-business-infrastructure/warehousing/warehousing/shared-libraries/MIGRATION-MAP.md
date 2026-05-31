# Shared Libraries Domain - Migration Map

**Audit Date:** 2025-01-21
**Source:** Foundation Backup 2025-12-25
**Target Structure:** Hexagonal SaaS Multi-Tenant

---

## Hexagonal SaaS Target Structure Reference

```
domains/
  shared-libraries/
    PRD.md
    TODOS.md
    PROGRESS.md
    domain/                      # Hexagonal Domain Layer
      core/                      # Core business logic
        exceptions/
        model/
        validation/
      security/                  # Security domain
        audit/
        security/
      communication/             # Communication domain
        messaging/
      testing/                   # Testing utilities
      utility/                   # Common utilities
    application/                 # Hexagonal Application Layer
      ports/                     # Use Case interfaces (in/out)
      services/                  # Application services
    adapters/                    # Hexagonal Adapter Layer
      inbound/
        rest/                    # REST controllers
        messaging/               # Message consumers
      outbound/
        persistence/             # JPA/Database adapters
        messaging/               # Kafka/RabbitMQ publishers
        external/                # External API adapters
    config/                      # Configuration
      application.yaml
      application-dev.yaml
      application-prod.yaml
    infrastructure/              # Infrastructure setup
      docker/
      kubernetes/
      observability/
        logging/
        metrics/
        tracing/
    tests/                       # All tests
      unit/
      integration/
      e2e/
```

---

## Migration Mapping Table

### Core Libraries Group

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `backend/java/core-libraries/shared-exceptions/src/main/java/com/gogidix/shared/exceptions/domain/exception/` | shared-libraries | shared-exceptions | domain/core/exceptions | Move 9 exception classes to domain layer. Already follows hexagonal pattern. |
| `backend/java/core-libraries/shared-exceptions/src/main/java/com/gogidix/shared/exceptions/adapter/` | shared-libraries | shared-exceptions | adapters | Already has adapter structure. |
| `backend/java/core-libraries/shared-exceptions/src/main/java/com/gogidix/shared/exceptions/application/` | shared-libraries | shared-exceptions | application | Already has application layer. |
| `backend/java/core-libraries/shared-model/src/main/java/com/gogidix/shared/model/domain/` | shared-libraries | shared-model | domain/core/model | Move domain entities and value objects. |
| `backend/java/core-libraries/shared-model/src/main/java/com/gogidix/shared/model/application/` | shared-libraries | shared-model | application | Model transformation services. |
| `backend/java/core-libraries/shared-validation/src/main/java/com/gogidix/shared/validation/domain/` | shared-libraries | shared-validation | domain/core/validation | Validation patterns and rules. |
| `backend/java/core-libraries/shared-validation/src/main/java/com/gogidix/shared/validation/application/` | shared-libraries | shared-validation | application | Validation services. |

### Security Libraries Group

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `backend/java/security-libraries/shared-security/src/main/java/com/gogidix/shared/security/` | shared-libraries | shared-security | domain/security | JWT/OAuth2 security implementation. |
| `backend/java/security-libraries/shared-audit/src/main/java/com/gogidix/shared/audit/domain/` | shared-libraries | shared-audit | domain/security/audit | Audit domain models. |
| `backend/java/security-libraries/shared-audit/src/main/java/com/gogidix/shared/audit/application/` | shared-libraries | shared-audit | application | Audit event services. |
| `backend/java/security-libraries/shared-audit/src/main/java/com/gogidix/shared/audit/api/` | shared-libraries | shared-audit | adapters/inbound/rest | REST controllers and DTOs. |
| `backend/java/security-libraries/shared-audit/src/main/java/com/gogidix/shared/audit/adapter/out/` | shared-libraries | shared-audit | adapters/outbound | Persistence adapters. |

### Communication Libraries Group

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `backend/java/communication-libraries/shared-messaging/src/main/java/com/gogidix/shared/messaging/domain/` | shared-libraries | shared-messaging | domain/communication | Message domain models and value objects. |
| `backend/java/communication-libraries/shared-messaging/src/main/java/com/gogidix/shared/messaging/application/` | shared-libraries | shared-messaging | application | Message handling use cases. |
| `backend/java/communication-libraries/shared-messaging/src/main/java/com/gogidix/shared/messaging/adapter/in/` | shared-libraries | shared-messaging | adapters/inbound/rest | Message controllers. |
| `backend/java/communication-libraries/shared-messaging/src/main/java/com/gogidix/shared/messaging/adapter/out/messaging/` | shared-libraries | shared-messaging | adapters/outbound/messaging | Kafka publishers. |
| `backend/java/communication-libraries/shared-messaging/src/main/java/com/gogidix/shared/messaging/adapter/out/persistence/` | shared-libraries | shared-messaging | adapters/outbound/persistence | Message repository. |

### Testing Libraries Group

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `backend/java/testing-libraries/shared-testing/src/` | shared-libraries | shared-testing | tests | Move to unified tests directory. |
| `backend/java/testing-libraries/shared-testing/pom.xml` | shared-libraries | shared-testing | config | Build configuration. |

### Utility Libraries Group

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `backend/java/utility-libraries/shared-utilities/src/main/java/com/gogidix/shared/utilities/` | shared-libraries | shared-utilities | domain/utility | Common utility classes. |
| `backend/java/utility-libraries/shared-utilities/owasp-suppressions.xml` | shared-libraries | shared-utilities | infrastructure | Security suppressions. |

### Build Libraries Group

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `backend/java/build-libraries/maven-config-service/settings.xml` | shared-libraries | build-config | config | Maven settings template. |
| `backend/java/build-libraries/maven-config-service/*.sh` | shared-libraries | build-config | infrastructure/scripts | Deployment scripts. |
| `backend/java/build-libraries/maven-config-service/*.bat` | shared-libraries | build-config | infrastructure/scripts | Windows scripts. |

### Frontend Libraries

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `frontend/web/gogidix-ui-library/src/components/` | shared-libraries | ui-library | adapters/ui/frontend | React components. |
| `frontend/web/gogidix-ui-library/src/themes/` | shared-libraries | ui-library | adapters/ui/themes | MUI theme configuration. |
| `frontend/web/gogidix-ui-library/package.json` | shared-libraries | ui-library | config | NPM package configuration. |
| `frontend/web/gogidix-ui-library/rollup.config.js` | shared-libraries | ui-library | infrastructure | Build configuration. |
| `frontend/web/gogidix-ui-library/tsconfig.json` | shared-libraries | ui-library | config | TypeScript configuration. |

### Development Tools

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `dev-tools/.env.shared-libraries` | shared-libraries | - | config | Base environment configuration. |
| `dev-tools/scripts/*.ps1` | shared-libraries | - | infrastructure/scripts | PowerShell migration scripts. |
| `dev-tools/scripts/*.sh` | shared-libraries | - | infrastructure/scripts | Bash utility scripts. |
| `dev-tools/logs/` | shared-libraries | - | infrastructure/observability/logs | Historical logs - retain for audit. |
| `dev-tools/error-logs/` | shared-libraries | - | infrastructure/observability/logs | Historical error logs - retain for audit. |
| `dev-tools/test-results/` | shared-libraries | - | tests/reports | Historical test reports. |

### Documentation

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `docs/api/` | shared-libraries | - | docs/api | API documentation. |
| `docs/architecture/` | shared-libraries | - | docs/architecture | Architecture diagrams. |
| `docs/smoke-tests/` | shared-libraries | - | docs/testing | Smoke test procedures. |
| `docs/testing/` | shared-libraries | - | docs/testing | Test documentation. |
| `SHARED_LIBRARIES_MIGRATION_CERTIFICATION.md` | shared-libraries | - | docs | Historical migration certification - retain. |
| `*/README.md` (various) | shared-libraries | - | docs | Individual service README files. |
| `*/ARCHITECTURE_DIAGRAM.md` (various) | shared-libraries | - | docs/architecture | Service-specific architecture docs. |
| `*/PHASE_*.md` (various) | shared-libraries | - | docs/implementation | Implementation phase documentation. |

### Configuration Files (All Services)

| Backup Path | Target Domain | Target Service/Module | Hex Layer | Notes |
|-------------|---------------|----------------------|-----------|-------|
| `*/pom.xml` | shared-libraries | [service-name] | config | Maven build configuration. |
| `*/.gitlab-ci.yml` | shared-libraries | - | infrastructure/ci-cd | CI/CD pipelines. |
| `*/docker-compose.yml` | shared-libraries | - | infrastructure/docker | Local development. |
| `*/Dockerfile` | shared-libraries | - | infrastructure/docker | Container images. |
| `*/Makefile` | shared-libraries | - | infrastructure/build | Build automation. |
| `*/.mavenrc`, `*/mvnw*` | shared-libraries | - | infrastructure/build | Maven wrapper. |

---

## Migration Actions Required

### High Priority (Structure Alignment)

1. **Flatten business groups into hexagonal layers:** Move from `core-libraries/`, `security-libraries/`, etc. grouping to domain-centric `domain/`, `application/`, `adapters/` structure.

2. **Consolidate configuration:** Move all `pom.xml`, `docker-compose.yml`, `Dockerfile` files to unified `config/` and `infrastructure/` directories.

3. **Unify tests:** Move all `src/test/java` contents to unified `tests/` directory with `unit/`, `integration/`, `e2e/` subdirectories.

4. **Create observability structure:** Add `infrastructure/observability/` with `logging/`, `metrics/`, `tracing/` subdirectories.

### Medium Priority (Tenancy Support)

5. **Add tenancy placeholders:** Create `domain/tenancy/` for tenant isolation patterns.

6. **Environment-specific configs:** Split configurations into `application.yaml`, `application-dev.yaml`, `application-prod.yaml`.

### Low Priority (Documentation)

7. **Consolidate docs:** Move scattered documentation to unified `docs/` structure.

8. **Archive historical logs:** Move `dev-tools/logs` and `dev-tools/error-logs` to archive.

---

**Migration Complexity:** Medium
**Estimated Effort:** 40-60 hours
**Risk Level:** Low (services are production-ready and well-structured)
