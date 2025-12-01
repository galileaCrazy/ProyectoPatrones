package com.edulearn.patterns.structural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite (Composite Pattern).
 * Representa un contenedor que puede tener hijos (módulos o temas).
 * Implementa las operaciones de manera que se propaguen a sus hijos.
 */
public class Modulo implements ComponenteModulo {
    private String nombre;
    private String descripcion;
    private List<ComponenteModulo> hijos;

    public Modulo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.hijos = new ArrayList<>();
    }

    /**
     * Agregar un componente hijo (módulo o tema)
     */
    public void agregar(ComponenteModulo componente) {
        hijos.add(componente);
    }

    /**
     * Eliminar un componente hijo
     */
    public void eliminar(ComponenteModulo componente) {
        hijos.remove(componente);
    }

    /**
     * Obtener todos los hijos
     */
    public List<ComponenteModulo> obtenerHijos() {
        return new ArrayList<>(hijos);
    }

    /**
     * Obtener hijo por índice
     */
    public ComponenteModulo obtenerHijo(int indice) {
        if (indice >= 0 && indice < hijos.size()) {
            return hijos.get(indice);
        }
        return null;
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
        int total = 0;
        for (ComponenteModulo hijo : hijos) {
            total += hijo.calcularDuracionTotal();
        }
        return total;
    }

    @Override
    public String renderizar(int nivel) {
        StringBuilder sb = new StringBuilder();
        String indentacion = "  ".repeat(nivel);

        sb.append(String.format("%s📁 Módulo: %s (Total: %dh)\n",
            indentacion, nombre, calcularDuracionTotal()));

        for (ComponenteModulo hijo : hijos) {
            sb.append(hijo.renderizar(nivel + 1)).append("\n");
        }

        return sb.toString().trim();
    }

    @Override
    public String obtenerInfo() {
        return String.format("{ tipo: 'Modulo', nombre: '%s', hijos: %d, duracionTotal: %dh }",
            nombre, hijos.size(), calcularDuracionTotal());
    }

    @Override
    public boolean esHoja() {
        return false;
    }

    /**
     * Cuenta el total de componentes en el árbol
     */
    public int contarComponentes() {
        int total = 1; // Este módulo
        for (ComponenteModulo hijo : hijos) {
            if (hijo instanceof Modulo) {
                total += ((Modulo) hijo).contarComponentes();
            } else {
                total++; // Tema (hoja)
            }
        }
        return total;
    }

    /**
     * Obtiene todos los temas (hojas) del árbol
     */
    public List<Tema> obtenerTodosTemas() {
        List<Tema> temas = new ArrayList<>();
        for (ComponenteModulo hijo : hijos) {
            if (hijo instanceof Tema) {
                temas.add((Tema) hijo);
            } else if (hijo instanceof Modulo) {
                temas.addAll(((Modulo) hijo).obtenerTodosTemas());
            }
        }
        return temas;
    }
}
