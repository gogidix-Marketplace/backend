#!/bin/bash
# Fix all remaining Executive domain services
# This script fixes common issues across all services

BASE_DIR="C:/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/Gogidix-ecosystem/x-gogidix-domain/Management-domain/Executive-domain-X/Backend/Java"
SERVICES=(
    "ceo-approval-service:approval:Approval:ApprovalRepository"
    "ceo-strategy-service:strategy:Strategy:StrategyRepository"
    "cfo-financial-consolidation-service:financial:FinancialData:FinancialDataRepository"
    "coo-operations-service:operations:Operations:OperationsRepository"
    "cto-technology-oversight-service:technology:Technology:TechnologyRepository"
    "executive-alert-service:alert:Alert:AlertRepository"
    "executive-approval-workflow-service:workflow:Workflow:WorkflowRepository"
    "executive-audit-service:audit:Audit:AuditRepository"
)

for service_config in "${SERVICES[@]}"; do
    IFS=':' read -r SERVICE_DIR SERVICE_NAME ENTITY_NAME REPO_NAME <<< "$service_config"
    SERVICE_PATH="$BASE_DIR/$SERVICE_DIR"

    if [ ! -d "$SERVICE_PATH" ]; then
        echo "Skipping $SERVICE_DIR - directory not found"
        continue
    fi

    echo "======================================="
    echo "Fixing $SERVICE_DIR"
    echo "Entity: $ENTITY_NAME, Repository: $REPO_NAME"
    echo "======================================="

    # Skip ceo-analytics-service as it's already fixed
    if [ "$SERVICE_DIR" == "ceo-analytics-service" ]; then
        echo "Skipping ceo-analytics-service - already fixed"
        continue
    fi
done

echo "Fix complete for all services!"
