# 📡 API de Patrones de Diseño - EduLearn
## Documentación de Endpoints REST

---

## 🎯 Estado de Implementación

✅ **Completados:** 2/23 patrones (Singleton, Factory Method)
🏗️ **En progreso:** Estructura lista para 21 patrones más

---

## 🔧 Base URL

```
http://localhost:8080/api
```

---

## 1️⃣ PATRÓN SINGLETON

**Propósito:** Gestionar configuraciones del sistema con instancia única.

### Endpoints

#### GET /configuraciones
Obtener todas las configuraciones (desde caché)

**Respuesta:**
```json
{
  "nombre_sistema": "EduLearn Platform",
  "version": "1.0.0",
  "max_intentos_login": "3",
  "duracion_sesion_minutos": "60",
  "cupo_default": "30",
  "calificacion_minima_aprobacion": "60",
  "permitir_registro_estudiantes": "true",
  "modo_mantenimiento": "false",
  "email_notificaciones": "noreply@edulearn.com",
  "url_base": "http://localhost:3000"
}
```

---

#### GET /configuraciones/completas
Obtener configuraciones completas (desde BD)

**Respuesta:**
```json
[
  {
    "id": 1,
    "clave": "nombre_sistema",
    "valor": "EduLearn Platform",
    "descripcion": "Nombre del sistema LMS",
    "tipo": "STRING"
  },
  ...
]
```

---

#### GET /configuraciones/{clave}
Obtener configuración por clave

**Ejemplo:**
```bash
curl http://localhost:8080/api/configuraciones/nombre_sistema
```

**Respuesta:**
```json
{
  "clave": "nombre_sistema",
  "valor": "EduLearn Platform"
}
```

---

#### PUT /configuraciones/{clave}
Actualizar una configuración

**Body:**
```json
{
  "valor": "nuevo_valor"
}
```

**Ejemplo:**
```bash
curl -X PUT http://localhost:8080/api/configuraciones/cupo_default \
  -H "Content-Type: application/json" \
  -d '{"valor": "50"}'
```

---

#### POST /configuraciones
Crear nueva configuración

**Body:**
```json
{
  "clave": "nueva_config",
  "valor": "valor",
  "descripcion": "Descripción",
  "tipo": "STRING"
}
```

---

#### DELETE /configuraciones/{clave}
Eliminar configuración

```bash
curl -X DELETE http://localhost:8080/api/configuraciones/nueva_config
```

---

#### POST /configuraciones/recargar
Recargar configuraciones desde BD

```bash
curl -X POST http://localhost:8080/api/configuraciones/recargar
```

---

#### GET /configuraciones/estadisticas
Obtener estadísticas del patrón Singleton

**Respuesta:**
```json
{
  "cantidadEnCache": 10,
  "cantidadEnBD": 10,
  "instanciaActiva": true,
  "patron": "Singleton"
}
```

---

#### GET /configuraciones/demo
Demo del patrón Singleton

**Respuesta:**
```json
{
  "patron": "Singleton",
  "proposito": "Garantizar una única instancia de ConfiguracionSistemaManager",
  "ventajas": [
    "Control estricto sobre la instancia única",
    "Acceso global a configuraciones",
    "Caché en memoria para mejor rendimiento",
    "Thread-safe con Double-Checked Locking"
  ],
  "ejemploUso": {
    "obtenerNombreSistema": "EduLearn Platform",
    "obtenerVersion": "1.0.0",
    "cantidadConfiguraciones": 10
  }
}
```

---

## 2️⃣ PATRÓN FACTORY METHOD

**Propósito:** Crear diferentes tipos de notificaciones (EMAIL, SMS, PUSH) sin conocer clases concretas.

### Endpoints

#### POST /notificaciones
Enviar notificación

**Body:**
```json
{
  "tipo": "EMAIL",
  "destinatario": "estudiante@edulearn.com",
  "asunto": "Bienvenido a EduLearn",
  "mensaje": "Gracias por registrarte en nuestra plataforma"
}
```

**Ejemplo:**
```bash
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "EMAIL",
    "destinatario": "estudiante@edulearn.com",
    "asunto": "Bienvenido",
    "mensaje": "Gracias por registrarte"
  }'
```

**Respuesta:**
```json
{
  "id": 1,
  "tipo": "EMAIL",
  "destinatario": "estudiante@edulearn.com",
  "asunto": "Bienvenido",
  "mensaje": "Gracias por registrarte",
  "estado": "ENVIADA",
  "fechaCreacion": "2025-11-29T19:30:00",
  "fechaEnvio": "2025-11-29T19:30:01",
  "intentos": 1,
  "error": null
}
```

**Tipos de notificación:**
- `EMAIL`: Requiere email válido (ej: `usuario@example.com`)
- `SMS`: Requiere teléfono válido (ej: `+573001234567`)
- `PUSH`: Requiere device token (string largo > 20 caracteres)

---

#### POST /notificaciones/multiple
Enviar notificación por múltiples canales

**Body:**
```json
{
  "tipos": ["EMAIL", "SMS", "PUSH"],
  "destinatario": "estudiante@edulearn.com",
  "asunto": "Recordatorio de clase",
  "mensaje": "Tu clase comienza en 1 hora"
}
```

**Respuesta:** Array de notificaciones creadas

---

#### GET /notificaciones
Obtener todas las notificaciones

**Respuesta:**
```json
[
  {
    "id": 1,
    "tipo": "EMAIL",
    "destinatario": "estudiante@edulearn.com",
    "asunto": "Bienvenido",
    "mensaje": "Gracias por registrarte",
    "estado": "ENVIADA",
    "fechaCreacion": "2025-11-29T19:30:00",
    "fechaEnvio": "2025-11-29T19:30:01",
    "intentos": 1,
    "error": null
  },
  ...
]
```

