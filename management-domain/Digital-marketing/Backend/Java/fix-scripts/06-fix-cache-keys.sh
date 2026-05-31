#!/bin/bash
# Script 6: Fix cache key SpEL expressions
SERVICE_DIR="$1"

# Fix wrong cache key syntax: "param" -> #param
find "$SERVICE_DIR/src" -name "*.java" -exec sed -i 's/key = "\(.*\)"/key = "#\1"/g' {} \;

echo "Fixed cache keys in: $SERVICE_DIR"
