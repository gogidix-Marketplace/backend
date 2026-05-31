# ==============================================================================
# Shared-Courier-Core - Complete Validation Script
# ==============================================================================
# Description: Validates all 20 courier services compile, tests pass, and coverage thresholds met
# Usage: .\VALIDATE_COURIER_CORE.ps1
# ==============================================================================

param(
    [switch]$SkipTests,
    [switch]$ContinueOnError,
    [double]$CoverageThreshold = 75.0
)

$ErrorActionPreference = "Continue"
$ScriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
# The script is in Backend\Java, so RootDir should be Backend\Java
$RootDir = $ScriptPath

# Colors for output
function Write-Status {
    param([string]$Message, [string]$Color = "White")
    Write-Host $Message -ForegroundColor $Color
}

function Write-Success { Write-Status @args -Color "Green" }
function Write-Error-Status { Write-Status @args -Color "Red" }
function Write-Warning-Status { Write-Status @args -Color "Yellow" }
function Write-Info { Write-Status @args -Color "Cyan" }
function Write-Header { Write-Status @args -Color "Magenta" }

# ==============================================================================
# INITIALIZATION
# ==============================================================================

Write-Header "`n=============================================================================="
Write-Header "  SHARED-COURIER-CORE - COMPLETE VALIDATION"
Write-Header "==============================================================================`n"

$Timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$LogFile = "$RootDir\VALIDATION_RESULTS_$(Get-Date -Format 'yyyyMMdd_HHmmss').log"

Start-Transcript -Path $LogFile -Force | Out-Null

Write-Info "Started: $Timestamp"
Write-Info "Root Directory: $RootDir"
Write-Info "Coverage Threshold: $CoverageThreshold%"
Write-Info "Log File: $LogFile`n"

# ==============================================================================
# SERVICE LIST
# ==============================================================================

$Services = @(
    @{ Name = "dispatch-core-service"; Path = "Dispatch/dispatch-core-service" },
    @{ Name = "assignment-service"; Path = "Dispatch/assignment-service" },
    @{ Name = "load-balancing-service"; Path = "Dispatch/load-balancing-service" },
    @{ Name = "routing-service"; Path = "Dispatch/routing-service" },
    @{ Name = "availability-service"; Path = "Driver/availability-service" },
    @{ Name = "driver-pool-service"; Path = "Driver/driver-pool-service" },
    @{ Name = "performance-service"; Path = "Driver/performance-service" },
    @{ Name = "commission-service"; Path = "Partner/commission-service" },
    @{ Name = "partner-portal-service"; Path = "Partner/partner-portal-service" },
    @{ Name = "discount-service"; Path = "Pricing/discount-service" },
    @{ Name = "dynamic-pricing-service"; Path = "Pricing/dynamic-pricing-service" },
    @{ Name = "pricing-engine-service"; Path = "Pricing/pricing-engine-service" },
    @{ Name = "public-booking-service"; Path = "PublicAPI/public-booking-service" },
    @{ Name = "public-quote-service"; Path = "PublicAPI/public-quote-service" },
    @{ Name = "public-tracking-service"; Path = "PublicAPI/public-tracking-service" },
    @{ Name = "tenant-config-service"; Path = "Tenant/tenant-config-service" },
    @{ Name = "eta-service"; Path = "Tracking/eta-service" },
    @{ Name = "gps-tracking-service"; Path = "Tracking/gps-tracking-service" },
    @{ Name = "location-service"; Path = "Tracking/location-service" },
    @{ Name = "notification-service"; Path = "Tracking/notification-service" }
)

# ==============================================================================
# RESULTS TRACKING
# ==============================================================================

$Results = [PSCustomObject]@{
    TotalServices = $Services.Count
    Compiled = 0
    CompileFailed = 0
    TestsPassed = 0
    TestsFailed = 0
    CoverageMet = 0
    CoverageBelowThreshold = 0
    JarBuilt = 0
    JarFailed = 0
    Errors = @()
}

# ==============================================================================
# FUNCTIONS
# ==============================================================================

$MavenPath = "C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\apache-maven-3.9.12\bin\mvn.cmd"

function Test-MavenAvailable {
    Write-Info "Checking Maven availability..."
    if (Test-Path $MavenPath) {
        Write-Success "[OK] Maven found at: $MavenPath"
        return $true
    }
    Write-Error-Status "[ERROR] Maven not found at: $MavenPath"
    return $false
}

