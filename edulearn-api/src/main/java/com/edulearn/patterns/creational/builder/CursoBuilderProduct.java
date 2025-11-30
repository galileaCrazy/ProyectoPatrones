package com.edulearn.patterns.creational.builder;

import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * Producto creado por el Builder
 * Clase inmutable que representa un curso complejo
 */
public class CursoBuilderProduct {
    private static final Logger logger = Logger.getLogger(CursoBuilderProduct.class.getName());

    // Todos los campos son final para inmutabilidad
    private final String nombre;
    private final String codigo;
    private final String descripcion;
    private final String modalidad;
    private final String nivelDificultad;
    private final Integer duracionHoras;
    private final Integer cupoMaximo;
    private final Double precio;
    private final String categoria;
    private final Boolean incluyeCertificado;
    private final Boolean incluyeVideoLectures;
    private final Boolean incluyeEvaluaciones;
    private final Boolean incluyeProyectoFinal;
    private final String requisitosPrevios;
    private final String objetivos;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final String estado;

    /**
     * Constructor package-private - solo el Builder puede crear instancias
     */
    CursoBuilderProduct(
        String nombre, String codigo, String descripcion, String modalidad,
        String nivelDificultad, Integer duracionHoras, Integer cupoMaximo,
        Double precio, String categoria, Boolean incluyeCertificado,
        Boolean incluyeVideoLectures, Boolean incluyeEvaluaciones,
        Boolean incluyeProyectoFinal, String requisitosPrevios,
        String objetivos, LocalDate fechaInicio, LocalDate fechaFin, String estado
    ) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.modalidad = modalidad;
        this.nivelDificultad = nivelDificultad;
        this.duracionHoras = duracionHoras;
        this.cupoMaximo = cupoMaximo;
        this.precio = precio;
        this.categoria = categoria;
        this.incluyeCertificado = incluyeCertificado;
        this.incluyeVideoLectures = incluyeVideoLectures;
        this.incluyeEvaluaciones = incluyeEvaluaciones;
        this.incluyeProyectoFinal = incluyeProyectoFinal;
        this.requisitosPrevios = requisitosPrevios;
        this.objetivos = objetivos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;

        // Log de creación
        logger.info("Curso creado con Builder: " + this.getNombre() + " [" + this.getCodigo() + "]");
    }

    // ========== GETTERS (sin setters - objeto inmutable) ==========

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public Boolean getIncluyeCertificado() {
        return incluyeCertificado;
    }

    public Boolean getIncluyeVideoLectures() {
        return incluyeVideoLectures;
    }

    public Boolean getIncluyeEvaluaciones() {
        return incluyeEvaluaciones;
    }

    public Boolean getIncluyeProyectoFinal() {
        return incluyeProyectoFinal;
    }

    public String getRequisitosPrevios() {
        return requisitosPrevios;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    /**
     * Representación en String del curso
     */
    @Override
    public String toString() {
        return String.format(
            "[CURSO BUILDER] %s (%s) - %s | %d hrs | %s | %s | Cupo: %d | $%.2f",
            nombre, codigo, nivelDificultad, duracionHoras, modalidad, categoria, cupoMaximo, precio
        );
    }

    /**
     * Obtiene un resumen del curso
     */
    public String getResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("CURSO: ").append(nombre).append("\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append("Código: ").append(codigo).append("\n");
        sb.append("Modalidad: ").append(modalidad).append("\n");
        sb.append("Nivel: ").append(nivelDificultad).append("\n");
        sb.append("Duración: ").append(duracionHoras).append(" horas\n");
        sb.append("Cupo: ").append(cupoMaximo).append(" estudiantes\n");
        sb.append("Precio: $").append(String.format("%.2f", precio)).append("\n");
        sb.append("Categoría: ").append(categoria).append("\n");
        sb.append("\nCaracterísticas:\n");
        sb.append("  - Certificado: ").append(incluyeCertificado ? "SÍ" : "NO").append("\n");
        sb.append("  - Video Lectures: ").append(incluyeVideoLectures ? "SÍ" : "NO").append("\n");
        sb.append("  - Evaluaciones: ").append(incluyeEvaluaciones ? "SÍ" : "NO").append("\n");
        sb.append("  - Proyecto Final: ").append(incluyeProyectoFinal ? "SÍ" : "NO").append("\n");

        if (descripcion != null && !descripcion.isEmpty()) {
            sb.append("\nDescripción:\n").append(descripcion).append("\n");
        }

        if (requisitosPrevios != null && !requisitosPrevios.isEmpty()) {
            sb.append("\nRequisitos:\n").append(requisitosPrevios).append("\n");
        }

        if (objetivos != null && !objetivos.isEmpty()) {
            sb.append("\nObjetivos:\n").append(objetivos).append("\n");
        }

        sb.append("\nEstado: ").append(estado).append("\n");
        sb.append("=".repeat(60));

        return sb.toString();
    }
}
