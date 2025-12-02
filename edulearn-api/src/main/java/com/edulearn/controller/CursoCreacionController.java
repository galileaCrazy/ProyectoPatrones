package com.edulearn.controller;

import com.edulearn.service.CursoCreacionService;
import com.edulearn.service.CursoCreacionService.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * CONTROLADOR PARA CREACIÓN DE CURSOS COMPLETOS
 * ==============================================
 * 
 * Este controlador expone endpoints para crear cursos completos
 * usando los patrones Abstract Factory y Builder.
 * 
 * Endpoints:
 * - POST /api/cursos/crear-completo: Crear curso con módulos, materiales y evaluaciones
 */
@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoCreacionController {

    private static final Logger logger = LoggerFactory.getLogger(CursoCreacionController.class);

    @Autowired
    private CursoCreacionService cursoCreacionService;

    /**
     * Crea un curso completo con módulos, materiales y evaluaciones
     * 
     * POST /api/cursos/crear-completo
     * 
     * Body JSON:
     * {
     *   "nombre": "Programación Web",
     *   "descripcion": "Curso completo de desarrollo web",
     *   "tipoCurso": "virtual",
     *   "profesorId": 2,
     *   "periodoAcademico": "2024-1",
     *   "duracion": 80,
     *   "cupoMaximo": 50,
     *   "estrategiaEvaluacion": "ponderada",
     *   "modulos": [
     *     {
     *       "titulo": "Módulo 1: Introducción",
     *       "descripcion": "Introducción al desarrollo web",
     *       "materiales": [
     *         {"nombre": "Video 1", "descripcion": "Conceptos básicos"},
     *         {"nombre": "PDF Guía", "descripcion": "Material de apoyo"}
     *       ],
     *       "evaluaciones": [
     *         {"nombre": "Quiz 1", "descripcion": "Evaluación de conceptos"}
     *       ]
     *     }
     *   ]
     * }
     */
    @PostMapping("/crear-completo")
    public ResponseEntity<Map<String, Object>> crearCursoCompleto(@RequestBody CursoCreacionRequest request) {
        logger.info("📥 Recibida solicitud de creación de curso: {}", request.nombre);

        try {
            // Validaciones básicas
            if (request.nombre == null || request.nombre.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("exito", false);
                error.put("mensaje", "El nombre del curso es obligatorio");
                return ResponseEntity.badRequest().body(error);
            }

            // Delegar al servicio
            Map<String, Object> resultado = cursoCreacionService.crearCursoCompleto(request);

            // Verificar éxito
            Boolean exito = (Boolean) resultado.get("exito");
            if (exito) {
                return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultado);
            }

        } catch (Exception e) {
            logger.error("❌ Error en el controlador: {}", e.getMessage(), e);
            logger.error("❌ Causa raíz:", e.getCause());

            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al procesar la solicitud: " + e.getMessage());
            error.put("errorTipo", e.getClass().getSimpleName());

            // Si hay una causa raíz, incluirla
            if (e.getCause() != null) {
                error.put("causaRaiz", e.getCause().getMessage());
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint simplificado para crear curso básico con un módulo de ejemplo
     * Útil para pruebas rápidas
     */
    @PostMapping("/crear-rapido")
    public ResponseEntity<Map<String, Object>> crearCursoRapido(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "virtual") String tipoCurso,
            @RequestParam Integer profesorId,
            @RequestParam(defaultValue = "2024-1") String periodoAcademico) {

        logger.info("⚡ Creación rápida de curso: {}", nombre);

        try {
            // Crear request con un módulo de ejemplo
            CursoCreacionRequest request = new CursoCreacionRequest();
            request.nombre = nombre;
            request.descripcion = "Curso creado mediante creación rápida";
            request.tipoCurso = tipoCurso;
            request.profesorId = profesorId;
            request.periodoAcademico = periodoAcademico;
            request.duracion = 40;
            request.cupoMaximo = 30;

            // Agregar un módulo de ejemplo con material y evaluación
            ModuloRequest modulo = new ModuloRequest();
            modulo.titulo = "Módulo 1: Introducción";
            modulo.descripcion = "Módulo introductorio del curso";
            
            MaterialRequest material = new MaterialRequest();
            material.nombre = "Material Principal";
            material.descripcion = "Material introductorio del curso";
            modulo.materiales = java.util.List.of(material);

            EvaluacionRequest evaluacion = new EvaluacionRequest();
            evaluacion.nombre = "Evaluación 1";
            evaluacion.descripcion = "Primera evaluación del curso";
            modulo.evaluaciones = java.util.List.of(evaluacion);

            request.modulos = java.util.List.of(modulo);

            // Crear el curso
            Map<String, Object> resultado = cursoCreacionService.crearCursoCompleto(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (Exception e) {
            logger.error("❌ Error en creación rápida: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint de demostración que crea un curso de ejemplo completo
     */
    @PostMapping("/crear-demo")
    public ResponseEntity<Map<String, Object>> crearCursoDemo(
            @RequestParam(defaultValue = "virtual") String tipoCurso) {

        logger.info("🎯 Creando curso de demostración tipo: {}", tipoCurso);

        try {
            CursoCreacionRequest request = new CursoCreacionRequest();
            request.nombre = "Curso Demo - " + tipoCurso;
            request.descripcion = "Curso de demostración creado con Abstract Factory + Builder";
            request.tipoCurso = tipoCurso;
            request.profesorId = 1; // Profesor por defecto
            request.periodoAcademico = "2024-1";
            request.duracion = 60;
            request.cupoMaximo = 40;
            request.estrategiaEvaluacion = "ponderada";

            // Módulo 1
            ModuloRequest modulo1 = new ModuloRequest();
            modulo1.titulo = "Módulo 1: Fundamentos";
            modulo1.descripcion = "Conceptos fundamentales del curso";
            
            MaterialRequest mat1 = new MaterialRequest();
            mat1.nombre = "Material 1.1";
            mat1.descripcion = "Introducción a los conceptos";
            
            MaterialRequest mat2 = new MaterialRequest();
            mat2.nombre = "Material 1.2";
            mat2.descripcion = "Ejercicios prácticos";
            
            modulo1.materiales = java.util.List.of(mat1, mat2);

            EvaluacionRequest eval1 = new EvaluacionRequest();
            eval1.nombre = "Quiz 1";
            eval1.descripcion = "Evaluación de fundamentos";
            
            modulo1.evaluaciones = java.util.List.of(eval1);

            // Módulo 2
            ModuloRequest modulo2 = new ModuloRequest();
            modulo2.titulo = "Módulo 2: Intermedio";
            modulo2.descripcion = "Conceptos intermedios";
            
            MaterialRequest mat3 = new MaterialRequest();
            mat3.nombre = "Material 2.1";
            mat3.descripcion = "Temas avanzados";
            
            modulo2.materiales = java.util.List.of(mat3);

            EvaluacionRequest eval2 = new EvaluacionRequest();
            eval2.nombre = "Examen Parcial";
            eval2.descripcion = "Evaluación intermedia";
            
            modulo2.evaluaciones = java.util.List.of(eval2);

            request.modulos = java.util.List.of(modulo1, modulo2);

            // Crear el curso
            Map<String, Object> resultado = cursoCreacionService.crearCursoCompleto(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (Exception e) {
            logger.error("❌ Error en curso demo: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
