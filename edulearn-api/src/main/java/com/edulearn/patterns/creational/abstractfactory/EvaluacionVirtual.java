package com.edulearn.patterns.creational.abstractfactory;

import com.edulearn.model.Evaluacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;

/**
 * Producto Concreto: Evaluacion Virtual
 * Implementación específica de IEvaluacion para cursos virtuales
 * Patrón: Abstract Factory
 */
public class EvaluacionVirtual implements IEvaluacion {
    private static final Logger logger = LoggerFactory.getLogger(EvaluacionVirtual.class);

    private String nombre;
    private String descripcion;

    public EvaluacionVirtual(String nombre, String descripcion) {
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
        return "QUIZ";
    }

    @Override
    public BigDecimal getPuntajeMaximo() {
        return new BigDecimal("100.00");
    }

    @Override
    public int getIntentosPermitidos() {
        return 3; // Múltiples intentos en quizzes virtuales
    }

    @Override
    public Integer getTiempoLimiteMinutos() {
        return 60; // 1 hora
    }

    @Override
    public BigDecimal getPesoPorcentual() {
        return new BigDecimal("20.00");
    }

    @Override
    public void mostrarInfo() {
        logger.info("  └─ Evaluación: {}", nombre);
        logger.info("     Tipo: {}", getTipoEvaluacion());
        logger.info("     Intentos: {}", getIntentosPermitidos());
        logger.info("     Tiempo límite: {} minutos", getTiempoLimiteMinutos());
        logger.info("     Peso: {}%", getPesoPorcentual());
    }

    @Override
    public Evaluacion toEntity() {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setNombre(nombre);  // ✅ FIX: Establecer el campo 'nombre' (NOT NULL en BD)
        evaluacion.setTitulo(nombre);
        evaluacion.setDescripcion(descripcion);
        evaluacion.setTipo(getTipoEvaluacion());  // ✅ FIX: Establecer el campo 'tipo' (NOT NULL en BD)
        evaluacion.setTipoEvaluacion(getTipoEvaluacion());
        evaluacion.setPuntajeMaximo(getPuntajeMaximo());
        evaluacion.setIntentosPermitidos(getIntentosPermitidos());
        evaluacion.setTiempoLimiteMinutos(getTiempoLimiteMinutos());
        evaluacion.setPesoPorcentual(getPesoPorcentual());
        evaluacion.setEstado("activa");
        return evaluacion;
    }
}
