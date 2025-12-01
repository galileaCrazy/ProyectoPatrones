package com.edulearn.patterns.comportamiento.template_method;

import com.edulearn.model.Curso;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoInscripcion;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller para el proceso de inscripción usando Template Method Pattern
 * 
 * Endpoints:
 * - POST /api/inscripciones/proceso - Procesar inscripción
 * - GET /api/inscripciones/proceso/tipos - Obtener tipos de inscripción
 * - GET /api/inscripciones/proceso/pasos/{tipo} - Obtener pasos por tipo
 * - GET /api/inscripciones/proceso/demo - Demo del patrón
 * - GET /api/inscripciones/proceso/cursos-disponibles - Cursos para inscripción
 * - GET /api/inscripciones/proceso/verificar/{estudianteId}/{cursoId} - Verificar elegibilidad
 */
@RestController
@RequestMapping("/api/inscripciones/proceso")
@CrossOrigin(origins = "*")
public class InscripcionTemplateController {
    
    private final InscripcionTemplateService inscripcionService;
    
    @Autowired
    public InscripcionTemplateController(InscripcionTemplateService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }
    
    /**
     * Procesa una solicitud de inscripción
     * 
     * Ejemplo de solicitud para inscripción gratuita:
     * {
     *   "estudianteId": 4,
     *   "cursoId": 4,
     *   "tipoInscripcion": "GRATUITA",
     *   "aceptaTerminos": true
     * }
     * 
     * Ejemplo para inscripción paga:
     * {
     *   "estudianteId": 4,
     *   "cursoId": 4,
     *   "tipoInscripcion": "PAGA",
     *   "aceptaTerminos": true,
     *   "metodoPago": "TARJETA",
     *   "numeroTarjeta": "4111111111111111",
     *   "monto": 500.00,
     *   "codigoDescuento": "PROMO10"
     * }
     * 
     * Ejemplo para inscripción con beca:
     * {
     *   "estudianteId": 4,
     *   "cursoId": 4,
     *   "tipoInscripcion": "BECA",
     *   "aceptaTerminos": true,
     *   "tipoBeca": "ACADEMICA",
     *   "codigoBeca": "BECA-2024-001",
     *   "porcentajeBeca": 100
     * }
     */
    @PostMapping
    public ResponseEntity<ResultadoInscripcion> procesarInscripcion(
            @RequestBody SolicitudInscripcion solicitud) {
        
        ResultadoInscripcion resultado = inscripcionService.procesarInscripcion(solicitud);
        
        if (resultado.isExitoso()) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.badRequest().body(resultado);
        }
    }
    
    /**
     * Obtiene los tipos de inscripción disponibles con sus descripciones
     */
    @GetMapping("/tipos")
    public ResponseEntity<List<Map<String, Object>>> getTiposInscripcion() {
        return ResponseEntity.ok(inscripcionService.getTiposInscripcion());
    }
    
    /**
     * Obtiene los pasos específicos para un tipo de inscripción
     */
    @GetMapping("/pasos/{tipo}")
    public ResponseEntity<?> getPasosPorTipo(@PathVariable String tipo) {
        List<String> pasos = inscripcionService.getPasosParaTipo(tipo);
        
        if (pasos.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Tipo de inscripción no válido",
                "tipo", tipo
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "tipo", tipo.toUpperCase(),
            "pasos", pasos,
            "totalPasos", pasos.size()
        ));
    }
    
    /**
     * Demo del patrón Template Method
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> getDemo() {
        return ResponseEntity.ok(inscripcionService.getDemoInfo());
    }
    
    /**
     * Obtiene los cursos disponibles para inscripción
     */
    @GetMapping("/cursos-disponibles")
    public ResponseEntity<List<Curso>> getCursosDisponibles() {
        return ResponseEntity.ok(inscripcionService.getCursosDisponibles());
    }
    
    /**
     * Verifica si un estudiante puede inscribirse a un curso
     */
    @GetMapping("/verificar/{estudianteId}/{cursoId}")
    public ResponseEntity<Map<String, Object>> verificarElegibilidad(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        return ResponseEntity.ok(inscripcionService.verificarElegibilidad(estudianteId, cursoId));
    }
    
    /**
     * Endpoint de salud/prueba
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "OK",
            "patron", "Template Method",
            "servicio", "Proceso de Inscripción"
        ));
    }
}
