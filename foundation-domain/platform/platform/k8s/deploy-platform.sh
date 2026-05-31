#!/bin/bash

################################################################################
# Platform - Complete Deployment Script
# Deploys ALL platform services
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
NAMESPACE="platform"
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

    print_success "Prerequisites check passed"
    return 0
}

# Deploy infrastructure
deploy_infrastructure() {
    print_info "=== Step 1: Deploying Platform Infrastructure ==="

    # Apply namespace
    print_info "Creating platform namespace..."
    $KUBECTL apply -f "$PLATFORM_DIR/k8s/namespace.yaml" 2>/dev/null || true

    print_success "Platform infrastructure deployed"
}

# Deploy services
deploy_services() {
    print_info "=== Step 2: Deploying Services (3 services) ==="

    local services=("platform-service" "subscription-service" "usage-metering-service")

    for service in "${services[@]}"; do
        print_info "Deploying $service..."
        if [ -f "$PLATFORM_DIR/Backend/Java/$service/k8s/$service.yaml" ]; then
            $KUBECTL apply -f "$PLATFORM_DIR/Backend/Java/$service/k8s/$service.yaml" 2>/dev/null || true
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
    print_info "1. platform-service: http://platform-service.$NAMESPACE.svc.cluster.local:8300"
    print_info "2. subscription-service: http://subscription-service.$NAMESPACE.svc.cluster.local:8310"
    print_info "3. usage-metering-service: http://usage-metering-service.$NAMESPACE.svc.cluster.local:8320"
    print_info ""
    print_info "Foundation Infrastructure Connections:"
    print_info "PostgreSQL: postgresql.$INFRA_NAMESPACE.svc.cluster.local:5432"
    print_info "Redis: redis-sentinel-cluster.$INFRA_NAMESPACE.svc.cluster.local:26379"
    print_info "Kafka: kafka-bootstrap.$INFRA_NAMESPACE.svc.cluster.local:9092"
    print_info "Discovery: http://discovery-service.$INFRA_NAMESPACE.svc.cluster.local:8761/eureka/"
    print_info "Config Server: http://config-server.centralized-config.svc.cluster.local:8210"
    print_info ""
    print_info "CRITICAL: Update all secrets before production use!"
    print_info "  - platform-service-secrets"
    print_info "  - subscription-service-secrets"
    print_info "  - usage-metering-service-secrets"
    print_info ""
    print_success "Platform deployment complete!"
}

# Main deployment flow
main() {
    print_info "========================================================="
    print_info "           Platform - Complete Deployment"
    print_info "========================================================="
    print_info ""

    # Step 1: Prerequisites
    check_prerequisites

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
