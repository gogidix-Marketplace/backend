#!/bin/bash
# Docker Compose Development Script for Gogidix Shared Exceptions Service
# Manages the complete development environment with all dependencies
# Author: Gogidix Development Team
# Version: 1.0.0
# Date: 2025-08-13

set -euo pipefail

# =================================================================
# Configuration
# =================================================================
SERVICE_NAME="shared-exceptions"
COMPOSE_FILE="docker-compose.yml"
PROJECT_NAME="gogidix-shared-exceptions"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# =================================================================
# Functions
# =================================================================
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_dependencies() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed or not in PATH"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose is not installed or not in PATH"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        log_error "Docker daemon is not running"
        exit 1
    fi
    
    log_info "Docker and Docker Compose are available"
}

create_env_file() {
    if [ ! -f .env ]; then
        log_info "Creating .env file with default values"
        cat > .env << 'EOF'
# Gogidix Shared Exceptions Service - Environment Variables
# Development Environment Configuration

# Service Configuration
SPRING_PROFILES_ACTIVE=docker
SERVICE_VERSION=1.0.0
JAVA_OPTS=-XX:MaxRAMPercentage=70.0 -XX:+UseG1GC

# Database Configuration
POSTGRES_DB=gogidix_shared_exceptions
POSTGRES_USER=postgres
POSTGRES_PASSWORD=gogidix_postgres_2025

# Redis Configuration
REDIS_PASSWORD=

# Security Configuration
JWT_ISSUER_URI=http://keycloak:8080/auth/realms/gogidix
JWT_JWK_SET_URI=http://keycloak:8080/auth/realms/gogidix/protocol/openid-connect/certs

# Monitoring Configuration
OTLP_ENDPOINT=http://jaeger:4318/v1/traces
PROMETHEUS_RETENTION=15d
GRAFANA_ADMIN_PASSWORD=admin

# Development Configuration
DEBUG_MODE=false
LOG_LEVEL=INFO
EOF
        log_success ".env file created with default values"
    else
        log_info ".env file already exists"
    fi
}

start_services() {
    local services="$1"
    
    log_info "Starting services: $services"
    
    docker-compose \
        -f "${COMPOSE_FILE}" \
        -p "${PROJECT_NAME}" \
        up -d $services
    
    if [ $? -eq 0 ]; then
        log_success "Services started successfully"
    else
        log_error "Failed to start services"
        exit 1
    fi
}

stop_services() {
    log_info "Stopping all services"
    
    docker-compose \
        -f "${COMPOSE_FILE}" \
        -p "${PROJECT_NAME}" \
        down
    
    if [ $? -eq 0 ]; then
        log_success "Services stopped successfully"
    else
        log_error "Failed to stop services"
    fi
}

restart_service() {
    local service="$1"
    
    log_info "Restarting service: $service"
    
    docker-compose \
        -f "${COMPOSE_FILE}" \
        -p "${PROJECT_NAME}" \
        restart "$service"
    
    if [ $? -eq 0 ]; then
        log_success "Service $service restarted successfully"
    else
        log_error "Failed to restart service $service"
    fi
}

show_status() {
    log_info "Service status:"
    
    docker-compose \
        -f "${COMPOSE_FILE}" \
        -p "${PROJECT_NAME}" \
        ps
}

show_logs() {
    local service="${1:-}"
    local lines="${2:-100}"
    
    if [ -n "$service" ]; then
        log_info "Showing logs for service: $service (last $lines lines)"
        docker-compose \
            -f "${COMPOSE_FILE}" \
            -p "${PROJECT_NAME}" \
            logs --tail="$lines" -f "$service"
    else
        log_info "Showing logs for all services (last $lines lines)"
        docker-compose \
            -f "${COMPOSE_FILE}" \
            -p "${PROJECT_NAME}" \
            logs --tail="$lines" -f
    fi
}

build_service() {
    local service="${1:-shared-exceptions}"
    
    log_info "Building service: $service"
    
    docker-compose \
        -f "${COMPOSE_FILE}" \
        -p "${PROJECT_NAME}" \
        build --no-cache "$service"
    
    if [ $? -eq 0 ]; then
        log_success "Service $service built successfully"
    else
        log_error "Failed to build service $service"
        exit 1
    fi
}

