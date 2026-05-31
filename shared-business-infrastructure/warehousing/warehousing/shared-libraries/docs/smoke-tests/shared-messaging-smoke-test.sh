#!/bin/bash
# Smoke test script for Shared Messaging Library
# Validates critical functionality after deployment

set -e

# Configuration
BASE_URL="${1:-http://localhost:8501}"
API_BASE="${BASE_URL}/api/messaging"
TIMEOUT=30
MAX_RETRIES=3

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test results tracking
TESTS_TOTAL=0
TESTS_PASSED=0
TESTS_FAILED=0

# Helper functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
    ((TESTS_PASSED++))
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
    ((TESTS_FAILED++))
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Test functions
test_health_endpoint() {
    log_info "Testing health endpoint..."
    ((TESTS_TOTAL++))
    
    local response
    if response=$(curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/actuator/health" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            log_success "Health endpoint is UP"
            return 0
        else
            log_error "Health endpoint status: $status"
            return 1
        fi
    else
        log_error "Health endpoint is not accessible"
        return 1
    fi
}

test_info_endpoint() {
    log_info "Testing info endpoint..."
    ((TESTS_TOTAL++))
    
    if curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/actuator/info" >/dev/null 2>&1; then
        log_success "Info endpoint is accessible"
        return 0
    else
        log_error "Info endpoint is not accessible"
        return 1
    fi
}

test_metrics_endpoint() {
    log_info "Testing metrics endpoint..."
    ((TESTS_TOTAL++))
    
    if curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/actuator/prometheus" >/dev/null 2>&1; then
        log_success "Metrics endpoint is accessible"
        return 0
    else
        log_error "Metrics endpoint is not accessible"
        return 1
    fi
}

test_messages_api() {
    log_info "Testing messages API endpoint..."
    ((TESTS_TOTAL++))
    
    if curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/messages" >/dev/null 2>&1; then
        log_success "Messages API endpoint is accessible"
        return 0
    else
        log_error "Messages API endpoint is not accessible"
        return 1
    fi
}

test_swagger_documentation() {
    log_info "Testing API documentation..."
    ((TESTS_TOTAL++))
    
    if curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/swagger-ui.html" >/dev/null 2>&1; then
        log_success "API documentation is accessible"
        return 0
    else
        log_warning "API documentation is not accessible (non-critical)"
        return 0  # Not critical for smoke test
    fi
}

test_create_message() {
    log_info "Testing message creation..."
    ((TESTS_TOTAL++))
    
    local response
    local payload='{"content":"Smoke test message","messageType":"SYSTEM_NOTIFICATION","priority":"MEDIUM","recipients":["test@gogidix.com"]}'
    
    if response=$(curl -s -f --connect-timeout $TIMEOUT \
        -H "Content-Type: application/json" \
        -d "$payload" \
        -X POST "${API_BASE}/messages" 2>/dev/null); then
        
        local message_id=$(echo "$response" | grep -o '"id":"[^"]*"' | cut -d'"' -f4)
        if [ -n "$message_id" ]; then
            log_success "Message created successfully (ID: $message_id)"
            echo "$message_id" > /tmp/smoke_test_message_id
            return 0
        else
            log_error "Message creation failed - no ID returned"
            return 1
        fi
    else
        log_error "Message creation failed - API call failed"
        return 1
    fi
}

test_get_message() {
    log_info "Testing message retrieval..."
    ((TESTS_TOTAL++))
    
    if [ -f "/tmp/smoke_test_message_id" ]; then
        local message_id=$(cat /tmp/smoke_test_message_id)
        
        if curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/messages/${message_id}" >/dev/null 2>&1; then
            log_success "Message retrieved successfully"
            return 0
        else
            log_error "Message retrieval failed"
            return 1
        fi
    else
        log_warning "Skipping message retrieval test (no message ID available)"
        return 0
    fi
}

test_database_connectivity() {
    log_info "Testing database connectivity..."
    ((TESTS_TOTAL++))
    
    local response
    if response=$(curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/actuator/health/db" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            log_success "Database connectivity is UP"
            return 0
        else
            log_error "Database connectivity status: $status"
            return 1
        fi
    else
        log_error "Database health check failed"
        return 1
    fi
}

test_redis_connectivity() {
    log_info "Testing Redis connectivity..."
    ((TESTS_TOTAL++))
    
    local response
    if response=$(curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/actuator/health/redis" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            log_success "Redis connectivity is UP"
            return 0
        else
            log_error "Redis connectivity status: $status"
            return 1
        fi
    else
        log_error "Redis health check failed"
        return 1
    fi
}

test_kafka_connectivity() {
    log_info "Testing Kafka connectivity..."
    ((TESTS_TOTAL++))
    
    local response
    if response=$(curl -s -f --connect-timeout $TIMEOUT "${API_BASE}/actuator/health/kafka" 2>/dev/null); then
        local status=$(echo "$response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
        if [ "$status" = "UP" ]; then
            log_success "Kafka connectivity is UP"
            return 0
        else
            log_error "Kafka connectivity status: $status"
            return 1
        fi
    else
        log_error "Kafka health check failed"
        return 1
    fi
}

# Main execution
main() {
    echo -e "${BLUE}=== Shared Messaging Library Smoke Tests ===${NC}"
    echo "Target: $API_BASE"
    echo "$(date)"
    echo ""
    
    # Wait for service to be ready
    log_info "Waiting for service to be ready..."
    local ready=false
    for i in $(seq 1 10); do
        if curl -s -f --connect-timeout 5 "${API_BASE}/actuator/health" >/dev/null 2>&1; then
            ready=true
            break
        fi
        echo -n "."
        sleep 2
    done
    
    if [ "$ready" = false ]; then
        log_error "Service is not ready after 20 seconds"
        exit 1
    fi
    
    log_info "Service is ready, starting smoke tests..."
    echo ""
    
    # Run all smoke tests
    test_health_endpoint || true
    test_info_endpoint || true
    test_metrics_endpoint || true
    test_database_connectivity || true
    test_redis_connectivity || true
    test_kafka_connectivity || true
    test_messages_api || true
    test_swagger_documentation || true
    test_create_message || true
    test_get_message || true
    
    # Cleanup
    rm -f /tmp/smoke_test_message_id
    
    # Summary
    echo ""
    echo -e "${BLUE}=== Smoke Test Summary ===${NC}"
    echo "Total Tests: $TESTS_TOTAL"
    echo -e "Passed: ${GREEN}$TESTS_PASSED${NC}"
    echo -e "Failed: ${RED}$TESTS_FAILED${NC}"
    
    local success_rate=$((TESTS_PASSED * 100 / TESTS_TOTAL))
    echo "Success Rate: ${success_rate}%"
    
    if [ $TESTS_FAILED -eq 0 ]; then
        echo -e "${GREEN}🎉 All smoke tests passed! Service is healthy.${NC}"
        exit 0
    elif [ $success_rate -ge 80 ]; then
        echo -e "${YELLOW}⚠️  Some tests failed, but success rate is acceptable (${success_rate}%).${NC}"
        exit 0
    else
        echo -e "${RED}💥 Too many tests failed! Service may have issues.${NC}"
        exit 1
    fi
}

# Parse command line arguments
case "${1:-smoke}" in
    "smoke"|"")
        main
        ;;
    "health")
        test_health_endpoint
        ;;
    "api")
        test_messages_api && test_create_message && test_get_message
        ;;
    "dependencies")
        test_database_connectivity && test_redis_connectivity && test_kafka_connectivity
        ;;
    *)
        echo -e "${BLUE}Usage: $0 [test]${NC}"
        echo ""
        echo -e "${GREEN}Available tests:${NC}"
        echo "  smoke        - Run all smoke tests (default)"
        echo "  health       - Health endpoint only"
        echo "  api          - API functionality only"
        echo "  dependencies - Database, Redis, Kafka connectivity"
        echo ""
        echo -e "${YELLOW}Examples:${NC}"
        echo "  $0 smoke                                    # Full smoke test"
        echo "  $0 health                                   # Health check only"
        echo "  $0 api                                      # API functionality"
        echo "  $0 smoke https://api.gogidix.com           # Remote smoke test"
        exit 1
        ;;
esac