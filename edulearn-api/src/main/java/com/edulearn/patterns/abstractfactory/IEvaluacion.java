package com.edulearn.patterns.abstractfactory;

import java.math.BigDecimal;

/**
 * Producto Abstracto: Interfaz para Evaluacion
 * Define el contrato que deben cumplir todos los tipos de evaluaciones
 * Patrón: Abstract Factory
 */
public interface IEvaluacion {
    /**
     * Obtiene el nombre de la evaluación
     */
    String getNombre();

    /**
     * Obtiene la descripción de la evaluación
     */
    String getDescripcion();

    /**
     * Obtiene el tipo de evaluación (QUIZ, EXAMEN, PROYECTO)
     */
    String getTipoEvaluacion();

    /**
     * Obtiene el puntaje máximo
     */
    BigDecimal getPuntajeMaximo();

    /**
     * Obtiene los intentos permitidos
     */
    int getIntentosPermitidos();

    /**
     * Obtiene el tiempo límite en minutos
     */
    Integer getTiempoLimiteMinutos();

    /**
     * Obtiene el peso porcentual
     */
    BigDecimal getPesoPorcentual();

    /**
     * Muestra la información de la evaluación
     */
    void mostrarInfo();
}
