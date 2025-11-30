package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA para almacenar cursos creados con el patrón Builder
 */
@Entity
@Table(name = "cursos_builder")
public class CursoBuilder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "modalidad", length = 50)
    private String modalidad; // PRESENCIAL, VIRTUAL, HIBRIDO

    @Column(name = "nivel_dificultad", length = 50)
    private String nivelDificultad; // BASICO, INTERMEDIO, AVANZADO

    @Column(name = "duracion_horas")
    private Integer duracionHoras;

    @Column(name = "cupo_maximo")
    private Integer cupoMaximo;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "incluye_certificado")
    private Boolean incluyeCertificado = false;

    @Column(name = "incluye_video_lectures")
    private Boolean incluyeVideoLectures = false;

    @Column(name = "incluye_evaluaciones")
    private Boolean incluyeEvaluaciones = true;

    @Column(name = "incluye_proyecto_final")
    private Boolean incluyeProyectoFinal = false;

    @Column(name = "requisitos_previos", columnDefinition = "TEXT")
    private String requisitosPrevios;

    @Column(name = "objetivos", columnDefinition = "TEXT")
    private String objetivos;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "estado", length = 50)
    private String estado; // BORRADOR, PUBLICADO, ARCHIVADO

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "tipo_construccion", length = 50)
    private String tipoConstruccion; // BASICO, PREMIUM, VIRTUAL, INTENSIVO, GRATUITO, CORPORATIVO, CUSTOM

    // Constructor
    public CursoBuilder() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getIncluyeCertificado() {
        return incluyeCertificado;
    }

    public void setIncluyeCertificado(Boolean incluyeCertificado) {
        this.incluyeCertificado = incluyeCertificado;
    }

    public Boolean getIncluyeVideoLectures() {
        return incluyeVideoLectures;
    }

    public void setIncluyeVideoLectures(Boolean incluyeVideoLectures) {
        this.incluyeVideoLectures = incluyeVideoLectures;
    }

    public Boolean getIncluyeEvaluaciones() {
        return incluyeEvaluaciones;
    }

    public void setIncluyeEvaluaciones(Boolean incluyeEvaluaciones) {
        this.incluyeEvaluaciones = incluyeEvaluaciones;
    }

    public Boolean getIncluyeProyectoFinal() {
        return incluyeProyectoFinal;
    }

    public void setIncluyeProyectoFinal(Boolean incluyeProyectoFinal) {
        this.incluyeProyectoFinal = incluyeProyectoFinal;
    }

    public String getRequisitosPrevios() {
        return requisitosPrevios;
    }

    public void setRequisitosPrevios(String requisitosPrevios) {
        this.requisitosPrevios = requisitosPrevios;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
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

    public String getTipoConstruccion() {
        return tipoConstruccion;
    }

    public void setTipoConstruccion(String tipoConstruccion) {
        this.tipoConstruccion = tipoConstruccion;
    }
}
