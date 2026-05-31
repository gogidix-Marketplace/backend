================================================================================
BLUEPRINT VALIDATION REPORT: country-hr-management-service
================================================================================

**Generated:** 2026-03-24T00:00:00Z
**Service Path:** C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\Human-resource\Backend\Java\country-hr-management-service

## SUMMARY

| Metric | Compliance |
|--------|------------|
| **Overall** | **55%** |
| POM Configuration | 75% |
| Domain Model Pattern | 25% |
| Structure & Packages | 65% |

## POM

✅ **Spring Boot 3.2.0 (3.x series)**
   - Current: `3.2.0`
   - Expected: `3.x`

✅ **Java 17**
   - Current: `17`

✅ **JaCoCo plugin present**

✅ **Coverage minimum 0.85 (>=85%)**
   - Current: `0.85`

✅ **Branch minimum 0.75 (>=75%)**
   - Current: `0.75`

⚠ **PIT mutation plugin threshold low**
   - Current: `60`
   - Expected: `60+`
   - Action: Consider increasing to 70%

## DOMAIN_MODEL

✅ **Does not use Lombok @SuperBuilder**
   - Uses standard Lombok @Builder only

⚠ **Domain models need validation for @Document**
   - Action: Verify all domain models have separate Entity classes

❌ **Missing manual Builder pattern**
   - Action: Implement manual Builder class

❌ **Fields should be final**
   - Action: Make domain model fields final

## STRUCTURE

⚠ **Test ratio N/A (target ~1:1.3)**

✅ **Has domain/model, domain/event, domain/repository**
✅ **Has domain/port/out**
✅ **Has infrastructure/config, infrastructure/persistence/mongodb**
✅ **Has application/service**
✅ **Has interfaces/rest**
✅ **Has shared/exception**

⚠ **Missing packages: domain/port/in**

## RECOMMENDED ACTIONS

### Critical (Must Fix)
1. Implement manual Builder pattern
2. Make domain model fields final
3. Verify/create separate Entity classes for all domain models

### Major (Should Fix)
1. Create domain/port/in package
2. Add more unit tests
3. Increase PIT mutation threshold to 70%

## CLASSIFICATION

**YELLOW (55% compliance)** - Minor fixes needed before testing
