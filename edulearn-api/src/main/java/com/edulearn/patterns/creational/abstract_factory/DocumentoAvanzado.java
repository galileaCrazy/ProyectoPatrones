package com.edulearn.patterns.creational.abstract_factory;

import java.util.logging.Logger;

/**
 * Producto concreto: Documento Avanzado
 */
public class DocumentoAvanzado implements IContenidoEducativo {
    private static final Logger logger = Logger.getLogger(DocumentoAvanzado.class.getName());

    @Override
    public String getTipo() {
        return "DOCUMENTO";
    }

    @Override
    public String getNivel() {
        return "AVANZADO";
    }

    @Override
    public String getDescripcion() {
        return "Documento técnico con arquitecturas empresariales, patrones complejos y best practices";
    }

    @Override
    public int getDuracionEstimada() {
        return 35; // 35 minutos de lectura
    }

    @Override
    public String renderizar() {
        String contenido = String.format(
            "[DOCUMENTO AVANZADO] Lectura: %d min - Páginas: 25 - Arquitecturas: SÍ - Research Papers: SÍ",
            getDuracionEstimada()
        );
        logger.info("Renderizando: " + contenido);
        return contenido;
    }
}
