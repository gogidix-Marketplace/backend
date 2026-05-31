#!/bin/bash
# Script to populate empty test resource directories with application-test.yml

# Find all empty test/resources directories and populate them
find ./Backend/Java -type d -path "*/src/test/resources" -empty | while read dir; do
    # Extract service name from path
    service_name=$(echo "$dir" | sed -n 's|.*/\([^/]*/src/test/resources\)|\1|p')

    # Create application-test.yml
    cat > "$dir/application-test.yml" << EOF
# Test application configuration
spring:
  application:
    name: service-test
  data:
    mongodb:
      uri: mongodb://localhost:27017/test_db
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: test-group
      auto-offset-reset: earliest

logging:
  level:
    com.gogidix.shared.warehousing: DEBUG
    org.springframework.data.mongodb: DEBUG
EOF
    echo "Populated: $dir"
done

echo "Test resources populated successfully"
