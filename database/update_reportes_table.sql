-- Actualizar tabla reportes_generados para el patrón Bridge
-- Agregar columnas necesarias para almacenar reportes generados

ALTER TABLE reportes_generados
ADD COLUMN IF NOT EXISTS titulo VARCHAR(200),
ADD COLUMN IF NOT EXISTS contenido TEXT,
ADD COLUMN IF NOT EXISTS formato VARCHAR(50),
ADD COLUMN IF NOT EXISTS fecha_generacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS ruta_archivo VARCHAR(500),
ADD COLUMN IF NOT EXISTS usuario_id INT;

-- Actualizar estados para que coincidan con el modelo
ALTER TABLE reportes_generados
MODIFY COLUMN estado VARCHAR(50) DEFAULT 'GENERADO';

-- Renombrar columna si existe
-- ALTER TABLE reportes_generados CHANGE generado_por usuario_id INT;
