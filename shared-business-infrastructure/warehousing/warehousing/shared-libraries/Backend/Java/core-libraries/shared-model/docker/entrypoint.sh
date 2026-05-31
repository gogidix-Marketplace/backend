#!/bin/bash
# =============================================================================
# GOGIDIX SHARED MODEL LIBRARY SERVICE - DOCKER ENTRYPOINT
# =============================================================================
# Service: shared-model-service
# Version: 1.0.0
# Description: Entry point script for the Shared Model Library Service container
# =============================================================================

set -e

# Print banner
echo "============================================================================="
echo "🚀 GOGIDIX SHARED MODEL LIBRARY SERVICE"
echo "============================================================================="
echo "Service: shared-model-service"
echo "Port: ${SERVER_PORT:-8704}"
echo "Profile: ${SPRING_PROFILES_ACTIVE:-production}"
echo "Java Version: $(java -version 2>&1 | head -n 1)"
echo "Memory Limit: $(cat /sys/fs/cgroup/memory.max 2>/dev/null || echo 'unlimited')"
echo "============================================================================="

# Environment validation
echo "🔍 Validating environment..."
if [ -z "$SPRING_PROFILES_ACTIVE" ]; then
    export SPRING_PROFILES_ACTIVE="production"
    echo "✅ Set default profile: production"
fi

if [ -z "$SERVER_PORT" ]; then
    export SERVER_PORT="8704"
    echo "✅ Set default port: 8704"
fi

# JVM memory configuration based on container memory
MEMORY_LIMIT=$(cat /sys/fs/cgroup/memory.max 2>/dev/null || echo "2147483648")
if [ "$MEMORY_LIMIT" != "max" ] && [ "$MEMORY_LIMIT" -gt 0 ]; then
    MEMORY_MB=$((MEMORY_LIMIT / 1024 / 1024))
    HEAP_SIZE=$((MEMORY_MB * 75 / 100))
    
    export JAVA_OPTS="$JAVA_OPTS -Xmx${HEAP_SIZE}m"
    echo "✅ Container memory: ${MEMORY_MB}MB, Heap: ${HEAP_SIZE}MB"
else
    echo "✅ Using default JVM memory settings"
fi

# Wait for dependencies (if specified)
if [ -n "$WAIT_FOR_SERVICES" ]; then
    echo "⏳ Waiting for dependent services..."
    for service in $(echo $WAIT_FOR_SERVICES | tr "," "\n"); do
        host=$(echo $service | cut -d: -f1)
        port=$(echo $service | cut -d: -f2)
        
        echo "   Waiting for $host:$port..."
        while ! nc -z $host $port; do
            sleep 2
        done
        echo "   ✅ $host:$port is ready"
    done
fi

# Wait for Eureka (if enabled)
if [ "$EUREKA_CLIENT_ENABLED" != "false" ] && [ -n "$EUREKA_SERVER_URL" ]; then
    EUREKA_HOST=$(echo $EUREKA_SERVER_URL | sed 's|http://||' | sed 's|/eureka/||' | cut -d: -f1)
    EUREKA_PORT=$(echo $EUREKA_SERVER_URL | sed 's|http://||' | sed 's|/eureka/||' | cut -d: -f2)
    EUREKA_PORT=${EUREKA_PORT:-8761}
    
    echo "⏳ Waiting for Eureka server at $EUREKA_HOST:$EUREKA_PORT..."
    while ! nc -z $EUREKA_HOST $EUREKA_PORT; do
        sleep 5
    done
    echo "✅ Eureka server is ready"
fi

# Application health check function
health_check() {
    local url="http://localhost:${SERVER_PORT}/shared-model/actuator/health"
    local max_attempts=30
    local attempt=1
    
    echo "🔍 Performing application health check..."
    while [ $attempt -le $max_attempts ]; do
        if curl -sf "$url" > /dev/null 2>&1; then
            echo "✅ Application is healthy"
            return 0
        fi
        
        echo "   Attempt $attempt/$max_attempts failed, retrying in 10s..."
        sleep 10
        attempt=$((attempt + 1))
    done
    
    echo "❌ Application health check failed after $max_attempts attempts"
    return 1
}

# Graceful shutdown handler
shutdown_handler() {
    echo "🛑 Received shutdown signal, gracefully shutting down..."
    
    # Send SIGTERM to Java process
    if [ -n "$JAVA_PID" ]; then
        kill -TERM "$JAVA_PID"
        
        # Wait for graceful shutdown (up to 30 seconds)
        local count=0
        while [ $count -lt 30 ] && kill -0 "$JAVA_PID" 2>/dev/null; do
            sleep 1
            count=$((count + 1))
        done
        
        # Force kill if still running
        if kill -0 "$JAVA_PID" 2>/dev/null; then
            echo "⚠️ Forcing application shutdown..."
            kill -KILL "$JAVA_PID"
        fi
    fi
    
    echo "✅ Application shutdown complete"
    exit 0
}

# Set up signal handlers
trap shutdown_handler SIGTERM SIGINT

# Create log directory
mkdir -p /app/logs

# Set proper permissions
chown -R appuser:appgroup /app/logs

# Log configuration
echo "📝 Log configuration:"
echo "   Log file: /app/logs/shared-model-service.log"
echo "   Log level: ${LOGGING_LEVEL_ROOT:-INFO}"

# Print final configuration
echo "🔧 Final configuration:"
echo "   JAVA_OPTS: $JAVA_OPTS"
echo "   SPRING_PROFILES_ACTIVE: $SPRING_PROFILES_ACTIVE"
echo "   SERVER_PORT: $SERVER_PORT"
echo "   Working Directory: $(pwd)"
echo "   Java Classpath: .:lib/*"

echo "============================================================================="
echo "🚀 Starting Shared Model Library Service..."
echo "============================================================================="

# Start the application in background
java $JAVA_OPTS -cp ".:lib/*" com.gogidix.libraries.sharedmodel.SharedModelApplication &
JAVA_PID=$!

# Wait for application to start
sleep 10

# Perform health check
if ! health_check; then
    echo "❌ Application failed to start properly"
    exit 1
fi

echo "✅ Shared Model Library Service started successfully"
echo "📊 Service URL: http://localhost:${SERVER_PORT}/shared-model"
echo "🔍 Health Check: http://localhost:${SERVER_PORT}/shared-model/actuator/health"
echo "📚 API Docs: http://localhost:${SERVER_PORT}/shared-model/swagger-ui.html"

# Wait for Java process
wait $JAVA_PID