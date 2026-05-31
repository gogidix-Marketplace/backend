@echo off
echo ========================================
echo Stopping All Services
echo ========================================
echo.

echo Stopping all Java processes...
taskkill /F /FI "WINDOWTITLE eq Audit Trail Service*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Onboarding Tracker Service*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Progress Step Service*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Status Broadcast Service*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Transaction Monitoring Service*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq Web Dashboard*" >nul 2>&1

echo.
echo All services stopped.
echo.
pause
