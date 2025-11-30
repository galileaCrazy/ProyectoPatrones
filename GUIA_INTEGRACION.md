# 🚀 Guía de Integración de Vistas - PlataformaCursos

## 📁 Estructura Final de Rutas

Después de la integración, tendrás estas rutas:

```
/                           → Redirige a /login o dashboard según sesión
/login                      → Página de login
/registro                   → Página de registro
/dashboard/estudiante       → Dashboard para estudiantes
/dashboard/profesor         → Dashboard para profesores
/dashboard/admin            → Dashboard para administradores
```

## ✅ PASO 1: Ejecutar Script de Copia de Componentes

Abre PowerShell en modo Administrador y ejecuta:

```powershell
cd C:\Users\USUARIO\Documents\PlataformaCursos
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\copiar-componentes.ps1
```

Este script copiará:
- ✅ `/components/*` (todos los componentes UI y de características)
- ✅ `/lib/utils.ts` (funciones utilitarias)
- ✅ `/hooks/*` (hooks personalizados)
- ✅ `components.json` (configuración de shadcn)

## ✅ PASO 2: Instalar Dependencias

```powershell
cd edulearn-frontend
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\instalar-dependencias.ps1
```

Este proceso tomará unos minutos. Instalará aproximadamente **40+ paquetes** necesarios.

### Si prefieres instalar manualmente:

```bash
cd edulearn-frontend

# Componentes Radix UI (base de shadcn/ui)
npm install @radix-ui/react-accordion@1.2.2 @radix-ui/react-alert-dialog@1.1.4 @radix-ui/react-aspect-ratio@1.1.1 @radix-ui/react-avatar@1.1.2 @radix-ui/react-checkbox@1.1.3 @radix-ui/react-collapsible@1.1.2 @radix-ui/react-context-menu@2.2.4 @radix-ui/react-dialog@1.1.4 @radix-ui/react-dropdown-menu@2.1.4 @radix-ui/react-hover-card@1.1.4 @radix-ui/react-label@2.1.1 @radix-ui/react-menubar@1.1.4 @radix-ui/react-navigation-menu@1.2.3 @radix-ui/react-popover@1.1.4 @radix-ui/react-progress@1.1.1 @radix-ui/react-radio-group@1.2.2 @radix-ui/react-scroll-area@1.2.2 @radix-ui/react-select@2.1.4 @radix-ui/react-separator@1.1.1 @radix-ui/react-slider@1.2.2 @radix-ui/react-slot@1.1.1 @radix-ui/react-switch@1.1.2 @radix-ui/react-tabs@1.1.2 @radix-ui/react-toast@1.2.4 @radix-ui/react-toggle@1.1.1 @radix-ui/react-toggle-group@1.1.1 @radix-ui/react-tooltip@1.1.6

# Utilidades de estilo
npm install class-variance-authority@0.7.1 clsx@2.1.1 tailwind-merge@2.5.5 tailwindcss-animate@1.0.7

# Componentes adicionales
npm install cmdk@1.0.4 lucide-react@0.454.0 next-themes@0.4.6 sonner@1.7.4 vaul@0.9.9

# Utilidades
npm install date-fns@4.1.0 recharts@2.15.4 react-day-picker@9.8.0 embla-carousel-react@8.5.1 input-otp@1.4.1 react-resizable-panels@2.1.7

# Formularios
npm install react-hook-form@7.60.0 @hookform/resolvers@3.10.0 zod@3.25.76
```

## ✅ PASO 3: Actualizar globals.css

Abre `src/app/globals.css` y reemplaza su contenido con:

```css
@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  :root {
    --background: 0 0% 100%;
    --foreground: 240 10% 3.9%;
    --card: 0 0% 100%;
    --card-foreground: 240 10% 3.9%;
    --popover: 0 0% 100%;
    --popover-foreground: 240 10% 3.9%;
    --primary: 240 5.9% 10%;
    --primary-foreground: 0 0% 98%;
    --secondary: 240 4.8% 95.9%;
    --secondary-foreground: 240 5.9% 10%;
    --muted: 240 4.8% 95.9%;
    --muted-foreground: 240 3.8% 46.1%;
    --accent: 240 4.8% 95.9%;
    --accent-foreground: 240 5.9% 10%;
    --destructive: 0 84.2% 60.2%;
    --destructive-foreground: 0 0% 98%;
    --border: 240 5.9% 90%;
    --input: 240 5.9% 90%;
    --ring: 240 5.9% 10%;
    --radius: 0.5rem;
    --chart-1: 12 76% 61%;
    --chart-2: 173 58% 39%;
    --chart-3: 197 37% 24%;
    --chart-4: 43 74% 66%;
    --chart-5: 27 87% 67%;
  }

  .dark {
    --background: 240 10% 3.9%;
    --foreground: 0 0% 98%;
    --card: 240 10% 3.9%;
    --card-foreground: 0 0% 98%;
    --popover: 240 10% 3.9%;
    --popover-foreground: 0 0% 98%;
    --primary: 0 0% 98%;
    --primary-foreground: 240 5.9% 10%;
    --secondary: 240 3.7% 15.9%;
    --secondary-foreground: 0 0% 98%;
    --muted: 240 3.7% 15.9%;
    --muted-foreground: 240 5% 64.9%;
    --accent: 240 3.7% 15.9%;
    --accent-foreground: 0 0% 98%;
    --destructive: 0 62.8% 30.6%;
    --destructive-foreground: 0 0% 98%;
    --border: 240 3.7% 15.9%;
    --input: 240 3.7% 15.9%;
    --ring: 240 4.9% 83.9%;
    --chart-1: 220 70% 50%;
    --chart-2: 160 60% 45%;
    --chart-3: 30 80% 55%;
    --chart-4: 280 65% 60%;
    --chart-5: 340 75% 55%;
  }
}

@layer base {
  * {
    @apply border-border;
  }
  body {
    @apply bg-background text-foreground;
  }
}
```

