# Rename JAR and POM files to match expected artifact IDs
$dirs = @{
    'shared-security' = 'shared-security-service'
    'shared-model' = 'shared-model-service'
    'shared-validation' = 'shared-validation-service'
}

foreach ($target in $dirs.Keys) {
    $source = $dirs[$target]
    $path = "C:\Users\TEMP.LAPTOP-1QDBFFCA\.m2\repository\com\gogidix\shared\$target\1.0.0"

    if (Test-Path $path) {
        Get-ChildItem $path | ForEach-Object {
            if ($_.Name -match $source) {
                $newName = $_.Name -replace $source, $target
                $newPath = Join-Path $_.Directory.FullName $newName
                Rename-Item -Path $_.FullName -NewName $newPath -Force
                Write-Host "Renamed: $($_.Name) -> $newName"
            }
        }
    }
}

Write-Host "Done renaming"
