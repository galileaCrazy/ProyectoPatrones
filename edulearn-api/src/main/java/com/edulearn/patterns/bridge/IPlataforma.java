package com.edulearn.patterns.bridge;

/**
 * PATRÓN BRIDGE - Implementación
 * Define la interfaz para las diferentes plataformas
 */
public interface IPlataforma {
    String getNombre();
    String renderizarNavegacion();
    String renderizarCursos();
    String renderizarPerfil();
    String obtenerResolucion();
    String obtenerCapacidades();
}