## ✅ PASO 4: Verificar Archivos Creados

Ya he creado estos archivos automáticamente:

### Páginas de Dashboard por Rol:
- ✅ `src/app/dashboard/estudiante/page.tsx`
- ✅ `src/app/dashboard/profesor/page.tsx`
- ✅ `src/app/dashboard/admin/page.tsx`

### Componentes Base:
- ✅ `src/lib/utils.ts`
- ✅ `src/components/theme-provider.tsx`

### Layout Actualizado:
- ✅ `src/app/layout.tsx` (con ThemeProvider integrado)

## ✅ PASO 5: Compilar y Probar

```bash
cd C:\Users\USUARIO\Documents\PlataformaCursos\edulearn-frontend

# Verificar que compile sin errores
npm run build

# Ejecutar en desarrollo
npm run dev
```

Abre en tu navegador: `http://localhost:3000`

## 🧪 PASO 6: Pruebas de Integración

### Prueba 1: Login y Redirección
1. Ve a `/login`
2. Inicia sesión con un usuario estudiante
3. Debes ser redirigido a `/dashboard/estudiante`
4. Verás el Dashboard completo con navegación lateral

### Prueba 2: Navegación entre Vistas
1. En el dashboard, haz clic en "Cursos" en el menú lateral
2. Deberías ver la lista de cursos
3. Prueba navegar a: Calendario, Evaluaciones, Foros

### Prueba 3: Roles Diferentes
1. Cierra sesión
2. Inicia como profesor
3. Verás opciones diferentes en el menú
4. Repite con administrador

## 🎨 PASO 7: Personalización (Opcional)

### Cambiar Colores del Tema

Edita las variables CSS en `globals.css` en la sección `:root` y `.dark`

### Modificar Logo

Coloca tu logo en `/public` y actualiza la referencia en:
`src/components/layout/navigation.tsx`

## 📊 Estructura de Componentes Integrados

```
src/
├── app/
│   ├── dashboard/
│   │   ├── estudiante/
│   │   │   └── page.tsx         ← Dashboard de estudiante
│   │   ├── profesor/
│   │   │   └── page.tsx         ← Dashboard de profesor
│   │   └── admin/
│   │       └── page.tsx         ← Dashboard de admin
│   ├── login/
│   │   └── page.tsx
│   ├── layout.tsx               ← Con ThemeProvider
│   └── page.tsx                 ← Redirige según rol
│
├── components/
│   ├── ui/                      ← ~50 componentes de shadcn
│   ├── auth/
│   │   └── login-page.tsx
│   ├── dashboard/
│   │   ├── dashboard.tsx        ← Orquestador principal
│   │   └── dashboard-content.tsx
│   ├── layout/
│   │   └── navigation.tsx       ← Menú lateral
│   ├── courses/
│   │   ├── courses-list.tsx
│   │   ├── course-detail.tsx
│   │   └── course-builder.tsx
│   ├── students/
│   ├── evaluations/
│   ├── reports/
│   ├── calendar/
│   ├── forums/
│   └── theme-provider.tsx
│
├── lib/
│   └── utils.ts                 ← Función cn() para clases
│
└── hooks/
    ├── use-mobile.ts
    └── use-toast.ts
```

## 🐛 Solución de Problemas Comunes

### Error: "Cannot find module '@/components/...'"

**Solución**: Verifica que el script de copia se ejecutó correctamente:
```powershell
cd C:\Users\USUARIO\Documents\PlataformaCursos
.\copiar-componentes.ps1
```

### Error: "Module not found: Can't resolve 'lucide-react'"

**Solución**: Instala las dependencias:
```bash
cd edulearn-frontend
.\instalar-dependencias.ps1
```

### Error de compilación en componentes

**Solución**: Verifica que `tsconfig.json` tenga el alias configurado:
```json
"paths": {
  "@/*": ["./src/*"]
}
```

### La navegación no cambia de vista

**Solución**: Asegúrate de que el componente Dashboard esté importado correctamente en cada página de rol.

## 📝 Notas Importantes

1. **Datos Mock**: Actualmente los componentes usan datos de prueba (mock data). Para conectar a tu API, necesitarás:
   - Reemplazar los arrays `MOCK_COURSES`, `MOCK_STUDENTS`, etc.
   - Usar `fetch` o `axios` para llamar a tu backend

2. **Autenticación**: El sistema usa `localStorage` para la sesión. En producción, considera usar:
   - JWT tokens
   - Cookies seguras
   - NextAuth.js

3. **Responsividad**: Todos los componentes son responsivos y funcionan en móviles.

4. **Modo Oscuro**: El tema oscuro funciona automáticamente gracias a `next-themes`.

## 🎯 Próximos Pasos Recomendados

1. **Conectar Backend**: Integrar con tu API Laravel
2. **Autenticación Real**: Implementar JWT o NextAuth
3. **Validación de Formularios**: Ya tienes react-hook-form y zod instalados
4. **Testing**: Agregar tests con Jest y React Testing Library
5. **Optimización**: Implementar lazy loading para listas grandes

---

## ✅ Checklist de Integración

- [ ] Ejecutar `copiar-componentes.ps1`
- [ ] Ejecutar `instalar-dependencias.ps1`
- [ ] Actualizar `globals.css`
- [ ] Compilar con `npm run build`
- [ ] Probar login y redirección
- [ ] Probar navegación en dashboard
- [ ] Verificar que cada rol vea su vista
- [ ] Probar modo oscuro/claro
- [ ] Verificar responsividad en móvil

---

¿Necesitas ayuda con algún paso específico? 🚀
