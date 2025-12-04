package com.edulearn.patterns.behavioral.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN STATE - Estado Concreto: En Creación
 * ===========================================
 * Representa el estado inicial del curso cuando está siendo creado.
 * Permite modificaciones completas del contenido.
 */
public class EnCreacionState implements CursoState {

    private static final Logger logger = LoggerFactory.getLogger(EnCreacionState.class);

    @Override
    public void activar(CursoContext context) {
        logger.warn("No se puede activar un curso desde estado 'en_creacion'. Use publicar() primero.");
        throw new IllegalStateException("Debe publicar el curso antes de activarlo");
    }

    @Override
    public void finalizar(CursoContext context) {
        logger.warn("No se puede finalizar un curso en creación");
        throw new IllegalStateException("El curso debe estar activo para finalizarlo");
    }

    @Override
    public void archivar(CursoContext context) {
        logger.info("Archivando curso en creación (borrador)");
        context.setEstado(new ArchivadoState());
        context.getCurso().setEstado("archivado");
    }

    @Override
    public void publicar(CursoContext context) {
        logger.info("Publicando curso - Transición a estado ACTIVO");
        context.setEstado(new ActivoState());
        context.getCurso().setEstado("activo");
    }

    @Override
    public boolean puedeInscribirEstudiantes() {
        return false; // No se permiten inscripciones mientras está en creación
    }

    @Override
    public boolean puedeModificarContenido() {
        return true; // Modificación completa permitida
    }

    @Override
    public boolean puedeEliminar() {
        return true; // Se puede eliminar un borrador
    }

    @Override
    public String getNombreEstado() {
        return "en_creacion";
    }

    @Override
    public String getDescripcion() {
        return "Curso en proceso de creación. Contenido editable, sin inscripciones activas.";
    }
}
