#!/bin/bash
# Script 5: Remove duplicate analytics packages
SERVICE_DIR="$1"

# Remove duplicate analytics package that causes conflicts
rm -rf "$SERVICE_DIR/src/main/java/com/gogidix/digitalmarketing/analytics"

# Remove duplicate marketing package if exists
rm -rf "$SERVICE_DIR/src/main/java/com/gogidix/marketing"

echo "Removed duplicate packages in: $SERVICE_DIR"
