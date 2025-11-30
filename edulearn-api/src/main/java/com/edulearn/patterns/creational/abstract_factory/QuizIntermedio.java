package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Quiz Intermedio
 */
public class QuizIntermedio implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(QuizIntermedio.class.getName());

    @Override
    public String getTipo() {
        return "QUIZ";
    }

    @Override
    public String getNivel() {
        return "INTERMEDIO";
    }

    @Override
    public String getDescripcion() {
        return "Quiz con preguntas mixtas y análisis de casos prácticos";
    }

    @Override
    public int getDuracionEstimada() {
        return 20; // 20 minutos
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[QUIZ INTERMEDIO] Duración: %d min - Preguntas: 15 - Tipo: Mixto - Reintentos: 3",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
