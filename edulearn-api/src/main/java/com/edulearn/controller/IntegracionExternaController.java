package com.edulearn.controller;

import com.edulearn.model.IntegracionCurso;
import com.edulearn.service.IntegracionExternaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar integraciones externas
 * Implementa el patrón Adapter para sistemas de videoconferencia y repositorios
 */
@RestController
@RequestMapping("/api/integraciones-externas")
@CrossOrigin(origins = "*")
public class IntegracionExternaController {

    private static final Logger logger = LoggerFactory.getLogger(IntegracionExternaController.class);

    @Autowired
    private IntegracionExternaService integracionService;

    /**
     * GET /api/integraciones/proveedores
     * Obtener proveedores disponibles
     */
    @GetMapping("/proveedores")
    public ResponseEntity<?> obtenerProveedoresDisponibles() {
        try {
            Map<String, Object> proveedores = integracionService.obtenerProveedoresDisponibles();
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            logger.error("Error al obtener proveedores: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener proveedores"));
        }
    }

    /**
     * POST /api/integraciones/crear
     * Crear una nueva integración
     *
     * Body: {
     *   "cursoId": 1,
     *   "profesorId": 2,
     *   "proveedor": "GOOGLE_MEET",
     *   "datos": {
     *     "nombre": "Clase de Matemáticas",
     *     "titulo": "Clase Semanal",
     *     "descripcion": "Reunión semanal del curso",
     *     "fechaInicio": "2025-01-15T10:00:00",
     *     "duracionMinutos": 90
     *   }
     * }
     */
    @PostMapping("/crear")
    public ResponseEntity<?> crearIntegracion(@RequestBody Map<String, Object> request) {
        try {
            Integer cursoId = (Integer) request.get("cursoId");
            Integer profesorId = (Integer) request.get("profesorId");
            String proveedor = (String) request.get("proveedor");
            @SuppressWarnings("unchecked")
            Map<String, Object> datos = (Map<String, Object>) request.get("datos");

            if (cursoId == null || profesorId == null || proveedor == null || datos == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Faltan parámetros requeridos"));
            }

            IntegracionCurso integracion = integracionService.crearIntegracion(
                cursoId, profesorId, proveedor, datos
            );

            logger.info("✅ Integración creada exitosamente: {}", integracion.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensaje", "Integración creada exitosamente",
                "integracion", integracion
            ));
        } catch (IllegalArgumentException e) {
            logger.error("Proveedor no soportado: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Proveedor no soportado", "mensaje", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error al crear integración: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al crear integración", "mensaje", e.getMessage()));
        }
    }

    /**
     * GET /api/integraciones/curso/{cursoId}
     * Obtener todas las integraciones de un curso
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<?> obtenerIntegracionesPorCurso(@PathVariable Integer cursoId) {
        try {
            List<IntegracionCurso> integraciones = integracionService.obtenerIntegracionesPorCurso(cursoId);
            return ResponseEntity.ok(integraciones);
        } catch (Exception e) {
            logger.error("Error al obtener integraciones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener integraciones"));
        }
    }

    /**
     * GET /api/integraciones/curso/{cursoId}/activas
     * Obtener integraciones activas de un curso
     */
    @GetMapping("/curso/{cursoId}/activas")
    public ResponseEntity<?> obtenerIntegracionesActivas(@PathVariable Integer cursoId) {
        try {
            List<IntegracionCurso> integraciones = integracionService.obtenerIntegracionesActivasPorCurso(cursoId);
            return ResponseEntity.ok(integraciones);
        } catch (Exception e) {
            logger.error("Error al obtener integraciones activas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener integraciones activas"));
        }
    }

    /**
     * GET /api/integraciones/curso/{cursoId}/tipo/{tipo}
     * Obtener integraciones por tipo (VIDEOCONFERENCIA, REPOSITORIO)
     */
    @GetMapping("/curso/{cursoId}/tipo/{tipo}")
    public ResponseEntity<?> obtenerIntegracionesPorTipo(
        @PathVariable Integer cursoId,
        @PathVariable String tipo
    ) {
        try {
            List<IntegracionCurso> integraciones = integracionService.obtenerIntegracionesPorTipo(cursoId, tipo);
            return ResponseEntity.ok(integraciones);
        } catch (Exception e) {
            logger.error("Error al obtener integraciones por tipo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener integraciones"));
        }
    }

    /**
     * GET /api/integraciones/{id}
     * Obtener detalle de una integración
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetalleIntegracion(@PathVariable Long id) {
        try {
            Map<String, Object> detalle = integracionService.obtenerDetalleIntegracion(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            logger.error("Integración no encontrada: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Integración no encontrada"));
        } catch (Exception e) {
            logger.error("Error al obtener detalle: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener detalle"));
        }
    }

    /**
     * PUT /api/integraciones/{id}
     * Actualizar una integración
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarIntegracion(
        @PathVariable Long id,
        @RequestBody Map<String, Object> datos
    ) {
        try {
            IntegracionCurso actualizada = integracionService.actualizarIntegracion(id, datos);
            logger.info("✅ Integración actualizada: {}", id);

            return ResponseEntity.ok(Map.of(
                "mensaje", "Integración actualizada exitosamente",
                "integracion", actualizada
            ));
        } catch (RuntimeException e) {
            logger.error("Integración no encontrada: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Integración no encontrada"));
        } catch (Exception e) {
            logger.error("Error al actualizar integración: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar integración"));
        }
    }

    /**
     * DELETE /api/integraciones/{id}
     * Eliminar una integración
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarIntegracion(@PathVariable Long id) {
        try {
            boolean eliminada = integracionService.eliminarIntegracion(id);

            if (eliminada) {
                logger.info("✅ Integración eliminada: {}", id);
                return ResponseEntity.ok(Map.of("mensaje", "Integración eliminada exitosamente"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No se pudo eliminar la integración"));
            }
        } catch (Exception e) {
            logger.error("Error al eliminar integración: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar integración"));
        }
    }

    /**
     * POST /api/integraciones/{id}/usar
     * Registrar uso de una integración
     */
    @PostMapping("/{id}/usar")
    public ResponseEntity<?> registrarUso(@PathVariable Long id) {
        try {
            integracionService.registrarUso(id);
            logger.info("📊 Uso registrado: {}", id);

            return ResponseEntity.ok(Map.of("mensaje", "Uso registrado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al registrar uso: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al registrar uso"));
        }
    }

    /**
     * GET /api/integraciones/curso/{cursoId}/estadisticas
     * Obtener estadísticas de integraciones de un curso
     */
    @GetMapping("/curso/{cursoId}/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable Integer cursoId) {
        try {
            Map<String, Object> stats = integracionService.obtenerEstadisticas(cursoId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener estadísticas"));
        }
    }

    /**
     * POST /api/integraciones/validar-credenciales
     * Validar credenciales de un proveedor
     *
     * Body: {
     *   "proveedor": "GOOGLE_MEET",
     *   "credenciales": {
     *     "accessToken": "ya29.xxx..."
     *   }
     * }
     */
    @PostMapping("/validar-credenciales")
    public ResponseEntity<?> validarCredenciales(@RequestBody Map<String, Object> request) {
        try {
            String proveedor = (String) request.get("proveedor");
            @SuppressWarnings("unchecked")
            Map<String, String> credenciales = (Map<String, String>) request.get("credenciales");

            if (proveedor == null || credenciales == null) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Faltan parámetros requeridos"));
            }

            boolean validas = integracionService.validarCredenciales(proveedor, credenciales);

            return ResponseEntity.ok(Map.of(
                "validas", validas,
                "mensaje", validas ? "Credenciales válidas" : "Credenciales inválidas"
            ));
        } catch (Exception e) {
            logger.error("Error al validar credenciales: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al validar credenciales"));
        }
    }
}
