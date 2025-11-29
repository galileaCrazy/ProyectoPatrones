Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Iniciando Plataforma EduLearn" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si MySQL está corriendo
Write-Host "Verificando MySQL..." -ForegroundColor Yellow
try {
    $mysqlVersion = mysql --version 2>&1
    if ($LASTEXITCODE -ne 0) { throw }
    Write-Host "[OK] MySQL detectado" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] MySQL no esta instalado o no esta en el PATH" -ForegroundColor Red
    Write-Host "Por favor, instala MySQL e importa el archivo Dump20251120.sql" -ForegroundColor Red
    pause
    exit 1
}

# Verificar si Node.js está instalado
Write-Host "Verificando Node.js..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version 2>&1
    if ($LASTEXITCODE -ne 0) { throw }
    Write-Host "[OK] Node.js $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] Node.js no esta instalado" -ForegroundColor Red
    Write-Host "Por favor, instala Node.js desde https://nodejs.org/" -ForegroundColor Red
    pause
    exit 1
}

# Verificar y localizar Maven
Write-Host "Verificando Maven..." -ForegroundColor Yellow
$mvnPath = $null

# Intentar encontrar Maven en ubicaciones comunes
$possiblePaths = @(
    "mvn",  # En el PATH
    "C:\ProgramData\chocolatey\lib\maven\apache-maven-3.9.11\bin\mvn.cmd",
    "C:\Program Files\Apache\Maven\bin\mvn.cmd",
    "C:\Maven\bin\mvn.cmd"
)

foreach ($path in $possiblePaths) {
    try {
        if ($path -eq "mvn") {
            $testResult = & $path --version 2>&1
        } else {
            if (Test-Path $path) {
                $testResult = & $path --version 2>&1
            } else {
                continue
            }
        }
        if ($LASTEXITCODE -eq 0) {
            $mvnPath = $path
            Write-Host "[OK] Maven detectado en: $mvnPath" -ForegroundColor Green
            break
        }
    } catch {
        continue
    }
}

if (-not $mvnPath) {
    Write-Host "[ERROR] Maven no esta instalado o no se pudo encontrar" -ForegroundColor Red
    Write-Host "Por favor, instala Maven desde https://maven.apache.org/" -ForegroundColor Red
    Write-Host "O ejecuta: choco install maven -y" -ForegroundColor Yellow
    pause
    exit 1
}

Write-Host ""
Write-Host "Todas las dependencias verificadas correctamente!" -ForegroundColor Green
Write-Host ""

# Limpiar puertos ocupados
Write-Host "Verificando puertos 8080 y 3000..." -ForegroundColor Yellow

# Limpiar puerto 8080
$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($port8080) {
    Write-Host "[ADVERTENCIA] Puerto 8080 ocupado. Liberando..." -ForegroundColor Yellow
    foreach ($conn in $port8080) {
        $processId = $conn.OwningProcess
        Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
    }
    Start-Sleep -Seconds 2
    Write-Host "[OK] Puerto 8080 liberado" -ForegroundColor Green
}

# Limpiar puerto 3000
$port3000 = Get-NetTCPConnection -LocalPort 3000 -ErrorAction SilentlyContinue
if ($port3000) {
    Write-Host "[ADVERTENCIA] Puerto 3000 ocupado. Liberando..." -ForegroundColor Yellow
    foreach ($conn in $port3000) {
        $processId = $conn.OwningProcess
        Stop-Process -Id $processId -Force -ErrorAction SilentlyContinue
    }
    Start-Sleep -Seconds 2
    Write-Host "[OK] Puerto 3000 liberado" -ForegroundColor Green
}

Write-Host ""

# Instalar dependencias del frontend si no existen
if (-not (Test-Path "edulearn-frontend\node_modules")) {
    Write-Host "Instalando dependencias del frontend..." -ForegroundColor Yellow
    Push-Location edulearn-frontend
    npm install
    Pop-Location
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Iniciando Backend (Spring Boot)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD\edulearn-api'; Write-Host 'Iniciando Spring Boot API...' -ForegroundColor Green; & '$mvnPath' spring-boot:run"

Write-Host "Esperando 10 segundos para que el backend inicie..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Iniciando Frontend (Next.js)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD\edulearn-frontend'; Write-Host 'Iniciando Next.js Frontend...' -ForegroundColor Green; npm run dev"

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   Plataforma EduLearn Iniciada!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Backend API: " -NoNewline
Write-Host "http://localhost:8080" -ForegroundColor Cyan
Write-Host "Frontend: " -NoNewline
Write-Host "http://localhost:3000" -ForegroundColor Cyan
Write-Host ""
Write-Host "Presiona cualquier tecla para cerrar esta ventana" -ForegroundColor Yellow
Write-Host "(las aplicaciones seguiran corriendo en sus propias ventanas)" -ForegroundColor Yellow
pause
