package com.edulearn.patterns.estructural.decorator;

/**
 * Clase Concreta (ComponenteConcreto)
 * Define un objeto al cual se le pueden añadir responsabilidades adicionales.
 */
public class ModuloBasico implements ModuloEducativo {
    private String nombre;
    private String contenido;

    public ModuloBasico(String nombre, String contenido) {
        this.nombre = nombre;
        this.contenido = contenido;
    }

    @Override
    public String mostrarContenido() {
        return "=== MÓDULO: " + nombre + " ===\n" + contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContenido() {
        return contenido;
    }
}
