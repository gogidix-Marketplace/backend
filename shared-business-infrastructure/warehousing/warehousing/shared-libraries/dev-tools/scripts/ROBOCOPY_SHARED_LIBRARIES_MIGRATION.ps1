# GOGIDIX ROBOCOPY SHARED LIBRARIES FAILED SERVICES MIGRATION
# Targeted migration of failed services from certified source

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Green
Write-Host "ROBOCOPY SHARED LIBRARIES MIGRATION" -ForegroundColor Green
Write-Host "Migrating FAILED Services from CERTIFIED SOURCE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Configuration
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
Write-Host "Session: robocopy-shared-libraries-$timestamp" -ForegroundColor Gray
Write-Host ""

# Source and target paths - ONLY FOR SHARED LIBRARIES
$sharedLibrariesSource = "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\Gogidix-GitLab-Staging\Foundation-Domain\gogidix-foundation-shared-libraries"
$sharedLibrariesTarget = "C:\Users\frich\Desktop\Gogidix-ecosystem\domains\foundation\shared-libraries"

# Failed services that need migration
$failedServices = @(
    "shared-exceptions",    # Failed: core-libraries
    "shared-model",         # Failed: core-libraries
    "shared-messaging",     # Failed: communication-libraries
    "shared-utilities"      # Failed: utility-libraries
)

# Business group mapping for destination
$businessGroupMapping = @{
    "shared-exceptions" = "core-libraries"
    "shared-model" = "core-libraries"
    "shared-messaging" = "communication-libraries"
    "shared-utilities" = "utility-libraries"
}

# Function to migrate a single service using robocopy
function Robocopy-Migrate-Service {
    param(
        [string]$ServiceName,
        [string]$SourcePath,
        [string]$TargetPath,
        [string]$BusinessGroup
    )

    Write-Host "🔄 Robocopy Migrating: $ServiceName -> $BusinessGroup/$ServiceName" -ForegroundColor Yellow
    Write-Host "  From: $SourcePath" -ForegroundColor Gray
    Write-Host "  To:   $TargetPath" -ForegroundColor Gray

    # Check if source exists
    if (-not (Test-Path $SourcePath)) {
        Write-Host "  ❌ ERROR: Source path not found: $SourcePath" -ForegroundColor Red
        return $false
    }

    # Ensure target business group directory exists
    $targetBusinessGroupPath = Join-Path $TargetPath "backend\java\$BusinessGroup"
    if (-not (Test-Path $targetBusinessGroupPath)) {
        Write-Host "  Creating business group directory: $targetBusinessGroupPath" -ForegroundColor Cyan
        New-Item -ItemType Directory -Path $targetBusinessGroupPath -Force | Out-Null
    }

    $targetServicePath = Join-Path $targetBusinessGroupPath $ServiceName

    # Remove existing target directory first
    if (Test-Path $targetServicePath) {
        Write-Host "  Removing existing target directory: $targetServicePath" -ForegroundColor Yellow
        Remove-Item -Path $targetServicePath -Recurse -Force
    }

    # Ensure target java directory exists
    if (-not (Test-Path $targetJavaPath)) {
        New-Item -ItemType Directory -Path $targetJavaPath -Force | Out-Null
        Write-Host "  Created target java directory" -ForegroundColor Cyan
    }

    try {
        # Use robocopy to copy the service
        Write-Host "  Starting robocopy..." -ForegroundColor Cyan

        # Robocopy options for complete migration:
        # /E - copy subdirectories including empty ones
        # /COPYALL - copy Data, Attributes, Timestamps, Security
        # /R:3 - Retry 3 times
        # /W:5 - Wait 5 seconds between retries
        # /NFL - No file list logging
        # /NDL - No directory list logging
        # /NJH - No job header
        # /NJS - No job summary
        # /NC - No file class

        $robocopyResult = robocopy $SourcePath $targetServicePath /E /COPYALL /R:3 /W:5 /NFL /NDL /NJH /NJS /NC

        if ($robocopyResult -ge 8) {
            Write-Host "  ❌ ERROR: Robocopy failed with exit code $robocopyResult" -ForegroundColor Red
            return $false
        } else {
            Write-Host "  ✅ SUCCESS: Service migrated completely" -ForegroundColor Green

            # Count migrated services
            $services = Get-ChildItem $targetJavaPath -Directory | Where-Object { Test-Path "$($_.FullName)\pom.xml" }
            $javaFiles = Get-ChildItem $targetJavaPath -Filter "*.java" -Recurse -File

            Write-Host "  Result: $($services.Count) services, $($javaFiles.Count) Java files migrated" -ForegroundColor Green
            return $true
        }
    }
    catch {
        Write-Host "  ERROR: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Execute migrations
Write-Host "Starting robocopy foundation domains migration..." -ForegroundColor Green
Write-Host "Note: Using proven robocopy methodology from AI Services" -ForegroundColor Cyan
Write-Host ""

$successCount = 0
$totalDomains = 3

# 1. Centralized Dashboard
if (Robocopy-Migrate-Domain "Centralized Dashboard" $centralizedDashboardSource $centralizedDashboardTarget) {
    $successCount++
}

# 2. Central Configuration
if (Robocopy-Migrate-Domain "Central Configuration" $centralizedConfigSource $centralizedConfigTarget) {
    $successCount++
}

# 3. Shared Libraries
if (Robocopy-Migrate-Domain "Shared Libraries" $sharedLibrariesSource $sharedLibrariesTarget) {
    $successCount++
}

# Final summary
Write-Host "========================================" -ForegroundColor Green
Write-Host "ROBOCOPY MIGRATION SUMMARY" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "Domains: $successCount/$totalDomains migrated successfully" -ForegroundColor $(if($successCount -eq $totalDomains) {"Green"} else {"Yellow"})

if ($successCount -eq $totalDomains) {
    Write-Host "STATUS: SUCCESS - All domains migrated!" -ForegroundColor Green
    Write-Host "Next: Run production readiness tests to validate migration" -ForegroundColor Cyan
} else {
    Write-Host "STATUS: PARTIAL - Some domains need attention" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Robocopy migration complete." -ForegroundColor Cyan
Write-Host ""