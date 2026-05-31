#!/bin/bash

# Gogidix Ecosystem - Start All Dashboards
# This script launches all dashboard applications in separate background processes

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "🚀 Starting Gogidix Ecosystem Dashboards..."
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to start a dashboard
start_dashboard() {
  local name=$1
  local dir=$2
  local port=$3

  echo -e "${BLUE}Starting ${name} on port ${port}...${NC}"
  cd "$dir"
  nohup npm run dev > "${name}.log" 2>&1 &
  echo $! > "${name}.pid"
  echo -e "${GREEN}✓ ${name} started (PID: $(cat ${name}.pid))${NC}"
  echo ""
}

# Start Launcher Hub
start_dashboard "launcher-hub" "$BASE_DIR/launcher-hub" 3000

# Wait a bit for launcher to start
sleep 2

# Start Finance Dashboard
start_dashboard "finance-dashboard" "$BASE_DIR/finance-web-dashboard" 3002

# Start Accountant Dashboard
start_dashboard "accountant-dashboard" "$BASE_DIR/finance-web-dashboard/accountant-dashboard" 3021

# Start CFO Dashboard
start_dashboard "cfo-dashboard" "$BASE_DIR/finance-web-dashboard/cfo-dashboard" 3022

# Start Reports Dashboard
start_dashboard "reports-dashboard" "$BASE_DIR/finance-web-dashboard/reports-dashboard" 3023

# Start Finance Portal
start_dashboard "finance-portal" "$BASE_DIR/finance-web-portal" 3024

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}All dashboards started!${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "🌐 Launcher Hub:    http://localhost:3000"
echo "💼 Finance Dashboard:   http://localhost:3002"
echo "💰 Accountant:         http://localhost:3021"
echo "📊 CFO Dashboard:      http://localhost:3022"
echo "📋 Reports:            http://localhost:3023"
echo "🌐 Finance Portal:     http://localhost:3024"
echo ""
echo "To stop all dashboards, run: ./stop-all.sh"
echo ""
echo "Log files available in each directory:"
echo "  - launcher-hub.log"
echo "  - finance-dashboard.log"
echo "  - accountant-dashboard.log"
echo "  - cfo-dashboard.log"
echo "  - reports-dashboard.log"
echo "  - finance-portal.log"
