package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA para Materiales Educativos
 * Creados por el patrón Abstract Factory
 */
@Entity
@Table(name = "materiales")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modulo_id")
    private Long moduloId;

    @Column(name = "curso_id")
    private Integer cursoId;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 255)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMaterial tipo; // ENUM requerido por la BD: video, pdf, documento, enlace, imagen, audio

    @Column(name = "tipo_material", nullable = false, length = 50)
    private String tipoMaterial; // VIDEO, PDF, DOCUMENTO, IMAGE, PRESENTACION, LINK, OTHER

    @Column(name = "url_recurso", length = 500)
    private String urlRecurso;

    @Column(name = "archivo_path", length = 500)
    private String archivoPath;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(nullable = false)
    private Integer orden = 1;

    @Column(name = "es_obligatorio")
    private Boolean esObligatorio = false;

    @Column(name = "duracion_segundos")
    private Integer duracionSegundos;

    @Column(name = "requiere_visualizacion")
    private Boolean requiereVisualizacion = false;

    @Column(length = 20)
    private String estado = "activo"; // activo, inactivo

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Material() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Material(String titulo, String descripcion, String tipoMaterial) {
        this();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipoMaterial = tipoMaterial;
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

    public Integer getCursoId() {
        return cursoId;
    }

    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
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

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public Long getTamanoBytes() {
        return tamanoBytes;
    }

    public void setTamanoBytes(Long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public String getArchivoPath() {
        return archivoPath;
    }

    public void setArchivoPath(String archivoPath) {
        this.archivoPath = archivoPath;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getEsObligatorio() {
        return esObligatorio;
    }

    public void setEsObligatorio(Boolean esObligatorio) {
        this.esObligatorio = esObligatorio;
    }

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public Boolean getRequiereVisualizacion() {
        return requiereVisualizacion;
    }

    public void setRequiereVisualizacion(Boolean requiereVisualizacion) {
        this.requiereVisualizacion = requiereVisualizacion;
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

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // ENUM para el campo 'tipo' de la BD
    public enum TipoMaterial {
        video, pdf, documento, enlace, imagen, audio
    }
}
