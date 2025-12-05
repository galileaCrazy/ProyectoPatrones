package com.edulearn.patterns.comportamiento.observer.events;

import com.edulearn.model.Material;
import org.springframework.context.ApplicationEvent;

/**
 * SPRING EVENTS - Evento de Material Subido
 * =========================================
 * Evento publicado cuando un profesor sube nuevo material a un curso.
 */
public class MaterialUploadedEvent extends ApplicationEvent {

    private final Material material;
    private final Integer cursoId;
    private final String cursoNombre;
    private final Integer profesorId;

    public MaterialUploadedEvent(Object source, Material material, Integer cursoId,
                                  String cursoNombre, Integer profesorId) {
        super(source);
        this.material = material;
        this.cursoId = cursoId;
        this.cursoNombre = cursoNombre;
        this.profesorId = profesorId;
    }

    public Material getMaterial() {
        return material;
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
        return String.format("MaterialUploadedEvent{material='%s', curso='%s'}",
            material.getTitulo(), cursoNombre);
    }
}
