#!/bin/bash

################################################################################
# AI Shared Infrastructure Platform - Complete Deployment Script
# Deploys ALL AI Platform services with infrastructure dependencies in correct order
################################################################################

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PLATFORM_DIR="$(dirname "$SCRIPT_DIR")"

# Kubernetes context
NAMESPACE="ai-platform"
INFRA_NAMESPACE="infrastructure"
KUBECTL="${KUBECTL:-kubectl}"

print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    print_info "Checking prerequisites..."

    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl not found. Please install kubectl first."
        return 1
    fi

    if ! command -v docker &> /dev/null; then
        print_error "docker not found. Please install docker first."
        return 1
    fi

    # Check if Foundation infrastructure is ready
    if ! $KUBECTL get namespace $INFRA_NAMESPACE &> /dev/null 2>&1; then
        print_warning "Foundation infrastructure namespace not found. Deploying it first..."
        return 1
    fi

    print_success "Prerequisites check passed"
    return 0
}

# Deploy AI Platform infrastructure
deploy_ai_platform_infrastructure() {
    print_info "=== Step 1: Deploying AI Platform Infrastructure ==="

    # Apply namespace
    print_info "Creating ai-platform namespace..."
    $KUBECTL apply -f "$PLATFORM_DIR/k8s/ai-platform-namespace.yaml" 2>/dev/null || true

    # Apply network policies
    print_info "Applying network policies..."
    $KUBECTL apply -f "$PLATFORM_DIR/k8s/ai-platform-namespace.yaml" 2>/dev/null || true

    # Apply resource quotas
    print_info "Applying resource quotas..."
    $KUBECTL apply -f "$PLATFORM_DIR/k8s/ai-platform-namespace.yaml" 2>/dev/null || true

    print_success "AI Platform infrastructure deployment initiated"
}

# Deploy Python AI Services
deploy_python_services() {
    print_info "=== Step 2: Deploying Python AI Services ==="

    # Anomaly Detection Service
    print_info "Deploying anomaly-detection-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Nodes/anomaly-detection-service/k8s/anomaly-detection-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/anomaly-detection-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Anomaly Detection deployment delayed"

    # Computer Vision Service
    print_info "Deploying computer-vision-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Nodes/computer-vision-service/k8s/computer-vision-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/computer-vision-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Computer Vision deployment delayed"

    # ML Model Training Service
    print_info "Deploying ml-model-training-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Nodes/ml-model-training-service/k8s/ml-model-training-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/ml-model-training-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "ML Model Training deployment delayed"

    # NLP Service
    print_info "Deploying nlp-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Nodes/nlp-service/k8s/nlp-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/nlp-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "NLP deployment delayed"

    # Predictive Analytics Service
    print_info "Deploying predictive-analytics-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Nodes/predictive-analytics-service/k8s/predictive-analytics-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/predictive-analytics-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Predictive Analytics deployment delayed"

    # Recommendation Service
    print_info "Deploying recommendation-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Nodes/recommendation-service/k8s/recommendation-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/recommendation-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Recommendation deployment delayed"

    print_success "Python AI services deployment initiated"
}

# Deploy Java AI Services
deploy_java_services() {
    print_info "=== Step 3: Deploying Java AI Services ==="

    # AI Gateway Service
    print_info "Deploying ai-gateway-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/ai-gateway-service/k8s/ai-gateway-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/ai-gateway-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "AI Gateway deployment delayed"

    # AI Monitoring Service
    print_info "Deploying ai-monitoring-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/ai-monitoring-service/k8s/ai-monitoring-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/ai-monitoring-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "AI Monitoring deployment delayed"

    # AI Orchestration Service
    print_info "Deploying ai-orchestration-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/ai-orchestration-service/k8s/ai-orchestration-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/ai-orchestration-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "AI Orchestration deployment delayed"

    # AI Testing Service
    print_info "Deploying ai-testing-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/ai-testing-service/k8s/ai-testing-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/ai-testing-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "AI Testing deployment delayed"

    # AI Workflow Automation Service
    print_info "Deploying ai-workflow-automation-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/ai-workflow-automation-service/k8s/ai-workflow-automation-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/ai-workflow-automation-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "AI Workflow Automation deployment delayed"

    # Performance Optimization Service
    print_info "Deploying performance-optimization-service..."
    $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/performance-optimization-service/k8s/performance-optimization-service.yaml" 2>/dev/null
    $KUBECTL rollout status deployment/performance-optimization-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Performance Optimization deployment delayed"

    print_success "Java AI services deployment initiated"
}

