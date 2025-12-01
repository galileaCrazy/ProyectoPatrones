package com.edulearn.patterns.structural.bridge;

import java.util.Map;

/**
 * Abstraction (Bridge Pattern).
 * Clase abstracta que define la interfaz para los reportes.
 * Mantiene una referencia a un objeto IFormatoReporte (implementación).
 * Este es el "puente" que separa la abstracción de la implementación.
 */
public abstract class Reporte {
    protected IFormatoReporte formato;

    public Reporte(IFormatoReporte formato) {
        this.formato = formato;
    }

    /**
     * Método template que usa el formato para generar el reporte
     */
    public String generar() {
        String titulo = obtenerTitulo();
        Map<String, Object> datos = obtenerDatos();

        StringBuilder reporte = new StringBuilder();
        reporte.append(formato.generarEncabezado(titulo));
        reporte.append(formato.generarCuerpo(datos));
        reporte.append(formato.generarPie());

        return reporte.toString();
    }

    /**
     * Métodos abstractos que las subclases deben implementar
     */
    protected abstract String obtenerTitulo();
    protected abstract Map<String, Object> obtenerDatos();

    /**
     * Permite cambiar el formato en tiempo de ejecución
     */
    public void setFormato(IFormatoReporte formato) {
        this.formato = formato;
    }

    public String obtenerExtension() {
        return formato.obtenerExtension();
    }

    public String obtenerTipoMIME() {
        return formato.obtenerTipoMIME();
    }
}
