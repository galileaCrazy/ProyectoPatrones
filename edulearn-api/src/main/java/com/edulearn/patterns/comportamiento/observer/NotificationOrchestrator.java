package com.edulearn.patterns.comportamiento.observer;

import com.edulearn.model.Curso;
import com.edulearn.model.Evaluacion;
import com.edulearn.model.Inscripcion;
import com.edulearn.model.Material;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;

/**
 * PATRÓN OBSERVER - Orquestador de Notificaciones
 * ===============================================
 * Servicio de alto nivel que simplifica el uso del patrón Observer
 * para los casos de uso específicos de EduLearn.
 *
 * Proporciona métodos convenientes para:
 * - Registrar usuarios como observers
 * - Enviar notificaciones contextuales
 * - Gestionar suscripciones a cursos
 * - Manejar eventos de negocio
 *
 * Este servicio actúa como FACADE sobre el patrón Observer,
 * proporcionando una API simple para los servicios de negocio.
 */
@Service
public class NotificationOrchestrator {

    private static final Logger logger = LoggerFactory.getLogger(NotificationOrchestrator.class);

    @Autowired
    private NotificationManager notificationManager;

    @Autowired
    private ObserverFactory observerFactory;

    @Autowired(required = false)
    private InscripcionRepository inscripcionRepository;

    @Autowired(required = false)
    private UsuarioRepository usuarioRepository;

    @PostConstruct
    public void init() {
        logger.info("NotificationOrchestrator inicializado");
    }

    // ================================================
    // GESTIÓN DE OBSERVERS - Registro y Suscripciones
    // ================================================

    /**
     * Registrar un usuario como observer en el sistema
     */
    public void registerUser(Integer userId, String userName, String userRole) {
        // Normalizar el rol para garantizar consistencia
        String normalizedRole = observerFactory.normalizeRole(userRole);

        Observer observer = observerFactory.createObserver(userId, userName, userRole);
        notificationManager.attach(observer);

        // Registrar también por rol para notificaciones broadcast por rol (usando el rol normalizado)
        notificationManager.attachToRole(observer, normalizedRole);

        logger.info("Usuario {} ({}) registrado como observer con rol {} (normalizado: {})",
            userId, userName, userRole, normalizedRole);
    }

    /**
     * Desregistrar un usuario del sistema de notificaciones
     */
    public void unregisterUser(Integer userId, String userName, String userRole) {
        Observer observer = observerFactory.createObserver(userId, userName, userRole);
        notificationManager.detach(observer);

        logger.info("Usuario {} ({}) desregistrado del sistema de notificaciones", userId, userName);
    }

    /**
     * Suscribir un estudiante a las notificaciones de un curso
     */
    public void subscribeStudentToCourse(Integer estudianteId, String estudianteNombre,
                                          Integer cursoId) {
        Observer observer = observerFactory.createStudentObserver(estudianteId, estudianteNombre);
        notificationManager.attachToCourse(observer, cursoId);

        logger.info("Estudiante {} suscrito a notificaciones del curso {}",
            estudianteId, cursoId);
    }

    /**
     * Desuscribir un estudiante de las notificaciones de un curso
     */
    public void unsubscribeStudentFromCourse(Integer estudianteId, String estudianteNombre,
                                               Integer cursoId) {
        Observer observer = observerFactory.createStudentObserver(estudianteId, estudianteNombre);
        notificationManager.detachFromCourse(observer, cursoId);

        logger.info("Estudiante {} desuscrito de notificaciones del curso {}",
            estudianteId, cursoId);
    }

    /**
     * Registrar el profesor de un curso
     */
    public void registerCourseTeacher(Integer cursoId, Integer profesorId) {
        notificationManager.registerCourseTeacher(cursoId, profesorId);
        logger.info("Profesor {} registrado para el curso {}", profesorId, cursoId);
    }

    // ================================================
    // NOTIFICACIONES DE EVENTOS - Casos de Uso
    // ================================================

