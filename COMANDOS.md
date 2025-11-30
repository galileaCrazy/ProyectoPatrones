# 🚀 COMANDOS RÁPIDOS - Copiar y Pegar

## Opción 1: Usar Scripts Automáticos (RECOMENDADO)

### Paso 1: Copiar componentes
```powershell
cd C:\Users\USUARIO\Documents\PlataformaCursos
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\copiar-componentes.ps1
```

### Paso 2: Instalar dependencias
```powershell
cd edulearn-frontend
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\instalar-dependencias.ps1
```

### Paso 3: Compilar y ejecutar
```bash
npm run build
npm run dev
```

---

## Opción 2: Comandos Manuales Completos

Si los scripts no funcionan, usa estos comandos:

### 1. Copiar manualmente los archivos

#### Copiar components/ui (todos los componentes)
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\ui" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\ui" /E /I /Y
```

#### Copiar components/auth
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\auth" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\auth" /E /I /Y
```

#### Copiar components/calendar
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\calendar" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\calendar" /E /I /Y
```

#### Copiar components/courses
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\courses" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\courses" /E /I /Y
```

#### Copiar components/dashboard
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\dashboard" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\dashboard" /E /I /Y
```

#### Copiar components/evaluations
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\evaluations" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\evaluations" /E /I /Y
```

#### Copiar components/forums
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\forums" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\forums" /E /I /Y
```

#### Copiar components/layout
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\layout" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\layout" /E /I /Y
```

#### Copiar components/reports
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\reports" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\reports" /E /I /Y
```

#### Copiar components/students
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\components\students" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\components\students" /E /I /Y
```

#### Copiar hooks
```powershell
xcopy "C:\Users\USUARIO\Documents\edu-learn-ui-views\hooks" "C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend\src\hooks" /E /I /Y
```

### 2. Instalar dependencias manualmente

```bash
cd C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend

npm install @radix-ui/react-accordion@1.2.2 @radix-ui/react-alert-dialog@1.1.4 @radix-ui/react-aspect-ratio@1.1.1 @radix-ui/react-avatar@1.1.2 @radix-ui/react-checkbox@1.1.3 @radix-ui/react-collapsible@1.1.2 @radix-ui/react-context-menu@2.2.4 @radix-ui/react-dialog@1.1.4 @radix-ui/react-dropdown-menu@2.1.4 @radix-ui/react-hover-card@1.1.4 @radix-ui/react-label@2.1.1 @radix-ui/react-menubar@1.1.4 @radix-ui/react-navigation-menu@1.2.3 @radix-ui/react-popover@1.1.4 @radix-ui/react-progress@1.1.1 @radix-ui/react-radio-group@1.2.2 @radix-ui/react-scroll-area@1.2.2 @radix-ui/react-select@2.1.4 @radix-ui/react-separator@1.1.1 @radix-ui/react-slider@1.2.2 @radix-ui/react-slot@1.1.1 @radix-ui/react-switch@1.1.2 @radix-ui/react-tabs@1.1.2 @radix-ui/react-toast@1.2.4 @radix-ui/react-toggle@1.1.1 @radix-ui/react-toggle-group@1.1.1 @radix-ui/react-tooltip@1.1.6

npm install class-variance-authority@0.7.1 clsx@2.1.1 tailwind-merge@2.5.5 tailwindcss-animate@1.0.7

npm install cmdk@1.0.4 lucide-react@0.454.0 next-themes@0.4.6 sonner@1.7.4 vaul@0.9.9

npm install date-fns@4.1.0 recharts@2.15.4 react-day-picker@9.8.0 embla-carousel-react@8.5.1 input-otp@1.4.1 react-resizable-panels@2.1.7

npm install react-hook-form@7.60.0 @hookform/resolvers@3.10.0 zod@3.25.76
```

### 3. Compilar y ejecutar
```bash
npm run build
npm run dev
```

---

## 🎯 Verificación Rápida

Después de ejecutar los comandos, verifica:

```bash
# 1. Verificar que existan los componentes
dir src\components\ui
dir src\components\dashboard
dir src\components\layout

# 2. Verificar que se instalaron las dependencias
npm list lucide-react
npm list @radix-ui/react-dialog

# 3. Iniciar servidor
npm run dev
```

Abre: http://localhost:3000

---

## ✅ Checklist Post-Instalación

- [ ] Carpeta `src/components/ui` existe y tiene ~50 archivos
- [ ] Carpeta `src/components/dashboard` existe
- [ ] Carpeta `src/components/layout` existe
- [ ] `node_modules` tiene las dependencias nuevas
- [ ] `npm run dev` inicia sin errores
- [ ] Al abrir localhost:3000 redirige a /login
- [ ] Al hacer login redirige a /dashboard/[rol]
- [ ] El dashboard muestra navegación lateral
- [ ] Puedes cambiar entre vistas (Cursos, Calendario, etc.)

---

## 🐛 Solución Rápida de Errores

### Error: "No se puede cargar porque no se encuentra el módulo"
```powershell
cd edulearn-frontend
npm install
```

### Error: "Cannot find module '@/components/ui/button'"
```powershell
# Verificar que se copiaron los componentes
dir src\components\ui\button.tsx

# Si no existe, volver a copiar
.\copiar-componentes.ps1
```

### Error de compilación
```bash
# Limpiar y reinstalar
rm -rf node_modules
rm package-lock.json
npm install
npm run build
```

---

## 📝 Notas Importantes

1. **Los scripts PowerShell son más rápidos** - Úsalos si es posible
2. **La instalación de dependencias toma 3-5 minutos** - Es normal
3. **Todos los archivos base ya están creados** - Solo falta copiar componentes e instalar deps
4. **Las rutas ya están configuradas** - Funcionarán automáticamente

---

## 🎉 Listo!

Una vez completados los pasos, tendrás:
- ✅ Dashboard completo funcional
- ✅ Navegación por roles
- ✅ Componentes UI profesionales
- ✅ Tema claro/oscuro
- ✅ Todas las vistas integradas

**¡Empieza con el primer comando!** 🚀
