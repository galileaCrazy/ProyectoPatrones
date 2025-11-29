package com.edulearn.patterns.flyweight;

/**
 * PATRÓN FLYWEIGHT - Interfaz Flyweight
 * Define la interfaz para objetos que pueden compartir estado intrínseco
 */
public interface RecursoVisualFlyweight {
    /**
     * Renderiza el recurso visual con estado extrínseco
     * @param contexto Estado extrínseco específico del curso
     * @return HTML/representación del recurso
     */
    String renderizar(ContextoCurso contexto);

    /**
     * Obtiene información del recurso compartido
     */
    RecursoInfo obtenerInfo();
}