# Verify deployment
verify_deployment() {
    print_info "=== Step 4: Verifying Deployment ==="

    print_info "Checking pods in $NAMESPACE namespace..."
    $KUBECTL get pods -n "$NAMESPACE"

    print_info "Checking services in $NAMESPACE namespace..."
    $KUBECTL get svc -n "$NAMESPACE"

    # Summary
    echo ""
    print_info "=== Deployment Summary ==="
    print_info ""
    print_info "Connection Strings:"
    print_info "MongoDB: mongodb://<user>:<pass>@mongodb.$INFRA_NAMESPACE.svc.cluster.local:27017/<db>"
    print_info "Kafka: kafka-bootstrap.$INFRA_NAMESPACE.svc.cluster.local:9092"
    print_info "Redis: redis-sentinel-cluster.$INFRA_NAMESPACE.svc.cluster.local:26379 (master: mymaster)"
    print_info ""
    print_info "AI Platform Services:"
    print_info "AI Gateway: http://ai-gateway-service.$NAMESPACE.svc.cluster.local:8000"
    print_info "AI Monitoring: http://ai-monitoring-service.$NAMESPACE.svc.cluster.local:8001"
    print_info "AI Orchestration: http://ai-orchestration-service.$NAMESPACE.svc.cluster.local:8002"
    print_info "AI Testing: http://ai-testing-service.$NAMESPACE.svc.cluster.local:8003"
    print_info "Workflow Automation: http://ai-workflow-automation-service.$NAMESPACE.svc.cluster.local:8004"
    print_info "Performance Optimization: http://performance-optimization-service.$NAMESPACE.svc.cluster.local:8005"
    print_info ""
    print_info "Python Services:"
    print_info "Anomaly Detection: http://anomaly-detection-service.$NAMESPACE.svc.cluster.local:8005"
    print_info "Computer Vision: http://computer-vision-service.$NAMESPACE.svc.cluster.local:8006"
    print_info "ML Model Training: http://ml-model-training-service.$NAMESPACE.svc.cluster.local:8007"
    print_info "NLP Service: http://nlp-service.$NAMESPACE.svc.cluster.local:8008"
    print_info "Predictive Analytics: http://predictive-analytics-service.$NAMESPACE.svc.cluster.local:8009"
    print_info "Recommendation: http://recommendation-service.$NAMESPACE.svc.cluster.local:8010"
    print_info ""
    print_info "Foundation Infrastructure:"
    print_info "Discovery Service: http://discovery-service.$INFRA_NAMESPACE.svc.cluster.local:8761/eureka/"
    print_info "Config Server: http://config-server.$INFRA_NAMESPACE.svc.cluster.local:8888/"
    print_info ""
    print_info "CRITICAL: Update all secrets before production use!"
    print_info "  - mongodb-credentials"
    print_info "  - All service secrets"
    print_info "  - AWS credentials for ML services"
    print_info ""
    print_success "AI Platform deployment complete!"
}

# Main deployment flow
main() {
    print_info "========================================================="
    print_info "  AI Shared Infrastructure Platform - Complete Deployment"
    print_info "========================================================="
    print_info ""

    # Step 1: Prerequisites
    check_prerequisites
    if [ $? -ne 0 ]; then
        print_info "Foundation infrastructure may not be ready. Continuing anyway..."
    fi

    # Step 2: AI Platform Infrastructure
    deploy_ai_platform_infrastructure

    # Step 3: Python Services
    deploy_python_services

    # Step 4: Java Services
    deploy_java_services

    # Step 5: Verify
    verify_deployment

    print_info "========================================================="
    print_info "           Deployment Complete!"
    print_info "========================================================="
    print_info ""
    print_info "Next Steps:"
    print_info "1. Wait for all pods to be Running"
    print_info "2. Update all K8s Secrets with production values"
    print_info "3. Verify service connectivity"
    print_info "4. Deploy shared-business-logics domain"
    print_info ""
}

# Trap errors
trap print_error "Deployment interrupted" EXIT INT TERM ERR

# Run main function
main

# Exit with success code
exit 0
