#!/bin/bash
# Docker Build Script for Gogidix Shared Exceptions Service
# Builds optimized Docker images with BuildKit and caching
# Author: Gogidix Development Team
# Version: 1.0.0
# Date: 2025-08-13

set -euo pipefail

# =================================================================
# Configuration
# =================================================================
SERVICE_NAME="shared-exceptions"
SERVICE_VERSION="1.0.0"
DOCKER_REGISTRY="gogidix"
IMAGE_NAME="${DOCKER_REGISTRY}/${SERVICE_NAME}"
BUILD_CONTEXT="."
DOCKERFILE="Dockerfile"

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

check_docker() {
    if ! command -v docker &> /dev/null; then
        log_error "Docker is not installed or not in PATH"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        log_error "Docker daemon is not running"
        exit 1
    fi
    
    log_info "Docker is available and running"
}

enable_buildkit() {
    export DOCKER_BUILDKIT=1
    export BUILDKIT_PROGRESS=plain
    log_info "BuildKit enabled for optimized builds"
}

build_image() {
    local tag="$1"
    local target="${2:-runtime}"
    
    log_info "Building Docker image: ${tag} (target: ${target})"
    
    docker build \
        --target "${target}" \
        --tag "${tag}" \
        --build-arg BUILDKIT_INLINE_CACHE=1 \
        --build-arg SERVICE_VERSION="${SERVICE_VERSION}" \
        --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
        --build-arg GIT_COMMIT="$(git rev-parse --short HEAD 2>/dev/null || echo 'unknown')" \
        --cache-from "${IMAGE_NAME}:cache" \
        --cache-from "${IMAGE_NAME}:latest" \
        --file "${DOCKERFILE}" \
        "${BUILD_CONTEXT}"
    
    if [ $? -eq 0 ]; then
        log_success "Successfully built: ${tag}"
    else
        log_error "Failed to build: ${tag}"
        exit 1
    fi
}

test_image() {
    local tag="$1"
    
    log_info "Testing Docker image: ${tag}"
    
    # Test that the image starts and passes health check
    local container_id
    container_id=$(docker run -d \
        --name "${SERVICE_NAME}-test" \
        --publish 18702:8702 \
        --env SPRING_PROFILES_ACTIVE=test \
        --env SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb \
        --env EUREKA_CLIENT_ENABLED=false \
        --env MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=health,info \
        "${tag}")
    
    if [ $? -ne 0 ]; then
        log_error "Failed to start test container"
        exit 1
    fi
    
    # Wait for application to start
    log_info "Waiting for application to start..."
    sleep 30
    
    # Check health endpoint
    local health_check_attempts=0
    local max_attempts=12
    
    while [ $health_check_attempts -lt $max_attempts ]; do
        if curl -f http://localhost:18702/api/v1/actuator/health &> /dev/null; then
            log_success "Health check passed"
            break
        else
            health_check_attempts=$((health_check_attempts + 1))
            log_info "Health check attempt ${health_check_attempts}/${max_attempts}..."
            sleep 5
        fi
    done
    
    # Cleanup test container
    docker stop "${container_id}" &> /dev/null
    docker rm "${container_id}" &> /dev/null
    
    if [ $health_check_attempts -ge $max_attempts ]; then
        log_error "Health check failed after ${max_attempts} attempts"
        exit 1
    fi
    
    log_success "Image test completed successfully"
}

push_image() {
    local tag="$1"
    
    if [ "${DOCKER_PUSH:-false}" = "true" ]; then
        log_info "Pushing Docker image: ${tag}"
        docker push "${tag}"
        
        if [ $? -eq 0 ]; then
            log_success "Successfully pushed: ${tag}"
        else
            log_error "Failed to push: ${tag}"
            exit 1
        fi
    else
        log_info "Skipping image push (set DOCKER_PUSH=true to enable)"
    fi
}

cleanup() {
    log_info "Cleaning up build artifacts..."
    
    # Remove test containers if they exist
    docker rm -f "${SERVICE_NAME}-test" &> /dev/null || true
    
    # Prune build cache if requested
    if [ "${DOCKER_PRUNE:-false}" = "true" ]; then
        docker system prune -f --filter "label=stage=builder"
        log_info "Build cache pruned"
    fi
}

show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -t, --tag TAG         Custom tag for the image (default: ${IMAGE_NAME}:${SERVICE_VERSION})"
    echo "  -p, --push           Push image to registry after build"
    echo "  --no-test            Skip image testing"
    echo "  --no-cache           Build without Docker cache"
    echo "  --prune              Clean up build cache after build"
    echo "  -h, --help           Show this help message"
    echo ""
    echo "Environment Variables:"
    echo "  DOCKER_PUSH=true     Enable pushing to registry"
    echo "  DOCKER_PRUNE=true    Enable build cache cleanup"
    echo "  DOCKER_NO_CACHE=true Disable build cache"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Build with defaults"
    echo "  $0 --tag ${IMAGE_NAME}:latest --push # Build and push with custom tag"
    echo "  $0 --no-test --prune                 # Build without testing and clean cache"
}

# =================================================================
# Main Script
# =================================================================
main() {
    local custom_tag=""
    local skip_test=false
    local no_cache_flag=""
    
    # Parse command line arguments
    while [[ $# -gt 0 ]]; do
        case $1 in
            -t|--tag)
                custom_tag="$2"
                shift 2
                ;;
            -p|--push)
                export DOCKER_PUSH=true
                shift
                ;;
            --no-test)
                skip_test=true
                shift
                ;;
            --no-cache)
                no_cache_flag="--no-cache"
                shift
                ;;
            --prune)
                export DOCKER_PRUNE=true
                shift
                ;;
            -h|--help)
                show_usage
                exit 0
                ;;
            *)
                log_error "Unknown option: $1"
                show_usage
                exit 1
                ;;
        esac
    done
    
    # Set default tag if not provided
    local image_tag="${custom_tag:-${IMAGE_NAME}:${SERVICE_VERSION}}"
    
    # Set trap for cleanup
    trap cleanup EXIT
    
    log_info "Starting Docker build for Gogidix Shared Exceptions Service"
    log_info "Service: ${SERVICE_NAME}"
    log_info "Version: ${SERVICE_VERSION}"
    log_info "Image Tag: ${image_tag}"
    
    # Pre-flight checks
    check_docker
    enable_buildkit
    
    # Build the image
    build_image "${image_tag}"
    
    # Test the image
    if [ "$skip_test" = false ]; then
        test_image "${image_tag}"
    else
        log_info "Skipping image testing"
    fi
    
    # Tag as latest if building the default version
    if [ "${custom_tag:-}" = "" ]; then
        docker tag "${image_tag}" "${IMAGE_NAME}:latest"
        log_info "Tagged as latest: ${IMAGE_NAME}:latest"
    fi
    
    # Push to registry if requested
    push_image "${image_tag}"
    if [ "${custom_tag:-}" = "" ]; then
        push_image "${IMAGE_NAME}:latest"
    fi
    
    # Show image information
    log_success "Build completed successfully!"
    log_info "Image information:"
    docker images "${IMAGE_NAME}" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    
    log_info "To run the container locally:"
    echo "docker run -d --name ${SERVICE_NAME} -p 8702:8702 ${image_tag}"
    
    log_info "To run with docker-compose:"
    echo "docker-compose up -d"
}

# Run main function
main "$@"