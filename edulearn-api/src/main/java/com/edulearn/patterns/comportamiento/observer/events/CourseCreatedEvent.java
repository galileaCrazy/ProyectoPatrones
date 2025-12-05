package com.edulearn.patterns.comportamiento.observer.events;

import com.edulearn.model.Curso;
import org.springframework.context.ApplicationEvent;

/**
 * SPRING EVENTS - Evento de Curso Creado
 * ======================================
 * Evento de Spring que se publica cuando se crea un nuevo curso.
 * Este es un enfoque alternativo al patrón Observer usando
 * el sistema de eventos de Spring Boot.
 *
 * Ventajas sobre Observer tradicional:
 * - Integración nativa con Spring
 * - Desacoplamiento automático
 * - Soporte para eventos asíncronos (@Async)
 * - Transaccionalidad (@TransactionalEventListener)
 */
public class CourseCreatedEvent extends ApplicationEvent {

    private final Curso curso;
    private final Integer creadorId;
    private final String creadorNombre;

    public CourseCreatedEvent(Object source, Curso curso, Integer creadorId, String creadorNombre) {
        super(source);
        this.curso = curso;
        this.creadorId = creadorId;
        this.creadorNombre = creadorNombre;
    }

    public Curso getCurso() {
        return curso;
    }

    public Integer getCreadorId() {
        return creadorId;
    }

    public String getCreadorNombre() {
        return creadorNombre;
    }

    @Override
    public String toString() {
        return String.format("CourseCreatedEvent{curso='%s', creador='%s'}",
            curso.getNombre(), creadorNombre);
    }
}
