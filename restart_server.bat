@echo off
echo Deteniendo servidor en puerto 8080...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080') do (
    taskkill /F /PID %%a 2>nul
    goto :continue
)

:continue
echo.
echo Esperando 3 segundos...
timeout /t 3 /nobreak > nul

echo.
echo Aplicando cambios en la base de datos...
cd /d C:\Users\USUARIO\Documents\PlataformaCursos
mysql -u root < add_motivo_rechazo.sql 2>nul

echo.
echo Iniciando servidor...
cd edulearn-api
start cmd /k "mvn spring-boot:run"

echo.
echo Servidor reiniciándose... Espera 20 segundos para que esté listo.
timeout /t 20 /nobreak
echo.
echo ¡Listo! El servidor debería estar funcionando en http://localhost:8080
