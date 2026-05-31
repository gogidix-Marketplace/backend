# GOGIDIX ROBOCOPY FAILED SERVICES MIGRATION
# Targeted migration of failed services from certified source to destination

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Green
Write-Host "ROBOCOPY FAILED SERVICES MIGRATION" -ForegroundColor Green
Write-Host "Migrating FAILED Services from CERTIFIED SOURCE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Configuration
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
Write-Host "Session: robocopy-failed-services-$timestamp" -ForegroundColor Gray
Write-Host ""

# Source and target paths
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

      # Remove existing target directory first (excluding locked files)
    if (Test-Path $targetServicePath) {
        Write-Host "  Removing target directory (excluding locked files): $targetServicePath" -ForegroundColor Yellow
        try {
            # Try to remove directory, but continue if some files are locked
            Remove-Item -Path $targetServicePath -Recurse -Force -ErrorAction SilentlyContinue
        } catch {
            Write-Host "  Some files locked, continuing with copy..." -ForegroundColor Yellow
        }
    }

    try {
        # Use robocopy with simpler options to avoid permission issues
        Write-Host "  Starting robocopy..." -ForegroundColor Cyan

        # Robocopy options (simplified to avoid permission issues):
        # /E - copy subdirectories including empty ones
        # /COPY:DAT - copy Data, Attributes, Timestamps
        # /R:1 - Retry 1 time
        # /W:5 - Wait 5 seconds between retries
        # /NFL - No file list logging
        /NDL - No directory list logging

        $robocopyResult = robocopy $SourcePath $targetServicePath /E /COPY:DAT /R:1 /W:5 /NFL /NDL

        if ($robocopyResult -ge 8) {
            Write-Host "  ❌ ERROR: Robocopy failed with exit code $robocopyResult" -ForegroundColor Red
            return $false
        } else {
            Write-Host "  ✅ SUCCESS: Service migrated completely" -ForegroundColor Green

            # Count migrated files
            $javaFiles = Get-ChildItem $targetServicePath -Filter "*.java" -Recurse -File
            $pomFiles = Get-ChildItem $targetServicePath -Filter "pom.xml" -Recurse -File

            Write-Host "  Result: $($javaFiles.Count) Java files, $($pomFiles.Count) POM files migrated" -ForegroundColor Green
            return $true
        }
    }
    catch {
        Write-Host "  ❌ ERROR: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Execute failed services migration
Write-Host "Starting targeted migration of FAILED services..." -ForegroundColor Green
Write-Host "Using certified source: $sharedLibrariesSource" -ForegroundColor Cyan
Write-Host "Target destination: $sharedLibrariesTarget" -ForegroundColor Cyan
Write-Host ""

Write-Host "Failed services to migrate:" -ForegroundColor Yellow
foreach ($service in $failedServices) {
    $businessGroup = $businessGroupMapping[$service]
    Write-Host "  - $service -> $businessGroup" -ForegroundColor Gray
}
Write-Host ""

$successCount = 0
$totalServices = $failedServices.Count

# Migrate each failed service
foreach ($serviceName in $failedServices) {
    $businessGroup = $businessGroupMapping[$serviceName]
    $sourceServicePath = Join-Path $sharedLibrariesSource "backend\java\$serviceName"

    if (Robocopy-Migrate-Service $serviceName $sourceServicePath $sharedLibrariesTarget $businessGroup) {
        $successCount++
        Write-Host "✅ $serviceName migration completed" -ForegroundColor Green
    } else {
        Write-Host "❌ $serviceName migration failed" -ForegroundColor Red
    }
}

# Final summary
Write-Host "========================================" -ForegroundColor Green
Write-Host "ROBOCOPY MIGRATION SUMMARY" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "Services: $successCount/$totalServices migrated successfully" -ForegroundColor $(if($successCount -eq $totalServices) {"Green"} else {"Yellow"})

if ($successCount -eq $totalServices) {
    Write-Host "STATUS: SUCCESS - All failed services migrated from certified source!" -ForegroundColor Green
    Write-Host "Next: Re-run production readiness tests to validate 100% production readiness" -ForegroundColor Cyan
} else {
    Write-Host "STATUS: PARTIAL - $($totalServices - $successCount) services still need attention" -ForegroundColor Yellow
}