function Invoke-Maven {
    param([string]$ServicePath, [string]$Arguments)

    $ServiceDir = Join-Path $RootDir $ServicePath
    Push-Location $ServiceDir

    try {
        # Use a temp file to avoid piping issues
        $TempFile = [System.IO.Path]::GetTempFileName()
        & cmd /c "`"$MavenPath`" $Arguments > `"$TempFile`" 2>&1"
        $ExitCode = $LASTEXITCODE
        $Output = Get-Content $TempFile -Raw
        Remove-Item $TempFile -ErrorAction SilentlyContinue
        $Success = $ExitCode -eq 0
        return @{ Success = $Success; Output = $Output; ExitCode = $ExitCode }
    }
    finally {
        Pop-Location
    }
}

function Get-TestCount {
    param([string]$ServicePath)

    $ServiceDir = Join-Path $RootDir $ServicePath
    $TestDir = Join-Path $ServiceDir "src\test\java"

    if (Test-Path $TestDir) {
        $TestFiles = Get-ChildItem -Path $TestDir -Filter "*Test.java" -Recurse -ErrorAction SilentlyContinue
        return $TestFiles.Count
    }

    return 0
}

function Get-JacocoCoverage {
    param([string]$ServicePath)

    $JacocoIndex = Join-Path $RootDir "$ServicePath\target\site\jacoco\index.html"

    if (Test-Path $JacocoIndex) {
        $Content = Get-Content $JacocoIndex -Raw
        if ($Content -match 'Total[^<]*<td[^>]*>(\d+)%</td>') {
            return [int]$Matches[1]
        }
    }

    return $null
}

function Validate-Service {
    param([PSCustomObject]$Service)

    $ServicePath = $Service.Path
    $ServiceName = $Service.Name
    $ServiceDir = Join-Path $RootDir $ServicePath

    Write-Header "`n[$ServiceName]"

    # Check if service directory exists
    if (-not (Test-Path $ServiceDir)) {
        Write-Error-Status "  [FAIL] Directory not found: $ServiceDir"
        $Results.CompileFailed++
        $Results.TestsFailed++
        $Results.JarFailed++
        $Results.Errors += "$ServiceName - Directory not found"
        return
    }

    # Get test count
    $TestCount = Get-TestCount $ServicePath
    Write-Info "  Test Files: $TestCount"

    # ==============================================================================
    # STEP 1: COMPILE
    # ==============================================================================

    Write-Info "  [1/4] Compiling..."
    $CompileResult = Invoke-Maven $ServicePath "clean compile"

    if ($CompileResult.Success) {
        Write-Success "  [OK] Compile: SUCCESS"
        $Results.Compiled++
    }
    else {
        Write-Error-Status "  [FAIL] Compile: FAILED"
        $Results.CompileFailed++
        $Results.Errors += "$ServiceName - Compilation failed"

        if (-not $ContinueOnError) {
            return
        }
    }

    # ==============================================================================
    # STEP 2: RUN TESTS
    # ==============================================================================

    if (-not $SkipTests) {
        Write-Info "  [2/4] Running Tests..."
        $TestResult = Invoke-Maven $ServicePath "test"

        if ($TestResult.Success) {
            Write-Success "  [OK] Tests: PASSED"
            $Results.TestsPassed++
        }
        else {
            Write-Error-Status "  [FAIL] Tests: FAILED"
            $Results.TestsFailed++
            $Results.Errors += "$ServiceName - Tests failed"

            if (-not $ContinueOnError) {
                return
            }
        }

        # ==============================================================================
        # STEP 3: GENERATE COVERAGE REPORT
        # ==============================================================================

        Write-Info "  [3/4] Generating Coverage..."
        $null = Invoke-Maven $ServicePath "jacoco:report"

        $Coverage = Get-JacocoCoverage $ServicePath
        if ($Coverage) {
            if ($Coverage -ge $CoverageThreshold) {
                Write-Success "  [OK] Coverage: $Coverage% (threshold: $CoverageThreshold%)"
                $Results.CoverageMet++
            }
            else {
                Write-Warning-Status "  [WARN] Coverage: $Coverage% (threshold: $CoverageThreshold%) - BELOW"
                $Results.CoverageBelowThreshold++
                $Results.Errors += "$ServiceName - Coverage $Coverage% below $CoverageThreshold%"
            }
        }
        else {
            Write-Warning-Status "  [WARN] Coverage: Report not generated"
            $Results.CoverageBelowThreshold++
        }
    }

    # ==============================================================================
    # STEP 4: BUILD JAR
    # ==============================================================================

    Write-Info "  [4/4] Building JAR..."
    $null = Invoke-Maven $ServicePath "package -DskipTests"

    $TargetDir = Join-Path $ServiceDir "target"
    $JarFiles = Get-ChildItem -Path $TargetDir -Filter "*.jar" -ErrorAction SilentlyContinue | Where-Object { $_.Name -notlike "*.sources.jar" -and $_.Name -notlike "*.javadoc.jar" }

    if ($JarFiles) {
        $JarSize = ($JarFiles | Measure-Object -Property Length -Sum).Sum / 1MB
        Write-Success "  [OK] JAR: Built ($([math]::Round($JarSize, 2)) MB)"
        $Results.JarBuilt++
    }
    else {
        Write-Error-Status "  [FAIL] JAR: Build FAILED"
        $Results.JarFailed++
        $Results.Errors += "$ServiceName - JAR build failed"
    }
}

