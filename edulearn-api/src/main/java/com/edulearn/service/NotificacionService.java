package com.edulearn.service;

import com.edulearn.model.Notificacion;
import com.edulearn.patterns.comportamiento.observer.NotificationEvent;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar notificaciones
 * Utiliza el patrón Observer para notificaciones internas del sistema
 */
@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    /**
     * PATRÓN OBSERVER - Registrar usuario como observador (unificado)
     * Delegado al NotificationOrchestrator para mantener una única fuente de verdad
     */
    public void registerUserObserver(Integer userId, String userName, String userRole) {
        notificationOrchestrator.registerUser(userId, userName, userRole);
    }

    /**
     * PATRÓN OBSERVER - Remover usuario como observador (unificado)
     */
    public void unregisterUserObserver(Integer userId, String userName, String userRole) {
        notificationOrchestrator.unregisterUser(userId, userName, userRole);
    }

    /**
     * PATRÓN OBSERVER - Notificar a un usuario específico (unificado)
     */
    public void notifySpecificUser(Integer userId, NotificationEvent event) {
        notificationOrchestrator.notifySpecificUser(userId, event);
    }

    /**
     * Obtener notificaciones no leídas de un usuario
     */
    public List<Notificacion> getUnreadNotifications(Integer userId) {
        return repository.findByDestinatarioAndEstado(userId.toString(), "NO_LEIDA");
    }

    /**
     * Marcar notificación como leída
     */
    public Notificacion markAsRead(Long notificationId) {
        Notificacion notif = repository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notif.setEstado("LEIDA");
        notif.setFechaEnvio(LocalDateTime.now());
        return repository.save(notif);
    }

    /**
     * Marcar todas las notificaciones de un usuario como leídas
     */
    public void markAllAsRead(Integer userId) {
        List<Notificacion> unread = getUnreadNotifications(userId);
        unread.forEach(notif -> {
            notif.setEstado("LEIDA");
            notif.setFechaEnvio(LocalDateTime.now());
        });
        repository.saveAll(unread);
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
     * Obtener número de notificaciones no leídas
     */
    public long getUnreadCount(Integer userId) {
        return getUnreadNotifications(userId).size();
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
