package com.edulearn.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codigo;
    private String nombre;
    private String descripcion;

    @Column(name = "tipo_curso")
    private String tipoCurso;

    private String estado;

    @Column(name = "profesor_titular_id")
    private Integer profesorTitularId;

    @Column(name = "periodo_academico")
    private String periodoAcademico;

    private Integer duracion;

    @Column(name = "estrategia_evaluacion")
    private String estrategiaEvaluacion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoCurso() { return tipoCurso; }
    public void setTipoCurso(String tipoCurso) { this.tipoCurso = tipoCurso; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Integer getProfesorTitularId() { return profesorTitularId; }
    public void setProfesorTitularId(Integer profesorTitularId) { this.profesorTitularId = profesorTitularId; }
    public String getPeriodoAcademico() { return periodoAcademico; }
    public void setPeriodoAcademico(String periodoAcademico) { this.periodoAcademico = periodoAcademico; }
    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
    public String getEstrategiaEvaluacion() { return estrategiaEvaluacion; }
    public void setEstrategiaEvaluacion(String estrategiaEvaluacion) { this.estrategiaEvaluacion = estrategiaEvaluacion; }
}