health_check() {
    log_info "Performing health checks..."
    
    # Check shared-exceptions service
    if curl -f http://localhost:8702/api/v1/actuator/health &> /dev/null; then
        log_success "Shared Exceptions service: healthy"
    else
        log_warning "Shared Exceptions service: not healthy or not started"
    fi
    
    # Check PostgreSQL
    if docker-compose -p "${PROJECT_NAME}" exec -T postgres pg_isready -U postgres &> /dev/null; then
        log_success "PostgreSQL: healthy"
    else
        log_warning "PostgreSQL: not healthy or not started"
    fi
    
    # Check Redis
    if docker-compose -p "${PROJECT_NAME}" exec -T redis redis-cli ping &> /dev/null; then
        log_success "Redis: healthy"
    else
        log_warning "Redis: not healthy or not started"
    fi
    
    # Check Kafka
    local kafka_topics
    kafka_topics=$(docker-compose -p "${PROJECT_NAME}" exec -T kafka kafka-topics.sh --bootstrap-server localhost:9092 --list 2>/dev/null | wc -l)
    if [ "$kafka_topics" -ge 0 ]; then
        log_success "Kafka: healthy ($kafka_topics topics)"
    else
        log_warning "Kafka: not healthy or not started"
    fi
}

cleanup() {
    log_info "Cleaning up containers, networks, and volumes..."
    
    docker-compose \
        -f "${COMPOSE_FILE}" \
        -p "${PROJECT_NAME}" \
        down --volumes --remove-orphans
    
    # Remove unused images
    docker image prune -f
    
    log_success "Cleanup completed"
}

show_usage() {
    echo "Usage: $0 COMMAND [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  up [services]         Start services (default: all)"
    echo "  down                  Stop all services"
    echo "  restart [service]     Restart a specific service"
    echo "  build [service]       Build a specific service"
    echo "  status                Show service status"
    echo "  logs [service] [n]    Show logs (optionally for specific service, last n lines)"
    echo "  health                Check service health"
    echo "  cleanup               Stop services and cleanup volumes"
    echo "  shell <service>       Open shell in service container"
    echo ""
    echo "Examples:"
    echo "  $0 up                           # Start all services"
    echo "  $0 up shared-exceptions postgres # Start specific services"
    echo "  $0 logs shared-exceptions 50    # Show last 50 log lines"
    echo "  $0 restart shared-exceptions    # Restart main service"
    echo "  $0 shell postgres              # Open PostgreSQL shell"
    echo ""
    echo "Quick Development Setup:"
    echo "  $0 up postgres redis kafka    # Start dependencies only"
    echo "  $0 build shared-exceptions     # Build main service"
    echo "  $0 up shared-exceptions        # Start main service"
}

open_shell() {
    local service="$1"
    
    log_info "Opening shell in service: $service"
    
    case "$service" in
        postgres)
            docker-compose -p "${PROJECT_NAME}" exec "$service" psql -U postgres -d gogidix_shared_exceptions
            ;;
        redis)
            docker-compose -p "${PROJECT_NAME}" exec "$service" redis-cli
            ;;
        shared-exceptions)
            docker-compose -p "${PROJECT_NAME}" exec "$service" /bin/sh
            ;;
        *)
            docker-compose -p "${PROJECT_NAME}" exec "$service" /bin/sh
            ;;
    esac
}

# =================================================================
# Main Script
# =================================================================
main() {
    if [ $# -eq 0 ]; then
        show_usage
        exit 1
    fi
    
    local command="$1"
    shift
    
    # Pre-flight checks
    check_dependencies
    create_env_file
    
    case "$command" in
        up)
            local services="${*:-}"
            start_services "$services"
            ;;
        down)
            stop_services
            ;;
        restart)
            local service="${1:-shared-exceptions}"
            restart_service "$service"
            ;;
        build)
            local service="${1:-shared-exceptions}"
            build_service "$service"
            ;;
        status)
            show_status
            ;;
        logs)
            local service="${1:-}"
            local lines="${2:-100}"
            show_logs "$service" "$lines"
            ;;
        health)
            health_check
            ;;
        cleanup)
            cleanup
            ;;
        shell)
            if [ $# -eq 0 ]; then
                log_error "Service name required for shell command"
                exit 1
            fi
            open_shell "$1"
            ;;
        *)
            log_error "Unknown command: $command"
            show_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@"