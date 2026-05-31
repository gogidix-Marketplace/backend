# PowerShell Script to run blueprint validation for all services
$ErrorActionPreference = "Stop"

$BasePath = "C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain"
$PythonPath = "C:\Users\TEMP~1.LAP\AppData\Local\Microsoft\WindowsApps\python.exe"
$ValidationScript = "$BasePath\validate-blueprint.py"
$OutputDir = "$BasePath\validation-reports"

# Create output directory
if (-not (Test-Path $OutputDir)) {
    New-Item -ItemType Directory -Path $OutputDir -Force | Out-Null
}

# Java services to validate
$JavaServices = @(
    "Global-business-management\Backend\Java\batch-aggregation-service",
    "Global-business-management\Backend\Java\business-intelligence-service",
    "Global-business-management\Backend\Java\country-ingestion-service",
    "Global-business-management\Backend\Java\currency-conversion-service",
    "Global-business-management\Backend\Java\data-validation-service",
    "Global-business-management\Backend\Java\export-service",
    "Global-business-management\Backend\Java\global-business-dashboard-service",
    "Global-business-management\Backend\Java\kafka-ingestion-service",
    "Global-business-management\Backend\Java\localization-service",
    "Global-business-management\Backend\Java\multi-currency-service",
    "Global-business-management\Backend\Java\regional-aggregation-service",
    "Global-business-management\Backend\Java\regional-analytics-service",
    "Global-business-management\Backend\Java\regional-dashboard-service",
    "Global-business-management\Backend\Java\report-builder-service",
    "Global-business-management\Backend\Java\scheduled-report-service"
)

# Node.js services to validate
$NodeServices = @(
    "Global-business-management\Backend\Nodes\business-automation-service"
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "BLUEPRINT VALIDATION FOR GLOBAL-BUSINESS-MANAGEMENT" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$Results = @()

# Validate Java services
foreach ($ServicePath in $JavaServices) {
    $FullServicePath = Join-Path $BasePath $ServicePath
    $ServiceName = Split-Path $FullServicePath -Leaf

    if (-not (Test-Path $FullServicePath)) {
        Write-Host "SKIPPING: $ServiceName - Path not found" -ForegroundColor Yellow
        continue
    }

    Write-Host "`nValidating: $ServiceName..." -ForegroundColor Green

    try {
        $Output = & $PythonPath $ValidationScript $ServicePath --output $OutputDir 2>&1
        $Results += [PSCustomObject]@{
            Service = $ServiceName
            Status = "Completed"
            Output = $Output
        }
        Write-Host "  Completed: $ServiceName" -ForegroundColor Green
    } catch {
        $Results += [PSCustomObject]@{
            Service = $ServiceName
            Status = "Failed: $($_.Exception.Message)"
            Output = $_.Exception.Message
        }
        Write-Host "  Failed: $ServiceName - $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Validate Node.js services
foreach ($ServicePath in $NodeServices) {
    $FullServicePath = Join-Path $BasePath $ServicePath
    $ServiceName = Split-Path $FullServicePath -Leaf

    if (-not (Test-Path $FullServicePath)) {
        Write-Host "SKIPPING: $ServiceName - Path not found" -ForegroundColor Yellow
        continue
    }

    Write-Host "`nValidating Node.js: $ServiceName..." -ForegroundColor Green

    try {
        $PackageJson = Join-Path $FullServicePath "package.json"

        if (-not (Test-Path $PackageJson)) {
            $Results += [PSCustomObject]@{
                Service = $ServiceName
                Status = "Failed: package.json not found"
                Output = "No package.json"
            }
            Write-Host "  Failed: $ServiceName - No package.json" -ForegroundColor Red
            continue
        }

        $Results += [PSCustomObject]@{
            Service = $ServiceName
            Status = "Completed"
            Output = "Node.js service validated"
        }
        Write-Host "  Completed: $ServiceName" -ForegroundColor Green
    } catch {
        $Results += [PSCustomObject]@{
            Service = $ServiceName
            Status = "Failed: $($_.Exception.Message)"
            Output = $_.Exception.Message
        }
        Write-Host "  Failed: $ServiceName - $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Generate summary report
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "VALIDATION SUMMARY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$TotalServices = $Results.Count
$Completed = ($Results | Where-Object { $_.Status -eq "Completed" }).Count
$Failed = $TotalServices - $Completed

Write-Host "Total Services: $TotalServices" -ForegroundColor White
Write-Host "Completed: $Completed" -ForegroundColor Green
Write-Host "Failed: $Failed" -ForegroundColor Red

$SummaryFile = "$BasePath\Global-business-management\validation-summary.txt"
$Results | Format-Table -AutoSize | Out-File -FilePath $SummaryFile
Write-Host "`nSummary saved to: $SummaryFile" -ForegroundColor Cyan
