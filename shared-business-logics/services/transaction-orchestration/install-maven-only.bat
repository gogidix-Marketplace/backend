@echo off
echo ========================================
echo Maven Installer (Java 17 Already Installed)
echo ========================================
echo.
echo This script will download and install Apache Maven.
echo Java 17 is already detected on your system.
echo.
echo IMPORTANT: This script requires Administrator privileges.
echo Please right-click and select "Run as administrator"
echo.
pause

REM Check for Administrator privileges
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo.
    echo ERROR: This script must be run as Administrator.
    echo Please right-click the file and select "Run as administrator"
    pause
    exit /b 1
)

echo.
echo Creating installation directory...
set INSTALL_DIR=C:\DevTools
if not exist "%INSTALL_DIR%" mkdir "%INSTALL_DIR%"
cd /d "%INSTALL_DIR%"

echo.
echo ========================================
echo Installing Apache Maven
echo ========================================
echo.

REM Download Maven
echo Downloading Apache Maven 3.9.6...
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip' -OutFile 'maven.zip'}"

if not exist "maven.zip" (
    echo ERROR: Failed to download Maven
    echo Please download manually from: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

echo Extracting Maven...
powershell -Command "Expand-Archive -Path 'maven.zip' -DestinationPath 'C:\Program Files' -Force"

if exist "C:\Program Files\apache-maven-3.9.6\bin\mvn.cmd" (
    echo Maven extracted successfully!
) else (
    echo ERROR: Failed to extract Maven
    pause
    exit /b 1
)

echo.
echo ========================================
echo Setting Environment Variables
echo ========================================
echo.

REM Detect Java installation
set JAVA_HOME=
if exist "C:\Program Files\Java\jdk-17" (
    set JAVA_HOME=C:\Program Files\Java\jdk-17
    echo Found Java 17 at: C:\Program Files\Java\jdk-17
) else if exist "C:\Program Files\Java\jdk-17.0" (
    set JAVA_HOME=C:\Program Files\Java\jdk-17.0
    echo Found Java 17 at: C:\Program Files\Java\jdk-17.0
) else (
    echo Unable to auto-detect Java 17 location.
    echo Please enter your Java installation path:
    set /p JAVA_HOME="Java 17 path: "
)

REM Set MAVEN_HOME
setx MAVEN_HOME "C:\Program Files\apache-maven-3.9.6" /M
if %errorLevel% equ 0 (
    echo MAVEN_HOME set to: C:\Program Files\apache-maven-3.9.6
) else (
    echo WARNING: Failed to set MAVEN_HOME
)

REM Set JAVA_HOME if not already set
if not defined JAVA_HOME+x (
    setx JAVA_HOME "%JAVA_HOME%" /M
    echo JAVA_HOME set to: %JAVA_HOME%
)

REM Add to PATH
setx PATH "%PATH%;C:\Program Files\apache-maven-3.9.6\bin" /M
if %errorLevel% equ 0 (
    echo Added Maven to system PATH
) else (
    echo WARNING: Failed to update PATH
)

echo.
echo ========================================
echo Installation Complete!
echo ========================================
echo.
echo IMPORTANT: You must restart your command prompt/terminal
echo for the environment variables to take effect.
echo.
echo After restarting, verify installations with:
echo   java -version
echo   mvn -version
echo.
echo Cleaning up installation files...
del /q "maven.zip"

echo.
echo Press any key to exit...
pause >nul
