#!/bin/bash
cd "services/ai-services/Backend/Java"

services=(
  "business-intelligence-insights/ai-churn-prediction-service"
  "business-intelligence-insights/ai-customer-segmentation-service"
  "business-intelligence-insights/ai-market-basket-analysis-service"
  "business-intelligence-insights/ai-product-recommendation-service"
  "business-intelligence-insights/ai-sales-forecasting-service"
  "business-intelligence-insights/ai-user-profiling-service"
  "business-intelligence-insights/intelligence-analysis-service"
  "business-intelligence-insights/research-intelligence-service"
  "business-operations/ai-business-automation-service"
  "business-operations/lead-generation-ai-service"
  "business-operations/supply-chain-optimization-service"
  "content-document-processing/ai-document-classification-service"
  "content-document-processing/ai-document-extraction-service"
  "content-document-processing/ai-document-processing-service"
  "content-document-processing/ai-ocr-service"
  "content-document-processing/ai-summarization-service"
  "content-document-processing/multimodal-processing-service"
  "content-document-processing/nlp-processing-service"
  "customer-experience-engagement/ai-chatbot-service"
  "customer-experience-engagement/ai-content-generation-service"
  "customer-experience-engagement/ai-customer-engagement-service"
  "customer-experience-engagement/ai-customer-feedback-service"
  "customer-experience-engagement/ai-email-optimization-service"
  "customer-experience-engagement/ai-notification-service"
  "customer-experience-engagement/ai-personalization-service"
  "customer-experience-engagement/ai-recommendation-engine-service"
  "customer-experience-engagement/ai-recommendation-service"
  "customer-experience-engagement/ai-search-service"
  "customer-experience-engagement/ai-sentiment-analysis-service"
  "customer-experience-engagement/ai-translation-service"
  "customer-experience-engagement/ai-voice-assistant-service"
  "customer-experience-engagement/ai-voice-service"
  "customer-experience-engagement/voice-recognition-service"
  "data-analytics/ai-analytics-dashboard-service"
  "data-analytics/ai-data-processing-service"
  "data-analytics/ai-data-validation-service"
  "data-analytics/ai-prediction-service"
  "data-analytics/ai-reporting-service"
  "data-analytics/predictive-analytics-service"
  "data-analytics/time-series-forecasting-service"
  "machine-learning-operations/ai-feature-extraction-service"
  "machine-learning-operations/ai-feature-store-service"
  "machine-learning-operations/ai-inference-service"
  "machine-learning-operations/ai-model-management-service"
  "machine-learning-operations/ai-model-training-service"
  "machine-learning-operations/ai-training-service"
  "security-fraud-prevention/ai-authentication-service"
  "security-fraud-prevention/ai-fraud-detection-service"
  "security-fraud-prevention/ai-security-analysis-service"
  "security-fraud-prevention/ai-security-service"
  "security-fraud-prevention/anomaly-detection-service"
)

passed=0
failed=0

for service in "${services[@]}"; do
  echo "Testing: $service"
  if cd "$service" 2>/dev/null; then
    result=$(mvn test -q 2>&1)
    if echo "$result" | grep -q "BUILD SUCCESS"; then
      echo "✓ PASSED"
      ((passed++))
    else
      echo "✗ FAILED"
      ((failed++))
    fi
    cd - > /dev/null
  else
    echo "✗ NOT FOUND"
    ((failed++))
  fi
done

echo ""
echo "========================================="
echo "AI Services Summary (51 total):"
echo "========================================="
echo "Passed: $passed"
echo "Failed: $failed"
