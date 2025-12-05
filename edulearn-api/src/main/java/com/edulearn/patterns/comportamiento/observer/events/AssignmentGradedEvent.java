package com.edulearn.patterns.comportamiento.observer.events;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * SPRING EVENTS - Evento de Tarea Calificada
 * ==========================================
 * Evento publicado cuando se califica una tarea.
 */
public class AssignmentGradedEvent extends ApplicationEvent {

    private final Integer estudianteId;
    private final String estudianteNombre;
    private final Long tareaId;
    private final String tareaNombre;
    private final BigDecimal calificacion;
    private final String feedback;

    public AssignmentGradedEvent(Object source, Integer estudianteId, String estudianteNombre,
                                  Long tareaId, String tareaNombre,
                                  BigDecimal calificacion, String feedback) {
        super(source);
        this.estudianteId = estudianteId;
        this.estudianteNombre = estudianteNombre;
        this.tareaId = tareaId;
        this.tareaNombre = tareaNombre;
        this.calificacion = calificacion;
        this.feedback = feedback;
    }

    public Integer getEstudianteId() {
        return estudianteId;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public Long getTareaId() {
        return tareaId;
    }

    public String getTareaNombre() {
        return tareaNombre;
    }

    public BigDecimal getCalificacion() {
        return calificacion;
    }

    public String getFeedback() {
        return feedback;
    }

    @Override
    public String toString() {
        return String.format("AssignmentGradedEvent{estudiante='%s', tarea='%s', nota=%s}",
            estudianteNombre, tareaNombre, calificacion);
    }
}
