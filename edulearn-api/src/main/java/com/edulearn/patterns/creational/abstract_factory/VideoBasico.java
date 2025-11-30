package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Video Básico
 */
public class VideoBasico implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(VideoBasico.class.getName());

    @Override
    public String getTipo() {
        return "VIDEO";
    }

    @Override
    public String getNivel() {
        return "BASICO";
    }

    @Override
    public String getDescripcion() {
        return "Video educativo con conceptos fundamentales y explicaciones simples";
    }

    @Override
    public int getDuracionEstimada() {
        return 15; // 15 minutos
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[VIDEO BÁSICO] Duración: %d min - Subtítulos: SÍ - Velocidad: Normal - Ejercicios: NO",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
