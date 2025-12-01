package com.edulearn.controller;

import com.edulearn.service.EstrategiaEvaluacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para gestionar las estrategias de evaluación (Patrón Strategy)
 */
@RestController
@RequestMapping("/api/estrategias-evaluacion")
@CrossOrigin(origins = "*")
public class EstrategiaEvaluacionController {

    private static final Logger logger = LoggerFactory.getLogger(EstrategiaEvaluacionController.class);

    @Autowired
    private EstrategiaEvaluacionService estrategiaService;

    /**
     * Endpoint para obtener todas las estrategias de evaluación disponibles
     *
     * @return Lista de estrategias con su información
     */
    @GetMapping("/disponibles")
    public ResponseEntity<Map<String, Object>> obtenerEstrategiasDisponibles() {
        logger.info("📋 Solicitando estrategias de evaluación disponibles");

        try {
            Map<String, String> estrategias = estrategiaService.obtenerEstrategiasDisponibles();

            // Formatear respuesta para el frontend
            List<Map<String, String>> estrategiasList = new ArrayList<>();

            estrategiasList.add(Map.of(
                "id", "PONDERADA",
                "nombre", "Evaluación Ponderada",
                "descripcion", estrategias.get("PONDERADA")
            ));

            estrategiasList.add(Map.of(
                "id", "SIMPLE",
                "nombre", "Promedio Simple",
                "descripcion", estrategias.get("SIMPLE")
            ));

            estrategiasList.add(Map.of(
                "id", "COMPETENCIAS",
                "nombre", "Evaluación por Competencias",
                "descripcion", estrategias.get("COMPETENCIAS")
            ));

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            resultado.put("estrategias", estrategiasList);
            resultado.put("total", estrategiasList.size());

            logger.info("✅ Se encontraron {} estrategias disponibles", estrategiasList.size());
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            logger.error("❌ Error al obtener estrategias: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al obtener estrategias: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para validar si una estrategia es válida
     *
     * @param nombreEstrategia Nombre de la estrategia a validar
     * @return Resultado de la validación
     */
    @GetMapping("/validar/{nombreEstrategia}")
    public ResponseEntity<Map<String, Object>> validarEstrategia(@PathVariable String nombreEstrategia) {
        logger.info("🔍 Validando estrategia: {}", nombreEstrategia);

        try {
            boolean esValida = estrategiaService.esEstrategiaValida(nombreEstrategia);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            resultado.put("estrategia", nombreEstrategia);
            resultado.put("valida", esValida);

            if (esValida) {
                logger.info("✅ Estrategia válida: {}", nombreEstrategia);
            } else {
                logger.warn("⚠️ Estrategia no válida: {}", nombreEstrategia);
            }

            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            logger.error("❌ Error al validar estrategia: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al validar estrategia: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint de prueba para calcular una calificación usando una estrategia específica
     *
     * @param request Solicitud con la estrategia y calificaciones
     * @return Calificación calculada
     */
    @PostMapping("/calcular")
    public ResponseEntity<Map<String, Object>> calcularCalificacion(@RequestBody Map<String, Object> request) {
        logger.info("🎯 Solicitud de cálculo de calificación");

        try {
            String estrategia = (String) request.get("estrategia");
            @SuppressWarnings("unchecked")
            Map<String, Double> calificaciones = (Map<String, Double>) request.get("calificaciones");

            if (estrategia == null || calificaciones == null) {
                logger.error("❌ Datos incompletos en la solicitud");
                Map<String, Object> error = new HashMap<>();
                error.put("exito", false);
                error.put("mensaje", "Debe proporcionar 'estrategia' y 'calificaciones'");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            double resultado = estrategiaService.calcularConEstrategia(estrategia, calificaciones);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("exito", true);
            respuesta.put("estrategia", estrategia);
            respuesta.put("calificacionFinal", resultado);
            respuesta.put("calificaciones", calificaciones);

            logger.info("✅ Calificación calculada: {}", resultado);
            return ResponseEntity.ok(respuesta);

        } catch (IllegalArgumentException e) {
            logger.error("❌ Error de validación: {}", e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

        } catch (Exception e) {
            logger.error("❌ Error al calcular calificación: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al calcular calificación: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
