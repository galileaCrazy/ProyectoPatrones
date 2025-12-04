package com.edulearn.patterns.behavioral.state;

import com.edulearn.model.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN STATE - Contexto
 * =======================
 * Mantiene una referencia al estado actual y delega
 * las operaciones específicas del estado al objeto State actual.
 */
public class CursoContext {

    private static final Logger logger = LoggerFactory.getLogger(CursoContext.class);

    private Curso curso;
    private CursoState estadoActual;

    public CursoContext(Curso curso) {
        this.curso = curso;
        // Inicializar estado según el estado del curso
        inicializarEstado();
    }

    private void inicializarEstado() {
        String estadoCurso = curso.getEstado();
        if (estadoCurso == null) {
            estadoCurso = "en_creacion";
            curso.setEstado(estadoCurso);
        }

        switch (estadoCurso.toLowerCase()) {
            case "en_creacion":
                this.estadoActual = new EnCreacionState();
                break;
            case "activo":
                this.estadoActual = new ActivoState();
                break;
            case "finalizado":
                this.estadoActual = new FinalizadoState();
                break;
            case "archivado":
                this.estadoActual = new ArchivadoState();
                break;
            default:
                this.estadoActual = new EnCreacionState();
                curso.setEstado("en_creacion");
        }
    }

    public void setEstado(CursoState nuevoEstado) {
        this.estadoActual = nuevoEstado;
        logger.info("Curso {} cambió a estado: {}",
            curso.getId(), nuevoEstado.getNombreEstado());
    }

    public CursoState getEstado() {
        return estadoActual;
    }

    public Curso getCurso() {
        return curso;
    }

    // Delegación de métodos al estado actual
    public void activar() {
        estadoActual.activar(this);
    }

    public void finalizar() {
        estadoActual.finalizar(this);
    }

    public void archivar() {
        estadoActual.archivar(this);
    }

    public void publicar() {
        estadoActual.publicar(this);
    }

    public boolean puedeInscribirEstudiantes() {
        return estadoActual.puedeInscribirEstudiantes();
    }

    public boolean puedeModificarContenido() {
        return estadoActual.puedeModificarContenido();
    }

    public boolean puedeEliminar() {
        return estadoActual.puedeEliminar();
    }

    public String getNombreEstado() {
        return estadoActual.getNombreEstado();
    }

    public String getEstadoActual() {
        return estadoActual.getNombreEstado();
    }

    public String getDescripcionEstado() {
        return estadoActual.getDescripcion();
    }
}
