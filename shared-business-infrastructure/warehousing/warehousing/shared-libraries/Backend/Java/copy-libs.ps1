$libs = @('shared-exceptions', 'shared-security-service', 'shared-model-service', 'shared-validation-service', 'shared-audit', 'shared-messaging', 'shared-testing', 'shared-utilities')
$baseSrc = 'C:\Users\TEMP.LAPTOP-1QDBFFCA\.m2\repository\com\gogidix\shared\'
$baseDst = 'C:\Users\TEMP.LAPTOP-1QDBFFCA\.m2\repository\com\gogidix\shared\'

foreach ($lib in $libs) {
    $src = $baseSrc + $lib + '\1.0.0-SNAPSHOT'
    $dst = $baseDst + $lib + '\1.0.0'
    if (Test-Path $src) {
        New-Item -ItemType Directory -Force -Path $dst | Out-Null
        Copy-Item -Path "$src\*" -Destination "$dst\" -Force
        Write-Host "Copied $lib"
    } else {
        Write-Host "Not found: $src"
    }
}
Write-Host "Done"
