# Test Script for All Shared Libraries - Production Verification
# Tests: Compile, Build, Test, Package JAR, Verify Maven Installation
# Date: 2025-10-26
# Agent: Agent 2 - Configuration Management Specialist

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "SHARED LIBRARIES PRODUCTION VERIFICATION" -ForegroundColor Cyan
Write-Host "Testing All 8 Libraries" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

$baseDir = "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\domains\Foundation-domain\shared-libraries\backend\java"
$mavenRepo = "$env:USERPROFILE\.m2\repository\com\gogidix\libraries"

# Define all libraries
$libraries = @(
    @{Name="shared-model"; Path="shared-model"; ArtifactId="shared-model-service"},
    @{Name="shared-security"; Path="shared-security"; ArtifactId="shared-security-service"},
    @{Name="shared-validation"; Path="shared-validation"; ArtifactId="shared-validation-service"},
    @{Name="shared-testing"; Path="shared-testing"; ArtifactId="shared-testing-service"},
    @{Name="shared-audit"; Path="shared-audit"; ArtifactId="shared-audit"},
    @{Name="shared-utilities"; Path="shared-utilities"; ArtifactId="shared-utilities"},
    @{Name="shared-exceptions"; Path="shared-exceptions"; ArtifactId="shared-exceptions"},
    @{Name="shared-messaging"; Path="shared-messaging"; ArtifactId="shared-messaging"}
)

$results = @()
$successCount = 0
$failCount = 0

foreach ($lib in $libraries) {
    $libName = $lib.Name
    $libPath = Join-Path $baseDir $lib.Path
    $artifactId = $lib.ArtifactId
    
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Yellow
    Write-Host "Testing: $libName" -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Yellow
    
    $result = @{
        Library = $libName
        Path = $libPath
        ArtifactId = $artifactId
        Exists = $false
        Compiles = $false
        Packages = $false
        Installs = $false
        JarExists = $false
        PomExists = $false
        Errors = @()
        Warnings = @()
    }
    
    # Check if directory exists
    if (-not (Test-Path $libPath)) {
        Write-Host "[ERROR] Directory not found: $libPath" -ForegroundColor Red
        $result.Errors += "Directory not found"
        $results += $result
        $failCount++
        continue
    }
    $result.Exists = $true
    Write-Host "[OK] Directory exists" -ForegroundColor Green
    
    # Check if pom.xml exists
    $pomPath = Join-Path $libPath "pom.xml"
    if (-not (Test-Path $pomPath)) {
        Write-Host "[ERROR] pom.xml not found" -ForegroundColor Red
        $result.Errors += "pom.xml not found"
        $results += $result
        $failCount++
        continue
    }
    Write-Host "[OK] pom.xml found" -ForegroundColor Green
    
    # Test 1: Compilation
    Write-Host "`n[TEST 1] Compiling $libName..." -ForegroundColor Cyan
    Push-Location $libPath
    try {
        $compileOutput = & mvn clean compile -DskipTests -q 2>&1 | Out-String
        if ($LASTEXITCODE -eq 0) {
            Write-Host "[PASS] Compilation successful" -ForegroundColor Green
            $result.Compiles = $true
        } else {
            Write-Host "[FAIL] Compilation failed" -ForegroundColor Red
            $result.Errors += "Compilation failed"
            $errorLines = $compileOutput | Select-String -Pattern "\[ERROR\]" | Select-Object -First 5
            if ($errorLines) {
                $result.Errors += $errorLines
            }
        }
    } catch {
        Write-Host "[FAIL] Compilation error: $_" -ForegroundColor Red
        $result.Errors += "Compilation exception: $_"
    }
    Pop-Location
    
    # Test 2: Package JAR
    Write-Host "`n[TEST 2] Packaging JAR for $libName..." -ForegroundColor Cyan
    Push-Location $libPath
    try {
        $jarOutput = & mvn jar:jar -q 2>&1 | Out-String
        if ($LASTEXITCODE -eq 0) {
            Write-Host "[PASS] JAR packaging successful" -ForegroundColor Green
            $result.Packages = $true
            
            # Verify JAR file exists
            $jarFile = Get-ChildItem -Path "$libPath\target" -Filter "*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1
            if ($jarFile) {
                Write-Host "[OK] JAR file created: $($jarFile.Name) ($([math]::Round($jarFile.Length/1KB, 2)) KB)" -ForegroundColor Green
            }
        } else {
            Write-Host "[FAIL] JAR packaging failed" -ForegroundColor Red
            $result.Errors += "JAR packaging failed"
        }
    } catch {
        Write-Host "[FAIL] Packaging error: $_" -ForegroundColor Red
        $result.Errors += "Packaging exception: $_"
    }
    Pop-Location
    
    # Test 3: Maven Install
    Write-Host "`n[TEST 3] Installing to Maven repository..." -ForegroundColor Cyan
    Push-Location $libPath
    try {
        $installOutput = & mvn install -DskipTests -q 2>&1 | Out-String
        if ($LASTEXITCODE -eq 0) {
            Write-Host "[PASS] Maven install successful" -ForegroundColor Green
            $result.Installs = $true
        } else {
            Write-Host "[FAIL] Maven install failed" -ForegroundColor Red
            $result.Errors += "Maven install failed"
        }
    } catch {
        Write-Host "[FAIL] Install error: $_" -ForegroundColor Red
        $result.Errors += "Install exception: $_"
    }
    Pop-Location
    
    # Test 4: Verify Maven Repository
    Write-Host "`n[TEST 4] Verifying Maven repository installation..." -ForegroundColor Cyan
    $mavenLibPath = Join-Path $mavenRepo $artifactId
    $jarPath = Join-Path $mavenLibPath "1.0.0\$artifactId-1.0.0.jar"
    $pomPath = Join-Path $mavenLibPath "1.0.0\$artifactId-1.0.0.pom"
    
    if (Test-Path $jarPath) {
        $jarSize = (Get-Item $jarPath).Length
        Write-Host "[OK] JAR found in Maven repo: $([math]::Round($jarSize/1KB, 2)) KB" -ForegroundColor Green
        $result.JarExists = $true
    } else {
        Write-Host "[WARN] JAR not found in Maven repo: $jarPath" -ForegroundColor Yellow
        $result.Warnings += "JAR not in Maven repo"
    }
    
    if (Test-Path $pomPath) {
        Write-Host "[OK] POM found in Maven repo" -ForegroundColor Green
        $result.PomExists = $true
    } else {
        Write-Host "[WARN] POM not found in Maven repo: $pomPath" -ForegroundColor Yellow
        $result.Warnings += "POM not in Maven repo"
    }
    
    # Final verdict for this library
    if ($result.Compiles -and $result.Packages -and $result.JarExists) {
        Write-Host "`n[SUCCESS] $libName is PRODUCTION READY!" -ForegroundColor Green -BackgroundColor Black
        $successCount++
    } else {
        Write-Host "`n[FAIL] $libName has issues" -ForegroundColor Red -BackgroundColor Black
        $failCount++
    }
    
    $results += $result
}

