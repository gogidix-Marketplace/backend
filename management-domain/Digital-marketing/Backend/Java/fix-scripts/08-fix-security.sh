#!/bin/bash
# Script 08: Replace SecurityConfig with simplified version
SERVICE_DIR="$1"
BLUEPRINT="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Digital-marketing/Backend/Java/analytics-service"

# Find and remove existing SecurityConfig files
find "$SERVICE_DIR/src" -name "SecurityConfig.java" -delete

echo "Removed existing SecurityConfig from: $SERVICE_DIR"
