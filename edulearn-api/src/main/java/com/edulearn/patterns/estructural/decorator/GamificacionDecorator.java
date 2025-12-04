package com.edulearn.patterns.estructural.decorator;

/**
 * Decorator Concreto A (DecoradorConcreto)
 * Añade funcionalidades de gamificación al módulo educativo.
 */
public class GamificacionDecorator extends ModuloDecorator {
    private int puntos;
    private String badge;

    public GamificacionDecorator(ModuloEducativo moduloEducativo, int puntos, String badge) {
        super(moduloEducativo);
        this.puntos = puntos;
        this.badge = badge;
    }

    @Override
    public String mostrarContenido() {
        // Invoca la operación del componente base
        String contenidoBase = super.mostrarContenido();

        // Añade la funcionalidad de gamificación
        return contenidoBase + "\n" + añadirGamificacion();
    }

    private String añadirGamificacion() {
        return "\n--- GAMIFICACIÓN ---\n" +
               "Puntos por completar: " + puntos + " pts\n" +
               "Badge disponible: " + badge;
    }

    public int getPuntos() {
        return puntos;
    }

    public String getBadge() {
        return badge;
    }
}
