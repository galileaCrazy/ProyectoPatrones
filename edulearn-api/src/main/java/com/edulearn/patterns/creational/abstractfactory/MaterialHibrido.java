package com.edulearn.patterns.creational.abstractfactory;

import com.edulearn.model.Material;
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

    @Override
    public Material toEntity() {
        Material material = new Material();
        material.setNombre(nombre);  // ✅ FIX: Establecer el campo 'nombre' (NOT NULL en BD)
        material.setTitulo(nombre);
        material.setDescripcion(descripcion);
        material.setTipo(Material.TipoMaterial.documento);  // ✅ FIX: Establecer el enum 'tipo' (NOT NULL en BD)
        material.setTipoMaterial(getTipoMaterial());
        material.setRequiereVisualizacion(requiereVisualizacion());
        material.setDuracionSegundos(getDuracionSegundos());
        material.setEstado("activo");
        return material;
    }
}
