#!/bin/bash
# Entrypoint script for Shared Messaging Library
# Handles environment setup and application startup

set -e

# Default values
DEFAULT_JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UseStringDeduplication"
DEFAULT_SPRING_PROFILES="docker"

# Use environment variables or defaults
JAVA_OPTS=${JAVA_OPTS:-$DEFAULT_JAVA_OPTS}
SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-$DEFAULT_SPRING_PROFILES}

# Wait for dependencies
wait_for_service() {
    local host=$1
    local port=$2
    local service_name=$3
    local timeout=${4:-30}
    
    echo "Waiting for $service_name at $host:$port..."
    
    for ((i=1; i<=timeout; i++)); do
        if nc -z "$host" "$port"; then
            echo "$service_name is ready!"
            return 0
        fi
        echo "Waiting for $service_name... ($i/$timeout)"
        sleep 1
    done
    
    echo "ERROR: $service_name at $host:$port is not available after $timeout seconds"
    return 1
}

# Wait for required services
if [ -n "$DB_HOST" ] && [ -n "$DB_PORT" ]; then
    wait_for_service "$DB_HOST" "$DB_PORT" "PostgreSQL Database"
fi

if [ -n "$REDIS_HOST" ] && [ -n "$REDIS_PORT" ]; then
    wait_for_service "$REDIS_HOST" "$REDIS_PORT" "Redis Cache"
fi

if [ -n "$KAFKA_HOST" ] && [ -n "$KAFKA_PORT" ]; then
    wait_for_service "$KAFKA_HOST" "$KAFKA_PORT" "Kafka Broker"
fi

# Health check endpoint wait
echo "Starting Shared Messaging Library..."
echo "Java Options: $JAVA_OPTS"
echo "Spring Profiles: $SPRING_PROFILES_ACTIVE"
echo "Application Port: 8501"

# Start application
exec java $JAVA_OPTS \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active="$SPRING_PROFILES_ACTIVE" \
    -jar app.jar \
    "$@"