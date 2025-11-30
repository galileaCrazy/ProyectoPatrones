package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Documento Básico
 */
public class DocumentoBasico implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(DocumentoBasico.class.getName());

    @Override
    public String getTipo() {
        return "DOCUMENTO";
    }

    @Override
    public String getNivel() {
        return "BASICO";
    }

    @Override
    public String getDescripcion() {
        return "Documento con teoría básica, definiciones y glosario";
    }

    @Override
    public int getDuracionEstimada() {
        return 10; // 10 minutos de lectura
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[DOCUMENTO BÁSICO] Lectura: %d min - Páginas: 5 - Imágenes: SÍ - Ejemplos: Básicos",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