    /**
     * CASO 1: Notificar creación de curso a todos los administradores
     */
    public void notifyCourseCreated(Curso curso) {
        NotificationEvent event = new NotificationEvent.Builder()
            .eventType(NotificationEvent.EventType.CURSO_CREADO)
            .title("Nuevo Curso Creado")
            .message(String.format("Se ha creado el curso '%s' (código: %s)",
                curso.getNombre(), curso.getCodigo()))
            .sourceUserId(curso.getProfesorTitularId())
            .targetId(curso.getId())
            .targetType("CURSO")
            .addMetadata("cursoNombre", curso.getNombre())
            .addMetadata("cursoCodigo", curso.getCodigo())
            .build();

        // Notificar a todos los administradores
        notificationManager.notifyRoleObservers("admin", event);

        logger.info("Notificación de curso creado enviada a administradores: {}", curso.getId());
    }

    /**
     * CASO 2: Notificar subida de material nuevo a estudiantes del curso
     */
    public void notifyMaterialUploaded(Material material, Integer cursoId, String cursoNombre) {
        NotificationEvent event = new NotificationEvent.Builder()
            .eventType(NotificationEvent.EventType.MATERIAL_AGREGADO)
            .title("Nuevo Material Disponible")
            .message(String.format("Se ha agregado nuevo material '%s' al curso '%s'",
                material.getTitulo(), cursoNombre))
            .sourceUserId(null) // El profesor que subió el material
            .targetId(material.getId().intValue())
            .targetType("MATERIAL")
            .addMetadata("cursoId", cursoId)
            .addMetadata("tipoMaterial", material.getTipoMaterial())
            .addMetadata("cursoNombre", cursoNombre)
            .build();

        // Notificar solo a estudiantes del curso específico
        notificationManager.notifyCourseObservers(cursoId, event);

        logger.info("Notificación de material nuevo enviada a estudiantes del curso {}",
            cursoId);
    }

    /**
     * CASO 3: Notificar creación de tarea a estudiantes del curso
     */
    public void notifyAssignmentCreated(Evaluacion tarea, Integer cursoId, String cursoNombre) {
        NotificationEvent event = new NotificationEvent.Builder()
            .eventType(NotificationEvent.EventType.TAREA_CREADA)
            .title("Nueva Tarea Asignada")
            .message(String.format("Nueva tarea '%s' en el curso '%s'. Fecha límite: %s",
                tarea.getTitulo(), cursoNombre,
                tarea.getFechaCierre() != null ? tarea.getFechaCierre() : "Sin límite"))
            .sourceUserId(null) // El profesor que creó la tarea
            .targetId(tarea.getId().intValue())
            .targetType("TAREA")
            .addMetadata("cursoId", cursoId)
            .addMetadata("cursoNombre", cursoNombre)
            .addMetadata("fechaCierre", tarea.getFechaCierre())
            .addMetadata("puntajeMaximo", tarea.getPuntajeMaximo())
            .build();

        // Notificar solo a estudiantes del curso específico
        notificationManager.notifyCourseObservers(cursoId, event);

        logger.info("Notificación de tarea creada enviada a estudiantes del curso {}",
            cursoId);
    }

    /**
     * CASO 4: Notificar inscripción de estudiante al profesor del curso
     */
    public void notifyStudentEnrolled(Inscripcion inscripcion, String estudianteNombre,
                                       String cursoNombre) {
        NotificationEvent event = new NotificationEvent.Builder()
            .eventType(NotificationEvent.EventType.ESTUDIANTE_INSCRITO)
            .title("Nuevo Estudiante Inscrito")
            .message(String.format("El estudiante %s se ha inscrito en tu curso '%s'",
                estudianteNombre, cursoNombre))
            .sourceUserId(inscripcion.getEstudianteId())
            .targetId(inscripcion.getCursoId())
            .targetType("INSCRIPCION")
            .addMetadata("estudianteId", inscripcion.getEstudianteId())
            .addMetadata("estudianteNombre", estudianteNombre)
            .addMetadata("cursoId", inscripcion.getCursoId())
            .addMetadata("cursoNombre", cursoNombre)
            .addMetadata("modalidad", inscripcion.getModalidad())
            .build();

        // Notificar al profesor del curso
        notificationManager.notifyCourseTeacher(inscripcion.getCursoId(), event);

        logger.info("Notificación de inscripción enviada al profesor del curso {}",
            inscripcion.getCursoId());
    }

