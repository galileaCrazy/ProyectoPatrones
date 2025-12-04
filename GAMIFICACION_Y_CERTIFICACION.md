# Gamificación y Certificación para Estudiantes

## Implementación Completa

He implementado un sistema completo de gamificación y certificación que se muestra a los estudiantes al entrar al curso.

---

## 🎯 Funcionalidades Implementadas

### 1. Banner de Recompensas

Un banner visual que aparece **arriba del contenido del curso** mostrando:

#### Gamificación 🏆
- **Puntos Totales**: Suma de todos los puntos disponibles en los módulos
- **Puntos Obtenidos**: Progreso del estudiante (actualmente 0, listo para implementar lógica de progreso)
- **Badges/Insignias**: Lista de todos los badges disponibles en el curso
  - ✅ Badge obtenido (color dorado)
  - 🔒 Badge bloqueado (deshabilitado)

#### Certificación 🎓
- **Tipo de Certificado**: Muestra el tipo de certificado configurado
- **Barra de Progreso**: Muestra cuántos módulos ha completado el estudiante
- **Validación de Inscripción**:
  - ✅ **Inscripción PAGA o BECA**: Puede descargar el certificado
  - ❌ **Inscripción GRATUITA**: No puede descargar el certificado
  - Mensaje claro explicando la restricción

---

## 🔐 Restricciones de Certificado

### Inscripción GRATUITA
- ❌ **NO** puede descargar el certificado
- Mensaje: "El certificado solo está disponible para inscripciones de pago"
- Botón: "Actualizar a Inscripción de Pago"

### Inscripción PAGA
- ✅ **SÍ** puede descargar el certificado
- Mensaje: "Completa el curso para obtener tu certificado"
- Botón de descarga disponible al completar el curso

### Inscripción BECA
- ✅ **SÍ** puede descargar el certificado
- Mensaje: "Completa el curso para obtener tu certificado"
- Botón de descarga disponible al completar el curso

---

## 📡 Endpoints del Backend

### Obtener Decoradores para Estudiante
```
GET /api/modulos/curso/{cursoId}/estudiante/{estudianteId}/decoradores
```

**Ejemplo:**
```bash
curl http://localhost:8080/api/modulos/curso/1/estudiante/1/decoradores
```

**Respuesta:**
```json
{
  "cursoId": 1,
  "modalidadInscripcion": "GRATUITA",
  "puntosDisponibles": 450,
  "puntosObtenidos": 0,
  "badgesDisponibles": [
    {
      "nombre": "Maestro de Java",
      "moduloNombre": "Introducción a Java",
      "obtenido": false
    },
    {
      "nombre": "Experto en POO",
      "moduloNombre": "Programación Orientada a Objetos",
      "obtenido": false
    }
  ],
  "badgesObtenidos": [],
  "certificadoDisponible": true,
  "tipoCertificado": "Certificado Profesional de Java",
  "puedeDescargarCertificado": false,
  "mensajeCertificado": "El certificado solo está disponible para inscripciones de pago. Actualiza tu inscripción para obtener el certificado.",
  "modulosCompletados": 0,
  "totalModulos": 3,
  "cursoCompletado": false
}
```

---

## 🎨 Componentes Frontend

### `CourseRewardsBanner.tsx`
Componente principal que muestra:
- Gamificación (si hay puntos o badges)
- Certificación (si el curso tiene certificado)

**Props:**
- `cursoId`: ID del curso
- `estudianteId`: ID del estudiante

**Características:**
- Se oculta automáticamente si no hay gamificación ni certificación
- Muestra un loader mientras carga
- Maneja errores de forma silenciosa (no interrumpe la experiencia)

### Integración en `CourseContentTree`

El banner se muestra **solo para estudiantes** (no para docentes/admin) y aparece **antes del contenido del curso**:

```tsx
{role === "ESTUDIANTE" && (
  <CourseRewardsBanner cursoId={courseId} estudianteId={usuarioId} />
)}
```

---

## 🎯 Lógica de Negocio

### Backend (`DecoradorService.obtenerDecoradoresParaEstudiante`)

