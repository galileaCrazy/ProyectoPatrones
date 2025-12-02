package com.edulearn.patterns.comportamiento.strategy;

import java.util.Map;

/**
 * Strategy Pattern - Interfaz para estrategias de evaluación
 *
 * Esta interfaz define el contrato que todas las estrategias de evaluación
 * deben cumplir. Cada estrategia implementa su propia lógica para calcular
 * la calificación final de un estudiante.
 *
 * Patrón: Strategy
 * Propósito: Permitir diferentes algoritmos de evaluación intercambiables
 */
public interface EstrategiaEvaluacion {

    /**
     * Calcula la calificación final basándose en las calificaciones parciales
     *
     * @param calificaciones Mapa con las calificaciones del estudiante
     *                       Ejemplo: {"tareas": 85.0, "examenes": 90.0, "proyecto": 88.0}
     * @return Calificación final calculada según la estrategia
     */
    double calcularCalificacionFinal(Map<String, Double> calificaciones);

    /**
     * Obtiene el nombre de la estrategia
     *
     * @return Nombre de la estrategia
     */
    String getNombreEstrategia();

    /**
     * Obtiene una descripción de cómo funciona esta estrategia
     *
     * @return Descripción de la estrategia
     */
    String getDescripcion();

    /**
     * Valida que las calificaciones proporcionadas sean válidas para esta estrategia
     *
     * @param calificaciones Mapa con las calificaciones a validar
     * @return true si las calificaciones son válidas, false en caso contrario
     */
    boolean validarCalificaciones(Map<String, Double> calificaciones);
}
