#!/bin/bash
# Fix executive-audit-service package errors
echo "Fixing executive-audit-service..."

# Fix package imports with double "audit"
find "executive-audit-service/src" -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.audit\.domain\.model\.audit/com.gogidix.management.executive.audit.domain.model/g' {} \;

# Also fix any other wrong package references
find "executive-audit-service/src" -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.domain\.service/com.gogidix.management.executive.audit.domain.service/g' {} \;
find "executive-audit-service/src" -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.application\./com.gogidix.management.executive.audit.application./g' {} \;
find "executive-audit-service/src" -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.infrastructure\./com.gogidix.management.executive.audit.infrastructure./g' {} \;
find "executive-audit-service/src" -name "*.java" -exec sed -i 's/com\.gogidix\.management\.executive\.interfaces\./com.gogidix.management.executive.audit.interfaces./g' {} \;

echo "Fixed executive-audit-service"