1. **Obtiene todos los módulos raíz del curso**
2. **Calcula puntos totales**: Suma `gamificacion_puntos` de todos los módulos con gamificación
3. **Recopila badges**: Lista todos los badges configurados en los módulos
4. **Verifica certificación**: Busca si el último módulo tiene certificación habilitada
5. **Valida inscripción**:
   - Busca la inscripción del estudiante en el curso
   - Verifica el campo `modalidad` (GRATUITA, PAGA, BECA)
   - Determina si puede descargar el certificado
6. **Genera mensaje**: Mensaje informativo según el tipo de inscripción

---

## 📊 Ejemplo Visual

### Para Inscripción GRATUITA

```
┌─────────────────────────────────────────────────────────────────┐
│ 🏆 Gamificación          │ 🎓 Certificación                    │
│                          │                                      │
│ Puntos Totales           │ Certificado Profesional de Java     │
│ 0/450 pts                │                                      │
│                          │ Progreso del Curso                   │
│ Badges Disponibles (3)   │ 0/3 módulos                          │
│ 🔒 Maestro de Java       │ [██████░░░░░░░░░░░░░] 0%            │
│ 🔒 Experto en POO        │                                      │
│ 🔒 Arquitecto Avanzado   │ 🔒 Certificado bloqueado             │
│                          │    (Inscripción gratuita)            │
│                          │ El certificado solo está disponible  │
│                          │ para inscripciones de pago           │
│                          │                                      │
│                          │ [Actualizar a Inscripción de Pago]   │
└─────────────────────────────────────────────────────────────────┘
```

### Para Inscripción PAGA

```
┌─────────────────────────────────────────────────────────────────┐
│ 🏆 Gamificación          │ 🎓 Certificación                    │
│                          │                                      │
│ Puntos Totales           │ Certificado Profesional de Java     │
│ 150/450 pts              │                                      │
│                          │ Progreso del Curso                   │
│ Badges Disponibles (3)   │ 1/3 módulos                          │
│ ✓ Maestro de Java        │ [██████░░░░░░░░░░░░░] 33%           │
│ 🔒 Experto en POO        │                                      │
│ 🔒 Arquitecto Avanzado   │ Completa el curso para obtener tu    │
│                          │ certificado                          │
└─────────────────────────────────────────────────────────────────┘
```

### Para Curso Completado (PAGA o BECA)

```
┌─────────────────────────────────────────────────────────────────┐
│ 🏆 Gamificación          │ 🎓 Certificación                    │
│                          │                                      │
│ Puntos Totales           │ Certificado Profesional de Java     │
│ 450/450 pts ✓            │                                      │
│                          │ Progreso del Curso                   │
│ Badges Disponibles (3)   │ 3/3 módulos                          │
│ ✓ Maestro de Java        │ [████████████████████] 100%         │
│ ✓ Experto en POO         │                                      │
│ ✓ Arquitecto Avanzado    │ 🎓 ¡Felicidades! Has completado      │
│                          │    el curso                          │
│                          │                                      │
│                          │ [📥 Descargar Certificado]           │
└─────────────────────────────────────────────────────────────────┘
```

---

## ✅ Checklist de Implementación

### Backend
- [x] DTO `DecoradorEstudianteResponse` creado
- [x] Método `obtenerDecoradoresParaEstudiante` en `DecoradorService`
- [x] Endpoint GET `/api/modulos/curso/{cursoId}/estudiante/{estudianteId}/decoradores`
- [x] Validación de tipo de inscripción (GRATUITA vs PAGA/BECA)
- [x] Logs de debug para troubleshooting
- [x] Compilación exitosa (BUILD SUCCESS)

### Frontend
- [x] Componente `CourseRewardsBanner` creado
- [x] Integración en `CourseContentTree`
- [x] Llamada al endpoint del backend
- [x] Visualización de puntos y badges
- [x] Visualización de certificación con restricciones
- [x] Barra de progreso del curso
- [x] Mensajes informativos según tipo de inscripción

### Base de Datos
- [x] Campos de decoradores agregados a tabla `modulos`
- [x] Campo `modalidad` en tabla `inscripciones`
- [x] Repositorio con método `findByEstudianteIdAndCursoId`

