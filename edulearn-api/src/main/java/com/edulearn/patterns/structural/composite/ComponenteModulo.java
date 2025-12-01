package com.edulearn.patterns.structural.composite;

/**
 * Component (Composite Pattern).
 * Define la interfaz común para objetos en la composición (tanto hojas como compuestos).
 */
public interface ComponenteModulo {
    /**
     * Obtiene el nombre del módulo
     */
    String getNombre();

    /**
     * Obtiene la descripción del módulo
     */
    String getDescripcion();

    /**
     * Calcula la duración total en horas (incluyendo submódulos si los tiene)
     */
    int calcularDuracionTotal();

    /**
     * Renderiza el módulo como texto con indentación según nivel
     */
    String renderizar(int nivel);

    /**
     * Obtiene la información del módulo en formato JSON-like
     */
    String obtenerInfo();

    /**
     * Verifica si es una hoja (no tiene hijos)
     */
    boolean esHoja();
}
