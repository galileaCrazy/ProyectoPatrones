package com.edulearn.patterns.creational.builder;

import java.time.LocalDate;

/**
 * DIRECTOR (parte opcional del patrón Builder)
 * =============================================
 * Propósito: Encapsular las diferentes formas de construir objetos complejos.
 * Define secuencias de construcción predefinidas (recetas).
 *
 * Ventajas:
 * - Separa la lógica de construcción del código cliente
 * - Proporciona recetas predefinidas para casos comunes
 * - Facilita la creación de objetos complejos sin conocer los detalles
 */
public class CursoDirector {

    /**
     * Construye un curso básico con configuración mínima
     */
    public CursoBuilderProduct construirCursoBasico(String nombre) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setModalidad("PRESENCIAL")
            .setNivelDificultad("BASICO")
            .setDuracionHoras(20)
            .setCupoMaximo(30)
            .setPrecio(0.0)
            .setIncluyeCertificado(false)
            .setIncluyeEvaluaciones(true)
            .setEstado("PUBLICADO")
            .build();
    }

    /**
     * Construye un curso premium con todas las características
     */
    public CursoBuilderProduct construirCursoPremium(String nombre, String categoria) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setCategoria(categoria)
            .setModalidad("HIBRIDO")
            .setNivelDificultad("AVANZADO")
            .setDuracionHoras(80)
            .setCupoMaximo(20)
            .setPrecio(299.99)
            .setIncluyeCertificado(true)
            .setIncluyeVideoLectures(true)
            .setIncluyeEvaluaciones(true)
            .setIncluyeProyectoFinal(true)
            .setRequisitosPrevios("Conocimientos previos de programación")
            .setObjetivos("Dominar conceptos avanzados y aplicarlos en proyectos reales")
            .setEstado("PUBLICADO")
            .build();
    }

    /**
     * Construye un curso virtual estándar
     */
    public CursoBuilderProduct construirCursoVirtual(String nombre, Integer duracion) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setModalidad("VIRTUAL")
            .setNivelDificultad("INTERMEDIO")
            .setDuracionHoras(duracion)
            .setCupoMaximo(100)
            .setPrecio(99.99)
            .setIncluyeCertificado(true)
            .setIncluyeVideoLectures(true)
            .setIncluyeEvaluaciones(true)
            .setIncluyeProyectoFinal(false)
            .setEstado("PUBLICADO")
            .build();
    }

    /**
     * Construye un curso intensivo presencial
     */
    public CursoBuilderProduct construirCursoIntensivo(String nombre, LocalDate fechaInicio) {
        LocalDate fechaFin = fechaInicio.plusWeeks(2);

        return new CursoBuilder()
            .setNombre(nombre)
            .setModalidad("PRESENCIAL")
            .setNivelDificultad("INTERMEDIO")
            .setDuracionHoras(40)
            .setCupoMaximo(15)
            .setPrecio(199.99)
            .setFechaInicio(fechaInicio)
            .setFechaFin(fechaFin)
            .setIncluyeCertificado(true)
            .setIncluyeEvaluaciones(true)
            .setIncluyeProyectoFinal(true)
            .setDescripcion("Curso intensivo de 2 semanas con práctica intensiva")
            .setEstado("PUBLICADO")
            .build();
    }

    /**
     * Construye un curso gratuito de introducción
     */
    public CursoBuilderProduct construirCursoGratuito(String nombre, String categoria) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setCategoria(categoria)
            .setModalidad("VIRTUAL")
            .setNivelDificultad("BASICO")
            .setDuracionHoras(10)
            .setCupoMaximo(200)
            .setPrecio(0.0)
            .setIncluyeCertificado(false)
            .setIncluyeVideoLectures(true)
            .setIncluyeEvaluaciones(false)
            .setIncluyeProyectoFinal(false)
            .setDescripcion("Curso introductorio gratuito para principiantes")
            .setEstado("PUBLICADO")
            .build();
    }

    /**
     * Construye un curso corporativo personalizado
     */
    public CursoBuilderProduct construirCursoCorporativo(
        String nombre,
        Integer duracion,
        Integer cupo,
        LocalDate fechaInicio
    ) {
        LocalDate fechaFin = fechaInicio.plusMonths(1);

        return new CursoBuilder()
            .setNombre(nombre)
            .setCategoria("CORPORATIVO")
            .setModalidad("HIBRIDO")
            .setNivelDificultad("INTERMEDIO")
            .setDuracionHoras(duracion)
            .setCupoMaximo(cupo)
            .setPrecio(499.99)
            .setFechaInicio(fechaInicio)
            .setFechaFin(fechaFin)
            .setIncluyeCertificado(true)
            .setIncluyeVideoLectures(true)
            .setIncluyeEvaluaciones(true)
            .setIncluyeProyectoFinal(true)
            .setDescripcion("Curso personalizado para empresas con seguimiento dedicado")
            .setRequisitosPrevios("Determinado por la empresa")
            .setObjetivos("Upskilling del equipo en tecnologías específicas")
            .setEstado("BORRADOR")
            .build();
    }
}
