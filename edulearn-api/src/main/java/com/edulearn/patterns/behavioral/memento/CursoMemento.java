package com.edulearn.patterns.behavioral.memento;

import java.time.LocalDateTime;

/**
 * PATRÓN MEMENTO - Memento
 * ========================
 * Captura el estado interno de un Curso sin violar el encapsulamiento.
 * Permite restaurar el curso a un estado anterior.
 */
public class CursoMemento {

    // Estado del curso
    private final Integer id;
    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final String tipoCurso;
    private final String estado;
    private final Integer profesorTitularId;
    private final String periodoAcademico;
    private final Integer duracion;
    private final String estrategiaEvaluacion;
    private final Integer cupoMaximo;

    // Metadata del memento
    private final LocalDateTime fechaCreacion;
    private final String operacion;
    private final String descripcionCambio;

    private CursoMemento(Builder builder) {
        this.id = builder.id;
        this.codigo = builder.codigo;
        this.nombre = builder.nombre;
        this.descripcion = builder.descripcion;
        this.tipoCurso = builder.tipoCurso;
        this.estado = builder.estado;
        this.profesorTitularId = builder.profesorTitularId;
        this.periodoAcademico = builder.periodoAcademico;
        this.duracion = builder.duracion;
        this.estrategiaEvaluacion = builder.estrategiaEvaluacion;
        this.cupoMaximo = builder.cupoMaximo;
        this.fechaCreacion = LocalDateTime.now();
        this.operacion = builder.operacion;
        this.descripcionCambio = builder.descripcionCambio;
    }

    // Getters - Solo lectura desde fuera
    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipoCurso() {
        return tipoCurso;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getProfesorTitularId() {
        return profesorTitularId;
    }

    public String getPeriodoAcademico() {
        return periodoAcademico;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public String getEstrategiaEvaluacion() {
        return estrategiaEvaluacion;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getOperacion() {
        return operacion;
    }

    public String getDescripcionCambio() {
        return descripcionCambio;
    }

    /**
     * Builder para crear Mementos
     */
    public static class Builder {
        private Integer id;
        private String codigo;
        private String nombre;
        private String descripcion;
        private String tipoCurso;
        private String estado;
        private Integer profesorTitularId;
        private String periodoAcademico;
        private Integer duracion;
        private String estrategiaEvaluacion;
        private Integer cupoMaximo;
        private String operacion;
        private String descripcionCambio;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public Builder tipoCurso(String tipoCurso) {
            this.tipoCurso = tipoCurso;
            return this;
        }

        public Builder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public Builder profesorTitularId(Integer profesorTitularId) {
            this.profesorTitularId = profesorTitularId;
            return this;
        }

        public Builder periodoAcademico(String periodoAcademico) {
            this.periodoAcademico = periodoAcademico;
            return this;
        }

        public Builder duracion(Integer duracion) {
            this.duracion = duracion;
            return this;
        }

        public Builder estrategiaEvaluacion(String estrategiaEvaluacion) {
            this.estrategiaEvaluacion = estrategiaEvaluacion;
            return this;
        }

        public Builder cupoMaximo(Integer cupoMaximo) {
            this.cupoMaximo = cupoMaximo;
            return this;
        }

        public Builder operacion(String operacion) {
            this.operacion = operacion;
            return this;
        }

        public Builder descripcionCambio(String descripcionCambio) {
            this.descripcionCambio = descripcionCambio;
            return this;
        }

        public CursoMemento build() {
            return new CursoMemento(this);
        }
    }

    @Override
    public String toString() {
        return String.format("CursoMemento[id=%d, nombre=%s, estado=%s, operacion=%s, fecha=%s]",
            id, nombre, estado, operacion, fechaCreacion);
    }
}
