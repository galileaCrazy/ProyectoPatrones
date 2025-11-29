-- Script para configurar la base de datos edulearn
-- Compatible con el modelo de Spring Boot

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS edulearn;
USE edulearn;

-- Crear tabla usuarios compatible con el modelo Usuario.java
CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    tipo_usuario VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_tipo_usuario (tipo_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla cursos
CREATE TABLE IF NOT EXISTS cursos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    duracion INT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla inscripciones
CREATE TABLE IF NOT EXISTS inscripciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id INT,
    curso_id INT,
    fecha_inscripcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar usuarios de prueba
INSERT INTO usuarios (nombre, email, password_hash, tipo_usuario) VALUES
('Estudiante Demo', 'estudiante@edulearn.com', 'student123', 'estudiante'),
('Profesor Demo', 'profesor@edulearn.com', 'prof456', 'profesor'),
('Admin Demo', 'admin@edulearn.com', 'admin789', 'administrador'),
('Juan Pérez', 'juan@edulearn.com', 'juan123', 'estudiante'),
('María García', 'maria@edulearn.com', 'maria123', 'profesor')
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar cursos de ejemplo
INSERT INTO cursos (nombre, descripcion, duracion) VALUES
('Patrones de Diseño', 'Curso completo de patrones de diseño en Java', 40),
('Spring Boot Avanzado', 'Desarrollo de aplicaciones empresariales con Spring Boot', 60),
('React y Next.js', 'Desarrollo frontend moderno', 50)
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Insertar estudiantes de ejemplo
INSERT INTO estudiantes (nombre, apellidos, email) VALUES
('Ana', 'López Martínez', 'ana.lopez@student.edu'),
('Carlos', 'Rodríguez Sánchez', 'carlos.rodriguez@student.edu'),
('Laura', 'Fernández García', 'laura.fernandez@student.edu')
ON DUPLICATE KEY UPDATE nombre=nombre;

-- Verificar los datos
SELECT 'USUARIOS' as tabla;
SELECT id, nombre, email, tipo_usuario FROM usuarios;

SELECT 'CURSOS' as tabla;
SELECT id, nombre, duracion FROM cursos;

SELECT 'ESTUDIANTES' as tabla;
SELECT id, nombre, apellidos, email FROM estudiantes;
