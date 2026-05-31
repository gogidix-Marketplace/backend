@echo off
echo ========================================
echo Java 21 and Maven Installer
echo ========================================
echo.
echo This script will download and install:
echo   - Java Development Kit (JDK) 21
echo   - Apache Maven
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
echo Step 1: Installing Java JDK 21
echo ========================================
echo.

REM Download Java JDK 21
echo Downloading Java JDK 21...
powershell -Command "& {[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://download.oracle.com/java/21/latest/jdk-21_windows-x64_bin.exe' -OutFile 'jdk-21-installer.exe'}"

if not exist "jdk-21-installer.exe" (
    echo ERROR: Failed to download Java JDK
    echo Please download manually from: https://www.oracle.com/java/technologies/downloads/#java21
    pause
    exit /b 1
)

echo Installing Java JDK 21...
start /wait jdk-21-installer.exe /s INSTALLDIR=C:\Program Files\Java\jdk-21

if exist "C:\Program Files\Java\jdk-21\bin\java.exe" (
    echo Java JDK 21 installed successfully!
) else (
    echo WARNING: Java installation may not have completed successfully
    echo Please check the installation logs
)

echo.
echo ========================================
echo Step 2: Installing Apache Maven
echo ========================================
echo.

REM Download Maven
echo Downloading Apache Maven...
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
echo Step 3: Setting Environment Variables
echo ========================================
echo.

REM Set JAVA_HOME
setx JAVA_HOME "C:\Program Files\Java\jdk-21" /M
if %errorLevel% equ 0 (
    echo JAVA_HOME set to: C:\Program Files\Java\jdk-21
) else (
    echo WARNING: Failed to set JAVA_HOME
)

REM Set MAVEN_HOME
setx MAVEN_HOME "C:\Program Files\apache-maven-3.9.6" /M
if %errorLevel% equ 0 (
    echo MAVEN_HOME set to: C:\Program Files\apache-maven-3.9.6
) else (
    echo WARNING: Failed to set MAVEN_HOME
)

REM Add to PATH
setx PATH "%PATH%;C:\Program Files\Java\jdk-21\bin;C:\Program Files\apache-maven-3.9.6\bin" /M
if %errorLevel% equ 0 (
    echo Added Java and Maven to system PATH
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
del /q "jdk-21-installer.exe"
del /q "maven.zip"

echo.
echo Press any key to exit...
pause >nul
