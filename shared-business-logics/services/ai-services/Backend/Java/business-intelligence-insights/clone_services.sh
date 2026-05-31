#!/bin/bash
# clone_services.sh - Parallel AI service cloning
# Usage: ./clone_services.sh [start_batch] [services_per_batch]

set -e  # Exit on error

BLUEPRINT="ai-customer-segmentation-service"
SERVICES_DIR="ai-services"
LOG_FILE="../CLONING_LOG.md"

# Array of services: batch_number:service_name:priority
declare -A SERVICES=(
    "1:ai-product-recommendation-service:HIGH"
    "2:ai-loyalty-program-service:HIGH"
    "3:ai-customer-churn-prediction-service:HIGH"
    "4:ai-customer-ltv-service:HIGH"
    "5:ai-customer-insight-service:HIGH"
    "6:ai-demographic-segmentation-service:HIGH"
    "7:ai-behavioral-segmentation-service:HIGH"
    "8:ai-psychographic-service:MEDIUM"
    "9:ai-transaction-segmentation-service:MEDIUM"
    "10:ai-customer-analytics-service:HIGH"
    "11:ai-campaign-optimization-service:MEDIUM"
    "12:ai-content-optimization-service:MEDIUM"
    "13:ai-offer-optimization-service:HIGH"
    "14:ai-recommendation-engine-service:HIGH"
    "15:ai-personalization-service:MEDIUM"
    "16:ai-demand-forecasting-service:HIGH"
    "17:ai-inventory-optimization-service:HIGH"
    "18:ai-price-optimization-service:HIGH"
    "19:ai-market-basket-service:MEDIUM"
    "20:ai-cross-sell-service:MEDIUM"
    "21:ai-upsell-service:MEDIUM"
    "22:ai-retention-service:HIGH"
    "23:ai-affinity-service:LOW"
    "24:ai-journey-service:MEDIUM"
    "25:ai-next-best-action-service:MEDIUM"
    "26:ai-sentiment-analysis-service:MEDIUM"
    "27:ai-emotion-service:LOW"
    "28:ai-competitor-analysis-service:MEDIUM"
    "29:ai-search-relevance-service:MEDIUM"
    "30:ai-lookalike-service:LOW"
    "31:ai-lead-scoring-service:HIGH"
    "32:ai-propensity-service:MEDIUM"
    "33:ai-attribution-service:HIGH"
    "34:ai-virality-service:LOW"
    "35:ai-assortment-service:MEDIUM"
    "36:ai-channel-optimization-service:MEDIUM"
    "37:ai-revenue-optimization-service:HIGH"
    "38:ai-qos-service:LOW"
    "39:ai-price-elasticity-service:MEDIUM"
    "40:ai-fraud-detection-service:HIGH"
    "41:ai-sla-service:HIGH"
    "42:ai-transaction-monitoring-service:HIGH"
)

