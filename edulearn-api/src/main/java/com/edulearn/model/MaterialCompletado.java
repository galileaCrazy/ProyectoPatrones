package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que rastrea qué materiales ha completado cada estudiante
 */
@Entity
@Table(name = "materiales_completados")
public class MaterialCompletado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id", nullable = false)
    private Integer estudianteId;

    @Column(name = "curso_id", nullable = false)
    private Integer cursoId;

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @Column(name = "fecha_completado", nullable = false)
    private LocalDateTime fechaCompletado;

    @Column(name = "tiempo_visualizacion_segundos")
    private Integer tiempoVisualizacionSegundos;

    public MaterialCompletado() {
        this.fechaCompletado = LocalDateTime.now();
    }

    public MaterialCompletado(Integer estudianteId, Integer cursoId, Long materialId) {
        this();
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.materialId = materialId;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public Long getMaterialId() { return materialId; }
    public void setMaterialId(Long materialId) { this.materialId = materialId; }

    public LocalDateTime getFechaCompletado() { return fechaCompletado; }
    public void setFechaCompletado(LocalDateTime fechaCompletado) { this.fechaCompletado = fechaCompletado; }

    public Integer getTiempoVisualizacionSegundos() { return tiempoVisualizacionSegundos; }
    public void setTiempoVisualizacionSegundos(Integer tiempoVisualizacionSegundos) {
        this.tiempoVisualizacionSegundos = tiempoVisualizacionSegundos;
    }
}
