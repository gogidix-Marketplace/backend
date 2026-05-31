# Rename files to remove -SNAPSHOT suffix from version
$pathBase = "C:\Users\TEMP.LAPTOP-1QDBFFCA\.m2\repository\com\gogidix\shared"

$dirs = @('shared-security', 'shared-model', 'shared-validation', 'shared-exceptions', 'shared-audit', 'shared-messaging', 'shared-utilities')

foreach ($dir in $dirs) {
    $path = "$pathBase\$dir\1.0.0"

    if (Test-Path $path) {
        Get-ChildItem $path -Filter '*-SNAPSHOT*' | ForEach-Object {
            $newName = $_.Name -replace '-SNAPSHOT', ''
            $newPath = Join-Path $_.Directory.FullName $newName

            # Delete existing file if it exists (usually .lastUpdated files)
            if (Test-Path $newPath) {
                Remove-Item $newPath -Force
            }

            Rename-Item -Path $_.FullName -NewName $newPath -Force
            Write-Host "Renamed: $($_.Name) -> $newName"
        }
    }
}

Write-Host "Done"
