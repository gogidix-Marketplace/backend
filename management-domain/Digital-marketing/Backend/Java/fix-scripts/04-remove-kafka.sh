#!/bin/bash
# Script 4: Remove Kafka/Retry annotations (missing dependencies)
SERVICE_DIR="$1"

# Remove EnableKafka and EnableRetry imports
find "$SERVICE_DIR/src" -name "*Application.java" -exec sed -i '/import.*kafka.*EnableKafka/d' {} \;
find "$SERVICE_DIR/src" -name "*Application.java" -exec sed -i '/import.*retry.*EnableRetry/d' {} \;

# Remove annotations
find "$SERVICE_DIR/src" -name "*Application.java" -exec sed -i 's/@EnableKafka//g' {} \;
find "$SERVICE_DIR/src" -name "*Application.java" -exec sed -i 's/@EnableRetry//g' {} \;

echo "Removed Kafka annotations in: $SERVICE_DIR"
