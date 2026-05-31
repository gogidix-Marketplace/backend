#!/bin/bash
# Docker build script for Shared Messaging Library
# Builds optimized production image

set -e

# Configuration
IMAGE_NAME="gogidix/shared-messaging"
IMAGE_TAG="${1:-latest}"
FULL_IMAGE_NAME="${IMAGE_NAME}:${IMAGE_TAG}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Shared Messaging Library Docker Build ===${NC}"
echo -e "${YELLOW}Building image: ${FULL_IMAGE_NAME}${NC}"

# Check if Dockerfile exists
if [ ! -f "Dockerfile" ]; then
    echo -e "${RED}Error: Dockerfile not found${NC}"
    exit 1
fi

# Check if entrypoint script exists
if [ ! -f "docker/entrypoint.sh" ]; then
    echo -e "${RED}Error: docker/entrypoint.sh not found${NC}"
    exit 1
fi

# Build the image
echo -e "${YELLOW}Building Docker image...${NC}"
docker build \
    --target runtime \
    --tag "${FULL_IMAGE_NAME}" \
    --build-arg BUILD_DATE="$(date -u +'%Y-%m-%dT%H:%M:%SZ')" \
    --build-arg VCS_REF="$(git rev-parse --short HEAD 2>/dev/null || echo 'unknown')" \
    --build-arg VERSION="${IMAGE_TAG}" \
    .

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Docker image built successfully: ${FULL_IMAGE_NAME}${NC}"
    
    # Display image information
    echo -e "${BLUE}Image information:${NC}"
    docker images "${IMAGE_NAME}" --format "table {{.Repository}}\t{{.Tag}}\t{{.ID}}\t{{.CreatedAt}}\t{{.Size}}"
    
    # Run basic image validation
    echo -e "${YELLOW}Running basic image validation...${NC}"
    
    # Check image layers
    echo -e "${BLUE}Image layers:${NC}"
    docker history "${FULL_IMAGE_NAME}" --no-trunc --format "table {{.CreatedBy}}\t{{.Size}}"
    
    # Test image can start (without dependencies)
    echo -e "${YELLOW}Testing image startup (quick test)...${NC}"
    CONTAINER_ID=$(docker run -d \
        -e SPRING_PROFILES_ACTIVE=test \
        -e DB_HOST=localhost \
        -e REDIS_HOST=localhost \
        -e KAFKA_HOST=localhost \
        "${FULL_IMAGE_NAME}")
    
    # Wait a bit and check if container is still running
    sleep 5
    if docker ps -q --no-trunc | grep -q "${CONTAINER_ID}"; then
        echo -e "${GREEN}✅ Container started successfully${NC}"
        docker logs "${CONTAINER_ID}" | tail -5
    else
        echo -e "${RED}❌ Container failed to start${NC}"
        docker logs "${CONTAINER_ID}"
    fi
    
    # Cleanup test container
    docker stop "${CONTAINER_ID}" >/dev/null 2>&1 || true
    docker rm "${CONTAINER_ID}" >/dev/null 2>&1 || true
    
    echo -e "${GREEN}=== Build completed successfully ===${NC}"
    echo -e "${YELLOW}To run with dependencies: docker-compose up${NC}"
    echo -e "${YELLOW}To run standalone: docker run -p 8501:8501 ${FULL_IMAGE_NAME}${NC}"
    
else
    echo -e "${RED}❌ Docker build failed${NC}"
    exit 1
fi