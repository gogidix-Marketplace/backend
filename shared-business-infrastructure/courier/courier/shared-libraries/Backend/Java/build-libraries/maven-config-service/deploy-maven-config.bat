@echo off
REM Automated Maven Configuration Deployment Script - Windows Version
REM Deploys optimized Maven settings to all shared-infrastructure services

setlocal enabledelayedexpansion

set "SCRIPT_DIR=%~dp0"
set "BASE_DIR=C:\Users\frich\Desktop\Gogidix-Technology\CLEAN-SOCIAL-ECOMMERCE-ECOSYSTEM"
set "SHARED_INFRA_DIR=%BASE_DIR%\shared-infrastructure"

echo 🚀 [Maven Config Service] Starting deployment to all services...
echo 📍 Base Directory: %BASE_DIR%
echo 📁 Shared Infrastructure: %SHARED_INFRA_DIR%

REM Verify source files exist
if not exist "%SCRIPT_DIR%mavenrc_pre.bat" (
    echo ❌ ERROR: mavenrc_pre.bat not found in %SCRIPT_DIR%
    exit /b 1
)

if not exist "%SCRIPT_DIR%settings.xml" (
    echo ❌ ERROR: settings.xml not found in %SCRIPT_DIR%
    exit /b 1
)

REM Create global Maven directory if it doesn't exist
set "MAVEN_HOME_DIR=%USERPROFILE%\.m2"
if not exist "%MAVEN_HOME_DIR%" mkdir "%MAVEN_HOME_DIR%"

REM Deploy global settings.xml
echo 📋 Deploying global Maven settings...
copy "%SCRIPT_DIR%settings.xml" "%MAVEN_HOME_DIR%\settings.xml" >nul
echo ✅ Global settings.xml deployed to %MAVEN_HOME_DIR%\settings.xml

REM Initialize counters
set /a successful_deployments=0
set /a failed_deployments=0

echo.
echo 🔄 Deploying to shared-infrastructure services...

REM Deploy to all services in shared-infrastructure
for /d %%d in ("%SHARED_INFRA_DIR%\*") do (
    set "service_name=%%~nxd"
    if not "!service_name!"=="maven-config-service" (
        echo 📦 Processing: !service_name!
        
        REM Deploy mavenrc_pre.bat
        copy "%SCRIPT_DIR%mavenrc_pre.bat" "%%d\mavenrc_pre.bat" >nul
        
        REM Deploy local settings.xml
        if not exist "%%d\.mvn" mkdir "%%d\.mvn"
        copy "%SCRIPT_DIR%settings.xml" "%%d\.mvn\settings.xml" >nul
        
        echo   ✅ !service_name!: Maven configuration deployed
        
        REM Verify deployment
        if exist "%%d\mavenrc_pre.bat" (
            if exist "%%d\.mvn\settings.xml" (
                echo   ✅ !service_name!: Verification passed
                set /a successful_deployments+=1
            ) else (
                echo   ❌ !service_name!: Verification failed
                set /a failed_deployments+=1
            )
        ) else (
            echo   ❌ !service_name!: Verification failed
            set /a failed_deployments+=1
        )
    )
)

REM Deploy to other domains if they exist
for %%domain in (centralized-dashboard haulage-logistics social-commerce) do (
    set "domain_dir=%BASE_DIR%\%%domain"
    if exist "!domain_dir!" (
        echo.
        echo 🔄 Deploying to %%domain services...
        
        for /d %%s in ("!domain_dir!\*") do (
            set "service_name=%%~nxs"
            echo 📦 Processing: !service_name!
            
            REM Deploy mavenrc_pre.bat
            copy "%SCRIPT_DIR%mavenrc_pre.bat" "%%s\mavenrc_pre.bat" >nul
            
            REM Deploy local settings.xml
            if not exist "%%s\.mvn" mkdir "%%s\.mvn"
            copy "%SCRIPT_DIR%settings.xml" "%%s\.mvn\settings.xml" >nul
            
            echo   ✅ !service_name!: Maven configuration deployed
            
            REM Verify deployment
            if exist "%%s\mavenrc_pre.bat" (
                if exist "%%s\.mvn\settings.xml" (
                    echo   ✅ !service_name!: Verification passed
                    set /a successful_deployments+=1
                ) else (
                    echo   ❌ !service_name!: Verification failed
                    set /a failed_deployments+=1
                )
            ) else (
                echo   ❌ !service_name!: Verification failed
                set /a failed_deployments+=1
            )
        )
    )
)

REM Performance benchmarking setup
echo.
echo 📊 Setting up performance benchmarking...

(
echo @echo off
echo REM Maven Build Performance Benchmark Script - Windows Version
echo.
echo set "SERVICE_DIR=%%1"
echo if "%%SERVICE_DIR%%"=="" ^(
echo     echo Usage: %%0 ^<service-directory^>
echo     exit /b 1
echo ^)
echo if not exist "%%SERVICE_DIR%%" ^(
echo     echo ERROR: Directory %%SERVICE_DIR%% does not exist
echo     exit /b 1
echo ^)
echo.
echo cd /d "%%SERVICE_DIR%%"
echo for %%i in ^("%%SERVICE_DIR%%"^) do set "SERVICE_NAME=%%~nxi"
echo.
echo echo 🚀 Benchmarking Maven build for: %%SERVICE_NAME%%
echo echo 📍 Directory: %%SERVICE_DIR%%
echo echo ⏰ Start time: %%date%% %%time%%
echo.
echo REM Record start time
echo for /f "delims=" %%%%i in ^('powershell -command "^(Get-Date^).Ticks"'^) do set START_TICKS=%%%%i
echo.
echo REM Run Maven build with timeout
echo timeout 600 mvn clean compile -q --batch-mode
echo if %%errorlevel%%==0 ^(
echo     for /f "delims=" %%%%i in ^('powershell -command "^(Get-Date^).Ticks"'^) do set END_TICKS=%%%%i
echo     powershell -command "Write-Host 'Total duration:' ^([Math]::Round^(^(%%END_TICKS%% - %%START_TICKS%%^) / 10000000, 2^)^) 'seconds'"
echo     echo ✅ Build completed successfully
echo ^) else ^(
echo     echo ❌ Build failed or timed out
echo ^)
echo.
echo echo 🏁 End time: %%date%% %%time%%
) > "%SCRIPT_DIR%benchmark-build.bat"

REM Summary report
echo.
echo 📈 DEPLOYMENT SUMMARY
echo ==================
echo ✅ Successful deployments: %successful_deployments%
echo ❌ Failed deployments: %failed_deployments%
set /a total_processed=%successful_deployments% + %failed_deployments%
echo 📊 Total services processed: %total_processed%

if %failed_deployments%==0 (
    echo.
    echo 🎉 All deployments successful!
    echo 🚀 Maven configuration service is now active across all services
    echo 📋 Global settings: %MAVEN_HOME_DIR%\settings.xml
    echo 🔧 Benchmark tool: %SCRIPT_DIR%benchmark-build.bat
) else (
    echo.
    echo ⚠️  Some deployments failed. Please check the logs above.
)

echo.
echo 🏁 Maven Configuration Service deployment completed!
pause