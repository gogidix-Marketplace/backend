#!/bin/bash
# =============================================================================
# Global Marketing Dashboard Service - Supabase Database Setup Automation
# =============================================================================
# This script automates the Supabase database setup using curl commands
#
# Prerequisites:
# - curl (for HTTP requests)
# - jq (for JSON parsing) - optional but recommended
#
# Usage: ./setup-supabase.sh [your-database-password]
#
# ============================================================================

set -e

# Configuration
SUPABASE_URL="https://jbtozjinbrfibzwxbnpl.supabase.co"
SUPABASE_PROJECT="jbtozjinbrfibzwxbnpl"
SUPABASE_ANON_KEY="sb_publishable_s_d7BtMw3aMElRNRpQWt2g_5g97orsX"
JWT_SECRET="your-jwt-secret-key-change-in-production"
JWT_EXPIRATION=3600000

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

# =============================================================================
# Functions
# ============================================================================

log() {
    echo -e "${GREEN}[$(date +%H:%M:%S)]${NC} $1"
}

log_error() {
    echo -e "${RED}[$(date +%H:%M:%S)]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[$(date +%H:%M:%S)]${NC} $1"
}

log_info() {
    echo -e "${BLUE}[$(date +%H:%M:%S)]${NC} $1"
}

check_curl() {
    if ! command -v curl &> /dev/null; then
        log_error "curl is not installed. Please install curl first."
        exit 1
    fi
}

check_jq() {
    if ! command -v jq &> /dev/null; then
        log_warn "jq is not installed. JSON parsing may be less reliable."
    fi
}

# =============================================================================
# Step 1: Check Prerequisites
# ============================================================================

log "Checking prerequisites..."

check_curl
check_jq

log_info "Prerequisites check ${GREEN}complete${NC}"

# =============================================================================
# Step 2: Prompt for Database Password (if not provided)
# ============================================================================

if [ -z "$1" ]; then
    echo ""
    echo "============================================="
    echo "  Supabase Database Password Required"
    echo "============================================="
    echo ""
    echo "Please enter your Supabase project database password."
    echo ""
    echo -n "You can find it at: ${BLUE}https://jbtozjinbrfibzwxbnpl.supabase.co${NC}"
    echo -n "  1. Go to Project Settings → Database"
    echo -n "  2. Find or create 'database_password'"
    echo -n "  3. Copy the connection string (jdbc:postgresql://postgres:YOUR-PASSWORD@...)"
    echo ""
    read -s -p "> Database password: " DB_PASSWORD

    # Update configuration with password
    DATABASE_URL="jdbc:postgresql://postgres:${DB_PASSWORD}@db.jbtozjinbrfibzwxbnpl.supabase.co:5432/postgres"

    log_info "Database password configured."
    echo ""
else
    DATABASE_URL="jdbc:postgresql://postgres:${1}@db.jbtozjinbrfibzwxbnpl.supabase.co:5432/postgres"
fi

# =============================================================================
# Step 3: Enable UUID Extension
# ============================================================================

log "Enabling UUID extension..."

ENABLE_UUID=$(
curl -s -X POST \
    -H "Authorization: Bearer ${SUPABASE_ANON_KEY}" \
    -H "apikey: ${SUPABASE_ANON_KEY}" \
    -H "Content-Type: application/json" \
    -d '{"name":"uuid-ossp","version":"1.1.0"}' \
    "${SUPABASE_URL}/rest/v1/extensions" 2>&1
)

if echo "$ENABLE_UUID" | grep -q '"enabled":true'; then
    log_info "UUID extension ${GREEN}enabled${NC}"
else
    log_error "Failed to enable UUID extension"
    echo "$ENABLE_UUID"
fi

# =============================================================================
# Step 4: Create Tables
# ============================================================================

