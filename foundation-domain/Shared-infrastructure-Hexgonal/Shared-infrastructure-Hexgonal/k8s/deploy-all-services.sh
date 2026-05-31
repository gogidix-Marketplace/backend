#!/bin/bash

################################################################################
# Foundation Domain - Complete Services Deployment Script
# Deploys ALL services with infrastructure dependencies in correct order
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
SHARED_INFRA_DIR="$(dirname "$SCRIPT_DIR")"

# Kubernetes context
NAMESPACE="infrastructure"
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

    print_success "Prerequisites check passed"
}

# Deploy infrastructure components
deploy_infrastructure() {
    print_info "=== Step 1: Deploying Infrastructure Components ==="

    # Apply namespace and policies
    print_info "Creating namespace and policies..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/k8s/namespace.yaml" 2>/dev/null || true
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/k8s/network-policies.yaml" 2>/dev/null || true
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/k8s/resource-quotas.yaml" 2>/dev/null || true

    # Deploy MongoDB
    print_info "Deploying MongoDB..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/mongodb/k8s/mongodb.yaml" 2>/dev/null
    $KUBECTL rollout status statefulset/mongodb -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "MongoDB deployment delayed"

    # Deploy PostgreSQL
    print_info "Deploying PostgreSQL..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/postgresql/k8s/postgresql.yaml" 2>/dev/null
    $KUBECTL rollout status statefulset/postgresql -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "PostgreSQL deployment delayed"

    # Deploy Kafka
    print_info "Deploying Kafka (Zookeeper + Kafka)..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/kafka/k8s/kafka.yaml" 2>/dev/null
    $KUBECTL rollout status statefulset/zookeeper -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Zookeeper deployment delayed"
    $KUBECTL rollout status statefulset/kafka -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Kafka deployment delayed"

    # Deploy Redis
    print_info "Deploying Redis + Sentinel..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/infrastructure/redis/k8s/redis.yaml" 2>/dev/null
    $KUBECTL rollout status statefulset/redis -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Redis deployment delayed"

    print_success "Infrastructure deployment initiated"
}

# Deploy core services
deploy_core_services() {
    print_info "=== Step 2: Deploying Core Gateway Services ==="

    # Config Server
    print_info "Deploying Config Server..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/infrastructure/config-server/k8s/config-server.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/config-server -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Config Server deployment delayed"

    # Discovery Service
    print_info "Deploying Discovery Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/gateway/discovery-service/k8s/discovery-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/discovery-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Discovery Service deployment delayed"

    # API Gateway
    print_info "Deploying API Gateway..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/gateway/api-gateway-service/k8s/api-gateway.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/api-gateway-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "API Gateway deployment delayed"

    # Caching Service
    print_info "Deploying Caching Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/infrastructure/caching-service/k8s/caching-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/caching-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Caching Service deployment delayed"

    # Message Broker Service
    print_info "Deploying Message Broker Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/infrastructure/message-broker-service/k8s/message-broker-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/message-broker-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Message Broker Service deployment delayed"

    print_success "Core services deployment initiated"
}

# Deploy security services
deploy_security_services() {
    print_info "=== Step 3: Deploying Security Services ==="

    # DLP Service
    print_info "Deploying DLP Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/dlp-service/k8s/dlp-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/dlp-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "DLP Service deployment delayed"

    # MFA Service
    print_info "Deploying MFA Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/mfa-service/k8s/mfa-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/mfa-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "MFA Service deployment delayed"

    # Secrets Management Service
    print_info "Deploying Secrets Management Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/secrets-management-service/k8s/secrets-management-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/secrets-management-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Secrets Management Service deployment delayed"

    # Security Analytics Service
    print_info "Deploying Security Analytics Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/security-analytics-service/k8s/security-analytics-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/security-analytics-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Security Analytics Service deployment delayed"

    # Security Management Service
    print_info "Deploying Security Management Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/security-management-service/k8s/security-management-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/security-management-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Security Management Service deployment delayed"

    # Security Orchestration Service
    print_info "Deploying Security Orchestration Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/security-orchestration-service/k8s/security-orchestration-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/security-orchestration-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Security Orchestration Service deployment delayed"

    # Threat Intelligence Service
    print_info "Deploying Threat Intelligence Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/security/threat-intelligence-service/k8s/threat-intelligence-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/threat-intelligence-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Threat Intelligence Service deployment delayed"

    print_success "Security services deployment initiated"
}

