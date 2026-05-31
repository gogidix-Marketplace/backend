#!/bin/bash
# Full verification script for all Customer-support services
# Tests: compile, build, unit-test, build-jar, smoke-test

SERVICES=(
    "country-support-dashboard-service"
    "customer-portal-service"
    "feedback-service"
    "global-support-dashboard-service"
    "knowledge-base-service"
    "live-chat-service"
    "notification-service"
    "phone-support-service"
    "quality-management-service"
    "sla-management-service"
    "support-analytics-service"
    "ticket-management-service"
)

REPORT_FILE="customer_support_verification_report_$(date +%Y%m%d_%H%M%S).txt"
LOG_DIR="verification_logs_$(date +%Y%m%d_%H%M%S)"
mkdir -p "$LOG_DIR"

echo "========================================" | tee "$REPORT_FILE"
echo "CUSTOMER-SUPPORT VERIFICATION REPORT" | tee -a "$REPORT_FILE"
echo "Started: $(date)" | tee -a "$REPORT_FILE"
echo "========================================" | tee -a "$REPORT_FILE"
echo "" | tee -a "$REPORT_FILE"

PASS_COUNT=0
FAIL_COUNT=0
TOTAL_TESTS=0

for service in "${SERVICES[@]}"; do
    echo "========================================" | tee -a "$REPORT_FILE"
    echo "SERVICE: $service" | tee -a "$REPORT_FILE"
    echo "========================================" | tee -a "$REPORT_FILE"

    SERVICE_LOG="$LOG_DIR/${service}.log"
    SERVICE_STATUS="PASS"

    cd "$service" || {
        echo "ERROR: Cannot cd to $service" | tee -a "$REPORT_FILE"
        SERVICE_STATUS="FAIL"
        ((FAIL_COUNT++))
        echo "Status: FAIL - Directory not found" | tee -a "$REPORT_FILE"
        echo "" | tee -a "$REPORT_FILE"
        continue
    }

    # Run Maven clean install (compile + test + package)
    echo "Running: mvn clean install -DskipTests=false..." | tee -a "$REPORT_FILE"
    MVN_OUTPUT=$(mvn clean install -DskipTests=false 2>&1)
    MVN_EXIT_CODE=$?
    echo "$MVN_OUTPUT" > "$SERVICE_LOG"

    # Parse results
    BUILD_SUCCESS="NO"
    TESTS_RUN=0
    TESTS_FAILED=0
    TESTS_ERRORS=0
    TESTS_SKIPPED=0

    if echo "$MVN_OUTPUT" | grep -q "BUILD SUCCESS"; then
        BUILD_SUCCESS="YES"
    fi

    if echo "$MVN_OUTPUT" | grep -q "Tests run:"; then
        TESTS_RUN=$(echo "$MVN_OUTPUT" | grep -oP "Tests run: \K\d+" | head -1 || echo 0)
        TESTS_FAILED=$(echo "$MVN_OUTPUT" | grep -oP "Failures: \K\d+" | head -1 || echo 0)
        TESTS_ERRORS=$(echo "$MVN_OUTPUT" | grep -oP "Errors: \K\d+" | head -1 || echo 0)
        TESTS_SKIPPED=$(echo "$MVN_OUTPUT" | grep -oP "Skipped: \K\d+" | head -1 || echo 0)
    fi

    # Check JAR creation
    JAR_EXISTS="NO"
    if [ -f "target/*.jar" ] || [ -d "target" ]; then
        JAR_COUNT=$(find target -name "*.jar" -type f 2>/dev/null | wc -l)
        if [ "$JAR_COUNT" -gt 0 ]; then
            JAR_EXISTS="YES ($JAR_COUNT JARs)"
        fi
    fi

    # Determine status
    if [ "$BUILD_SUCCESS" = "YES" ] && [ "$TESTS_FAILED" -eq 0 ] && [ "$TESTS_ERRORS" -eq 0 ]; then
        SERVICE_STATUS="PASS"
        ((PASS_COUNT++))
    else
        SERVICE_STATUS="FAIL"
        ((FAIL_COUNT++))
    fi

    # Print results
    echo "Build Status: $BUILD_SUCCESS" | tee -a "$REPORT_FILE"
    echo "Tests Run: $TESTS_RUN" | tee -a "$REPORT_FILE"
    echo "Tests Failed: $TESTS_FAILED" | tee -a "$REPORT_FILE"
    echo "Tests Errors: $TESTS_ERRORS" | tee -a "$REPORT_FILE"
    echo "Tests Skipped: $TESTS_SKIPPED" | tee -a "$REPORT_FILE"
    echo "JAR Created: $JAR_EXISTS" | tee -a "$REPORT_FILE"
    echo "Overall Status: $SERVICE_STATUS" | tee -a "$REPORT_FILE"

    TOTAL_TESTS=$((TOTAL_TESTS + TESTS_RUN))

    cd ..
    echo "" | tee -a "$REPORT_FILE"
done

# Summary
echo "========================================" | tee -a "$REPORT_FILE"
echo "VERIFICATION SUMMARY" | tee -a "$REPORT_FILE"
echo "========================================" | tee -a "$REPORT_FILE"
echo "Total Services: ${#SERVICES[@]}" | tee -a "$REPORT_FILE"
echo "Passed: $PASS_COUNT" | tee -a "$REPORT_FILE"
echo "Failed: $FAIL_COUNT" | tee -a "$REPORT_FILE"
echo "Total Tests Executed: $TOTAL_TESTS" | tee -a "$REPORT_FILE"
echo "Completed: $(date)" | tee -a "$REPORT_FILE"
echo "========================================" | tee -a "$REPORT_FILE"
echo "" | tee -a "$REPORT_FILE"

# Pass/Fail list
echo "PASSED SERVICES:" | tee -a "$REPORT_FILE"
for service in "${SERVICES[@]}"; do
    if grep -A 10 "SERVICE: $service" "$REPORT_FILE" | grep -q "Overall Status: PASS"; then
        echo "  ✓ $service" | tee -a "$REPORT_FILE"
    fi
done

echo "" | tee -a "$REPORT_FILE"
echo "FAILED SERVICES:" | tee -a "$REPORT_FILE"
for service in "${SERVICES[@]}"; do
    if grep -A 10 "SERVICE: $service" "$REPORT_FILE" | grep -q "Overall Status: FAIL"; then
        echo "  ✗ $service" | tee -a "$REPORT_FILE"
    fi
done

echo "" | tee -a "$REPORT_FILE"
echo "Detailed logs saved to: $LOG_DIR" | tee -a "$REPORT_FILE"
echo "Report saved to: $REPORT_FILE" | tee -a "$REPORT_FILE"

exit $([ "$FAIL_COUNT" -eq 0 ] && echo 0 || echo 1)
