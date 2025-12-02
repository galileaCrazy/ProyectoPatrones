-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: edulearn
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
-- Table structure for table `configuraciones_sistema`
--

DROP TABLE IF EXISTS `configuraciones_sistema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuraciones_sistema` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `clave` varchar(100) NOT NULL,
  `valor` varchar(500) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `tipo` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `clave` (`clave`),
  KEY `idx_clave` (`clave`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuraciones_sistema`
--

LOCK TABLES `configuraciones_sistema` WRITE;
/*!40000 ALTER TABLE `configuraciones_sistema` DISABLE KEYS */;
INSERT INTO `configuraciones_sistema` VALUES (1,'nombre_sistema','EduLearn Platform','Nombre del sistema LMS','STRING'),(2,'version','2.0.0','Versión del sistema','STRING'),(3,'max_intentos_login','3','Máximo de intentos de login antes de bloqueo','INTEGER'),(4,'duracion_sesion_minutos','60','Duración de la sesión en minutos','INTEGER'),(5,'cupo_default','30','Cupo por defecto para nuevos cursos','INTEGER'),(6,'calificacion_minima_aprobacion','60','Calificación mínima para aprobar (0-100)','INTEGER'),(7,'permitir_registro_estudiantes','true','Permitir auto-registro de estudiantes','BOOLEAN'),(8,'modo_mantenimiento','false','Sistema en modo mantenimiento','BOOLEAN'),(9,'email_notificaciones','noreply@edulearn.com','Email para envío de notificaciones','STRING'),(10,'url_base','http://localhost:3000','URL base del frontend','STRING');
/*!40000 ALTER TABLE `configuraciones_sistema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenidos_educativos`
--

DROP TABLE IF EXISTS `contenidos_educativos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contenidos_educativos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo` varchar(50) NOT NULL,
  `nivel` varchar(50) NOT NULL,
  `descripcion` text,
  `duracion_estimada` int DEFAULT NULL,
  `contenido_renderizado` text,
  `fecha_creacion` datetime DEFAULT NULL,
  `curso_id` bigint DEFAULT NULL,
  `activo` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_tipo` (`tipo`),
  KEY `idx_nivel` (`nivel`),
  KEY `idx_tipo_nivel` (`tipo`,`nivel`),
  KEY `idx_curso_id` (`curso_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenidos_educativos`
--

LOCK TABLES `contenidos_educativos` WRITE;
/*!40000 ALTER TABLE `contenidos_educativos` DISABLE KEYS */;
/*!40000 ALTER TABLE `contenidos_educativos` ENABLE KEYS */;
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
  `tipo_curso` varchar(50) NOT NULL DEFAULT 'virtual',
  `estado` varchar(20) NOT NULL DEFAULT 'activo',
  `estrategia_evaluacion` varchar(50) DEFAULT NULL,
  `configuracion_evaluacion` json DEFAULT NULL,
  `profesor_titular_id` int NOT NULL,
  `periodo_academico` varchar(20) NOT NULL,
  `duracion` int DEFAULT '40',
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
  KEY `idx_codigo_cursos` (`codigo`),
  KEY `idx_tipo_curso_cursos` (`tipo_curso`),
  KEY `idx_estado_cursos` (`estado`),
  KEY `idx_profesor_cursos` (`profesor_titular_id`),
  KEY `idx_periodo_cursos` (`periodo_academico`),
  FULLTEXT KEY `ft_cursos` (`nombre`,`descripcion`),
  CONSTRAINT `cursos_ibfk_1` FOREIGN KEY (`profesor_titular_id`) REFERENCES `profesores` (`usuario_id`),
  CONSTRAINT `cursos_ibfk_2` FOREIGN KEY (`curso_origen_id`) REFERENCES `cursos` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (4,'FVDRTR','Patrones de Dise??o','Este curso te introduce a los patrones de dise??o m??s utilizados en el desarrollo de software profesional. Aprender??s a identificar problemas comunes en la arquitectura de aplicaciones y a aplicar soluciones probadas que mejoran la estructura, legibilidad, mantenimiento y escalabilidad de tu c??digo.','presencial','en_creacion','ponderada',NULL,1,'2025-2026',40,NULL,NULL,'2025-12-01 06:46:20','2025-12-01 06:46:20',40,0,NULL,NULL,'2025-11-20 22:58:19','2025-11-20 22:58:19'),(5,'PRES-001','Matem??ticas Avanzadas','Curso presencial de matem??ticas','presencial','en_creacion','ponderada',NULL,1,'2024-1',40,NULL,NULL,'2025-12-01 06:46:20','2025-12-01 06:46:20',40,0,NULL,NULL,'2025-11-21 00:49:19','2025-11-21 00:49:19'),(6,'VIRT-001','Programaci??n Web','Curso virtual de desarrollo web','virtual','en_creacion','promedio_simple',NULL,1,'2024-1',40,NULL,NULL,'2025-12-01 06:46:20','2025-12-01 06:46:20',100,0,NULL,NULL,'2025-11-21 00:49:20','2025-11-21 00:49:20'),(7,'HIBR-001','Inteligencia Artificial','Curso h??brido de IA y Machine Learning','hibrido','en_creacion','por_competencias',NULL,1,'2024-1',40,NULL,NULL,'2025-12-01 06:46:20','2025-12-01 16:22:55',60,0,NULL,NULL,'2025-11-21 00:49:20','2025-12-01 22:22:55'),(13,'CURSO-1764617740684','Curso de programación','En este curso de programación nos enfocaremos en Python. Aprenderás las habilidades fundamentales de la programación a través de explicaciones prácticas sobre el funcionamiento de algoritmos y el pensamiento lógico para la resolución de problemas. Dominarás el uso de funciones y variables, condicionales, bucles y excepciones.','presencial','ACTIVO','ponderada',NULL,1,'Enero-Junio 2026',40,NULL,NULL,'2025-12-01 13:35:40','2025-12-01 16:22:55',30,0,NULL,NULL,'2025-12-01 19:35:40','2025-12-01 22:22:55'),(14,'CURSO-1764617868782','Full Stack Java','Conviértete en un programador capaz de impulsar todas las fases del desarrollo de software, sitios web y apps. Desarrolla desde la interfaz hasta las estructuras internas y las bases de datos. Aprende a programar soluciones complejas e innovadoras con Java y haz crecer tu carrera.','virtual','ACTIVO','simple',NULL,1,'Enero-Junio 2026',40,NULL,NULL,'2025-12-01 13:37:48','2025-12-01 16:22:55',30,0,NULL,NULL,'2025-12-01 19:37:48','2025-12-01 22:22:55');
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
  `id` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `codigo_estudiante` varchar(50) DEFAULT NULL,
  `programa` varchar(100) DEFAULT NULL,
  `nivel_academico` varchar(50) DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'ACTIVO',
  `matricula` varchar(20) NOT NULL,
  `programa_academico` varchar(100) DEFAULT NULL,
  `semestre` int DEFAULT NULL,
  `promedio_general` decimal(4,2) DEFAULT NULL,
  PRIMARY KEY (`usuario_id`),
  UNIQUE KEY `matricula` (`matricula`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `uk_usuario_id` (`usuario_id`),
  UNIQUE KEY `uk_codigo_estudiante` (`codigo_estudiante`),
  KEY `idx_usuario_id_estudiantes` (`usuario_id`),
  KEY `idx_codigo_estudiantes` (`codigo_estudiante`),
  CONSTRAINT `estudiantes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiantes`
--

LOCK TABLES `estudiantes` WRITE;
/*!40000 ALTER TABLE `estudiantes` DISABLE KEYS */;
INSERT INTO `estudiantes` VALUES (1,4,'EST-2024-001','Ingenier??a en Sistemas',NULL,NULL,'ACTIVO','EST-2024-001','Ingenier??a en Sistemas',5,8.50),(2,5,'EST-2024-002','Ingenier??a en Sistemas',NULL,NULL,'ACTIVO','EST-2024-002','Ingenier??a en Sistemas',3,9.00),(3,6,'EST-2024-003','Ciencias de la Computaci??n',NULL,NULL,'ACTIVO','EST-2024-003','Ciencias de la Computaci??n',7,8.80),(4,7,'EST-2024-004','Ingenier??a en Software',NULL,NULL,'ACTIVO','EST-2024-004','Ingenier??a en Software',4,8.20),(5,8,'EST-2024-005','Tecnolog??as de la Informaci??n',NULL,NULL,'ACTIVO','EST-2024-005','Tecnolog??as de la Informaci??n',6,9.20),(6,16,'EST791724',NULL,NULL,NULL,'ACTIVO','EST791724',NULL,NULL,NULL);
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
  `titulo` varchar(255) NOT NULL DEFAULT '',
  `tipo` varchar(50) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `tipo_evaluacion` varchar(50) NOT NULL DEFAULT '',
  `puntaje_maximo` decimal(10,2) NOT NULL,
  `peso_porcentual` decimal(5,2) DEFAULT NULL,
  `competencias` json DEFAULT NULL,
  `intentos_permitidos` int DEFAULT '1',
  `tiempo_limite_minutos` int DEFAULT NULL,
  `fecha_apertura` datetime DEFAULT NULL,
  `fecha_cierre` datetime DEFAULT NULL,
  `instrucciones` text,
  `estado` varchar(20) DEFAULT 'activa',
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `rubrica` json DEFAULT NULL,
  `configuracion` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_modulo` (`modulo_id`),
  KEY `idx_fechas` (`fecha_apertura`,`fecha_cierre`),
  KEY `idx_curso_id_evaluaciones` (`modulo_id`),
  KEY `idx_tipo_evaluacion_evaluaciones` (`tipo_evaluacion`),
  KEY `idx_estado_evaluaciones` (`estado`),
  KEY `idx_fecha_cierre_evaluaciones` (`fecha_cierre`),
  CONSTRAINT `evaluaciones_ibfk_1` FOREIGN KEY (`modulo_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluaciones`
--

LOCK TABLES `evaluaciones` WRITE;
/*!40000 ALTER TABLE `evaluaciones` DISABLE KEYS */;
INSERT INTO `evaluaciones` VALUES (5,14,'Examen Parcial de Herencia','EXAMEN','Evaluación de Herencia','Evalúa herencia, clases derivadas y sobrescritura.','EXAMEN',100.00,20.00,'[\"Comprensión de herencia\", \"Identificación de jerarquías\"]',2,60,'2025-12-01 16:56:24','2025-12-08 16:56:24','Responde cada pregunta cuidadosamente.','activa','2025-12-01 16:56:24','2025-12-01 16:56:24',NULL,NULL,'2025-12-01 22:56:24');
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
  `estado` varchar(20) DEFAULT 'ACTIVA',
  `fecha_solicitud` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_aprobacion` timestamp NULL DEFAULT NULL,
  `fecha_inicio_cursado` date DEFAULT NULL,
  `fecha_fin_cursado` date DEFAULT NULL,
  `calificacion_final` decimal(4,2) DEFAULT NULL,
  `fecha_finalizacion` datetime DEFAULT NULL,
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
  KEY `idx_estudiante_inscripciones` (`estudiante_id`),
  KEY `idx_curso_inscripciones` (`curso_id`),
  KEY `idx_estado_inscripciones` (`estado`),
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
  `tipo_sistema` varchar(50) NOT NULL DEFAULT '',
  `nombre_configuracion` varchar(255) NOT NULL DEFAULT '',
  `api_key` varchar(500) DEFAULT NULL,
  `api_secret` varchar(500) DEFAULT NULL,
  `url_base` varchar(500) DEFAULT NULL,
  `estado` varchar(50) NOT NULL DEFAULT 'ACTIVO',
  `curso_id` int DEFAULT NULL,
  `sala_reunion` text,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `ultima_sincronizacion` datetime DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` varchar(50) NOT NULL,
  `proveedor` varchar(100) NOT NULL,
  `configuracion` json NOT NULL,
  `activo` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_tipo_sistema_integraciones` (`tipo_sistema`),
  KEY `idx_estado_integraciones` (`estado`),
  KEY `idx_curso_id_integraciones` (`curso_id`),
  CONSTRAINT `fk_integraciones_curso` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE
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
  `curso_id` int DEFAULT NULL,
  `titulo` varchar(255) NOT NULL DEFAULT '',
  `metadata_id` int DEFAULT NULL,
  `tipo` enum('video','pdf','documento','enlace','imagen','audio') NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `descripcion` text,
  `tipo_material` varchar(50) NOT NULL DEFAULT '',
  `url_recurso` varchar(500) DEFAULT NULL,
  `archivo_path` varchar(500) DEFAULT NULL,
  `tamano_bytes` bigint DEFAULT NULL,
  `duracion_segundos` int DEFAULT NULL,
  `orden` int DEFAULT '0',
  `es_obligatorio` tinyint(1) DEFAULT '0',
  `requiere_visualizacion` tinyint(1) DEFAULT '0',
  `estado` varchar(20) DEFAULT 'activo',
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `activo` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `metadata_id` (`metadata_id`),
  KEY `idx_modulo` (`modulo_id`),
  KEY `idx_tipo` (`tipo`),
  KEY `idx_modulo_id_materiales` (`modulo_id`),
  KEY `idx_curso_id_materiales` (`curso_id`),
  KEY `idx_tipo_material_materiales` (`tipo_material`),
  KEY `idx_orden_materiales` (`modulo_id`,`orden`),
  FULLTEXT KEY `ft_materiales` (`nombre`,`descripcion`),
  CONSTRAINT `fk_materiales_curso` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `materiales_ibfk_1` FOREIGN KEY (`modulo_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `materiales_ibfk_2` FOREIGN KEY (`metadata_id`) REFERENCES `materiales_metadata` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
INSERT INTO `materiales` VALUES (8,13,4,'Video: ¿Qué es POO?',NULL,'video','Video: ¿Qué es POO?','Introducción a los conceptos de POO','VIDEO','https://youtube.com/watch?v=example1',NULL,NULL,900,1,1,0,'activo','2025-12-01 16:49:06','2025-12-01 16:49:06',1,'2025-12-01 22:49:06'),(9,13,4,'Lectura: Historia de POO',NULL,'pdf','Lectura: Historia de POO','Documento sobre la evolución de la programación orientada a objetos','PDF','/recursos/historia-poo.pdf',NULL,NULL,1200,2,1,0,'activo','2025-12-01 16:49:06','2025-12-01 16:49:06',1,'2025-12-01 22:49:06'),(10,13,4,'Presentación: Paradigmas de Programación',NULL,'pdf','Presentación: Paradigmas de Programación','Comparación entre diferentes paradigmas','DOCUMENTO','/recursos/paradigmas.pdf',NULL,NULL,600,3,0,0,'activo','2025-12-01 16:49:06','2025-12-01 16:49:06',1,'2025-12-01 22:49:06'),(11,15,4,'Video: Introducción a Herencia',NULL,'video','Video: Introducción a Herencia','Conceptos básicos de herencia','VIDEO','https://youtube.com/watch?v=example8',NULL,NULL,1200,1,1,0,'activo','2025-12-01 16:51:48','2025-12-01 16:51:48',1,'2025-12-01 22:51:48');
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
  `titulo` varchar(255) NOT NULL DEFAULT '',
  `modulo_padre_id` int DEFAULT NULL,
  `tipo` varchar(50) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `orden` int NOT NULL,
  `estado` varchar(20) DEFAULT 'draft',
  `duracion_estimada` int DEFAULT NULL,
  `fecha_creacion` datetime DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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
  KEY `idx_curso_id_modulos` (`curso_id`),
  KEY `idx_orden_modulos` (`curso_id`,`orden`),
  KEY `idx_estado_modulos` (`estado`),
  FULLTEXT KEY `ft_modulos` (`nombre`,`descripcion`),
  CONSTRAINT `modulos_ibfk_1` FOREIGN KEY (`curso_id`) REFERENCES `cursos` (`id`) ON DELETE CASCADE,
  CONSTRAINT `modulos_ibfk_2` FOREIGN KEY (`modulo_padre_id`) REFERENCES `modulos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modulos`
--

LOCK TABLES `modulos` WRITE;
/*!40000 ALTER TABLE `modulos` DISABLE KEYS */;
INSERT INTO `modulos` VALUES (1,13,'Unidad 1. Introducción a la programación con Python',NULL,'contenido','Unidad 1. Introducción a la programación con Python','',1,'draft',NULL,'2025-12-01 13:35:41','2025-12-01 13:35:41',NULL,NULL,1,NULL,1,'2025-12-01 19:35:40','2025-12-01 19:35:40'),(2,13,'Unidad 2. Condicionales',NULL,'contenido','Unidad 2. Condicionales','',2,'draft',NULL,'2025-12-01 13:35:41','2025-12-01 13:35:41',NULL,NULL,1,NULL,1,'2025-12-01 19:35:40','2025-12-01 19:35:40'),(3,13,'Unidad 3. Bucles',NULL,'contenido','Unidad 3. Bucles','',3,'draft',NULL,'2025-12-01 13:35:41','2025-12-01 13:35:41',NULL,NULL,1,NULL,1,'2025-12-01 19:35:40','2025-12-01 19:35:40'),(4,13,'Unidad 4. Excepciones',NULL,'contenido','Unidad 4. Excepciones','',4,'draft',NULL,'2025-12-01 13:35:41','2025-12-01 13:35:41',NULL,NULL,1,NULL,1,'2025-12-01 19:35:40','2025-12-01 19:35:40'),(5,14,'Modulo 1',NULL,'contenido','Modulo 1','',1,'draft',NULL,'2025-12-01 13:37:49','2025-12-01 13:37:49',NULL,NULL,1,NULL,1,'2025-12-01 19:37:48','2025-12-01 19:37:48'),(6,14,'Modulo 2',NULL,'contenido','Modulo 2','',2,'draft',NULL,'2025-12-01 13:37:49','2025-12-01 13:37:49',NULL,NULL,1,NULL,1,'2025-12-01 19:37:48','2025-12-01 19:37:48'),(7,14,'Modulo 3',NULL,'contenido','Modulo 3','',3,'draft',NULL,'2025-12-01 13:37:49','2025-12-01 13:37:49',NULL,NULL,1,NULL,1,'2025-12-01 19:37:48','2025-12-01 19:37:48'),(8,14,'Modulo 4',NULL,'contenido','Modulo 4','',4,'draft',NULL,'2025-12-01 13:37:49','2025-12-01 13:37:49',NULL,NULL,1,NULL,1,'2025-12-01 19:37:48','2025-12-01 19:37:48'),(9,14,'Modulo 5',NULL,'contenido','Modulo 5','',5,'draft',NULL,'2025-12-01 13:37:49','2025-12-01 13:37:49',NULL,NULL,1,NULL,1,'2025-12-01 19:37:48','2025-12-01 19:37:48'),(10,14,'Modulo 6',NULL,'contenido','Modulo 6','',6,'draft',NULL,'2025-12-01 13:37:49','2025-12-01 13:37:49',NULL,NULL,1,NULL,1,'2025-12-01 19:37:48','2025-12-01 19:37:48'),(12,4,'Introducción a POO',NULL,'contenido','Introducción a POO','En este módulo aprenderás los conceptos fundamentales de la Programación Orientada a Objetos',1,'published',180,'2025-12-01 16:47:10','2025-12-01 16:47:10',NULL,NULL,1,NULL,1,'2025-12-01 22:47:10','2025-12-01 22:47:10'),(13,4,'Conceptos Fundamentales',12,'contenido','Conceptos Fundamentales','Definiciones básicas y principios de POO',1,'published',90,'2025-12-01 16:47:10','2025-12-01 16:47:10',NULL,NULL,1,NULL,1,'2025-12-01 22:47:10','2025-12-01 22:47:10'),(14,4,'Herencia y Polimorfismo',NULL,'contenido','Herencia y Polimorfismo','Conceptos avanzados de reutilización de código',3,'draft',300,'2025-12-01 16:49:15','2025-12-01 16:49:15',NULL,NULL,1,NULL,1,'2025-12-01 22:49:15','2025-12-01 22:49:15'),(15,4,'Herencia Simple',14,'contenido','Herencia Simple','Extender clases y heredar comportamiento',1,'draft',120,'2025-12-01 16:49:15','2025-12-01 16:49:15',NULL,NULL,1,NULL,1,'2025-12-01 22:49:15','2025-12-01 22:49:15');
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
-- Table structure for table `notificaciones_patron`
--

DROP TABLE IF EXISTS `notificaciones_patron`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificaciones_patron` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tipo` varchar(50) NOT NULL,
  `destinatario` varchar(255) NOT NULL,
  `asunto` varchar(255) DEFAULT NULL,
  `mensaje` text NOT NULL,
  `estado` varchar(50) DEFAULT 'PENDIENTE',
  `fecha_creacion` datetime DEFAULT NULL,
  `fecha_envio` datetime DEFAULT NULL,
  `intentos` int DEFAULT '0',
  `error` text,
  PRIMARY KEY (`id`),
  KEY `idx_tipo` (`tipo`),
  KEY `idx_estado` (`estado`),
  KEY `idx_destinatario` (`destinatario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificaciones_patron`
--

LOCK TABLES `notificaciones_patron` WRITE;
/*!40000 ALTER TABLE `notificaciones_patron` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificaciones_patron` ENABLE KEYS */;
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
INSERT INTO `profesores` VALUES (1,'PROF-001','Ciencias Exactas','Matem??ticas','Doctorado'),(2,'PROF-002','Ingenier??a','Sistemas Computacionales','Maestr??a'),(3,'PROF-003','Tecnolog??as','Inteligencia Artificial','Doctorado');
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
  `password` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `tipo_usuario` varchar(50) NOT NULL,
  `estado` varchar(20) DEFAULT 'ACTIVO',
  `fecha_registro` datetime DEFAULT CURRENT_TIMESTAMP,
  `activo` tinyint(1) DEFAULT '1',
  `ultimo_acceso` timestamp NULL DEFAULT NULL,
  `preferencias` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_tipo_usuario` (`tipo_usuario`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'gali@gmail.com','12341234','12341234','Galilea','Santiago Jimenez','profesor','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:40:26','2025-12-01 12:48:05'),(2,'hector@universidad.edu','$2a$10$hash1','$2a$10$hash1','Hector','P??rez Garc??a','profesor','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(3,'fausto@universidad.edu','$2a$10$hash2','$2a$10$hash2','Fausto','L??pez Rodr??guez','profesor','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(4,'magdalena@universidad.edu','$2a$10$hash3','$2a$10$hash3','Magdalena','Mart??nez S??nchez','profesor','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(5,'claraAurora@universidad.edu','$2a$10$hash4','$2a$10$hash4','Clara Aurora','Gonz??lez D??az','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(6,'carolina@universidad.edu','$2a$10$hash5','$2a$10$hash5','Carolina','Hern??ndez Torres','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(7,'enmannuel@universidad.edu','$2a$10$hash6','$2a$10$hash6','Enmanuel','Ram??rez Flores','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(8,'omar@universidad.edu','$2a$10$hash7','$2a$10$hash7','Omar','Jim??nez Castro','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(9,'dalia@universidad.edu','$2a$10$hash8','$2a$10$hash8','Dalia','Morales Ruiz','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(10,'admin@universidad.edu','$2a$10$hash9','$2a$10$hash9','Admin','Sistema','administrador','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-20 22:56:40','2025-12-01 12:48:05'),(11,'estudiante@demo.com','estudiante123','estudiante123','Estudiante','Demo','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-29 14:41:51','2025-12-01 12:48:05'),(12,'admin@demo.com','admin123','admin123','Admin','Demo','administrador','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-29 14:41:51','2025-12-01 12:48:05'),(13,'admin@itoaxca.edu.mx','$2a$10$TaAU9mZO2UZ6QRq2UZ6QRvWO2UZ6QRq2UZ6QRq2UZ6QRq2UZ6QRq2U','$2a$10$TaAU9mZO2UZ6QRq2UZ6QRvWO2UZ6QRq2UZ6QRq2UZ6QRq2UZ6QRq2U','Administrador','Principal','administrador','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-30 15:07:21','2025-12-01 12:48:05'),(14,'david@itoaxaca.edu.mx','$2a$10$QXgDHR5HfMO/c9ZgEzhWLeiy2FDo4KduicazrlEgkc5Yqhvw3Q9F2','$2a$10$QXgDHR5HfMO/c9ZgEzhWLeiy2FDo4KduicazrlEgkc5Yqhvw3Q9F2','David','Almaraz Vasquez','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-30 16:22:47','2025-12-01 12:48:05'),(16,'22160991@itoaxaca.edu.mx','$2a$10$bctm.01.bL/ocitnQ/U8bebfQ/17tUA5GDT0YRai1o0bOh1bcYWW6','$2a$10$bctm.01.bL/ocitnQ/U8bebfQ/17tUA5GDT0YRai1o0bOh1bcYWW6','David','Almaraz Vasquez','estudiante','ACTIVO','2025-12-01 06:47:57',1,NULL,NULL,'2025-11-30 16:38:08','2025-12-01 12:48:05');
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

-- Dump completed on 2025-12-01 17:02:52
