package com.edulearn.patterns.creational.builder;

import com.edulearn.model.Curso;

/**
 * DIRECTOR (parte opcional del patrón Builder)
 * =============================================
 * Propósito: Encapsular las diferentes formas de construir cursos.
 * Define secuencias de construcción predefinidas para el sistema real.
 *
 * Ventajas:
 * - Separa la lógica de construcción del código cliente
 * - Proporciona recetas predefinidas para casos comunes del sistema
 * - Facilita la creación de cursos sin conocer los detalles
 */
public class CursoDirector {

    /**
     * Construye un curso regular estándar
     */
    public Curso construirCursoRegular(String nombre, String periodoAcademico) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setTipoCurso("REGULAR")
            .setPeriodoAcademico(periodoAcademico)
            .setDuracion(40)
            .setEstado("ACTIVO")
            .build();
    }

    /**
     * Construye un curso intensivo
     */
    public Curso construirCursoIntensivo(String nombre, Integer profesorId) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setTipoCurso("INTENSIVO")
            .setDuracion(80)
            .setProfesorTitularId(profesorId)
            .setEstado("ACTIVO")
            .setDescripcion("Curso intensivo con mayor carga horaria")
            .build();
    }

    /**
     * Construye un curso de certificación
     */
    public Curso construirCursoCertificacion(String nombre, Integer profesorId, String periodoAcademico) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setTipoCurso("CERTIFICACION")
            .setDuracion(60)
            .setProfesorTitularId(profesorId)
            .setPeriodoAcademico(periodoAcademico)
            .setEstado("ACTIVO")
            .setDescripcion("Curso de certificación profesional")
            .build();
    }

    /**
     * Construye un curso completo con todos los parámetros
     */
    public Curso construirCursoCompleto(
        String nombre,
        String descripcion,
        String tipoCurso,
        Integer profesorId,
        String periodoAcademico,
        Integer duracion
    ) {
        return new CursoBuilder()
            .setNombre(nombre)
            .setDescripcion(descripcion)
            .setTipoCurso(tipoCurso)
            .setProfesorTitularId(profesorId)
            .setPeriodoAcademico(periodoAcademico)
            .setDuracion(duracion)
            .setEstado("ACTIVO")
            .build();
    }
}
