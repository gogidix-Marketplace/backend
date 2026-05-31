@echo off
REM Centralized Maven Configuration Service - Windows Version
REM Optimized for Enterprise Java Microservices Build Performance

REM JVM Memory Settings - Optimized for large multi-module builds
set "MAVEN_OPTS=-Xmx3072m -Xms1024m -XX:MaxPermSize=512m -XX:ReservedCodeCacheSize=512m"

REM Enable parallel builds for faster compilation
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.artifact.threads=4"

REM Network optimization for dependency resolution
set "MAVEN_OPTS=%MAVEN_OPTS% -Dhttp.keepAlive=true"
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.pool=true"
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.pool.size=20"
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.httpconnectionManager.maxTotal=20"
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.httpconnectionManager.maxPerRoute=10"

REM Timeout settings to prevent hanging builds
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.connectionTimeout=300000"
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.readTimeout=1800000"

REM Retry configuration for transient failures
set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.wagon.http.retryHandler.count=3"

REM Enable batch mode for CI/CD environments
if "%CI%"=="true" set "MAVEN_OPTS=%MAVEN_OPTS% -B"
if "%GITLAB_CI%"=="true" set "MAVEN_OPTS=%MAVEN_OPTS% -B"

REM Performance monitoring
set "MAVEN_OPTS=%MAVEN_OPTS% -Dorg.slf4j.simpleLogger.showDateTime=true"
set "MAVEN_OPTS=%MAVEN_OPTS% -Dorg.slf4j.simpleLogger.dateTimeFormat=HH:mm:ss.SSS"

echo [Maven Config Service] Loaded optimized settings for %COMPUTERNAME%
echo [Maven Config Service] MAVEN_OPTS: %MAVEN_OPTS%