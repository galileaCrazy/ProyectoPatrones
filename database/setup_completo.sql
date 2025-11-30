-- ==================================================================
-- SCRIPT COMPLETO DE CONFIGURACIÓN DE BASE DE DATOS EDULEARN
-- Incluye todas las tablas: usuarios + tablas de patrones
-- ==================================================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS edulearn;
USE edulearn;

-- ========== TABLAS PRINCIPALES ==========

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_tipo_usuario (tipo_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de cursos
CREATE TABLE IF NOT EXISTS cursos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    duracion INT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255),
    codigo VARCHAR(50) UNIQUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de inscripciones
CREATE TABLE IF NOT EXISTS inscripciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id INT,
    curso_id INT,
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========== TABLAS DE PATRONES DE DISEÑO ==========

-- PATRÓN SINGLETON: Configuraciones del sistema
CREATE TABLE IF NOT EXISTS configuraciones_sistema (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    clave VARCHAR(100) NOT NULL UNIQUE,
    valor VARCHAR(500) NOT NULL,
    descripcion VARCHAR(255),
    tipo VARCHAR(50),
    INDEX idx_clave (clave)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- PATRÓN FACTORY METHOD: Notificaciones (EMAIL, SMS, PUSH)
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

-- PATRÓN ABSTRACT FACTORY: Contenidos educativos
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

-- ==================================================================
-- DATOS INICIALES
-- ==================================================================

-- Insertar usuarios de prueba (con contraseñas en texto plano, se actualizarán después)
INSERT INTO usuarios (nombre, apellidos, email, password_hash, tipo_usuario) VALUES
('Estudiante Demo', NULL, 'estudiante@edulearn.com', 'student123', 'estudiante'),
('Profesor Demo', NULL, 'profesor@edulearn.com', 'prof456', 'profesor'),
('Admin Demo', NULL, 'admin@edulearn.com', 'admin789', 'administrador'),
('Juan', 'Pérez', 'juan@edulearn.com', 'juan123', 'estudiante'),
('María', 'García', 'maria@edulearn.com', 'maria123', 'profesor')
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar cursos de ejemplo
INSERT INTO cursos (nombre, descripcion, duracion) VALUES
('Patrones de Diseño', 'Curso completo de patrones de diseño en Java', 40),
('Spring Boot Avanzado', 'Desarrollo de aplicaciones empresariales con Spring Boot', 60),
('React y Next.js', 'Desarrollo frontend moderno', 50)
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar estudiantes de ejemplo
INSERT INTO estudiantes (nombre, apellidos, email, codigo) VALUES
('Ana', 'López Martínez', 'ana.lopez@student.edu', 'EST100001'),
('Carlos', 'Rodríguez Sánchez', 'carlos.rodriguez@student.edu', 'EST100002'),
('Laura', 'Fernández García', 'laura.fernandez@student.edu', 'EST100003')
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar configuraciones del sistema para el patrón Singleton
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

-- ==================================================================
-- VERIFICACIÓN
-- ==================================================================

SELECT '=== TABLAS CREADAS ===' as mensaje;
SHOW TABLES;

SELECT '=== USUARIOS ===' as tabla;
SELECT id, nombre, email, tipo_usuario FROM usuarios;

SELECT '=== CURSOS ===' as tabla;
SELECT id, nombre, duracion FROM cursos;

SELECT '=== ESTUDIANTES ===' as tabla;
SELECT id, nombre, apellidos, email, codigo FROM estudiantes;

SELECT '=== CONFIGURACIONES ===' as tabla;
SELECT clave, valor, tipo FROM configuraciones_sistema;
