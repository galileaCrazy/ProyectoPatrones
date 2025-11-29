package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

/**
 * Producto Concreto: Evaluacion Híbrida
 * Implementación específica de IEvaluacion para cursos híbridos
 * Patrón: Abstract Factory
 */
public class EvaluacionHibrida implements IEvaluacion {
    private static final Logger logger = LoggerFactory.getLogger(EvaluacionHibrida.class);

    private String nombre;
    private String descripcion;

    public EvaluacionHibrida(String nombre, String descripcion) {
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
        return "PROYECTO";
    }

    @Override
    public BigDecimal getPuntajeMaximo() {
        return new BigDecimal("100.00");
    }

    @Override
    public int getIntentosPermitidos() {
        return 2; // Dos entregas permitidas
    }

    @Override
    public Integer getTiempoLimiteMinutos() {
        return null; // Sin límite de tiempo
    }

    @Override
    public BigDecimal getPesoPorcentual() {
        return new BigDecimal("50.00");
    }

    @Override
    public void mostrarInfo() {
        logger.info("  └─ Evaluación: {}", nombre);
        logger.info("     Tipo: {}", getTipoEvaluacion());
        logger.info("     Intentos: {}", getIntentosPermitidos());
        logger.info("     Tiempo límite: Sin límite");
        logger.info("     Peso: {}%", getPesoPorcentual());
    }
}
