package com.edulearn.controller;

import com.edulearn.model.ContenidoEducativo;
import com.edulearn.service.ContenidoEducativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST para Contenidos Educativos
 * Demuestra el patrón Abstract Factory
 */
@RestController
@RequestMapping("/api/contenidos")
@CrossOrigin(origins = "*")
public class ContenidoEducativoController {

    @Autowired
    private ContenidoEducativoService service;

    /**
     * POST /api/contenidos/crear
     * Crear un contenido educativo específico
     */
    @PostMapping("/crear")
    public ResponseEntity<ContenidoEducativo> crear(@RequestBody CrearContenidoRequest request) {
        ContenidoEducativo contenido = service.crearContenido(
            request.getNivel(),
            request.getTipo(),
            request.getCursoId()
        );
        return ResponseEntity.ok(contenido);
    }

    /**
     * POST /api/contenidos/crear-familia
     * Crear familia completa (Video + Documento + Quiz) para un nivel
     */
    @PostMapping("/crear-familia")
    public ResponseEntity<List<ContenidoEducativo>> crearFamilia(@RequestBody CrearFamiliaRequest request) {
        List<ContenidoEducativo> contenidos = service.crearFamiliaCompleta(
            request.getNivel(),
            request.getCursoId()
        );
        return ResponseEntity.ok(contenidos);
    }

    /**
     * GET /api/contenidos
     * Obtener todos los contenidos
     */
    @GetMapping
    public ResponseEntity<List<ContenidoEducativo>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    /**
     * GET /api/contenidos/tipo/{tipo}
     * Obtener contenidos por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ContenidoEducativo>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.obtenerPorTipo(tipo));
    }

    /**
     * GET /api/contenidos/nivel/{nivel}
     * Obtener contenidos por nivel
     */
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<ContenidoEducativo>> obtenerPorNivel(@PathVariable String nivel) {
        return ResponseEntity.ok(service.obtenerPorNivel(nivel));
    }

    /**
     * GET /api/contenidos/curso/{cursoId}
     * Obtener contenidos por curso
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ContenidoEducativo>> obtenerPorCurso(@PathVariable Long cursoId) {
        return ResponseEntity.ok(service.obtenerPorCurso(cursoId));
    }

    /**
     * GET /api/contenidos/estadisticas
     * Obtener estadísticas de contenidos
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(service.obtenerEstadisticas());
    }

    /**
     * GET /api/contenidos/demo
     * Demostración del patrón Abstract Factory
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        return ResponseEntity.ok(Map.of(
            "patron", "Abstract Factory",
            "proposito", "Proveer interfaz para crear familias de objetos relacionados sin especificar clases concretas",
            "familias", List.of("BASICO", "INTERMEDIO", "AVANZADO"),
            "productos", List.of("VIDEO", "DOCUMENTO", "QUIZ"),
            "ejemplo", Map.of(
                "nivel", "INTERMEDIO",
                "contenidos", List.of(
                    "VideoIntermedio: 25 min con ejercicios",
                    "DocumentoIntermedio: 12 páginas con diagramas UML",
                    "QuizIntermedio: 15 preguntas mixtas, 3 reintentos"
                )
            ),
            "ventajas", List.of(
                "Asegura compatibilidad entre productos de una familia",
                "Aísla clases concretas del código cliente",
                "Facilita intercambio de familias de productos",
                "Promueve consistencia entre productos relacionados"
            )
        ));
    }
}

/**
 * DTO para crear contenido individual
 */
class CrearContenidoRequest {
    private String nivel; // BASICO, INTERMEDIO, AVANZADO
    private String tipo; // VIDEO, DOCUMENTO, QUIZ
    private Long cursoId;

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
}

/**
 * DTO para crear familia completa
 */
class CrearFamiliaRequest {
    private String nivel; // BASICO, INTERMEDIO, AVANZADO
    private Long cursoId;

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
}
