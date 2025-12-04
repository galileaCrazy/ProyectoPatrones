package com.edulearn.patterns.behavioral.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN STATE - Estado Concreto: Archivado
 * ==========================================
 * Representa un curso archivado para referencia histórica.
 * Solo lectura, sin ninguna modificación permitida.
 */
public class ArchivadoState implements CursoState {

    private static final Logger logger = LoggerFactory.getLogger(ArchivadoState.class);

    @Override
    public void activar(CursoContext context) {
        logger.warn("No se puede activar un curso archivado");
        throw new IllegalStateException("Un curso archivado no puede volver a estado activo");
    }

    @Override
    public void finalizar(CursoContext context) {
        logger.warn("No se puede finalizar un curso ya archivado");
        throw new IllegalStateException("Un curso archivado ya está finalizado");
    }

    @Override
    public void archivar(CursoContext context) {
        logger.info("El curso ya está archivado");
        // No hace nada, ya está archivado
    }

    @Override
    public void publicar(CursoContext context) {
        logger.warn("No se puede publicar un curso archivado");
        throw new IllegalStateException("Un curso archivado no se puede publicar");
    }

    @Override
    public boolean puedeInscribirEstudiantes() {
        return false; // No permite inscripciones
    }

    @Override
    public boolean puedeModificarContenido() {
        return false; // Solo lectura
    }

    @Override
    public boolean puedeEliminar() {
        return true; // Puede eliminarse de forma permanente
    }

    @Override
    public String getNombreEstado() {
        return "archivado";
    }

    @Override
    public String getDescripcion() {
        return "Curso archivado. Solo para referencia histórica.";
    }
}
