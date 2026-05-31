#!/bin/bash
# =============================================================================
# GOGIDIX SHARED MODEL LIBRARY SERVICE - HEALTH CHECK
# =============================================================================
# Service: shared-model-service
# Version: 1.0.0
# Description: Health check script for the Shared Model Library Service
# =============================================================================

set -e

# Configuration
SERVICE_PORT=${SERVER_PORT:-8704}
SERVICE_NAME="shared-model-service"
HEALTH_ENDPOINT="http://localhost:${SERVICE_PORT}/shared-model/actuator/health"
TIMEOUT=${HEALTH_CHECK_TIMEOUT:-10}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Logging function
log() {
    local level=$1
    shift
    local message="$@"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')
    
    case $level in
        "INFO")
            echo -e "${GREEN}[INFO]${NC} [$timestamp] $message"
            ;;
        "WARN")
            echo -e "${YELLOW}[WARN]${NC} [$timestamp] $message"
            ;;
        "ERROR")
            echo -e "${RED}[ERROR]${NC} [$timestamp] $message"
            ;;
        *)
            echo "[$timestamp] $message"
            ;;
    esac
}

# Check if port is listening
check_port() {
    local port=$1
    local host=${2:-localhost}
    
    if command -v nc >/dev/null 2>&1; then
        nc -z "$host" "$port" >/dev/null 2>&1
    elif command -v telnet >/dev/null 2>&1; then
        echo "" | telnet "$host" "$port" >/dev/null 2>&1
    else
        # Fallback using /proc/net/tcp
        local hex_port=$(printf "%04X" "$port")
        grep -q ":${hex_port} " /proc/net/tcp 2>/dev/null
    fi
}

# Check HTTP endpoint
check_http_endpoint() {
    local url=$1
    local expected_status=${2:-200}
    
    if command -v curl >/dev/null 2>&1; then
        local response=$(curl -s -o /dev/null -w "%{http_code}" --max-time "$TIMEOUT" "$url" 2>/dev/null)
        [ "$response" = "$expected_status" ]
    elif command -v wget >/dev/null 2>&1; then
        wget -q -O /dev/null --timeout="$TIMEOUT" "$url" >/dev/null 2>&1
    else
        log "ERROR" "Neither curl nor wget available for HTTP health check"
        return 1
    fi
}

# Get application status from health endpoint
get_application_status() {
    local url=$1
    
    if command -v curl >/dev/null 2>&1; then
        curl -s --max-time "$TIMEOUT" "$url" 2>/dev/null | \
        grep -o '"status":"[^"]*"' | \
        cut -d':' -f2 | \
        tr -d '"' 2>/dev/null || echo "UNKNOWN"
    else
        echo "UNKNOWN"
    fi
}

# Check memory usage
check_memory_usage() {
    local threshold=${MEMORY_THRESHOLD:-85}
    
    if [ -f /proc/meminfo ]; then
        local total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}')
        local avail_mem=$(grep MemAvailable /proc/meminfo | awk '{print $2}')
        
        if [ "$total_mem" -gt 0 ] && [ "$avail_mem" -gt 0 ]; then
            local usage_percent=$(( (total_mem - avail_mem) * 100 / total_mem ))
            
            if [ "$usage_percent" -gt "$threshold" ]; then
                log "WARN" "High memory usage: ${usage_percent}%"
                return 1
            else
                log "INFO" "Memory usage: ${usage_percent}%"
                return 0
            fi
        fi
    fi
    
    return 0
}

# Check disk usage
check_disk_usage() {
    local threshold=${DISK_THRESHOLD:-85}
    local mount_point=${1:-/app}
    
    if command -v df >/dev/null 2>&1; then
        local usage_percent=$(df "$mount_point" 2>/dev/null | tail -1 | awk '{print $5}' | tr -d '%')
        
        if [ -n "$usage_percent" ] && [ "$usage_percent" -gt "$threshold" ]; then
            log "WARN" "High disk usage on $mount_point: ${usage_percent}%"
            return 1
        else
            log "INFO" "Disk usage on $mount_point: ${usage_percent}%"
            return 0
        fi
    fi
    
    return 0
}

# Main health check function
main() {
    log "INFO" "Starting health check for $SERVICE_NAME"
    
    local exit_code=0
    local checks_passed=0
    local total_checks=0
    
    # Check 1: Port listening
    total_checks=$((total_checks + 1))
    log "INFO" "Checking if port $SERVICE_PORT is listening..."
    if check_port "$SERVICE_PORT"; then
        log "INFO" "✅ Port $SERVICE_PORT is listening"
        checks_passed=$((checks_passed + 1))
    else
        log "ERROR" "❌ Port $SERVICE_PORT is not listening"
        exit_code=1
    fi
    
    # Check 2: HTTP Health endpoint
    total_checks=$((total_checks + 1))
    log "INFO" "Checking health endpoint: $HEALTH_ENDPOINT"
    if check_http_endpoint "$HEALTH_ENDPOINT"; then
        log "INFO" "✅ Health endpoint is responding"
        checks_passed=$((checks_passed + 1))
        
        # Get detailed status
        local app_status=$(get_application_status "$HEALTH_ENDPOINT")
        log "INFO" "Application status: $app_status"
        
        if [ "$app_status" != "UP" ] && [ "$app_status" != "UNKNOWN" ]; then
            log "WARN" "Application status is not UP: $app_status"
            exit_code=1
        fi
    else
        log "ERROR" "❌ Health endpoint is not responding"
        exit_code=1
    fi
    
    # Check 3: Memory usage
    total_checks=$((total_checks + 1))
    log "INFO" "Checking memory usage..."
    if check_memory_usage; then
        log "INFO" "✅ Memory usage is within acceptable limits"
        checks_passed=$((checks_passed + 1))
    else
        log "WARN" "⚠️ Memory usage is high"
        # Not a critical failure, continue
        checks_passed=$((checks_passed + 1))
    fi
    
    # Check 4: Disk usage
    total_checks=$((total_checks + 1))
    log "INFO" "Checking disk usage..."
    if check_disk_usage "/app"; then
        log "INFO" "✅ Disk usage is within acceptable limits"
        checks_passed=$((checks_passed + 1))
    else
        log "WARN" "⚠️ Disk usage is high"
        # Not a critical failure, continue
        checks_passed=$((checks_passed + 1))
    fi
    
    # Summary
    log "INFO" "Health check completed: $checks_passed/$total_checks checks passed"
    
    if [ $exit_code -eq 0 ]; then
        log "INFO" "✅ $SERVICE_NAME is healthy"
    else
        log "ERROR" "❌ $SERVICE_NAME is unhealthy"
    fi
    
    return $exit_code
}

# Run health check
main "$@"