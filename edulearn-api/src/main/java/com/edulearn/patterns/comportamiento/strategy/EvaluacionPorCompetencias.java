package com.edulearn.patterns.comportamiento.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Strategy Pattern - Estrategia de Evaluación por Competencias
 *
 * Esta estrategia calcula la calificación final basándose en el desempeño
 * en diferentes competencias o habilidades. Se enfoca en evaluar el dominio
 * de habilidades específicas más que en promedios numéricos.
 *
 * Criterios de evaluación:
 * - Si el estudiante aprueba TODAS las competencias (>= 70): Calificación final es el promedio
 * - Si falla alguna competencia (< 70): Calificación final penalizada (-10 puntos)
 * - Bonus: Si todas las competencias son >= 90: +5 puntos de bonus
 *
 * Patrón: Strategy (Estrategia Concreta)
 * Caso de uso: Cursos basados en desarrollo de habilidades específicas
 */
@Component
public class EvaluacionPorCompetencias implements EstrategiaEvaluacion {

    private static final Logger logger = LoggerFactory.getLogger(EvaluacionPorCompetencias.class);

    private static final double UMBRAL_APROBACION = 70.0;
    private static final double UMBRAL_EXCELENCIA = 90.0;
    private static final double PENALIZACION = 10.0;
    private static final double BONUS_EXCELENCIA = 5.0;

    @Override
    public double calcularCalificacionFinal(Map<String, Double> calificaciones) {
        logger.info("📊 Calculando calificación con Evaluación por Competencias");

        if (calificaciones == null || calificaciones.isEmpty()) {
            logger.warn("⚠️ No hay calificaciones para calcular");
            return 0.0;
        }

        // Calcular promedio base
        double suma = 0.0;
        int count = 0;
        boolean todasAprobadas = true;
        boolean todasExcelentes = true;

        for (Map.Entry<String, Double> entry : calificaciones.entrySet()) {
            double valor = entry.getValue();
            suma += valor;
            count++;

            logger.info("📝 Competencia {}: {}", entry.getKey(), valor);

            // Verificar si aprobó esta competencia
            if (valor < UMBRAL_APROBACION) {
                todasAprobadas = false;
                logger.warn("⚠️ Competencia {} NO aprobada (< {})", entry.getKey(), UMBRAL_APROBACION);
            }

            // Verificar si alcanzó excelencia
            if (valor < UMBRAL_EXCELENCIA) {
                todasExcelentes = false;
            }
        }

        double promedio = count > 0 ? suma / count : 0.0;
        logger.info("📊 Promedio base de competencias: {}", promedio);

        // Aplicar reglas de evaluación por competencias
        double calificacionFinal = promedio;

        if (!todasAprobadas) {
            // Penalización por no aprobar todas las competencias
            calificacionFinal -= PENALIZACION;
            logger.warn("⚠️ Penalización aplicada: -{} puntos (no todas las competencias aprobadas)", PENALIZACION);
        } else if (todasExcelentes) {
            // Bonus por excelencia en todas las competencias
            calificacionFinal += BONUS_EXCELENCIA;
            logger.info("⭐ Bonus por excelencia aplicado: +{} puntos (todas >= {})", BONUS_EXCELENCIA, UMBRAL_EXCELENCIA);
        }

        // Asegurar que la calificación esté en el rango 0-100
        calificacionFinal = Math.max(0, Math.min(100, calificacionFinal));

        logger.info("✅ Calificación final por competencias: {}", calificacionFinal);

        return Math.round(calificacionFinal * 100.0) / 100.0; // Redondear a 2 decimales
    }

    @Override
    public String getNombreEstrategia() {
        return "Evaluación por Competencias";
    }

    @Override
    public String getDescripcion() {
        return "Evalúa el dominio de competencias específicas. Penaliza si no se aprueban todas (>= 70), " +
               "otorga bonus si todas son excelentes (>= 90)";
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

        logger.info("✅ Calificaciones válidas para Evaluación por Competencias");
        return true;
    }
}
