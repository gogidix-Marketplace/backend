#!/bin/bash
# Script to customize a single executive service

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

OLD_PACKAGE="com.gogidix.management.executive"
OLD_MAIN_CLASS="ExecutiveDashboardServiceApplication"
OLD_ENTITY="Dashboard"

customize_service() {
    local SERVICE_NAME=$1
    local NEW_PACKAGE_SUB=$2
    local NEW_MAIN_CLASS=$3
    local NEW_ENTITY=$4

    echo "Customizing $SERVICE_NAME..."

    local SERVICE_DIR="$BASE_DIR/$SERVICE_NAME"

    if [ ! -d "$SERVICE_DIR" ]; then
        echo "  ERROR: Service directory does not exist"
        return 1
    fi

    # Delete Shared folder
    if [ -d "$SERVICE_DIR/Shared" ]; then
        rm -rf "$SERVICE_DIR/Shared"
        echo "  Deleted Shared folder"
    fi

    # Update pom.xml using different delimiter
    local POM_FILE="$SERVICE_DIR/pom.xml"
    if [ -f "$POM_FILE" ]; then
        # Update artifactId
        sed -i "s|<artifactId>executive-dashboard-service</artifactId>|<artifactId>$SERVICE_NAME</artifactId>|g" "$POM_FILE"
        # Update groupId
        sed -i 's|<groupId>com.gogidix.executive</groupId>|<groupId>com.gogidix.management.executive</groupId>|g' "$POM_FILE"
        echo "  Updated pom.xml"
    fi

    # Update and rename Java files
    find "$SERVICE_DIR/src" -type f -name "*.java" | while read -r java_file; do
        # Update package declarations
        sed -i "s|package $OLD_PACKAGE\.|package com.gogidix.management.executive.$NEW_PACKAGE_SUB.|g" "$java_file"

        # Update imports
        sed -i "s|import $OLD_PACKAGE\.|import com.gogidix.management.executive.$NEW_PACKAGE_SUB.|g" "$java_file"

        # Update main class name
        sed -i "s|$OLD_MAIN_CLASS|$NEW_MAIN_CLASS|g" "$java_file"

        # Update entity-related class names (more specific to avoid partial matches)
        sed -i "s|${OLD_ENTITY}Repository|${NEW_ENTITY}Repository|g" "$java_file"
        sed -i "s|${OLD_ENTITY}CommandService|${NEW_ENTITY}CommandService|g" "$java_file"
        sed -i "s|${OLD_ENTITY}QueryService|${NEW_ENTITY}QueryService|g" "$java_file"
        sed -i "s|${OLD_ENTITY}Controller|${NEW_ENTITY}Controller|g" "$java_file"
        sed -i "s|${OLD_ENTITY}Dto|${NEW_ENTITY}Dto|g" "$java_file"
        sed -i "s|${OLD_ENTITY}AggregateDto|${NEW_ENTITY}AggregateDto|g" "$java_file"
        sed -i "s|Create${OLD_ENTITY}Request|Create${NEW_ENTITY}Request|g" "$java_file"
        sed -i "s|Create${OLD_ENTITY}Command|Create${NEW_ENTITY}Command|g" "$java_file"
        sed -i "s|Update${OLD_ENTITY}Command|Update${NEW_ENTITY}Command|g" "$java_file"
        sed -i "s|Delete${OLD_ENTITY}Command|Delete${NEW_ENTITY}Command|g" "$java_file"
        sed -i "s|Get${OLD_ENTITY}Query|Get${NEW_ENTITY}Query|g" "$java_file"
        sed -i "s|List${OLD_ENTITY}Query|List${NEW_ENTITY}Query|g" "$java_file"

        # Update entity class name (more careful)
        sed -i "s|class $OLD_ENTITY |class $NEW_ENTITY |g" "$java_file"
        sed -i "s|extends $OLD_ENTITY|extends $NEW_ENTITY|g" "$java_file"
        sed -i "s|<${OLD_ENTITY}>|<${NEW_ENTITY}>|g" "$java_file"
        sed -i "s|${OLD_ENTITY} |${NEW_ENTITY} |g" "$java_file"
        sed -i "s|(${OLD_ENTITY})|(${NEW_ENTITY})|g" "$java_file"
        sed -i "s|, $OLD_ENTITY|, ${NEW_ENTITY}|g" "$java_file"

        # Fix document collection names
        sed -i "s|collection = \"executive_dashboards\"|collection = \"${SERVICE_NAME}\"|g" "$java_file"

        # Fix scans
        sed -i "s|basePackages = \"$OLD_PACKAGE\"|basePackages = \"com.gogidix.management.executive.$NEW_PACKAGE_SUB\"|g" "$java_file"
    done

    # Rename files
    find "$SERVICE_DIR/src" -type f -name "*${OLD_ENTITY}*.java" | while read -r old_file; do
        dir_name=$(dirname "$old_file")
        base_name=$(basename "$old_file")
        new_base_name=$(echo "$base_name" | sed "s/${OLD_ENTITY}/${NEW_ENTITY}/g")
        mv "$old_file" "$dir_name/$new_base_name"
        echo "  Renamed: $base_name -> $new_base_name"
    done

    find "$SERVICE_DIR/src" -type f -name "*${OLD_MAIN_CLASS}*.java" | while read -r old_file; do
        dir_name=$(dirname "$old_file")
        base_name=$(basename "$old_file")
        new_base_name=$(echo "$base_name" | sed "s/${OLD_MAIN_CLASS}/${NEW_MAIN_CLASS}/g")
        mv "$old_file" "$dir_name/$new_base_name"
        echo "  Renamed: $base_name -> $new_base_name"
    done

    echo "  Done customizing $SERVICE_NAME"
}

# Customize all 9 services
customize_service "ceo-analytics-service" "analytics" "CeoAnalyticsServiceApplication" "Analytics"
customize_service "ceo-approval-service" "approval" "CeoApprovalServiceApplication" "Approval"
customize_service "ceo-strategy-service" "strategy" "CeoStrategyServiceApplication" "Strategy"
customize_service "cfo-financial-consolidation-service" "financial" "CfoFinancialConsolidationServiceApplication" "FinancialData"
customize_service "coo-operations-service" "operations" "CooOperationsServiceApplication" "Operations"
customize_service "cto-technology-oversight-service" "technology" "CtoTechnologyOversightServiceApplication" "Technology"
customize_service "executive-alert-service" "alert" "ExecutiveAlertServiceApplication" "Alert"
customize_service "executive-approval-workflow-service" "workflow" "ExecutiveApprovalWorkflowServiceApplication" "Workflow"
customize_service "executive-audit-service" "audit" "ExecutiveAuditServiceApplication" "Audit"

echo ""
echo "All services customized!"
