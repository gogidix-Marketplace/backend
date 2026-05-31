@echo off
echo ========================================
echo Opening Java and Maven Download Pages
echo ========================================
echo.
echo This will open the official download pages in your browser.
echo.
echo Instructions:
echo.
echo 1. JAVA JDK 21:
echo    - Click "Windows x64 Installer"
echo    - Run the installer with default settings
echo    - Note the installation path (usually: C:\Program Files\Java\jdk-21)
echo.
echo 2. APACHE MAVEN:
echo    - Download the binary zip file (e.g., apache-maven-3.9.6-bin.zip)
echo    - Extract to: C:\Program Files\apache-maven-3.9.6
echo.
echo 3. ENVIRONMENT VARIABLES:
echo    - Open System Properties (Win + X, then System)
echo    - Click "Advanced system settings"
echo    - Click "Environment Variables"
echo    - Add JAVA_HOME = C:\Program Files\Java\jdk-21
echo    - Add MAVEN_HOME = C:\Program Files\apache-maven-3.9.6
echo    - Edit PATH: Add %%JAVA_HOME%%\bin and %%MAVEN_HOME%%\bin
echo.
echo Press any key to open download pages...
pause >nul

echo Opening Java download page...
start https://www.oracle.com/java/technologies/downloads/#java21

timeout /t 2 /nobreak >nul

echo Opening Maven download page...
start https://maven.apache.org/download.cgi

echo.
echo Download pages opened in your browser!
echo.
echo After installing, run setup-environment.bat to configure environment variables automatically.
echo.
pause
