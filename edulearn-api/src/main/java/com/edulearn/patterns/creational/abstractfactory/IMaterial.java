package com.edulearn.patterns.creational.abstractfactory;

import com.edulearn.model.Material;

/**
 * Producto Abstracto: Interfaz para Material
 * Define el contrato que deben cumplir todos los tipos de materiales
 * Patrón: Abstract Factory
 */
public interface IMaterial {
    /**
     * Obtiene el nombre del material
     */
    String getNombre();

    /**
     * Obtiene la descripción del material
     */
    String getDescripcion();

    /**
     * Obtiene el tipo de material (VIDEO, PDF, DOCUMENTO)
     */
    String getTipoMaterial();

    /**
     * Indica si requiere visualización obligatoria
     */
    boolean requiereVisualizacion();

    /**
     * Obtiene la duración en segundos (si aplica)
     */
    Integer getDuracionSegundos();

    /**
     * Muestra la información del material
     */
    void mostrarInfo();

    /**
     * Convierte el producto abstracto a entidad JPA para persistencia
     * @return Material entidad JPA
     */
    Material toEntity();
}
