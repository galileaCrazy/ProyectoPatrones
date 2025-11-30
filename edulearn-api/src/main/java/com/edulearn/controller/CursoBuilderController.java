package com.edulearn.controller;

import com.edulearn.model.CursoBuilder;
import com.edulearn.service.CursoBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para cursos con patrón Builder
 */
@RestController
@RequestMapping("/api/cursos-builder")
@CrossOrigin(origins = "*")
public class CursoBuilderController {

    @Autowired
    private CursoBuilderService service;

    /**
     * POST /api/cursos-builder/personalizado
     * Crear curso con builder personalizado
     */
    @PostMapping("/personalizado")
    public ResponseEntity<CursoBuilder> crearPersonalizado(@RequestBody Map<String, Object> parametros) {
        CursoBuilder curso = service.crearCursoPersonalizado(parametros);
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos-builder/basico
     * Crear curso básico usando Director
     */
    @PostMapping("/basico")
    public ResponseEntity<CursoBuilder> crearBasico(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        CursoBuilder curso = service.crearCursoBasico(nombre);
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos-builder/premium
     * Crear curso premium usando Director
     */
    @PostMapping("/premium")
    public ResponseEntity<CursoBuilder> crearPremium(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String categoria = request.get("categoria");
        CursoBuilder curso = service.crearCursoPremium(nombre, categoria);
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos-builder/virtual
     * Crear curso virtual usando Director
     */
    @PostMapping("/virtual")
    public ResponseEntity<CursoBuilder> crearVirtual(@RequestBody Map<String, Object> request) {
        String nombre = (String) request.get("nombre");
        Integer duracion = (Integer) request.get("duracion");
        CursoBuilder curso = service.crearCursoVirtual(nombre, duracion);
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos-builder/intensivo
     * Crear curso intensivo usando Director
     */
    @PostMapping("/intensivo")
    public ResponseEntity<CursoBuilder> crearIntensivo(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        LocalDate fechaInicio = LocalDate.parse(request.get("fechaInicio"));
        CursoBuilder curso = service.crearCursoIntensivo(nombre, fechaInicio);
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos-builder/gratuito
     * Crear curso gratuito usando Director
     */
    @PostMapping("/gratuito")
    public ResponseEntity<CursoBuilder> crearGratuito(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String categoria = request.get("categoria");
        CursoBuilder curso = service.crearCursoGratuito(nombre, categoria);
        return ResponseEntity.ok(curso);
    }

    /**
     * POST /api/cursos-builder/corporativo
     * Crear curso corporativo usando Director
     */
    @PostMapping("/corporativo")
    public ResponseEntity<CursoBuilder> crearCorporativo(@RequestBody Map<String, Object> request) {
        String nombre = (String) request.get("nombre");
        Integer duracion = (Integer) request.get("duracion");
        Integer cupo = (Integer) request.get("cupo");
        LocalDate fechaInicio = LocalDate.parse((String) request.get("fechaInicio"));
        CursoBuilder curso = service.crearCursoCorporativo(nombre, duracion, cupo, fechaInicio);
        return ResponseEntity.ok(curso);
    }

    /**
     * GET /api/cursos-builder
     * Obtener todos los cursos
     */
    @GetMapping
    public ResponseEntity<List<CursoBuilder>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    /**
     * GET /api/cursos-builder/modalidad/{modalidad}
     * Obtener cursos por modalidad
     */
    @GetMapping("/modalidad/{modalidad}")
    public ResponseEntity<List<CursoBuilder>> obtenerPorModalidad(@PathVariable String modalidad) {
        return ResponseEntity.ok(service.obtenerPorModalidad(modalidad));
    }

    /**
     * GET /api/cursos-builder/nivel/{nivel}
     * Obtener cursos por nivel
     */
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<CursoBuilder>> obtenerPorNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(service.obtenerPorNivel(nivel));
    }

    /**
     * GET /api/cursos-builder/tipo/{tipo}
     * Obtener cursos por tipo de construcción
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<CursoBuilder>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.obtenerPorTipoConstruccion(tipo));
    }

    /**
     * GET /api/cursos-builder/estadisticas
     * Obtener estadísticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(service.obtenerEstadisticas());
    }

    /**
     * GET /api/cursos-builder/demo
     * Demostración del patrón Builder
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        return ResponseEntity.ok(Map.of(
            "patron", "Builder",
            "proposito", "Separar la construcción de un objeto complejo de su representación",
            "ventajas", List.of(
                "Permite construir objetos paso a paso",
                "El mismo proceso puede crear diferentes representaciones",
                "Aísla el código de construcción de la representación",
                "Proporciona mejor control sobre el proceso de construcción"
            ),
            "tiposDisponibles", List.of(
                "BASICO - Curso básico con configuración mínima",
                "PREMIUM - Curso premium con todas las características",
                "VIRTUAL - Curso virtual estándar",
                "INTENSIVO - Curso intensivo presencial de 2 semanas",
                "GRATUITO - Curso introductorio gratuito",
                "CORPORATIVO - Curso personalizado para empresas",
                "CUSTOM - Curso personalizado con builder flexible"
            ),
            "ejemploUso", Map.of(
                "descripcion", "Crear curso paso a paso con Builder fluent API",
                "codigo", "new CursoBuilder().setNombre(...).setModalidad(...).build()"
            )
        ));
    }
}
