package com.edulearn.patterns.comportamiento.observer.events;

import com.edulearn.model.Inscripcion;
import org.springframework.context.ApplicationEvent;

/**
 * SPRING EVENTS - Evento de Estudiante Inscrito
 * =============================================
 * Evento publicado cuando un estudiante se inscribe en un curso.
 */
public class StudentEnrolledEvent extends ApplicationEvent {

    private final Inscripcion inscripcion;
    private final String estudianteNombre;
    private final String cursoNombre;

    public StudentEnrolledEvent(Object source, Inscripcion inscripcion,
                                 String estudianteNombre, String cursoNombre) {
        super(source);
        this.inscripcion = inscripcion;
        this.estudianteNombre = estudianteNombre;
        this.cursoNombre = cursoNombre;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public String getEstudianteNombre() {
        return estudianteNombre;
    }

    public String getCursoNombre() {
        return cursoNombre;
    }

    @Override
    public String toString() {
        return String.format("StudentEnrolledEvent{estudiante='%s', curso='%s'}",
            estudianteNombre, cursoNombre);
    }
}
