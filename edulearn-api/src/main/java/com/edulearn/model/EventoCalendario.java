package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para representar eventos en el Calendario Académico
 * Parte del patrón Singleton para configuración centralizada
 */
@Entity
@Table(name = "eventos_calendario")
public class EventoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false, length = 50)
    private String tipo; // CLASE, EXAMEN, FERIADO, EVENTO_ESPECIAL, ENTREGA, VACACIONES

    @Column(length = 50)
    private String categoria; // ACADEMICO, ADMINISTRATIVO, SOCIAL

    @ManyToOne
    @JoinColumn(name = "periodo_academico_id")
    private PeriodoAcademico periodoAcademico;

    @Column(name = "curso_id")
    private Integer cursoId; // Opcional: si el evento es específico de un curso

    @Column(length = 20)
    private String color; // Color para visualización en el calendario (hex)

    @Column(name = "es_dia_completo")
    private Boolean esDiaCompleto = false;

    @Column(name = "es_recurrente")
    private Boolean esRecurrente = false;

    @Column(name = "patron_recurrencia", length = 50)
    private String patronRecurrencia; // DIARIO, SEMANAL, MENSUAL

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "creado_por")
    private Integer creadoPor; // ID del usuario que creó el evento

    // Constructores
    public EventoCalendario() {
        this.fechaCreacion = LocalDateTime.now();
        this.color = "#3b82f6"; // Azul por defecto
    }

    public EventoCalendario(String titulo, LocalDateTime fechaInicio, LocalDateTime fechaFin, String tipo) {
        this();
        this.titulo = titulo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public PeriodoAcademico getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getEsDiaCompleto() {
        return esDiaCompleto;
    }

    public void setEsDiaCompleto(Boolean esDiaCompleto) {
        this.esDiaCompleto = esDiaCompleto;
    }

    public Boolean getEsRecurrente() {
        return esRecurrente;
    }

    public void setEsRecurrente(Boolean esRecurrente) {
        this.esRecurrente = esRecurrente;
    }

    public String getPatronRecurrencia() {
        return patronRecurrencia;
    }

    public void setPatronRecurrencia(String patronRecurrencia) {
        this.patronRecurrencia = patronRecurrencia;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(Integer creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * Verificar si el evento está activo en este momento
     */
    public boolean estaActivo() {
        LocalDateTime ahora = LocalDateTime.now();
        return (fechaInicio.isBefore(ahora) || fechaInicio.isEqual(ahora)) &&
               (fechaFin.isAfter(ahora) || fechaFin.isEqual(ahora));
    }

    /**
     * Verificar si el evento es próximo (dentro de las próximas 24 horas)
     */
    public boolean esProximo() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime dentroDe24Horas = ahora.plusHours(24);
        return fechaInicio.isAfter(ahora) && fechaInicio.isBefore(dentroDe24Horas);
    }
}
