package com.edulearn.service;

import com.edulearn.model.Notificacion;
import com.edulearn.patterns.creational.factory_method.NotificacionFactory;
import com.edulearn.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar notificaciones
 * Utiliza el patrón Factory Method (NotificacionFactory)
 */
@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    /**
     * Enviar notificación usando Factory Method
     */
    public Notificacion enviarNotificacion(String tipo, String destinatario, String asunto, String mensaje) {
        // Crear notificación en BD
        Notificacion notif = new Notificacion(tipo, destinatario, asunto, mensaje);
        notif = repository.save(notif);

        try {
            // Usar Factory Method para crear y enviar notificación
            NotificacionFactory factory = NotificacionFactory.getFactory(tipo);
            boolean enviado = factory.enviarNotificacion(destinatario, asunto, mensaje);

            // Actualizar estado
            if (enviado) {
                notif.setEstado("ENVIADA");
                notif.setFechaEnvio(LocalDateTime.now());
            } else {
                notif.setEstado("FALLIDA");
                notif.setError("Validación de destinatario fallida");
            }
            notif.setIntentos(notif.getIntentos() + 1);

        } catch (Exception e) {
            notif.setEstado("FALLIDA");
            notif.setError(e.getMessage());
            notif.setIntentos(notif.getIntentos() + 1);
        }

        return repository.save(notif);
    }

    /**
     * Enviar notificación múltiple (varios tipos a la vez)
     */
    public List<Notificacion> enviarNotificacionMultiple(
        List<String> tipos,
        String destinatario,
        String asunto,
        String mensaje
    ) {
        return tipos.stream()
            .map(tipo -> enviarNotificacion(tipo, destinatario, asunto, mensaje))
            .toList();
    }

    /**
     * Obtener todas las notificaciones
     */
    public List<Notificacion> obtenerTodas() {
        return repository.findAll();
    }

    /**
     * Obtener notificaciones por tipo
     */
    public List<Notificacion> obtenerPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    /**
     * Obtener notificaciones por estado
     */
    public List<Notificacion> obtenerPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    /**
     * Obtener notificaciones por destinatario
     */
    public List<Notificacion> obtenerPorDestinatario(String destinatario) {
        return repository.findByDestinatario(destinatario);
    }

    /**
     * Reintentar envío de notificación fallida
     */
    public Notificacion reintentarEnvio(Long id) {
        Notificacion notif = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        if (!"FALLIDA".equals(notif.getEstado())) {
            throw new RuntimeException("Solo se pueden reintentar notificaciones fallidas");
        }

        return enviarNotificacion(
            notif.getTipo(),
            notif.getDestinatario(),
            notif.getAsunto(),
            notif.getMensaje()
        );
    }

    /**
     * Obtener estadísticas de notificaciones
     */
    public Map<String, Object> obtenerEstadisticas() {
        long total = repository.count();
        long enviadas = repository.findByEstado("ENVIADA").size();
        long fallidas = repository.findByEstado("FALLIDA").size();
        long pendientes = repository.findByEstado("PENDIENTE").size();

        return Map.of(
            "total", total,
            "enviadas", enviadas,
            "fallidas", fallidas,
            "pendientes", pendientes,
            "tasaExito", total > 0 ? (enviadas * 100.0 / total) : 0,
            "porTipo", Map.of(
                "EMAIL", repository.findByTipo("EMAIL").size(),
                "SMS", repository.findByTipo("SMS").size(),
                "PUSH", repository.findByTipo("PUSH").size()
            )
        );
    }
}
