#!/bin/bash
# Final comprehensive fix for all 9 services

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

echo "Starting comprehensive fix for all 9 services..."

# Fix package suffixes issues
for service_dir in "$BASE_DIR"/*-service; do
    service_name=$(basename "$service_dir")

    # Determine package suffix based on service name
    case $service_name in
        "ceo-analytics-service")
            suffix="analytics"
            entity="Analytics"
            ;;
        "ceo-approval-service")
            suffix="approval"
            entity="Approval"
            ;;
        "ceo-strategy-service")
            suffix="strategy"
            entity="Strategy"
            ;;
        "cfo-financial-consolidation-service")
            suffix="financial"
            entity="FinancialData"
            ;;
        "coo-operations-service")
            suffix="operations"
            entity="Operations"
            ;;
        "cto-technology-oversight-service")
            suffix="technology"
            entity="Technology"
            ;;
        "executive-alert-service")
            suffix="alert"
            entity="Alert"
            ;;
        "executive-approval-workflow-service")
            suffix="workflow"
            entity="Workflow"
            ;;
        "executive-audit-service")
            suffix="audit"
            entity="Audit"
            ;;
        *)
            continue
            ;;
    esac

    echo "Processing $service_name..."

    # Fix all Java files
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null | while read -r java_file; do
        # Fix duplicated package suffixes
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.$suffix\.|package com.gogidix.management.executive.$suffix.|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.|package com.gogidix.management.executive.$suffix.|g" "$java_file"

        # Fix wrong package paths (model instead of domain.model, etc.)
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.model|package com.gogidix.management.executive.$suffix.domain.model|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.command|package com.gogidix.management.executive.$suffix.application.command|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.query|package com.gogidix.management.executive.$suffix.application.query|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.dto|package com.gogidix.management.executive.$suffix.application.dto|g" "$java_file"
        sed -i "s|package com\.gogidix\.management\.executive\.$suffix\.mapper|package com.gogidix.management.executive.$suffix.application.mapper|g" "$java_file"

        # Fix imports
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.model|import com.gogidix.management.executive.$suffix.domain.model|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.command|import com.gogidix.management.executive.$suffix.application.command|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.query|import com.gogidix.management.executive.$suffix.application.query|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.dto|import com.gogidix.management.executive.$suffix.application.dto|g" "$java_file"
        sed -i "s|import com\.gogidix\.management\.executive\.$suffix\.mapper|import com.gogidix.management.executive.$suffix.application.mapper|g" "$java_file"
    done

    echo "  Fixed $service_name"
done

echo ""
echo "All services fixed!"
