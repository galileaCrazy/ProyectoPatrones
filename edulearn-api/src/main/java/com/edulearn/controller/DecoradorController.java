package com.edulearn.controller;

import com.edulearn.patterns.estructural.decorator.dto.DecoradorRequest;
import com.edulearn.patterns.estructural.decorator.dto.DecoradorResponse;
import com.edulearn.patterns.estructural.decorator.service.DecoradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para el patrón Decorator
 * Endpoint: /api/modulos/{id}/decoradores
 *
 * Permite extender funcionalidades de módulos educativos sin modificar su estructura original
 */
@RestController
@RequestMapping("/api/modulos")
@CrossOrigin(origins = "*")
public class DecoradorController {

    @Autowired
    private DecoradorService decoradorService;

    @Autowired
    private com.edulearn.repository.ModuloRepository moduloRepository;

    /**
     * Aplica decoradores (gamificación y/o certificación) a un módulo
     *
     * POST /api/modulos/{id}/decoradores
     *
     * @param id ID del módulo a decorar
     * @param request Datos de los decoradores a aplicar
     * @return Respuesta con el contenido decorado
     */
    @PostMapping("/{id}/decoradores")
    public ResponseEntity<?> aplicarDecoradores(
            @PathVariable Long id,
            @RequestBody DecoradorRequest request) {
        System.out.println("=== DECORATOR CONTROLLER ===");
        System.out.println("ID recibido: " + id);
        System.out.println("Tipo de ID: " + id.getClass().getName());
        System.out.println("Request: " + request);

        try {
            DecoradorResponse response = decoradorService.aplicarDecoradores(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Error de validación (ej: certificación solo en último módulo)
            System.out.println("Error de validación: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error runtime: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Módulo no encontrado con ID: " + id);
        }
    }

    /**
     * Obtiene el contenido decorado de un módulo
     *
     * GET /api/modulos/{id}/decoradores
     *
     * @param id ID del módulo
     * @return Respuesta con el contenido decorado
     */
    @GetMapping("/{id}/decoradores")
    public ResponseEntity<?> obtenerModuloDecorado(@PathVariable Long id) {
        try {
            DecoradorResponse response = decoradorService.obtenerModuloDecorado(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Módulo no encontrado con ID: " + id);
        }
    }

    /**
     * Verifica si un módulo puede tener certificación
     * (Solo el último módulo del curso puede tener certificación)
     *
     * GET /api/modulos/{id}/decoradores/puede-certificar
     *
     * @param id ID del módulo
     * @return true si puede tener certificación, false si no
     */
    @GetMapping("/{id}/decoradores/puede-certificar")
    public ResponseEntity<Boolean> puedeTenerCertificacion(@PathVariable Long id) {
        System.out.println("=== PUEDE CERTIFICAR ENDPOINT ===");
        System.out.println("ID recibido: " + id);

        try {
            boolean puede = decoradorService.esUltimoModuloDelCurso(id);
            System.out.println("Resultado: " + puede);
            return ResponseEntity.ok(puede);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }

    /**
     * Elimina todos los decoradores de un módulo
     *
     * DELETE /api/modulos/{id}/decoradores
     *
     * @param id ID del módulo
     * @return Respuesta sin contenido
     */
    @DeleteMapping("/{id}/decoradores")
    public ResponseEntity<Void> eliminarDecoradores(@PathVariable Long id) {
        try {
            decoradorService.eliminarDecoradores(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Obtiene información de decoradores para un estudiante en un curso
     * Incluye gamificación, certificación y validación según tipo de inscripción
     *
     * GET /api/modulos/curso/{cursoId}/estudiante/{estudianteId}/decoradores
     *
     * @param cursoId ID del curso
     * @param estudianteId ID del estudiante
     * @return Información de decoradores para el estudiante
     */
    @GetMapping("/curso/{cursoId}/estudiante/{estudianteId}/decoradores")
    public ResponseEntity<?> obtenerDecoradoresParaEstudiante(
            @PathVariable Integer cursoId,
            @PathVariable Integer estudianteId) {
        try {
            com.edulearn.patterns.estructural.decorator.dto.DecoradorEstudianteResponse response =
                    decoradorService.obtenerDecoradoresParaEstudiante(cursoId, estudianteId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error al obtener decoradores para estudiante: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener información de decoradores: " + e.getMessage());
        }
    }

    /**
     * ENDPOINT TEMPORAL DE DEBUG - Ver todos los módulos
     */
    @GetMapping("/debug/todos")
    public ResponseEntity<?> listarTodosModulos() {
        try {
            java.util.List<com.edulearn.model.Modulo> modulos = moduloRepository.findAll();
            java.util.List<java.util.Map<String, Object>> resultado = new java.util.ArrayList<>();

            for (com.edulearn.model.Modulo m : modulos) {
                java.util.Map<String, Object> info = new java.util.HashMap<>();
                info.put("id", m.getId());
                info.put("nombre", m.getNombre());
                info.put("cursoId", m.getCursoId());
                info.put("orden", m.getOrden());
                resultado.add(info);
            }

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
