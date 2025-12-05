package com.edulearn.patterns.comportamiento.observer.events;

import com.edulearn.model.Evaluacion;
import org.springframework.context.ApplicationEvent;

/**
 * SPRING EVENTS - Evento de Tarea Creada
 * ======================================
 * Evento publicado cuando se crea una nueva tarea/evaluación.
 */
public class AssignmentCreatedEvent extends ApplicationEvent {

    private final Evaluacion tarea;
    private final Integer cursoId;
    private final String cursoNombre;
    private final Integer profesorId;

    public AssignmentCreatedEvent(Object source, Evaluacion tarea, Integer cursoId,
                                   String cursoNombre, Integer profesorId) {
        super(source);
        this.tarea = tarea;
        this.cursoId = cursoId;
        this.cursoNombre = cursoNombre;
        this.profesorId = profesorId;
    }

    public Evaluacion getTarea() {
        return tarea;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public String getCursoNombre() {
        return cursoNombre;
    }

    public Integer getProfesorId() {
        return profesorId;
    }

    @Override
    public String toString() {
        return String.format("AssignmentCreatedEvent{tarea='%s', curso='%s'}",
            tarea.getTitulo(), cursoNombre);
    }
}
