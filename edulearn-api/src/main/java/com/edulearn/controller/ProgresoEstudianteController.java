package com.edulearn.controller;

import com.edulearn.model.HistorialProgresoEstudiante;
import com.edulearn.model.ProgresoEstudiante;
import com.edulearn.patterns.comportamiento.memento.ProgresoMemento;
import com.edulearn.service.ProgresoEstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para gestionar el progreso del estudiante usando el patrón Memento
 */
@RestController
@RequestMapping("/api/progreso")
@CrossOrigin(origins = "*")
public class ProgresoEstudianteController {

    @Autowired
    private ProgresoEstudianteService progresoService;

    /**
     * Obtener el progreso actual de un estudiante en un curso
     * GET /api/progreso/{estudianteId}/{cursoId}
     */
    @GetMapping("/{estudianteId}/{cursoId}")
    public ResponseEntity<?> obtenerProgreso(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        try {
            ProgresoEstudiante progreso = progresoService.obtenerOCrearProgreso(estudianteId, cursoId);
            return ResponseEntity.ok(progreso);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener progreso", e.getMessage()));
        }
    }

    /**
     * Obtener todos los progresos de un estudiante
     * GET /api/progreso/estudiante/{estudianteId}
     */
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<?> obtenerProgresosPorEstudiante(@PathVariable Integer estudianteId) {
        try {
            List<ProgresoEstudiante> progresos = progresoService.obtenerProgresosPorEstudiante(estudianteId);
            return ResponseEntity.ok(progresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener progresos", e.getMessage()));
        }
    }

    /**
     * Actualizar el progreso del estudiante
     * PUT /api/progreso
     */
    @PutMapping
    public ResponseEntity<?> actualizarProgreso(@RequestBody ProgresoEstudiante progreso) {
        try {
            ProgresoEstudiante actualizado = progresoService.actualizarProgreso(progreso);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al actualizar progreso", e.getMessage()));
        }
    }

    /**
     * GUARDAR ESTADO (Patrón Memento) - Crea un punto de restauración
     * POST /api/progreso/guardar/{estudianteId}/{cursoId}
     */
    @PostMapping("/guardar/{estudianteId}/{cursoId}")
    public ResponseEntity<?> guardarEstado(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            String descripcion = (body != null && body.containsKey("descripcion"))
                    ? body.get("descripcion")
                    : "Guardado manual";

            ProgresoMemento memento = progresoService.guardarEstadoProgreso(estudianteId, cursoId, descripcion);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Estado guardado exitosamente");
            respuesta.put("fechaGuardado", memento.getFechaGuardado());
            respuesta.put("porcentajeCompletado", memento.getPorcentajeCompletado());
            respuesta.put("moduloActual", memento.getModuloActualId());
            respuesta.put("descripcion", descripcion);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al guardar estado", e.getMessage()));
        }
    }

    /**
     * RESTAURAR ÚLTIMO ESTADO (Patrón Memento)
     * POST /api/progreso/restaurar/{estudianteId}/{cursoId}
     */
    @PostMapping("/restaurar/{estudianteId}/{cursoId}")
    public ResponseEntity<?> restaurarUltimoEstado(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        try {
            ProgresoEstudiante progresoRestaurado = progresoService.restaurarUltimoEstado(estudianteId, cursoId);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Progreso restaurado exitosamente");
            respuesta.put("progreso", progresoRestaurado);

            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("No se pudo restaurar", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al restaurar estado", e.getMessage()));
        }
    }

    /**
     * RESTAURAR ESTADO ESPECÍFICO (Patrón Memento)
     * POST /api/progreso/restaurar/{estudianteId}/{cursoId}/{historialId}
     */
    @PostMapping("/restaurar/{estudianteId}/{cursoId}/{historialId}")
    public ResponseEntity<?> restaurarEstadoEspecifico(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId,
            @PathVariable Integer historialId) {
        try {
            ProgresoEstudiante progresoRestaurado = progresoService.restaurarEstadoPorId(
                    estudianteId, cursoId, historialId);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Progreso restaurado exitosamente desde el historial");
            respuesta.put("progreso", progresoRestaurado);

            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(crearRespuestaError("No se pudo restaurar", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al restaurar estado", e.getMessage()));
        }
    }

    /**
     * Obtener el historial de estados guardados
     * GET /api/progreso/historial/{estudianteId}/{cursoId}
     */
    @GetMapping("/historial/{estudianteId}/{cursoId}")
    public ResponseEntity<?> obtenerHistorial(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        try {
            List<HistorialProgresoEstudiante> historial = progresoService.obtenerHistorial(estudianteId, cursoId);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al obtener historial", e.getMessage()));
        }
    }

    /**
     * Limpiar el historial de un estudiante en un curso
     * DELETE /api/progreso/historial/{estudianteId}/{cursoId}
     */
    @DeleteMapping("/historial/{estudianteId}/{cursoId}")
    public ResponseEntity<?> limpiarHistorial(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        try {
            progresoService.limpiarHistorial(estudianteId, cursoId);
            return ResponseEntity.ok(crearRespuestaExito("Historial limpiado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearRespuestaError("Error al limpiar historial", e.getMessage()));
        }
    }

    // Métodos auxiliares
    private Map<String, String> crearRespuestaError(String mensaje, String detalle) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", mensaje);
        respuesta.put("detalle", detalle);
        return respuesta;
    }

    private Map<String, String> crearRespuestaExito(String mensaje) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }
}
