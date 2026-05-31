#!/bin/bash
# Fix all imports for all 9 services

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

echo "Fixing imports for all services..."

# Define service configurations
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

for service_name in "${!PACKAGE_SUFFIXES[@]}"; do
    suffix="${PACKAGE_SUFFIXES[$service_name]}"
    entity="${ENTITIES[$service_name]}"
    service_dir="$BASE_DIR/$service_name"

    echo "Fixing $service_name..."

    # Find and fix all Java files
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null | while read -r java_file; do
        # Fix entity import - should be from domain.model
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.$entity;|import com.gogidix.management.executive.$suffix.domain.model.$entity;|g" "$java_file"

        # Fix repository import - should be from domain.repository
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.${entity}Repository;|import com.gogidix.management.executive.$suffix.domain.repository.${entity}Repository;|g" "$java_file"

        # Fix other common imports
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.BaseEntity;|import com.gogidix.management.executive.$suffix.domain.model.BaseEntity;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.KpiWidget;|import com.gogidix.management.executive.$suffix.domain.model.KpiWidget;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.KpiWidgetRepository;|import com.gogidix.management.executive.$suffix.domain.repository.KpiWidgetRepository;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.DashboardId;|import com.gogidix.management.executive.$suffix.domain.model.${entity}Id;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.TenantId;|import com.gogidix.management.executive.$suffix.domain.model.TenantId;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.DomainException;|import com.gogidix.management.executive.$suffix.domain.model.DomainException;|g" "$java_file"

        # Fix other domain model imports
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.AnalyticsData;|import com.gogidix.management.executive.$suffix.domain.model.${entity}Data;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.DataFeed;|import com.gogidix.management.executive.$suffix.domain.model.DataFeed;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.ExecutiveSummary;|import com.gogidix.management.executive.$suffix.domain.model.ExecutiveSummary;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.PerformanceBenchmark;|import com.gogidix.management.executive.$suffix.domain.model.PerformanceBenchmark;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.MetricType;|import com.gogidix.management.executive.$suffix.domain.model.MetricType;|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.MetricValue;|import com.gogidix.management.executive.$suffix.domain.model.MetricValue;|g" "$java_file"
    done
done

echo "All imports fixed!"
