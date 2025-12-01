package com.edulearn.service;

import com.edulearn.patterns.comportamiento.strategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para gestionar las estrategias de evaluación
 *
 * Este servicio actúa como una fábrica y gestor de estrategias,
 * permitiendo obtener la estrategia correcta según el nombre y
 * proporcionar información sobre las estrategias disponibles.
 */
@Service
public class EstrategiaEvaluacionService {

    private static final Logger logger = LoggerFactory.getLogger(EstrategiaEvaluacionService.class);

    @Autowired
    private EvaluacionPonderada evaluacionPonderada;

    @Autowired
    private PromedioSimple promedioSimple;

    @Autowired
    private EvaluacionPorCompetencias evaluacionPorCompetencias;

    /**
     * Obtiene una estrategia de evaluación por su nombre
     *
     * @param nombreEstrategia Nombre de la estrategia a obtener
     * @return Instancia de la estrategia solicitada
     * @throws IllegalArgumentException si la estrategia no existe
     */
    public EstrategiaEvaluacion obtenerEstrategia(String nombreEstrategia) {
        logger.info("🔍 Buscando estrategia: {}", nombreEstrategia);

        if (nombreEstrategia == null || nombreEstrategia.trim().isEmpty()) {
            logger.error("❌ Nombre de estrategia vacío o nulo");
            throw new IllegalArgumentException("El nombre de la estrategia no puede estar vacío");
        }

        switch (nombreEstrategia.toUpperCase().trim()) {
            case "EVALUACION_PONDERADA":
            case "PONDERADA":
                logger.info("✅ Estrategia encontrada: Evaluación Ponderada");
                return evaluacionPonderada;

            case "PROMEDIO_SIMPLE":
            case "SIMPLE":
                logger.info("✅ Estrategia encontrada: Promedio Simple");
                return promedioSimple;

            case "EVALUACION_POR_COMPETENCIAS":
            case "COMPETENCIAS":
                logger.info("✅ Estrategia encontrada: Evaluación por Competencias");
                return evaluacionPorCompetencias;

            default:
                logger.error("❌ Estrategia no encontrada: {}", nombreEstrategia);
                throw new IllegalArgumentException("Estrategia de evaluación no válida: " + nombreEstrategia);
        }
    }

    /**
     * Obtiene la lista de todas las estrategias disponibles con su información
     *
     * @return Mapa con nombre y descripción de cada estrategia
     */
    public Map<String, String> obtenerEstrategiasDisponibles() {
        logger.info("📋 Listando estrategias disponibles");

        Map<String, String> estrategias = new HashMap<>();
        estrategias.put("PONDERADA", evaluacionPonderada.getDescripcion());
        estrategias.put("SIMPLE", promedioSimple.getDescripcion());
        estrategias.put("COMPETENCIAS", evaluacionPorCompetencias.getDescripcion());

        logger.info("✅ Se encontraron {} estrategias disponibles", estrategias.size());
        return estrategias;
    }

    /**
     * Calcula una calificación usando la estrategia especificada
     *
     * @param nombreEstrategia Nombre de la estrategia a usar
     * @param calificaciones Mapa con las calificaciones del estudiante
     * @return Calificación final calculada
     */
    public double calcularConEstrategia(String nombreEstrategia, Map<String, Double> calificaciones) {
        logger.info("🎯 Calculando calificación con estrategia: {}", nombreEstrategia);

        EstrategiaEvaluacion estrategia = obtenerEstrategia(nombreEstrategia);
        ContextoEvaluacion contexto = new ContextoEvaluacion(estrategia);

        double resultado = contexto.ejecutarEvaluacion(calificaciones);

        logger.info("✅ Calificación calculada: {}", resultado);
        return resultado;
    }

    /**
     * Valida si un nombre de estrategia es válido
     *
     * @param nombreEstrategia Nombre a validar
     * @return true si la estrategia existe, false en caso contrario
     */
    public boolean esEstrategiaValida(String nombreEstrategia) {
        try {
            obtenerEstrategia(nombreEstrategia);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
