#!/bin/bash
# Script 09: Create Application class if missing
SERVICE_DIR="$1"
SERVICE_NAME=$(basename "$SERVICE_DIR")

# Convert kebab-case to PascalCase
APP_NAME=$(echo "$SERVICE_NAME" | sed 's/-service//g' | sed 's/\b\(.\)/\u\1/g')
APP_CLASS="${APP_NAME}Application"

# Check if Application class exists
if ! find "$SERVICE_DIR/src" -name "*Application.java" -type f | grep -q .; then
    # Create Application class
    mkdir -p "$SERVICE_DIR/src/main/java/com/gogidix/digitalmarketing/${SERVICE_NAME%-service}/"

    cat > "$SERVICE_DIR/src/main/java/com/gogidix/digitalmarketing/${SERVICE_NAME%-service}/${APP_CLASS}.java" << EOF
package com.gogidix.digitalmarketing.${SERVICE_NAME%-service};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ${APP_NAME} Application
 */
@SpringBootApplication(scanBasePackages = {
    "com.gogidix.digitalmarketing.${SERVICE_NAME%-service}",
    "com.gogidix.digitalmarketing.shared"
})
@EnableMongoAuditing
@EnableCaching
@EnableScheduling
public class ${APP_CLASS} {

    public static void main(String[] args) {
        SpringApplication.run(${APP_CLASS}.class, args);
    }
}
EOF
    echo "Created Application class: ${APP_CLASS}.java"
else
    echo "Application class exists in: $SERVICE_DIR"
fi
