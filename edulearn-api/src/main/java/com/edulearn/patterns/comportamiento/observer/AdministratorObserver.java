package com.edulearn.patterns.comportamiento.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN OBSERVER - Observer para Administradores
 * ===============================================
 * Observer concreto que representa a un usuario con rol Administrador.
 *
 * Eventos de interés:
 * - Creación de cursos (CURSO_CREADO)
 * - Actualización de cursos (CURSO_ACTUALIZADO)
 * - Eliminación de cursos (CURSO_ELIMINADO)
 * - Eventos importantes del sistema
 *
 * Los administradores reciben notificaciones sobre cambios
 * estructurales en la plataforma que requieren supervisión.
 */
public class AdministratorObserver extends AbstractUserObserver {

    private static final Logger logger = LoggerFactory.getLogger(AdministratorObserver.class);

    public AdministratorObserver(Integer userId, String userName) {
        super(userId, userName, "admin");
        initializeInterests();
    }

    /**
     * Configurar eventos de interés para administradores
     */
    private void initializeInterests() {
        // Eventos de cursos
        interestedEvents.add(NotificationEvent.EventType.CURSO_CREADO);
        interestedEvents.add(NotificationEvent.EventType.CURSO_ACTUALIZADO);
        interestedEvents.add(NotificationEvent.EventType.CURSO_ELIMINADO);

        // Eventos administrativos - SOLO solicitudes de beca
        interestedEvents.add(NotificationEvent.EventType.BECA_SOLICITADA);

        logger.info("AdministratorObserver {} inicializado con {} eventos de interés",
            userId, interestedEvents.size());
    }

    @Override
    protected void processNotification(NotificationEvent event) {
        // Lógica específica para administradores
        switch (event.getEventType()) {
            case NotificationEvent.EventType.CURSO_CREADO:
                handleCourseCreated(event);
                break;
            case NotificationEvent.EventType.CURSO_ELIMINADO:
                handleCourseDeleted(event);
                break;
            default:
                // Procesamiento estándar
                break;
        }
    }

    /**
     * Manejar evento de creación de curso
     */
    private void handleCourseCreated(NotificationEvent event) {
        logger.info("Admin {} notificado de nuevo curso ID: {}",
            userId, event.getTargetId());

        // Aquí se puede agregar lógica adicional como:
        // - Enviar email de resumen
        // - Actualizar dashboard administrativo
        // - Validar configuración del curso
    }

    /**
     * Manejar evento de eliminación de curso
     */
    private void handleCourseDeleted(NotificationEvent event) {
        logger.warn("Admin {} notificado de eliminación de curso ID: {}",
            userId, event.getTargetId());

        // Aquí se puede agregar lógica adicional como:
        // - Notificar a otros admins
        // - Archivar datos del curso
        // - Generar reporte de auditoría
    }
}
