package com.edulearn.patterns.structural.composite;

/**
 * Leaf (Composite Pattern).
 * Representa un elemento hoja en la estructura (tema/lección individual).
 * No puede tener hijos.
 */
public class Tema implements ComponenteModulo {
    private String nombre;
    private String descripcion;
    private int duracionHoras;

    public Tema(String nombre, String descripcion, int duracionHoras) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionHoras = duracionHoras;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public int calcularDuracionTotal() {
        return duracionHoras;
    }

    @Override
    public String renderizar(int nivel) {
        String indentacion = "  ".repeat(nivel);
        return String.format("%s📄 Tema: %s (%dh)", indentacion, nombre, duracionHoras);
    }

    @Override
    public String obtenerInfo() {
        return String.format("{ tipo: 'Tema', nombre: '%s', duracion: %dh }", nombre, duracionHoras);
    }

    @Override
    public boolean esHoja() {
        return true;
    }
}
