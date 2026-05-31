#!/bin/bash
# Final efficient fix for all 9 services

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

# Function to fix a single service
fix_service() {
    local service_name=$1
    local suffix=$2
    local entity=$3
    local service_dir="$BASE_DIR/$service_name"

    echo "Fixing $service_name..."

    # Use find with -exec for better performance
    # Fix packages first
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null -print0 | xargs -0 sed -i \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.analytics\.|package com.gogidix.management.executive.analytics.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.approval\.|package com.gogidix.management.executive.approval.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.strategy\.|package com.gogidix.management.executive.strategy.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.financial\.|package com.gogidix.management.executive.financial.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.operations\.|package com.gogidix.management.executive.operations.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.technology\.|package com.gogidix.management.executive.technology.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.alert\.|package com.gogidix.management.executive.alert.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.workflow\.|package com.gogidix.management.executive.workflow.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.audit\.|package com.gogidix.management.executive.audit.|g"

    # Fix repository and service packages
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null -print0 | xargs -0 sed -i \
        -e "s|package com\.gogidix\.management\.executive\.$suffix\.repository|package com.gogidix.management.executive.$suffix.domain.repository|g" \
        -e "s|package com\.gogidix\.management\.executive\.$suffix\.service|package com.gogidix.management.executive.$suffix.domain.service|g"

    # Fix all imports to use full paths
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null -print0 | xargs -0 sed -i \
        -e "s|import com\.gogidix\.management\.executive\.$suffix\.|import com.gogidix.management.executive.$suffix.domain.|g"

    # Fix specific incorrect patterns
    find "$service_dir/src" -type f -name "*.java" 2>/dev/null -print0 | xargs -0 sed -i \
        -e "s|import com\.gogidix\.management\.executive\.$suffix\.domain\.domain\.|import com.gogidix.management.executive.$suffix.domain.|g" \
        -e "s|package com\.gogidix\.management\.executive\.$suffix\.domain\.domain\.|package com.gogidix.management.executive.$suffix.domain.|g"

    echo "  Fixed $service_name"
}

# Export the function and run in parallel
export -f fix_service
export BASE_DIR

# Fix all services
fix_service "ceo-analytics-service" "analytics" "Analytics" &
fix_service "ceo-approval-service" "approval" "Approval" &
fix_service "ceo-strategy-service" "strategy" "Strategy" &
fix_service "cfo-financial-consolidation-service" "financial" "FinancialData" &
fix_service "coo-operations-service" "operations" "Operations" &
fix_service "cto-technology-oversight-service" "technology" "Technology" &
fix_service "executive-alert-service" "alert" "Alert" &
fix_service "executive-approval-workflow-service" "workflow" "Workflow" &
fix_service "executive-audit-service" "audit" "Audit" &

# Wait for all to complete
wait

echo ""
echo "All services fixed!"
