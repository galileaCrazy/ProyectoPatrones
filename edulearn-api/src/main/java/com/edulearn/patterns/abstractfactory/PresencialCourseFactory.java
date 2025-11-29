package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fábrica Concreta: PresencialCourseFactory
 * Implementa CourseComponentFactory para crear familias de productos
 * específicos para cursos presenciales:
 * - CursoPresencial
 * - MaterialPresencial (PDF)
 * - EvaluacionPresencial (EXAMEN)
 *
 * Patrón: Abstract Factory
 */
public class PresencialCourseFactory implements CourseComponentFactory {
    private static final Logger logger = LoggerFactory.getLogger(PresencialCourseFactory.class);

    @Override
    public ICurso crearCurso(String codigo, String nombre, String descripcion,
                            int profesorId, String periodoAcademico) {
        logger.info("✓ Creando Curso PRESENCIAL...");
        return new CursoPresencial(codigo, nombre, descripcion, profesorId, periodoAcademico);
    }

    @Override
    public IMaterial crearMaterial(String nombre, String descripcion) {
        logger.info("✓ Creando Material PRESENCIAL (PDF)...");
        return new MaterialPresencial(nombre, descripcion);
    }

    @Override
    public IEvaluacion crearEvaluacion(String nombre, String descripcion) {
        logger.info("✓ Creando Evaluación PRESENCIAL (EXAMEN)...");
        return new EvaluacionPresencial(nombre, descripcion);
    }

    @Override
    public String getTipoFactory() {
        return "Presencial";
    }
}
