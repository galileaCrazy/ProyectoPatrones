package com.edulearn.patterns.comportamiento.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Strategy Pattern - Estrategia de Evaluación Ponderada
 *
 * Esta estrategia calcula la calificación final aplicando pesos específicos
 * a cada componente de evaluación:
 * - Tareas: 30%
 * - Exámenes: 50%
 * - Proyecto Final: 20%
 *
 * Patrón: Strategy (Estrategia Concreta)
 * Caso de uso: Cursos donde se quiere dar más peso a exámenes
 */
@Component
public class EvaluacionPonderada implements EstrategiaEvaluacion {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionPonderada.class);

    // Pesos de cada componente (deben sumar 1.0 = 100%)
    private static final double PESO_TAREAS = 0.30;      // 30%
    private static final double PESO_EXAMENES = 0.50;    // 50%
    private static final double PESO_PROYECTO = 0.20;    // 20%

    @Override
    public double calcularCalificacionFinal(Map<String, Double> calificaciones) {
        logger.info("📊 Calculando calificación con Evaluación Ponderada");

        double tareas = calificaciones.getOrDefault("tareas", 0.0);
        double examenes = calificaciones.getOrDefault("examenes", 0.0);
        double proyecto = calificaciones.getOrDefault("proyecto", 0.0);

        double calificacionFinal = (tareas * PESO_TAREAS) +
                                   (examenes * PESO_EXAMENES) +
                                   (proyecto * PESO_PROYECTO);

        logger.info("📝 Tareas: {} ({}%)", tareas, PESO_TAREAS * 100);
        logger.info("📝 Exámenes: {} ({}%)", examenes, PESO_EXAMENES * 100);
        logger.info("📝 Proyecto: {} ({}%)", proyecto, PESO_PROYECTO * 100);
        logger.info("✅ Calificación final ponderada: {}", calificacionFinal);

        return Math.round(calificacionFinal * 100.0) / 100.0; // Redondear a 2 decimales
    }

    @Override
    public String getNombreEstrategia() {
        return "Evaluación Ponderada";
    }

    @Override
    public String getDescripcion() {
        return "Calcula la calificación aplicando pesos: Tareas 30%, Exámenes 50%, Proyecto 20%";
    }

    @Override
    public boolean validarCalificaciones(Map<String, Double> calificaciones) {
        if (calificaciones == null || calificaciones.isEmpty()) {
            logger.error("❌ Las calificaciones no pueden estar vacías");
            return false;
        }

        // Validar que todas las calificaciones estén en el rango 0-100
        for (Map.Entry<String, Double> entry : calificaciones.entrySet()) {
            Double valor = entry.getValue();
            if (valor == null || valor < 0 || valor > 100) {
                logger.error("❌ Calificación inválida para {}: {}", entry.getKey(), valor);
                return false;
            }
        }

        logger.info("✅ Calificaciones válidas para Evaluación Ponderada");
        return true;
    }
}
