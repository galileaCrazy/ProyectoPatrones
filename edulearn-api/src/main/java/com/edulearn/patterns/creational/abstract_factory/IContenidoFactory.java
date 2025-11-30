package com.edulearn.patterns.creational.abstract_factory;

/**
 * PATRÓN ABSTRACT FACTORY
 * =======================
 * Propósito: Proveer una interfaz para crear familias de objetos relacionados
 * sin especificar sus clases concretas.
 *
 * Uso en EduLearn: Crear diferentes tipos de contenido educativo (Video, Documento, Quiz)
 * con diferentes niveles de dificultad (Básico, Intermedio, Avanzado).
 *
 * Ventajas:
 * - Asegura que los productos de una familia sean compatibles
 * - Aísla las clases concretas del código cliente
 * - Facilita el intercambio de familias de productos
 * - Promueve consistencia entre productos relacionados
 */
public interface IContenidoFactory {
    IContenidoEducativo crearVideo();
    IContenidoEducativo crearDocumento();
    IContenidoEducativo crearQuiz();
    String getNivel(); // BASICO, INTERMEDIO, AVANZADO
}
