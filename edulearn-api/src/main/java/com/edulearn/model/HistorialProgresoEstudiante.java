package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que almacena el historial de estados del progreso (mementos)
 * Cada registro es un punto de restauración
 */
@Entity
@Table(name = "historial_progreso_estudiante")
public class HistorialProgresoEstudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id", nullable = false)
    private Integer estudianteId;

    @Column(name = "curso_id", nullable = false)
    private Integer cursoId;

    @Column(name = "modulo_actual_id")
    private Integer moduloActualId;

    @Column(name = "porcentaje_completado")
    private Integer porcentajeCompletado;

    @Column(name = "calificacion_acumulada")
    private Double calificacionAcumulada;

    @Column(name = "lecciones_completadas")
    private Integer leccionesCompletadas;

    @Column(name = "evaluaciones_completadas")
    private Integer evaluacionesCompletadas;

    @Column(name = "estado_curso", length = 20)
    private String estadoCurso;

    @Column(name = "notas_estudiante", columnDefinition = "TEXT")
    private String notasEstudiante;

    @Column(name = "fecha_guardado", nullable = false)
    private LocalDateTime fechaGuardado;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    public HistorialProgresoEstudiante() {
        this.fechaGuardado = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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

    public LocalDateTime getFechaGuardado() { return fechaGuardado; }
    public void setFechaGuardado(LocalDateTime fechaGuardado) { this.fechaGuardado = fechaGuardado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
