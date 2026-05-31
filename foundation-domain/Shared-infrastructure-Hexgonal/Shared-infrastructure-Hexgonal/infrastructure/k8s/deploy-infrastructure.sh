#!/bin/bash

# Foundation Domain Infrastructure Deployment Script
# Deploys MongoDB, PostgreSQL, Kafka (Zookeeper), and Redis to Kubernetes

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
INFRA_DIR="$(dirname "$SCRIPT_DIR")"

# Functions
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_kubectl() {
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl not found. Please install kubectl first."
        exit 1
    fi
    print_success "kubectl found: $(kubectl version --client -o json | jq -r '.clientVersion.gitVersion' 2>/dev/null || kubectl version --client)"
}

check_cluster() {
    if ! kubectl cluster-info &> /dev/null; then
        print_error "Cannot connect to Kubernetes cluster. Please check your kubeconfig."
        exit 1
    fi
    print_success "Kubernetes cluster is accessible"
}

deploy_namespace() {
    print_info "Creating infrastructure namespace..."
    kubectl apply -f "$SCRIPT_DIR/namespace.yaml"
    print_success "Infrastructure namespace created"
}

deploy_network_policies() {
    print_info "Applying network policies..."
    kubectl apply -f "$SCRIPT_DIR/network-policies.yaml"
    print_success "Network policies applied"
}

deploy_resource_quotas() {
    print_info "Applying resource quotas..."
    kubectl apply -f "$SCRIPT_DIR/resource-quotas.yaml"
    print_success "Resource quotas applied"
}

deploy_mongodb() {
    print_info "Deploying MongoDB (3 replicas)..."
    kubectl apply -f "$INFRA_DIR/mongodb/k8s/mongodb.yaml"
    print_success "MongoDB deployment submitted"

    # Wait for MongoDB to be ready
    print_info "Waiting for MongoDB StatefulSet to be ready..."
    kubectl rollout status statefulset/mongodb -n infrastructure --timeout=300s
    print_success "MongoDB is ready"
}

deploy_postgresql() {
    print_info "Deploying PostgreSQL (2 replicas)..."
    kubectl apply -f "$INFRA_DIR/postgresql/k8s/postgresql.yaml"
    print_success "PostgreSQL deployment submitted"

    # Wait for PostgreSQL to be ready
    print_info "Waiting for PostgreSQL StatefulSet to be ready..."
    kubectl rollout status statefulset/postgresql -n infrastructure --timeout=300s
    print_success "PostgreSQL is ready"
}

deploy_zookeeper() {
    print_info "Deploying Zookeeper (3 replicas)..."
    kubectl apply -f "$INFRA_DIR/kafka/k8s/kafka.yaml"
    print_success "Zookeeper deployment submitted"

    # Wait for Zookeeper to be ready
    print_info "Waiting for Zookeeper StatefulSet to be ready..."
    kubectl rollout status statefulset/zookeeper -n infrastructure --timeout=300s
    print_success "Zookeeper is ready"
}

deploy_kafka() {
    print_info "Deploying Kafka (3 replicas)..."
    # Kafka is in the same file as Zookeeper, already applied

    # Wait for Kafka to be ready
    print_info "Waiting for Kafka StatefulSet to be ready..."
    kubectl rollout status statefulset/kafka -n infrastructure --timeout=300s
    print_success "Kafka is ready"

    # Create topics if Strimzi operator is available
    if kubectl get crd kafkatopics.kafka.strimzi.io &> /dev/null; then
        print_info "Creating Kafka topics..."
        kubectl apply -f "$INFRA_DIR/kafka/k8s/kafka.yaml" | grep KafkaTopic || print_warning "Kafka topics already exist or Strimzi operator not ready"
        print_success "Kafka topics created"
    else
        print_warning "Strimzi operator not found. Topics will be auto-created by Kafka."
    fi
}

deploy_redis() {
    print_info "Deploying Redis (3 replicas + Sentinel)..."
    kubectl apply -f "$INFRA_DIR/redis/k8s/redis.yaml"
    print_success "Redis deployment submitted"

    # Wait for Redis to be ready
    print_info "Waiting for Redis StatefulSet to be ready..."
    kubectl rollout status statefulset/redis -n infrastructure --timeout=300s
    print_success "Redis is ready"

    # Wait for Redis Sentinel to be ready
    print_info "Waiting for Redis Sentinel StatefulSet to be ready..."
    kubectl rollout status statefulset/redis-sentinel -n infrastructure --timeout=300s
    print_success "Redis Sentinel is ready"
}

show_status() {
    echo ""
    print_info "=== Infrastructure Deployment Status ==="
    echo ""
    kubectl get all -n infrastructure
    echo ""
    print_info "=== Persistent Volume Claims ==="
    echo ""
    kubectl get pvc -n infrastructure
    echo ""
    print_info "=== Services ==="
    echo ""
    kubectl get svc -n infrastructure
    echo ""
}

# Main deployment flow
main() {
    print_info "=== Foundation Domain Infrastructure Deployment ==="
    echo ""

    # Pre-flight checks
    check_kubectl
    check_cluster
    echo ""

    # Step 1: Namespace and policies
    deploy_namespace
    deploy_network_policies
    deploy_resource_quotas
    echo ""

    # Step 2: Deploy databases and messaging
    print_warning "Starting deployment. This may take several minutes..."
    echo ""

    deploy_mongodb
    deploy_postgresql
    deploy_zookeeper
    deploy_kafka
    deploy_redis

    echo ""
    print_success "=== All infrastructure components deployed successfully! ==="

    # Show final status
    show_status

    echo ""
    print_info "Next steps:"
    echo "  1. Update secrets with production passwords"
    echo "  2. Configure monitoring (Prometheus/Grafana)"
    echo "  3. Deploy application services"
    echo ""
    print_info "Connection strings:"
    echo "  MongoDB: mongodb://<user>:<password>@mongodb.infrastructure.svc.cluster.local:27017/<database>?replicaSet=rs0"
    echo "  PostgreSQL: postgresql://<user>:<password>@postgresql.infrastructure.svc.cluster.local:5432/<database>"
    echo "  Kafka: kafka-bootstrap.infrastructure.svc.cluster.local:9092"
    echo "  Redis: redis-sentinel-cluster.infrastructure.svc.cluster.local:26379 (master: mymaster)"
    echo ""
}

# Run main function
main "$@"
