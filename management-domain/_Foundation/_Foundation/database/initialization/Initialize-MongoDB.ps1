# =============================================================================
# Quick MongoDB Initialization Script
# Management-Domain - Gogidix Ecosystem
#
# Run this script to initialize MongoDB for Phase 2 development
# =============================================================================

# Prerequisites:
# - MongoDB must be running on localhost:27017
# - mongosh or mongo shell must be installed

# Run initialization
Write-Host "Initializing MongoDB for Management-Domain..." -ForegroundColor Green
Write-Host ""

$scriptPath = Join-Path $PSScriptRoot "init-database.js"

if (Test-Path $scriptPath) {
    Write-Host "Running initialization script..." -ForegroundColor Yellow
    mongosh $scriptPath

    Write-Host ""
    Write-Host "✓ MongoDB initialization complete!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Open mongosh to explore: mongosh" -ForegroundColor White
    Write-Host "2. List databases: show dbs" -ForegroundColor White
    Write-Host "3. Switch database: use management_executive" -ForegroundColor White
    Write-Host "4. Show collections: show collections" -ForegroundColor White
    Write-Host "5. Check indexes: db.approvals.getIndexes()" -ForegroundColor White
    Write-Host ""
    Write-Host "MongoDB is ready for Phase 2 development!" -ForegroundColor Green
} else {
    Write-Host "ERROR: init-database.js not found at: $scriptPath" -ForegroundColor Red
    Write-Host "Please ensure you're running this script from the initialization folder." -ForegroundColor Red
    exit 1
}
