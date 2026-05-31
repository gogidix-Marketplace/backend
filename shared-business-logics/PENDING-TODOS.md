# PENDING-TODOS — shared-business-logics

**Status**: BLOCKER — Root pom.xml references 5 non-existent module paths  
**CI Result**: BUILD FAILURE — 8 child module paths not found  
**Root Path**: `shared-business-logics/`

---

## CRITICAL BLOCKER

The root `pom.xml` at `shared-business-logics/pom.xml` references child modules via `Backend/Java` sub-paths that DO NOT EXIST:

| Referenced Path | Actual Path | Status |
|----------------|-------------|--------|
| `orchestration-services/Backend/Java` | Does not exist | MISSING |
| `transaction-orchestration/Backend/Java` | `transaction-orchestration/` (no Backend/Java) | WRONG PATH |
| `universal-tracking-services/Backend/Java` | `universal-tracking-services/` (no Backend/Java) | WRONG PATH |
| `centralized-dashboard/Backend/Java` | `centralized-dashboard/` (no Backend/Java) | WRONG PATH |
| `ai-services/Backend/Java` | `ai-services/` (no Backend/Java) | WRONG PATH |
| `business-operations` | `business-operations/` | OK |
| `business-support` | `business-support/` | OK |
| `security` | `security/` | OK |

Additionally, there's a `services/` directory with 8 sub-directories (ai-services, business-operations, etc.) that appear to be duplicates of the top-level directories.

## GAPS

| # | Gap | Severity | Details |
|---|-----|----------|---------|
| 1 | Root pom.xml module paths wrong | BLOCKER | 5 of 8 modules use `Backend/Java` suffix that doesn't exist |
| 2 | Duplicate directory structure | HIGH | Both `shared-business-logics/services/` and top-level dirs exist with same names |
| 3 | `orchestration-services` missing entirely | BLOCKER | Referenced in pom.xml but no such directory exists at any level |
| 4 | event-driven-architecture in services/ only | MEDIUM | EDA is in `services/event-driven-architecture/` but not referenced in root pom.xml |
| 5 | No api-gateway CI verification | HIGH | `shared-business-logics/api-gateway/` has pom.xml but never CI-tested |
| 6 | Stale Gogidix repo references | MEDIUM | Parent pom references private Gogidix Maven repos |

## PENDING

- [ ] **FIX ROOT POM.XML** — Remove `Backend/Java` suffix from all module references
- [ ] Resolve duplicate dirs (services/ vs top-level) — pick one structure
- [ ] Create or locate `orchestration-services` module
- [ ] Add `event-driven-architecture` to root pom.xml modules
- [ ] CI-verify root `mvn verify` passes
- [ ] Add api-gateway CI verification
- [ ] Remove stale Gogidix Maven repo references
- [ ] Add JaCoCo to all modules
- [ ] Add Dockerfiles to all deployable services
