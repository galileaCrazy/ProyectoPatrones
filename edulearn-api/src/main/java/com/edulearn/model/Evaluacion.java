package com.edulearn.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para Evaluaciones
 * Creadas por el patrón Abstract Factory
 */
@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modulo_id", nullable = false)
    private Long moduloId;

    @Column(length = 200)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String tipo; // Campo requerido por la BD

    @Column(name = "tipo_evaluacion", nullable = false, length = 50)
    private String tipoEvaluacion; // QUIZ, EXAMEN, PROYECTO, TAREA, PRESENTACION

    @Column(name = "puntaje_maximo", precision = 10, scale = 2)
    private BigDecimal puntajeMaximo = new BigDecimal("100.00");

    @Column(name = "peso_porcentual", precision = 5, scale = 2)
    private BigDecimal pesoPorcentual = new BigDecimal("0.00");

    @Column(name = "intentos_permitidos")
    private Integer intentosPermitidos = 1;

    @Column(name = "tiempo_limite_minutos")
    private Integer tiempoLimiteMinutos;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Column(columnDefinition = "TEXT")
    private String instrucciones;

    @Column(length = 20)
    private String estado = "activa"; // activa, inactiva, finalizada

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Evaluacion() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Evaluacion(String titulo, String descripcion, String tipoEvaluacion) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoEvaluacion = tipoEvaluacion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public BigDecimal getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(BigDecimal puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public BigDecimal getPesoPorcentual() {
        return pesoPorcentual;
    }

    public void setPesoPorcentual(BigDecimal pesoPorcentual) {
        this.pesoPorcentual = pesoPorcentual;
    }

    public Integer getIntentosPermitidos() {
        return intentosPermitidos;
    }

    public void setIntentosPermitidos(Integer intentosPermitidos) {
        this.intentosPermitidos = intentosPermitidos;
    }

    public Integer getTiempoLimiteMinutos() {
        return tiempoLimiteMinutos;
    }

    public void setTiempoLimiteMinutos(Integer tiempoLimiteMinutos) {
        this.tiempoLimiteMinutos = tiempoLimiteMinutos;
    }

    public LocalDateTime getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDateTime fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
