package com.edulearn.patterns.comportamiento.memento;

/**
 * Originator: Objeto que crea y restaura mementos
 * Representa el progreso actual del estudiante en un curso
 */
public class ProgresoOriginator {
    private Integer estudianteId;
    private Integer cursoId;
    private Integer moduloActualId;
    private Integer porcentajeCompletado;
    private Double calificacionAcumulada;
    private Integer leccionesCompletadas;
    private Integer evaluacionesCompletadas;
    private String estadoCurso;
    private String notasEstudiante;

    public ProgresoOriginator(Integer estudianteId, Integer cursoId) {
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.moduloActualId = 1;
        this.porcentajeCompletado = 0;
        this.calificacionAcumulada = 0.0;
        this.leccionesCompletadas = 0;
        this.evaluacionesCompletadas = 0;
        this.estadoCurso = "EN_PROGRESO";
        this.notasEstudiante = "";
    }

    /**
     * Crea un memento con el estado actual
     */
    public ProgresoMemento guardarEstado() {
        return new ProgresoMemento(
            estudianteId,
            cursoId,
            moduloActualId,
            porcentajeCompletado,
            calificacionAcumulada,
            leccionesCompletadas,
            evaluacionesCompletadas,
            estadoCurso,
            notasEstudiante
        );
    }

    /**
     * Restaura el estado desde un memento
     */
    public void restaurarEstado(ProgresoMemento memento) {
        this.estudianteId = memento.getEstudianteId();
        this.cursoId = memento.getCursoId();
        this.moduloActualId = memento.getModuloActualId();
        this.porcentajeCompletado = memento.getPorcentajeCompletado();
        this.calificacionAcumulada = memento.getCalificacionAcumulada();
        this.leccionesCompletadas = memento.getLeccionesCompletadas();
        this.evaluacionesCompletadas = memento.getEvaluacionesCompletadas();
        this.estadoCurso = memento.getEstadoCurso();
        this.notasEstudiante = memento.getNotasEstudiante();
    }

    // Métodos para modificar el estado
    public void avanzarModulo() {
        this.moduloActualId++;
        recalcularPorcentaje();
    }

    public void completarLeccion() {
        this.leccionesCompletadas++;
        recalcularPorcentaje();
    }

    public void completarEvaluacion(Double calificacion) {
        this.evaluacionesCompletadas++;
        this.calificacionAcumulada = (this.calificacionAcumulada + calificacion) / 2;
        recalcularPorcentaje();
    }

    private void recalcularPorcentaje() {
        // Cálculo simple: cada lección vale algo del progreso
        int progresoLecciones = leccionesCompletadas * 5;
        int progresoEvaluaciones = evaluacionesCompletadas * 15;
        this.porcentajeCompletado = Math.min(100, progresoLecciones + progresoEvaluaciones);

        if (this.porcentajeCompletado >= 100) {
            this.estadoCurso = "COMPLETADO";
        }
    }

    public void suspenderCurso() {
        this.estadoCurso = "SUSPENDIDO";
    }

    public void reanudarCurso() {
        this.estadoCurso = "EN_PROGRESO";
    }

    // Getters y Setters
    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public Integer getModuloActualId() { return moduloActualId; }
    public void setModuloActualId(Integer moduloActualId) { this.moduloActualId = moduloActualId; }

    public Integer getPorcentajeCompletado() { return porcentajeCompletado; }
    public void setPorcentajeCompletado(Integer porcentajeCompletado) { this.porcentajeCompletado = porcentajeCompletado; }

    public Double getCalificacionAcumulada() { return calificacionAcumulada; }
    public void setCalificacionAcumulada(Double calificacionAcumulada) { this.calificacionAcumulada = calificacionAcumulada; }

    public Integer getLeccionesCompletadas() { return leccionesCompletadas; }
    public void setLeccionesCompletadas(Integer leccionesCompletadas) { this.leccionesCompletadas = leccionesCompletadas; }

    public Integer getEvaluacionesCompletadas() { return evaluacionesCompletadas; }
    public void setEvaluacionesCompletadas(Integer evaluacionesCompletadas) { this.evaluacionesCompletadas = evaluacionesCompletadas; }

    public String getEstadoCurso() { return estadoCurso; }
    public void setEstadoCurso(String estadoCurso) { this.estadoCurso = estadoCurso; }

    public String getNotasEstudiante() { return notasEstudiante; }
    public void setNotasEstudiante(String notasEstudiante) { this.notasEstudiante = notasEstudiante; }

    @Override
    public String toString() {
        return String.format("Progreso: %d%% - Módulo: %d - Estado: %s - Calificación: %.2f",
            porcentajeCompletado, moduloActualId, estadoCurso, calificacionAcumulada);
    }
}
