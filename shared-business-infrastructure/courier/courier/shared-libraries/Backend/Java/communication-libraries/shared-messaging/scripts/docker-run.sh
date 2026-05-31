#!/bin/bash
# Docker run script for Shared Messaging Library
# Starts the complete development environment

set -e

# Configuration
COMPOSE_FILE="docker-compose.yml"
PROJECT_NAME="shared-messaging"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
cleanup() {
    echo -e "${YELLOW}Cleaning up...${NC}"
    docker-compose -p "${PROJECT_NAME}" down
}

show_services() {
    echo -e "${BLUE}=== Service Access URLs ===${NC}"
    echo -e "${GREEN}Application Services:${NC}"
    echo "  📨 Shared Messaging API: http://localhost:8501/api/messaging"
    echo "  🔍 API Documentation: http://localhost:8501/api/messaging/swagger-ui.html"
    echo "  ❤️  Health Check: http://localhost:8501/api/messaging/actuator/health"
    echo ""
    echo -e "${GREEN}Development Tools:${NC}"
    echo "  🐘 PostgreSQL (PgAdmin): http://localhost:5050 (admin@gogidix.com / admin123)"
    echo "  🔴 Redis Commander: http://localhost:8081"
    echo "  📊 Kafka UI: http://localhost:8080"
    echo "  📧 MailHog UI: http://localhost:8025"
    echo ""
    echo -e "${GREEN}Direct Connections:${NC}"
    echo "  🐘 PostgreSQL: localhost:5432 (postgres/postgres)"
    echo "  🔴 Redis: localhost:6379 (password: redis123)"
    echo "  📨 Kafka: localhost:9092"
    echo "  📧 SMTP: localhost:1025"
}

wait_for_health() {
    echo -e "${YELLOW}Waiting for application to be healthy...${NC}"
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f -s http://localhost:8501/api/messaging/actuator/health >/dev/null 2>&1; then
            echo -e "${GREEN}✅ Application is healthy!${NC}"
            return 0
        fi
        
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}❌ Application failed to become healthy${NC}"
    return 1
}

# Main execution
echo -e "${BLUE}=== Shared Messaging Library Development Environment ===${NC}"

# Check if docker-compose.yml exists
if [ ! -f "${COMPOSE_FILE}" ]; then
    echo -e "${RED}Error: ${COMPOSE_FILE} not found${NC}"
    exit 1
fi

# Set up cleanup trap
trap cleanup EXIT

# Parse command line arguments
case "${1:-up}" in
    "up"|"start")
        echo -e "${YELLOW}Starting development environment...${NC}"
        
        # Pull latest images
        echo -e "${YELLOW}Pulling latest images...${NC}"
        docker-compose -p "${PROJECT_NAME}" pull
        
        # Start services
        echo -e "${YELLOW}Starting services...${NC}"
        docker-compose -p "${PROJECT_NAME}" up -d
        
        # Wait for health
        if wait_for_health; then
            show_services
            echo -e "${GREEN}=== Environment is ready! ===${NC}"
            echo -e "${YELLOW}Use 'docker-compose -p ${PROJECT_NAME} logs -f' to view logs${NC}"
            echo -e "${YELLOW}Use 'Ctrl+C' to stop all services${NC}"
            
            # Follow logs
            docker-compose -p "${PROJECT_NAME}" logs -f
        else
            echo -e "${RED}Environment failed to start properly${NC}"
            docker-compose -p "${PROJECT_NAME}" logs shared-messaging
            exit 1
        fi
        ;;
        
    "down"|"stop")
        echo -e "${YELLOW}Stopping development environment...${NC}"
        docker-compose -p "${PROJECT_NAME}" down
        echo -e "${GREEN}✅ Environment stopped${NC}"
        ;;
        
    "restart")
        echo -e "${YELLOW}Restarting development environment...${NC}"
        docker-compose -p "${PROJECT_NAME}" restart
        if wait_for_health; then
            show_services
            echo -e "${GREEN}✅ Environment restarted${NC}"
        fi
        ;;
        
    "logs")
        service="${2:-shared-messaging}"
        echo -e "${YELLOW}Showing logs for ${service}...${NC}"
        docker-compose -p "${PROJECT_NAME}" logs -f "${service}"
        ;;
        
    "status")
        echo -e "${BLUE}=== Service Status ===${NC}"
        docker-compose -p "${PROJECT_NAME}" ps
        echo ""
        show_services
        ;;
        
    "clean")
        echo -e "${YELLOW}Cleaning up environment...${NC}"
        docker-compose -p "${PROJECT_NAME}" down -v --remove-orphans
        docker system prune -f
        echo -e "${GREEN}✅ Environment cleaned${NC}"
        ;;
        
    "build")
        echo -e "${YELLOW}Building application image...${NC}"
        docker-compose -p "${PROJECT_NAME}" build shared-messaging
        echo -e "${GREEN}✅ Image built${NC}"
        ;;
        
    "test")
        echo -e "${YELLOW}Running application tests...${NC}"
        docker-compose -p "${PROJECT_NAME}" run --rm shared-messaging ./mvnw test
        ;;
        
    "shell")
        service="${2:-shared-messaging}"
        echo -e "${YELLOW}Opening shell in ${service}...${NC}"
        docker-compose -p "${PROJECT_NAME}" exec "${service}" /bin/bash
        ;;
        
    *)
        echo -e "${BLUE}Usage: $0 [command]${NC}"
        echo ""
        echo -e "${GREEN}Commands:${NC}"
        echo "  up, start    - Start the development environment"
        echo "  down, stop   - Stop the development environment"
        echo "  restart      - Restart the development environment"
        echo "  logs [svc]   - Show logs (default: shared-messaging)"
        echo "  status       - Show service status and URLs"
        echo "  clean        - Clean up all containers and volumes"
        echo "  build        - Build application image"
        echo "  test         - Run application tests"
        echo "  shell [svc]  - Open shell in service (default: shared-messaging)"
        echo ""
        echo -e "${YELLOW}Examples:${NC}"
        echo "  $0 up           # Start environment"
        echo "  $0 logs kafka   # Show Kafka logs"
        echo "  $0 shell redis  # Open Redis shell"
        exit 1
        ;;
esac