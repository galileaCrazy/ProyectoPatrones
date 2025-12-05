package com.edulearn.patterns.comportamiento.observer.events.listeners;

import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.comportamiento.observer.events.AssignmentCreatedEvent;
import com.edulearn.patterns.comportamiento.observer.events.AssignmentGradedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * SPRING EVENTS - Listener para Eventos de Tareas
 * ===============================================
 * Event Listener que maneja eventos relacionados con tareas/evaluaciones.
 */
@Component
public class AssignmentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentEventListener.class);

    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    /**
     * Escucha evento de tarea creada y notifica a estudiantes
     */
    @EventListener
    @Async
    public void handleAssignmentCreated(AssignmentCreatedEvent event) {
        logger.info("Procesando evento: {}", event);

        try {
            notificationOrchestrator.notifyAssignmentCreated(
                event.getTarea(),
                event.getCursoId(),
                event.getCursoNombre()
            );

            logger.info("Notificaciones de tarea creada enviadas exitosamente");
        } catch (Exception e) {
            logger.error("Error al procesar evento de tarea creada: {}", e.getMessage(), e);
        }
    }

    /**
     * Escucha evento de tarea calificada y notifica al estudiante
     */
    @EventListener
    @Async
    public void handleAssignmentGraded(AssignmentGradedEvent event) {
        logger.info("Procesando evento: {}", event);

        try {
            notificationOrchestrator.notifyAssignmentGraded(
                event.getEstudianteId(),
                event.getEstudianteNombre(),
                event.getTareaId(),
                event.getTareaNombre(),
                event.getCalificacion(),
                event.getFeedback()
            );

            logger.info("Notificación de tarea calificada enviada exitosamente");
        } catch (Exception e) {
            logger.error("Error al procesar evento de tarea calificada: {}", e.getMessage(), e);
        }
    }
}
