package com.edulearn.patterns.creational.abstractfactory;

/**
 * Fábrica Abstracta: CourseComponentFactory
 * Define la interfaz para crear familias de objetos relacionados
 * (ICurso, IMaterial, IEvaluacion) sin especificar sus clases concretas
 *
 * Patrón: Abstract Factory
 * Propósito: Proporcionar una interfaz para crear familias de objetos relacionados
 * sin especificar sus clases concretas.
 */
public interface CourseComponentFactory {

    /**
     * Crea un curso específico según el tipo de fábrica
     * @param codigo código único del curso
     * @param nombre nombre del curso
     * @param descripcion descripción del curso
     * @param profesorId ID del profesor titular
     * @param periodoAcademico período académico del curso
     * @return ICurso - Producto abstracto
     */
    ICurso crearCurso(String codigo, String nombre, String descripcion,
                      int profesorId, String periodoAcademico);

    /**
     * Crea materiales específicos según el tipo de fábrica
     * @param nombre nombre del material
     * @param descripcion descripción del material
     * @return IMaterial - Producto abstracto
     */
    IMaterial crearMaterial(String nombre, String descripcion);

    /**
     * Crea evaluaciones específicas según el tipo de fábrica
     * @param nombre nombre de la evaluación
     * @param descripcion descripción de la evaluación
     * @return IEvaluacion - Producto abstracto
     */
    IEvaluacion crearEvaluacion(String nombre, String descripcion);

    /**
     * Obtiene el nombre del tipo de curso que crea esta fábrica
     * @return nombre del tipo (ej: "Presencial", "Virtual", "Híbrido")
     */
    String getTipoFactory();
}
