package com.edulearn.controller;

import com.edulearn.model.Notificacion;
import com.edulearn.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar notificaciones
 * Utiliza el patrón Observer para notificaciones internas del sistema
 */
@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    /**
     * PATRÓN OBSERVER: Registrar usuario como observador
     * POST /api/notificaciones/observer/register
     */
    @PostMapping("/observer/register")
    public ResponseEntity<Map<String, Object>> registerObserver(@RequestBody Map<String, Object> request) {
        Integer userId = (Integer) request.get("userId");
        String userName = (String) request.get("userName");
        String userRole = (String) request.get("userRole");

        service.registerUserObserver(userId, userName, userRole);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Usuario registrado como observador",
            "userId", userId
        ));
    }

    /**
     * PATRÓN OBSERVER: Obtener notificaciones no leídas de un usuario
     * GET /api/notificaciones/usuario/{userId}/no-leidas
     */
    @GetMapping("/usuario/{userId}/no-leidas")
    public ResponseEntity<List<Notificacion>> getUnreadNotifications(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getUnreadNotifications(userId));
    }

    /**
     * PATRÓN OBSERVER: Obtener contador de notificaciones no leídas
     * GET /api/notificaciones/usuario/{userId}/count
     */
    @GetMapping("/usuario/{userId}/count")
    public ResponseEntity<Map<String, Object>> getUnreadCount(@PathVariable Integer userId) {
        long count = service.getUnreadCount(userId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    /**
     * PATRÓN OBSERVER: Marcar notificación como leída
     * PUT /api/notificaciones/{id}/leer
     */
    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsRead(id));
    }

    /**
     * PATRÓN OBSERVER: Marcar todas las notificaciones como leídas
     * PUT /api/notificaciones/usuario/{userId}/leer-todas
     */
    @PutMapping("/usuario/{userId}/leer-todas")
    public ResponseEntity<Map<String, Object>> markAllAsRead(@PathVariable Integer userId) {
        service.markAllAsRead(userId);
        return ResponseEntity.ok(Map.of("success", true, "message", "Todas las notificaciones marcadas como leídas"));
    }

    /**
     * Obtener todas las notificaciones
     * GET /api/notificaciones
     */
    @GetMapping
    public ResponseEntity<List<Notificacion>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    /**
     * Obtener notificaciones por tipo
     * GET /api/notificaciones/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Notificacion>> obtenerPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.obtenerPorTipo(tipo));
    }

    /**
     * Obtener notificaciones por estado
     * GET /api/notificaciones/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Notificacion>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.obtenerPorEstado(estado));
    }

    /**
     * Obtener notificaciones por destinatario
     * GET /api/notificaciones/destinatario/{destinatario}
     */
    @GetMapping("/destinatario/{destinatario}")
    public ResponseEntity<List<Notificacion>> obtenerPorDestinatario(@PathVariable String destinatario) {
        return ResponseEntity.ok(service.obtenerPorDestinatario(destinatario));
    }


    /**
     * Obtener estadísticas
     * GET /api/notificaciones/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(service.obtenerEstadisticas());
    }

    /**
     * Demo del patrón Observer
     * GET /api/notificaciones/demo
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        return ResponseEntity.ok(Map.of(
            "patron", "Observer",
            "proposito", "Notificar automáticamente a usuarios interesados sobre eventos del sistema",
            "ventajas", List.of(
                "Desacoplamiento entre el Subject y los Observers",
                "Los usuarios reciben notificaciones automáticas",
                "Fácil agregar nuevos tipos de observadores",
                "Notificaciones en tiempo real"
            ),
            "eventosDisponibles", List.of(
                "CURSO_CREADO", "CURSO_ACTUALIZADO",
                "TAREA_CREADA", "TAREA_ACTUALIZADA", "TAREA_CALIFICADA",
                "ESTUDIANTE_INSCRITO", "MATERIAL_AGREGADO"
            ),
            "ejemploUso", Map.of(
                "descripcion", "Cuando un profesor crea una tarea, todos los estudiantes inscritos reciben notificación automáticamente",
                "endpoint", "POST /api/notificaciones/observer/register",
                "body", Map.of(
                    "userId", 1,
                    "userName", "Juan Pérez",
                    "userRole", "estudiante"
                )
            )
        ));
    }

}
