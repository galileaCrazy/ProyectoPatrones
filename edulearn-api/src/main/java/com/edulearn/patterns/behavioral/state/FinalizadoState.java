package com.edulearn.patterns.behavioral.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN STATE - Estado Concreto: Finalizado
 * ==========================================
 * Representa un curso que ha terminado.
 * No permite inscripciones ni modificaciones.
 */
public class FinalizadoState implements CursoState {

    private static final Logger logger = LoggerFactory.getLogger(FinalizadoState.class);

    @Override
    public void activar(CursoContext context) {
        logger.warn("No se puede reactivar un curso finalizado directamente");
        throw new IllegalStateException("Un curso finalizado no puede volver a estado activo");
    }

    @Override
    public void finalizar(CursoContext context) {
        logger.info("El curso ya está finalizado");
        // No hace nada, ya está finalizado
    }

    @Override
    public void archivar(CursoContext context) {
        logger.info("Archivando curso finalizado - Transición a estado ARCHIVADO");
        context.setEstado(new ArchivadoState());
        context.getCurso().setEstado("archivado");
    }

    @Override
    public void publicar(CursoContext context) {
        logger.warn("No se puede publicar un curso finalizado");
        throw new IllegalStateException("Un curso finalizado no se puede publicar nuevamente");
    }

    @Override
    public boolean puedeInscribirEstudiantes() {
        return false; // No más inscripciones
    }

    @Override
    public boolean puedeModificarContenido() {
        return false; // Contenido bloqueado
    }

    @Override
    public boolean puedeEliminar() {
        return false; // Mantener registro histórico
    }

    @Override
    public String getNombreEstado() {
        return "finalizado";
    }

    @Override
    public String getDescripcion() {
        return "Curso finalizado. Solo lectura, sin inscripciones.";
    }
}
