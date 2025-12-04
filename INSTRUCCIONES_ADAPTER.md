# Patrón Adapter - Integraciones Externas

## Implementación Completa

Este módulo implementa el patrón de diseño **Adapter** para integrar sistemas externos (videoconferencias y repositorios de archivos) con una interfaz unificada.

## Instrucciones de Instalación

### 1. Ejecutar Script SQL

**IMPORTANTE**: Antes de usar el sistema, debes crear la tabla en la base de datos.

Ejecuta el siguiente comando en tu terminal:

```bash
mysql -u root -p edulearn < edulearn-api/src/main/resources/db/migration/adapter_pattern.sql
```

O entra a MySQL y copia/pega el contenido del archivo:
```bash
mysql -u root -p edulearn
```

Luego copia y pega el contenido de: `edulearn-api/src/main/resources/db/migration/adapter_pattern.sql`

### 2. Reiniciar el Backend

```bash
cd edulearn-api
mvn spring-boot:run
```

### 3. Acceder al Sistema

1. Inicia sesión como **Profesor**
2. Ve a la sección **"Integraciones"** en el menú lateral
3. Selecciona un curso
4. Crea integraciones:
   - **Videoconferencias**: Google Meet o Zoom
   - **Repositorios**: Google Drive o OneDrive

## Estructura de Archivos

### Backend
```
edulearn-api/src/main/java/com/edulearn/
├── patterns/structural/adapter/
│   ├── IntegracionExterna.java (Interfaz común)
│   ├── videoconferencia/
│   │   ├── GoogleMeetAdapter.java
│   │   └── ZoomAdapter.java
│   └── repositorio/
│       ├── GoogleDriveAdapter.java
│       └── OneDriveAdapter.java
├── model/IntegracionCurso.java
├── repository/IntegracionCursoRepository.java
├── service/IntegracionExternaService.java
└── controller/IntegracionExternaController.java
```

### Frontend
```
edulearn-frontend/src/components/integrations/
├── integrations-courses-selector.tsx (Selector de cursos)
└── integrations-manager.tsx (Gestor de integraciones)
```

## Endpoints API

Base URL: `http://localhost:8080/api/integraciones-externas`

### Crear Integración
```http
POST /crear
Content-Type: application/json

{
  "cursoId": 1,
  "profesorId": 2,
  "proveedor": "GOOGLE_MEET",
  "datos": {
    "nombre": "Clase Semanal",
    "descripcion": "Reunión del martes",
    "fechaInicio": "2025-12-10T10:00:00",
    "duracionMinutos": 90
  }
}
```

### Listar Integraciones del Curso
```http
GET /curso/{cursoId}/activas
```

### Obtener Estadísticas
```http
GET /curso/{cursoId}/estadisticas
```

### Eliminar Integración
```http
DELETE /{id}
```

### Registrar Uso
```http
POST /{id}/usar
```

## Proveedores Soportados

### Videoconferencias
- **Google Meet**: Genera URLs de demostración (en producción usaría Google Calendar API)
- **Zoom**: Genera IDs de reunión y passwords (en producción usaría Zoom API)

### Repositorios
- **Google Drive**: Crea enlaces de demostración (en producción usaría Google Drive API)
- **OneDrive**: Genera enlaces de demostración (en producción usaría Microsoft Graph API)

**NOTA IMPORTANTE**: Los enlaces generados son de demostración y apuntan a las páginas principales de cada servicio. Para implementación en producción, se requerirían:
- Credenciales OAuth2 de cada proveedor
- Tokens de acceso y refresh
- Integración real con las APIs oficiales

## Características

✅ Interfaz unificada para todos los proveedores
✅ Simulación funcional (no requiere credenciales reales)
✅ Registro de uso y estadísticas
✅ Persistencia en base de datos
✅ API REST completa
✅ Interfaz de usuario intuitiva
✅ Sin emojis, solo íconos React

## Patrón de Diseño

El **Patrón Adapter** permite que sistemas con interfaces diferentes trabajen juntos:

```
┌─────────────────┐
│ Frontend/Cliente│
└────────┬────────┘
         │
         ▼
┌────────────────────┐
│ IntegracionExterna │ ◄── Interfaz común
│    (Interface)     │
└────────┬───────────┘
         │
    ┌────┴────┬──────────┬─────────┐
    │         │          │         │
    ▼         ▼          ▼         ▼
┌────────┐ ┌────┐ ┌────────┐ ┌────────┐
│ Meet   │ │Zoom│ │ Drive  │ │OneDrive│
│Adapter │ │ A. │ │Adapter │ │Adapter │
└────────┘ └────┘ └────────┘ └────────┘
```

Cada adaptador traduce las llamadas de la interfaz común a las API específicas de cada proveedor.

## Próximos Pasos

Una vez ejecutado el script SQL:
1. Reinicia el backend
2. Accede como profesor
3. Prueba crear integraciones
4. Verifica que se guarden en la BD
5. Prueba abrir los recursos generados

## Troubleshooting

**Error**: "Error al crear integración"
- **Solución**: Asegúrate de haber ejecutado el script SQL

**Error**: "No tienes cursos asignados"
- **Solución**: Asegúrate de que el profesor tiene cursos asignados en el sistema

**Error**: "Cannot read properties of undefined"
- **Solución**: Verifica que el backend esté corriendo en puerto 8080
