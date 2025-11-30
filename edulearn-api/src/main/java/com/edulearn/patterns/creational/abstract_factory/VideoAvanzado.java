package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Video Avanzado
 */
public class VideoAvanzado implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(VideoAvanzado.class.getName());

    @Override
    public String getTipo() {
        return "VIDEO";
    }

    @Override
    public String getNivel() {
        return "AVANZADO";
    }

    @Override
    public String getDescripcion() {
        return "Video educativo con temas complejos, arquitecturas y patrones avanzados";
    }

    @Override
    public int getDuracionEstimada() {
        return 40; // 40 minutos
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[VIDEO AVANZADO] Duración: %d min - Subtítulos: SÍ - Velocidad: 2x - Ejercicios: SÍ - Proyecto Final: SÍ",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
