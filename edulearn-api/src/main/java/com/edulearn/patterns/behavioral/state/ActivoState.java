package com.edulearn.patterns.behavioral.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN STATE - Estado Concreto: Activo
 * =======================================
 * Representa un curso publicado y activo.
 * Permite inscripciones de estudiantes y modificaciones limitadas.
 */
public class ActivoState implements CursoState {

    private static final Logger logger = LoggerFactory.getLogger(ActivoState.class);

    @Override
    public void activar(CursoContext context) {
        logger.info("El curso ya está activo");
        // No hace nada, ya está en estado activo
    }

    @Override
    public void finalizar(CursoContext context) {
        logger.info("Finalizando curso - Transición a estado FINALIZADO");
        context.setEstado(new FinalizadoState());
        context.getCurso().setEstado("finalizado");
    }

    @Override
    public void archivar(CursoContext context) {
        logger.info("Archivando curso activo - Transición a estado ARCHIVADO");
        context.setEstado(new ArchivadoState());
        context.getCurso().setEstado("archivado");
    }

    @Override
    public void publicar(CursoContext context) {
        logger.info("El curso ya está publicado");
        // No hace nada, ya está publicado
    }

    @Override
    public boolean puedeInscribirEstudiantes() {
        return true; // Las inscripciones están abiertas
    }

    @Override
    public boolean puedeModificarContenido() {
        return true; // Se puede modificar contenido (con cuidado)
    }

    @Override
    public boolean puedeEliminar() {
        return false; // No se puede eliminar un curso activo con estudiantes
    }

    @Override
    public String getNombreEstado() {
        return "activo";
    }

    @Override
    public String getDescripcion() {
        return "Curso activo. Inscripciones abiertas, modificaciones permitidas.";
    }
}
