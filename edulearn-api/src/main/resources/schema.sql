-- ==================================================================
-- ESQUEMA DE BASE DE DATOS PARA EDULEARN
-- Tablas para soportar los 23 patrones de diseño
-- ==================================================================

-- ========== PATRÓN SINGLETON ==========
-- Tabla para configuraciones del sistema
CREATE TABLE IF NOT EXISTS configuraciones_sistema (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(100) NOT NULL UNIQUE,
    valor VARCHAR(500) NOT NULL,
    descripcion VARCHAR(255),
    tipo VARCHAR(50),
    INDEX idx_clave (clave)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== PATRÓN FACTORY METHOD ==========
-- Tabla para notificaciones (EMAIL, SMS, PUSH)
CREATE TABLE IF NOT EXISTS notificaciones_patron (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    destinatario VARCHAR(255) NOT NULL,
    asunto VARCHAR(255),
    mensaje TEXT NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_creacion DATETIME,
    fecha_envio DATETIME,
    intentos INT DEFAULT 0,
    error TEXT,
    INDEX idx_tipo (tipo),
    INDEX idx_estado (estado),
    INDEX idx_destinatario (destinatario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==================================================================
-- Insertar configuraciones por defecto
-- ==================================================================
INSERT INTO configuraciones_sistema (clave, valor, descripcion, tipo) VALUES
('nombre_sistema', 'EduLearn Platform', 'Nombre del sistema LMS', 'STRING'),
('version', '1.0.0', 'Versión del sistema', 'STRING'),
('max_intentos_login', '3', 'Máximo de intentos de login antes de bloqueo', 'INTEGER'),
('duracion_sesion_minutos', '60', 'Duración de la sesión en minutos', 'INTEGER'),
('cupo_default', '30', 'Cupo por defecto para nuevos cursos', 'INTEGER'),
('calificacion_minima_aprobacion', '60', 'Calificación mínima para aprobar (0-100)', 'INTEGER'),
('permitir_registro_estudiantes', 'true', 'Permitir auto-registro de estudiantes', 'BOOLEAN'),
('modo_mantenimiento', 'false', 'Sistema en modo mantenimiento', 'BOOLEAN'),
('email_notificaciones', 'noreply@edulearn.com', 'Email para envío de notificaciones', 'STRING'),
('url_base', 'http://localhost:3000', 'URL base del frontend', 'STRING')
ON DUPLICATE KEY UPDATE valor=VALUES(valor);