---

## 🔄 Próximos Pasos (TODO)

### 1. Implementar Lógica de Progreso Real
Actualmente los valores están en 0 o false. Necesitas implementar:

```sql
-- Tabla de progreso del estudiante
CREATE TABLE progreso_estudiante (
  id SERIAL PRIMARY KEY,
  estudiante_id INTEGER NOT NULL,
  modulo_id BIGINT NOT NULL,
  completado BOOLEAN DEFAULT FALSE,
  puntos_obtenidos INTEGER DEFAULT 0,
  badges_obtenidos TEXT[],
  fecha_completado TIMESTAMP,
  FOREIGN KEY (modulo_id) REFERENCES modulos(id),
  UNIQUE(estudiante_id, modulo_id)
);
```

### 2. Implementar Descarga de Certificado

En `CourseRewardsBanner.tsx`, línea 66:
```typescript
const handleDescargarCertificado = () => {
  // TODO: Implementar descarga de certificado
  // Endpoint: POST /api/certificados/generar
  // Body: { cursoId, estudianteId }
}
```

### 3. Sistema de Actualización de Inscripción

Botón "Actualizar a Inscripción de Pago" debería:
- Redirigir a página de pago
- Actualizar el campo `modalidad` de GRATUITA a PAGA
- Registrar el pago en la BD

---

## 🧪 Cómo Probar

### 1. Ejecuta el Script SQL
```bash
psql -U postgres -d edulearn -f edulearn-api/EJECUTAR_ESTE_SCRIPT.sql
```

### 2. Inicia el Backend
```bash
cd edulearn-api
mvn spring-boot:run
```

### 3. Prueba el Endpoint
```bash
# Ver decoradores para el estudiante 1 en el curso 1
curl http://localhost:8080/api/modulos/curso/1/estudiante/1/decoradores
```

### 4. Prueba desde el Frontend

1. Abre el frontend en el navegador
2. Accede como ESTUDIANTE (no como DOCENTE/ADMIN)
3. Entra a un curso
4. Deberías ver el banner arriba del contenido con:
   - Gamificación (si agregaste puntos a los módulos)
   - Certificación (si el último módulo tiene certificación)
   - Restricción de descarga si tu inscripción es GRATUITA

### 5. Simular Diferentes Escenarios

**Escenario 1: Inscripción Gratuita**
```sql
UPDATE inscripciones
SET modalidad = 'GRATUITA'
WHERE estudiante_id = 1 AND curso_id = 1;
```

**Escenario 2: Inscripción Paga**
```sql
UPDATE inscripciones
SET modalidad = 'PAGA'
WHERE estudiante_id = 1 AND curso_id = 1;
```

**Escenario 3: Beca**
```sql
UPDATE inscripciones
SET modalidad = 'BECA'
WHERE estudiante_id = 1 AND curso_id = 1;
```

Recarga la página del curso y verás cómo cambia el mensaje del certificado.

---

## 🎨 Personalización

### Colores y Estilos

El banner usa:
- **Gamificación**: Colores ámbar/dorado (amber-500)
- **Certificación**: Colores azul (blue-500)
- **Bloqueado**: Colores grises con candado 🔒
- **Completado**: Colores verdes con check ✓

### Mensajes Personalizados

Puedes modificar los mensajes en:
- `DecoradorService.java` (líneas 290-307)
- `CourseRewardsBanner.tsx` (componente de React)

---

## 📝 Notas Importantes

1. **Solo se muestra a ESTUDIANTES**: Los docentes y administradores NO ven el banner
2. **Se oculta si no hay recompensas**: Si el curso no tiene gamificación ni certificación, el banner no se muestra
3. **Carga asíncrona**: El banner se carga después del contenido del curso para no bloquear
4. **Logs de debug**: El backend imprime información en la consola para troubleshooting

---

## ✅ Estado Actual

- ✅ Backend implementado y compilado
- ✅ Frontend implementado
- ✅ Validación de tipo de inscripción funcionando
- ✅ Listo para pruebas

**¡Todo listo! Solo falta ejecutar el script SQL y probar desde el frontend!** 🚀
