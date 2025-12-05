package com.edulearn.patterns.comportamiento.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN OBSERVER - Observer para Profesores
 * ==========================================
 * Observer concreto que representa a un usuario con rol Profesor.
 *
 * Eventos de interés:
 * - Inscripción de estudiantes en sus cursos (ESTUDIANTE_INSCRITO)
 * - Entrega de tareas (TAREA_ENTREGADA)
 * - Actualizaciones de sus cursos (CURSO_ACTUALIZADO)
 *
 * Los profesores reciben notificaciones sobre actividad
 * en los cursos que imparten.
 */
public class TeacherObserver extends AbstractUserObserver {

    private static final Logger logger = LoggerFactory.getLogger(TeacherObserver.class);

    public TeacherObserver(Integer userId, String userName) {
        super(userId, userName, "profesor");
        initializeInterests();
    }

    /**
     * Configurar eventos de interés para profesores
     */
    private void initializeInterests() {
        // Eventos de inscripciones
        interestedEvents.add(NotificationEvent.EventType.ESTUDIANTE_INSCRITO);

        // Eventos de tareas
        interestedEvents.add(NotificationEvent.EventType.TAREA_ENTREGADA);

        // Eventos de curso
        interestedEvents.add(NotificationEvent.EventType.CURSO_ACTUALIZADO);

        logger.info("TeacherObserver {} inicializado con {} eventos de interés",
            userId, interestedEvents.size());
    }

    @Override
    protected void processNotification(NotificationEvent event) {
        // Lógica específica para profesores
        switch (event.getEventType()) {
            case NotificationEvent.EventType.ESTUDIANTE_INSCRITO:
                handleStudentEnrolled(event);
                break;
            case NotificationEvent.EventType.TAREA_ENTREGADA:
                handleAssignmentSubmitted(event);
                break;
            default:
                // Procesamiento estándar
                break;
        }
    }

    /**
     * Manejar evento de inscripción de estudiante
     */
    private void handleStudentEnrolled(NotificationEvent event) {
        Integer cursoId = event.getTargetId();
        Integer estudianteId = event.getSourceUserId();

        logger.info("Profesor {} notificado: Estudiante {} inscrito en curso {}",
            userId, estudianteId, cursoId);

        // Aquí se puede agregar lógica adicional como:
        // - Enviar email de bienvenida al estudiante
        // - Actualizar lista de estudiantes en cache
        // - Verificar capacidad del curso
        // - Asignar al estudiante a un grupo
    }

    /**
     * Manejar evento de entrega de tarea
     */
    private void handleAssignmentSubmitted(NotificationEvent event) {
        Integer tareaId = event.getTargetId();
        Integer estudianteId = event.getSourceUserId();

        logger.info("Profesor {} notificado: Estudiante {} entregó tarea {}",
            userId, estudianteId, tareaId);

        // Aquí se puede agregar lógica adicional como:
        // - Agregar tarea a cola de calificación
        // - Enviar notificación push
        // - Actualizar dashboard del profesor
        // - Verificar fecha de entrega (tardía/a tiempo)
    }
}
