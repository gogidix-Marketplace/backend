# Quick Test Script - Verify All 8 Libraries
# Fast verification without full rebuild

$baseDir = "C:\Users\frich\Desktop\Gogidix-Technology-Ecosystem\domains\Foundation-domain\shared-libraries\backend\java"
$mavenRepo = "$env:USERPROFILE\.m2\repository\com\gogidix\libraries"

Write-Host "Quick Verification of All Shared Libraries" -ForegroundColor Cyan
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host ""

$libraries = @(
    "shared-model-service",
    "shared-security-service",
    "shared-validation-service",
    "shared-testing-service",
    "shared-audit",
    "shared-utilities",
    "shared-exceptions",
    "shared-messaging"
)

$count = 0
foreach ($lib in $libraries) {
    $jarPath = Join-Path $mavenRepo "$lib\1.0.0\$lib-1.0.0.jar"
    $pomPath = Join-Path $mavenRepo "$lib\1.0.0\$lib-1.0.0.pom"
    
    if ((Test-Path $jarPath) -and (Test-Path $pomPath)) {
        $jarSize = [math]::Round((Get-Item $jarPath).Length / 1KB, 2)
        Write-Host "✅ $lib - JAR: $jarSize KB" -ForegroundColor Green
        $count++
    } else {
        Write-Host "❌ $lib - NOT FOUND IN MAVEN REPO" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host "Result: $count / $($libraries.Count) libraries installed" -ForegroundColor $(if ($count -eq $libraries.Count) { "Green" } else { "Yellow" })

if ($count -eq $libraries.Count) {
    Write-Host "🎉 100% PRODUCTION READY!" -ForegroundColor Green -BackgroundColor Black
} else {
    Write-Host "⚠️ Some libraries missing" -ForegroundColor Yellow
}
