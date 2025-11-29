package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

/**
 * Producto Concreto: Evaluacion Presencial
 * Implementación específica de IEvaluacion para cursos presenciales
 * Patrón: Abstract Factory
 */
public class EvaluacionPresencial implements IEvaluacion {
    private static final Logger logger = LoggerFactory.getLogger(EvaluacionPresencial.class);

    private String nombre;
    private String descripcion;

    public EvaluacionPresencial(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String getTipoEvaluacion() {
        return "EXAMEN";
    }

    @Override
    public BigDecimal getPuntajeMaximo() {
        return new BigDecimal("100.00");
    }

    @Override
    public int getIntentosPermitidos() {
        return 1; // Un solo intento en exámenes presenciales
    }

    @Override
    public Integer getTiempoLimiteMinutos() {
        return 120; // 2 horas
    }

    @Override
    public BigDecimal getPesoPorcentual() {
        return new BigDecimal("40.00");
    }

    @Override
    public void mostrarInfo() {
        logger.info("  └─ Evaluación: {}", nombre);
        logger.info("     Tipo: {}", getTipoEvaluacion());
        logger.info("     Intentos: {}", getIntentosPermitidos());
        logger.info("     Tiempo límite: {} minutos", getTiempoLimiteMinutos());
        logger.info("     Peso: {}%", getPesoPorcentual());
    }
}
