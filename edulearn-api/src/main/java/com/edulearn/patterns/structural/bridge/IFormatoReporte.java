package com.edulearn.patterns.structural.bridge;

import java.util.Map;

/**
 * Implementor (Bridge Pattern).
 * Define la interfaz para las implementaciones concretas de formatos de reporte.
 * Esta es la parte de "implementación" del Bridge.
 */
public interface IFormatoReporte {
    /**
     * Genera el encabezado del reporte
     */
    String generarEncabezado(String titulo);

    /**
     * Genera el cuerpo del reporte con los datos
     */
    String generarCuerpo(Map<String, Object> datos);

    /**
     * Genera el pie del reporte
     */
    String generarPie();

    /**
     * Retorna el tipo MIME del formato
     */
    String obtenerTipoMIME();

    /**
     * Retorna la extensión del archivo
     */
    String obtenerExtension();
}
