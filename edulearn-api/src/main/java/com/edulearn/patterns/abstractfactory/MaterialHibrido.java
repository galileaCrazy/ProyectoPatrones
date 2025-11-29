package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producto Concreto: Material Híbrido
 * Implementación específica de IMaterial para cursos híbridos
 * Patrón: Abstract Factory
 */
public class MaterialHibrido implements IMaterial {
    private static final Logger logger = LoggerFactory.getLogger(MaterialHibrido.class);

    private String nombre;
    private String descripcion;

    public MaterialHibrido(String nombre, String descripcion) {
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
        return "DOCUMENTO";
    }

    @Override
    public boolean requiereVisualizacion() {
        return false;
    }

    @Override
    public Integer getDuracionSegundos() {
        return null;
    }

    @Override
    public void mostrarInfo() {
        logger.info("  ├─ Material: {}", nombre);
        logger.info("  │  Tipo: {}", getTipoMaterial());
        logger.info("  │  Visualización obligatoria: No");
    }
}
