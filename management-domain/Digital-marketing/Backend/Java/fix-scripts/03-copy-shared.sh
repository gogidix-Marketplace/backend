#!/bin/bash
# Script 3: Copy shared infrastructure from analytics-service
SERVICE_DIR="$1"
BLUEPRINT="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Digital-marketing/Backend/Java/analytics-service"

# Remove old shared folders
rm -rf "$SERVICE_DIR/src/main/java/com/gogidix/marketing"
rm -rf "$SERVICE_DIR/src/main/java/com/gogidix/digitalmarketing/shared"

# Copy fresh shared folder
mkdir -p "$SERVICE_DIR/src/main/java/com/gogidix/digitalmarketing"
cp -r "$BLUEPRINT/src/main/java/com/gogidix/digitalmarketing/shared" "$SERVICE_DIR/src/main/java/com/gogidix/digitalmarketing/"

echo "Copied shared folder to: $SERVICE_DIR"
