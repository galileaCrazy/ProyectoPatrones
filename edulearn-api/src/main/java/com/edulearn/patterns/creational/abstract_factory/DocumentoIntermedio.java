package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Documento Intermedio
 */
public class DocumentoIntermedio implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(DocumentoIntermedio.class.getName());

    @Override
    public String getTipo() {
        return "DOCUMENTO";
    }

    @Override
    public String getNivel() {
        return "INTERMEDIO";
    }

    @Override
    public String getDescripcion() {
        return "Documento con casos de estudio, diagramas UML y ejercicios prácticos";
    }

    @Override
    public int getDuracionEstimada() {
        return 20; // 20 minutos de lectura
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[DOCUMENTO INTERMEDIO] Lectura: %d min - Páginas: 12 - Diagramas: SÍ - Casos de Estudio: SÍ",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
