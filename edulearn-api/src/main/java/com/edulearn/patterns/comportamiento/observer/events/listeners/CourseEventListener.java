package com.edulearn.patterns.comportamiento.observer.events.listeners;

import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.comportamiento.observer.events.CourseCreatedEvent;
import com.edulearn.patterns.comportamiento.observer.events.MaterialUploadedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * SPRING EVENTS - Listener para Eventos de Curso
 * ==============================================
 * Event Listener que maneja eventos relacionados con cursos.
 * Actúa como "Observer" en el patrón Observer usando Spring Events.
 *
 * Ventajas:
 * - Procesamiento asíncrono con @Async
 * - Desacoplamiento total del publicador
 * - Manejo de excepciones centralizado
 * - Fácil testing y mockeo
 */
@Component
public class CourseEventListener {

    private static final Logger logger = LoggerFactory.getLogger(CourseEventListener.class);

    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    /**
     * Escucha evento de curso creado y notifica a administradores
     */
    @EventListener
    @Async // Procesamiento asíncrono para no bloquear el hilo principal
    public void handleCourseCreated(CourseCreatedEvent event) {
        logger.info("Procesando evento: {}", event);

        try {
            // Delegar al orquestador de notificaciones
            notificationOrchestrator.notifyCourseCreated(event.getCurso());

            logger.info("Notificaciones de curso creado enviadas exitosamente");
        } catch (Exception e) {
            logger.error("Error al procesar evento de curso creado: {}", e.getMessage(), e);
        }
    }

    /**
     * Escucha evento de material subido y notifica a estudiantes
     */
    @EventListener
    @Async
    public void handleMaterialUploaded(MaterialUploadedEvent event) {
        logger.info("Procesando evento: {}", event);

        try {
            notificationOrchestrator.notifyMaterialUploaded(
                event.getMaterial(),
                event.getCursoId(),
                event.getCursoNombre()
            );

            logger.info("Notificaciones de material subido enviadas exitosamente");
        } catch (Exception e) {
            logger.error("Error al procesar evento de material subido: {}", e.getMessage(), e);
        }
    }
}
