@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   Starting Gogidix Ecosystem Dashboards
echo ========================================
echo.

set "BASE_DIR=%~dp0"
set "NODE_PATH=where node.exe 2>nul"

:: Find node.exe
for %%i in (node.exe) do set "NODE_PATH=%%i"
if not defined NODE_PATH (
    echo ERROR: Node.js not found in PATH
    echo Please install Node.js and add it to your PATH
    pause
    exit /b 1
)

echo Using Node from: !NODE_PATH!
echo.

:: Define all dashboards with their paths
set "DASHBOARDS[0]=Finance-Hub|finance-web-dashboard|3020"
set "DASHBOARDS[1]=Accountant|finance-web-dashboard/accountant-dashboard|3021"
set "DASHBOARDS[2]=CFO-Dashboard|finance-web-dashboard/cfo-dashboard|3022"
set "DASHBOARDS[3]=Reports|finance-web-dashboard/reports-dashboard|3023"
set "DASHBOARDS[4]=Finance-Portal|finance-web-portal|3024"
set "DASHBOARDS[5]=HR-Dashboard|../Human-resource/Frontends/Web/hr-web-dashboard|3030"
set "DASHBOARDS[6]=HR-Portal|../Human-resource/Frontends/Web/hr-web-portal|3031"
set "DASHBOARDS[7]=Sales-Dashboard|../Sales-department/Frontends/Web/sales-web-dashboard|3040"
set "DASHBOARDS[8]=Sales-Portal|../Sales-department/Frontends/Web/sales-web-portal|3041"
set "DASHBOARDS[9]=Global-Marketing|../Digital-marketing/Frontends/Web/marketing-web-dashboard/global-marketing-dashboard|3050"
set "DASHBOARDS[10]=Country-Marketing|../Digital-marketing/Frontends/Web/marketing-web-dashboard/country-marketing-dashboard|3051"
set "DASHBOARDS[11]=Marketing-Portal|../Digital-marketing/Frontends/Web/marketing-web-portal|3052"
set "DASHBOARDS[12]=Corporate-Admin|../Digital-marketing/Frontends/Web/corporate-website-admin|3053"
set "DASHBOARDS[13]=CEO-Dashboard|../Executive-domain/Frontends/Web/ceo-web-dashboard|3010"
set "DASHBOARDS[14]=C-Suite-Hub|../Executive-domain/Frontends/Web/executive-web-dashboard|3011"
set "DASHBOARDS[15]=Support-Dashboard|../Customer-support/Frontends/Web/support-web-dashboard|3060"
set "DASHBOARDS[16]=Support-Portal|../Customer-support/Frontends/Web/support-web-portal|3061"

:: Start each dashboard in a new window
for /L %%i in (0,1,16) do (
    for /f "tokens=1-3 delims=|" %%a in ("!DASHBOARDS[%%i]!") do (
        set "NAME=%%a"
        set "PATH_REL=%%b"
        set "PORT=%%c"

        echo Starting [!NAME!] on port !PORT!...
        start "Gogidix - !NAME! - !PORT!" cmd /k "cd /d "!BASE_DIR!!PATH_REL!" && title Gogidix - !NAME! - Port !PORT! && npm run dev"

        :: Wait a bit between starts
        timeout /t 2 /nobreak >nul
    )
)

echo.
echo ========================================
echo   All dashboards started!
echo ========================================
echo.
echo Launcher Hub: http://localhost:3000
echo.
echo Dashboards are running in separate windows.
echo Close this window to keep dashboards running, or
echo press a key to close this window only.
echo.
pause
