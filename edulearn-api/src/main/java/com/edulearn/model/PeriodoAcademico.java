package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad para representar un Período Académico
 * Parte del patrón Singleton para configuración centralizada
 */
@Entity
@Table(name = "periodos_academicos")
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo; // Ej: "2025-1", "2025-2"

    @Column(nullable = false, length = 100)
    private String nombre; // Ej: "Primer Semestre 2025"

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "fecha_inicio_inscripciones")
    private LocalDate fechaInicioInscripciones;

    @Column(name = "fecha_fin_inscripciones")
    private LocalDate fechaFinInscripciones;

    @Column(nullable = false, length = 20)
    private String estado; // ACTIVO, FINALIZADO, PROXIMO

    @Column(name = "es_actual")
    private Boolean esActual = false; // Marca el período académico actual

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Constructores
    public PeriodoAcademico() {}

    public PeriodoAcademico(String codigo, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = "PROXIMO";
        this.esActual = false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getFechaInicioInscripciones() {
        return fechaInicioInscripciones;
    }

    public void setFechaInicioInscripciones(LocalDate fechaInicioInscripciones) {
        this.fechaInicioInscripciones = fechaInicioInscripciones;
    }

    public LocalDate getFechaFinInscripciones() {
        return fechaFinInscripciones;
    }

    public void setFechaFinInscripciones(LocalDate fechaFinInscripciones) {
        this.fechaFinInscripciones = fechaFinInscripciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getEsActual() {
        return esActual;
    }

    public void setEsActual(Boolean esActual) {
        this.esActual = esActual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Verificar si el período está activo actualmente
     */
    public boolean estaActivo() {
        LocalDate hoy = LocalDate.now();
        return (fechaInicio.isBefore(hoy) || fechaInicio.isEqual(hoy)) &&
               (fechaFin.isAfter(hoy) || fechaFin.isEqual(hoy)) &&
               "ACTIVO".equals(estado);
    }

    /**
     * Verificar si están abiertas las inscripciones
     */
    public boolean inscripcionesAbiertas() {
        if (fechaInicioInscripciones == null || fechaFinInscripciones == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return (fechaInicioInscripciones.isBefore(hoy) || fechaInicioInscripciones.isEqual(hoy)) &&
               (fechaFinInscripciones.isAfter(hoy) || fechaFinInscripciones.isEqual(hoy));
    }
}
