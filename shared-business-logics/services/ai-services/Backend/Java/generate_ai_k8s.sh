#!/bin/bash
# Script to generate K8s manifests for all AI Services

BASE_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/shared-business-logics/ai-services/Backend/Java"
NAMESPACE="ai-services"
TEAM="team-f-ai-services"

declare -A PORT_MAP=(
    ["business-intelligence-insights/ai-customer-segmentation-service"]=8200
    ["business-intelligence-insights/ai-product-recommendation-service"]=8201
    ["business-intelligence-insights/ai-user-profiling-service"]=8202
    ["business-intelligence-insights/intelligence-analysis-service"]=8203
    ["business-intelligence-insights/research-intelligence-service"]=8204
    ["business-operations/ai-churn-prediction-service"]=8210
    ["business-operations/lead-generation-ai-service"]=8211
    ["business-operations/supply-chain-optimization-service"]=8212
    ["content-document-processing/ai-document-classification-service"]=8220
    ["content-document-processing/ai-document-extraction-service"]=8221
    ["content-document-processing/ai-document-processing-service"]=8222
    ["content-document-processing/ai-ocr-service"]=8223
    ["content-document-processing/ai-summarization-service"]=8224
    ["content-document-processing/multimodal-processing-service"]=8225
    ["content-document-processing/nlp-processing-service"]=8226
    ["customer-experience-engagement/ai-content-generation-service"]=8230
    ["customer-experience-engagement/ai-customer-engagement-service"]=8231
    ["customer-experience-engagement/ai-notification-service"]=8232
    ["customer-experience-engagement/ai-personalization-service"]=8233
    ["customer-experience-engagement/ai-recommendation-service"]=8234
    ["customer-experience-engagement/ai-search-service"]=8235
    ["customer-experience-engagement/ai-translation-service"]=8236
    ["customer-experience-engagement/ai-voice-service"]=8237
    ["customer-experience-engagement/voice-recognition-service"]=8238
    ["data-analytics/ai-analytics-dashboard-service"]=8240
    ["data-analytics/ai-data-processing-service"]=8241
    ["data-analytics/ai-data-validation-service"]=8242
    ["data-analytics/ai-prediction-service"]=8243
    ["data-analytics/ai-reporting-service"]=8244
    ["data-analytics/predictive-analytics-service"]=8245
    ["data-analytics/time-series-forecasting-service"]=8246
    ["machine-learning-operations/ai-feature-extraction-service"]=8250
    ["machine-learning-operations/ai-feature-store-service"]=8251
    ["machine-learning-operations/ai-inference-service"]=8252
    ["machine-learning-operations/ai-model-management-service"]=8253
    ["machine-learning-operations/ai-model-training-service"]=8254
    ["machine-learning-operations/ai-training-service"]=8255
    ["security-fraud-prevention/ai-authentication-service"]=8260
    ["security-fraud-prevention/ai-fraud-detection-service"]=8261
    ["security-fraud-prevention/ai-security-analysis-service"]=8262
    ["security-fraud-prevention/ai-security-service"]=8263
    ["security-fraud-prevention/anomaly-detection-service"]=8264
)

for service_path in "${!PORT_MAP[@]}"; do
    PORT=${PORT_MAP[$service_path]}
    SERVICE_NAME=$(basename "$service_path")
    SERVICE_DIR="$BASE_DIR/$service_path"
    K8S_DIR="$SERVICE_DIR/k8s"
    
    if [ ! -d "$K8S_DIR" ]; then
        mkdir -p "$K8S_DIR"
    fi
    
    DB_NAME="${SERVICE_NAME//-ai-/}"
    
    # Create configmap.yaml
    cat > "$K8S_DIR/configmap.yaml" << CMEOF
apiVersion: v1
kind: ConfigMap
metadata:
  name: ${SERVICE_NAME}-config
  namespace: ${NAMESPACE}
  labels:
    app: ${SERVICE_NAME}
    team: ${TEAM}
data:
  mongodb-host: "mongodb"
  mongodb-port: "27017"
  mongodb-db: "${DB_NAME}"
  redis-host: "redis"
  redis-port: "6379"
  kafka-bootstrap-servers: "kafka:9092"
CMEOF

    # Create secrets.yaml
    cat > "$K8S_DIR/secrets.yaml" << SECRETS
apiVersion: v1
kind: Secret
metadata:
  name: ${SERVICE_NAME}-secrets
  namespace: ${NAMESPACE}
  labels:
    app: ${SERVICE_NAME}
    team: ${TEAM}
type: Opaque
stringData:
  mongodb-user: "${DB_NAME}_user"
  mongodb-password: "CHANGE_ME_PROD_PASSWORD"
  redis-password: ""
SECRETS

    # Create service.yaml
    cat > "$K8S_DIR/service.yaml" << SVC
apiVersion: v1
kind: Service
metadata:
  name: ${SERVICE_NAME}
  namespace: ${NAMESPACE}
  labels:
    app: ${SERVICE_NAME}
    team: ${TEAM}
spec:
  type: ClusterIP
  selector:
    app: ${SERVICE_NAME}
  ports:
  - name: http
    port: ${PORT}
    targetPort: ${PORT}
    protocol: TCP
SVC

    echo "Created 4 K8s files for $SERVICE_NAME (port $PORT)"
done

echo ""
echo "AI Services K8s Manifests Complete"
echo "Total services processed: ${#PORT_MAP[@]}"
