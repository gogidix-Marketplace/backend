# PENDING-TODOS — platform/shared-libraries

**Status**: BLOCKING — All downstream services depend on these modules  
**CI Result**: Install Shared Libraries (Mandatory) — SUCCESS on latest run  
**Root Path**: `platform/shared-libraries/Backend/Java/`  
**Modules**: 11 (shared-libraries-bom, shared-exceptions, shared-model, shared-validation, shared-service-discovery, shared-audit, shared-security, shared-messaging, shared-testing, shared-utilities, shared-multitenancy)

---

## GAPS

| # | Gap | Severity | Details |
|---|-----|----------|---------|
| 1 | `shared-audit` compilation failure | BLOCKER | `AuditEventService.java` references `AuditEventRepositoryPort` from `port/out/` — previously gitignored. Fixed in latest commit. Needs CI re-verify. |
| 2 | No integration tests | HIGH | All 11 modules installed with `-DskipTests`. No integration test suites exist. |
| 3 | JaCoCo not configured | MEDIUM | No `jacoco-maven-plugin` in any module pom.xml. Cannot enforce 85% coverage. |
| 4 | No Dockerfiles | MEDIUM | These are JAR libraries, not deployable services. Docker not needed. |
| 5 | Gogidix private repo references | LOW | Parent pom references `ggx-commerce/gogidix-foundation-domain` and `ggx-commerce/shared-business-logics` Maven repos. These cause 401 on CI. Resolved by installing locally first, but pom.xml still contains stale `<repositories>` entries. |

## BLOCKERS

1. **CI dependency chain**: All other builds fail if shared-libraries install fails. Currently working after gitignore fix.
2. **Stale Maven repos**: Parent pom references private Gogidix GitHub Package repos that CI cannot authenticate to. Workaround: install locally before builds.

## PENDING

- [ ] Verify `shared-audit` compiles in CI after gitignore fix
- [ ] Add JaCoCo plugin to all 11 modules
- [ ] Remove stale Gogidix `<repositories>` from parent pom.xml
- [ ] Add integration tests for shared-libraries-bom dependency management
- [ ] Publish to GitHub Packages for cross-job cache sharing
