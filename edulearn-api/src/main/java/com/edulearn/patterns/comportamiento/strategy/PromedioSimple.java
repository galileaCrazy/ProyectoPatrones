package com.edulearn.patterns.comportamiento.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Strategy Pattern - Estrategia de Promedio Simple
 *
 * Esta estrategia calcula la calificación final como el promedio aritmético
 * simple de todas las evaluaciones, sin aplicar pesos diferentes.
 * Todas las evaluaciones tienen el mismo valor.
 *
 * Fórmula: (Tareas + Exámenes + Proyecto) / 3
 *
 * Patrón: Strategy (Estrategia Concreta)
 * Caso de uso: Cursos donde todas las evaluaciones tienen la misma importancia
 */
@Component
public class PromedioSimple implements EstrategiaEvaluacion {

    private static final Logger logger = LoggerFactory.getLogger(PromedioSimple.class);

    @Override
    public double calcularCalificacionFinal(Map<String, Double> calificaciones) {
        logger.info("📊 Calculando calificación con Promedio Simple");

        if (calificaciones == null || calificaciones.isEmpty()) {
            logger.warn("⚠️ No hay calificaciones para calcular");
            return 0.0;
        }

        double suma = 0.0;
        int count = 0;

        for (Map.Entry<String, Double> entry : calificaciones.entrySet()) {
            double valor = entry.getValue();
            suma += valor;
            count++;
            logger.info("📝 {}: {}", entry.getKey(), valor);
        }

        double promedio = count > 0 ? suma / count : 0.0;

        logger.info("✅ Promedio simple: {} (suma: {}, cantidad: {})", promedio, suma, count);

        return Math.round(promedio * 100.0) / 100.0; // Redondear a 2 decimales
    }

    @Override
    public String getNombreEstrategia() {
        return "Promedio Simple";
    }

    @Override
    public String getDescripcion() {
        return "Calcula el promedio aritmético de todas las evaluaciones sin aplicar pesos";
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

        logger.info("✅ Calificaciones válidas para Promedio Simple");
        return true;
    }
}
