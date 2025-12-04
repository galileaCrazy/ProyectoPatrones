-- ============================================================
-- Script SQL para insertar datos de prueba
-- Módulos y Materiales del Curso con Patrón Proxy
-- ============================================================

-- Limpiar datos previos (opcional)
-- DELETE FROM materiales WHERE curso_id = 1;
-- DELETE FROM modulos_curso WHERE curso_id = 1;

-- ============================================================
-- MÓDULOS PRINCIPALES (Nivel 0)
-- ============================================================

-- Módulo 1: Patrón Proxy - Lazy Loading
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Patrón Proxy - Lazy Loading',
 'Implementación del patrón Proxy con carga diferida y control de acceso. Aprende a optimizar recursos cargando contenido solo cuando es necesario.',
 1, 4, NULL, false, 0, 'ACTIVO', NOW());

-- Obtener el ID del módulo recién insertado
SET @modulo1_id = LAST_INSERT_ID();

-- Submódulo 1.1: Conceptos Teóricos
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Conceptos Teóricos',
 'Fundamentos teóricos del patrón Proxy',
 1, 2, @modulo1_id, false, 1, 'ACTIVO', NOW());

SET @submodulo1_1_id = LAST_INSERT_ID();

-- Submódulo 1.2: Implementación Práctica
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Implementación Práctica',
 'Aplicación práctica del patrón Proxy en proyectos reales',
 2, 2, @modulo1_id, false, 1, 'ACTIVO', NOW());

SET @submodulo1_2_id = LAST_INSERT_ID();

-- Lecciones del Submódulo 1.1
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, '¿Qué es el Patrón Proxy?',
 'Introducción al patrón Proxy y sus beneficios',
 1, 1, @submodulo1_1_id, true, 2, 'ACTIVO', NOW()),
(1, 'Tipos de Proxy',
 'Virtual Proxy, Protection Proxy, Remote Proxy',
 2, 1, @submodulo1_1_id, true, 2, 'ACTIVO', NOW());

-- Lecciones del Submódulo 1.2
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Código del Backend',
 'Implementación del patrón Proxy en Java/Spring Boot',
 1, 1, @submodulo1_2_id, true, 2, 'ACTIVO', NOW()),
(1, 'Integración con Frontend',
 'Conectar el Proxy con React/Next.js',
 2, 1, @submodulo1_2_id, true, 2, 'ACTIVO', NOW());

-- ============================================================
-- MATERIALES DEL MÓDULO 1
-- ============================================================

INSERT INTO materiales (modulo_id, curso_id, titulo, nombre, descripcion, tipo, tipo_material, url_recurso, tamano_bytes, orden, duracion_segundos, es_obligatorio, estado, fecha_creacion, fecha_actualizacion)
VALUES
-- Material 1: Video de introducción
(@modulo1_id, 1,
 'Introducción al Patrón Proxy',
 'Introducción al Patrón Proxy',
 'Video explicativo sobre los conceptos básicos del patrón Proxy',
 'video', 'VIDEO',
 'https://www.youtube.com/watch?v=ejemplo1',
 471859200, -- 450 MB
 1, 900, -- 15 minutos
 true, 'activo', NOW(), NOW()),

-- Material 2: PDF de documentación
(@modulo1_id, 1,
 'Documentación del Patrón Proxy',
 'Documentación del Patrón Proxy',
 'Guía completa en PDF sobre el patrón Proxy',
 'pdf', 'PDF',
 '/materiales/proxy-pattern.pdf',
 891289600, -- 850 MB
 2, 600, -- 10 minutos
 false, 'activo', NOW(), NOW()),

-- Material 3: Lectura
(@modulo1_id, 1,
 'Lazy Loading en UI',
 'Lazy Loading en UI',
 'Artículo sobre carga diferida en interfaces de usuario',
 'documento', 'LECTURE',
 '/materiales/lazy-loading-ui.html',
 0,
 3, 480, -- 8 minutos
 false, 'activo', NOW(), NOW()),

-- Material 4: Quiz
(@modulo1_id, 1,
 'Quiz: Patrón Proxy',
 'Quiz: Patrón Proxy',
 'Evaluación sobre conceptos del patrón Proxy',
 'enlace', 'QUIZ',
 '/evaluaciones/quiz-proxy',
 0,
 4, 300, -- 5 minutos
 true, 'activo', NOW(), NOW());

-- ============================================================
-- MÓDULO 2: Patrón Strategy
-- ============================================================

INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Patrón Strategy',
 'Implementación del patrón Strategy para diferentes algoritmos de evaluación.',
 2, 3, NULL, false, 0, 'ACTIVO', NOW());

SET @modulo2_id = LAST_INSERT_ID();

-- Materiales del Módulo 2
INSERT INTO materiales (modulo_id, curso_id, titulo, nombre, descripcion, tipo, tipo_material, url_recurso, tamano_bytes, orden, duracion_segundos, es_obligatorio, estado, fecha_creacion, fecha_actualizacion)
VALUES
(@modulo2_id, 1,
 'Introducción al Strategy',
 'Introducción al Strategy',
 'Video sobre el patrón Strategy',
 'video', 'VIDEO',
 'https://www.youtube.com/watch?v=ejemplo2',
 0,
 1, 720, -- 12 minutos
 true, 'activo', NOW(), NOW()),

(@modulo2_id, 1,
 'Implementación en Java',
 'Implementación en Java',
 'Código de ejemplo del patrón Strategy',
 'documento', 'LECTURE',
 '/materiales/strategy-java.html',
 0,
 2, 900, -- 15 minutos
 false, 'activo', NOW(), NOW());

-- ============================================================
-- MÓDULO 3: Chain of Responsibility (Bloqueado)
-- ============================================================

INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Patrón Chain of Responsibility',
 'Cadena de validaciones para procesos de inscripción.',
 3, 5, NULL, false, 0, 'INACTIVO', NOW());

SET @modulo3_id = LAST_INSERT_ID();

-- Submódulo del Módulo 3
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Conceptos',
 'Conceptos fundamentales del patrón',
 1, 2, @modulo3_id, false, 1, 'INACTIVO', NOW());

SET @submodulo3_1_id = LAST_INSERT_ID();

-- Lección del Submódulo 3.1
INSERT INTO modulos_curso (curso_id, nombre, descripcion, orden, duracion_horas, modulo_padre_id, es_hoja, nivel, estado, fecha_creacion)
VALUES
(1, 'Introducción',
 'Introducción al patrón Chain of Responsibility',
 1, 1, @submodulo3_1_id, true, 2, 'INACTIVO', NOW());

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================

-- Verificar datos insertados
SELECT 'Módulos insertados:' AS Resultado;
SELECT id, nombre, nivel, estado, modulo_padre_id FROM modulos_curso WHERE curso_id = 1 ORDER BY orden, id;

SELECT 'Materiales insertados:' AS Resultado;
SELECT id, nombre, tipo_material, duracion_segundos FROM materiales WHERE curso_id = 1 ORDER BY modulo_id, orden;
