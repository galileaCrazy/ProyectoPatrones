-- =====================================================
-- SCRIPT COMPLETO PARA HABILITAR EL PATRÓN DECORATOR
-- VERSIÓN MYSQL / MariaDB
-- =====================================================
-- Instrucciones: Copia y pega este script completo
-- en tu cliente de base de datos MySQL
-- =====================================================

-- Paso 1: Agregar columnas para el patrón Decorator
-- =====================================================

-- Gamificación
ALTER TABLE modulos ADD COLUMN gamificacion_habilitada BOOLEAN DEFAULT FALSE;
ALTER TABLE modulos ADD COLUMN gamificacion_puntos INTEGER;
ALTER TABLE modulos ADD COLUMN gamificacion_badge VARCHAR(100);

-- Certificación
ALTER TABLE modulos ADD COLUMN certificacion_habilitada BOOLEAN DEFAULT FALSE;
ALTER TABLE modulos ADD COLUMN certificacion_tipo VARCHAR(100);
ALTER TABLE modulos ADD COLUMN certificacion_activa BOOLEAN DEFAULT FALSE;

-- Paso 2: Verificar el estado actual de los módulos
-- =====================================================

SELECT
    id,
    nombre,
    curso_id,
    orden,
    modulo_padre_id,
    CASE
        WHEN modulo_padre_id IS NULL THEN 'MÓDULO RAÍZ'
        ELSE 'SUBMÓDULO'
    END as tipo
FROM modulos
ORDER BY curso_id, orden;

-- Paso 3: Actualizar el orden de módulos
-- =====================================================
-- Esto asegura que cada módulo raíz tenga un orden secuencial

-- Si tus módulos ya tienen orden, puedes omitir esto
-- Si no, actualiza manualmente cada módulo según su posición real

-- Ejemplo para el curso 1:
-- UPDATE modulos SET orden = 1 WHERE id = (ID_DEL_PRIMER_MODULO) AND curso_id = 1;
-- UPDATE modulos SET orden = 2 WHERE id = (ID_DEL_SEGUNDO_MODULO) AND curso_id = 1;
-- UPDATE modulos SET orden = 3 WHERE id = (ID_DEL_TERCER_MODULO) AND curso_id = 1;

-- Paso 4: Verificar cuál es el último módulo de cada curso
-- =====================================================

SELECT
    m.curso_id as 'Curso ID',
    m.id as 'ID del Último Módulo',
    m.nombre as 'Nombre del Último Módulo',
    m.orden as 'Orden',
    '✅ Este módulo PUEDE tener certificación' as 'Estado'
FROM modulos m
WHERE m.modulo_padre_id IS NULL
  AND m.orden = (
    SELECT MAX(orden)
    FROM modulos m2
    WHERE m2.curso_id = m.curso_id
      AND m2.modulo_padre_id IS NULL
  )
ORDER BY m.curso_id;

-- Paso 5: Agregar datos de ejemplo (OPCIONAL)
-- =====================================================

-- Ejemplo: Agregar gamificación al primer módulo de cada curso
UPDATE modulos
SET
    gamificacion_habilitada = TRUE,
    gamificacion_puntos = 100,
    gamificacion_badge = 'Iniciador'
WHERE modulo_padre_id IS NULL
  AND orden = 1;

-- Ejemplo: Agregar certificación al último módulo de cada curso
UPDATE modulos m
INNER JOIN (
    SELECT curso_id, MAX(orden) as max_orden
    FROM modulos
    WHERE modulo_padre_id IS NULL
    GROUP BY curso_id
) m2 ON m.curso_id = m2.curso_id AND m.orden = m2.max_orden
SET
    m.certificacion_habilitada = TRUE,
    m.certificacion_tipo = 'Certificado de Finalización del Curso',
    m.certificacion_activa = TRUE
WHERE m.modulo_padre_id IS NULL;

-- Paso 6: Verificación final
-- =====================================================

SELECT
    m.id as 'ID',
    m.nombre as 'Nombre',
    m.curso_id as 'Curso',
    m.orden as 'Orden',
    CASE WHEN m.modulo_padre_id IS NULL THEN 'Raíz' ELSE 'Sub' END as 'Tipo',
    CASE WHEN m.gamificacion_habilitada THEN CONCAT(m.gamificacion_puntos, ' pts') ELSE '-' END as 'Gamificación',
    CASE WHEN m.certificacion_habilitada THEN m.certificacion_tipo ELSE '-' END as 'Certificación'
FROM modulos m
ORDER BY m.curso_id, m.orden;

-- =====================================================
-- ✅ SCRIPT COMPLETADO
-- =====================================================
