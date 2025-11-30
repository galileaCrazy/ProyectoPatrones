package com.edulearn.patterns.creational.builder;

import java.time.LocalDate;

/**
 * PATRÓN BUILDER
 * ==============
 * Propósito: Separar la construcción de un objeto complejo de su representación,
 * permitiendo crear diferentes representaciones usando el mismo proceso de construcción.
 *
 * Uso en EduLearn: Construir cursos complejos con múltiples configuraciones opcionales
 * de manera flexible y legible.
 *
 * Ventajas:
 * - Permite construir objetos paso a paso
 * - El mismo proceso de construcción puede crear diferentes representaciones
 * - Aísla el código de construcción de la representación
 * - Proporciona mejor control sobre el proceso de construcción
 */
public class CursoBuilder {

    // Campos obligatorios
    private String nombre;
    private String codigo;

    // Campos opcionales con valores por defecto
    private String descripcion = "";
    private String modalidad = "PRESENCIAL"; // PRESENCIAL, VIRTUAL, HIBRIDO
    private String nivelDificultad = "BASICO"; // BASICO, INTERMEDIO, AVANZADO
    private Integer duracionHoras = 40;
    private Integer cupoMaximo = 30;
    private Double precio = 0.0;
    private String categoria = "GENERAL";
    private Boolean incluyeCertificado = false;
    private Boolean incluyeVideoLectures = false;
    private Boolean incluyeEvaluaciones = true;
    private Boolean incluyeProyectoFinal = false;
    private String requisitosPrevios = "";
    private String objetivos = "";
    private LocalDate fechaInicio = null;
    private LocalDate fechaFin = null;
    private String estado = "BORRADOR"; // BORRADOR, PUBLICADO, ARCHIVADO

    /**
     * Constructor - requiere campos obligatorios
     */
    public CursoBuilder(String nombre, String codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
    }

    /**
     * Constructor vacío - genera código automático
     */
    public CursoBuilder() {
        this.codigo = "CURSO-" + System.currentTimeMillis();
    }

    // ========== SETTERS FLUENT API ==========

    public CursoBuilder setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public CursoBuilder setCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public CursoBuilder setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public CursoBuilder setModalidad(String modalidad) {
        this.modalidad = modalidad;
        return this;
    }

    public CursoBuilder setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
        return this;
    }

    public CursoBuilder setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
        return this;
    }

    public CursoBuilder setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
        return this;
    }

    public CursoBuilder setPrecio(Double precio) {
        this.precio = precio;
        return this;
    }

    public CursoBuilder setCategoria(String categoria) {
        this.categoria = categoria;
        return this;
    }

    public CursoBuilder setIncluyeCertificado(Boolean incluyeCertificado) {
        this.incluyeCertificado = incluyeCertificado;
        return this;
    }

    public CursoBuilder setIncluyeVideoLectures(Boolean incluyeVideoLectures) {
        this.incluyeVideoLectures = incluyeVideoLectures;
        return this;
    }

    public CursoBuilder setIncluyeEvaluaciones(Boolean incluyeEvaluaciones) {
        this.incluyeEvaluaciones = incluyeEvaluaciones;
        return this;
    }

    public CursoBuilder setIncluyeProyectoFinal(Boolean incluyeProyectoFinal) {
        this.incluyeProyectoFinal = incluyeProyectoFinal;
        return this;
    }

    public CursoBuilder setRequisitosPrevios(String requisitosPrevios) {
        this.requisitosPrevios = requisitosPrevios;
        return this;
    }

    public CursoBuilder setObjetivos(String objetivos) {
        this.objetivos = objetivos;
        return this;
    }

    public CursoBuilder setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public CursoBuilder setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public CursoBuilder setEstado(String estado) {
        this.estado = estado;
        return this;
    }

    /**
     * Método de construcción - crea el producto final
     */
    public CursoBuilderProduct build() {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre del curso es obligatorio");
        }

        if (duracionHoras <= 0) {
            throw new IllegalStateException("La duración debe ser mayor a 0");
        }

        if (cupoMaximo <= 0) {
            throw new IllegalStateException("El cupo máximo debe ser mayor a 0");
        }

        // Crear y retornar el producto
        return new CursoBuilderProduct(
            nombre, codigo, descripcion, modalidad, nivelDificultad,
            duracionHoras, cupoMaximo, precio, categoria, incluyeCertificado,
            incluyeVideoLectures, incluyeEvaluaciones, incluyeProyectoFinal,
            requisitosPrevios, objetivos, fechaInicio, fechaFin, estado
        );
    }

    /**
     * Reset - permite reutilizar el builder
     */
    public void reset() {
        this.nombre = null;
        this.codigo = "CURSO-" + System.currentTimeMillis();
        this.descripcion = "";
        this.modalidad = "PRESENCIAL";
        this.nivelDificultad = "BASICO";
        this.duracionHoras = 40;
        this.cupoMaximo = 30;
        this.precio = 0.0;
        this.categoria = "GENERAL";
        this.incluyeCertificado = false;
        this.incluyeVideoLectures = false;
        this.incluyeEvaluaciones = true;
        this.incluyeProyectoFinal = false;
        this.requisitosPrevios = "";
        this.objetivos = "";
        this.fechaInicio = null;
        this.fechaFin = null;
        this.estado = "BORRADOR";
    }
}
