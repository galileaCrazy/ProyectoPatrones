package com.edulearn.patterns.abstractfactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producto Concreto: Material Virtual
 * Implementación específica de IMaterial para cursos virtuales
 * Patrón: Abstract Factory
 */
public class MaterialVirtual implements IMaterial {
    private static final Logger logger = LoggerFactory.getLogger(MaterialVirtual.class);

    private String nombre;
    private String descripcion;

    public MaterialVirtual(String nombre, String descripcion) {
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
        return "VIDEO";
    }

    @Override
    public boolean requiereVisualizacion() {
        return true; // Videos requieren visualización completa
    }

    @Override
    public Integer getDuracionSegundos() {
        return 1800; // 30 minutos por defecto
    }

    @Override
    public void mostrarInfo() {
        logger.info("  ├─ Material: {}", nombre);
        logger.info("  │  Tipo: {}", getTipoMaterial());
        logger.info("  │  Duración: {} minutos", (getDuracionSegundos() / 60));
        logger.info("  │  Visualización obligatoria: Sí");
    }
}
