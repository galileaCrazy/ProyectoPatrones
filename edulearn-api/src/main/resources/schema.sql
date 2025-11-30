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

-- ========== PATRÓN ABSTRACT FACTORY ==========
-- Tabla para contenidos educativos (VIDEO, DOCUMENTO, QUIZ) x (BASICO, INTERMEDIO, AVANZADO)
CREATE TABLE IF NOT EXISTS contenidos_educativos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    nivel VARCHAR(50) NOT NULL,
    descripcion TEXT,
    duracion_estimada INT,
    contenido_renderizado TEXT,
    fecha_creacion DATETIME,
    curso_id BIGINT,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_tipo (tipo),
    INDEX idx_nivel (nivel),
    INDEX idx_tipo_nivel (tipo, nivel),
    INDEX idx_curso_id (curso_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== PATRÓN BUILDER ==========
-- Tabla para cursos construidos con el patrón Builder
CREATE TABLE IF NOT EXISTS cursos_builder (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    modalidad VARCHAR(50),
    nivel_dificultad VARCHAR(50),
    duracion_horas INT,
    cupo_maximo INT,
    precio DECIMAL(10,2),
    categoria VARCHAR(100),
    incluye_certificado BOOLEAN DEFAULT FALSE,
    incluye_video_lectures BOOLEAN DEFAULT FALSE,
    incluye_evaluaciones BOOLEAN DEFAULT TRUE,
    incluye_proyecto_final BOOLEAN DEFAULT FALSE,
    requisitos_previos TEXT,
    objetivos TEXT,
    fecha_inicio DATE,
    fecha_fin DATE,
    estado VARCHAR(50),
    fecha_creacion DATETIME,
    tipo_construccion VARCHAR(50),
    INDEX idx_modalidad (modalidad),
    INDEX idx_nivel_dificultad (nivel_dificultad),
    INDEX idx_categoria (categoria),
    INDEX idx_estado (estado),
    INDEX idx_tipo_construccion (tipo_construccion)
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
