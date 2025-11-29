Write-Host "========================================" -ForegroundColor Red
Write-Host "   Deteniendo Plataforma EduLearn" -ForegroundColor Red
Write-Host "========================================" -ForegroundColor Red
Write-Host ""

# Detener procesos en puerto 8080 (Backend)
Write-Host "Buscando procesos en puerto 8080 (Backend)..." -ForegroundColor Yellow
$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($port8080) {
    foreach ($conn in $port8080) {
        $processId = $conn.OwningProcess
        $process = Get-Process -Id $processId -ErrorAction SilentlyContinue
        if ($process) {
            Write-Host "Deteniendo proceso: $($process.ProcessName) (PID: $processId)" -ForegroundColor Yellow
            Stop-Process -Id $processId -Force
            Write-Host "[OK] Proceso detenido" -ForegroundColor Green
        }
    }
} else {
    Write-Host "[INFO] No hay procesos usando el puerto 8080" -ForegroundColor Cyan
}

# Detener procesos en puerto 3000 (Frontend)
Write-Host "Buscando procesos en puerto 3000 (Frontend)..." -ForegroundColor Yellow
$port3000 = Get-NetTCPConnection -LocalPort 3000 -ErrorAction SilentlyContinue
if ($port3000) {
    foreach ($conn in $port3000) {
        $processId = $conn.OwningProcess
        $process = Get-Process -Id $processId -ErrorAction SilentlyContinue
        if ($process) {
            Write-Host "Deteniendo proceso: $($process.ProcessName) (PID: $processId)" -ForegroundColor Yellow
            Stop-Process -Id $processId -Force
            Write-Host "[OK] Proceso detenido" -ForegroundColor Green
        }
    }
} else {
    Write-Host "[INFO] No hay procesos usando el puerto 3000" -ForegroundColor Cyan
}

# Detener procesos de Node.js y Java relacionados
Write-Host ""
Write-Host "Buscando procesos de Node.js y Java..." -ForegroundColor Yellow
$nodeProcesses = Get-Process -Name node -ErrorAction SilentlyContinue
$javaProcesses = Get-Process -Name java -ErrorAction SilentlyContinue

if ($nodeProcesses) {
    Write-Host "Deteniendo procesos de Node.js..." -ForegroundColor Yellow
    Stop-Process -Name node -Force -ErrorAction SilentlyContinue
    Write-Host "[OK] Procesos de Node.js detenidos" -ForegroundColor Green
}

if ($javaProcesses) {
    Write-Host "Deteniendo procesos de Java..." -ForegroundColor Yellow
    Stop-Process -Name java -Force -ErrorAction SilentlyContinue
    Write-Host "[OK] Procesos de Java detenidos" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   Limpieza completada!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
pause
