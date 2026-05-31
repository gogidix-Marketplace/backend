#!/bin/bash
# Script to customize a single executive service

BLUEPRINT_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain/Backend/Java/executive-dashboard-service"
BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

OLD_PACKAGE="com.gogidix.management.executive"
OLD_MAIN_CLASS="ExecutiveDashboardServiceApplication"
OLD_ENTITY="Dashboard"

customize_service() {
    local SERVICE_NAME=$1
    local NEW_PACKAGE=$2
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

    # Update pom.xml
    local POM_FILE="$SERVICE_DIR/pom.xml"
    if [ -f "$POM_FILE" ]; then
        sed -i "s/<artifactId>executive-dashboard-service<\\/artifactId>/<artifactId>$SERVICE_NAME<\\/artifactId>/g" "$POM_FILE"
        sed -i 's/<groupId>com\.gogidix\.executive<\\/groupId>/<groupId>com.gogidix.management.executive<\/groupId>/g' "$POM_FILE"

        # Rename files
        find "$SERVICE_DIR/src" -type f -name "*.java" | while read -r java_file; do
            # Update package declarations
            sed -i "s/package $OLD_PACKAGE\./package $NEW_PACKAGE\./g" "$java_file"

            # Update imports
            sed -i "s/import $OLD_PACKAGE\./import $NEW_PACKAGE\./g" "$java_file"

            # Update main class name
            sed -i "s/$OLD_MAIN_CLASS/$NEW_MAIN_CLASS/g" "$java_file"

            # Update entity-related class names
            sed -i "s/${OLD_ENTITY}Repository/${NEW_ENTITY}Repository/g" "$java_file"
            sed -i "s/${OLD_ENTITY}CommandService/${NEW_ENTITY}CommandService/g" "$java_file"
            sed -i "s/${OLD_ENTITY}QueryService/${NEW_ENTITY}QueryService/g" "$java_file"
            sed -i "s/${OLD_ENTITY}Controller/${NEW_ENTITY}Controller/g" "$java_file"

            # Update entity class name
            sed -i "s/\\b${OLD_ENTITY}\\b/${NEW_ENTITY}/g" "$java_file"

            # Fix document collection names
            sed -i "s/collection = \"executive_dashboards\"/collection = \"${SERVICE_NAME}\"/g" "$java_file"
        done

        # Rename files
        find "$SERVICE_DIR/src" -type f -name "*${OLD_ENTITY}*.java" | while read -r old_file; do
            new_file=$(echo "$old_file" | sed "s/${OLD_ENTITY}/${NEW_ENTITY}/g")
            mv "$old_file" "$new_file"
        done

        find "$SERVICE_DIR/src" -type f -name "*${OLD_MAIN_CLASS}*.java" | while read -r old_file; do
            new_file=$(echo "$old_file" | sed "s/${OLD_MAIN_CLASS}/${NEW_MAIN_CLASS}/g")
            mv "$old_file" "$new_file"
        done

        echo "  Updated pom.xml and Java files"
    fi

    echo "  Done customizing $SERVICE_NAME"
}

# Customize all 9 services
customize_service "ceo-analytics-service" "com.gogidix.management.executive.analytics" "CeoAnalyticsServiceApplication" "Analytics"
customize_service "ceo-approval-service" "com.gogidix.management.executive.approval" "CeoApprovalServiceApplication" "Approval"
customize_service "ceo-strategy-service" "com.gogidix.management.executive.strategy" "CeoStrategyServiceApplication" "Strategy"
customize_service "cfo-financial-consolidation-service" "com.gogidix.management.executive.financial" "CfoFinancialConsolidationServiceApplication" "FinancialData"
customize_service "coo-operations-service" "com.gogidix.management.executive.operations" "CooOperationsServiceApplication" "Operations"
customize_service "cto-technology-oversight-service" "com.gogidix.management.executive.technology" "CtoTechnologyOversightServiceApplication" "Technology"
customize_service "executive-alert-service" "com.gogidix.management.executive.alert" "ExecutiveAlertServiceApplication" "Alert"
customize_service "executive-approval-workflow-service" "com.gogidix.management.executive.workflow" "ExecutiveApprovalWorkflowServiceApplication" "Workflow"
customize_service "executive-audit-service" "com.gogidix.management.executive.audit" "ExecutiveAuditServiceApplication" "Audit"

echo ""
echo "All services customized!"
