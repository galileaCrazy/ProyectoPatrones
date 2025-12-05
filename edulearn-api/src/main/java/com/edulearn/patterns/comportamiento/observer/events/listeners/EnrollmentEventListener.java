package com.edulearn.patterns.comportamiento.observer.events.listeners;

import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.comportamiento.observer.events.StudentEnrolledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * SPRING EVENTS - Listener para Eventos de Inscripción
 * ===================================================
 * Event Listener que maneja eventos relacionados con inscripciones.
 */
@Component
public class EnrollmentEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentEventListener.class);

    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    /**
     * Escucha evento de estudiante inscrito y notifica al profesor
     * También suscribe al estudiante a las notificaciones del curso
     */
    @EventListener
    @Async
    public void handleStudentEnrolled(StudentEnrolledEvent event) {
        logger.info("Procesando evento: {}", event);

        try {
            // 1. Notificar al profesor
            notificationOrchestrator.notifyStudentEnrolled(
                event.getInscripcion(),
                event.getEstudianteNombre(),
                event.getCursoNombre()
            );

            // 2. Suscribir al estudiante a las notificaciones del curso
            notificationOrchestrator.subscribeStudentToCourse(
                event.getInscripcion().getEstudianteId(),
                event.getEstudianteNombre(),
                event.getInscripcion().getCursoId()
            );

            logger.info("Notificación de inscripción enviada y estudiante suscrito al curso");
        } catch (Exception e) {
            logger.error("Error al procesar evento de inscripción: {}", e.getMessage(), e);
        }
    }
}
