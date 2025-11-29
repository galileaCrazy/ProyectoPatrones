package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fábrica Concreta: HibridoCourseFactory
 * Implementa CourseComponentFactory para crear familias de productos
 * específicos para cursos híbridos:
 * - CursoHibrido
 * - MaterialHibrido (DOCUMENTO)
 * - EvaluacionHibrida (PROYECTO)
 *
 * Patrón: Abstract Factory
 */
public class HibridoCourseFactory implements CourseComponentFactory {
    private static final Logger logger = LoggerFactory.getLogger(HibridoCourseFactory.class);

    @Override
    public ICurso crearCurso(String codigo, String nombre, String descripcion,
                            int profesorId, String periodoAcademico) {
        logger.info("✓ Creando Curso HÍBRIDO...");
        return new CursoHibrido(codigo, nombre, descripcion, profesorId, periodoAcademico);
    }

    @Override
    public IMaterial crearMaterial(String nombre, String descripcion) {
        logger.info("✓ Creando Material HÍBRIDO (DOCUMENTO)...");
        return new MaterialHibrido(nombre, descripcion);
    }

    @Override
    public IEvaluacion crearEvaluacion(String nombre, String descripcion) {
        logger.info("✓ Creando Evaluación HÍBRIDA (PROYECTO)...");
        return new EvaluacionHibrida(nombre, descripcion);
    }

    @Override
    public String getTipoFactory() {
        return "Híbrido";
    }
}
