# Script para copiar componentes de edu-learn-ui-views a PlataformaCursos
# Ejecutar desde: C:\Users\USUARIO\Documents\PlataformaCursos

Write-Host "=== Iniciando copia de componentes ===" -ForegroundColor Green

$source = "C:\Users\USUARIO\Documents\edu-learn-ui-views"
$dest = "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src"

# 1. Copiar carpeta components completa
Write-Host "Copiando carpeta components..." -ForegroundColor Yellow
if (Test-Path "$source\components") {
    Copy-Item -Path "$source\components\*" -Destination "$dest\components\" -Recurse -Force
    Write-Host "Componentes copiados correctamente" -ForegroundColor Green
}

# 2. Copiar lib
Write-Host "Copiando lib..." -ForegroundColor Yellow
if (Test-Path "$source\lib") {
    Copy-Item -Path "$source\lib\*" -Destination "$dest\lib\" -Recurse -Force
    Write-Host "Lib copiado correctamente" -ForegroundColor Green
}

# 3. Copiar hooks
Write-Host "Copiando hooks..." -ForegroundColor Yellow
if (Test-Path "$source\hooks") {
    Copy-Item -Path "$source\hooks\*" -Destination "$dest\hooks\" -Recurse -Force
    Write-Host "Hooks copiados correctamente" -ForegroundColor Green
}

# 4. Copiar components.json
Write-Host "Copiando components.json..." -ForegroundColor Yellow
if (Test-Path "$source\components.json") {
    Copy-Item -Path "$source\components.json" -Destination ".\edulearn-frontend\components.json" -Force
    Write-Host "components.json copiado correctamente" -ForegroundColor Green
}

# 5. Copiar archivos de configuracion adicionales si existen
Write-Host "Copiando configuraciones adicionales..." -ForegroundColor Yellow
if (Test-Path "$source\tailwind.config.ts") {
    Copy-Item -Path "$source\tailwind.config.ts" -Destination ".\edulearn-frontend\tailwind.config.ts" -Force
    Write-Host "tailwind.config.ts copiado correctamente" -ForegroundColor Green
}

Write-Host ""
Write-Host "=== Copia completada ===" -ForegroundColor Green
Write-Host "Siguiente paso: Instalar dependencias con npm install" -ForegroundColor Cyan
