#!/bin/bash
# Script to fix corrupted package names in all 9 services

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

# Services with their correct package suffixes
declare -A SERVICES=(
    ["ceo-analytics-service"]="analytics"
    ["ceo-approval-service"]="approval"
    ["ceo-strategy-service"]="strategy"
    ["cfo-financial-consolidation-service"]="financial"
    ["coo-operations-service"]="operations"
    ["cto-technology-oversight-service"]="technology"
    ["executive-alert-service"]="alert"
    ["executive-approval-workflow-service"]="workflow"
    ["executive-audit-service"]="audit"
)

# Entity mappings
declare -A ENTITIES=(
    ["ceo-analytics-service"]="Analytics"
    ["ceo-approval-service"]="Approval"
    ["ceo-strategy-service"]="Strategy"
    ["cfo-financial-consolidation-service"]="FinancialData"
    ["coo-operations-service"]="Operations"
    ["cto-technology-oversight-service"]="Technology"
    ["executive-alert-service"]="Alert"
    ["executive-approval-workflow-service"]="Workflow"
    ["executive-audit-service"]="Audit"
)

fix_service() {
    local SERVICE_NAME=$1
    local PACKAGE_SUFFIX=$2
    local ENTITY=$3

    echo "Fixing $SERVICE_NAME..."

    local SERVICE_DIR="$BASE_DIR/$SERVICE_NAME"
    local TARGET_PACKAGE="com.gogidix.management.executive.$PACKAGE_SUFFIX"

    # Find and fix all Java files
    find "$SERVICE_DIR/src" -type f -name "*.java" 2>/dev/null | while read -r java_file; do
        # Fix corrupted package declarations (remove duplicated parts)
        sed -i "s|package com\.gogidix\.management\.executive\.\([a-z]*\)\.\([a-z]*\)\.\([a-z]*\)\.\([a-z]*\)\.|package $TARGET_PACKAGE\.|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.\([a-z]*\)\.\([a-z]*\)\.\([a-z]*\)\.|package $TARGET_PACKAGE\.|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.\([a-z]*\)\.\([a-z]*\)\.|package $TARGET_PACKAGE\.|g" "$java_file"

        # Fix corrupted imports
        sed -i "s|import com\.gogidix\.management\.executive\.\([a-z]*\)\.\([a-z]*\)\.\([a-z]*\)\.\([a-z]*\)\.|import $TARGET_PACKAGE\.|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.\([a-z]*\)\.\([a-z]*\)\.\([a-z]*\)\.|import $TARGET_PACKAGE\.|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.\([a-z]*\)\.\([a-z]*\)\.|import $TARGET_PACKAGE\.|g" "$java_file"

        # Fix remaining old package references
        sed -i "s|package com\.gogidix\.management\.executive\.|package $TARGET_PACKAGE\.|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.|import $TARGET_PACKAGE\.|g" "$java_file"

        # Fix DashboardStatus to AnalyticsStatus/ApprovalStatus/etc
        sed -i "s|DashboardStatus|${ENTITY}Status|g" "$java_file"
    done

    echo "  Fixed $SERVICE_NAME"
}

# Fix all services
for SERVICE_NAME in "${!SERVICES[@]}"; do
    PACKAGE_SUFFIX="${SERVICES[$SERVICE_NAME]}"
    ENTITY="${ENTITIES[$SERVICE_NAME]}"
    fix_service "$SERVICE_NAME" "$PACKAGE_SUFFIX" "$ENTITY"
done

echo ""
echo "All services fixed!"
