-- =====================================================
-- SCRIPT SQL PARA EL PATRÓN MEMENTO
-- Sistema de Guardado y Restauración de Progreso
-- =====================================================

-- Tabla: progreso_estudiante
-- Almacena el estado actual del progreso del estudiante en un curso
CREATE TABLE IF NOT EXISTS progreso_estudiante (
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
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(usuario_id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,

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

    -- Restricciones
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(usuario_id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE,

    -- Índices para mejorar el rendimiento
    INDEX idx_estudiante_curso (estudiante_id, curso_id),
    INDEX idx_fecha (fecha_guardado),
    INDEX idx_estudiante (estudiante_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- DATOS DE EJEMPLO PARA PRUEBAS
-- =====================================================

-- Insertar progreso de ejemplo (ajusta los IDs según tu base de datos)
-- INSERT INTO progreso_estudiante (estudiante_id, curso_id, modulo_actual_id, porcentaje_completado, calificacion_acumulada, lecciones_completadas, evaluaciones_completadas, estado_curso, notas_estudiante)
-- VALUES
-- (1, 1, 3, 45, 8.5, 9, 2, 'EN_PROGRESO', 'Muy interesante el tema de patrones'),
-- (1, 2, 2, 30, 7.8, 6, 1, 'EN_PROGRESO', 'Necesito repasar el módulo 1'),
-- (2, 1, 5, 80, 9.2, 16, 4, 'EN_PROGRESO', 'Excelente curso');

-- Insertar historial de ejemplo
-- INSERT INTO historial_progreso_estudiante (estudiante_id, curso_id, modulo_actual_id, porcentaje_completado, calificacion_acumulada, lecciones_completadas, evaluaciones_completadas, estado_curso, notas_estudiante, descripcion)
-- VALUES
-- (1, 1, 2, 30, 8.0, 6, 1, 'EN_PROGRESO', 'Inicio del curso', 'Checkpoint: Completado módulo 2'),
-- (1, 1, 3, 45, 8.5, 9, 2, 'EN_PROGRESO', 'Muy interesante el tema de patrones', 'Checkpoint: Completado módulo 3');

-- =====================================================
-- CONSULTAS ÚTILES PARA VERIFICAR EL SISTEMA
-- =====================================================

-- Ver todos los progresos de un estudiante
-- SELECT * FROM progreso_estudiante WHERE estudiante_id = 1;

-- Ver el historial de un estudiante en un curso específico
-- SELECT * FROM historial_progreso_estudiante
-- WHERE estudiante_id = 1 AND curso_id = 1
-- ORDER BY fecha_guardado DESC;

-- Ver estadísticas de progreso por curso
-- SELECT
--     c.titulo as curso,
--     COUNT(*) as total_estudiantes,
--     AVG(p.porcentaje_completado) as promedio_completado,
--     AVG(p.calificacion_acumulada) as promedio_calificacion
-- FROM progreso_estudiante p
-- JOIN cursos c ON p.curso_id = c.id
-- GROUP BY c.id, c.titulo;

-- Ver estudiantes con mayor avance
-- SELECT
--     e.matricula,
--     u.nombre,
--     u.apellidos,
--     c.titulo as curso,
--     p.porcentaje_completado,
--     p.calificacion_acumulada,
--     p.estado_curso
-- FROM progreso_estudiante p
-- JOIN estudiantes e ON p.estudiante_id = e.usuario_id
-- JOIN usuarios u ON e.usuario_id = u.id
-- JOIN cursos c ON p.curso_id = c.id
-- ORDER BY p.porcentaje_completado DESC
-- LIMIT 10;