create_tables() {
    log "Creating database tables..."

    SQL_FILE="./supabase_tables.sql"

    # Read SQL file
    if [ ! -f "$SQL_FILE" ]; then
        log_error "SQL file not found: $SQL_FILE"
        exit 1
    fi

    # Execute SQL via SQL Editor API
    CREATE_RESPONSE=$(
        curl -s -X POST \
            -H "Authorization: Bearer ${SUPABASE_ANON_KEY}" \
            -H "apikey: ${SUPABASE_ANON_KEY}" \
            -H "Content-Type: application/json" \
            -H "Prefer: return=representation" \
            -H "Accept: application/json" \
            -d @\"@ \
            -T "dashboard_metrics, country_stats, global_alerts" \
            "{\"queries\": [$(cat "$SQL_FILE" | sed 's/\\/\\\\/\\\\/g')]}"
            \"\" \
            "${SUPABASE_URL}/sql" 2>&1
    )

    if echo "$CREATE_RESPONSE" | grep -q '"error":null'; then
        log_info "Tables ${GREEN}created${NC}"

        # Check results
        if command -v jq &> /dev/null; then
            RESULTS=$(echo "$CREATE_RESPONSE" | jq -r)
            RESULTS_COUNT=$(echo "$RESULTS" | jq '.data | length')
            log_info "Created $RESULTS_COUNT tables"
        else
            RESULTS_COUNT=$(echo "$CREATE_RESPONSE" | grep -o '"name":"dashboard_metrics"' | wc -l)
            if [ "$RESULTS_COUNT" -eq 3 ]; then
                log_info "All 3 tables created"
            else
                log_warn "Expected 3 tables, got $RESULTS_COUNT"
        fi
    else
        log_error "Failed to create tables"
        echo "$CREATE_RESPONSE"
    fi
}

# =============================================================================
# Step 5: Enable Row Level Security
# ============================================================================

enable_rls() {
    log "Enabling Row Level Security..."

    RLS_QUERY=$(
        curl -s -X POST \
            -H "Authorization: Bearer ${SUPABASE_ANON_KEY}" \
            -H "apikey: ${SUPABASE_ANON_KEY}" \
            -H "Content-Type: application/json" \
            -d '{
                "name": "enable_rls",
                "queries": [
                    "ALTER TABLE dashboard_metrics ENABLE ROW LEVEL SECURITY;",
                    "CREATE POLICY dashboard_metrics_tenant_policy ON dashboard_metrics USING (SELECT tenant_id) FROM dashboard_metrics) FOR SELECT",
                    "ALTER TABLE country_stats ENABLE ROW LEVEL SECURITY;",
                    "CREATE POLICY country_stats_tenant_policy ON country_stats USING (SELECT tenant_id) FROM country_stats) FOR SELECT",
                    "ALTER TABLE global_alerts ENABLE ROW LEVEL SECURITY;",
                    "CREATE POLICY global_alerts_tenant_policy ON global_alerts USING (SELECT tenant_id) FROM global_alerts) FOR SELECT"
                ]
            }' \
            "${SUPABASE_URL}/sql" 2>&1
    )

    if echo "$RLS_QUERY" | grep -q '"error":null'; then
        log_info "Row Level Security ${GREEN}enabled${NC}"
    else
        log_error "Failed to enable RLS"
        echo "$RLS_QUERY"
    fi
}

# =============================================================================
# Step 6: Insert Sample Data
# ============================================================================

insert_sample_data() {
    log "Inserting sample data..."

    INSERT_METRICS=$(
        curl -s -X POST \
            -H "Authorization: Bearer ${SUPABASE_ANON_KEY}" \
            -H "apikey: ${SUPABASE_ANON_KEY}" \
            -H "Content-Type: application/json" \
            -d '{
                "name": "dashboard_metrics",
                "values": [
                    {"tenant_id": "default", "name": "Total Revenue", "value": 125000.50, "category": "REVENUE", "period": "DAILY", "trend": "UP"},
                    {"tenant_id": "default", "name": "Active Users", "value": 15234, "category": "USERS", "period": "DAILY", "trend": "UP"},
                    {"tenant_id": "default", "name": "Conversion Rate", "value": 3.2, "category": "PERFORMANCE", "period": "DAILY", "trend": "STABLE"}
                ]
            }' \
            "${SUPABASE_URL}/rest/v1/dashboards" 2>&1
    )

    if echo "$INSERT_METRICS" | grep -q '"error":null'; then
        log_info "Sample metrics ${GREEN}inserted${NC}"
    else
        log_error "Failed to insert sample metrics"
        echo "$INSERT_METRICS"
    fi
}

# =============================================================================
# Step 7: Verify Setup
# ============================================================================