# ==============================================================================
# VALIDATION EXECUTION
# ==============================================================================

if (-not (Test-MavenAvailable)) {
    Stop-Transcript
    exit 1
}

Write-Header "=============================================================================="
Write-Header "  VALIDATING ALL SERVICES"
Write-Header "=============================================================================="

foreach ($Service in $Services) {
    Validate-Service $Service
}

# ==============================================================================
# FINAL REPORT
# ==============================================================================

Write-Header "`n=============================================================================="
Write-Header "  VALIDATION SUMMARY REPORT"
Write-Header "==============================================================================`n"

Write-Host "Services:" -ForegroundColor Cyan
Write-Host "  Total:      $($Results.TotalServices)" -ForegroundColor White
Write-Host "  Compiled:  $($Results.Compiled) / $($Results.TotalServices)" -ForegroundColor White

Write-Host "  Tests:      $($Results.TestsPassed) passed" -ForegroundColor Green
if ($Results.TestsFailed -gt 0) {
    Write-Host "             $($Results.TestsFailed) failed" -ForegroundColor Red
}

Write-Host "  Coverage:   $($Results.CoverageMet) met threshold ($CoverageThreshold%)" -ForegroundColor Green
if ($Results.CoverageBelowThreshold -gt 0) {
    Write-Host "             $($Results.CoverageBelowThreshold) below" -ForegroundColor Yellow
}

Write-Host "  JARs:       $($Results.JarBuilt) built" -ForegroundColor Green
if ($Results.JarFailed -gt 0) {
    Write-Host "             $($Results.JarFailed) failed" -ForegroundColor Red
}

# ==============================================================================
# STATUS VERDICT
# ==============================================================================

Write-Host "`n"
$AllTestsPassed = ($Results.TestsFailed -eq 0) -and ($Results.TestsPassed -eq $Results.TotalServices)
$AllCoverageMet = $Results.CoverageBelowThreshold -eq 0
$AllJarsBuilt = ($Results.JarFailed -eq 0) -and ($Results.JarBuilt -eq $Results.TotalServices)

if ($AllTestsPassed -and $AllCoverageMet -and $AllJarsBuilt) {
    Write-Header "=============================================================================="
    Write-Success "  *** ALL VALIDATIONS PASSED ***"
    Write-Header "==============================================================================`n"
    Write-Success "All $($Results.TotalServices) services:"
    Write-Success "  - Compile: SUCCESS"
    Write-Success "  - Tests: PASSED"
    Write-Success "  - Coverage: Met $CoverageThreshold% threshold"
    Write-Success "  - JARs: Built successfully"
    Write-Host "`n"
    Write-Success "READY TO PROCEED TO NEXT DOMAIN`n"
}
else {
    Write-Header "=============================================================================="
    Write-Error-Status "  *** VALIDATION FAILED ***"
    Write-Header "==============================================================================`n"

    if (-not $AllTestsPassed) {
        Write-Error-Status "[X] Some tests failed - cannot proceed to next domain"
    }
    if (-not $AllCoverageMet) {
        Write-Error-Status "[X] Some services below coverage threshold - cannot proceed to next domain"
    }
    if (-not $AllJarsBuilt) {
        Write-Error-Status "[X] Some JARs failed to build - cannot proceed to next domain"
    }
    Write-Host "`n"
}

# ==============================================================================
# ERROR DETAILS
# ==============================================================================

if ($Results.Errors.Count -gt 0) {
    Write-Host "`nErrors encountered:" -ForegroundColor Yellow
    foreach ($Error in $Results.Errors) {
        Write-Host "  - $Error" -ForegroundColor Red
    }
    Write-Host "`n"
}

Stop-Transcript | Out-Null

# Exit with appropriate code
if ($AllTestsPassed -and $AllCoverageMet -and $AllJarsBuilt) {
    exit 0
}
else {
    exit 1
}
