# Patrón Decorator - Módulos Educativos

## Descripción

El patrón Decorator permite añadir nuevas funcionalidades a los módulos educativos de forma dinámica, sin modificar su estructura original. En esta implementación, los módulos pueden ser extendidos con:

- **Gamificación**: Puntos y badges para motivar a los estudiantes
- **Certificación**: Certificados oficiales al completar el módulo

## Estructura del Patrón

### 1. Componente Abstracto (Interfaz)
**`ModuloEducativo.java`**
- Define el contrato base con el método `mostrarContenido()`

### 2. Componente Concreto
**`ModuloBasico.java`**
- Implementación básica de un módulo educativo
- Contiene el contenido base del módulo

### 3. Decorador Abstracto
**`ModuloDecorator.java`**
- Clase abstracta que mantiene una referencia al componente decorado
- Implementa la interfaz `ModuloEducativo`
- Delega las llamadas al componente envuelto

### 4. Decoradores Concretos

**`GamificacionDecorator.java`**
- Añade funcionalidad de gamificación
- Propiedades: `puntos`, `badge`
- Extiende el contenido base con información de gamificación

**`CertificacionDecorator.java`**
- Añade funcionalidad de certificación
- Propiedades: `tipoCertificado`, `certificadoActivo`
- Extiende el contenido base con información de certificación

## API REST

### Endpoints

#### Aplicar Decoradores
```http
POST /api/modulos/{id}/decoradores
Content-Type: application/json

{
  "gamificacionHabilitada": true,
  "gamificacionPuntos": 150,
  "gamificacionBadge": "Maestro de Java",
  "certificacionHabilitada": true,
  "certificacionTipo": "Certificado Profesional",
  "certificacionActiva": true
}
```

**Respuesta:**
```json
{
  "moduloId": 1,
  "nombre": "Introducción a Java",
  "contenidoBase": "Descripción del módulo...",
  "contenidoDecorado": "=== MÓDULO: Introducción a Java ===\n...\n\n--- GAMIFICACIÓN ---\n...\n\n--- CERTIFICACIÓN ---\n...",
  "gamificacionHabilitada": true,
  "gamificacionPuntos": 150,
  "gamificacionBadge": "Maestro de Java",
  "certificacionHabilitada": true,
  "certificacionTipo": "Certificado Profesional",
  "certificacionActiva": true
}
```

#### Obtener Módulo Decorado
```http
GET /api/modulos/{id}/decoradores
```

#### Eliminar Decoradores
```http
DELETE /api/modulos/{id}/decoradores
```

## Frontend

### Componente: `module-decorators.tsx`

Dialog interactivo que permite a los docentes/administradores:

1. **Habilitar/deshabilitar gamificación**
   - Configurar puntos por completar
   - Definir badge o insignia

2. **Habilitar/deshabilitar certificación**
   - Especificar tipo de certificado
   - Activar/desactivar disponibilidad de descarga

### Integración en `course-content-tree.tsx`

- Botón con ícono de estrella (Sparkles) en cada módulo
- Solo visible para DOCENTE/ADMIN en hover
- Al hacer clic, abre el dialog de decoradores
- Recarga automáticamente los módulos después de aplicar cambios

## Base de Datos

### Campos Agregados a la Tabla `modulos`

```sql
-- Gamificación
gamificacion_habilitada BOOLEAN DEFAULT FALSE
gamificacion_puntos INTEGER
gamificacion_badge VARCHAR(100)

-- Certificación
certificacion_habilitada BOOLEAN DEFAULT FALSE
certificacion_tipo VARCHAR(100)
certificacion_activa BOOLEAN DEFAULT FALSE
```

### Migración

Ejecutar el script: `edulearn-api/src/main/resources/db/migration/decorator_pattern.sql`

## Ejemplo de Uso

### Código Java

```java
// Crear módulo base
ModuloEducativo modulo = new ModuloBasico(
    "Introducción a Java",
    "Conceptos básicos de programación en Java"
);

// Añadir gamificación
modulo = new GamificacionDecorator(
    modulo,
    150,  // puntos
    "Maestro de Java"  // badge
);

// Añadir certificación
modulo = new CertificacionDecorator(
    modulo,
    "Certificado Profesional",
    true  // certificado activo
);

// Mostrar contenido decorado
System.out.println(modulo.mostrarContenido());
```

### Desde la Interfaz

1. Navegar a un curso como DOCENTE o ADMIN
2. Pasar el cursor sobre un módulo
3. Hacer clic en el botón con ícono de estrella (Sparkles)
4. Configurar gamificación y/o certificación
5. Hacer clic en "Aplicar Decoradores"

## Ventajas del Patrón

- **No modifica la clase base**: `ModuloBasico` permanece intacto
- **Extensibilidad**: Fácil añadir nuevos decoradores (ej: PrerequisitosDecorator)
- **Composición flexible**: Los decoradores se pueden combinar en cualquier orden
- **Principio Open/Closed**: Abierto a extensión, cerrado a modificación (SOLID)
- **Responsabilidad única**: Cada decorador tiene una función específica

## Demostración

Ejecutar la clase `DecoradorDemo.java` para ver ejemplos de uso:

```bash
cd edulearn-api/src/main/java
javac com/edulearn/patterns/estructural/decorator/*.java
java com.edulearn.patterns.estructural.decorator.DecoratorDemo
```

## Notas Importantes

- Los decoradores son **opcionales**: un módulo puede tener ninguno, uno o ambos
- Los decoradores **no afectan** la funcionalidad base del módulo
- La configuración de decoradores se **persiste** en la base de datos
- Los decoradores pueden ser **eliminados** en cualquier momento sin afectar el módulo base
