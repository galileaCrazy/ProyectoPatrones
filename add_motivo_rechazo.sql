-- Agregar columna motivo_rechazo a la tabla inscripciones
USE edulearn_db;

ALTER TABLE inscripciones
ADD COLUMN motivo_rechazo VARCHAR(500) NULL
COMMENT 'Motivo por el cual se rechazó la inscripción (solo para becas rechazadas)';
