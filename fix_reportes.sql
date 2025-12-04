-- Agregar columna contenido a la tabla reportes_generados si no existe
USE edulearn;

ALTER TABLE reportes_generados
ADD COLUMN IF NOT EXISTS contenido TEXT AFTER titulo;

-- Verificar la estructura de la tabla
DESCRIBE reportes_generados;
