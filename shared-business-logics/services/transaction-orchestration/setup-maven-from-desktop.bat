@echo off
echo ========================================
echo Setup Maven from Desktop
echo ========================================
echo.
echo Found Maven at: C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\apache-maven-3.9.12
echo.
echo This will configure Maven to work from your desktop location.
echo.

REM Check for Administrator privileges
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo WARNING: Not running as administrator.
    echo Environment variables will be set for current user only.
    echo For system-wide settings, run as administrator.
    echo.
    pause
)

set MAVEN_PATH=C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\apache-maven-3.9.12

echo.
echo Setting Maven environment variables...
echo.

REM Set MAVEN_HOME
setx MAVEN_HOME "%MAVEN_PATH%"
if %errorLevel% equ 0 (
    echo [OK] MAVEN_HOME = %MAVEN_PATH%
) else (
    echo [ERROR] Failed to set MAVEN_HOME
)

REM Add Maven bin to PATH
setx PATH "%PATH%;%MAVEN_PATH%\bin"
if %errorLevel% equ 0 (
    echo [OK] Added Maven to PATH
) else (
    echo [ERROR] Failed to update PATH
)

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo IMPORTANT: Close and reopen ALL terminal windows
echo for the environment variables to take effect.
echo.
echo Then verify with:
echo   mvn -version
echo.
echo Expected output:
echo   Apache Maven 3.9.12
echo   Maven home: C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\apache-maven-3.9.12
echo   Java version: 17.0.x
echo.
pause
