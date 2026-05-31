# GOGIDIX COPY FAILED SERVICES FROM CERTIFIED SOURCE
# Simple file copy approach to avoid robocopy permission issues

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Green
Write-Host "COPY FAILED SERVICES FROM CERTIFIED SOURCE" -ForegroundColor Green
Write-Host "Simple file copy approach for reliable migration" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Configuration
$timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
Write-Host "Session: copy-failed-services-$timestamp" -ForegroundColor Gray
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

# Function to copy a service from source to destination
function Copy-Service {
    param(
        [string]$ServiceName,
        [string]$SourcePath,
        [string]$TargetPath,
        [string]$BusinessGroup
    )

    Write-Host "🔄 Copying: $ServiceName -> $BusinessGroup/$ServiceName" -ForegroundColor Yellow
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

    try {
        # Remove existing target directory if it exists
        if (Test-Path $targetServicePath) {
            Write-Host "  Removing existing directory: $targetServicePath" -ForegroundColor Yellow
            try {
                Remove-Item -Path $targetServicePath -Recurse -Force -ErrorAction Stop
            } catch {
                Write-Host "  Some files locked, continuing with copy..." -ForegroundColor Yellow
                # Force remove by deleting files first then directory
                Get-ChildItem -Path $targetServicePath -Recurse -File -ErrorAction SilentlyContinue | Remove-Item -Force -ErrorAction SilentlyContinue
                Remove-Item -Path $targetServicePath -Recurse -Force -ErrorAction SilentlyContinue
            }
        }

        # Create target directory
        New-Item -ItemType Directory -Path $targetServicePath -Force | Out-Null

        Write-Host "  Starting copy operation..." -ForegroundColor Cyan

        # Use PowerShell's Copy-Item which is more reliable than robocopy
        # Copy all items except target directory to avoid locked files
        Get-ChildItem -Path $SourcePath | Where-Object { $_.Name -ne "target" } | ForEach-Object {
            Copy-Item -Path $_.FullName -Destination $targetServicePath -Recurse -Force
        }

        # Count migrated files
        $javaFiles = Get-ChildItem $targetServicePath -Filter "*.java" -Recurse -File -ErrorAction SilentlyContinue
        $pomFiles = Get-ChildItem $targetServicePath -Filter "pom.xml" -Recurse -File -ErrorAction SilentlyContinue

        Write-Host "  ✅ SUCCESS: Service copied completely" -ForegroundColor Green
        Write-Host "  Result: $($javaFiles.Count) Java files, $($pomFiles.Count) POM files copied" -ForegroundColor Green
        return $true
    }
    catch {
        Write-Host "  ❌ ERROR: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Execute failed services migration
Write-Host "Starting copy of FAILED services from certified source..." -ForegroundColor Green
Write-Host "Source: $sharedLibrariesSource" -ForegroundColor Cyan
Write-Host "Destination: $sharedLibrariesTarget" -ForegroundColor Cyan
Write-Host ""

Write-Host "Failed services to copy:" -ForegroundColor Yellow
foreach ($service in $failedServices) {
    $businessGroup = $businessGroupMapping[$service]
    Write-Host "  - $service -> $businessGroup" -ForegroundColor Gray
}
Write-Host ""

$successCount = 0
$totalServices = $failedServices.Count

# Copy each failed service
foreach ($serviceName in $failedServices) {
    $businessGroup = $businessGroupMapping[$serviceName]
    $sourceServicePath = Join-Path $sharedLibrariesSource "backend\java\$serviceName"

    if (Copy-Service $serviceName $sourceServicePath $sharedLibrariesTarget $businessGroup) {
        $successCount++
        Write-Host "✅ $serviceName copy completed" -ForegroundColor Green
    } else {
        Write-Host "❌ $serviceName copy failed" -ForegroundColor Red
    }
}

# Final summary
Write-Host "========================================" -ForegroundColor Green
Write-Host "COPY MIGRATION SUMMARY" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host "Services: $successCount/$totalServices copied successfully" -ForegroundColor $(if($successCount -eq $totalServices) {"Green"} else {"Yellow"})

if ($successCount -eq $totalServices) {
    Write-Host "STATUS: SUCCESS - All failed services copied from certified source!" -ForegroundColor Green
    Write-Host "Next: Re-run production readiness tests to validate 100% production readiness" -ForegroundColor Cyan
} else {
    Write-Host "STATUS: PARTIAL - $($totalServices - $successCount) services still need attention" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Migration completed at: $(Get-Date)" -ForegroundColor Cyan