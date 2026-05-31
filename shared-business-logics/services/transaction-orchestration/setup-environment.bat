@echo off
echo ========================================
echo Setup Java and Maven Environment Variables
echo ========================================
echo.
echo This script will help you set up environment variables for Java and Maven.
echo Please make sure you have installed:
echo   - Java JDK 21
echo   - Apache Maven
echo.
echo Default installation paths:
echo   Java:   C:\Program Files\Java\jdk-21
echo   Maven:  C:\Program Files\apache-maven-3.9.6
echo.
echo If you installed to different locations, you'll need to update manually.
echo.
pause

REM Check for Administrator privileges
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo WARNING: Not running as administrator.
    echo Environment variables will be set for current user only.
    echo For system-wide settings, run as administrator.
    echo.
)

echo.
echo Checking for Java installation...
if exist "C:\Program Files\Java\jdk-21\bin\java.exe" (
    echo Found Java at: C:\Program Files\Java\jdk-21
    set JAVA_PATH=C:\Program Files\Java\jdk-21
) else (
    echo Java not found at default location.
    echo Please enter your Java installation path:
    set /p JAVA_PATH="Java path: "
)

echo.
echo Checking for Maven installation...
if exist "C:\Program Files\apache-maven-3.9.6\bin\mvn.cmd" (
    echo Found Maven at: C:\Program Files\apache-maven-3.9.6
    set MAVEN_PATH=C:\Program Files\apache-maven-3.9.6
) else (
    echo Maven not found at default location.
    echo Please enter your Maven installation path:
    set /p MAVEN_PATH="Maven path: "
)

echo.
echo Setting environment variables...

REM Set JAVA_HOME
setx JAVA_HOME "%JAVA_PATH%"
if %errorLevel% equ 0 (
    echo ✓ JAVA_HOME = %JAVA_PATH%
) else (
    echo ✗ Failed to set JAVA_HOME
)

REM Set MAVEN_HOME
setx MAVEN_HOME "%MAVEN_PATH%"
if %errorLevel% equ 0 (
    echo ✓ MAVEN_HOME = %MAVEN_PATH%
) else (
    echo ✗ Failed to set MAVEN_HOME
)

REM Add to PATH
setx PATH "%PATH%;%JAVA_PATH%\bin;%MAVEN_PATH%\bin"
if %errorLevel% equ 0 (
    echo ✓ Added Java and Maven to PATH
) else (
    echo ✗ Failed to update PATH
)

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo IMPORTANT: Close and reopen your command prompt/terminal
echo for the environment variables to take effect.
echo.
echo Then verify with:
echo   java -version
echo   mvn -version
echo.
pause
