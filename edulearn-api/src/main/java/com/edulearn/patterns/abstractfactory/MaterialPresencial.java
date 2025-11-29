package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producto Concreto: Material Presencial
 * Implementación específica de IMaterial para cursos presenciales
 * Patrón: Abstract Factory
 */
public class MaterialPresencial implements IMaterial {
    private static final Logger logger = LoggerFactory.getLogger(MaterialPresencial.class);

    private String nombre;
    private String descripcion;

    public MaterialPresencial(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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
    public String getTipoMaterial() {
        return "PDF";
    }

    @Override
    public boolean requiereVisualizacion() {
        return false; // PDFs no requieren visualización obligatoria
    }

    @Override
    public Integer getDuracionSegundos() {
        return null; // PDFs no tienen duración
    }

    @Override
    public void mostrarInfo() {
        logger.info("  ├─ Material: {}", nombre);
        logger.info("  │  Tipo: {}", getTipoMaterial());
        logger.info("  │  Visualización obligatoria: No");
    }
}
