@echo off
REM Gogidix Ecosystem - Start All Dashboards
REM This script launches all dashboard applications

setlocal enabledelayedexpansion

set BASE_DIR=%~dp0
echo ========================================
echo   Gogidix Ecosystem Launcher
echo ========================================
echo.
echo Starting all dashboards in separate windows...
echo.

REM Start Launcher Hub
echo [1/6] Starting Launcher Hub (port 3000)...
start "Gogidix - Launcher Hub" cmd /k "cd /d "%BASE_DIR%launcher-hub" && npm run dev"
timeout /t 2 /nobreak >nul

REM Start Finance Dashboard
echo [2/6] Starting Finance Dashboard (port 3002)...
start "Gogidix - Finance Dashboard" cmd /k "cd /d "%BASE_DIR%finance-web-dashboard" && npm run dev"
timeout /t 2 /nobreak >nul

REM Start Accountant Dashboard
echo [3/6] Starting Accountant Dashboard (port 3021)...
start "Gogidix - Accountant Dashboard" cmd /k "cd /d "%BASE_DIR%finance-web-dashboard\accountant-dashboard" && npm run dev"
timeout /t 2 /nobreak >nul

REM Start CFO Dashboard
echo [4/6] Starting CFO Dashboard (port 3022)...
start "Gogidix - CFO Dashboard" cmd /k "cd /d "%BASE_DIR%finance-web-dashboard\cfo-dashboard" && npm run dev"
timeout /t 2 /nobreak >nul

REM Start Reports Dashboard
echo [5/6] Starting Reports Dashboard (port 3023)...
start "Gogidix - Reports Dashboard" cmd /k "cd /d "%BASE_DIR%finance-web-dashboard\reports-dashboard" && npm run dev"
timeout /t 2 /nobreak >nul

REM Start Finance Portal
echo [6/6] Starting Finance Portal (port 3024)...
start "Gogidix - Finance Portal" cmd /k "cd /d "%BASE_DIR%finance-web-portal" && npm run dev"

echo.
echo ========================================
echo   All dashboards started!
echo ========================================
echo.
echo Access the dashboards at:
echo.
echo   [Launcher Hub]     http://localhost:3000
echo   [Finance Dashboard]   http://localhost:3002
echo   [Accountant]         http://localhost:3021
echo   [CFO Dashboard]      http://localhost:3022
echo   [Reports]            http://localhost:3023
echo   [Finance Portal]     http://localhost:3024
echo.
echo Press any key to close this window (dashboards will continue running)...
pause >nul
