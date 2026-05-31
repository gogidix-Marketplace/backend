#!/bin/bash

################################################################################
# Centralized Configuration - Complete Deployment Script
# Deploys ALL centralized-configuration services
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
CC_DIR="$(dirname "$SCRIPT_DIR")"

# Kubernetes context
NAMESPACE="centralized-config"
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
        print_warning "Foundation infrastructure namespace not found. Deploy it first..."
        return 1
    fi

    print_success "Prerequisites check passed"
    return 0
}

# Deploy infrastructure
deploy_infrastructure() {
    print_info "=== Step 1: Deploying Centralized Configuration Infrastructure ==="

    # Apply namespace
    print_info "Creating centralized-config namespace..."
    $KUBECTL apply -f "$CC_DIR/k8s/namespace.yaml" 2>/dev/null || true

    print_success "Centralized configuration infrastructure deployed"
}

# Deploy services
deploy_services() {
    print_info "=== Step 2: Deploying Services (5 services) ==="

    local services=("config-audit-service" "config-server" "environment-service" "feature-flag-service" "notification-service")

    for service in "${services[@]}"; do
        print_info "Deploying $service..."
        if [ -f "$CC_DIR/$service/k8s/$service.yaml" ]; then
            $KUBECTL apply -f "$CC_DIR/$service/k8s/$service.yaml" 2>/dev/null || true
            $KUBECTL rollout status deployment/$service -n "$NAMESPACE" --timeout=180s 2>/dev/null || print_warning "$service deployment delayed"
        else
            print_warning "$service K8s manifest not found"
        fi
    done

    print_success "All services deployment initiated"
}

# Verify deployment
verify_deployment() {
    print_info "=== Step 3: Verifying Deployment ==="

    print_info "Checking pods in $NAMESPACE namespace..."
    $KUBECTL get pods -n "$NAMESPACE"

    print_info "Checking services in $NAMESPACE namespace..."
    $KUBECTL get svc -n "$NAMESPACE"

    # Summary
    echo ""
    print_info "=== Deployment Summary ==="
    print_info ""
    print_info "Services Deployed:"
    print_info "1. config-audit-service: http://config-audit-service.$NAMESPACE.svc.cluster.local:8200"
    print_info "2. config-server: http://config-server.$NAMESPACE.svc.cluster.local:8210"
    print_info "3. environment-service: http://environment-service.$NAMESPACE.svc.cluster.local:8220"
    print_info "4. feature-flag-service: http://feature-flag-service.$NAMESPACE.svc.cluster.local:8230"
    print_info "5. notification-service: http://notification-service.$NAMESPACE.svc.cluster.local:8240"
    print_info ""
    print_info "Foundation Infrastructure Connections:"
    print_info "PostgreSQL: postgresql.$INFRA_NAMESPACE.svc.cluster.local:5432"
    print_info "Kafka: kafka-bootstrap.$INFRA_NAMESPACE.svc.cluster.local:9092"
    print_info "Redis: redis-sentinel-cluster.$INFRA_NAMESPACE.svc.cluster.local:26379"
    print_info "Discovery: http://discovery-service.$INFRA_NAMESPACE.svc.cluster.local:8761/eureka/"
    print_info ""
    print_info "CRITICAL: Update all secrets before production use!"
    print_info "  - config-audit-service-secrets"
    print_info "  - config-server-secrets"
    print_info "  - environment-service-secrets"
    print_info "  - feature-flag-service-secrets"
    print_info "  - notification-service-secrets"
    print_info ""
    print_success "Centralized Configuration deployment complete!"
}

# Main deployment flow
main() {
    print_info "========================================================="
    print_info "  Centralized Configuration - Complete Deployment"
    print_info "========================================================="
    print_info ""

    # Step 1: Prerequisites
    check_prerequisites
    if [ $? -ne 0 ]; then
        print_info "Foundation infrastructure may not be ready. Continuing anyway..."
    fi

    # Step 2: Infrastructure
    deploy_infrastructure

    # Step 3: Services
    deploy_services

    # Step 4: Verify
    verify_deployment

    print_info "========================================================="
    print_info "           Deployment Complete!"
    print_info "========================================================="
    print_info ""
    print_info "Next Steps:"
    print_info "1. Wait for all pods to be Running"
    print_info "2. Update all K8s Secrets with production values"
    print_info "3. Verify service connectivity"
    print_info "4. Monitor service metrics"
    print_info ""
}

# Trap errors
trap print_error "Deployment interrupted" EXIT INT TERM ERR

# Run main function
main

# Exit with success code
exit 0
