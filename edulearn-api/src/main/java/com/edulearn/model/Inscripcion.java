package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id")
    private Integer estudianteId;

    @Column(name = "curso_id")
    private Integer cursoId;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }
    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
}
