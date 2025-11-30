package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Quiz Avanzado
 */
public class QuizAvanzado implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(QuizAvanzado.class.getName());

    @Override
    public String getTipo() {
        return "QUIZ";
    }

    @Override
    public String getNivel() {
        return "AVANZADO";
    }

    @Override
    public String getDescripcion() {
        return "Quiz con problemas complejos, diseño de sistemas y preguntas abiertas";
    }

    @Override
    public int getDuracionEstimada() {
        return 30; // 30 minutos
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[QUIZ AVANZADO] Duración: %d min - Preguntas: 20 - Tipo: Abiertas + Código - Reintentos: 1",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
