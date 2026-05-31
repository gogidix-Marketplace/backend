#!/bin/bash
# Comprehensive fix for all remaining issues in all 9 services

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

echo "Starting comprehensive fix for all 9 services..."

# Service configurations
declare -A PACKAGE_SUFFIXES=(
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

declare -A PLURALS=(
    ["ceo-analytics-service"]="Analytics"
    ["ceo-approval-service"]="Approvals"
    ["ceo-strategy-service"]="Strategies"
    ["cfo-financial-consolidation-service"]="FinancialData"
    ["coo-operations-service"]="Operations"
    ["cto-technology-oversight-service"]="Technology"
    ["executive-alert-service"]="Alerts"
    ["executive-approval-workflow-service"]="Workflows"
    ["executive-audit-service"]="Audits"
)

for service_name in "${!PACKAGE_SUFFIXES[@]}"; do
    suffix="${PACKAGE_SUFFIXES[$service_name]}"
    entity="${ENTITIES[$service_name]}"
    plural="${PLURALS[$service_name]}"
    service_dir="$BASE_DIR/$service_name"

    echo "Fixing $service_name..."

    # Fix all Java files
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null | while read -r java_file; do
        # Fix remaining package issues
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.repository|package com.gogidix.management.executive.$suffix.domain.repository|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.service|package com.gogidix.management.executive.$suffix.domain.service|g" "$java_file"

        # Fix imports
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.repository|import com.gogidix.management.executive.$suffix.domain.repository|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.service|import com.gogidix.management.executive.$suffix.domain.service|g" "$java_file"

        # Fix Query class references
        sed -i "s|ListDashboardsQuery|List${plural}Query|g" "$java_file"
        sed -i "s|GetDashboardQuery|Get${entity}Query|g" "$java_file"
        sed -i "s|DashboardQueryService|${entity}QueryService|g" "$java_file"
        sed -i "s|DashboardCommandService|${entity}CommandService|g" "$java_file"
        sed -i "s|DashboardController|${entity}Controller|g" "$java_file"
        sed -i "s|DashboardRepository|${entity}Repository|g" "$java_file"
        sed -i "s|DashboardDto|${entity}Dto|g" "$java_file"
        sed -i "s|DashboardAggregateDto|${entity}AggregateDto|g" "$java_file"
        sed -i "s|CreateDashboardRequest|Create${entity}Request|g" "$java_file"
        sed -i "s|CreateDashboardCommand|Create${entity}Command|g" "$java_file"
        sed -i "s|UpdateDashboardCommand|Update${entity}Command|g" "$java_file"
        sed -i "s|DeleteDashboardCommand|Delete${entity}Command|g" "$java_file"

        # Fix dashboard variable names to entity names
        sed -i "s| dashboard | ${entity,,} |g" "$java_file"
        sed -i "s|dashboard\.|${entity,,}.|g" "$java_file"
        sed -i "s|dashboardRepository|${entity,,}Repository|g" "$java_file"
        sed -i "s|DashboardId|${entity}Id|g" "$java_file"
    done

    # Rename incorrectly named files
    find "$service_dir/src" -type f -name "*Analyticss*" 2>/dev/null | while read -r old_file; do
        new_file=$(echo "$old_file" | sed "s/Analyticss/Analytics/g")
        if [ "$old_file" != "$new_file" ]; then
            mv "$old_file" "$new_file"
            echo "  Renamed: $(basename "$old_file") -> $(basename "$new_file")"
        fi
    done

    echo "  Fixed $service_name"
done

echo ""
echo "All services comprehensively fixed!"
