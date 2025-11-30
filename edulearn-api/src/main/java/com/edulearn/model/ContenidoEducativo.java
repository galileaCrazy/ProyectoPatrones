package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA para almacenar contenidos educativos creados
 * mediante el patrón Abstract Factory
 */
@Entity
@Table(name = "contenidos_educativos")
public class ContenidoEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo; // VIDEO, DOCUMENTO, QUIZ

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel; // BASICO, INTERMEDIO, AVANZADO

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion_estimada")
    private Integer duracionEstimada; // en minutos

    @Column(name = "contenido_renderizado", columnDefinition = "TEXT")
    private String contenidoRenderizado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "curso_id")
    private Long cursoId; // Referencia al curso (opcional)

    @Column(name = "activo")
    private Boolean activo = true;

    // Constructors
    public ContenidoEducativo() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public ContenidoEducativo(String tipo, String nivel, String descripcion, Integer duracionEstimada, String contenidoRenderizado) {
        this();
        this.tipo = tipo;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.duracionEstimada = duracionEstimada;
        this.contenidoRenderizado = contenidoRenderizado;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public String getContenidoRenderizado() {
        return contenidoRenderizado;
    }

    public void setContenidoRenderizado(String contenidoRenderizado) {
        this.contenidoRenderizado = contenidoRenderizado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
