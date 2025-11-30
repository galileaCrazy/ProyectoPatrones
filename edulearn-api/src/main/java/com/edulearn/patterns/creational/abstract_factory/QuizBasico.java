package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Quiz Básico
 */
public class QuizBasico implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(QuizBasico.class.getName());

    @Override
    public String getTipo() {
        return "QUIZ";
    }

    @Override
    public String getNivel() {
        return "BASICO";
    }

    @Override
    public String getDescripcion() {
        return "Quiz con preguntas de opción múltiple sobre conceptos básicos";
    }

    @Override
    public int getDuracionEstimada() {
        return 10; // 10 minutos
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[QUIZ BÁSICO] Duración: %d min - Preguntas: 10 - Tipo: Opción múltiple - Reintentos: Ilimitados",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
