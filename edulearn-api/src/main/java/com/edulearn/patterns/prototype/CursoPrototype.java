package com.edulearn.patterns.prototype;

import com.edulearn.model.Curso;

/**
 * Prototype - Permite clonar objetos sin depender de sus clases concretas
 * Patrón: Prototype
 * Propósito: Especificar tipos de objetos mediante una instancia prototípica
 * y crear nuevos objetos copiando este prototipo
 */
public class CursoPrototype implements Cloneable {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoCurso;
    private Integer profesorTitularId;
    private String periodoAcademico;
    private Integer duracion;

    public CursoPrototype(Curso curso) {
        this.codigo = curso.getCodigo();
        this.nombre = curso.getNombre();
        this.descripcion = curso.getDescripcion();
        this.tipoCurso = curso.getTipoCurso();
        this.profesorTitularId = curso.getProfesorTitularId();
        this.periodoAcademico = curso.getPeriodoAcademico();
        this.duracion = curso.getDuracion();
    }

    @Override
    public CursoPrototype clone() {
        try {
            return (CursoPrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar curso", e);
        }
    }

    public Curso toEntity() {
        Curso curso = new Curso();
        curso.setCodigo(this.codigo);
        curso.setNombre(this.nombre);
        curso.setDescripcion(this.descripcion);
        curso.setTipoCurso(this.tipoCurso);
        curso.setProfesorTitularId(this.profesorTitularId);
        curso.setPeriodoAcademico(this.periodoAcademico);
        curso.setDuracion(this.duracion);
        curso.setEstado("activo");
        return curso;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getTipoCurso() { return tipoCurso; }
    public void setTipoCurso(String tipoCurso) { this.tipoCurso = tipoCurso; }
    public Integer getProfesorTitularId() { return profesorTitularId; }
    public void setProfesorTitularId(Integer profesorTitularId) { this.profesorTitularId = profesorTitularId; }
    public String getPeriodoAcademico() { return periodoAcademico; }
    public void setPeriodoAcademico(String periodoAcademico) { this.periodoAcademico = periodoAcademico; }
    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
}