verify_setup() {
    log "Verifying database setup..."

    # Check tables
    TABLES_CHECK=$(
        curl -s -X GET \
            -H "Authorization: Bearer ${SUPABASE_ANON_KEY}" \
            -H "apikey: ${SUPABASE_ANON_KEY}" \
            -H "Accept: application/json" \
            "${SUPABASE_URL}/rest/v1/tables?schema=public" 2>&1
    )

    TABLES_COUNT=$(echo "$TABLES_CHECK" | grep -o '"name":"dashboard_metrics"' | wc -l)
    if [ "$TABLES_COUNT" -gt 0 ]; then
        log_info "Table dashboard_metrics ${GREEN}found${NC}"
    else
        log_warn "Table dashboard_metrics not found"
    fi

    TABLES_COUNT=$(echo "$TABLES_CHECK" | grep -o '"name":"country_stats"' | wc -l)
    if [ "$TABLES_COUNT" -gt 0 ]; then
        log_info "Table country_stats ${GREEN}found${NC}"
    else
        log_warn "Table country_stats not found"
    fi

    TABLES_COUNT=$(echo "$TABLES_CHECK" | grep -o '"name":"global_alerts"' | wc -l)
    if [ "$TABLES_COUNT" -gt 0 ]; then
        log_info "Table global_alerts ${GREEN}found${NC}"
    else
        log_warn "Table global_alerts not found"
    fi

    # Check RLS
    if command -v jq &> /dev/null; then
        RLS_CHECK=$(
            curl -s -X GET \
                -H "Authorization: Bearer ${SUPABASE_ANON_KEY}" \
                -H "apikey: ${SUPABASE_ANON_KEY}" \
                "${SUPABASE_URL}/rest/v1/policies" 2>&1
        )

        if echo "$RLS_CHECK" | grep -q '"error":null'; then
            RLS_ENABLED=$(echo "$RLS_CHECK" | jq -r '.data | any(.policies[].name == "enable_rls")')
            if [ "$RLS_ENABLED" == "true" ]; then
                log_info "Row Level Security ${GREEN}enabled${NC}"
            else
                log_warn "Row Level Security may not be enabled"
            fi
        fi
    fi

    log_info "Setup verification ${GREEN}complete${NC}"
}

# =============================================================================
# Step 8: Update Application Configuration
# ============================================================================

update_config() {
    log "Updating application configuration..."

    CONFIG_FILE="../src/main/resources/application.yml"

    # Create backup
    cp "$CONFIG_FILE" "${CONFIG_FILE}.backup" 2>/dev/null

    # Replace database URL placeholder with actual value
    if [ -n "$DATABASE_URL" ]; then
        # Use sed for cross-platform compatibility
        if [[ "$OSTYPE" == "darwin"* ]]; then
            sed -i '' 's|datasource:|url: jdbc:postgresql:\/\/postgres:\[YOUR-PASSWORD\]@db\.jbtozjinbrfibzwxbnpl\.supabase\.co:5432\/postgres|datasource:|url: '"'"$DATABASE_URL"'"'' "$CONFIG_FILE"
        elif [[ "$OSTYPE" == "linux"* ]]; then
            sed -i 's|datasource:|url: jdbc:postgresql:\/\/postgres:\[YOUR-PASSWORD\]@db\.jbtozjinbrfibzwxbnpl\.supabase\.co:5432\/postgres|datasource:|url: '"'"$DATABASE_URL"'"'' "$CONFIG_FILE"
        else
            # Windows (Git Bash)
            sed -i 's|datasource:|url: jdbc:postgresql:\/\/postgres:\[YOUR-PASSWORD\]@db\.jbtozjinbrfibzwxbnpl\.supabase\.co:5432\/postgres|datasource:|url: '"'"$DATABASE_URL"'"'' "$CONFIG_FILE"
        fi

    log_info "Configuration updated with database URL"
}

# =============================================================================
# Main Menu
# ============================================================================

show_menu() {
    echo ""
    echo "============================================="
    echo -e "   ${BLUE}Supabase Database Setup${NC}   "
    echo "============================================="
    echo ""
    echo -e "  ${GREEN}1)${NC} Create Database Tables"
    echo -e "  ${GREEN}2)${NC} Enable Row Level Security"
    echo -e "  ${GREEN}3)${NC} Insert Sample Data"
    echo -e "  ${GREEN}4)${NC} Verify Setup"
    echo -e "  ${GREEN}5)${NC} Update Application Config"
    echo -e "  ${GREEN}6)${NC} Run All"
    echo -e "  ${YELLOW}0)${NC} Exit"
    echo ""
    echo "============================================="
}

# =============================================================================
# Main Execution
# ============================================================================

# Check for command line argument or run full setup
if [ "$1" == "full" ] || [ "$1" == "" ]; then
    log "Starting full Supabase setup..."

    create_tables
    enable_rls
    insert_sample_data
    verify_setup
    update_config

    log ""
    log_info "============================================="
    log_info "  ${GREEN}Setup Complete!${NC}"
    log_info "============================================="
    log ""
    echo -e "${GREEN}Next steps:${NC}"
    echo -e "  1. Update application.yml with your database password"
    echo -e "  2. Restart the application: mvn spring-boot:run"
    echo -e "  3. Test authentication: curl -X POST http://localhost:8080/api/auth/signin -d '{\"email\":\"test@example.com\",\"password\":\"password\"}'"
    echo -e "  4. Test dashboard: curl -X GET http://localhost:8080/api/dashboard/statistics -H \"Authorization: Bearer <TOKEN>\""

else
    # Interactive menu
    show_menu

    # Wait for user input
    read -p "> Press Enter to continue..." -r

    # Show menu again
    show_menu
fi

# =============================================================================
# End
# =============================================================================
