@echo off
echo === Copiando componentes ===
echo.

set SOURCE=C:\Users\USUARIO\Documents\edu-learn-ui-views
set DEST=C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src

echo Copiando components...
xcopy "%SOURCE%\components" "%DEST%\components" /E /I /Y /Q
echo OK - Components copiados
echo.

echo Copiando lib...
xcopy "%SOURCE%\lib" "%DEST%\lib" /E /I /Y /Q
echo OK - Lib copiado
echo.

echo Copiando hooks...
xcopy "%SOURCE%\hooks" "%DEST%\hooks" /E /I /Y /Q
echo OK - Hooks copiados
echo.

echo Copiando components.json...
copy "%SOURCE%\components.json" ".\edulearn-frontend\components.json" /Y >nul
echo OK - components.json copiado
echo.

echo === Copia completada ===
echo.
echo Siguiente paso: Instalar dependencias
echo cd edulearn-frontend
echo npm install
echo.
pause
