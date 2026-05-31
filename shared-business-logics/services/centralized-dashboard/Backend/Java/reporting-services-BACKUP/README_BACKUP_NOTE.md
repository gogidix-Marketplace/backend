# BACKUP NOTE - reporting-services

**Status:** BACKED UP - Duplicate Service Identified
**Date:** 2025-02-15
**Backup Location:** centralized-dashboard/Backend/Java/reporting-services-BACKUP

---

## Duplicate Information

This service is a DUPLICATE of:
- **Location:** `shared-infrastructure-x/Backend/Java/05-analytics-intelligence/reporting-service`
- **Also exists:** `shared-infrastructure-x/Backend/Java/05-analytics-intelligence/reporting-service-new`
- **Category:** Analytics & Intelligence

---

## Action Required

1. **Review:** Compare this backup with both reporting-service and reporting-service-new in shared-infrastructure-x
2. **Decision:**
   - If shared-infrastructure-x version(s) are complete → DELETE this backup
   - If this backup has unique features → Migrate those features to shared-infrastructure-x
   - Consider merging reporting-service and reporting-service-new if they overlap

---

## Migration Notes (if needed)

- Source domain: centralized-dashboard
- Target domain: shared-infrastructure-x
- Target category: 05-analytics-intelligence
- Refactoring needed: Align with hexagonal architecture pattern
