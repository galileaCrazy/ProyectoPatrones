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
 * Demuestra el uso del patrón Factory Method
 */
@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    /**
     * Enviar notificación
     * POST /api/notificaciones
     */
    @PostMapping
    public ResponseEntity<Notificacion> enviar(@RequestBody NotificacionRequest request) {
        Notificacion notif = service.enviarNotificacion(
            request.getTipo(),
            request.getDestinatario(),
            request.getAsunto(),
            request.getMensaje()
        );
        return ResponseEntity.ok(notif);
    }

    /**
     * Enviar notificación múltiple (varios canales)
     * POST /api/notificaciones/multiple
     */
    @PostMapping("/multiple")
    public ResponseEntity<List<Notificacion>> enviarMultiple(@RequestBody NotificacionMultipleRequest request) {
        List<Notificacion> notifs = service.enviarNotificacionMultiple(
            request.getTipos(),
            request.getDestinatario(),
            request.getAsunto(),
            request.getMensaje()
        );
        return ResponseEntity.ok(notifs);
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
     * Reintentar envío de notificación fallida
     * POST /api/notificaciones/{id}/reintentar
     */
    @PostMapping("/{id}/reintentar")
    public ResponseEntity<Notificacion> reintentar(@PathVariable Long id) {
        Notificacion notif = service.reintentarEnvio(id);
        return ResponseEntity.ok(notif);
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
     * Demo del patrón Factory Method
     * GET /api/notificaciones/demo
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        return ResponseEntity.ok(Map.of(
            "patron", "Factory Method",
            "proposito", "Crear diferentes tipos de notificaciones sin conocer las clases concretas",
            "ventajas", List.of(
                "Desacoplamiento entre cliente y clases concretas",
                "Fácil extensión con nuevos tipos",
                "Cumple principio Open/Closed",
                "Centraliza lógica de creación"
            ),
            "tiposDisponibles", List.of("EMAIL", "SMS", "PUSH"),
            "ejemploUso", Map.of(
                "descripcion", "Enviar notificación de bienvenida por email",
                "endpoint", "POST /api/notificaciones",
                "body", Map.of(
                    "tipo", "EMAIL",
                    "destinatario", "estudiante@example.com",
                    "asunto", "Bienvenido a EduLearn",
                    "mensaje", "Gracias por registrarte en nuestra plataforma"
                )
            )
        ));
    }

    // DTOs
    static class NotificacionRequest {
        private String tipo;
        private String destinatario;
        private String asunto;
        private String mensaje;

        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getDestinatario() { return destinatario; }
        public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
        public String getAsunto() { return asunto; }
        public void setAsunto(String asunto) { this.asunto = asunto; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }

    static class NotificacionMultipleRequest {
        private List<String> tipos;
        private String destinatario;
        private String asunto;
        private String mensaje;

        public List<String> getTipos() { return tipos; }
        public void setTipos(List<String> tipos) { this.tipos = tipos; }
        public String getDestinatario() { return destinatario; }
        public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
        public String getAsunto() { return asunto; }
        public void setAsunto(String asunto) { this.asunto = asunto; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}
