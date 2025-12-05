-- =====================================================
-- SCRIPT SQL PARA EL PATRÓN MEMENTO (VERSION 2)
-- Sistema de Guardado y Restauración de Progreso
-- Usando nombres diferentes para evitar conflictos
-- =====================================================

-- Tabla: estudiante_progreso_memento
-- Almacena el estado actual del progreso del estudiante en un curso
CREATE TABLE IF NOT EXISTS estudiante_progreso_memento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id INT NOT NULL,
    curso_id INT NOT NULL,
    modulo_actual_id INT DEFAULT 1,
    porcentaje_completado INT DEFAULT 0,
    calificacion_acumulada DECIMAL(5,2) DEFAULT 0.00,
    lecciones_completadas INT DEFAULT 0,
    evaluaciones_completadas INT DEFAULT 0,
    estado_curso VARCHAR(20) DEFAULT 'EN_PROGRESO',
    notas_estudiante TEXT,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    ultima_leccion_vista VARCHAR(200),

    -- Restricciones
    UNIQUE KEY unique_estudiante_curso (estudiante_id, curso_id),

    -- Índices para mejorar el rendimiento
    INDEX idx_estudiante (estudiante_id),
    INDEX idx_curso (curso_id),
    INDEX idx_estado (estado_curso)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla: historial_progreso_estudiante
-- Almacena los mementos (puntos de restauración) del progreso
CREATE TABLE IF NOT EXISTS historial_progreso_estudiante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id INT NOT NULL,
    curso_id INT NOT NULL,
    modulo_actual_id INT,
    porcentaje_completado INT,
    calificacion_acumulada DECIMAL(5,2),
    lecciones_completadas INT,
    evaluaciones_completadas INT,
    estado_curso VARCHAR(20),
    notas_estudiante TEXT,
    fecha_guardado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descripcion VARCHAR(500),

    -- Índices para mejorar el rendimiento
    INDEX idx_estudiante_curso (estudiante_id, curso_id),
    INDEX idx_fecha (fecha_guardado),
    INDEX idx_estudiante (estudiante_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- DATOS DE EJEMPLO PARA PRUEBAS
-- =====================================================

-- Ver todos los progresos
-- SELECT * FROM estudiante_progreso_memento;

-- Ver el historial de un estudiante en un curso específico
-- SELECT * FROM historial_progreso_estudiante
-- WHERE estudiante_id = 1 AND curso_id = 1
-- ORDER BY fecha_guardado DESC;