# Function to clone one service
clone_service() {
    local IFS=':'
    read -ra BATCH_NUM SERVICE_NAME PRIORITY <<< "$1"

    # Parse input
    BATCH_NUM="${entry%%:*}"
    SERVICE_NAME="${entry#*:}"
    PRIORITY="${entry##*:}"

    echo "=========================================="
    echo "CLONING SERVICE: $SERVICE_NAME"
    echo "BATCH: $BATCH_NUM | PRIORITY: $PRIORITY"
    echo "STARTED: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "=========================================="

    # Clone blueprint
    echo "→ Cloning blueprint..."
    cp -r "$BLUEPRINT" "$SERVICE_NAME"
    cd "$SERVICE_NAME" 2>/dev/null || { echo "✗ Failed to create directory"; return 1; }

    # Update package names
    echo "→ Updating package names..."
    find . -name "*.java" -type f -exec sed -i \
        's/aicustomersegmentationservice/aicustomersegmentationservice/g' {} \;

    # Determine new package name
    OLD_PKG="aicustomersegmentationservice"
    SERVICE_KEBAB=$(echo "$SERVICE_NAME" | sed 's/ai-//g' | sed 's/-service//g')
    NEW_PKG="ai${SERVICE_KEBAB}service"

    # Update main class
    if [ -f "src/main/java/com/gogidix/aiservices/aicustomersegmentationservice/AICustomerSegmentationServiceApplication.java" ]; then
        OLD_CLASS="AICustomerSegmentationServiceApplication"
        NEW_PREFIX="AI${SERVICE_KEBAB}"
        NEW_CLASS="${NEW_PREFIX}ServiceApplication"
        mv "src/main/java/com/gogidix/aiservices/aicustomersegmentationservice/${OLD_CLASS}.java" \
           "src/main/java/com/gogidix/aiservices/aicustomersegmentationservice/${NEW_CLASS}.java" 2>/dev/null
    fi

    # Update README
    if [ -f "README.md" ]; then
        sed -i "s/AI Customer Segmentation Service/AI ${SERVICE_KEBAB} Service/g" README.md
        sed -i "s/ai-customer-segmentation-service/${SERVICE_NAME}/g" README.md
        sed -i "s/customer segment/${SERVICE_KEBAB}/g" README.md
    fi

    echo "→ Package: ${NEW_PKG}"
    echo "→ Main Class: ${NEW_CLASS}"

    # Compile test
    echo "→ Running compilation test..."
    mvn clean compile -q
    if [ $? -ne 0 ]; then
        echo "✗ COMPILATION FAILED"
        echo "SERVICE: $SERVICE_NAME"
        echo "STATUS: COMPILING"
        echo "ERRORS: 1"
        return 1
    fi
    echo "✓ Compilation passed"

    # Run tests
    echo "→ Running unit tests..."
    mvn test -q -Djacoco.skip=true
    TEST_RESULT=$?
    if [ $TEST_RESULT -ne 0 ]; then
        echo "✗ TESTS FAILED"
        echo "SERVICE: $SERVICE_NAME"
        echo "STATUS: TESTING"
        echo "ERRORS: 1"
        return 1
    fi

    # Count tests
    TEST_OUTPUT=$(find target/surefire-reports -name "TEST-*.xml" -exec cat {} \; 2>/dev/null
    TESTS=$(echo "$TEST_OUTPUT" | grep -oP 'tests="[0-9]*"' | grep -oP 'tests="[0-9]*"' | head -1)
    FAILURES=$(echo "$TEST_OUTPUT" | grep -oP 'failures="[0-9]*"' | grep -oP 'failures="[0-9]*"' | head -1)
    ERRORS=$(echo "$TEST_OUTPUT" | grep -oP 'errors="[0-9]*"' | grep -oP 'errors="[0-9]*"' | head -1)

    echo "✓ Tests: ${TESTS} | Failures: ${FAILURES} | Errors: ${ERRORS}"

    # Package
    echo "→ Creating JAR..."
    mvn package -DskipTests -q
    if [ $? -ne 0 ]; then
        echo "✗ PACKAGE FAILED"
        echo "SERVICE: $SERVICE_NAME"
        echo "STATUS: PACKAGING"
        echo "ERRORS: 1"
        return 1
    fi
    echo "✓ JAR created"

    # Commit
    echo "→ Committing..."
    git add -A
    git commit -m "feat: ${SERVICE_NAME} cloned from blueprint

Tests: ${TESTS}
Coverage: TBD"
    git tag v1.0.0

    echo "SERVICE: $SERVICE_NAME"
    echo "STATUS: DONE"
    echo "=========================================="

    # Log completion
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] SERVICE_DONE: $SERVICE_NAME" >> "$LOG_FILE"
    echo "- Batch ${BATCH_NUM} complete" >> "$LOG_FILE"

    cd ..
    return 0
}

# Main execution - process batches sequentially
main() {
    local BATCH_SIZE=${2:-5}  # Default 5 services per batch
    local TOTAL=42
    local START_AT=${1:-1}

    echo "╔════════════════════════════════════════════╗"
    echo "║     AI SERVICES RAPID CLONING EXECUTION             ║"
    echo "║     Target: 42 services in parallel batches            ║"
    echo "║     Batch Size: $BATCH_SIZE services per batch            ║"
    echo "║     Starting Service: $START_AT                          ║"
    echo "╚════════════════════════════════════════════════╝"
    echo ""

    # Create log file
    echo "# AI Services Cloning Log" > "$LOG_FILE"
    echo "Started: $(date '+%Y-%m-%d %H:%M:%S')" >> "$LOG_FILE"
    echo "Batch Size: $BATCH_SIZE" >> "$LOG_FILE"
    echo "Services: $TOTAL total" >> "$LOG_FILE"
    echo "" >> "$LOG_FILE"

    local completed=0
    local failed=0

    # Process services in batches
    for entry in "${SERVICES[@]}"; do
        if clone_service; then
            ((completed++))
        else
            ((failed++))
        fi

        # Brief pause between batches for system stability
        if [ $((completed % BATCH_SIZE)) -eq 0 ] && [ $completed -lt $TOTAL ]; then
            echo ""
            echo "--- Batch Complete: $completed/$TOTAL services ---"
            sleep 2
        fi
    done

    echo ""
    echo "╔══════════════════════════════════════════════╗"
    echo "║                    CLONING SUMMARY                     ║"
    echo "╠═════════════════════════════════════════════╣"
    echo "║ Completed: ${completed}/${TOTAL} services                   ║"
    echo "║ Failed: ${failed}/${TOTAL} services                         ║"
    echo "║ Success Rate: $(( completed * 100 / TOTAL ))%             ║"
    echo "╚══════════════════════════════════════════════════════╝"

    if [ $completed -eq $TOTAL ]; then
        echo "✓ ALL 42 SERVICES CLONED SUCCESSFULLY!"
        echo ""
        echo "Next: Deploy to Kubernetes"
        return 0
    else
        echo "✗ Some services failed. Check $LOG_FILE"
        return 1
    fi
}

# Usage
if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    echo "Usage: $0 [start_batch] [services_per_batch]"
    echo "  start_batch: Service number to start (default: 1)"
    echo "  services_per_batch: Services per batch (default: 5)"
    echo ""
    echo "Processes 42 services in parallel batches of 5."
    echo "Each service: clone → test → package → commit"
    echo ""
    echo "Output: ../CLONING_LOG.md"
    exit 0
fi

main "$@"
