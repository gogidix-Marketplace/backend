#!/bin/bash
set -e

TEMPLATE="ceo-strategy-service"

# Function to recreate a service
recreate_service() {
    local PACKAGE=$1
    local SERVICE_NAME=$2
    local CLASS_NAME=$(echo $PACKAGE | sed 's/./\U&/')
    local COLLECTION=$3

    echo "=========================================="
    echo "Recreating: $SERVICE_NAME"
    echo "=========================================="

    # Backup existing
    if [ -d "$SERVICE_NAME" ]; then
        mv "$SERVICE_NAME" "${SERVICE_NAME}-backup-$(date +%s)"
        echo "  Backed up existing service"
    fi

    # Copy template
    cp -r "$TEMPLATE" "$SERVICE_NAME"
    echo "  Copied template"

    # Update Java files
    find "$SERVICE_NAME/src" -name "*.java" -type f | while read file; do
        # Replace package paths
        sed -i "s/\.strategy\./\.${PACKAGE}\./g" "$file"
        sed -i "s/executive\.strategy\./executive\.\${PACKAGE}\./g" "$file"
        sed -i "s/strategy\./\${PACKAGE}\./g" "$file"
        
        # Replace class names (Strategy -> Operations, etc.)
        sed -i "s/\bStrategy\b/${CLASS_NAME}/g" "$file"
        sed -i "s/\bSTRATEGY\b/$(echo $CLASS_NAME | tr 'a-z' 'A-Z')/g" "$file"
        
        # Replace KpiWidget references
        sed -i "s/KpiWidget/${CLASS_NAME}.Widget/g" "$file"
        
        # Replace dashboard/dashboards
        sed -i "s/\bdashboard\b/${PACKAGE}s/g" "$file"
        sed -i "s/\bdashboards\b/${PACKAGE}s/g" "$file"
        sed -i "s/\bDashboard\b/${CLASS_NAME}/g" "$file"
        
        # Replace collection name
        sed -i "s/collection = \"ceo-strategy-service\"/collection = \"${COLLECTION}\"/g" "$file"
    done
    echo "  Updated Java files"

    # Rename directory
    if [ -d "$SERVICE_NAME/src/main/java/com/gogidix/management/executive/strategy" ]; then
        mv "$SERVICE_NAME/src/main/java/com/gogidix/management/executive/strategy" \
           "$SERVICE_NAME/src/main/java/com/gogidix/management/executive/$PACKAGE"
        echo "  Renamed directory"
    fi

    # Update pom.xml
    sed -i "s/ceo-strategy-service/${SERVICE_NAME}/g" "$SERVICE_NAME/pom.xml"
    sed -i "s/CEO Strategy Service/${CLASS_NAME} Service/g" "$SERVICE_NAME/pom.xml"
    echo "  Updated pom.xml"

    # Remove wrong directories
    for dir in application domain infrastructure interfaces; do
        if [ -d "$SERVICE_NAME/src/main/java/com/gogidix/management/executive/$dir" ]; then
            # Only remove if the package-specific directory exists
            if [ -d "$SERVICE_NAME/src/main/java/com/gogidix/management/executive/$PACKAGE" ]; then
                rm -rf "$SERVICE_NAME/src/main/java/com/gogidix/management/executive/$dir"
            fi
        fi
    done

    echo "  ✓ $SERVICE_NAME recreated!"
}

# Recreate all services
recreate_service "operations" "coo-operations-service" "coo-operations-service"
recreate_service "financial" "cfo-financial-consolidation-service" "cfo-financial-consolidation-service"
recreate_service "technology" "cto-technology-oversight-service" "cto-technology-oversight-service"
recreate_service "alert" "executive-alert-service" "executive-alert-service"
recreate_service "workflow" "executive-approval-workflow-service" "executive-approval-workflow-service"

echo ""
echo "=========================================="
echo "All services recreated!"
echo "=========================================="