# Deploy communication services
deploy_communication_services() {
    print_info "=== Step 4: Deploying Communication Services ==="

    # Email Sender Service
    print_info "Deploying Email Sender Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/email-sender-service/k8s/email-sender-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/email-sender-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Email Sender Service deployment delayed"

    # SMS Sender Service
    print_info "Deploying SMS Sender Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/sms-sender-service/k8s/sms-sender-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/sms-sender-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "SMS Sender Service deployment delayed"

    # Notification Service
    print_info "Deploying Notification Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/notification-service/k8s/notification-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/notification-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Notification Service deployment delayed"

    # Webhook Management Service
    print_info "Deploying Webhook Management Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/webhook-management-service/k8s/webhook-management-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/webhook-management-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Webhook Management Service deployment delayed"

    # Message Queue Management Service
    print_info "Deploying Message Queue Management Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/message-queue-management-service/k8s/message-queue-management-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/message-queue-management-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Message Queue Management Service deployment delayed"

    # Event Bus Bridge Service
    print_info "Deploying Event Bus Bridge Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/event-bus-bridge-service/k8s/event-bus-bridge-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/event-bus-bridge-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Event Bus Bridge Service deployment delayed"

    # Status Broadcast Service
    print_info "Deploying Status Broadcast Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/status-broadcast-service/k8s/status-broadcast-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/status-broadcast-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Status Broadcast Service deployment delayed"

    # Social Media Integration Service
    print_info "Deploying Social Media Integration Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/social-media-integration-service/k8s/social-media-integration-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/social-media-integration-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Social Media Integration Service deployment delayed"

    # Message Broker (secondary)
    print_info "Deploying Message Broker (Communication)..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/communication/message-broker/k8s/message-broker.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/message-broker -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Message Broker deployment delayed"

    print_success "Communication services deployment initiated"
}

# Deploy storage and observability services
deploy_storage_observability_services() {
    print_info "=== Step 5: Deploying Storage & Observability Services ==="

    # File Storage Service
    print_info "Deploying File Storage Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/storage/file-storage-service/k8s/file-storage-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/file-storage-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "File Storage Service deployment delayed"

    # Audit Service
    print_info "Deploying Audit Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/observability/audit-service/k8s/audit-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/audit-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Audit Service deployment delayed"

    # Rate Limiting Service
    print_info "Deploying Rate Limiting Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/observability/rate-limiting-service/k8s/rate-limiting-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/rate-limiting-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "Rate Limiting Service deployment delayed"

    print_success "Storage & Observability services deployment initiated"
}

# Deploy API management services
deploy_api_management_services() {
    print_info "=== Step 6: Deploying API Management Services ==="

    # API Rate Limit Service
    print_info "Deploying API Rate Limit Service..."
    $KUBECTL apply -f "$SHARED_INFRA_DIR/services/api-management/api-rate-limit-service/k8s/api-rate-limit-service.yaml" 2>/dev/null || true
    $KUBECTL rollout status deployment/api-rate-limit-service -n "$NAMESPACE" --timeout=300s 2>/dev/null || print_warning "API Rate Limit Service deployment delayed"

    print_success "API Management services deployment initiated"
}

# Verify deployment
verify_deployment() {
    print_info "=== Step 7: Verifying Deployment ==="

    print_info "Checking pods..."
    $KUBECTL get pods -n "$NAMESPACE"

    print_info "Checking services..."
    $KUBECTL get svc -n "$NAMESPACE"

    # Summary
    echo ""
    print_info "=== Deployment Summary ==="
    print_info ""
    print_info "Connection Strings:"
    print_info "MongoDB: mongodb://<user>:<pass>@mongodb-0.$NAMESPACE.svc.cluster.local:27017,..."
    print_info "PostgreSQL: postgresql://<user>:<pass>@postgresql.$NAMESPACE.svc.cluster.local:5432/<db>"
    print_info "Kafka: kafka-bootstrap.$NAMESPACE.svc.cluster.local:9092"
    print_info "Redis: redis-sentinel-cluster.$NAMESPACE.svc.cluster.local:26379 (master: mymaster)"
    print_info ""
    print_info "Core Services:"
    print_info "Discovery Service: http://discovery-service:8761/eureka/"
    print_info "Config Server: http://config-server:8888/"
    print_info "API Gateway: http://api-gateway-service:8080/"
    print_info ""
    print_info "CRITICAL: Update all secrets before production use!"
    print_info "  - mongodb-credentials"
    print_info "  - postgresql-credentials"
    print_info "  - redis-credentials"
    print_info "  - kafka-credentials"
    print_info "  - All service secrets (dlp, mfa, secrets, etc.)"
    print_info ""
    print_success "Foundation Domain deployment complete!"
}

# Main deployment flow
main() {
    print_info "========================================================"
    print_info "  Foundation Domain - Complete Deployment"
    print_info "========================================================"
    print_info ""

    # Step 1: Prerequisites
    check_prerequisites
    if [ $? -ne 0 ]; then
        exit 1
    fi

    # Step 2: Infrastructure
    deploy_infrastructure

    # Step 3: Core Services
    deploy_core_services

    # Step 4: Security Services
    deploy_security_services

    # Step 5: Communication Services
    deploy_communication_services

    # Step 6: Storage & Observability
    deploy_storage_observability_services

    # Step 7: API Management
    deploy_api_management_services

    # Step 8: Verify
    verify_deployment

    print_info "========================================================"
    print_info "           Deployment Complete!"
    print_info "========================================================"
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
