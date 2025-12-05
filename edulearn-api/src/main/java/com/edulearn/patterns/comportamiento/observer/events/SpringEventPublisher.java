package com.edulearn.patterns.comportamiento.observer.events;

import com.edulearn.model.Curso;
import com.edulearn.model.Evaluacion;
import com.edulearn.model.Inscripcion;
import com.edulearn.model.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * SPRING EVENTS - Publicador de Eventos
 * =====================================
 * Servicio que encapsula la publicación de eventos de Spring.
 * Proporciona métodos convenientes para publicar eventos desde
 * los servicios de negocio.
 *
 * USO EN SERVICIOS:
 * En lugar de inyectar NotificationOrchestrator, los servicios
 * inyectan este SpringEventPublisher y publican eventos.
 *
 * Ejemplo en CursoService:
 * ```java
 * @Autowired
 * private SpringEventPublisher eventPublisher;
 *
 * public Curso crearCurso(Curso curso) {
 *     Curso saved = cursoRepository.save(curso);
 *     eventPublisher.publishCourseCreated(saved, profesorId, profesorNombre);
 *     return saved;
 * }
 * ```
 */
@Service
public class SpringEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(SpringEventPublisher.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Publicar evento de curso creado
     */
    public void publishCourseCreated(Curso curso, Integer creadorId, String creadorNombre) {
        logger.info("Publicando evento: Curso creado - {}", curso.getNombre());

        CourseCreatedEvent event = new CourseCreatedEvent(
            this,
            curso,
            creadorId,
            creadorNombre
        );

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publicar evento de material subido
     */
    public void publishMaterialUploaded(Material material, Integer cursoId,
                                         String cursoNombre, Integer profesorId) {
        logger.info("Publicando evento: Material subido - {} en curso {}",
            material.getTitulo(), cursoNombre);

        MaterialUploadedEvent event = new MaterialUploadedEvent(
            this,
            material,
            cursoId,
            cursoNombre,
            profesorId
        );

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publicar evento de tarea creada
     */
    public void publishAssignmentCreated(Evaluacion tarea, Integer cursoId,
                                          String cursoNombre, Integer profesorId) {
        logger.info("Publicando evento: Tarea creada - {} en curso {}",
            tarea.getTitulo(), cursoNombre);

        AssignmentCreatedEvent event = new AssignmentCreatedEvent(
            this,
            tarea,
            cursoId,
            cursoNombre,
            profesorId
        );

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publicar evento de estudiante inscrito
     */
    public void publishStudentEnrolled(Inscripcion inscripcion,
                                        String estudianteNombre, String cursoNombre) {
        logger.info("Publicando evento: Estudiante {} inscrito en curso {}",
            estudianteNombre, cursoNombre);

        StudentEnrolledEvent event = new StudentEnrolledEvent(
            this,
            inscripcion,
            estudianteNombre,
            cursoNombre
        );

        applicationEventPublisher.publishEvent(event);
    }

    /**
     * Publicar evento de tarea calificada
     */
    public void publishAssignmentGraded(Integer estudianteId, String estudianteNombre,
                                         Long tareaId, String tareaNombre,
                                         BigDecimal calificacion, String feedback) {
        logger.info("Publicando evento: Tarea {} calificada para estudiante {}",
            tareaNombre, estudianteNombre);

        AssignmentGradedEvent event = new AssignmentGradedEvent(
            this,
            estudianteId,
            estudianteNombre,
            tareaId,
            tareaNombre,
            calificacion,
            feedback
        );

        applicationEventPublisher.publishEvent(event);
    }
}
