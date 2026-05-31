#!/bin/bash
# Health check script for Shared Utilities Library
# Used in CI/CD pipeline for deployment validation

set -e

# Configuration
SERVICE_NAME="shared-utilities"
HEALTH_ENDPOINT="${HEALTH_ENDPOINT:-http://localhost:8707/api/utilities/actuator/health}"
MAX_ATTEMPTS=${MAX_ATTEMPTS:-30}
SLEEP_INTERVAL=${SLEEP_INTERVAL:-5}

echo "🧪 Starting health check for $SERVICE_NAME..."
echo "Health endpoint: $HEALTH_ENDPOINT"
echo "Max attempts: $MAX_ATTEMPTS"

# Function to check service health
check_health() {
    local attempt=$1
    echo "Attempt $attempt/$MAX_ATTEMPTS: Checking service health..."
    
    # Use curl to check health endpoint
    local response_code=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_ENDPOINT" || echo "000")
    
    if [ "$response_code" = "200" ]; then
        echo "✅ Service is healthy (HTTP $response_code)"
        return 0
    else
        echo "❌ Service unhealthy (HTTP $response_code)"
        return 1
    fi
}

# Function to get detailed health status
get_health_details() {
    echo "📋 Getting detailed health status..."
    local health_response=$(curl -s "$HEALTH_ENDPOINT" 2>/dev/null)
    
    if [ $? -eq 0 ]; then
        echo "Health response: $health_response"
        
        # Parse health status
        local status=$(echo "$health_response" | grep -o '"status":"[^"]*"' | cut -d'"' -f4 2>/dev/null)
        
        if [ "$status" = "UP" ]; then
            echo "✅ Overall status: UP"
            return 0
        else
            echo "❌ Overall status: $status"
            return 1
        fi
    else
        echo "❌ Failed to get health details"
        return 1
    fi
}

# Function to test utility endpoints
test_utility_endpoints() {
    echo "🔧 Testing utility endpoints..."
    
    # Test health endpoint
    echo "Testing health endpoint..."
    local health_code=$(curl -s -o /dev/null -w "%{http_code}" "$HEALTH_ENDPOINT" || echo "000")
    if [ "$health_code" = "200" ]; then
        echo "✅ Health endpoint: OK"
    else
        echo "❌ Health endpoint: FAIL (HTTP $health_code)"
        return 1
    fi
    
    # Test utility types endpoint
    echo "Testing utility types endpoint..."
    local types_endpoint="${HEALTH_ENDPOINT%/actuator/health}/types"
    local types_code=$(curl -s -o /dev/null -w "%{http_code}" "$types_endpoint" || echo "000")
    if [ "$types_code" = "200" ]; then
        echo "✅ Utility types endpoint: OK"
    else
        echo "⚠️ Utility types endpoint: WARN (HTTP $types_code) - may require authentication"
    fi
    
    return 0
}

# Main health check loop
for attempt in $(seq 1 $MAX_ATTEMPTS); do
    if check_health $attempt; then
        echo "🎉 Service health check passed!"
        
        # Get detailed health information
        get_health_details
        
        # Test additional endpoints
        test_utility_endpoints
        
        echo "✅ All health checks completed successfully"
        exit 0
    fi
    
    if [ $attempt -lt $MAX_ATTEMPTS ]; then
        echo "⏳ Waiting ${SLEEP_INTERVAL}s before next attempt..."
        sleep $SLEEP_INTERVAL
    fi
done

echo "❌ Health check failed after $MAX_ATTEMPTS attempts"
echo "💡 Please check:"
echo "   - Service logs: docker logs $SERVICE_NAME"
echo "   - Service status: docker ps"
echo "   - Network connectivity"
echo "   - Health endpoint: $HEALTH_ENDPOINT"

exit 1