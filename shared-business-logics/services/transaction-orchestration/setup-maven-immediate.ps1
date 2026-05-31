# Setup Maven Immediately (PowerShell)
# This script sets environment variables for both current session and future sessions

Write-Host "========================================"  -ForegroundColor Cyan
Write-Host "Setup Maven from Desktop"  -ForegroundColor Cyan
Write-Host "========================================"  -ForegroundColor Cyan
Write-Host ""

$MavenPath = "C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\apache-maven-3.9.12"

# Check if Maven exists at that location
if (!(Test-Path $MavenPath)) {
    Write-Host "ERROR: Maven not found at: $MavenPath" -ForegroundColor Red
    Write-Host "Please check the path and try again." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "Found Maven at: $MavenPath" -ForegroundColor Green
Write-Host ""

# Set for current session
$env:MAVEN_HOME = $MavenPath
$env:PATH = $env:PATH + ";$MavenPath\bin"

Write-Host "Environment variables set for CURRENT SESSION:" -ForegroundColor Yellow
Write-Host "  MAVEN_HOME = $env:MAVEN_HOME" -ForegroundColor Green
Write-Host ""

# Set for future sessions (persistent)
try {
    [Environment]::SetEnvironmentVariable("MAVEN_HOME", $MavenPath, "User")
    Write-Host "[OK] MAVEN_HOME set for future sessions" -ForegroundColor Green

    # Get current PATH and add Maven
    $CurrentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
    if ($CurrentPath -notlike "*apache-maven*") {
        [Environment]::SetEnvironmentVariable("PATH", "$CurrentPath;$MavenPath\bin", "User")
        Write-Host "[OK] Maven added to PATH for future sessions" -ForegroundColor Green
    } else {
        Write-Host "[OK] Maven already in PATH for future sessions" -ForegroundColor Green
    }
} catch {
    Write-Host "[ERROR] Failed to set persistent environment variables: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================"  -ForegroundColor Cyan
Write-Host "Testing Maven Installation"  -ForegroundColor Cyan
Write-Host "========================================"  -ForegroundColor Cyan
Write-Host ""

# Test Maven
& "$MavenPath\bin\mvn.cmd" --version

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================"  -ForegroundColor Green
    Write-Host "SUCCESS! Maven is ready to use!"  -ForegroundColor Green
    Write-Host "========================================"  -ForegroundColor Green
    Write-Host ""
    Write-Host "Maven will work in this PowerShell session." -ForegroundColor Yellow
    Write-Host "For other terminals, close and reopen them." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "You can now run: start-all-services.bat" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "ERROR: Maven test failed" -ForegroundColor Red
}

Write-Host ""
Read-Host "Press Enter to exit"
