# Debug del Patrón Decorator

## Problemas Reportados

1. **Error 404**: "Módulo no encontrado con ID: 1"
2. **Certificación deshabilitada**: No reconoce el último módulo para permitir certificación

## Solución: Logs de Debug Agregados

He agregado logs detallados en el backend para identificar exactamente qué está pasando.

## Pasos para Debuggear

### 1. Ejecutar el Script SQL (MUY IMPORTANTE)

**Primero debes ejecutar el script SQL para agregar los campos necesarios:**

```bash
# Conéctate a tu base de datos
# Si usas PostgreSQL:
psql -U postgres -d edulearn -f edulearn-api/src/main/resources/db/migration/decorator_pattern.sql

# Si usas MySQL:
mysql -u root -p edulearn < edulearn-api/src/main/resources/db/migration/decorator_pattern.sql

# O ejecuta manualmente el script desde tu cliente de BD favorito
```

### 2. Verificar que Hay Módulos en la BD

**Opción A: Usar el endpoint de debug que creé**

Primero, inicia el backend:
```bash
cd edulearn-api
mvn spring-boot:run
```

Luego, en otra terminal o navegador, accede a:
```bash
curl http://localhost:8080/api/modulos/debug/todos
```

O abre en el navegador: `http://localhost:8080/api/modulos/debug/todos`

**Esto te mostrará todos los módulos con sus IDs reales.**

**Opción B: Consultar directamente en la BD**

```sql
SELECT id, nombre, curso_id, orden, modulo_padre_id
FROM modulos
ORDER BY curso_id, orden;
```

### 3. Reiniciar el Backend con Logs

```bash
cd edulearn-api
mvn spring-boot:run
```

**Los logs ahora mostrarán:**
- Qué ID se recibe en el controlador
- Qué módulo se busca en la BD
- Todos los módulos del curso
- Cuál es el último módulo (con orden más alto)
- Si el módulo puede tener certificación

### 4. Probar desde el Frontend

1. Abre el frontend: `http://localhost:3000`
2. Ve al contenido de un curso
3. Pasa el cursor sobre un módulo
4. Haz clic en el botón de estrella (Sparkles)
5. **Observa la consola del backend** (donde corre `mvn spring-boot:run`)

### 5. Leer los Logs

Los logs se verán así:

```
=== PUEDE CERTIFICAR ENDPOINT ===
ID recibido: 25
=== VERIFICANDO SI ES ÚLTIMO MÓDULO ===
moduloId a verificar: 25
Módulo encontrado: Módulo 3
Curso ID: 1
Orden del módulo: 3
Módulos del curso (raíz): 3
  - ID: 23, Nombre: Módulo 1, Orden: 1
  - ID: 24, Nombre: Módulo 2, Orden: 2
  - ID: 25, Nombre: Módulo 3, Orden: 3
Último módulo: ID=25, Nombre=Módulo 3, Orden=3
¿Es el último?: true
Resultado: true
```

## Posibles Causas del Error

### Causa 1: El módulo no existe en la BD

**Síntoma:** Los logs muestran "Módulo no encontrado con ID: X"

**Solución:**
1. Verifica que el script SQL se ejecutó correctamente
2. Verifica que hay módulos en la tabla `modulos`
3. Usa el endpoint de debug: `http://localhost:8080/api/modulos/debug/todos`

### Causa 2: El orden de los módulos es NULL

**Síntoma:** Los logs muestran que el orden es `null` o todos tienen orden 0

**Solución:**
```sql
-- Actualizar el orden de los módulos manualmente
UPDATE modulos SET orden = 1 WHERE id = X;
UPDATE modulos SET orden = 2 WHERE id = Y;
UPDATE modulos SET orden = 3 WHERE id = Z;
```

### Causa 3: Todos los módulos tienen moduloPadreId

**Síntoma:** Los logs muestran "Módulos del curso (raíz): 0"

**Solución:**
Los módulos principales deben tener `modulo_padre_id = NULL`:
```sql
UPDATE modulos
SET modulo_padre_id = NULL
WHERE id IN (SELECT id FROM modulos WHERE curso_id = 1 LIMIT 3);
```

### Causa 4: El frontend envía el ID como String

**Síntoma:** Los logs muestran que el ID se recibe correctamente pero no se encuentra

**Solución:** Ya está manejado en el backend (convierte String a Long automáticamente)

## Verificación Final

Una vez que todo funcione, deberías ver:

**Para módulos NO finales:**
```
Último módulo: ID=25, Nombre=Módulo 3, Orden=3
¿Es el último?: false
Resultado: false
```

**Para el último módulo:**
```
Último módulo: ID=25, Nombre=Módulo 3, Orden=3
¿Es el último?: true
Resultado: true
```

## Endpoints de Debug Disponibles

### 1. Ver todos los módulos
```
GET http://localhost:8080/api/modulos/debug/todos
```

### 2. Verificar si puede tener certificación
```
GET http://localhost:8080/api/modulos/{id}/decoradores/puede-certificar
```

Ejemplo:
```
GET http://localhost:8080/api/modulos/25/decoradores/puede-certificar
```

### 3. Ver contenido decorado de un módulo
```
GET http://localhost:8080/api/modulos/{id}/decoradores
```

## Comandos Útiles

### Listar todos los módulos con orden
```sql
SELECT
    m.id,
    m.nombre,
    m.curso_id,
    m.orden,
    m.modulo_padre_id,
    CASE
        WHEN m.modulo_padre_id IS NULL THEN 'RAÍZ'
        ELSE 'SUBMÓDULO'
    END as tipo
FROM modulos m
ORDER BY m.curso_id, m.orden;
```

### Encontrar el último módulo de cada curso
```sql
SELECT
    curso_id,
    id,
    nombre,
    orden
FROM modulos m1
WHERE modulo_padre_id IS NULL
  AND orden = (
    SELECT MAX(orden)
    FROM modulos m2
    WHERE m2.curso_id = m1.curso_id
      AND m2.modulo_padre_id IS NULL
  )
ORDER BY curso_id;
```

### Actualizar orden de módulos secuencialmente
```sql
-- Para el curso con ID 1
UPDATE modulos SET orden = 1 WHERE id = (ID_DEL_PRIMER_MODULO);
UPDATE modulos SET orden = 2 WHERE id = (ID_DEL_SEGUNDO_MODULO);
UPDATE modulos SET orden = 3 WHERE id = (ID_DEL_TERCER_MODULO);
```

## Siguiente Paso

**Ejecuta estos comandos en orden:**

1. **Ejecutar script SQL**
2. **Verificar módulos:** `curl http://localhost:8080/api/modulos/debug/todos`
3. **Iniciar backend:** `mvn spring-boot:run`
4. **Probar desde frontend**
5. **Revisar logs en la consola del backend**
6. **Compartir los logs** si el problema persiste

Los logs detallados nos dirán exactamente qué está pasando.
