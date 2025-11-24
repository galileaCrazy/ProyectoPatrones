-- Script para crear la tabla de usuarios en la base de datos plataformaCursos
-- Ejecutar este script en MySQL antes de usar el sistema de login/registro

USE plataformaCursos;

-- Eliminar la tabla si existe (solo para desarrollo/testing)
DROP TABLE IF EXISTS usuarios;

-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    tipo_usuario ENUM('Estudiante', 'Profesor', 'Administrador') NOT NULL,

    -- Campos específicos para Estudiante
    matricula VARCHAR(50) UNIQUE,
    carrera VARCHAR(100),

    -- Campos específicos para Profesor
    departamento VARCHAR(100),
    especialidad VARCHAR(100),

    -- Campos específicos para Administrador
    nivel_acceso VARCHAR(50),
    area VARCHAR(100),

    -- Campos de auditoría
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,

    INDEX idx_correo (correo),
    INDEX idx_tipo_usuario (tipo_usuario),
    INDEX idx_matricula (matricula)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar usuarios de prueba
INSERT INTO usuarios (nombre, apellidos, correo, contrasena, tipo_usuario, matricula, carrera)
VALUES ('Juan', 'Pérez García', 'juan.perez@itoaxa.edu.mx', 'student123', 'Estudiante', 'EST001', 'Ingeniería en Sistemas');

INSERT INTO usuarios (nombre, apellidos, correo, contrasena, tipo_usuario, departamento, especialidad)
VALUES ('María', 'González López', 'maria.gonzalez@itoaxa.edu.mx', 'prof456', 'Profesor', 'Ingeniería de Software', 'Patrones de Diseño');

INSERT INTO usuarios (nombre, apellidos, correo, contrasena, tipo_usuario, nivel_acceso, area)
VALUES ('Carlos', 'Rodríguez Martínez', 'carlos.rodriguez@itoaxa.edu.mx', 'admin789', 'Administrador', 'ALTO', 'Administración General');

-- Verificar la creación de la tabla y los datos
SELECT * FROM usuarios;