    /**
     * CASO 5: Notificar calificación de tarea al estudiante
     */
    public void notifyAssignmentGraded(Integer estudianteId, String estudianteNombre,
                                        Long tareaId, String tareaNombre,
                                        BigDecimal calificacion, String feedback) {
        NotificationEvent event = new NotificationEvent.Builder()
            .eventType(NotificationEvent.EventType.TAREA_CALIFICADA)
            .title("Tarea Calificada")
            .message(String.format("Tu tarea '%s' ha sido calificada. Calificación: %s",
                tareaNombre, calificacion))
            .sourceUserId(null) // El profesor que calificó
            .targetId(tareaId.intValue())
            .targetType("TAREA")
            .addMetadata("estudianteId", estudianteId)
            .addMetadata("tareaId", tareaId)
            .addMetadata("tareaNombre", tareaNombre)
            .addMetadata("calificacion", calificacion)
            .addMetadata("feedback", feedback)
            .build();

        // Notificar solo al estudiante específico
        notificationManager.notifySpecificUser(estudianteId, event);

        logger.info("Notificación de calificación enviada al estudiante {}", estudianteId);
    }

    /**
     * Notificar entrega de tarea al profesor
     */
    public void notifyAssignmentSubmitted(Integer estudianteId, String estudianteNombre,
                                           Long tareaId, String tareaNombre,
                                           Integer cursoId) {
        NotificationEvent event = new NotificationEvent.Builder()
            .eventType(NotificationEvent.EventType.TAREA_ENTREGADA)
            .title("Tarea Entregada")
            .message(String.format("El estudiante %s ha entregado la tarea '%s'",
                estudianteNombre, tareaNombre))
            .sourceUserId(estudianteId)
            .targetId(tareaId.intValue())
            .targetType("TAREA")
            .addMetadata("estudianteId", estudianteId)
            .addMetadata("estudianteNombre", estudianteNombre)
            .addMetadata("tareaId", tareaId)
            .addMetadata("tareaNombre", tareaNombre)
            .addMetadata("cursoId", cursoId)
            .build();

        // Notificar al profesor del curso
        notificationManager.notifyCourseTeacher(cursoId, event);

        logger.info("Notificación de entrega enviada al profesor del curso {}", cursoId);
    }

    // ================================================
    // MÉTODOS DE UTILIDAD
    // ================================================

    /**
     * Suscribir automáticamente a todos los estudiantes inscritos en un curso
     */
    public void subscribeAllStudentsToCourse(Integer cursoId) {
        if (inscripcionRepository == null) {
            logger.warn("InscripcionRepository no disponible. No se pueden suscribir estudiantes.");
            return;
        }

        // Obtener todas las inscripciones activas del curso
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);

        for (Inscripcion inscripcion : inscripciones) {
            if ("Activa".equalsIgnoreCase(inscripcion.getEstadoInscripcion())) {
                // Obtener nombre del estudiante (simplificado)
                String estudianteNombre = "Estudiante " + inscripcion.getEstudianteId();

                subscribeStudentToCourse(
                    inscripcion.getEstudianteId(),
                    estudianteNombre,
                    cursoId
                );
            }
        }

        logger.info("Estudiantes del curso {} suscritos a notificaciones", cursoId);
    }

    /**
     * Obtener estadísticas del sistema de notificaciones
     */
    public void logStatistics() {
        var stats = notificationManager.getStatistics();
        logger.info("=== Estadísticas del Sistema de Notificaciones ===");
        logger.info("Total de observadores: {}", stats.get("totalObservers"));
        logger.info("Cursos con observadores: {}", stats.get("coursesWithObservers"));
        logger.info("Roles con observadores: {}", stats.get("rolesWithObservers"));
        logger.info("Profesores registrados: {}", stats.get("registeredTeachers"));
    }

    // ================================================
    // MÉTODOS DELEGADOS AL NOTIFICATIONMANAGER
    // ================================================

    /**
     * Notificar a todos los observadores de un rol específico
     */
    public void notifyRoleObservers(String role, NotificationEvent event) {
        notificationManager.notifyRoleObservers(role, event);
    }

    /**
     * Notificar a un usuario específico
     */
    public void notifySpecificUser(Integer userId, NotificationEvent event) {
        notificationManager.notifySpecificUser(userId, event);
    }
}
