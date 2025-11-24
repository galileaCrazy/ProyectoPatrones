-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: plataformacursos
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `configuracion_academica`
--

DROP TABLE IF EXISTS `configuracion_academica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_academica` (
  `id` int NOT NULL DEFAULT '1',
  `periodo_actual` varchar(20) NOT NULL,
  `fecha_inicio_periodo` date NOT NULL,
  `fecha_fin_periodo` date NOT NULL,
  `reglas_inscripcion` json DEFAULT NULL,
  `calendario_academico` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `singleton_check` CHECK ((`id` = 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_academica`
--

LOCK TABLES `configuracion_academica` WRITE;
/*!40000 ALTER TABLE `configuracion_academica` DISABLE KEYS */;
/*!40000 ALTER TABLE `configuracion_academica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curso_integraciones`
--

DROP TABLE IF EXISTS `curso_integraciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curso_integraciones` (
  `curso_id` int NOT NULL,
  `integracion_id` int NOT NULL,
  `configuracion_especifica` json DEFAULT NULL,
  PRIMARY KEY (`curso_id`,`integracion_id`),
  KEY `integracion_id` (`integracion_id`),
  CONSTRAINT `curso_integraciones_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `curso_integraciones_ibfk_2` FOREIGN KEY (`integracion_id`) REFERENCES `integraciones_externas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curso_integraciones`
--

LOCK TABLES `curso_integraciones` WRITE;
/*!40000 ALTER TABLE `curso_integraciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `curso_integraciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `tipo_curso` enum('presencial','virtual','hibrido') NOT NULL,
  `estado` enum('en_creacion','activo','finalizado','archivado') DEFAULT 'en_creacion',
  `estrategia_evaluacion` enum('ponderada','promedio_simple','por_competencias') DEFAULT 'ponderada',
  `configuracion_evaluacion` json DEFAULT NULL,
  `profesor_titular_id` int NOT NULL,
  `periodo_academico` varchar(20) NOT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `cupo_maximo` int DEFAULT '30',
  `cupo_actual` int DEFAULT '0',
  `curso_origen_id` int DEFAULT NULL,
  `metadata` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `profesor_titular_id` (`profesor_titular_id`),
  KEY `curso_origen_id` (`curso_origen_id`),
  KEY `idx_estado` (`estado`),
  KEY `idx_periodo` (`periodo_academico`),
  KEY `idx_tipo` (`tipo_curso`),
  FULLTEXT KEY `ft_cursos` (`nombre`,`descripcion`),
  CONSTRAINT `cursos_ibfk_1` FOREIGN KEY (`profesor_titular_id`) REFERENCES `profesores` (`usuario_id`),
  CONSTRAINT `cursos_ibfk_2` FOREIGN KEY (`curso_origen_id`) REFERENCES `cursos` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (4,'FVDRTR','Patrones de Diseño','Este curso te introduce a los patrones de diseño más utilizados en el desarrollo de software profesional. Aprenderás a identificar problemas comunes en la arquitectura de aplicaciones y a aplicar soluciones probadas que mejoran la estructura, legibilidad, mantenimiento y escalabilidad de tu código.','presencial','en_creacion','ponderada',NULL,1,'2025-2026',NULL,NULL,40,0,NULL,NULL,'2025-11-20 22:58:19','2025-11-20 22:58:19'),(5,'PRES-001','Matemáticas Avanzadas','Curso presencial de matemáticas','presencial','en_creacion','ponderada',NULL,1,'2024-1',NULL,NULL,40,0,NULL,NULL,'2025-11-21 00:49:19','2025-11-21 00:49:19'),(6,'VIRT-001','Programación Web','Curso virtual de desarrollo web','virtual','en_creacion','promedio_simple',NULL,1,'2024-1',NULL,NULL,100,0,NULL,NULL,'2025-11-21 00:49:20','2025-11-21 00:49:20'),(7,'HIBR-001','Inteligencia Artificial','Curso híbrido de IA y Machine Learning','hibrido','en_creacion','por_competencias',NULL,1,'2024-1',NULL,NULL,60,0,NULL,NULL,'2025-11-21 00:49:20','2025-11-21 00:49:20');
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entregas_evaluacion`
--

DROP TABLE IF EXISTS `entregas_evaluacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entregas_evaluacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `evaluacion_id` int NOT NULL,
  `estudiante_id` int NOT NULL,
  `intento_numero` int DEFAULT '1',
  `contenido_entrega` json DEFAULT NULL,
  `archivo_url` varchar(500) DEFAULT NULL,
  `fecha_entrega` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `calificacion` decimal(10,2) DEFAULT NULL,
  `retroalimentacion` text,
  `calificado_por` int DEFAULT NULL,
  `fecha_calificacion` timestamp NULL DEFAULT NULL,
  `estado` enum('pendiente','entregado','calificado','devuelto') DEFAULT 'pendiente',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_intento` (`evaluacion_id`,`estudiante_id`,`intento_numero`),
  KEY `calificado_por` (`calificado_por`),
  KEY `idx_estudiante` (`estudiante_id`),
  KEY `idx_estado` (`estado`),
  CONSTRAINT `entregas_evaluacion_ibfk_1` FOREIGN KEY (`evaluacion_id`) REFERENCES `evaluaciones` (`id`) ON DELETE CASCADE,
  CONSTRAINT `entregas_evaluacion_ibfk_2` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`usuario_id`),
  CONSTRAINT `entregas_evaluacion_ibfk_3` FOREIGN KEY (`calificado_por`) REFERENCES `profesores` (`usuario_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entregas_evaluacion`
--

LOCK TABLES `entregas_evaluacion` WRITE;
/*!40000 ALTER TABLE `entregas_evaluacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `entregas_evaluacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estadisticas_curso`
--

DROP TABLE IF EXISTS `estadisticas_curso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estadisticas_curso` (
  `curso_id` int NOT NULL,
  `total_inscritos` int DEFAULT '0',
  `promedio_calificaciones` decimal(4,2) DEFAULT NULL,
  `tasa_completado` decimal(5,2) DEFAULT NULL,
  `tiempo_promedio_minutos` int DEFAULT NULL,
  `participacion_foros` int DEFAULT '0',
  `ultima_actualizacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`curso_id`),
  CONSTRAINT `estadisticas_curso_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estadisticas_curso`
--

LOCK TABLES `estadisticas_curso` WRITE;
/*!40000 ALTER TABLE `estadisticas_curso` DISABLE KEYS */;
/*!40000 ALTER TABLE `estadisticas_curso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiantes`
--

DROP TABLE IF EXISTS `estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiantes` (
  `usuario_id` int NOT NULL,
  `matricula` varchar(20) NOT NULL,
  `programa_academico` varchar(100) DEFAULT NULL,
  `semestre` int DEFAULT NULL,
  `promedio_general` decimal(4,2) DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `matricula` (`matricula`),
  CONSTRAINT `estudiantes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiantes`
--

LOCK TABLES `estudiantes` WRITE;
/*!40000 ALTER TABLE `estudiantes` DISABLE KEYS */;
INSERT INTO `estudiantes` VALUES (4,'EST-2024-001','Ingeniería en Sistemas',5,8.50),(5,'EST-2024-002','Ingeniería en Sistemas',3,9.00),(6,'EST-2024-003','Ciencias de la Computación',7,8.80),(7,'EST-2024-004','Ingeniería en Software',4,8.20),(8,'EST-2024-005','Tecnologías de la Información',6,9.20);
/*!40000 ALTER TABLE `estudiantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluaciones`
--

DROP TABLE IF EXISTS `evaluaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluaciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `modulo_id` int NOT NULL,
  `tipo` enum('examen','tarea','proyecto','participacion','quiz') NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `puntaje_maximo` decimal(10,2) NOT NULL,
  `peso_porcentual` decimal(5,2) DEFAULT NULL,
  `competencias` json DEFAULT NULL,
  `intentos_permitidos` int DEFAULT '1',
  `tiempo_limite_minutos` int DEFAULT NULL,
  `fecha_apertura` datetime DEFAULT NULL,
  `fecha_cierre` datetime DEFAULT NULL,
  `rubrica` json DEFAULT NULL,
  `configuracion` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_modulo` (`modulo_id`),
  KEY `idx_fechas` (`fecha_apertura`,`fecha_cierre`),
  CONSTRAINT `evaluaciones_ibfk_1` FOREIGN KEY (`modulo_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluaciones`
--

LOCK TABLES `evaluaciones` WRITE;
/*!40000 ALTER TABLE `evaluaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `evaluaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `foros`
--

DROP TABLE IF EXISTS `foros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `foros` (
  `id` int NOT NULL AUTO_INCREMENT,
  `curso_id` int NOT NULL,
  `modulo_id` int DEFAULT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `tipo` enum('general','dudas','debate','anuncios') DEFAULT 'general',
  `activo` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `curso_id` (`curso_id`),
  KEY `modulo_id` (`modulo_id`),
  CONSTRAINT `foros_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `foros_ibfk_2` FOREIGN KEY (`modulo_id`) REFERENCES `modulos` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `foros`
--

LOCK TABLES `foros` WRITE;
/*!40000 ALTER TABLE `foros` DISABLE KEYS */;
/*!40000 ALTER TABLE `foros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_operaciones`
--

DROP TABLE IF EXISTS `historial_operaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_operaciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `entidad_tipo` varchar(50) NOT NULL,
  `entidad_id` int NOT NULL,
  `operacion` enum('crear','modificar','eliminar') NOT NULL,
  `datos_anteriores` json DEFAULT NULL,
  `datos_nuevos` json DEFAULT NULL,
  `revertido` tinyint(1) DEFAULT '0',
  `fecha_reversion` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `idx_entidad` (`entidad_tipo`,`entidad_id`),
  KEY `idx_fecha` (`created_at` DESC),
  CONSTRAINT `historial_operaciones_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_operaciones`
--

LOCK TABLES `historial_operaciones` WRITE;
/*!40000 ALTER TABLE `historial_operaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `historial_operaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inscripciones`
--

DROP TABLE IF EXISTS `inscripciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inscripciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `estudiante_id` int NOT NULL,
  `curso_id` int NOT NULL,
  `estado` enum('solicitada','validando','aprobada','rechazada','activa','completada','abandonada') DEFAULT 'solicitada',
  `fecha_solicitud` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_aprobacion` timestamp NULL DEFAULT NULL,
  `fecha_inicio_cursado` date DEFAULT NULL,
  `fecha_fin_cursado` date DEFAULT NULL,
  `calificacion_final` decimal(4,2) DEFAULT NULL,
  `porcentaje_completado` decimal(5,2) DEFAULT '0.00',
  `validaciones_pasadas` json DEFAULT NULL,
  `motivo_rechazo` text,
  `certificado_generado` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_inscripcion` (`estudiante_id`,`curso_id`),
  KEY `idx_estado` (`estado`),
  KEY `idx_curso` (`curso_id`),
  CONSTRAINT `inscripciones_ibfk_1` FOREIGN KEY (`estudiante_id`) REFERENCES `estudiantes` (`usuario_id`),
  CONSTRAINT `inscripciones_ibfk_2` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inscripciones`
--

LOCK TABLES `inscripciones` WRITE;
/*!40000 ALTER TABLE `inscripciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `inscripciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `integraciones_externas`
--

DROP TABLE IF EXISTS `integraciones_externas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `integraciones_externas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('videoconferencia','repositorio','autenticacion','pago','otro') NOT NULL,
  `proveedor` varchar(100) NOT NULL,
  `configuracion` json NOT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `integraciones_externas`
--

LOCK TABLES `integraciones_externas` WRITE;
/*!40000 ALTER TABLE `integraciones_externas` DISABLE KEYS */;
/*!40000 ALTER TABLE `integraciones_externas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiales`
--

DROP TABLE IF EXISTS `materiales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `modulo_id` int NOT NULL,
  `metadata_id` int DEFAULT NULL,
  `tipo` enum('video','pdf','documento','enlace','imagen','audio') NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `descripcion` text,
  `url_recurso` varchar(500) DEFAULT NULL,
  `tamano_bytes` bigint DEFAULT NULL,
  `duracion_segundos` int DEFAULT NULL,
  `orden` int DEFAULT '0',
  `requiere_visualizacion` tinyint(1) DEFAULT '0',
  `activo` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `metadata_id` (`metadata_id`),
  KEY `idx_modulo` (`modulo_id`),
  KEY `idx_tipo` (`tipo`),
  FULLTEXT KEY `ft_materiales` (`nombre`,`descripcion`),
  CONSTRAINT `materiales_ibfk_1` FOREIGN KEY (`modulo_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `materiales_ibfk_2` FOREIGN KEY (`metadata_id`) REFERENCES `materiales_metadata` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
/*!40000 ALTER TABLE `materiales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiales_metadata`
--

DROP TABLE IF EXISTS `materiales_metadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiales_metadata` (
  `id` int NOT NULL AUTO_INCREMENT,
  `autor` varchar(200) DEFAULT NULL,
  `licencia` varchar(100) DEFAULT NULL,
  `idioma` varchar(10) DEFAULT 'es',
  `etiquetas` json DEFAULT NULL,
  `derechos_autor` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales_metadata`
--

LOCK TABLES `materiales_metadata` WRITE;
/*!40000 ALTER TABLE `materiales_metadata` DISABLE KEYS */;
/*!40000 ALTER TABLE `materiales_metadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mensajes_foro`
--

DROP TABLE IF EXISTS `mensajes_foro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mensajes_foro` (
  `id` int NOT NULL AUTO_INCREMENT,
  `foro_id` int NOT NULL,
  `usuario_id` int NOT NULL,
  `mensaje_padre_id` int DEFAULT NULL,
  `contenido` text NOT NULL,
  `archivos_adjuntos` json DEFAULT NULL,
  `editado` tinyint(1) DEFAULT '0',
  `fecha_edicion` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `mensaje_padre_id` (`mensaje_padre_id`),
  KEY `idx_foro_fecha` (`foro_id`,`created_at` DESC),
  CONSTRAINT `mensajes_foro_ibfk_1` FOREIGN KEY (`foro_id`) REFERENCES `foros` (`id`) ON DELETE CASCADE,
  CONSTRAINT `mensajes_foro_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `mensajes_foro_ibfk_3` FOREIGN KEY (`mensaje_padre_id`) REFERENCES `mensajes_foro` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mensajes_foro`
--

LOCK TABLES `mensajes_foro` WRITE;
/*!40000 ALTER TABLE `mensajes_foro` DISABLE KEYS */;
/*!40000 ALTER TABLE `mensajes_foro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modulos`
--

DROP TABLE IF EXISTS `modulos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `modulos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `curso_id` int NOT NULL,
  `modulo_padre_id` int DEFAULT NULL,
  `tipo` enum('unidad','leccion','actividad','evaluacion') NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `orden` int NOT NULL,
  `duracion_estimada` int DEFAULT NULL,
  `fecha_disponible` datetime DEFAULT NULL,
  `fecha_limite` datetime DEFAULT NULL,
  `obligatorio` tinyint(1) DEFAULT '1',
  `configuracion` json DEFAULT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_curso_orden` (`curso_id`,`orden`),
  KEY `idx_padre` (`modulo_padre_id`),
  FULLTEXT KEY `ft_modulos` (`nombre`,`descripcion`),
  CONSTRAINT `modulos_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `modulos_ibfk_2` FOREIGN KEY (`modulo_padre_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modulos`
--

LOCK TABLES `modulos` WRITE;
/*!40000 ALTER TABLE `modulos` DISABLE KEYS */;
/*!40000 ALTER TABLE `modulos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificaciones`
--

DROP TABLE IF EXISTS `notificaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `tipo` enum('actividad','tarea','calificacion','anuncio','recordatorio','sistema') NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `mensaje` text NOT NULL,
  `datos_adicionales` json DEFAULT NULL,
  `leida` tinyint(1) DEFAULT '0',
  `fecha_lectura` timestamp NULL DEFAULT NULL,
  `prioridad` enum('baja','media','alta','urgente') DEFAULT 'media',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_usuario_leida` (`usuario_id`,`leida`),
  KEY `idx_fecha` (`created_at` DESC),
  CONSTRAINT `notificaciones_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificaciones`
--

LOCK TABLES `notificaciones` WRITE;
/*!40000 ALTER TABLE `notificaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesores`
--

DROP TABLE IF EXISTS `profesores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profesores` (
  `usuario_id` int NOT NULL,
  `numero_empleado` varchar(20) NOT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `especialidad` varchar(200) DEFAULT NULL,
  `grado_academico` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `numero_empleado` (`numero_empleado`),
  CONSTRAINT `profesores_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesores`
--

LOCK TABLES `profesores` WRITE;
/*!40000 ALTER TABLE `profesores` DISABLE KEYS */;
INSERT INTO `profesores` VALUES (1,'PROF-001','Ciencias Exactas','Matemáticas','Doctorado'),(2,'PROF-002','Ingeniería','Sistemas Computacionales','Maestría'),(3,'PROF-003','Tecnologías','Inteligencia Artificial','Doctorado');
/*!40000 ALTER TABLE `profesores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progreso_estudiante`
--

DROP TABLE IF EXISTS `progreso_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progreso_estudiante` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscripcion_id` int NOT NULL,
  `modulo_id` int NOT NULL,
  `estado` enum('no_iniciado','en_progreso','completado') DEFAULT 'no_iniciado',
  `porcentaje_completado` decimal(5,2) DEFAULT '0.00',
  `tiempo_dedicado_minutos` int DEFAULT '0',
  `ultima_posicion` json DEFAULT NULL,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_completado` timestamp NULL DEFAULT NULL,
  `intentos` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_progreso` (`inscripcion_id`,`modulo_id`),
  KEY `modulo_id` (`modulo_id`),
  KEY `idx_inscripcion` (`inscripcion_id`),
  CONSTRAINT `progreso_estudiante_ibfk_1` FOREIGN KEY (`inscripcion_id`) REFERENCES `inscripciones` (`id`) ON DELETE CASCADE,
  CONSTRAINT `progreso_estudiante_ibfk_2` FOREIGN KEY (`modulo_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progreso_estudiante`
--

LOCK TABLES `progreso_estudiante` WRITE;
/*!40000 ALTER TABLE `progreso_estudiante` DISABLE KEYS */;
/*!40000 ALTER TABLE `progreso_estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progreso_snapshots`
--

DROP TABLE IF EXISTS `progreso_snapshots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `progreso_snapshots` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inscripcion_id` int NOT NULL,
  `snapshot_data` json NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_inscripcion_fecha` (`inscripcion_id`,`created_at` DESC),
  CONSTRAINT `progreso_snapshots_ibfk_1` FOREIGN KEY (`inscripcion_id`) REFERENCES `inscripciones` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progreso_snapshots`
--

LOCK TABLES `progreso_snapshots` WRITE;
/*!40000 ALTER TABLE `progreso_snapshots` DISABLE KEYS */;
/*!40000 ALTER TABLE `progreso_snapshots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reportes_generados`
--

DROP TABLE IF EXISTS `reportes_generados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reportes_generados` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo_reporte` varchar(100) NOT NULL,
  `parametros` json DEFAULT NULL,
  `generado_por` int NOT NULL,
  `archivo_url` varchar(500) DEFAULT NULL,
  `estado` enum('procesando','completado','error') DEFAULT 'procesando',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `generado_por` (`generado_por`),
  CONSTRAINT `reportes_generados_ibfk_1` FOREIGN KEY (`generado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reportes_generados`
--

LOCK TABLES `reportes_generados` WRITE;
/*!40000 ALTER TABLE `reportes_generados` DISABLE KEYS */;
/*!40000 ALTER TABLE `reportes_generados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suscripciones_notificacion`
--

DROP TABLE IF EXISTS `suscripciones_notificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suscripciones_notificacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `curso_id` int DEFAULT NULL,
  `tipo_evento` varchar(50) NOT NULL,
  `canal` enum('sistema','email','push','sms') DEFAULT 'sistema',
  `activo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_suscripcion` (`usuario_id`,`curso_id`,`tipo_evento`,`canal`),
  KEY `curso_id` (`curso_id`),
  CONSTRAINT `suscripciones_notificacion_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `suscripciones_notificacion_ibfk_2` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suscripciones_notificacion`
--

LOCK TABLES `suscripciones_notificacion` WRITE;
/*!40000 ALTER TABLE `suscripciones_notificacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `suscripciones_notificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `tipo_usuario` enum('estudiante','profesor','administrador') NOT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `ultimo_acceso` timestamp NULL DEFAULT NULL,
  `preferencias` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_tipo_usuario` (`tipo_usuario`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'gali@gmail.com','12341234','Galilea','Santiago Jimenez','profesor',1,NULL,NULL,'2025-11-20 22:40:26','2025-11-20 22:40:26'),(2,'hector@universidad.edu','$2a$10$hash1','Hector','Pérez García','profesor',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(3,'fausto@universidad.edu','$2a$10$hash2','Fausto','López Rodríguez','profesor',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(4,'magdalena@universidad.edu','$2a$10$hash3','Magdalena','Martínez Sánchez','profesor',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(5,'claraAurora@universidad.edu','$2a$10$hash4','Clara Aurora','González Díaz','estudiante',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(6,'carolina@universidad.edu','$2a$10$hash5','Carolina','Hernández Torres','estudiante',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(7,'enmannuel@universidad.edu','$2a$10$hash6','Enmanuel','Ramírez Flores','estudiante',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(8,'omar@universidad.edu','$2a$10$hash7','Omar','Jiménez Castro','estudiante',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(9,'dalia@universidad.edu','$2a$10$hash8','Dalia','Morales Ruiz','estudiante',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40'),(10,'admin@universidad.edu','$2a$10$hash9','Admin','Sistema','administrador',1,NULL,NULL,'2025-11-20 22:56:40','2025-11-20 22:56:40');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-20 21:33:03
