package com.edulearn.patterns.estructural.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Composite - Representa un módulo que puede contener submódulos y elementos hojas
 * Implementa el patrón Composite para manejar la estructura jerárquica
 */
public class ModuloCompuesto extends ComponenteCurso {
    private List<ComponenteCurso> hijos;
    private Integer duracionEstimada;
    private String estado;

    public ModuloCompuesto(Long id, String nombre, String tipo, String descripcion, Integer orden,
                          Integer duracionEstimada, String estado) {
        super(id, nombre, tipo, descripcion, orden);
        this.hijos = new ArrayList<>();
        this.duracionEstimada = duracionEstimada;
        this.estado = estado;
    }

    @Override
    public void agregar(ComponenteCurso componente) {
        componente.setPadre(this);
        hijos.add(componente);
        // Mantener ordenados por el campo orden
        hijos.sort((c1, c2) -> c1.getOrden().compareTo(c2.getOrden()));
    }

    @Override
    public void remover(ComponenteCurso componente) {
        hijos.remove(componente);
        componente.setPadre(null);
    }

    @Override
    public ComponenteCurso obtenerHijo(int indice) {
        if (indice >= 0 && indice < hijos.size()) {
            return hijos.get(indice);
        }
        return null;
    }

    @Override
    public List<ComponenteCurso> obtenerHijos() {
        return new ArrayList<>(hijos);
    }

    @Override
    public boolean esHoja() {
        return false;
    }

    @Override
    public String obtenerInformacion() {
        StringBuilder info = new StringBuilder();
        info.append("Módulo: ").append(nombre)
            .append(" (").append(tipo).append(")")
            .append(" - Estado: ").append(estado)
            .append(" - Duración: ").append(calcularDuracionTotal()).append(" min")
            .append(" - Submódulos/Contenidos: ").append(hijos.size());

        if (descripcion != null && !descripcion.isEmpty()) {
            info.append("\n  Descripción: ").append(descripcion);
        }

        return info.toString();
    }

    @Override
    public Integer calcularDuracionTotal() {
        // Si no hay hijos, usar la duración estimada del módulo
        if (hijos.isEmpty()) {
            return duracionEstimada != null ? duracionEstimada : 0;
        }

        // Sumar la duración de todos los hijos
        int duracionHijos = hijos.stream()
            .mapToInt(ComponenteCurso::calcularDuracionTotal)
            .sum();

        // Devolver el mayor entre la duración estimada y la suma de los hijos
        return Math.max(duracionEstimada != null ? duracionEstimada : 0, duracionHijos);
    }

    public int contarElementos() {
        return hijos.size() + hijos.stream()
            .filter(h -> !h.esHoja())
            .mapToInt(h -> ((ModuloCompuesto) h).contarElementos())
            .sum();
    }

    public List<ComponenteCurso> obtenerTodosLosElementos() {
        List<ComponenteCurso> elementos = new ArrayList<>();
        elementos.add(this);

        for (ComponenteCurso hijo : hijos) {
            if (hijo.esHoja()) {
                elementos.add(hijo);
            } else {
                elementos.addAll(((ModuloCompuesto) hijo).obtenerTodosLosElementos());
            }
        }

        return elementos;
    }

    // Getters adicionales
    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
