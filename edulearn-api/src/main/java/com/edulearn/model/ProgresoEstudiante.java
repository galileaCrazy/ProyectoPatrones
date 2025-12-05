package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa el progreso del estudiante en un curso
 * Utiliza el patrón Memento para guardar y restaurar estados
 */
@Entity
@Table(name = "estudiante_progreso_memento")
public class ProgresoEstudiante {
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
    private String estadoCurso; // EN_PROGRESO, COMPLETADO, SUSPENDIDO

    @Column(name = "notas_estudiante", columnDefinition = "TEXT")
    private String notasEstudiante;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "ultima_leccion_vista", length = 200)
    private String ultimaLeccionVista;

    public ProgresoEstudiante() {
        this.fechaInicio = LocalDateTime.now();
        this.fechaUltimaActualizacion = LocalDateTime.now();
        this.porcentajeCompletado = 0;
        this.calificacionAcumulada = 0.0;
        this.leccionesCompletadas = 0;
        this.evaluacionesCompletadas = 0;
        this.estadoCurso = "EN_PROGRESO";
        this.moduloActualId = 1;
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

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }
    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) { this.fechaUltimaActualizacion = fechaUltimaActualizacion; }

    public String getUltimaLeccionVista() { return ultimaLeccionVista; }
    public void setUltimaLeccionVista(String ultimaLeccionVista) { this.ultimaLeccionVista = ultimaLeccionVista; }
}
