package com.edulearn.patterns.comportamiento.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN OBSERVER - Observer para Estudiantes
 * ===========================================
 * Observer concreto que representa a un usuario con rol Estudiante.
 *
 * Eventos de interés:
 * - Creación de tareas en sus cursos (TAREA_CREADA)
 * - Actualización de tareas (TAREA_ACTUALIZADA)
 * - Calificación de tareas (TAREA_CALIFICADA)
 * - Nuevo material agregado (MATERIAL_AGREGADO)
 * - Actualizaciones de curso (CURSO_ACTUALIZADO)
 *
 * Los estudiantes reciben notificaciones sobre actividad académica
 * en los cursos en los que están inscritos.
 */
public class StudentObserver extends AbstractUserObserver {

    private static final Logger logger = LoggerFactory.getLogger(StudentObserver.class);

    public StudentObserver(Integer userId, String userName) {
        super(userId, userName, "estudiante");
        initializeInterests();
    }

    /**
     * Configurar eventos de interés para estudiantes
     */
    private void initializeInterests() {
        // Eventos de tareas
        interestedEvents.add(NotificationEvent.EventType.TAREA_CREADA);
        interestedEvents.add(NotificationEvent.EventType.TAREA_ACTUALIZADA);
        interestedEvents.add(NotificationEvent.EventType.TAREA_CALIFICADA);

        // Eventos de material
        interestedEvents.add(NotificationEvent.EventType.MATERIAL_AGREGADO);

        // Eventos de curso
        interestedEvents.add(NotificationEvent.EventType.CURSO_ACTUALIZADO);

        // Eventos de becas - para el propio estudiante
        interestedEvents.add(NotificationEvent.EventType.BECA_APROBADA);
        interestedEvents.add(NotificationEvent.EventType.BECA_RECHAZADA);

        logger.info("StudentObserver {} inicializado con {} eventos de interés",
            userId, interestedEvents.size());
    }

    @Override
    protected void processNotification(NotificationEvent event) {
        // Lógica específica para estudiantes
        switch (event.getEventType()) {
            case NotificationEvent.EventType.TAREA_CREADA:
                handleAssignmentCreated(event);
                break;
            case NotificationEvent.EventType.TAREA_CALIFICADA:
                handleAssignmentGraded(event);
                break;
            case NotificationEvent.EventType.MATERIAL_AGREGADO:
                handleMaterialAdded(event);
                break;
            default:
                // Procesamiento estándar
                break;
        }
    }

    /**
     * Manejar evento de creación de tarea
     */
    private void handleAssignmentCreated(NotificationEvent event) {
        Integer tareaId = event.getTargetId();
        Integer cursoId = (Integer) event.getMetadata().get("cursoId");

        logger.info("Estudiante {} notificado: Nueva tarea {} en curso {}",
            userId, tareaId, cursoId);

        // Aquí se puede agregar lógica adicional como:
        // - Agregar tarea al calendario del estudiante
        // - Calcular días hasta la fecha límite
        // - Enviar recordatorio programado
        // - Actualizar indicadores de tareas pendientes
    }

    /**
     * Manejar evento de tarea calificada
     */
    private void handleAssignmentGraded(NotificationEvent event) {
        Integer tareaId = event.getTargetId();
        Object calificacion = event.getMetadata().get("calificacion");

        logger.info("Estudiante {} notificado: Tarea {} calificada con {}",
            userId, tareaId, calificacion);

        // Aquí se puede agregar lógica adicional como:
        // - Enviar notificación push prioritaria
        // - Actualizar promedio del estudiante
        // - Mostrar mensaje en UI
        // - Enviar email con feedback detallado
    }

    /**
     * Manejar evento de material agregado
     */
    private void handleMaterialAdded(NotificationEvent event) {
        Integer materialId = event.getTargetId();
        Integer cursoId = (Integer) event.getMetadata().get("cursoId");
        String tipoMaterial = (String) event.getMetadata().get("tipoMaterial");

        logger.info("Estudiante {} notificado: Nuevo material {} ({}) en curso {}",
            userId, materialId, tipoMaterial, cursoId);

        // Aquí se puede agregar lógica adicional como:
        // - Marcar como no visto
        // - Actualizar progreso del curso
        // - Enviar notificación si el material es obligatorio
        // - Preparar descarga automática si está configurado
    }
}
