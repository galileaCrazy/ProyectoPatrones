package com.edulearn.patterns.estructural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente base del patrón Composite
 * Define la interfaz común para todos los elementos del árbol del curso
 */
public abstract class ComponenteCurso {
    protected Long id;
    protected String nombre;
    protected String tipo;
    protected String descripcion;
    protected Integer orden;
    protected ComponenteCurso padre;

    public ComponenteCurso(Long id, String nombre, String tipo, String descripcion, Integer orden) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    // Operaciones comunes
    public abstract String obtenerInformacion();
    public abstract Integer calcularDuracionTotal();
    public abstract List<ComponenteCurso> obtenerHijos();
    public abstract boolean esHoja();

    // Operaciones para composite (por defecto lanzan excepción)
    public void agregar(ComponenteCurso componente) {
        throw new UnsupportedOperationException("Operación no soportada");
    }

    public void remover(ComponenteCurso componente) {
        throw new UnsupportedOperationException("Operación no soportada");
    }

    public ComponenteCurso obtenerHijo(int indice) {
        throw new UnsupportedOperationException("Operación no soportada");
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public ComponenteCurso getPadre() {
        return padre;
    }

    public void setPadre(ComponenteCurso padre) {
        this.padre = padre;
    }
}
