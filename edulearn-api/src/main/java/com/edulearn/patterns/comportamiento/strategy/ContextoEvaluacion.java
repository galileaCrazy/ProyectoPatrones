package com.edulearn.patterns.comportamiento.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Strategy Pattern - Contexto
 *
 * Esta clase mantiene una referencia a una estrategia de evaluación y delega
 * el cálculo de calificaciones a la estrategia actual. Permite cambiar
 * dinámicamente la estrategia de evaluación en tiempo de ejecución.
 *
 * Patrón: Strategy (Contexto)
 * Responsabilidad: Mantener y ejecutar la estrategia actual
 */
public class ContextoEvaluacion {

    private static final Logger logger = LoggerFactory.getLogger(ContextoEvaluacion.class);

    private EstrategiaEvaluacion estrategia;

    /**
     * Constructor que inicializa el contexto con una estrategia
     *
     * @param estrategia Estrategia de evaluación a utilizar
     */
    public ContextoEvaluacion(EstrategiaEvaluacion estrategia) {
        this.estrategia = estrategia;
        logger.info("🎯 Contexto de evaluación creado con estrategia: {}", estrategia.getNombreEstrategia());
    }

    /**
     * Constructor por defecto (sin estrategia inicial)
     */
    public ContextoEvaluacion() {
        logger.info("🎯 Contexto de evaluación creado sin estrategia");
    }

    /**
     * Cambia la estrategia de evaluación en tiempo de ejecución
     *
     * @param estrategia Nueva estrategia a utilizar
     */
    public void setEstrategia(EstrategiaEvaluacion estrategia) {
        this.estrategia = estrategia;
        logger.info("🔄 Estrategia cambiada a: {}", estrategia.getNombreEstrategia());
    }

    /**
     * Obtiene la estrategia actual
     *
     * @return Estrategia de evaluación actual
     */
    public EstrategiaEvaluacion getEstrategia() {
        return this.estrategia;
    }

    /**
     * Ejecuta el cálculo de calificación usando la estrategia actual
     *
     * @param calificaciones Mapa con las calificaciones del estudiante
     * @return Calificación final calculada por la estrategia
     * @throws IllegalStateException si no hay estrategia configurada
     */
    public double ejecutarEvaluacion(Map<String, Double> calificaciones) {
        if (estrategia == null) {
            logger.error("❌ No hay estrategia configurada");
            throw new IllegalStateException("No se ha configurado una estrategia de evaluación");
        }

        logger.info("🎯 Ejecutando evaluación con estrategia: {}", estrategia.getNombreEstrategia());

        // Validar calificaciones antes de calcular
        if (!estrategia.validarCalificaciones(calificaciones)) {
            logger.error("❌ Las calificaciones no son válidas para la estrategia: {}", estrategia.getNombreEstrategia());
            throw new IllegalArgumentException("Calificaciones inválidas para la estrategia seleccionada");
        }

        // Delegar el cálculo a la estrategia
        return estrategia.calcularCalificacionFinal(calificaciones);
    }

    /**
     * Obtiene información sobre la estrategia actual
     *
     * @return Descripción de la estrategia
     */
    public String obtenerInformacionEstrategia() {
        if (estrategia == null) {
            return "No hay estrategia configurada";
        }
        return String.format("Estrategia: %s - %s",
                estrategia.getNombreEstrategia(),
                estrategia.getDescripcion());
    }
}
