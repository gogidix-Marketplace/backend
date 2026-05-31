# Digital-marketing Domain - Compilation Fix Plan

**Assessment Date:** 2026-04-05  
**Total Services:** 16 Java + 1 Node.js  
**Services Requiring Fixes:** 14 Java services

---

## Compilation Results Summary

| Service | Status | Errors | Priority |
|----------|--------|--------|----------|
| analytics-service | ✅ PASS | 0 | - |
| marketing-automation-service | ✅ PASS | 0 | - |
| brand-management-service | ❌ FAIL | Wrong package imports | High |
| budget-management-service | ❌ FAIL | Missing symbol | High |
| campaign-management-service | ❌ FAIL | JWT syntax error | High |
| content-management-service | ❌ FAIL | Wrong package imports | High |
| corporate-cms-service | ❌ FAIL | JWT syntax error | High |
| corporate-website-service | ❌ FAIL | Illegal character `#` | High |
| country-marketing-dashboard-service | ❌ FAIL | Wrong package imports | High |
| email-marketing-service | ❌ FAIL | Wrong file/interface | High |
| global-marketing-dashboard-service | ❌ FAIL | Missing Spring packages | High |
| integration-service | ❌ FAIL | Wrong package imports | High |
| lead-generation-service | ❌ FAIL | JWT syntax error | High |
| seo-service | ❌ FAIL | Wrong package imports | High |
| social-media-service | ❌ FAIL | JWT syntax + illegal char | High |

---

## Common Error Patterns

### 1. Wrong Package Imports (Most Common)
**Error:** `package com.gogidix.marketing.xxx.shared.domain does not exist`

**Affected Services:**
- brand-management-service
- content-management-service
- country-marketing-dashboard-service
- email-marketing-service
- integration-service
- seo-service

**Fix:** Replace shared packages with local package structure
```bash
# Remove 'shared' from imports
sed -i 's/\.shared\.domain//g' **/*.java
sed -i 's/\.shared\.infrastructure\.persistence//g' **/*.java
sed -i 's/\.shared\.infrastructure\.security//g' **/*.java
```

### 2. JWT Token Syntax Errors
**Error:** `<identifier> expected` in JwtTokenUtil.java

**Affected Services:**
- campaign-management-service
- corporate-cms-service
- lead-generation-service
- social-media-service

**Fix:** Fix Java syntax in JWT files - likely missing imports or malformed code

### 3. Interface Naming Mismatch
**Error:** `interface EmailMetricsRepository is public, should be declared in a file named EmailMetricsRepository.java`

**Affected Service:**
- email-marketing-service

**Fix:** Move interface to correct file or rename file to match interface name

### 4. Illegal Characters
**Error:** `illegal character: '#'`

**Affected Services:**
- corporate-website-service
- social-media-service

**Fix:** Remove or replace illegal characters in source files

### 5. Missing Spring Security Packages
**Error:** `package org.springframework.security.oauth2.jwt does not exist`

**Affected Service:**
- global-marketing-dashboard-service

**Fix:** Update Spring Security dependencies or remove OAuth2 imports

---

## Fix Strategy

### Phase 1: Fix Package Imports (7 services)
```bash
# Script to fix shared package imports
for svc in brand-management-service content-management-service \
  country-marketing-dashboard-service email-marketing-service \
  integration-service seo-service; do
  cd "$svc"
  find . -name "*.java" -exec sed -i 's/\.shared\.domain//g' {} \;
  find . -name "*.java" -exec sed -i 's/\.shared\.infrastructure\.persistence//g' {} \;
  find . -name "*.java" -exec sed -i 's/\.shared\.infrastructure\.security//g' {} \;
  cd ..
done
```

### Phase 2: Fix JWT Syntax (4 services)
Review and fix JWT files in:
- campaign-management-service
- corporate-cms-service  
- lead-generation-service
- social-media-service

### Phase 3: Fix Interface Naming (1 service)
Fix email-marketing-service repository interface

### Phase 4: Fix Illegal Characters (2 services)
Fix corporate-website-service and social-media-service

### Phase 5: Fix Dependencies (1 service)
Fix global-marketing-dashboard-service Spring Security imports

---

## Blueprint Services

**Use these as references:**
- analytics-service (✅ Compiles)
- marketing-automation-service (✅ Compiles)

---

## Estimated Fix Time

| Phase | Services | Time |
|-------|----------|------|
| Package imports | 7 | 30 min |
| JWT syntax | 4 | 1 hour |
| Interface naming | 1 | 15 min |
| Illegal characters | 2 | 30 min |
| Dependencies | 1 | 30 min |
| **Total** | **14** | **~3 hours** |

---

## Commands to Verify Fixes

```bash
# Test compilation after fixes
cd Digital-marketing/Backend/Java
for svc in */; do
  echo "Testing: $svc"
  cd "$svc" && mvn compile -q && echo "✅ PASS" || echo "❌ FAIL"
  cd ..
done
```

---

## Next Steps

1. ✅ Assessment complete
2. ⏳ Execute Phase 1: Fix package imports
3. ⏳ Execute Phase 2-5: Fix remaining issues
4. ⏳ Build JARs for all services
5. ⏳ Document final results

---

**Status:** Ready for fix implementation  
**Created:** 2026-04-05  
**Owner:** Development Team
