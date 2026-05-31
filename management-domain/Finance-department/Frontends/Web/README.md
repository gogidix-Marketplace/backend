# Gogidix Ecosystem - Finance Department Web Applications

## Overview

This directory contains all the web-based financial dashboards and portals for the Gogidix ecosystem. Each application is built with React, Vite, TypeScript, and Tailwind CSS.

## Applications

| Application | Port | Description |
|-------------|------|-------------|
| **Launcher Hub** | 3000 | Unified launcher for all dashboards |
| **Finance Dashboard** | 3002 | Main financial hub |
| **Accountant Dashboard** | 3021 | Daily operations and bookkeeping |
| **CFO Dashboard** | 3022 | Executive overview and KPIs |
| **Reports Dashboard** | 3023 | Financial reports |
| **Finance Portal** | 3024 | Client-facing portal |

## Quick Start

### Option 1: Start All at Once (Recommended)

**Windows:**
```batch
start-all.bat
```

**Git Bash / Linux:**
```bash
chmod +x start-all.sh stop-all.sh
./start-all.sh
```

### Option 2: Start Individually

Open separate terminals for each dashboard:

```bash
# Terminal 1
cd launcher-hub
npm run dev

# Terminal 2
cd finance-web-dashboard
npm run dev

# Terminal 3
cd finance-web-dashboard/accountant-dashboard
npm run dev

# Terminal 4
cd finance-web-dashboard/cfo-dashboard
npm run dev

# Terminal 5
cd finance-web-dashboard/reports-dashboard
npm run dev

# Terminal 6
cd finance-web-portal
npm run dev
```

## Access the Applications

1. Open **http://localhost:3000** for the Launcher Hub
2. Click "Check All Services" to verify dashboards are running
3. Click "Launch" on any online dashboard to open it

## Stop All Dashboards

**Git Bash / Linux:**
```bash
./stop-all.sh
```

**Windows:** Close the terminal windows or press Ctrl+C in each.

## Project Structure

```
Web/
├── launcher-hub/           # Unified launcher (port 3000)
├── finance-web-dashboard/  # Main finance dashboard (port 3002)
│   ├── accountant-dashboard/  # Accountant view (port 3021)
│   ├── cfo-dashboard/         # CFO view (port 3022)
│   └── reports-dashboard/     # Reports view (port 3023)
└── finance-web-portal/     # External portal (port 3024)

start-all.bat              # Windows startup script
start-all.sh               # Linux/Mac startup script
stop-all.sh                # Stop all dashboards
```

## Development

Each application can be developed independently. All share the same tech stack:
- **React 18** - UI library
- **Vite** - Build tool and dev server
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling

## Building for Production

```bash
cd [dashboard-directory]
npm run build
npm run preview
```

## Troubleshooting

**Port already in use:**
- Change the port in the `vite.config.ts` file
- Or stop the process using that port

**Dependencies not found:**
```bash
npm install
```

**Build errors:**
```bash
rm -rf node_modules dist
npm install
npm run build
```
