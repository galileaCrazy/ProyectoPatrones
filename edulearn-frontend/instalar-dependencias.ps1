# Script para instalar todas las dependencias necesarias
# Ejecutar desde: C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend

Write-Host "=== Instalando dependencias de UI ===" -ForegroundColor Green

# Componentes Radix UI
Write-Host ""
Write-Host "Instalando componentes Radix UI..." -ForegroundColor Yellow
npm install @radix-ui/react-accordion@1.2.2 @radix-ui/react-alert-dialog@1.1.4 @radix-ui/react-aspect-ratio@1.1.1 @radix-ui/react-avatar@1.1.2 @radix-ui/react-checkbox@1.1.3 @radix-ui/react-collapsible@1.1.2 @radix-ui/react-context-menu@2.2.4 @radix-ui/react-dialog@1.1.4 @radix-ui/react-dropdown-menu@2.1.4 @radix-ui/react-hover-card@1.1.4 @radix-ui/react-label@2.1.1 @radix-ui/react-menubar@1.1.4 @radix-ui/react-navigation-menu@1.2.3 @radix-ui/react-popover@1.1.4 @radix-ui/react-progress@1.1.1 @radix-ui/react-radio-group@1.2.2 @radix-ui/react-scroll-area@1.2.2 @radix-ui/react-select@2.1.4 @radix-ui/react-separator@1.1.1 @radix-ui/react-slider@1.2.2 @radix-ui/react-slot@1.1.1 @radix-ui/react-switch@1.1.2 @radix-ui/react-tabs@1.1.2 @radix-ui/react-toast@1.2.4 @radix-ui/react-toggle@1.1.1 @radix-ui/react-toggle-group@1.1.1 @radix-ui/react-tooltip@1.1.6

# Utilidades de estilo
Write-Host ""
Write-Host "Instalando utilidades de estilo..." -ForegroundColor Yellow
npm install class-variance-authority@0.7.1 clsx@2.1.1 tailwind-merge@2.5.5 tailwindcss-animate@1.0.7

# Componentes adicionales
Write-Host ""
Write-Host "Instalando componentes adicionales..." -ForegroundColor Yellow
npm install cmdk@1.0.4 lucide-react@0.454.0 next-themes@0.4.6 sonner@1.7.4 vaul@0.9.9

# Librerias de utilidad
Write-Host ""
Write-Host "Instalando librerias de utilidad..." -ForegroundColor Yellow
npm install date-fns@4.1.0 recharts@2.15.4 react-day-picker@9.8.0 embla-carousel-react@8.5.1 input-otp@1.4.1 react-resizable-panels@2.1.7

# Formularios y validacion
Write-Host ""
Write-Host "Instalando formularios y validacion..." -ForegroundColor Yellow
npm install react-hook-form@7.60.0 @hookform/resolvers@3.10.0 zod@3.25.76

Write-Host ""
Write-Host "=== Instalacion completada ===" -ForegroundColor Green
Write-Host "Ahora puedes ejecutar: npm run dev" -ForegroundColor Cyan
