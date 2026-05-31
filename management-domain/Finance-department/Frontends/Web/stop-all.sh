#!/bin/bash

# Gogidix Ecosystem - Stop All Dashboards
# This script stops all running dashboard applications

BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

echo "🛑 Stopping Gogidix Ecosystem Dashboards..."
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to stop a dashboard
stop_dashboard() {
  local name=$1
  local pid_file="${BASE_DIR}/${name}.pid"

  if [ -f "$pid_file" ]; then
    local pid=$(cat "$pid_file")
    if kill -0 "$pid" 2>/dev/null; then
      echo -e "${YELLOW}Stopping ${name} (PID: ${pid})...${NC}"
      kill "$pid"
      rm "$pid_file"
      echo -e "${GREEN}✓ ${name} stopped${NC}"
    else
      echo -e "${RED}✗ ${name} was not running${NC}"
      rm "$pid_file"
    fi
  else
    echo -e "${YELLOW}⚠ ${name} PID file not found${NC}"
  fi
}

# Stop all dashboards
stop_dashboard "launcher-hub"
stop_dashboard "finance-dashboard"
stop_dashboard "accountant-dashboard"
stop_dashboard "cfo-dashboard"
stop_dashboard "reports-dashboard"
stop_dashboard "finance-portal"

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}All dashboards stopped!${NC}"
echo -e "${GREEN}========================================${NC}"
