package com.edulearn.patterns.builder;

import com.edulearn.model.Curso;

/**
 * Builder - Permite construir objetos complejos paso a paso
 * Patrón: Builder
 * Propósito: Separar la construcción de un objeto complejo de su representación
 */
public class CursoBuilder {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoCurso;
    private Integer profesorTitularId;
    private String periodoAcademico;
    private Integer duracion;
    private String estado;

    public CursoBuilder() {
        this.estado = "activo";
    }

    public CursoBuilder codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public CursoBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public CursoBuilder descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public CursoBuilder tipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
        return this;
    }

    public CursoBuilder profesorTitularId(Integer profesorTitularId) {
        this.profesorTitularId = profesorTitularId;
        return this;
    }

    public CursoBuilder periodoAcademico(String periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
        return this;
    }

    public CursoBuilder duracion(Integer duracion) {
        this.duracion = duracion;
        return this;
    }

    public CursoBuilder estado(String estado) {
        this.estado = estado;
        return this;
    }

    public Curso build() {
        Curso curso = new Curso();
        curso.setCodigo(this.codigo);
        curso.setNombre(this.nombre);
        curso.setDescripcion(this.descripcion);
        curso.setTipoCurso(this.tipoCurso);
        curso.setProfesorTitularId(this.profesorTitularId);
        curso.setPeriodoAcademico(this.periodoAcademico);
        curso.setDuracion(this.duracion);
        curso.setEstado(this.estado);
        return curso;
    }
}
