#!/bin/bash
# Comprehensive health check script for Shared Messaging Library
# Validates all critical components and dependencies

set -e

# Configuration
BASE_URL="http://localhost:8501/api/messaging"
TIMEOUT=10
MAX_RETRIES=3

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Health check functions
check_basic_health() {
    echo -e "${BLUE}Checking basic application health...${NC}"
    
    local url="${BASE_URL}/actuator/health"
    local response
    
    for i in $(seq 1 $MAX_RETRIES); do
        if response=$(curl -s -f --connect-timeout $TIMEOUT "$url" 2>/dev/null); then
            local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
            if [ "$status" = "UP" ]; then
                echo -e "${GREEN}✅ Basic health check passed${NC}"
                return 0
            else
                echo -e "${YELLOW}⚠️  Health status: $status (attempt $i/$MAX_RETRIES)${NC}"
            fi
        else
            echo -e "${YELLOW}⚠️  Health endpoint not accessible (attempt $i/$MAX_RETRIES)${NC}"
        fi
        
        if [ $i -lt $MAX_RETRIES ]; then
            sleep 2
        fi
    done
    
    echo -e "${RED}❌ Basic health check failed${NC}"
    return 1
}

check_database_health() {
    echo -e "${BLUE}Checking database connectivity...${NC}"
    
    local url="${BASE_URL}/actuator/health/db"
    local response
    
    if response=$(curl -s -f --connect-timeout $TIMEOUT "$url" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            echo -e "${GREEN}✅ Database health check passed${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠️  Database status: $status${NC}"
        fi
    else
        echo -e "${RED}❌ Database health endpoint not accessible${NC}"
    fi
    
    return 1
}

check_redis_health() {
    echo -e "${BLUE}Checking Redis connectivity...${NC}"
    
    local url="${BASE_URL}/actuator/health/redis"
    local response
    
    if response=$(curl -s -f --connect-timeout $TIMEOUT "$url" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            echo -e "${GREEN}✅ Redis health check passed${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠️  Redis status: $status${NC}"
        fi
    else
        echo -e "${RED}❌ Redis health endpoint not accessible${NC}"
    fi
    
    return 1
}

check_kafka_health() {
    echo -e "${BLUE}Checking Kafka connectivity...${NC}"
    
    local url="${BASE_URL}/actuator/health/kafka"
    local response
    
    if response=$(curl -s -f --connect-timeout $TIMEOUT "$url" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            echo -e "${GREEN}✅ Kafka health check passed${NC}"
            return 0
        else
            echo -e "${YELLOW}⚠️  Kafka status: $status${NC}"
        fi
    else
        echo -e "${RED}❌ Kafka health endpoint not accessible${NC}"
    fi
    
    return 1
}

check_api_endpoints() {
    echo -e "${BLUE}Checking critical API endpoints...${NC}"
    
    # Check messages endpoint
    if curl -s -f --connect-timeout $TIMEOUT "${BASE_URL}/messages" >/dev/null 2>&1; then
        echo -e "${GREEN}✅ Messages API endpoint accessible${NC}"
    else
        echo -e "${RED}❌ Messages API endpoint not accessible${NC}"
        return 1
    fi
    
    # Check Swagger documentation
    if curl -s -f --connect-timeout $TIMEOUT "${BASE_URL}/swagger-ui.html" >/dev/null 2>&1; then
        echo -e "${GREEN}✅ API documentation accessible${NC}"
    else
        echo -e "${YELLOW}⚠️  API documentation not accessible${NC}"
    fi
    
    return 0
}

check_metrics() {
    echo -e "${BLUE}Checking metrics and monitoring...${NC}"
    
    # Check Prometheus metrics
    if curl -s -f --connect-timeout $TIMEOUT "${BASE_URL}/actuator/prometheus" >/dev/null 2>&1; then
        echo -e "${GREEN}✅ Prometheus metrics endpoint accessible${NC}"
    else
        echo -e "${YELLOW}⚠️  Prometheus metrics not accessible${NC}"
    fi
    
    # Check application info
    if curl -s -f --connect-timeout $TIMEOUT "${BASE_URL}/actuator/info" >/dev/null 2>&1; then
        echo -e "${GREEN}✅ Application info endpoint accessible${NC}"
    else
        echo -e "${YELLOW}⚠️  Application info not accessible${NC}"
    fi
    
    return 0
}

show_detailed_health() {
    echo -e "${BLUE}=== Detailed Health Information ===${NC}"
    
    # Show full health details
    local health_response
    if health_response=$(curl -s -f --connect-timeout $TIMEOUT "${BASE_URL}/actuator/health" 2>/dev/null); then
        echo -e "${GREEN}Full health status:${NC}"
        echo "$health_response" | jq '.' 2>/dev/null || echo "$health_response"
    fi
    
    echo ""
    
    # Show application info
    local info_response
    if info_response=$(curl -s -f --connect-timeout $TIMEOUT "${BASE_URL}/actuator/info" 2>/dev/null); then
        echo -e "${GREEN}Application information:${NC}"
        echo "$info_response" | jq '.' 2>/dev/null || echo "$info_response"
    fi
}

# Main health check execution
main() {
    echo -e "${BLUE}=== Shared Messaging Library Health Check ===${NC}"
    echo "$(date)"
    echo ""
    
    local exit_code=0
    
    # Run all health checks
    if ! check_basic_health; then
        exit_code=1
    fi
    
    if ! check_database_health; then
        exit_code=1
    fi
    
    if ! check_redis_health; then
        exit_code=1
    fi
    
    if ! check_kafka_health; then
        exit_code=1
    fi
    
    if ! check_api_endpoints; then
        exit_code=1
    fi
    
    check_metrics
    
    echo ""
    
    # Show detailed information if requested
    if [ "${1:-}" = "--detailed" ] || [ "${1:-}" = "-d" ]; then
        show_detailed_health
        echo ""
    fi
    
    # Final status
    if [ $exit_code -eq 0 ]; then
        echo -e "${GREEN}🎉 All health checks passed! Service is healthy.${NC}"
    else
        echo -e "${RED}💥 Some health checks failed! Service may have issues.${NC}"
    fi
    
    echo ""
    echo -e "${BLUE}Service URLs:${NC}"
    echo "  📨 API: ${BASE_URL}"
    echo "  🔍 Swagger: ${BASE_URL}/swagger-ui.html"
    echo "  ❤️  Health: ${BASE_URL}/actuator/health"
    echo "  📊 Metrics: ${BASE_URL}/actuator/prometheus"
    
    exit $exit_code
}

# Parse command line arguments
case "${1:-health}" in
    "health"|"")
        main
        ;;
    "detailed"|"-d"|"--detailed")
        main --detailed
        ;;
    "basic")
        check_basic_health
        ;;
    "database"|"db")
        check_database_health
        ;;
    "redis")
        check_redis_health
        ;;
    "kafka")
        check_kafka_health
        ;;
    "api")
        check_api_endpoints
        ;;
    "metrics")
        check_metrics
        ;;
    *)
        echo -e "${BLUE}Usage: $0 [check]${NC}"
        echo ""
        echo -e "${GREEN}Available checks:${NC}"
        echo "  health     - Run all health checks (default)"
        echo "  detailed   - Run all checks with detailed output"
        echo "  basic      - Basic application health only"
        echo "  database   - Database connectivity only"
        echo "  redis      - Redis connectivity only"
        echo "  kafka      - Kafka connectivity only"
        echo "  api        - API endpoints only"
        echo "  metrics    - Metrics endpoints only"
        exit 1
        ;;
esac