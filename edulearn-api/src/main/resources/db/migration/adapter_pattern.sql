-- Migración para Patrón Adapter - Integraciones Externas
-- Fecha: 2025-12-04

-- Tabla para almacenar integraciones externas (videoconferencias y repositorios)
CREATE TABLE IF NOT EXISTS integraciones_cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id INT NOT NULL,
    profesor_id INT NOT NULL,
    tipo VARCHAR(50) NOT NULL COMMENT 'VIDEOCONFERENCIA, REPOSITORIO',
    proveedor VARCHAR(50) NOT NULL COMMENT 'GOOGLE_MEET, ZOOM, GOOGLE_DRIVE, ONEDRIVE',
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    url_recurso TEXT COMMENT 'URL de la reunión o carpeta compartida',
    identificador_externo VARCHAR(200) COMMENT 'ID en el sistema externo',
    configuracion TEXT COMMENT 'JSON con configuración adicional',
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA' COMMENT 'ACTIVA, INACTIVA, ELIMINADA',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NULL,
    fecha_uso DATETIME NULL COMMENT 'Última vez que se usó',
    veces_usado INT DEFAULT 0,
    INDEX idx_curso (curso_id),
    INDEX idx_profesor (profesor_id),
    INDEX idx_tipo (tipo),
    INDEX idx_estado (estado),
    INDEX idx_proveedor (proveedor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Integraciones externas usando patrón Adapter';
