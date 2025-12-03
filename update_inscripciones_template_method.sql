-- Script para actualizar la tabla inscripciones con soporte para Template Method Pattern
-- Agrega columnas para modalidad, estado de inscripción y certificado garantizado
-- Monto de pago establecido: 500 pesos para todos los cursos

USE edulearn;

-- Agregar columna modalidad (tipo de inscripción)
ALTER TABLE inscripciones
ADD COLUMN modalidad VARCHAR(20) DEFAULT 'GRATUITA'
COMMENT 'Tipo de inscripción: GRATUITA, PAGA, BECA';

-- Agregar columna estado_inscripcion (estado actual del proceso)
ALTER TABLE inscripciones
ADD COLUMN estado_inscripcion VARCHAR(50) DEFAULT 'Activa'
COMMENT 'Estado: Activa, Pendiente de Pago, Pendiente de Aprobación/Documentación, Completada';

-- Agregar columna certificado_garantizado (beneficio adicional)
ALTER TABLE inscripciones
ADD COLUMN certificado_garantizado TINYINT(1) DEFAULT 0
COMMENT 'Indica si el estudiante tiene garantizado el certificado al finalizar (1=Sí, 0=No)';

-- Agregar columna tipo_beca (para inscripciones con beca)
ALTER TABLE inscripciones
ADD COLUMN tipo_beca VARCHAR(50) NULL
COMMENT 'Tipo de beca: ACADEMICA, DEPORTIVA, SOCIECONOMICA, CULTURAL, TECNM';

-- Agregar columna codigo_beca (para validación de becas)
ALTER TABLE inscripciones
ADD COLUMN codigo_beca VARCHAR(100) NULL
COMMENT 'Código de validación de la beca';

-- Agregar columna metodo_pago (para inscripciones pagas)
ALTER TABLE inscripciones
ADD COLUMN metodo_pago VARCHAR(50) NULL
COMMENT 'Método de pago utilizado: TARJETA, TRANSFERENCIA, PAYPAL, etc.';

-- Agregar columna transaccion_id (para inscripciones pagas)
ALTER TABLE inscripciones
ADD COLUMN transaccion_id VARCHAR(100) NULL
COMMENT 'ID de la transacción de pago';

-- Agregar columna monto_pagado (para inscripciones pagas)
-- Monto fijo: 500 pesos para todos los cursos
ALTER TABLE inscripciones
ADD COLUMN monto_pagado DECIMAL(10,2) NULL DEFAULT 500.00
COMMENT 'Monto pagado por la inscripción (500 pesos para todos los cursos)';

-- Agregar índices para mejorar el rendimiento de consultas
CREATE INDEX idx_inscripciones_modalidad ON inscripciones(modalidad);
CREATE INDEX idx_inscripciones_estado ON inscripciones(estado_inscripcion);
CREATE INDEX idx_inscripciones_tipo_beca ON inscripciones(tipo_beca);

-- Actualizar inscripciones existentes con valores por defecto
UPDATE inscripciones
SET modalidad = 'GRATUITA',
    estado_inscripcion = 'Activa',
    certificado_garantizado = 0
WHERE modalidad IS NULL;

-- Verificar la estructura actualizada
DESCRIBE inscripciones;

-- Mostrar datos de ejemplo
SELECT id, estudiante_id, curso_id, modalidad, estado_inscripcion,
       certificado_garantizado, tipo_beca, monto_pagado, fecha_inscripcion
FROM inscripciones
LIMIT 5;
