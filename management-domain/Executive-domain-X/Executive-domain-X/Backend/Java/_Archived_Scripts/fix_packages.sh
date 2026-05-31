#!/bin/bash
# Fix all package declarations and imports in all services

BASE_DIR="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"

# Function to fix a single service
fix_service() {
    local SERVICE_DIR=$1
    local PACKAGE_BASE=$2  # e.g., "com.gogidix.management.executive.analytics"

    echo "Fixing $SERVICE_DIR..."

    # Fix all Java files
    find "$SERVICE_DIR/src" -type f -name "*.java" -exec sed -i \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.[a-z]*\.[a-z]*\.|package ${PACKAGE_BASE}.|g" \
        -e "s|package com\.gogidix\.management\.executive\.[a-z]*\.[a-z]*\.|package ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.[a-z]*\.[a-z]*\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.[a-z]*\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.analytics\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.approval\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.strategy\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.financial\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.operations\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.technology\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.alert\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.workflow\.|import ${PACKAGE_BASE}.|g" \
        -e "s|import com\.gogidix\.management\.executive\.[a-z]*\.audit\.|import ${PACKAGE_BASE}.|g" \
        {} \;

    echo "  Done"
}

# Fix all 9 services
fix_service "$BASE_DIR/ceo-analytics-service" "com.gogidix.management.executive.analytics"
fix_service "$BASE_DIR/ceo-approval-service" "com.gogidix.management.executive.approval"
fix_service "$BASE_DIR/ceo-strategy-service" "com.gogidix.management.executive.strategy"
fix_service "$BASE_DIR/cfo-financial-consolidation-service" "com.gogidix.management.executive.financial"
fix_service "$BASE_DIR/coo-operations-service" "com.gogidix.management.executive.operations"
fix_service "$BASE_DIR/cto-technology-oversight-service" "com.gogidix.management.executive.technology"
fix_service "$BASE_DIR/executive-alert-service" "com.gogidix.management.executive.alert"
fix_service "$BASE_DIR/executive-approval-workflow-service" "com.gogidix.management.executive.workflow"
fix_service "$BASE_DIR/executive-audit-service" "com.gogidix.management.executive.audit"

echo "All services fixed!"