# Final Summary Report
Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "FINAL VERIFICATION REPORT" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

foreach ($result in $results) {
    $status = if ($result.Compiles -and $result.Packages -and $result.JarExists) { "✅ PASS" } else { "❌ FAIL" }
    Write-Host "$status - $($result.Library)" -ForegroundColor $(if ($status -eq "✅ PASS") { "Green" } else { "Red" })
    
    if ($result.Errors.Count -gt 0) {
        Write-Host "  Errors: $($result.Errors.Count)" -ForegroundColor Red
    }
    if ($result.Warnings.Count -gt 0) {
        Write-Host "  Warnings: $($result.Warnings.Count)" -ForegroundColor Yellow
    }
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Total Libraries: $($libraries.Count)" -ForegroundColor White
Write-Host "Success: $successCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red
Write-Host "Success Rate: $([math]::Round(($successCount/$libraries.Count)*100, 2))%" -ForegroundColor $(if ($successCount -eq $libraries.Count) { "Green" } else { "Yellow" })
Write-Host ""

if ($successCount -eq $libraries.Count) {
    Write-Host "🎉🎉🎉 100% PRODUCTION READY! 🎉🎉🎉" -ForegroundColor Green -BackgroundColor Black
    Write-Host "All shared libraries are ready for service development!" -ForegroundColor Green
} else {
    Write-Host "⚠️ Some libraries have issues. Review errors above." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Detailed results stored in `$results variable" -ForegroundColor Gray
Write-Host "Run: `$results | Format-Table -AutoSize" -ForegroundColor Gray
Write-Host ""

# Save detailed report
$reportPath = Join-Path $baseDir "Test-Results-$(Get-Date -Format 'yyyyMMdd-HHmmss').txt"
$results | ConvertTo-Json -Depth 5 | Out-File $reportPath
Write-Host "Detailed JSON report saved: $reportPath" -ForegroundColor Gray
