#!/bin/bash
services=(
  "services/business-operations/billing-service"
  "services/business-operations/payment-processing-service"
  "services/business-operations/tenant-management-service"
  "services/business-operations/user-management-service"
  "services/business-support/sla-management-service"
  "services/centralized-dashboard/Backend/Java/analytics-services/analytics-data-service"
  "services/centralized-dashboard/Backend/Java/analytics-services/business-intelligence-service"
  "services/centralized-dashboard/Backend/Java/analytics-services/metrics-aggregation-service"
  "services/centralized-dashboard/Backend/Java/core-services/dashboard-core-service"
  "services/centralized-dashboard/Backend/Java/core-services/dashboard-shared-service"
  "services/centralized-dashboard/Backend/Java/core-services/data-aggregation-service"
  "services/centralized-dashboard/Backend/Java/data-services/centralized-data-aggregation-service"
  "services/centralized-dashboard/Backend/Java/gateway-services/api-gateway-service"
  "services/centralized-dashboard/Backend/Java/gateway-services/chart-service"
  "services/centralized-dashboard/Backend/Java/gateway-services/websocket-gateway-service"
  "services/centralized-dashboard/Backend/Java/reporting-services/centralized-reporting-service"
  "services/security/auth-service"
  "services/transaction-orchestration/Backend/Java/audit-trail-service"
  "services/transaction-orchestration/Backend/Java/onboarding-tracker-service"
  "services/transaction-orchestration/Backend/Java/progress-step-service"
  "services/transaction-orchestration/Backend/Java/status-broadcast-service"
  "services/transaction-orchestration/Backend/Java/transaction-monitoring-service"
  "services/universal-tracking-services/Backend/Java/universal-tracking-service"
)

passed=0
failed=0
failed_services=""

for service in "${services[@]}"; do
  echo "Testing: $service"
  if cd "$service" 2>/dev/null; then
    if mvn test -q 2>&1 | grep -q "BUILD SUCCESS"; then
      echo "✓ PASSED"
      ((passed++))
    else
      echo "✗ FAILED"
      ((failed++))
      failed_services="$failed_services$service\n"
    fi
    cd - > /dev/null
  else
    echo "✗ NOT FOUND"
    ((failed++))
  fi
done

echo ""
echo "========================================="
echo "Summary (excluding ai-services):"
echo "========================================="
echo "Passed: $passed"
echo "Failed: $failed"
echo ""
echo "Failed services:"
echo -e "$failed_services"
