@echo off
REM Gogidix Ecosystem - Complete Startup
REM This script starts the launcher service and the launcher hub

echo ========================================
echo   Gogidix Ecosystem Launcher Setup
echo ========================================
echo.

REM Start Launcher Service (port 2999)
echo [1/2] Starting Launcher Service...
start "Gogidix - Launcher Service" cmd /k "cd /d "%~dp0launcher-service" && echo Launcher Service running on http://localhost:2999 && npm start"

REM Wait for service to start
echo Waiting for service to initialize...
timeout /t 3 /nobreak >nul

REM Start Launcher Hub (port 3000)
echo [2/2] Starting Launcher Hub...
start "Gogidix - Launcher Hub" cmd /k "cd /d "%~dp0launcher-hub" && echo Launcher Hub running on http://localhost:3000 && npm run dev"

echo.
echo ========================================
echo   All services started!
echo ========================================
echo.
echo Launcher Hub: http://localhost:3000
echo Launcher Service: http://localhost:2999
echo.
echo Press any key to close this window (services will continue running)...
pause >nul
