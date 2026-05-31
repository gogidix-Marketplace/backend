@echo off
echo ========================================
echo Starting Transaction Orchestration System
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 and set JAVA_HOME
    echo See LOCAL-SETUP.md for details
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven and set MAVEN_HOME
    echo See LOCAL-SETUP.md for details
    pause
    exit /b 1
)

echo Starting all 5 backend services...
echo.

REM Start Audit Trail Service
echo [1/5] Starting Audit Trail Service (Port 8081)...
start "Audit Trail Service" cmd /k "cd Backend\Java\audit-trail-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

REM Start Onboarding Tracker Service
echo [2/5] Starting Onboarding Tracker Service (Port 8082)...
start "Onboarding Tracker Service" cmd /k "cd Backend\Java\onboarding-tracker-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

REM Start Progress Step Service
echo [3/5] Starting Progress Step Service (Port 8083)...
start "Progress Step Service" cmd /k "cd Backend\Java\progress-step-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

REM Start Status Broadcast Service
echo [4/5] Starting Status Broadcast Service (Port 8084)...
start "Status Broadcast Service" cmd /k "cd Backend\Java\status-broadcast-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

REM Start Transaction Monitoring Service
echo [5/5] Starting Transaction Monitoring Service (Port 8085)...
start "Transaction Monitoring Service" cmd /k "cd Backend\Java\transaction-monitoring-service && mvn spring-boot:run"
timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo All backend services starting...
echo ========================================
echo.
echo Services will take 30-60 seconds to fully start.
echo.
echo Access the services at:
echo   - Audit Trail API:     http://localhost:8081
echo   - Onboarding Tracker:  http://localhost:8082
echo   - Progress Step:       http://localhost:8083
echo   - Status Broadcast:    http://localhost:8084
echo   - Transaction Monitor: http://localhost:8085
echo.
echo Starting React frontend...
echo.

REM Start Frontend
echo Starting Web Dashboard (Port 3000)...
start "Web Dashboard" cmd /k "cd Frontends\Web && npm run dev"

echo.
echo ========================================
echo System startup complete!
echo ========================================
echo.
echo Open http://localhost:3000 in your browser
echo.
echo Press any key to exit this window (services will continue running)...
pause >nul
