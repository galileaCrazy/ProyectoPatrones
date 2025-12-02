package com.edulearn.patterns.creational.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fábrica Concreta: VirtualCourseFactory
 * Implementa CourseComponentFactory para crear familias de productos
 * específicos para cursos virtuales:
 * - CursoVirtual
 * - MaterialVirtual (VIDEO)
 * - EvaluacionVirtual (QUIZ)
 *
 * Patrón: Abstract Factory
 */
public class VirtualCourseFactory implements CourseComponentFactory {
    private static final Logger logger = LoggerFactory.getLogger(VirtualCourseFactory.class);

    @Override
    public ICurso crearCurso(String codigo, String nombre, String descripcion,
                            int profesorId, String periodoAcademico) {
        logger.info("✓ Creando Curso VIRTUAL...");
        return new CursoVirtual(codigo, nombre, descripcion, profesorId, periodoAcademico);
    }

    @Override
    public IMaterial crearMaterial(String nombre, String descripcion) {
        logger.info("✓ Creando Material VIRTUAL (VIDEO)...");
        return new MaterialVirtual(nombre, descripcion);
    }

    @Override
    public IEvaluacion crearEvaluacion(String nombre, String descripcion) {
        logger.info("✓ Creando Evaluación VIRTUAL (QUIZ)...");
        return new EvaluacionVirtual(nombre, descripcion);
    }

    @Override
    public String getTipoFactory() {
        return "Virtual";
    }
}
