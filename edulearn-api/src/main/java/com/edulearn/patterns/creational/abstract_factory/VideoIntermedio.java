package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Video Intermedio
 */
public class VideoIntermedio implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(VideoIntermedio.class.getName());

    @Override
    public String getTipo() {
        return "VIDEO";
    }

    @Override
    public String getNivel() {
        return "INTERMEDIO";
    }

    @Override
    public String getDescripcion() {
        return "Video educativo con ejemplos prácticos y casos de uso reales";
    }

    @Override
    public int getDuracionEstimada() {
        return 25; // 25 minutos
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[VIDEO INTERMEDIO] Duración: %d min - Subtítulos: SÍ - Velocidad: 1.5x - Ejercicios: SÍ",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
