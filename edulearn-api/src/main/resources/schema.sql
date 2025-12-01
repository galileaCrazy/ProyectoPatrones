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

-- ========== PATRONES BUILDER Y PROTOTYPE ==========
-- Tabla para cursos (usada por Builder y Prototype)
-- NOTA: Si la tabla ya existe con más campos, no se sobrescribirá
CREATE TABLE IF NOT EXISTS cursos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    tipo_curso VARCHAR(50),
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    profesor_titular_id INT NULL,
    periodo_academico VARCHAR(50) NULL,
    duracion INT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_tipo_curso (tipo_curso),
    INDEX idx_estado (estado),
    INDEX idx_profesor_titular_id (profesor_titular_id),
    INDEX idx_periodo_academico (periodo_academico)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== PATRÓN ADAPTER ==========
-- Tabla para integraciones externas (Zoom, Google Meet, MS Teams, etc.)
CREATE TABLE IF NOT EXISTS integraciones_externas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_sistema VARCHAR(50) NOT NULL,
    nombre_configuracion VARCHAR(255) NOT NULL,
    api_key VARCHAR(500),
    api_secret VARCHAR(500),
    url_base VARCHAR(500),
    estado VARCHAR(50) NOT NULL DEFAULT 'ACTIVO',
    curso_id INT,
    sala_reunion TEXT,
    fecha_creacion DATETIME,
    ultima_sincronizacion DATETIME,
    INDEX idx_tipo_sistema (tipo_sistema),
    INDEX idx_estado (estado),
    INDEX idx_curso_id (curso_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== PATRÓN BRIDGE ==========
-- Tabla para reportes generados (Bridge separa abstracción de implementación)
CREATE TABLE IF NOT EXISTS reportes_generados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_reporte VARCHAR(50) NOT NULL,
    formato VARCHAR(50) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    contenido TEXT,
    ruta_archivo VARCHAR(500),
    usuario_id INT,
    fecha_generacion DATETIME,
    parametros TEXT,
    estado VARCHAR(50),
    INDEX idx_tipo_reporte (tipo_reporte),
    INDEX idx_formato (formato),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========== PATRÓN COMPOSITE ==========
-- Tabla para módulos y submódulos de cursos (estructura jerárquica)
CREATE TABLE IF NOT EXISTS modulos_curso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    curso_id INT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    orden INT,
    duracion_horas INT,
    modulo_padre_id INT,
    es_hoja BOOLEAN DEFAULT TRUE,
    nivel INT DEFAULT 0,
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    fecha_creacion DATETIME,
    INDEX idx_curso_id (curso_id),
    INDEX idx_modulo_padre_id (modulo_padre_id),
    INDEX idx_estado (estado)
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
