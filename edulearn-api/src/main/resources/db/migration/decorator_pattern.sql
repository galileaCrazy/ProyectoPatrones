-- =====================================================
-- SCRIPT DE MIGRACIÓN: Patrón Decorator
-- =====================================================
-- Agrega campos para gamificación y certificación
-- a la tabla modulos (Patrón Decorator)
-- =====================================================

-- Agregar campos de gamificación
ALTER TABLE modulos ADD COLUMN IF NOT EXISTS gamificacion_habilitada BOOLEAN DEFAULT FALSE;
ALTER TABLE modulos ADD COLUMN IF NOT EXISTS gamificacion_puntos INTEGER;
ALTER TABLE modulos ADD COLUMN IF NOT EXISTS gamificacion_badge VARCHAR(100);

-- Agregar campos de certificación
ALTER TABLE modulos ADD COLUMN IF NOT EXISTS certificacion_habilitada BOOLEAN DEFAULT FALSE;
ALTER TABLE modulos ADD COLUMN IF NOT EXISTS certificacion_tipo VARCHAR(100);
ALTER TABLE modulos ADD COLUMN IF NOT EXISTS certificacion_activa BOOLEAN DEFAULT FALSE;

-- Comentarios para documentación
COMMENT ON COLUMN modulos.gamificacion_habilitada IS 'Indica si el módulo tiene gamificación habilitada (Patrón Decorator)';
COMMENT ON COLUMN modulos.gamificacion_puntos IS 'Puntos que obtiene el estudiante al completar el módulo';
COMMENT ON COLUMN modulos.gamificacion_badge IS 'Badge o insignia que se otorga al completar el módulo';
COMMENT ON COLUMN modulos.certificacion_habilitada IS 'Indica si el módulo otorga certificación (Patrón Decorator)';
COMMENT ON COLUMN modulos.certificacion_tipo IS 'Tipo de certificado que se otorga';
COMMENT ON COLUMN modulos.certificacion_activa IS 'Indica si el certificado está activo y disponible para descarga';

-- Datos de ejemplo (opcional - comentar si no se desea)
-- Actualizar algunos módulos existentes con decoradores de ejemplo
UPDATE modulos
SET gamificacion_habilitada = TRUE,
    gamificacion_puntos = 150,
    gamificacion_badge = 'Maestro del Módulo'
WHERE id = 1;

UPDATE modulos
SET certificacion_habilitada = TRUE,
    certificacion_tipo = 'Certificado de Finalización',
    certificacion_activa = TRUE
WHERE id = 2;

UPDATE modulos
SET gamificacion_habilitada = TRUE,
    gamificacion_puntos = 200,
    gamificacion_badge = 'Experto en Programación',
    certificacion_habilitada = TRUE,
    certificacion_tipo = 'Certificado Profesional',
    certificacion_activa = TRUE
WHERE id = 3;