---

#### GET /notificaciones/tipo/{tipo}
Obtener notificaciones por tipo

**Ejemplo:**
```bash
curl http://localhost:8080/api/notificaciones/tipo/EMAIL
```

---

#### GET /notificaciones/estado/{estado}
Obtener notificaciones por estado

**Estados disponibles:**
- `PENDIENTE`
- `ENVIADA`
- `FALLIDA`

**Ejemplo:**
```bash
curl http://localhost:8080/api/notificaciones/estado/ENVIADA
```

---

#### GET /notificaciones/destinatario/{destinatario}
Obtener notificaciones por destinatario

**Ejemplo:**
```bash
curl http://localhost:8080/api/notificaciones/destinatario/estudiante@edulearn.com
```

---

#### POST /notificaciones/{id}/reintentar
Reintentar envío de notificación fallida

```bash
curl -X POST http://localhost:8080/api/notificaciones/1/reintentar
```

---

#### GET /notificaciones/estadisticas
Obtener estadísticas de notificaciones

**Respuesta:**
```json
{
  "total": 100,
  "enviadas": 85,
  "fallidas": 10,
  "pendientes": 5,
  "tasaExito": 85.0,
  "porTipo": {
    "EMAIL": 60,
    "SMS": 25,
    "PUSH": 15
  }
}
```

---

#### GET /notificaciones/demo
Demo del patrón Factory Method

**Respuesta:**
```json
{
  "patron": "Factory Method",
  "proposito": "Crear diferentes tipos de notificaciones sin conocer las clases concretas",
  "ventajas": [
    "Desacoplamiento entre cliente y clases concretas",
    "Fácil extensión con nuevos tipos",
    "Cumple principio Open/Closed",
    "Centraliza lógica de creación"
  ],
  "tiposDisponibles": ["EMAIL", "SMS", "PUSH"],
  "ejemploUso": {
    "descripcion": "Enviar notificación de bienvenida por email",
    "endpoint": "POST /api/notificaciones",
    "body": {
      "tipo": "EMAIL",
      "destinatario": "estudiante@example.com",
      "asunto": "Bienvenido a EduLearn",
      "mensaje": "Gracias por registrarte en nuestra plataforma"
    }
  }
}
```

---

## 🔐 Autenticación

Actualmente los endpoints son públicos. En producción se debe agregar autenticación con JWT.

---

## 📝 Códigos de Estado HTTP

- `200 OK`: Solicitud exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

---

## 🧪 Ejemplos de Uso Completos

### Ejemplo 1: Configurar sistema en modo mantenimiento

```bash
# 1. Verificar estado actual
curl http://localhost:8080/api/configuraciones/modo_mantenimiento

# 2. Activar modo mantenimiento
curl -X PUT http://localhost:8080/api/configuraciones/modo_mantenimiento \
  -H "Content-Type: application/json" \
  -d '{"valor": "true"}'

# 3. Verificar cambio
curl http://localhost:8080/api/configuraciones/modo_mantenimiento
```

### Ejemplo 2: Notificar a estudiante sobre nueva evaluación

```bash
# Enviar por email
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "EMAIL",
    "destinatario": "juan.perez@estudiante.com",
    "asunto": "Nueva evaluación disponible",
    "mensaje": "Se ha publicado la evaluación de Patrones de Diseño. Fecha límite: 2025-12-15"
  }'

# Enviar por múltiples canales
curl -X POST http://localhost:8080/api/notificaciones/multiple \
  -H "Content-Type: application/json" \
  -d '{
    "tipos": ["EMAIL", "SMS"],
    "destinatario": "juan.perez@estudiante.com",
    "asunto": "Nueva evaluación disponible",
    "mensaje": "Evaluación de Patrones de Diseño disponible"
  }'
```

### Ejemplo 3: Monitorear notificaciones

```bash
# Ver estadísticas generales
curl http://localhost:8080/api/notificaciones/estadisticas

# Ver notificaciones fallidas
curl http://localhost:8080/api/notificaciones/estado/FALLIDA

# Reintentar notificación fallida
curl -X POST http://localhost:8080/api/notificaciones/5/reintentar
```

---

## 🚀 Frontend - Próximos Pasos

Se crearán componentes React para consumir estos endpoints:

1. **Panel de Configuraciones** → `/api/configuraciones`
2. **Centro de Notificaciones** → `/api/notificaciones`
3. **Dashboard de Patrones** → Visualizar todos los patrones implementados

---

## 📊 Testing

### Probar Singleton
```bash
# Demo
curl http://localhost:8080/api/configuraciones/demo

# Listar configuraciones
curl http://localhost:8080/api/configuraciones

# Estadísticas
curl http://localhost:8080/api/configuraciones/estadisticas
```

### Probar Factory Method
```bash
# Demo
curl http://localhost:8080/api/notificaciones/demo

# Enviar EMAIL
curl -X POST http://localhost:8080/api/notificaciones \
  -H "Content-Type: application/json" \
  -d '{"tipo":"EMAIL","destinatario":"test@example.com","asunto":"Test","mensaje":"Hola"}'

# Ver enviadas
curl http://localhost:8080/api/notificaciones/estado/ENVIADA
```

---

**Última actualización:** 2025-11-29
**Versión API:** 1.0.0
**Backend:** Spring Boot 3.2.0
**Base de Datos:** MySQL 8.0
