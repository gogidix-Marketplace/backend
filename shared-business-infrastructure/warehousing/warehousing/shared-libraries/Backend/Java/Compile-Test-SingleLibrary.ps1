# Single Library Test Script
# Usage: .\Compile-Test-SingleLibrary.ps1 -LibraryName "shared-utilities"

param(
    [Parameter(Mandatory=$true)]
    [string]$LibraryName
)

$baseDir = "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\domains\Foundation-domain\shared-libraries\backend\java"
$libPath = Join-Path $baseDir $LibraryName

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Testing Library: $LibraryName" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

if (-not (Test-Path $libPath)) {
    Write-Host "[ERROR] Directory not found: $libPath" -ForegroundColor Red
    exit 1
}

Write-Host "Library Path: $libPath" -ForegroundColor Gray
Write-Host ""

# Step 1: Clean
Write-Host "[STEP 1] Cleaning..." -ForegroundColor Cyan
Push-Location $libPath
& mvn clean -q
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Clean successful" -ForegroundColor Green
} else {
    Write-Host "❌ Clean failed" -ForegroundColor Red
}
Pop-Location

# Step 2: Compile
Write-Host "`n[STEP 2] Compiling..." -ForegroundColor Cyan
Push-Location $libPath
$compileStart = Get-Date
& mvn compile -DskipTests
$compileEnd = Get-Date
$compileDuration = ($compileEnd - $compileStart).TotalSeconds

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilation successful ($([math]::Round($compileDuration, 2))s)" -ForegroundColor Green
} else {
    Write-Host "❌ Compilation failed" -ForegroundColor Red
    Pop-Location
    exit 1
}
Pop-Location

# Step 3: Package JAR
Write-Host "`n[STEP 3] Packaging JAR..." -ForegroundColor Cyan
Push-Location $libPath
& mvn jar:jar -q
if ($LASTEXITCODE -eq 0) {
    $jarFile = Get-ChildItem -Path "$libPath\target" -Filter "*.jar" | Select-Object -First 1
    if ($jarFile) {
        Write-Host "✅ JAR created: $($jarFile.Name) ($([math]::Round($jarFile.Length/1KB, 2)) KB)" -ForegroundColor Green
    } else {
        Write-Host "⚠️ JAR packaging reported success but file not found" -ForegroundColor Yellow
    }
} else {
    Write-Host "❌ JAR packaging failed" -ForegroundColor Red
}
Pop-Location

# Step 4: Run Tests (Optional)
Write-Host "`n[STEP 4] Running tests (optional)..." -ForegroundColor Cyan
Push-Location $libPath
$testOutput = & mvn test 2>&1 | Out-String
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Tests passed" -ForegroundColor Green
    $testOutput | Select-String -Pattern "Tests run:" | ForEach-Object { Write-Host "  $_" -ForegroundColor Gray }
} else {
    Write-Host "⚠️ Tests failed or skipped" -ForegroundColor Yellow
}
Pop-Location

# Step 5: Install to Maven
Write-Host "`n[STEP 5] Installing to Maven repository..." -ForegroundColor Cyan
Push-Location $libPath
& mvn install -DskipTests -q
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Maven install successful" -ForegroundColor Green
} else {
    Write-Host "❌ Maven install failed" -ForegroundColor Red
    Pop-Location
    exit 1
}
Pop-Location

# Step 6: Verify Installation
Write-Host "`n[STEP 6] Verifying Maven repository..." -ForegroundColor Cyan
$mavenRepo = "$env:USERPROFILE\.m2\repository\com\gogidix\libraries"

# Try to find the artifact
$found = $false
Get-ChildItem -Path $mavenRepo -Recurse -Filter "*.jar" | Where-Object { $_.Name -like "$LibraryName*" -or $_.Directory.Name -eq $LibraryName } | ForEach-Object {
    Write-Host "✅ Found: $($_.FullName)" -ForegroundColor Green
    Write-Host "   Size: $([math]::Round($_.Length/1KB, 2)) KB" -ForegroundColor Gray
    $found = $true
}

if (-not $found) {
    Write-Host "⚠️ JAR not found in Maven repository" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "COMPLETE!" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Cyan
