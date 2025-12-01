package com.edulearn.patterns.creational.builder;

import com.edulearn.model.Curso;

/**
 * PATRÓN BUILDER
 * ==============
 * Propósito: Separar la construcción de un objeto complejo de su representación,
 * permitiendo crear diferentes representaciones usando el mismo proceso de construcción.
 *
 * Uso en EduLearn: Construir cursos del sistema con múltiples configuraciones
 * opcionales de manera flexible y legible.
 *
 * Ventajas:
 * - Permite construir objetos paso a paso
 * - El mismo proceso de construcción puede crear diferentes representaciones
 * - Aísla el código de construcción de la representación
 * - Proporciona mejor control sobre el proceso de construcción
 */
public class CursoBuilder {

    // Campos del modelo Curso existente
    private String codigo;
    private String nombre;
    private String descripcion = "";
    private String tipoCurso = "REGULAR"; // REGULAR, INTENSIVO, CERTIFICACION, etc
    private String estado = "ACTIVO"; // ACTIVO, INACTIVO, FINALIZADO
    private Integer profesorTitularId;
    private String periodoAcademico;
    private Integer duracion = 40; // en horas
    private String estrategiaEvaluacion; // PONDERADA, SIMPLE, COMPETENCIAS

    /**
     * Constructor vacío - genera código automático
     */
    public CursoBuilder() {
        this.codigo = "CURSO-" + System.currentTimeMillis();
    }

    /**
     * Constructor con nombre
     */
    public CursoBuilder(String nombre) {
        this();
        this.nombre = nombre;
    }

    // ========== SETTERS FLUENT API ==========

    public CursoBuilder setCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public CursoBuilder setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public CursoBuilder setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public CursoBuilder setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
        return this;
    }

    public CursoBuilder setEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public CursoBuilder setProfesorTitularId(Integer profesorTitularId) {
        this.profesorTitularId = profesorTitularId;
        return this;
    }

    public CursoBuilder setPeriodoAcademico(String periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
        return this;
    }

    public CursoBuilder setDuracion(Integer duracion) {
        this.duracion = duracion;
        return this;
    }

    public CursoBuilder setEstrategiaEvaluacion(String estrategiaEvaluacion) {
        this.estrategiaEvaluacion = estrategiaEvaluacion;
        return this;
    }

    /**
     * Método de construcción - crea el Curso
     */
    public Curso build() {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre del curso es obligatorio");
        }

        if (duracion <= 0) {
            throw new IllegalStateException("La duración debe ser mayor a 0");
        }

        // Crear y retornar el curso
        Curso curso = new Curso();
        curso.setCodigo(codigo);
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setTipoCurso(tipoCurso);
        curso.setEstado(estado);
        curso.setProfesorTitularId(profesorTitularId);
        curso.setPeriodoAcademico(periodoAcademico);
        curso.setDuracion(duracion);
        curso.setEstrategiaEvaluacion(estrategiaEvaluacion);

        return curso;
    }

    /**
     * Reset - permite reutilizar el builder
     */
    public void reset() {
        this.codigo = "CURSO-" + System.currentTimeMillis();
        this.nombre = null;
        this.descripcion = "";
        this.tipoCurso = "REGULAR";
        this.estado = "ACTIVO";
        this.profesorTitularId = null;
        this.periodoAcademico = null;
        this.duracion = 40;
        this.estrategiaEvaluacion = null;
    }
}
