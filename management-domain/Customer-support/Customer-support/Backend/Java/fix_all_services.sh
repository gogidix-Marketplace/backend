#!/bin/bash
# Fix script for all Customer-support services

SERVICES="country-support-dashboard-service feedback-service global-support-dashboard-service knowledge-base-service live-chat-service notification-service quality-management-service sla-management-service support-analytics-service ticket-management-service"

for service in $SERVICES; do
    echo "Processing $service..."
    cd "$service" || continue

    # Create test properties to disable MongoDB
    mkdir -p src/test/resources
    cat > src/test/resources/application-test.properties << 'EOF'
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
spring.security.enabled=false
EOF

    cd ..
done

echo "Done creating test properties files"
