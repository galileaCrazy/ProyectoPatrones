package com.edulearn.patterns.creational.abstract_factory;

/**
 * Proveedor de factories - Retorna la factory apropiada según el nivel
 */
public class ContenidoFactoryProvider {

    /**
     * Obtiene la factory apropiada según el nivel de dificultad
     * @param nivel BASICO, INTERMEDIO, AVANZADO
     * @return Factory correspondiente
     */
    public static IContenidoFactory getFactory(String nivel) {
        switch (nivel.toUpperCase()) {
            case "BASICO":
                return new ContenidoBasicoFactory();
            case "INTERMEDIO":
                return new ContenidoIntermedioFactory();
            case "AVANZADO":
                return new ContenidoAvanzadoFactory();
            default:
                throw new IllegalArgumentException("Nivel no soportado: " + nivel);
        }
    }
}
