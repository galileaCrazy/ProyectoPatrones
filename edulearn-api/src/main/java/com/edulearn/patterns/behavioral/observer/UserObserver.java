package com.edulearn.patterns.behavioral.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * PATRÓN OBSERVER - Observer Concreto
 * ===================================
 * Implementación concreta de Observer que representa a un usuario
 * que recibe notificaciones del sistema.
 *
 * Cada usuario puede suscribirse a diferentes tipos de eventos según
 * su rol y preferencias.
 */
public class UserObserver implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(UserObserver.class);

    private final Integer userId;
    private final String userName;
    private final String userRole;
    private final Set<String> interestedEvents;

    public UserObserver(Integer userId, String userName, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.interestedEvents = new HashSet<>();
        initializeDefaultInterests();
    }

    /**
     * Inicializar intereses por defecto según el rol del usuario
     */
    private void initializeDefaultInterests() {
        if ("profesor".equalsIgnoreCase(userRole)) {
            // Profesores se interesan en eventos de sus cursos
            interestedEvents.add(NotificationEvent.EventType.TAREA_ENTREGADA);
            interestedEvents.add(NotificationEvent.EventType.ESTUDIANTE_INSCRITO);
            interestedEvents.add(NotificationEvent.EventType.CURSO_ACTUALIZADO);
        } else if ("estudiante".equalsIgnoreCase(userRole)) {
            // Estudiantes se interesan en eventos de los cursos donde están inscritos
            interestedEvents.add(NotificationEvent.EventType.TAREA_CREADA);
            interestedEvents.add(NotificationEvent.EventType.TAREA_ACTUALIZADA);
            interestedEvents.add(NotificationEvent.EventType.TAREA_CALIFICADA);
            interestedEvents.add(NotificationEvent.EventType.MATERIAL_AGREGADO);
            interestedEvents.add(NotificationEvent.EventType.CURSO_ACTUALIZADO);
        } else if ("admin".equalsIgnoreCase(userRole)) {
            // Administradores se interesan en todos los eventos
            interestedEvents.add(NotificationEvent.EventType.CURSO_CREADO);
            interestedEvents.add(NotificationEvent.EventType.CURSO_ACTUALIZADO);
            interestedEvents.add(NotificationEvent.EventType.CURSO_ELIMINADO);
            interestedEvents.add(NotificationEvent.EventType.TAREA_CREADA);
            interestedEvents.add(NotificationEvent.EventType.ESTUDIANTE_INSCRITO);
        }
    }

    @Override
    public void update(NotificationEvent event) {
        logger.info("Usuario {} ({}) recibió notificación: {} - {}",
            userName, userRole, event.getTitle(), event.getMessage());

        // Aquí se podría agregar lógica adicional como:
        // - Enviar email
        // - Enviar push notification
        // - Actualizar contador de notificaciones no leídas
        // - Reproducir sonido
    }

    @Override
    public Integer getObserverId() {
        return userId;
    }

    @Override
    public boolean isInterestedIn(String eventType) {
        return interestedEvents.contains(eventType);
    }

    /**
     * Suscribirse a un tipo de evento adicional
     */
    public void subscribeToEvent(String eventType) {
        interestedEvents.add(eventType);
        logger.info("Usuario {} suscrito a evento: {}", userName, eventType);
    }

    /**
     * Desuscribirse de un tipo de evento
     */
    public void unsubscribeFromEvent(String eventType) {
        interestedEvents.remove(eventType);
        logger.info("Usuario {} desuscrito de evento: {}", userName, eventType);
    }

    /**
     * Obtener todos los eventos a los que está suscrito
     */
    public Set<String> getSubscribedEvents() {
        return new HashSet<>(interestedEvents);
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserObserver that = (UserObserver) obj;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
