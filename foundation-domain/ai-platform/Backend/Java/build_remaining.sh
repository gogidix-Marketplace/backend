#!/bin/bash
MAVEN_HOME="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/apache-maven-3.9.12"
MAVEN="$MAVEN_HOME/bin/mvn.cmd"
BASE_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Foundation-domain/ai-services/Backend/Java"

# All service paths (relative to BASE_DIR)
services=(
"customer-experience-engagement/ai-notification-service"
"customer-experience-engagement/ai-personalization-service"
"customer-experience-engagement/ai-recommendation-service"
"customer-experience-engagement/ai-search-service"
"customer-experience-engagement/ai-translation-service"
"customer-experience-engagement/ai-voice-service"
"customer-experience-engagement/voice-recognition-service"
"content-document-processing/ai-document-processing-service"
"content-document-processing/multimodal-processing-service"
"content-document-processing/nlp-processing-service"
"data-analytics/ai-analytics-dashboard-service"
"data-analytics/ai-data-processing-service"
"data-analytics/ai-data-validation-service"
"data-analytics/ai-prediction-service"
"data-analytics/ai-reporting-service"
"data-analytics/predictive-analytics-service"
"data-analytics/time-series-forecasting-service"
"infrastructure-platform/ai-gateway-service"
"infrastructure-platform/ai-monitoring-service"
"infrastructure-platform/ai-orchestration-service"
"infrastructure-platform/ai-testing-service"
"infrastructure-platform/ai-workflow-automation-service"
"infrastructure-platform/performance-optimization-service"
"infrastructure-platform/tenant-service"
"machine-learning-operations/ai-feature-extraction-service"
"machine-learning-operations/ai-feature-store-service"
"machine-learning-operations/ai-inference-service"
"machine-learning-operations/ai-model-management-service"
"machine-learning-operations/ai-model-training-service"
"machine-learning-operations/ai-training-service"
"security-fraud-prevention/ai-security-analysis-service"
"security-fraud-prevention/ai-security-service"
"security-fraud-prevention/anomaly-detection-service"
"business-intelligence-insights/ai-user-profiling-service"
"business-intelligence-insights/intelligence-analysis-service"
"business-intelligence-insights/research-intelligence-service"
"business-operations/lead-generation-ai-service"
"business-operations/supply-chain-optimization-service"
)

failed=()
succeeded=()
skipped=()

for service in "${services[@]}"; do
    service_dir="$BASE_DIR/$service"
    service_name=$(basename "$service")
    
    if [ ! -f "$service_dir/pom.xml" ]; then
        echo "⚠ Skipping $service_name - no pom.xml"
        skipped+=("$service_name")
        continue
    fi
    
    if [ -f "$service_dir/target/*.jar" ]; then
        echo "⊘ Skipping $service_name - already built"
        succeeded+=("$service_name")
        continue
    fi
    
    echo "→ Building $service_name..."
    cd "$service_dir"
    "$MAVEN" clean package -DskipTests -q > build.log 2>&1
    
    if [ $? -eq 0 ] && [ -f "$service_dir/target/*.jar" ]; then
        echo "✓ $service_name built successfully"
        succeeded+=("$service_name")
        rm -f "$service_dir/build.log"
    else
        echo "✗ $service_name failed to build"
        failed+=("$service_name")
    fi
done

echo ""
echo "====================================="
echo "       BUILD SUMMARY"
echo "====================================="
echo ""
echo "Succeeded: ${#succeeded[@]}"
echo "Failed:    ${#failed[@]}"
echo "Skipped:   ${#skipped[@]}"

if [ ${#succeeded[@]} -gt 0 ]; then
    echo ""
    echo "Successfully built services:"
    for name in "${succeeded[@]}"; do
        echo "  ✓ $name"
    done
fi

if [ ${#failed[@]} -gt 0 ]; then
    echo ""
    echo "Failed services:"
    for name in "${failed[@]}"; do
        echo "  ✗ $name"
    done
fi

if [ ${#skipped[@]} -gt 0 ]; then
    echo ""
    echo "Skipped services:"
    for name in "${skipped[@]}"; do
        echo "  ⚠ $name"
    done
fi
