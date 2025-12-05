package com.edulearn.patterns.comportamiento.memento;

import java.time.LocalDateTime;

/**
 * Memento: Almacena el estado del progreso del estudiante
 * Este objeto es inmutable y solo puede ser leído por el Originator
 */
public class ProgresoMemento {
    private final Integer estudianteId;
    private final Integer cursoId;
    private final Integer moduloActualId;
    private final Integer porcentajeCompletado;
    private final Double calificacionAcumulada;
    private final Integer leccionesCompletadas;
    private final Integer evaluacionesCompletadas;
    private final String estadoCurso; // EN_PROGRESO, COMPLETADO, SUSPENDIDO
    private final LocalDateTime fechaGuardado;
    private final String notasEstudiante;

    public ProgresoMemento(Integer estudianteId, Integer cursoId, Integer moduloActualId,
                          Integer porcentajeCompletado, Double calificacionAcumulada,
                          Integer leccionesCompletadas, Integer evaluacionesCompletadas,
                          String estadoCurso, String notasEstudiante) {
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.moduloActualId = moduloActualId;
        this.porcentajeCompletado = porcentajeCompletado;
        this.calificacionAcumulada = calificacionAcumulada;
        this.leccionesCompletadas = leccionesCompletadas;
        this.evaluacionesCompletadas = evaluacionesCompletadas;
        this.estadoCurso = estadoCurso;
        this.fechaGuardado = LocalDateTime.now();
        this.notasEstudiante = notasEstudiante;
    }

    // Getters (sin setters para inmutabilidad)
    public Integer getEstudianteId() { return estudianteId; }
    public Integer getCursoId() { return cursoId; }
    public Integer getModuloActualId() { return moduloActualId; }
    public Integer getPorcentajeCompletado() { return porcentajeCompletado; }
    public Double getCalificacionAcumulada() { return calificacionAcumulada; }
    public Integer getLeccionesCompletadas() { return leccionesCompletadas; }
    public Integer getEvaluacionesCompletadas() { return evaluacionesCompletadas; }
    public String getEstadoCurso() { return estadoCurso; }
    public LocalDateTime getFechaGuardado() { return fechaGuardado; }
    public String getNotasEstudiante() { return notasEstudiante; }

    @Override
    public String toString() {
        return String.format("Progreso guardado: %d%% - Módulo: %d - Fecha: %s",
            porcentajeCompletado, moduloActualId, fechaGuardado);
    }
}
